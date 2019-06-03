package renderer;

import com.jogamp.opengl.GL2;

public class Settings {

	// Debug flag
	public static boolean DEBUG = true;
	
	// View Settings
	// 1.0 GL units = 1.0m
	public static double viewDistance = 850.0; // View distance is set slightly further than the furthest point of the sky box

	// Window size fields
	public static int windowWidth = 1920 / 2;
	public static int windowHeight = (1080 / 2);
    public static double aspectRatio;

	// OpenGL Fields
    public static GL2 gl;

	// Animator Fields
	public static int FPS = 60;
    public static double deltaTime, prevTick;
	public static double speedModifier = 1.0;

	// Generic Material Settings
	public static float zeroMaterial[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	public static float redAmbient[] = { 0.3f, 0.0f, 0.0f, 1.0f };
	public static float blueDiffuse[] = { 0.1f, 0.5f, 0.8f, 1.0f };
	public static float redDiffuse[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	public static float whiteSpecular[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	public static float redEmissive[] = { 0.9f, 0.0f, 0.0f, 1.0f };

	public static float noShininess[] = { 0.0f }; // no highlight
	public static float highShininess[] = { 20.0f }; // small, sharp highlight

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

	public void updateResolution() {
		aspectRatio = (double)windowWidth / (double)windowHeight;
	}
}