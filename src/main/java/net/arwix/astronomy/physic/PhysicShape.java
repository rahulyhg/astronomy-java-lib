package net.arwix.astronomy.physic;

import net.arwix.astronomy.VSOP87.VSOP87Objects;

public abstract class PhysicShape {
    public PhysicShape() {
    }

    // экваториальный радиус в км
    public static double getEquatorialRadius(VSOP87Objects object) {
        switch(object) {
        case Sun:
            return 696000.0D;
        case Mercury:
            return 2439.0D;
        case Venus:
            return 6051.0D;
        case Earth:
            return 6378.14D;
        case Mars:
            return 3393.4D;
        case Jupiter:
            return 71398.0D;
        case Saturn:
            return 60000.0D;
        case Uranus:
            return 25400.0D;
        case Neptune:
            return 24300.0D;
        default:
            return 0.0D;
        }
    }

    // сплюснутость планеты
    public static double getGeometricalFlattening(VSOP87Objects object) {
        switch(object) {
        case Earth:
            return 0.00335281D;
        case Mars:
            return 0.0051865D;
        case Jupiter:
            return 0.0648088D;
        case Saturn:
            return 0.1076209D;
        case Uranus:
            return 0.03D;
        case Neptune:
            return 0.0259D;
        default:
            return 0.0D;
        }
    }
}
