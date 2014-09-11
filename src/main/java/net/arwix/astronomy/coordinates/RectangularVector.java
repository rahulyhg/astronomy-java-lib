package net.arwix.astronomy.coordinates;

public class RectangularVector extends Vector {

    public double x;
    public double y;
    public double z;

    public RectangularVector() {
        this(0.0, 0.0, 0.0);
    }

    public RectangularVector(Vector vector) {
        set(vector);
    }

    public RectangularVector(double x, double y, double z) {
        set(x, y, z);
    }

    public RectangularVector(double[] array) {
        set(array[0], array[1], array[2]);
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void set(Vector vector) {
        RectangularVector rectangularVector = (RectangularVector) (
                vector.getType() == VectorType.RECTANGULAR ? vector : convert(vector, VectorType.RECTANGULAR));
        set(rectangularVector.x, rectangularVector.y, rectangularVector.z);
    }

    @Override
    public VectorType getType() {
        return VectorType.RECTANGULAR;
    }

    public double[] toArray() {
        return new double[]{this.x, this.y, this.z};
    }

}