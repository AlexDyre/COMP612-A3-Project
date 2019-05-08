package renderer;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Main class for the 3D animated fish tank
 * @author Jordan Carter - 1317225
 */
public class Main {
	public static Renderer renderer;
	public static Settings settings;

	public static void main(String[] args) {
		settings = new Settings();
		Settings.deltaTime = 0.0;
		Settings.speedModifier = 1.0;
		//Settings.windowHeight = 400;
		//Settings.windowWidth = 400;

		if (Settings.DEBUG)
			System.out.println("Initialising Project");
		
		Frame frame = new Frame("COMP612 Project/A3");
		GLCanvas canvas = new GLCanvas();
		renderer = new Renderer(canvas);
		
		canvas.addGLEventListener(renderer);
		frame.add(canvas);
		frame.setSize(Settings.windowWidth, Settings.windowHeight);
		
		final FPSAnimator animator = new FPSAnimator(canvas, Settings.FPS);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.requestFocus();
		animator.start();
	}
}
