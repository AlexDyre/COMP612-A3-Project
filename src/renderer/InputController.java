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

    private double inputSensitivity = 2;

    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean fire = false;

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
        System.out.println("Space: Fire Gun");
        System.out.println("W: Pitch Plane Down");
        System.out.println("S: Pitch Plane Up");
        System.out.println("A: Turn Plane Left");
        System.out.println("D: Turn Plane Right");
        System.out.println("1: Slow Speed");
        System.out.println("2: Normal Speed");
        System.out.println("3: Fast Speed");
        System.out.println("4: Very Fast Speed");
        System.out.println("~: Toggle Wireframe");
    }

    public void triggerActions() {
        if (up)
            renderer.player.rotation.z += 0.5 * inputSensitivity;
        if (down)
            renderer.player.rotation.z -= 0.5 * inputSensitivity;
        if (left) {
            renderer.player.rotation.y += 0.5 * inputSensitivity;
            renderer.player.plane.rotation.x = -15;
        }
        if (right) {
            renderer.player.rotation.y -= 0.5 * inputSensitivity;
            renderer.player.plane.rotation.x = 15;
        }
        if (fire)
            renderer.player.fireGun();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            down = true;
        } else if (key == KeyEvent.VK_S) {
            up = true;
        }
        if (key == KeyEvent.VK_A) {
            left = true;
        } else if (key == KeyEvent.VK_D) {
            right = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            fire = true;
        }
        if (key == KeyEvent.VK_BACK_QUOTE) {
            renderer.toggleWireframe();
        }
        if (key == KeyEvent.VK_1) {
            Settings.speedModifier = Settings.Speed.SLOW.speed;
        }
        if (key == KeyEvent.VK_2) {
            Settings.speedModifier = Settings.Speed.NORMAL.speed;
        }
        if (key == KeyEvent.VK_3) {
            Settings.speedModifier = Settings.Speed.FAST.speed;
        }
        if (key == KeyEvent.VK_4) {
            Settings.speedModifier = Settings.Speed.VFAST.speed;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            down = false;
        } else if (key == KeyEvent.VK_S) {
            up = false;
        }
        if (key == KeyEvent.VK_A) {
            left = false;
            renderer.player.plane.rotation.x = 0;
        } else if (key == KeyEvent.VK_D) {
            right = false;
            renderer.player.plane.rotation.x = 0;
        }
        if (key == KeyEvent.VK_SPACE) {
            fire = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}