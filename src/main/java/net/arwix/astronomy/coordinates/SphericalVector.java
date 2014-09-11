package net.arwix.astronomy.coordinates;

public class SphericalVector extends Vector {

    /**
     * азимут вектора
     * или долгота
     */
    public double phi;
    /**
     * высота вектора
     * или широта
     */
    public double theta;
    /**
     * длина вектора
     */
    public double r;

    public SphericalVector() {
        this(0.0, 0.0, 0.0);
    }

    public SphericalVector(Vector vector) {
        set(vector);
    }

    /**
     * @param phi   азимут или долгота
     * @param theta высота или широта
     *              радиус равен 1.0
     */
    public SphericalVector(double phi, double theta) {
        set(phi, theta, 1.0);
    }

    /**
     * @param phi   азимут или долгота
     * @param theta высота или широта
     * @param r     длина
     */
    public SphericalVector(double phi, double theta, double r) {
        set(phi, theta, r);
    }

    public void set(double phi, double theta, double r) {
        this.phi = phi;
        this.theta = theta;
        this.r = r;
    }

    @Override
    public void set(Vector vector) {
        SphericalVector sphericalVector = (SphericalVector) (
                vector.getType() == VectorType.SPHERICAL ? vector : convert(vector, VectorType.SPHERICAL));
        set(sphericalVector.phi, sphericalVector.theta, sphericalVector.r);
    }

    @Override
    public VectorType getType() {
        return VectorType.SPHERICAL;
    }
}