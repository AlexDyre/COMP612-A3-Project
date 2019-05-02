package renderer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import util.obj.ObjObject;

public class Renderer implements GLEventListener {

    private GLCanvas canvas;
    private TrackballCamera camera;
    private GL2 gl;


    // Test objects
    ObjObject testCube;

    public Renderer(GLCanvas canvas) {
        this.canvas = canvas;
    }
    
    public void initRenderer() {

    }

    /**
     * Updates time
     */
    public void update() {
		double _tick = System.currentTimeMillis() / 1000.0;
		Settings.deltaTime = _tick - Settings.prevTick;
		Settings.prevTick = _tick;
	}

	@Override
    public void display(GLAutoDrawable drawable) {
        //System.out.println("Frame");
        // Update time
        update();

        GL2 gl = drawable.getGL().getGL2();
        // select and clear the model-view matrix
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_LINE_SMOOTH);
		gl.glEnable(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glCullFace(GL2.GL_BACK);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // Camera
		camera.draw(gl);

        testCube.draw(gl);

        // Flush before ending frame
        gl.glFlush();
    }

    

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Enable V-Sync
        gl.setSwapInterval(1);
        
        // Setup the drawing area and shading
		gl.glClearColor(0.9f, 0.9f, 0.9f, 1f);
        gl.glShadeModel(GL2.GL_SMOOTH);
        
        // Enable depth testing
        gl.glEnable(GL2.GL_DEPTH_TEST);
        // intialise the camera
        this.camera = new TrackballCamera(canvas);
        testCube = new ObjObject("colorcube.obj", gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        System.out.println("W: " + width + " H: " + height + " AR: " + width / (double) height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

}