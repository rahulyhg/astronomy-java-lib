package net.arwix.astronomy;

public class Constant {

    public static final double PI2 = 2.0 * Math.PI;
    public static final double RAD = Math.PI / 180.0;
    public static final double DEG = 180.0 / Math.PI;
    public static final double ARCS = 3600.0 * 180.0 / Math.PI;

    // радиусы Земли, Солнца, Луны в км
    public static final double R_Earth = 6378.137;
    public static final double R_Sun = 696000.0;
    public static final double R_Moon = 1738.0;

    public static final double MJD_J2000 = 51544.5;        // MJD на эпоху J2000.0
    public static final double T_J2000 = 0.0;           // эпоха J2000.0
    public static final double T_B1950 = -0.500002108;   // эпоха B1950
    public static final double JD_SECOND = 0.000011574074074074074074;
    public static final double JD_MINUTE = 0.00069444444444444444444;
    public static final double JD_HOUR = 0.041666666666666666666;
    public static final double JD_DAY = 1.0;

    public static final double kGauss = 0.01720209895;  // гравитационная константа
    public static final double GM_Sun = kGauss * kGauss;  // [AU^3/d^2]

    public static final double AU = 149597870.0;    // 1ае

    public static final double C_Light = 173.14;         // скорость света [AU/d]

}
