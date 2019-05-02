package util;

/**
 * Class defining color holding RGB
 * @author Jordan Carter - 1317225
 */
public class ColorRGB {
	public double red, blue, green;
	
	/**
	 * Default constructor for RGB, initialises all colors to max (white)
	 */
	public ColorRGB() {
		this.red = 1.0;
		this.green = 1.0;
		this.blue = 1.0;
	}
	
	/**
	 * Constructor for RGB
	 * @param red
	 * @param green
	 * @param blue
	 */
	public ColorRGB(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	@Override
	public String toString() {
		return "R: " + red + " G: " + green + " B: " + blue;
	}
}
