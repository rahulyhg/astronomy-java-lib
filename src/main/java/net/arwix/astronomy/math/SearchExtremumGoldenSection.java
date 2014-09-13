package net.arwix.astronomy.math;

/**
 * Вычисление экстремума функции методом золотого сечения
 */
public final class SearchExtremumGoldenSection {

    public interface Function {
        public double calculation(double x);
    }

    public static final double GOLDEN_RATIO = 0.5 + Math.sqrt(5) / 2.0;

    private Function function;
    private double a, b, e;
    private int maxSteps;

    private Double min;
    private Double max;

    /**
     * @param f        функция реализующа интерфейс {@link Function}
     * @param a        начальная точка отразка
     * @param b        конечная точка отрезка
     * @param e        желаемя ночность
     * @param maxSteps максимальное число шагов
     */
    public SearchExtremumGoldenSection(Function f, double a, double b, double e, int maxSteps) {
        this.a = a;
        this.b = b;
        this.e = e;
        this.maxSteps = maxSteps;
        this.function = f;
    }

    public double getMax() {
        if (this.max == null) this.max = doMax(a, b);
        return this.max;
    }

    public double getMin() {
        if (this.min == null) this.min = doMin(a, b);
        return this.min;
    }

    private Double doMax(double a, double b) {
        int step = 0;
        do {
            step++;
            double d = (b - a) / GOLDEN_RATIO;
            double x1 = b - d;
            double x2 = a + d;
            double y1 = function.calculation(x1);
            double y2 = function.calculation(x2);
            if (y1 <= y2) {
                a = x1;
            } else {
                b = x2;
            }
        } while (Math.abs(a - b) > this.e && step < maxSteps);
        return (a + b) / 2;
    }

    private Double doMin(double a, double b) {
        int step = 0;
        do {
            step++;
            double d = (b - a) / GOLDEN_RATIO;
            double x1 = b - d;
            double x2 = a + d;
            double y1 = function.calculation(x1);
            double y2 = function.calculation(x2);
            if (y1 >= y2) {
                a = x1;
            } else {
                b = x2;
            }
        } while (Math.abs(a - b) > this.e && step < maxSteps);
        return (a + b) / 2;
    }

}