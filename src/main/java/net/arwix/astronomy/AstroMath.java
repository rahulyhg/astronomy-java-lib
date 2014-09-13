package net.arwix.astronomy;

import net.arwix.astronomy.calendar.CalendarMath;
import net.arwix.astronomy.coordinates.Matrix;
import net.arwix.astronomy.coordinates.SphericalVector;
import net.arwix.astronomy.coordinates.Vector;
import net.arwix.astronomy.coordinates.VectorType;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public abstract class AstroMath {

    public static double frac(double x) {
        return x - Math.floor(x);
    }

    //------------------------------------------------------------------------------
    //
    // Modulo: calculates x mod y
    //
    //------------------------------------------------------------------------------

    public static double modulo(double x, double y) {
        return y * frac(x / y);
    }

    /**
     * Преобразование экваториальных координат в эклиптические
     * Transformation of equatorial to ecliptical coordinates
     *
     * @param T Время в юлианских столетиях с эпохи J2000
     * @return Матрица преобразования
     */
    public static Matrix getEquatorialToEclipticalCoordinates(double T) {
        final double eps = Math.toRadians(23.43929111 - (46.8150 + (0.00059 - 0.001813 * T) * T) * T / 3600.0);
        return Matrix.RotateX(eps);
    }

    /**
     * Преобразование эклиптических координат в экваториальные
     *
     * @param T Время в юлианских столетиях с эпохи J2000
     * @return Матрица преобразования
     */
    public static Matrix getEclipticalToEquatorialCoordinates(double T) {
        return Matrix.Transp(getEquatorialToEclipticalCoordinates(T));
    }

    /**
     * Прецессия в эклиптических координатах Precession of ecliptic coordinates
     *
     * @param T1 Исходная эпоха в J2000
     * @param T2 Требуемая эпоха в J2000
     * @return Матрица преобразования координат
     */
    public static Matrix getEclipticalPrecession(double T1, double T2) {
        final double dT = T2 - T1;
        double Pi, pi, p_a;

        Pi = 174.876383889
                * Constant.RAD
                + (((3289.4789 + 0.60622 * T1) * T1) + ((-869.8089 - 0.50491 * T1) + 0.03536 * dT)
                * dT) / Constant.ARCS;

        pi = ((47.0029 - (0.06603 - 0.000598 * T1) * T1) + ((-0.03302 + 0.000598 * T1) + 0.000060 * dT)
                * dT)
                * dT / Constant.ARCS;

        p_a = ((5029.0966 + (2.22226 - 0.000042 * T1) * T1) + ((1.11113 - 0.000042 * T1) - 0.000006 * dT)
                * dT)
                * dT / Constant.ARCS;

        return Matrix.RotateZ(-(Pi + p_a)).Multiply(Matrix.RotateX(pi)).Multiply(Matrix.RotateZ(Pi));
        // return R_z(-(Pi+p_a)) * R_x(pi) * R_z(Pi);
    }

    /**
     * Прецессия в экваториальных координатах Precession of equatorial coordinates
     *
     * @param T1 Исходная эпоха в J2000
     * @param T2 Требуемая эпоха в J2000
     * @return Матрица преобразования координат
     */
    public static Matrix getEquatorialPrecession(double T1, double T2) {
        final double dT = T2 - T1;
        double zeta, z, theta;

        zeta = ((2306.2181 + (1.39656 - 0.000139 * T1) * T1) + ((0.30188 - 0.000344 * T1) + 0.017998 * dT)
                * dT)
                * dT / Constant.ARCS;

        z = zeta + ((0.79280 + 0.000411 * T1) + 0.000205 * dT) * dT * dT / Constant.ARCS;

        theta = ((2004.3109 - (0.85330 + 0.000217 * T1) * T1) - ((0.42665 + 0.000217 * T1) + 0.041833 * dT)
                * dT)
                * dT / Constant.ARCS;

        return Matrix.RotateZ(-z).Multiply(Matrix.RotateY(theta)).Multiply(Matrix.RotateZ(-zeta));

        // return R_z(-z) * R_y(theta) * R_z(-zeta);
    }

    /**
     * Преобразование от средних к истинным экватору и равноденствию
     * Transformation from mean to true equator and equinox
     *
     * @param T Время в юлианских столетиях от эпохи J2000
     * @return матрица нутации Nutation matrix
     */
    public static Matrix getNutation(double T) {
        double ls, D, F, N;
        double eps, dpsi, deps;

        // Mean arguments of lunar and solar motion
        // Средние аргументы лунных и солнечных движений
        ls = Constant.PI2 * frac(0.993133 + 99.997306 * T);   // mean anomaly Sun Средняя аномалия Солнца
        D = Constant.PI2 * frac(0.827362 + 1236.853087 * T);   // diff. longitude Moon-Sun разность долгот
        // Луна-Солнце
        F = Constant.PI2 * frac(0.259089 + 1342.227826 * T);   // mean argument of latitude Средний аргумент
        // широты
        N = Constant.PI2 * frac(0.347346 - 5.372447 * T);   // longit. ascending node Долгота восходящего узла

        // Nutation angles углы нутации
        dpsi = (-17.200 * sin(N) - 1.319 * sin(2 * (F - D + N)) - 0.227 * sin(2 * (F + N)) + 0.206
                * sin(2 * N) + 0.143 * sin(ls))
                / Constant.ARCS;
        deps = (+9.203 * cos(N) + 0.574 * cos(2 * (F - D + N)) + 0.098 * cos(2 * (F + N)) - 0.090 * cos(2 * N))
                / Constant.ARCS;

        // Mean obliquity of the ecliptic средний наклон эклиптики
        eps = 0.4090928 - 2.2696E-4 * T;

        return Matrix.RotateX(-eps - deps).Multiply(Matrix.RotateZ(-dpsi))
                .Multiply(Matrix.RotateX(+eps));

        // return R_x(-eps-deps)*R_z(-dpsi)*R_x(+eps);
    }

    public static class AzimuthCoordintates {
        private final double mLambda;
        private final double mPhi;
        private final Matrix mHourMatrix;

        /**
         * Вычисление азимутальных координат
         *
         * @param longitude долгота в радианах
         * @param latitude  широта в радианах
         */
        public AzimuthCoordintates(double longitude, double latitude) {
            mLambda = longitude;
            mPhi = latitude;
            mHourMatrix = Matrix.RotateY(Math.PI / 2.0 - mPhi);

        }

        /**
         * Получение азимутальных координат
         *
         * @param aMJD                     модифицированная юлианская дата
         * @param GeoEquatorialCoordinates ГеоЦентрические экваториальные координаты
         * @return азимутальный вектор
         */
        public SphericalVector get(double aMJD, Vector GeoEquatorialCoordinates) {
            final double timeGMST = CalendarMath.getGMST(aMJD);
            SphericalVector vector = (SphericalVector) GeoEquatorialCoordinates.getVectorInType(VectorType.SPHERICAL);
            final double tau = timeGMST + mLambda - vector.phi;
            return (SphericalVector) mHourMatrix.MultiplyMV(new SphericalVector(tau, vector.theta, vector.r)).getVectorInType(VectorType.SPHERICAL);
        }

        /**
         * часовой угол
         *
         * @param aMJD модифицированная юлианская дата
         * @return часовой угол в радианах
         */
        public double getHourAngle(double aMJD) {
            final double timeGMST = CalendarMath.getGMST(aMJD);
            return timeGMST + mLambda;
        }


    }


    // единичная матрица
    // identity matrix
    // abstract public Matrix Id3D(double RotAngle);

}