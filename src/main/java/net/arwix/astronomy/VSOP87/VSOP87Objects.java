package net.arwix.astronomy.VSOP87;

import net.arwix.astronomy.AstroMath;
import net.arwix.astronomy.Constant;
import net.arwix.astronomy.Epoch;
import net.arwix.astronomy.HelioEclipticPosition;
import net.arwix.astronomy.coordinates.RectangularVector;
import net.arwix.astronomy.coordinates.Vector;

public enum VSOP87Objects {


    Sun, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune;

    /**
     * get geometric heliocentric ecliptic coordinates
     *
     * @param epoch эпоха
     * @return координаты
     */
    public Vector getHelioEclipticPosition(double T, Epoch epoch) {
        switch (epoch) {
            case APPARENT:
                return new PositionApparent(this).getEclipticCoordinates(T);
            case J2000:
                return new PositionJ2000(this).getEclipticCoordinates(T);
        }
        return null;
    }

    public Vector getGeoEquatorialPosition(double T, Epoch epoch) {
        Vector earthEcliptical = Earth.getHelioEclipticPosition(T, epoch);
        Vector objEcliptical = this.getHelioEclipticPosition(T, epoch);
        Vector objGeoEcliptic = Vector.substract(objEcliptical, earthEcliptical);
        final double dT = objGeoEcliptic.norm() / Constant.C_Light / 36525.0;
        T = T - dT;
        switch (epoch) {
            case APPARENT:
                earthEcliptical = Earth.getHelioEclipticPosition(T, epoch);
                objEcliptical = this.getHelioEclipticPosition(T, epoch);
                objGeoEcliptic = Vector.substract(objEcliptical, earthEcliptical);
                return AstroMath.getNutation(T).Multiply(AstroMath.getEclipticalToEquatorialCoordinates(T)).MultiplyMV(objGeoEcliptic);
            case J2000:
                objEcliptical = this.getHelioEclipticPosition(T, epoch);
                objGeoEcliptic = Vector.substract(objEcliptical, earthEcliptical);
                return AstroMath.getEclipticalToEquatorialCoordinates(Constant.T_J2000).MultiplyMV(objGeoEcliptic);
        }
        return null;
    }

    private static class PositionJ2000 implements HelioEclipticPosition {
        private final VSOP87Objects mObject;

        private PositionJ2000(VSOP87Objects object) {
            mObject = object;
        }

