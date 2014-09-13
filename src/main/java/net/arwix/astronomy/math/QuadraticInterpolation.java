package net.arwix.astronomy.math;

/**
 * Квадратичная интерполяция
 * Находит корни и экстремум параболы, интерполирующей три эквидистантных значения функции f при x = {-1, 0, 1}
 */
public final class QuadraticInterpolation {

    public static class Roots {
        final public double root1;
        final public double root2;
        final public int count;

        public Roots(double root1, double root2, int count) {
            this.root1 = root1;
            this.root2 = root2;
            this.count = count;
        }
    }

    public static class Result {
        final public PointD extremum;
        final public Roots roots;

        public Result(PointD extremum, Roots roots) {
            this.extremum = extremum;
            this.roots = roots;
        }
    }

    private final Result result;

    /**
     * Квадратичная интерполяция
     * Находит корни и экстремум параболы, интерполирующей три эквидистантных значения функции
     *
     * @param y_minus Значение функции в точке x = -1
     * @param y_0     Значение функции в точке x = 0
     * @param y_plus  Значение функции в точке x = +1
     */
    public QuadraticInterpolation(double y_minus, double y_0, double y_plus) {
        this.result = impl(y_minus, y_0, y_plus);
    }

    /**
     * Результат может быть и без корней тогда Roots = null или roots.count = 0
     *
     * @return {@link Result} корни и экстремум
     * элементы
     * 0 - абсцисса экстремума
     * 1 - ордината экстремума
     * 2,3 - корни
     * 4 - колличество найденых корней
     */
    public Result getResult() {
        return result;
    }

    private static Result impl(double y_minus, double c, double y_plus) {
        // Коэффициенты итерполирующей параболы y=a*x^2+b*x+c
        final double a = 0.5 * (y_plus + y_minus) - c;
        final double b = 0.5 * (y_plus - y_minus);

        // Находим экстремум
        final double xe = -b / (2.0 * a);
        final PointD extremum = new PointD(xe, (a * xe + b) * xe + c);

        final double dis = b * b - 4.0 * a * c; // дискриминант уравнения y=a*x^2+b*x+c

        Result result;
        if (dis >= 0) // Парабола имеет корни roots
        {
            final double dx = 0.5 * Math.sqrt(dis) / Math.abs(a);

            double root1 = extremum.x - dx;
            double root2 = extremum.x + dx;
            int count = 0;

            if (Math.abs(root1) <= 1.0) count++;
            if (Math.abs(root2) <= 1.0) count++;
            if (root1 < -1.0) root1 = root2;
            result = new Result(extremum, new Roots(root1, root2, count));
        } else result = new Result(extremum, null);
        return result;
    }
}