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


public class TrackingCamera extends Entity implements MouseListener, MouseMotionListener, MouseWheelListener {

    public Player player;

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
    	canvas.addMouseListener(this);
    	canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
    }

    public void update() {


        //pos = player.pos;

        pos = new Vector3(player.pos.x + cameraOffset.x, player.pos.y + cameraOffset.y, player.pos.z + cameraOffset.z);
        
        //pos.x = player.pos.x + cameraOffset.x + Math.sin(Math.toRadians(player.rotation.x));
        //pos.y = player.pos.y + cameraOffset.y + Math.sin(Math.toRadians(player.rotation.y));
        //pos.z = player.pos.z + cameraOffset.z + Math.cos(Math.toRadians(player.rotation.x));

        double r = distanceToOrigin * Math.cos(Math.toRadians(rotation.y));
		
		target.x = pos.x + (r * Math.sin(Math.toRadians(rotation.x)));
		target.y = pos.y + (r * Math.sin(Math.toRadians(rotation.y)));
		target.z = pos.z + (r * Math.cos(Math.toRadians(rotation.x)));
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
        // setting up perspective projection
        // far distance is hardcoded to 3*cameraDistance. If your scene is bigger,
        // you might need to adapt this
        // default
        glu.gluPerspective(fieldOfView, Settings.aspectRatio, 0.1, Settings.viewDistance);

        // then set up the camera position and orientation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        update(); // Update transforms and targets

        //System.out.println("POS: " + pos);
        //System.out.println("TARGET: " + target);

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
     * Sets up the lookAt point
     * @param x X coordinate of the lookAt point
     * @param y Y coordinate of the lookAt point
     * @param z Z coordinate of the lookAt point
     */
    public void setLookAt(double x, double y, double z) {
        //lookAt = new double[]{x, y, z};
    }

    /**
     * Resets the camera rotations.
     */
    public void reset() {
        rotation.x = rotation.y = 0;
        oldMousePos = null;
    }

    public void setAngle(double x, double y) {
        rotation.x = x;
        rotation.y = y;
        oldMousePos = null;
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

                rotation.x -= p.x - oldMousePos.x;
                rotation.y += p.y - oldMousePos.y;

                // limit Y rotation angle to avoid gimbal lock
                rotation.y = Math.min(89.9, Math.max(-89.9, rotation.y));
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
            fieldOfView *= 1.1;
            clicks--;
        }
        while (clicks < 0) {
            fieldOfView /= 1.1;
            clicks++;
        }
        limitFieldOfView();
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {

    }

    @Override
    public void drawObject(GL2 gl) {
        
    }

  
}
