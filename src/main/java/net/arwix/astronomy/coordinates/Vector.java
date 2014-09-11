package net.arwix.astronomy.coordinates;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public abstract class Vector {

    /**
     * Конвектор координат возвращает всегда новый объект
     *
     * @param vector вектор
     * @param toType желаемое преобразование
     * @return вектор в треуемой системе координат
     */
    static Vector convert(Vector vector, VectorType toType) {
        RectangularVector rectVector;
        SphericalVector sphericVector;
        switch (vector.getType()) {
            case SPHERICAL:
                if (toType == VectorType.SPHERICAL) return new SphericalVector(vector);
                sphericVector = (SphericalVector) vector;
                final double cosEl = Math.cos(sphericVector.theta);
                return new RectangularVector(
                        sphericVector.r * cos(sphericVector.phi) * cosEl,
                        sphericVector.r * sin(sphericVector.phi) * cosEl,
                        sphericVector.r * sin(sphericVector.theta)
                );
            case RECTANGULAR:
                if (toType == VectorType.RECTANGULAR) return new RectangularVector(vector);
                rectVector = (RectangularVector) vector;
                sphericVector = new SphericalVector();

                // Длина проекции на плоскость XY
                final double XYSqr = rectVector.x * rectVector.x + rectVector.y * rectVector.y;

                // Модуль вектора
                sphericVector.r = sqrt(XYSqr + rectVector.z * rectVector.z);

                // Азимут вектора
                if ((rectVector.x == 0.0) && (rectVector.y == 0.0))
                    sphericVector.phi = 0.0;
                else
                    sphericVector.phi = atan2(rectVector.y, rectVector.x);
                if (sphericVector.phi < 0.0) sphericVector.phi += 2.0 * Math.PI;

                // высота вектора
                final double rho = sqrt(XYSqr);
                if ((rectVector.z == 0.0) && (rho == 0.0))
                    sphericVector.theta = 0.0;
                else
                    sphericVector.theta = atan2(rectVector.z, rho);
                return sphericVector;
            default:
                throw new IllegalArgumentException();
        }
    }

    static Vector inverseConvert(Vector vector) {
        switch (vector.getType()) {
            case SPHERICAL:
                return convert(vector, VectorType.RECTANGULAR);
            case RECTANGULAR:
                return convert(vector, VectorType.SPHERICAL);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Скалярное перемножение векторов
     *
     * @param leftVector  вектор
     * @param rightVector вектор
     * @return скалярное произведение
     */
    public static double dot(Vector leftVector, Vector rightVector) {
        RectangularVector v1 = (RectangularVector) leftVector.getVectorInType(VectorType.RECTANGULAR);
        RectangularVector v2 = (RectangularVector) rightVector.getVectorInType(VectorType.RECTANGULAR);
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    /**
     * нормализатор вектора
     *
     * @param vector вектор для нормализации
     * @return модуль вектора
     */
    public static double norm(Vector vector) {
        return sqrt(dot(vector, vector));
    }

    /**
     * Сложение координат
     *
     * @param leftVector  вектор
     * @param rightVector вектор
     * @return новый вектор
     */
    public static Vector add(Vector leftVector, Vector rightVector) {
        RectangularVector v1 = (RectangularVector) leftVector.getVectorInType(VectorType.RECTANGULAR);
        RectangularVector v2 = (RectangularVector) rightVector.getVectorInType(VectorType.RECTANGULAR);
        return new RectangularVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    /**
     * Вычитание координат
     *
     * @param leftVector  вектор
     * @param rightVector вектор
     * @return новый вектор
     */
    public static Vector substract(Vector leftVector, Vector rightVector) {
        RectangularVector v1 = (RectangularVector) leftVector.getVectorInType(VectorType.RECTANGULAR);
        RectangularVector v2 = (RectangularVector) rightVector.getVectorInType(VectorType.RECTANGULAR);
        return new RectangularVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     * Скалярное умножение
     *
     * @param vector  вектор
     * @param fScalar множитель
     * @return новый вектор
     */
    public static Vector scalarMultiply(Vector vector, double fScalar) {
        RectangularVector v = (RectangularVector) vector.getVectorInType(VectorType.RECTANGULAR);
        return new RectangularVector(v.x * fScalar, v.y * fScalar, v.z * fScalar);
    }

    /**
     * Скалярное деление
     *
     * @param vector  вектор
     * @param fScalar делитель
     * @return новый вектор
     */
    public static Vector scalarDivide(Vector vector, double fScalar) {
        RectangularVector v = (RectangularVector) vector.getVectorInType(VectorType.RECTANGULAR);
        return new RectangularVector(v.x / fScalar, v.y / fScalar, v.z / fScalar);
    }

    /**
     * Унарный минус
     *
     * @param vector исходный вектор
     * @return новый вектор
     */
    public static Vector unaryMinus(Vector vector) {
        RectangularVector v = (RectangularVector) vector.getVectorInType(VectorType.RECTANGULAR);
        return new RectangularVector(-v.x, -v.y, -v.z);
    }

    /**
     * Векторное произведение
     *
     * @param leftVector  вектор
     * @param rightVector вектор
     * @return новый объект
     */
    public static Vector multiply(Vector leftVector, Vector rightVector) {
        RectangularVector v1 = (RectangularVector) leftVector.getVectorInType(VectorType.RECTANGULAR);
        RectangularVector v2 = (RectangularVector) rightVector.getVectorInType(VectorType.RECTANGULAR);

        RectangularVector r = new RectangularVector();
        r.x = v1.y * v2.z - v1.z * v2.y;
        r.y = v1.z * v2.x - v1.x * v2.z;
        r.z = v1.x * v2.y - v1.y * v2.x;
        return r;
    }

    public abstract VectorType getType();

    public Vector getVectorInType(VectorType type) {
        return this.getType() == type ? this : convert(this, type);
    }

    /**
     * Установка координат текущего вектора
     *
     * @param vector исходник координат
     */
    public abstract void set(Vector vector);

    /**
     * Скалярное перемножение векторов левый вектор текущий
     *
     * @param rightVector вектор
     * @return скалярное произведение
     */
    public double dot(Vector rightVector) {
        return dot(this, rightVector);
    }

    /**
     * нормализатор вектора
     *
     * @return модуль вектора
     */
    public double norm() {
        return sqrt(dot(this, this));
    }

    /**
     * Сложение координат
     *
     * @param rightVector вектор
     * @return текущий измененный объект
     */
    public Vector add(Vector rightVector) {
        set(add(this, rightVector));
        return this;
    }

    /**
     * Вычитание координат
     *
     * @param rightVector вектор
     * @return текущий измененный объект
     */
    public Vector substract(Vector rightVector) {
        set(substract(this, rightVector));
        return this;
    }

    /**
     * Скалярное умножение
     *
     * @param fScalar множитель
     * @return текущий измененный объект
     */
    public Vector scalarMultiply(double fScalar) {
        set(scalarMultiply(this, fScalar));
        return this;
    }

    /**
     * Скалярное деление
     *
     * @param fScalar делитель
     * @return текущий измененный объект
     */
    public Vector scalarDivide(double fScalar) {
        set(scalarDivide(this, fScalar));
        return this;
    }

    /**
     * Унарный минус
     *
     * @return текущий измененный объект
     */
    public Vector unaryMinus() {
        set(unaryMinus(this));
        return this;
    }

    /**
     * Правое векторное произведение
     *
     * @param rightVector вектор
     * @return текущий измененный объект
     */
    public Vector multiplyRight(Vector rightVector) {
        set(multiply(this, rightVector));
        return this;
    }

    /**
     * Левое векторное произведение
     *
     * @param leftVector вектор
     * @return текущий измененный объект
     */
    public Vector multiplyLeft(Vector leftVector) {
        set(multiply(leftVector, this));
        return this;
    }
}