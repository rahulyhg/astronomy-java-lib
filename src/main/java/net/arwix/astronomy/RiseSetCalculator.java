package net.arwix.astronomy;

import net.arwix.astronomy.VSOP87.VSOP87Objects;
import net.arwix.astronomy.calendar.SimpleDate;

import static java.lang.Math.*;

/**
 * Вычисление моментов восхода и захода объектов
 *
 * @author Vitaliy Sheyanov <vit.onix@gmail.com>
 * @version 1.0
 */
public class RiseSetCalculator {

    private VSOP87Objects obj;

    /**
     * Перечесление различных вариантов восхода захода и кульминации
     */
    public static enum Event {
        RiseSet, CivilTwilight, NautiTwilight, AstroTwilight, UpperCulmination, LowerCulmination
    }

    /**
     * класс возвращаемых значений восхода и захода
     * <p/>
     * признак нахождения целый день над или под горизонтом if rise = set =
     * null и above = true то объект не заходит
     */
    final public static class Result {

        final public SimpleDate rise, set;
        final public boolean above;

        public Result(SimpleDate rise, SimpleDate set, boolean above) {
            this.rise = rise;
            this.set = set;
            this.above = above;
        }
    }

    /**
     * класс возвращаемых значений верхней и нижней кульминаций
     */
    final public static class ResultCulmination {

        final public SimpleDate upperCulmination, lowerCulmination;
        final public boolean aboveUpper, aboveLower;

        public ResultCulmination(SimpleDate upperCulmination, SimpleDate lowerCulmination, boolean aboveUpper,
                                 boolean aboveLower) {
            this.upperCulmination = upperCulmination;
            this.lowerCulmination = lowerCulmination;
            this.aboveUpper = aboveUpper;
            this.aboveLower = aboveLower;
        }
    }

    public RiseSetCalculator(VSOP87Objects object) {
        if (object == null) throw new NullPointerException();
        this.obj = object;
    }

    // TODO Moon sin(toRadians(+8.0 / 60.0)
    private double getSinRefractionAngle(Event event) {
        if (!(obj instanceof VSOP87Objects.Sun) && (event != Event.RiseSet))
            throw new IndexOutOfBoundsException();
        if (obj instanceof VSOP87Objects.Sun) {
            switch (event) {
                case RiseSet:
                    return sin(toRadians(-50.0 / 60.0));
                case CivilTwilight:
                    return sin(toRadians(-6.0));
                case NautiTwilight:
                    return sin(toRadians(-12.0));
                case AstroTwilight:
                    return sin(toRadians(-18.0));
            }
        }
        return sin(toRadians(-34.0 / 60.0));
    }

    /**
     * Синус высоты объекта над горизонтом
     *
     * @param MJD          на расчетную дату
     * @param longitudeRAD долгота в радианах
     * @param cosPhi       косинус широты
     * @param sinPhi       синус широты
     * @return cинус высоты Солнца или Луны в момент искомого события
     */
    private double getSinAltitude(double MJD, double longitudeRAD, double cosPhi,
                                  double sinPhi) {
        final double T, tau;
        final Polar p;

        T = (MJD - MJD_J2000) / 36525.0;
        p = obj.getLowPrecisionEquatorialCoordinates(T);

        // часовой угол
        tau = Date.getGMST(MJD) + longitudeRAD - p.elements[PHI];

        return sinPhi * sin(p.elements[THETA]) + cosPhi * cos(p.elements[THETA]) * cos(tau);
    }

