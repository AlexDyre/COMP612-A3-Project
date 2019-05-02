package util;

/**
 * Class defining a quaternion
 * @author Jordan Carter - 1317225
 */
public class Quaternion {
    public double r, x, y, z;

    /**
     * Default constructor, initialises all values to 0
     */
    public Quaternion() {
        this.r = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Constructor generating a new quaternion for given euler angles
     * @param x degrees of rotation across x-axis
     * @param y degrees of rotation across y-axis
     * @param z degrees of rotation across z-axis
     */
    public Quaternion(Vector3 euler) {
        this();
        rotateEuler(euler);
    }

    /**
     * Constructor creating a quaternion with given values (May not be mathmatically correct)
     * @param r
     * @param x
     * @param y
     * @param z
     */
    public Quaternion(double r, double x, double y, double z) {
        this.r = r;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Calculates rotation quaternion based on euler angles
     * @param euler
     */
    public void rotateEuler(Vector3 euler) {
        r = euler.x + euler.y + euler.z;
        // Need to check if R is equal to 0
        // If r is equal to zero, rotation values will not be handled correctly on integrated graphics due to a divide by zero error
        if (r != 0) {
            x = euler.x / r;
            y = euler.y / r;
            z = euler.z / r;
        } else {
            x = 0.0;
            y = 0.0;
            z = 0.0;
        }
    }

    @Override
    public String toString() {
        return "" + x+ ", " + y + ", " + z+ ", "+ r;
    }
}