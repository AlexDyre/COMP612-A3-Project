package util;

import com.jogamp.opengl.GL2;

import renderer.Settings;

/**
 * Class defining a color object for RGB+A
 * @author Jordan Carter - 1317225
 */
public class ColorRGBA {
	public double red, blue, green, alpha;
	
	/**
	 * Default constructor
	 */
	public ColorRGBA() {
		this.red = 1.0;
		this.green = 1.0;
		this.blue = 1.0;
		this.alpha = 1.0;
	}
	
	/**
	 * Constructor for RGBA
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public ColorRGBA(double red, double green, double blue, double alpha) {
		this.red = red / 255;
		this.green = green / 255;
		this.blue = blue / 255;
		this.alpha = alpha;
	}

	/**
	 * Sets the GL renderer to use the colors defined color representation
	 */
	public void set() {
		Settings.gl.glColor4d(red, green, blue, alpha);
		//float[] color = {(float) red, (float) green, (float) blue, (float) alpha};
		//System.out.println("Color G is: " + green);
		//Settings.gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, color, 0);
	}
	
	@Override
	public String toString() {
		return "R: " + red + " G: " + green + " B: " + blue + " A: " + alpha;
	}
}
