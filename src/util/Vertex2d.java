package util;

/**
 * 
 * @author Jordan Carter - 1317225
 *
 */
public class Vertex2d {
	
	public double x, y;
	
	/**
	 * Initialises a new Vertex2d with x = x, y = y
	 * @param x double x pos
	 * @param y double y pos
	 */
	public Vertex2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initialises a new Vertex2d with x = 0.0, y = 0.0
	 */
	public Vertex2d() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}
}
