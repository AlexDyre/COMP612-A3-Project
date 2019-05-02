package util;

/**
 * Class Defining Vector3
 * @author Jordan Carter - 1317225
 *
 */
public class Vector3 {
	
	public double x, y, z;
	
	/**
	 * Initialises a new Vector3 with x = x, y = y, z = z
	 * @param x double x pos
	 * @param y double y pos
	 * @param z double z pos
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Initialises a new Vector3 with x = 0.0, y = 0.0, z = 0.0
	 */
	public Vector3() {
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}
	
	/**
	 * Constructor creating a copied Vector3
	 */
	public Vector3(Vector3 vector3) {
		this.x = vector3.x;
		this.y = vector3.y;
		this.z = vector3.z;
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y + " z: " + z;
	}
	
	/**
	 * Multiplies vector by x
	 * @param x
	 */
	public void multiply(double x) {
		this.x *= x;
		this.y *= x;
		this.z *= x;
	}
	
	/**
	 * Returns length of the vector
	 * @return
	 */
	public double length() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Normalises the vector
	 */
	public void normalise() {
		double length = this.length();
		x /= length;
		y /= length;
		z /= length;
	}
	
	/**
	 * Returns a new normalised vector 3 of this vector
	 * @return Vector3
	 */
	public Vector3 normalised() {
		Vector3 vector3 = new Vector3(this);
		vector3.normalise();
		return vector3;
	}
	
	/**
	 * Method not implemented
	 * @param target
	 * @param distance
	 */
	public void move(Vector3 target, double distance) {
		System.err.println("Vector3.move not implemented");
	}

	/**
	 * Returns distance from self to other vector
	 * @param other
	 * @return
	 */
	public double distance(Vector3 other) {
		double dx = other.x - x;
		double dy = other.y - y;
		double dz = other.z - z;
		return Math.sqrt((dx*dx) + (dy*dy) + (dz*dz));
	}

	/**
	 * Checks if vector is equal to other
	 * @param other
	 * @return
	 */
	public boolean equals(Vector3 other) {
		if (x == other.x && y == other.y && z == other.z) {
			System.err.println("Point is the same");
			System.out.println(this);
			System.out.println(other);
		}

		return  (x == other.x && y == other.y && z == other.z);
	}

	/**
	 * Returns a new vector by adding this vector to other
	 * @param other
	 * @return
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Lerps this vector towards target by distance
	 * @param target
	 * @param distance
	 */
	public void lerp(Vector3 target, double distance) {
		x += (target.x - x) * distance;
		y += (target.y - y) * distance;
		z += (target.z - z) * distance;
	}

	/**
	 * Returns dot product of vector
	 * @param other
	 * @return
	 */
	public double dot(Vector3 other) {
		return (x * z) + (other.x * other.z);
	}

	/**
	 * Returns magnitude of vector
	 * @return
	 */
	public double mag() {
		return Math.sqrt((x*x) + (z*z));
	}

	/**
	 * Returns a Y angle from one vector to other
	 * @param other
	 * @return
	 */
	public double angle(Vector3 other) {
		double angle = Math.atan2(other.z - z, other.x - x);

		return Math.toDegrees(angle);
	}
}
