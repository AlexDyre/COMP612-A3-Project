package util;

import java.util.Random;

/**
 * Math halper utility class
 * @author Jordan Carter - 1317225
 */
public class MathHelpers {
	
	/**
	 * Returns distance between two numbers
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static float distance(float p1, float p2) {
		return p2 - p1;
	}

	/**
	 * Linear interpolation between v0, v1 between t = 0.0 - 1.0 
	 * @param v0
	 * @param v1
	 * @param t
	 * @return
	 */
	public static float lerp(float v0, float v1, float t) {
		return (1.0f - t) * v0 + t * v1;
	}

	/**
	 * Linear interpolation between v0, v1 between t = 0.0 - 1.0 
	 * @param v0
	 * @param v1
	 * @param t
	 * @return
	 */
	public static double lerp(double v0, double v1, double t) {
		return (1.0 - t) * v0 + t * v1;
	}

	/**
	 * Inverse Linear interpolation between v0, v1 between t = 0.0 - 1.0 
	 * @param v0
	 * @param v1
	 * @param t
	 * @return
	 */
	public static double inverseLerp(double v0, double v1, double t) {
		t = 1 - t;
		return (1.0 - t) * v0 + t * v1;
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param rand
	 * @return
	 */
	public static Vertex2d randomPointInRectangle(Vertex2d p1, Vertex2d p2, Random rand) {
		double length = p2.x - p1.x;
		double height = p2.y - p1.y;
		double x = p2.x - (rand.nextDouble() * length);
		double y = p2.y - (rand.nextDouble() * height);
		
		return new Vertex2d(x, y);
	}
	
	/**
	 * Returns a random point in a quad
	 * @param points
	 * @param rand
	 * @return
	 */
	public static Vertex2d pointInQuad(Vertex2d[] points, Random rand) {
		Vertex2d[] tri = new Vertex2d[3];
		// choose either the top "triangle" or bottom from the set of 4 vertices
		if (rand.nextInt(2) > 0) {
			// top
			tri[0] = points[0];
			tri[1] = points[2];
			tri[2] = points[3];
		} else {
			// bottom
			tri[0] = points[0];
			tri[1] = points[1];
			tri[2] = points[2];
		}
		
		Vertex2d pointInQuad = pointInTriangle(tri, rand);
		return pointInQuad;
	}
	
	/**
	 * Returns a random point in a triangle
	 * @param tri
	 * @param rand
	 * @return
	 */
	public static Vertex2d pointInTriangle(Vertex2d[] tri, Random rand) {
		boolean valid = false;
		Vertex2d point = new Vertex2d();
		while (!valid) {
			
			if (tri[0].y < tri[1].y) {
				point = randomPointInRectangle(tri[0], tri[1], rand);
			} else {
				point = randomPointInRectangle(tri[0], tri[2], rand);
			}
			
			// check if point lies in triangle
			double s1 = tri[2].y - tri[0].y;
			double s2 = tri[2].x - tri[0].x;
			double s3 = tri[1].y - tri[0].y;
			double s4 = point.y - tri[0].y;

			double w1 = (tri[0].x * s1 + s4 * s2 - point.x * s1) / (s3 * s2 - (tri[1].x - tri[0].x) * s1);
			double w2 = (s4- w1 * s3) / s1;
			valid = w1 >= 0 && w2 >= 0 && (w1 + w2) <= 1;
		}
		
		return point;
	}
	
	/**
	 * Checks if a point lies inside a triangle
	 * @param tri
	 * @param point
	 * @return
	 */
	public static boolean pointIsInTriangle(Vertex2d[] tri, Vertex2d point) {
		boolean valid = false;
		// check if point lies in triangle
		double s1 = tri[2].y - tri[0].y;
		double s2 = tri[2].x - tri[0].x;
		double s3 = tri[1].y - tri[0].y;
		double s4 = point.y - tri[0].y;

		double w1 = (tri[0].x * s1 + s4 * s2 - point.x * s1) / (s3 * s2 - (tri[1].x - tri[0].x) * s1);
		double w2 = (s4- w1 * s3) / s1;
		valid =  w1 >= 0 && w2 >= 0 && (w1 + w2) <= 1;
		return valid;
	}

	/**
	 * Returns the area of a triangle
	 * @param p Vertex2d array (3 points)
	 * @return area of triangle
	 */
	public static double area(Vertex2d[] p) {
		return Math.abs((p[0].x * (p[1].y - p[2].y) + p[1].x * (p[2].y - p[0].y) + p[2].x * (p[0].y - p[1].y)) / 2.0);
	}
}
