package net.arwix.astronomy.events;

import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public enum ObjectType {

    Sun(-0.833), Moon(+0.133), Dot(-0.5667);

    private double sinRefractionAngle;

    private ObjectType(double angle) {
        this.sinRefractionAngle = sin(toRadians(angle));
    }

    double getSinRefractionAngle() {
        return this.sinRefractionAngle;
    }
}