        public Vector getEclipticCoordinates(double T) {
            T = T / 10.0;
            switch (mObject) {
                case Sun:
                    return new RectangularVector(0, 0, 0);
                case Mercury:
                    return new RectangularVector(
                            A_Mercury.X0(T) + A_Mercury.X1(T) + A_Mercury.X2(T) + A_Mercury.X3(T) + A_Mercury.X4(T) + A_Mercury.X5(T),
                            A_Mercury.Y0(T) + A_Mercury.Y1(T) + A_Mercury.Y2(T) + A_Mercury.Y3(T) + A_Mercury.Y4(T) + A_Mercury.Y5(T),
                            A_Mercury.Z0(T) + A_Mercury.Z1(T) + A_Mercury.Z2(T) + A_Mercury.Z3(T) + A_Mercury.Z4(T) + A_Mercury.Z5(T));
                case Venus:
                    return new RectangularVector(
                            A_Venus.X0(T) + A_Venus.X1(T) + A_Venus.X2(T) + A_Venus.X3(T) + A_Venus.X4(T) + A_Venus.X5(T),
                            A_Venus.Y0(T) + A_Venus.Y1(T) + A_Venus.Y2(T) + A_Venus.Y3(T) + A_Venus.Y4(T) + A_Venus.Y5(T),
                            A_Venus.Z0(T) + A_Venus.Z1(T) + A_Venus.Z2(T) + A_Venus.Z3(T) + A_Venus.Z4(T) + A_Venus.Z5(T));
                case Earth:
                    return new RectangularVector(
                            A_Earth.X0(T) + A_Earth.X1(T) + A_Earth.X2(T) + A_Earth.X3(T) + A_Earth.X4(T) + A_Earth.X5(T),
                            A_Earth.Y0(T) + A_Earth.Y1(T) + A_Earth.Y2(T) + A_Earth.Y3(T) + A_Earth.Y4(T) + A_Earth.Y5(T),
                            A_Earth.Z0(T) + A_Earth.Z1(T) + A_Earth.Z2(T) + A_Earth.Z3(T) + A_Earth.Z4(T) + A_Earth.Z5(T));
                case Mars:
                    return new RectangularVector(
                            A_Mars.X0(T) + A_Mars.X1(T) + A_Mars.X2(T) + A_Mars.X3(T) + A_Mars.X4(T) + A_Mars.X5(T),
                            A_Mars.Y0(T) + A_Mars.Y1(T) + A_Mars.Y2(T) + A_Mars.Y3(T) + A_Mars.Y4(T) + A_Mars.Y5(T),
                            A_Mars.Z0(T) + A_Mars.Z1(T) + A_Mars.Z2(T) + A_Mars.Z3(T) + A_Mars.Z4(T) + A_Mars.Z5(T));
                case Jupiter:
                    return new RectangularVector(
                            A_Jupiter.X0(T) + A_Jupiter.X1(T) + A_Jupiter.X2(T) + A_Jupiter.X3(T) + A_Jupiter.X4(T) + A_Jupiter.X5(T),
                            A_Jupiter.Y0(T) + A_Jupiter.Y1(T) + A_Jupiter.Y2(T) + A_Jupiter.Y3(T) + A_Jupiter.Y4(T) + A_Jupiter.Y5(T),
                            A_Jupiter.Z0(T) + A_Jupiter.Z1(T) + A_Jupiter.Z2(T) + A_Jupiter.Z3(T) + A_Jupiter.Z4(T) + A_Jupiter.Z5(T));
                case Saturn:
                    return new RectangularVector(
                            A_Saturn.X0(T) + A_Saturn.X1(T) + A_Saturn.X2(T) + A_Saturn.X3(T) + A_Saturn.X4(T) + A_Saturn.X5(T),
                            A_Saturn.Y0(T) + A_Saturn.Y1(T) + A_Saturn.Y2(T) + A_Saturn.Y3(T) + A_Saturn.Y4(T) + A_Saturn.Y5(T),
                            A_Saturn.Z0(T) + A_Saturn.Z1(T) + A_Saturn.Z2(T) + A_Saturn.Z3(T) + A_Saturn.Z4(T) + A_Saturn.Z5(T));
                case Uranus:
                    return new RectangularVector(
                            A_Uranus.X0(T) + A_Uranus.X1(T) + A_Uranus.X2(T) + A_Uranus.X3(T) + A_Uranus.X4(T) + A_Uranus.X5(T),
                            A_Uranus.Y0(T) + A_Uranus.Y1(T) + A_Uranus.Y2(T) + A_Uranus.Y3(T) + A_Uranus.Y4(T) + A_Uranus.Y5(T),
                            A_Uranus.Z0(T) + A_Uranus.Z1(T) + A_Uranus.Z2(T) + A_Uranus.Z3(T) + A_Uranus.Z4(T) + A_Uranus.Z5(T));
                case Neptune:
                    return new RectangularVector(
                            A_Neptune.X0(T) + A_Neptune.X1(T) + A_Neptune.X2(T) + A_Neptune.X3(T) + A_Neptune.X4(T) + A_Neptune.X5(T),
                            A_Neptune.Y0(T) + A_Neptune.Y1(T) + A_Neptune.Y2(T) + A_Neptune.Y3(T) + A_Neptune.Y4(T) + A_Neptune.Y5(T),
                            A_Neptune.Z0(T) + A_Neptune.Z1(T) + A_Neptune.Z2(T) + A_Neptune.Z3(T) + A_Neptune.Z4(T) + A_Neptune.Z5(T));
            }
            return null;
        }
    }

    private class PositionApparent implements HelioEclipticPosition {
        private final VSOP87Objects mObject;

        private PositionApparent(VSOP87Objects object) {
            mObject = object;
        }

