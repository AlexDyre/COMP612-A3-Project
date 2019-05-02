package util;

/**
 * Class defining Vector2
 * @author Jordan Carter - 1317225
 *
 */
public class Vector2 {
	
	public double x, y;
	
	/**
	 * Initialises a new Vertex2d with x = x, y = y
	 * @param x double x pos
	 * @param y double y pos
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initialises a new Vertex2d with x = 0.0, y = 0.0
	 */
	public Vector2() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}
}
