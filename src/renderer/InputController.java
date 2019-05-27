package renderer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.awt.GLCanvas;

/**
 * Input controller class
 * @author Jordan Carter - 1317225
 */
public class InputController implements KeyListener {

    private Renderer renderer;

    private double inputSensitivity = 5;

    /**
     * Constructor for the input controller
     * @param canvas
     * @param renderer
     */
    public InputController (GLCanvas canvas, Renderer renderer) {
        this.renderer = renderer;
        canvas.addKeyListener(this);
        printControls();
    }

    /**
     * Prints the controls handled by the input controller
     */
    public void printControls() {
        System.out.println("Key mapping:\n--------------------------------------------");
        System.out.println("Space: Pause");
        System.out.println("1: Slow Speed");
        System.out.println("2: Normal Speed");
        System.out.println("3: Fast Speed");
        System.out.println("~: Toggle Wireframe");
        System.out.println("Arrow Keys: Set Camera Orientation");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
		
		switch (key) {
		case KeyEvent.VK_1:
            System.out.println("Slow Speed");
            Settings.speedModifier = Settings.Speed.SLOW.speed;
            break;
        case KeyEvent.VK_2:
            System.out.println("Normal Speed");
            Settings.speedModifier = Settings.Speed.NORMAL.speed;
            break;
        case KeyEvent.VK_3:
            System.out.println("Fast Speed");
            Settings.speedModifier = Settings.Speed.FAST.speed;
			break;
        case KeyEvent.VK_SPACE:
            System.out.println("Paused animation");
            Settings.speedModifier = Settings.Speed.PAUSE.speed;
            break;
        case KeyEvent.VK_BACK_QUOTE:
            renderer.toggleWireframe();
            break;
        case KeyEvent.VK_W:
            renderer.player.rotation.z -= 0.5 * inputSensitivity;
            
            break;
        case KeyEvent.VK_S:
            renderer.player.rotation.z += 0.5 * inputSensitivity;
            break;
        case KeyEvent.VK_A:
            renderer.player.rotation.y += 0.5 * inputSensitivity;
            //renderer.player.rotation.x += 0.5 * inputSensitivity;
            break;
        case KeyEvent.VK_D:
            renderer.player.rotation.y -= 0.5 * inputSensitivity;
            //renderer.player.rotation.x -= 0.5 * inputSensitivity;
            break;
		default:
			break;
		}
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}