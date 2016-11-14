package net.arwix.astronomy.physic;

import java.util.Calendar;

import net.arwix.astronomy.AstroMath;
import net.arwix.astronomy.Constant;
import net.arwix.astronomy.Epoch;
import net.arwix.astronomy.VSOP87.VSOP87Objects;
import net.arwix.astronomy.calendar.CalendarMath;
import net.arwix.astronomy.coordinates.Matrix;
import net.arwix.astronomy.coordinates.SphericalVector;
import net.arwix.astronomy.coordinates.Vector;
import net.arwix.astronomy.coordinates.VectorType;
import net.arwix.astronomy.physic.PhysicOrientation.Sense;
import net.arwix.astronomy.physic.PhysicOrientation.SystemType;

public class PhysicCalculator {
    private VSOP87Objects mObject;
    private double mT;
    // Гелиографической широта центральной точки диска
    // Heliographic latitude of the central point of the solar disk; also called the B-angle.
    private double B;
    private double mApparentDiameter;

    public PhysicCalculator(VSOP87Objects object, Calendar date) {
        this.mObject = object;
        this.mT = CalendarMath.getJT(date);
        this.calculate();
    }

    public void calculate() {
        Vector objVector = this.mObject.getGeocentricEquatorialPosition(this.mT, Epoch.APPARENT);
        Vector earthVector = VSOP87Objects.Earth.getGeocentricEquatorialPosition(this.mT, Epoch.APPARENT);
        Vector drVector = Vector.substract(objVector, earthVector);
        double delta = Vector.norm(drVector);
        this.mApparentDiameter = Constant.ARCS * 2.0 * Math.asin(PhysicShape.getEquatorialRadius(this.mObject) / (delta * Constant.AU));
        Matrix orientation = PhysicOrientation.getOrientation(this.mObject, this.mT, SystemType.I);
        Matrix P = AstroMath.getEquatorialPrecession(this.mT, 0.0D);
        Matrix e1 = orientation.Multiply(P);
        PhysicCalculator.Rotation rotation_I = new PhysicCalculator.Rotation(Vector.unaryMinus(drVector), e1, PhysicOrientation.getSense(this.mObject), PhysicShape.getGeometricalFlattening(this.mObject));
        this.B = rotation_I.latitude;
    }

    public double getApparentDiameter() {
        return this.mApparentDiameter;
    }

    public double getB() {
        return this.B;
    }

    protected static class Rotation {
        public double longitude;
        public double latitude;
        public double planetocentricLatitude;

        public Rotation(Vector r, Matrix E, Sense sense, double f) {
            SphericalVector s = (SphericalVector)E.MultiplyMV(r).getVectorInType(VectorType.SPHERICAL);
            this.planetocentricLatitude = s.theta;
            this.longitude = s.phi;
            if (sense == Sense.DIRECT) this.longitude = -1.0 * this.longitude;
            this.longitude = PhysicOrientation.modulo(this.longitude, 6.283185307179586D);
            this.latitude = Math.atan(Math.tan(this.planetocentricLatitude) / ((1.0D - f) * (1.0D - f)));
        }
    }
}
