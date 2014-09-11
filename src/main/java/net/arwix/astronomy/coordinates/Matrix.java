package net.arwix.astronomy.coordinates;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix {

    private double elements[][] = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};

    public Matrix() {

    }

    public Matrix(Vector vector1, Vector vector2, Vector vector3) {
        double[] v1 = ((RectangularVector) vector1.getVectorInType(VectorType.RECTANGULAR)).toArray();
        double[] v2 = ((RectangularVector) vector2.getVectorInType(VectorType.RECTANGULAR)).toArray();
        double[] v3 = ((RectangularVector) vector3.getVectorInType(VectorType.RECTANGULAR)).toArray();
        for (int i = 0; i < 3; i++) {
            this.elements[i][0] = v1[i];
            this.elements[i][1] = v2[i];
            this.elements[i][2] = v3[i];
        }
    }

    /**
     * матрицы поворта вокруг осей базиса
     * elementary rotations
     */
    public static Matrix RotateX(double angle) {
        final double s = sin(angle);
        final double c = cos(angle);

        Matrix out = new Matrix();

        out.elements[0][0] = 1.0;
        out.elements[0][1] = 0.0;
        out.elements[0][2] = 0.0;
        out.elements[1][0] = 0.0;
        out.elements[1][1] = +c;
        out.elements[1][2] = +s;
        out.elements[2][0] = 0.0;
        out.elements[2][1] = -s;
        out.elements[2][2] = +c;

        return out;
    }

    public static Matrix RotateY(double angle) {
        final double s = sin(angle);
        final double c = cos(angle);

        Matrix out = new Matrix();

        out.elements[0][0] = +c;
        out.elements[0][1] = 0.0;
        out.elements[0][2] = -s;
        out.elements[1][0] = 0.0;
        out.elements[1][1] = 1.0;
        out.elements[1][2] = 0.0;
        out.elements[2][0] = +s;
        out.elements[2][1] = 0.0;
        out.elements[2][2] = +c;

        return out;
    }

    public static Matrix RotateZ(double angle) {
        final double s = sin(angle);
        final double c = cos(angle);

        Matrix out = new Matrix();

        out.elements[0][0] = +c;
        out.elements[0][1] = +s;
        out.elements[0][2] = 0.0;
        out.elements[1][0] = -s;
        out.elements[1][1] = +c;
        out.elements[1][2] = 0.0;
        out.elements[2][0] = 0.0;
        out.elements[2][1] = 0.0;
        out.elements[2][2] = 1.0;

        return out;
    }

    /**
     * Transpose of matrix
     */
    static public Matrix Transp(Matrix Mat) {
        Matrix out = new Matrix();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                out.elements[i][j] = Mat.elements[j][i];
        return out;
    }

    /**
     * матричное векторное умножение, матрицы на вектор
     *
     * @param matrix матрица
     * @param vector вектор
     * @return новый вектор
     */
    public static Vector MultiplyMV(Matrix matrix, Vector vector) {
        double[] v = ((RectangularVector) vector.getVectorInType(VectorType.RECTANGULAR)).toArray();
        double[] r = new double[3];
        for (int i = 0; i < 3; i++) {
            double scalp = 0.0;
            for (int j = 0; j < 3; j++)
                scalp += matrix.elements[i][j] * v[j];
            r[i] = scalp;
        }
        return new RectangularVector(r);
    }

    /**
     * матричное векторное умножение, вектора на матрицу
     *
     * @param vector вектор
     * @param matrix матрица
     * @return новый вектор
     */
    public static Vector MultiplyVM(Vector vector, Matrix matrix) {
        double[] v = ((RectangularVector) vector.getVectorInType(VectorType.RECTANGULAR)).toArray();
        double[] r = new double[3];
        for (int j = 0; j < 3; j++) {
            double scalp = 0.0;
            for (int i = 0; i < 3; i++) {
                scalp += v[i] * matrix.elements[i][j];
            }
            r[j] = scalp;
        }
        return new RectangularVector(r);
    }

    /**
     * матричное умножение
     *
     * @param left  матрица
     * @param right матрица
     * @return новая матрица
     */
    public static Matrix Multiply(Matrix left, Matrix right) {
        Matrix result = new Matrix();
        double scalp;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                scalp = 0.0;
                for (int k = 0; k < 3; k++)
                    scalp += left.elements[i][k] * right.elements[k][j];
                result.elements[i][j] = scalp;
            }
        }
        return result;
    }

    /**
     * матричное векторное умножение, текущий матрицы на вектор
     *
     * @param vector вектор
     * @return новый вектор
     */
    public Vector MultiplyMV(Vector vector) {
        return MultiplyMV(this, vector);
    }

    /**
     * матричное векторное умножение, вектора на текущую матрицу
     *
     * @param vector вектор
     * @return новый вектор
     */
    public Vector MultiplyVM(Vector vector) {
        return MultiplyVM(vector, this);
    }

    /**
     * матричное умножение
     *
     * @param right матрица
     * @return текущая измененая матрица
     */
    public Matrix Multiply(Matrix right) {
        Matrix result = new Matrix();
        double scalp;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                scalp = 0.0;
                for (int k = 0; k < 3; k++)
                    scalp += this.elements[i][k] * right.elements[k][j];
                result.elements[i][j] = scalp;
            }
        }
        this.elements = result.elements;
        return this;
    }

    public Vector getRowX() {
        double[] r = new double[3];
        System.arraycopy(this.elements[0], 0, r, 0, 3);
        return new RectangularVector(r);
    }

    public Vector getRowY() {
        double[] r = new double[3];
        System.arraycopy(this.elements[1], 0, r, 0, 3);
        return new RectangularVector(r);
    }

    public Vector getRowZ() {
        double[] r = new double[3];
        System.arraycopy(this.elements[2], 0, r, 0, 3);
        return new RectangularVector(r);
    }

}