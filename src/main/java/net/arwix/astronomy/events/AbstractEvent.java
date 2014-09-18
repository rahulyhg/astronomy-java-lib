package net.arwix.astronomy.events;

import net.arwix.astronomy.Constant;
import net.arwix.astronomy.Epoch;
import net.arwix.astronomy.GeocentricEquatorialCoordinates;
import net.arwix.astronomy.calendar.CalendarMath;
import net.arwix.astronomy.coordinates.Location;
import net.arwix.astronomy.coordinates.SphericalVector;
import net.arwix.astronomy.coordinates.VectorType;
import net.arwix.astronomy.math.QuadraticInterpolation;

import java.util.Calendar;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Вычисление моментов восхода и захода объектов
 *
 * @author Vitaliy Sheyanov <vit.onix@gmail.com>
 * @version 1.0
 */
abstract class AbstractEvent {

    protected Calendar date, innerDate;
    protected Location location;
    private GeocentricEquatorialCoordinates coordinates;
    protected double deltaT;

    protected boolean isValid;
    protected Calendar riseDate, downDate;
    protected boolean above;

    public AbstractEvent(GeocentricEquatorialCoordinates coordinates) {
        this.isValid = false;
        this.innerDate = Calendar.getInstance();
        this.coordinates = coordinates;
    }

    public void setDate(Calendar date) {
        this.isValid = false;
        this.date = date;
        this.innerDate.setTimeZone(date.getTimeZone());
        this.innerDate.setTime(date.getTime());
        this.deltaT = CalendarMath.getDeltaTofDay(innerDate);
    }

    public Calendar getDate() {
        return this.date;
    }

    public void setLocation(Location location) {
        this.isValid = false;
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    protected void resetTimeInnerDate() {
        this.innerDate.set(Calendar.HOUR_OF_DAY, 0);
        this.innerDate.set(Calendar.MINUTE, 0);
        this.innerDate.set(Calendar.SECOND, 0);
        this.innerDate.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Синус высоты объекта над горизонтом
     *
     * @param MJD         на расчетную дату
     * @param longitude   долгота в радианах
     * @param cosLatitude косинус широты
     * @param sinLatitude синус широты
     * @return cинус высоты Солнца или Луны в момент искомого события
     */
    protected double getSinAltitude(double MJD, double longitude, double cosLatitude,
                                  double sinLatitude) {
        final double T, tau;
        final SphericalVector p;

        T = (MJD - Constant.MJD_J2000 - this.deltaT) / 36525.0;
        p = (SphericalVector) coordinates.getGeocentricEquatorialPosition(T, Epoch.APPARENT).getVectorInType(VectorType.SPHERICAL);

        // часовой угол
        tau = CalendarMath.getGMST(MJD) + longitude - p.phi;

        return sinLatitude * sin(p.theta) + cosLatitude * cos(p.theta) * cos(tau);
    }

    /**
     * Расчет моментов восхода/захода Объекта и наступления сумерек
     * <p/>
     * с учетом рефракции время в UT
     *
     * @param sinRefractionAngle {@link ObjectType#getSinRefractionAngle()}
     * @return @see RiseSet
     */
    final protected void callCircleOfLatitudeRiseSet(
            double sinRefractionAngle) {
        isValid = false;
        // latitude = 65.5;
        // 27.05.2012
//        final double refraction = getSinRefractionAngle(event);
        resetTimeInnerDate();
        riseDate = null;
        downDate = null;
        final double MJD0 = CalendarMath.getMJD(innerDate);
        final double cosLatitude = cos(this.location.latitude);
        final double sinLatitude = sin(this.location.latitude);

        double hour = 1.0;
        double y_minus, y_0, y_plus;

        // Инициализация поиска
        y_minus = getSinAltitude(MJD0 + (hour - 1.0) / 24.0, this.location.longitude, cosLatitude, sinLatitude) - sinRefractionAngle;

        // признак что выше горизонта
        above = (y_minus > 0.0);
        boolean rises = false;
        boolean sets = false;

        double LT_Rise, LT_Set;

        // перебор интервалов [0h-2h] to [22h-24h]
        do {
            y_0 = getSinAltitude(MJD0 + hour / 24.0, this.location.longitude, cosLatitude, sinLatitude) - sinRefractionAngle;
            y_plus = getSinAltitude(MJD0 + (hour + 1.0) / 24.0, this.location.longitude, cosLatitude, sinLatitude) - sinRefractionAngle;

            // определние параболы по трем значением y_minus,y_0,y_plus
            QuadraticInterpolation.Result quadResult = new QuadraticInterpolation(y_minus, y_0,
                    y_plus).getResult();

            // корень один
            if (quadResult.roots != null && quadResult.roots.count == 1) {
                if (y_minus < 0.0) {
                    LT_Rise = hour + quadResult.roots.root1;
                    riseDate = Calendar.getInstance(date.getTimeZone());
                    riseDate.setTime(date.getTime());
                    CalendarMath.setHours(riseDate, LT_Rise);
                    rises = true;
                } else {
                    LT_Set = hour + quadResult.roots.root1;
                    downDate = Calendar.getInstance(date.getTimeZone());
                    downDate.setTime(date.getTime());
                    CalendarMath.setHours(downDate, LT_Set);
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
                riseDate = Calendar.getInstance(date.getTimeZone());
                downDate = Calendar.getInstance(date.getTimeZone());
                riseDate.setTime(date.getTime());
                downDate.setTime(date.getTime());
                CalendarMath.setHours(riseDate, LT_Rise);
                CalendarMath.setHours(downDate, LT_Set);
                rises = true;
                sets = true;
            }

            y_minus = y_plus; // подготовка к обработке следующего интервала
            hour += 2.0;
        } while (!((hour == 25.0) || (rises && sets)));
        isValid = true;
    }

}