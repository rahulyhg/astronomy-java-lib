package net.arwix.astronomy.coordinates;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Ортографическая проекция точки сферы на плоскость
 * может применятся для построения сетки координат и получения регионов по их координатам на сфере
 * <p/>
 * Входящие данные
 * - долгота и широта центральной точки проекции на сфере
 */
public class OrthographicProjection {

    private double sinT_CP;
    private double cosT_CP;
    private double longitudeCenterPoint;

    private double sinT;
    private double cosT;
    private double cosDelta;

    public double x;
    public double y;
    public double cosDistance;


    /**
     * Результирующий класс
     * <p/>
     * immutable
     * <p/>
     * Отдает:
     * координаты точки на проекции
     * косинус угла сферы для возможности определения видимая это точка или она расположена противоположной стороне сферы
     */
    public static class ResultPoint {
        public final double x;
        public final double y;
        public final double cosDistance;

        private ResultPoint(double x, double y, double cosDistance) {
            this.x = x;
            this.y = y;
            this.cosDistance = cosDistance;
        }
    }


    /**
     * Иницилизация класса
     *
     * @param longitudeCenterPoint долгота центральной точки проекции
     * @param latitudeCenterPoint  широта центральной точки проекции
     */
    public OrthographicProjection(double longitudeCenterPoint, double latitudeCenterPoint) {
        this.sinT_CP = sin(latitudeCenterPoint);
        this.cosT_CP = cos(latitudeCenterPoint);
        this.longitudeCenterPoint = longitudeCenterPoint;
    }


    /**
     * Вычисление проекции и сохранение результатов
     * в
     * {@link #x}
     * {@link #y}
     * {@link #cosDistance}
     *
     * @param point долгота и широта точки а так же радиус сферы
     */
    public void callProjection(SphericalVector point) {
        cosT = cos(point.theta);
        sinT = sin(point.theta);
        cosDelta = cos(point.phi - longitudeCenterPoint);

        x = point.r * cosT * sin(point.phi - longitudeCenterPoint);
        y = point.r * (cosT_CP * sinT - sinT_CP * cosT * cosDelta);
        cosDistance = sinT_CP * sinT + cosT_CP * cosT * cosDelta;
    }

    /**
     * Вычисление проекции и получение результата в {@link ResultPoint}
     * в отличии от {@link #callProjection(SphericalVector)} не схораняет данные в текущем объекте, что позволяет использовать в параллельных потоках
     *
     * @param point долгота и широта точки а так же радиус сферы
     * @return ResultPoint
     */
    public ResultPoint getProjection(SphericalVector point) {
        final double cosT = cos(point.theta);
        final double sinT = sin(point.theta);
        final double cosDelta = cos(point.phi - longitudeCenterPoint);

        final double x = point.r * cosT * sin(point.phi - longitudeCenterPoint);
        final double y = point.r * (cosT_CP * sinT - sinT_CP * cosT * cosDelta);
        final double cosDistance = sinT_CP * sinT + cosT_CP * cosT * cosDelta;
        return new ResultPoint(x, y, cosDistance);
    }

    /**
     * Иницилизация для пакетной обработки данных если широта константа, долгота меняется
     *
     * @param point долгота и широта точки а так же радиус сферы
     * @return необходимые данные сохраняемые между интерациями {@link #runBatchLogitude(SphericalVector, double[])}
     */
    public double[] initBatchLongitude(SphericalVector point) {
        return new double[]{cos(point.theta), sin(point.theta)};
    }

    /**
     * Интерация пакетной обработки данных {@see #initBatchLongitude(SphericalVector)}
     *
     * @param point     долгота и широта точки а так же радиус сферы
     * @param initBatch данные получениые при выполнении метода {@link #initBatchLongitude}
     */
    public void runBatchLogitude(SphericalVector point, double[] initBatch) {
        cosT = initBatch[0];
        sinT = initBatch[1];
        cosDelta = cos(point.phi - longitudeCenterPoint);

        x = point.r * cosT * sin(point.phi - longitudeCenterPoint);
        y = point.r * (cosT_CP * sinT - sinT_CP * cosT * cosDelta);
        cosDistance = sinT_CP * sinT + cosT_CP * cosT * cosDelta;
    }

    /**
     * Иницилизация для пакетной обработки данных если долгота константа, широта меняется
     *
     * @param point долгота и широта точки а так же радиус сферы
     * @return необходимые данные сохраняемые между интерациями {@link #runBatchLatitude(SphericalVector, double[])}}
     */
    public double[] initBatchLatitude(SphericalVector point) {
        return new double[]{cos(point.phi - longitudeCenterPoint), sin(point.phi - longitudeCenterPoint)};
    }

    /**
     * Интерация пакетной обработки данных {@see #initBatchLatitude(SphericalVector)}
     *
     * @param point     долгота и широта точки а так же радиус сферы
     * @param initBatch данные получениые при выполнении метода {@link #initBatchLatitude}
     */
    public void runBatchLatitude(SphericalVector point, double[] initBatch) {
        cosT = cos(point.theta);
        sinT = sin(point.theta);
        cosDelta = initBatch[0];

        x = point.r * cosT * initBatch[1];
        y = point.r * (cosT_CP * sinT - sinT_CP * cosT * cosDelta);
        cosDistance = sinT_CP * sinT + cosT_CP * cosT * cosDelta;
    }


}