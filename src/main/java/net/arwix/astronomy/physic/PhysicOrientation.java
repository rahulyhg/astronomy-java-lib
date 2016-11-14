package net.arwix.astronomy.physic;

import net.arwix.astronomy.VSOP87.VSOP87Objects;
import net.arwix.astronomy.coordinates.Matrix;

public abstract class PhysicOrientation {

	public enum Sense {
		RETROGRADE, DIRECT
	}

	public enum SystemType {
		I, II, III
	}

	public static final int RETROGRADE = -1;
	public static final int DIRECT = 1;

	public PhysicOrientation() {
	}

	// направление вращения
	public static Sense getSense(VSOP87Objects object) {
		switch (object) {
		case Venus:
			return Sense.RETROGRADE;
		case Uranus:
			return Sense.RETROGRADE;
		default:
			return Sense.DIRECT;
		}
	}

	public static Matrix getOrientation(VSOP87Objects object, double T, SystemType systemType) {
		double d = 36525.0D * T;
		double w;
		switch (object) {
		case Sun:
			return getOrientationFromDegress(286.13, 63.87, 84.182 + 14.1844 * d);
		case Mercury:
			return getOrientationFromDegress(281.01 - 0.033 * T, 61.45 - 0.005 * T, 329.68 + 6.1385025 * d);
		case Venus:
			return getOrientationFromDegress(272.76, 67.16, 160.2D - 1.4813688D * d);
		case Earth:
			return getOrientationFromDegress(-0.641 * T, 90.0 - 0.557 * T, 190.16 + 360.9856235 * d);
		case Mars:
			return getOrientationFromDegress(317.681 - 0.108 * T, 52.886 - 0.061 * T, 176.901 + 350.891983 * d);
		case Jupiter:
			switch (systemType) {
			case II:
				w = 43.30 + 870.27 * d;
				break;
			case III:
				w = 284.695 + 870.536 * d;
				break;
			default:
				w = 67.1 + 877.9 * d;
				break;
			}
			return getOrientationFromDegress(268.05 - 0.009 * T, 64.49 + 0.003 * T, w);
		case Saturn:
			switch (systemType) {
			case II:
				w = 227.2037 + 844.3000000 * d;
				break;
			case III:
				w = 38.90 + 810.7939024 * d;
				break;
			default:
				w = 227.2037 + 844.3000000 * d;
				break;
			}
			return getOrientationFromDegress(40.589 - 0.036 * T, 83.537 - 0.004 * T, w);
		case Uranus:
			return getOrientationFromDegress(257.311, -15.175, 203.81 - 501.1600928 * d);

		case Neptune:
			double n = Math.toRadians(357.85 + 52.316 * T);
			return getOrientationFromDegress(299.36 + 0.70 * Math.sin(n), 43.46 - 0.51 * Math.cos(n),
					253.18 + 536.3128492 * d - 0.48 * Math.sin(n));

		}
		return null;
	}

	protected static Matrix getOrientation(double RA, double Dec, double W) {
		return Matrix.RotateZ(W)
				.Multiply(Matrix.RotateX(Math.PI / 2.0 - Dec).Multiply(Matrix.RotateZ(Math.PI / 2.0 + RA)));
	}

	protected static Matrix getOrientationFromDegress(double RA, double Dec, double W) {
		return getOrientation(Math.toRadians(RA), Math.toRadians(Dec), Math.toRadians(modulo(W, 360)));
	}

	private static double frac(double x) {
		return x - Math.floor(x);
	}

	static double modulo(double x, double y) {
		return y * frac(x / y);
	}
}