        public Vector getEclipticCoordinates(double T) {
            T = T / 10.0;
            switch (mObject) {
                case Sun:
                    return new RectangularVector(0, 0, 0);
                case Mercury:
                    return new RectangularVector(
                            C_Mercury.X0(T) + C_Mercury.X1(T) + C_Mercury.X2(T) + C_Mercury.X3(T) + C_Mercury.X4(T) + C_Mercury.X5(T),
                            C_Mercury.Y0(T) + C_Mercury.Y1(T) + C_Mercury.Y2(T) + C_Mercury.Y3(T) + C_Mercury.Y4(T) + C_Mercury.Y5(T),
                            C_Mercury.Z0(T) + C_Mercury.Z1(T) + C_Mercury.Z2(T) + C_Mercury.Z3(T) + C_Mercury.Z4(T) + C_Mercury.Z5(T));
                case Venus:
                    return new RectangularVector(
                            C_Venus.X0(T) + C_Venus.X1(T) + C_Venus.X2(T) + C_Venus.X3(T) + C_Venus.X4(T) + C_Venus.X5(T),
                            C_Venus.Y0(T) + C_Venus.Y1(T) + C_Venus.Y2(T) + C_Venus.Y3(T) + C_Venus.Y4(T) + C_Venus.Y5(T),
                            C_Venus.Z0(T) + C_Venus.Z1(T) + C_Venus.Z2(T) + C_Venus.Z3(T) + C_Venus.Z4(T) + C_Venus.Z5(T));
                case Earth:
                    return new RectangularVector(
                            C_Earth.X0(T) + C_Earth.X1(T) + C_Earth.X2(T) + C_Earth.X3(T) + C_Earth.X4(T) + C_Earth.X5(T),
                            C_Earth.Y0(T) + C_Earth.Y1(T) + C_Earth.Y2(T) + C_Earth.Y3(T) + C_Earth.Y4(T) + C_Earth.Y5(T),
                            C_Earth.Z0(T) + C_Earth.Z1(T) + C_Earth.Z2(T) + C_Earth.Z3(T) + C_Earth.Z4(T) + C_Earth.Z5(T));
                case Mars:
                    return new RectangularVector(
                            C_Mars.X0(T) + C_Mars.X1(T) + C_Mars.X2(T) + C_Mars.X3(T) + C_Mars.X4(T) + C_Mars.X5(T),
                            C_Mars.Y0(T) + C_Mars.Y1(T) + C_Mars.Y2(T) + C_Mars.Y3(T) + C_Mars.Y4(T) + C_Mars.Y5(T),
                            C_Mars.Z0(T) + C_Mars.Z1(T) + C_Mars.Z2(T) + C_Mars.Z3(T) + C_Mars.Z4(T) + C_Mars.Z5(T));
                case Jupiter:
                    return new RectangularVector(
                            C_Jupiter.X0(T) + C_Jupiter.X1(T) + C_Jupiter.X2(T) + C_Jupiter.X3(T) + C_Jupiter.X4(T) + C_Jupiter.X5(T),
                            C_Jupiter.Y0(T) + C_Jupiter.Y1(T) + C_Jupiter.Y2(T) + C_Jupiter.Y3(T) + C_Jupiter.Y4(T) + C_Jupiter.Y5(T),
                            C_Jupiter.Z0(T) + C_Jupiter.Z1(T) + C_Jupiter.Z2(T) + C_Jupiter.Z3(T) + C_Jupiter.Z4(T) + C_Jupiter.Z5(T));
                case Saturn:
                    return new RectangularVector(
                            C_Saturn.X0(T) + C_Saturn.X1(T) + C_Saturn.X2(T) + C_Saturn.X3(T) + C_Saturn.X4(T) + C_Saturn.X5(T),
                            C_Saturn.Y0(T) + C_Saturn.Y1(T) + C_Saturn.Y2(T) + C_Saturn.Y3(T) + C_Saturn.Y4(T) + C_Saturn.Y5(T),
                            C_Saturn.Z0(T) + C_Saturn.Z1(T) + C_Saturn.Z2(T) + C_Saturn.Z3(T) + C_Saturn.Z4(T) + C_Saturn.Z5(T));
                case Uranus:
                    return new RectangularVector(
                            C_Uranus.X0(T) + C_Uranus.X1(T) + C_Uranus.X2(T) + C_Uranus.X3(T) + C_Uranus.X4(T) + C_Uranus.X5(T),
                            C_Uranus.Y0(T) + C_Uranus.Y1(T) + C_Uranus.Y2(T) + C_Uranus.Y3(T) + C_Uranus.Y4(T) + C_Uranus.Y5(T),
                            C_Uranus.Z0(T) + C_Uranus.Z1(T) + C_Uranus.Z2(T) + C_Uranus.Z3(T) + C_Uranus.Z4(T) + C_Uranus.Z5(T));
                case Neptune:
                    return new RectangularVector(
                            C_Neptune.X0(T) + C_Neptune.X1(T) + C_Neptune.X2(T) + C_Neptune.X3(T) + C_Neptune.X4(T) + C_Neptune.X5(T),
                            C_Neptune.Y0(T) + C_Neptune.Y1(T) + C_Neptune.Y2(T) + C_Neptune.Y3(T) + C_Neptune.Y4(T) + C_Neptune.Y5(T),
                            C_Neptune.Z0(T) + C_Neptune.Z1(T) + C_Neptune.Z2(T) + C_Neptune.Z3(T) + C_Neptune.Z4(T) + C_Neptune.Z5(T));
            }
            return null;
        }
    }


}
