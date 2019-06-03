package renderer;

//package <your package>;
/**
 * Class for a simple camera that rotates around the point of view
 * using mouse dragging.
 *
 * @author Jacqueline Whalley
 */

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import objects.Entity;
import objects.Player;
import util.Vector3;


public class TrackingCamera implements MouseListener, MouseMotionListener, MouseWheelListener {

    public Player player;

    public double cameraDistance = 12.0;
    public double angleAroundPlane = 90.0;
    public double pitchAroundPlane = 18.0;
    public Vector3 pos;

    public boolean firstPerson = false;
    public Vector3 firstPersonOffset;

    // some hard limitations to camera values
    private static final double MIN_DISTANCE = 1;
    private static final double MIN_FOV = 1;
    private static final double MAX_FOV = 80;
    
    // the point to look at
    private Vector3 target = new Vector3(0,0,0);
    private Vector3 forward = new Vector3(0, 1, 0);
    private Vector3 side = new Vector3(1, 0, 0);

    public Vector3 cameraOffset = new Vector3();

    // old mouse position for dragging
    private Point oldMousePos;
    private int mouseButton;

    // camera parameters
    double fieldOfView      = 45;
    double distanceToOrigin = 5;
    double windowWidth      = 1;
    double windowHeight     = 1;

    // GLU context
    GLU glu = new GLU();

    /**
     * Constructor of the trackball camera
     * @param drawable the GL drawable context to register this camera with
     */
    public TrackingCamera(GLCanvas canvas) {
        this.pos = new Vector3();
    	canvas.addMouseListener(this);
    	canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
        this.firstPersonOffset = new Vector3();
    }


    public double calculateHorizontal() {
        return cameraDistance * Math.cos(Math.toRadians(pitchAroundPlane));
    }

    public double calculateVertical() {
        return cameraDistance * Math.sin(Math.toRadians(pitchAroundPlane));
    }

    public void calculateCameraPos(double horizontal, double vertical) {
        double theta = player.rotation.y + angleAroundPlane;

        double xPos = horizontal * Math.sin(Math.toRadians(theta));
        double zPos = horizontal * Math.cos(Math.toRadians(theta));

        pos.x = player.pos.x - xPos;
        pos.z = player.pos.z - zPos;
        pos.y = player.pos.y + vertical;
    }

    public void update() {


        double verticalDistance = calculateVertical();
        double horizontalDistance = calculateHorizontal();

        calculateCameraPos(horizontalDistance, verticalDistance);

        if (!firstPerson) {
            target = player.pos;
        } else {
            pos = new Vector3(player.pos.x + firstPersonOffset.x, player.pos.y + firstPersonOffset.y, player.pos.z + firstPersonOffset.z);
        }
        
    }

    /**
     * "Draws" the camera.
     * This sets up the projection matrix and
     * the camera position and orientation.
     * This method has to be called first thing
     * in the <code>display()</code> method
     * of the main program
     *
     * @param gl then OpenGL context to draw the camera in
     */
    public void draw(GL2 gl) {
        // set up projection first
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        // set up perspective projection
        glu.gluPerspective(fieldOfView, Settings.aspectRatio, 0.1, Settings.viewDistance);

        // then set up the camera position and orientation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        update(); // Update transforms and targets

        glu.gluLookAt(
            pos.x, pos.y, pos.z,                // eye
            target.x, target.y, target.z,       // center
            forward.x, forward.y, forward.z);   // up
    }

    /**
     * Gets the distance of the camera from the lookAt point
     * @return the distance of the camera from the lookAt point
     */
    public double getDistance() {
        return distanceToOrigin;
    }

    /**
     * Sets the distance of the camera to the lookAt point.
     * @param dist the new distance of the camera to the lookAt point
     */
    public void setDistance(double dist) {
        distanceToOrigin = dist;
        limitDistance();
    }

    /**
     * Limits the distance of the camera to valid values.
     */
    private void limitDistance() {
        if (distanceToOrigin < MIN_DISTANCE) {
            distanceToOrigin = MIN_DISTANCE;
        }
    }

    /**
     * Gets the field of view angle of the camera
     * @return the field of view of the camera in degrees
     */
    public double getFieldOfView() {
        return fieldOfView;
    }

    /**
     * Sets the field of view angle of the camera.
     * @param fov the new field of view angle of the camera in degrees
     */
    public void setFieldOfView(double fov) {
        fieldOfView = fov;
        limitFieldOfView();
    }

    /**
     * Limits the field of view angle to a valid range.
     */
    private void limitFieldOfView() {
        if (fieldOfView < MIN_FOV) {
            fieldOfView = MIN_FOV;
        }
        if (fieldOfView > MAX_FOV) {
            fieldOfView = MAX_FOV;
        }
    }

    /**
     * Passes a new window size to the camera.
     * This method should be called from the <code>reshape()</code> method
     * of the main program.
     *
     * @param width the new window width in pixels
     * @param height the new window height in pixels
     */
    public void newWindowSize(int width, int height) {
        windowWidth = Math.max(1.0, width);
        windowHeight = Math.max(1.0, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	//System.out.println("CLICK");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        oldMousePos = e.getPoint();
        mouseButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        oldMousePos = null;
        mouseButton = -0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        if (oldMousePos != null) {
            // dragging with left mouse button: rotate
            if (mouseButton == MouseEvent.BUTTON1) {

                //rotation.x -= p.x - oldMousePos.x;
                angleAroundPlane -= (p.x - oldMousePos.x) * 0.1;
                pitchAroundPlane += (p.y - oldMousePos.y) * 0.1;

                // limit Y rotation angle to avoid gimbal lock
                // TODO: look at rotation lock again
                //rotation.y = Math.min(89.9, Math.max(-89.9, rotation.y));
            } // dragging with right mouse button: change distance
            else if (mouseButton == MouseEvent.BUTTON3) {
                distanceToOrigin += 0.1 * (p.y - oldMousePos.y);
                limitDistance();
            }

        }
        oldMousePos = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        // zoom using the FoV
        while (clicks > 0) {
            cameraDistance *= 1.1;
            clicks--;
        }
        while (clicks < 0) {
            cameraDistance /= 1.1;
            clicks++;
        }
        limitFieldOfView();
    }
}
