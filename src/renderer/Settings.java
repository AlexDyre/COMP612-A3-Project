package renderer;

import com.jogamp.opengl.GL2;

public class Settings {

	// Debug flag
    public static boolean DEBUG = false;

	// Window size fields
	public static int windowWidth = 1920 / 2;
	public static int windowHeight = (1080 / 2);
    public static double aspectRatio = (double)windowWidth / (double)windowHeight;

	// OpenGL Fields
    public static GL2 gl;

	// Animator Fields
	public static int FPS = 60;
    public static double deltaTime, prevTick;
	public static double speedModifier;

    /**
     * Animation Speed Enum
     */
    public enum Speed {
		PAUSE (0.0),
		SLOW (0.25),
		NORMAL (1.0),
		FAST (2.5),
		VFAST (10.0);

		public double speed;

		Speed(double speed) {
			this.speed = speed;
		}
	}
}