    final public ResultCulmination getCulmination(Date date, double longitude, double latitude) {

        // для вычисления экстремума не нужен но нужен для потнятия выше или ниже горизонта
        final double refraction = getSinRefractionAngle(Event.RiseSet);
        final double lambda = toRadians(longitude);
        final double phi = toRadians(latitude);
        Date innerDate = date.clone();
        innerDate.resetTime();
        final double MJD0 = innerDate.getMJD(true) + innerDate.getDeltaMJD();
        final double Cphi = cos(phi);
        final double Sphi = sin(phi);

        SearchExtremumGoldenSection.Function function = new SearchExtremumGoldenSection.Function() {
            public double calculation(double x) {
                return getSinAltitude(MJD0 + x / 24.0, lambda, Cphi, Sphi) - refraction;
            }
        };

        SearchExtremumGoldenSection culmination = new SearchExtremumGoldenSection(function, 0,
                24.0, Math.ulp(100), 50);

        Date upperCulmination = innerDate.clone().setTime(new Time(culmination.getMax()));
        boolean aboveUpper = (getSinAltitude(
                upperCulmination.getMJD(true) + upperCulmination.getDeltaMJD(), lambda, Cphi, Sphi) - refraction) > 0;
        Date lowerCulmination = innerDate.clone().setTime(new Time(culmination.getMin()));
        boolean aboveLower = (getSinAltitude(
                lowerCulmination.getMJD(true) + lowerCulmination.getDeltaMJD(), lambda, Cphi, Sphi) - refraction) > 0;
        return new ResultCulmination(upperCulmination, lowerCulmination, aboveUpper, aboveLower);
    }

    /**
     * Расчет моментов восхода/захода Солнца и наступления сумерек
     * <p/>
     * с учетом рефракции время в UT
     *
     * @param event     {@link Event}
     * @param longitude долгота в градусах и десятичных долях градуса
     * @param latitude  широта в градусах и десятичных долях градуса
     * @return @see RiseSet
     */
    final public Result getRiseSet(Event event, Date date, double longitude, double latitude) {
        // latitude = 65.5;
        // 27.05.2012
        final double refraction = getSinRefractionAngle(event);
        final double lambda = toRadians(longitude);
        final double phi = toRadians(latitude);
        Date innerDate = date.clone();
        innerDate.resetTime();
        Date riseDate = null, downDate = null;
        final double MJD0 = innerDate.getMJD(true) + innerDate.getDeltaMJD();
        final double Cphi = cos(phi);
        final double Sphi = sin(phi);

        double hour = 1.0;
        double y_minus, y_0, y_plus;

        // Инициализация поиска
        y_minus = getSinAltitude(MJD0 + (hour - 1.0) / 24.0, lambda, Cphi, Sphi) - refraction;

        // признак что выше горизонта
        boolean above = (y_minus > 0.0);
        boolean rises = false;
        boolean sets = false;

        double LT_Rise, LT_Set;

        // перебор интервалов [0h-2h] to [22h-24h]
        do {
            y_0 = getSinAltitude(MJD0 + hour / 24.0, lambda, Cphi, Sphi) - refraction;
            y_plus = getSinAltitude(MJD0 + (hour + 1.0) / 24.0, lambda, Cphi, Sphi) - refraction;

            // определние параболы по трем значением y_minus,y_0,y_plus
            QuadraticInterpolation.Result quadResult = new QuadraticInterpolation(y_minus, y_0,
                    y_plus).getResult();

            // корень один
            if (quadResult.roots != null && quadResult.roots.count == 1) {
                if (y_minus < 0.0) {
                    LT_Rise = hour + quadResult.roots.root1;
                    riseDate = innerDate.clone().setTime(new Time(LT_Rise));
                    rises = true;
                } else {
                    LT_Set = hour + quadResult.roots.root1;
                    downDate = innerDate.clone().setTime(new Time(LT_Set));
                    sets = true;
                }
            }

            // корня два
            if (quadResult.roots != null && quadResult.roots.count == 2) {
                if (quadResult.extremum.y < 0.0) {
                    LT_Rise = hour + quadResult.roots.root2;
                    LT_Set = hour + quadResult.roots.root1;
                } else {
                    LT_Rise = hour + quadResult.roots.root1;
                    LT_Set = hour + quadResult.roots.root2;
                }
                riseDate = innerDate.clone().setTime(new Time(LT_Rise));
                downDate = innerDate.clone().setTime(new Time(LT_Set));
                rises = true;
                sets = true;
            }

            y_minus = y_plus; // подготовка к обработке следующего интервала
            hour += 2.0;
        } while (!((hour == 25.0) || (rises && sets)));

//        if (!(riseDate != null || downDate != null)) {
//            // rTimeRS.setAbove(above);
//        }

        return new Result(riseDate, downDate, above);
        // return rTimeRS;
    }

}