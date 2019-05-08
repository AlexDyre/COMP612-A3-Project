package renderer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import objects.terrain.Terrain;
import util.ColorRGBA;
import util.Vector3;
import util.obj.ObjObject;

public class Renderer implements GLEventListener {

    private GLCanvas canvas;
    private TrackballCamera camera;
    private GL2 gl;


    // Test objects
    ObjObject testCube;
    Terrain terrain;

    public Renderer(GLCanvas canvas) {
        this.canvas = canvas;
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
        // Lights
		lights(gl);

        //System.out.println("Draw cube");
        testCube.draw(gl);

        //terrain
        terrain.draw(gl);

        // Flush before ending frame
        gl.glFlush();
    }

    

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        Settings.gl = gl;

        // Enable V-Sync
        gl.setSwapInterval(1);
        
        // Setup the drawing area and shading
		gl.glClearColor(0.9f, 0.9f, 0.9f, 1f);
        gl.glShadeModel(GL2.GL_SMOOTH);
        
        // Enable depth testing
        gl.glEnable(GL2.GL_DEPTH_TEST);
        // intialise the camera
        this.camera = new TrackballCamera(canvas);

        //use the lights
		this.lights(gl);

        testCube = new ObjObject("colorcube.obj", gl);
        testCube.scale = new Vector3(0.1, 0.1, 0.1);
        System.out.println(testCube);
        //testCube.draw(gl);
        terrain = new Terrain(20, 0.5, new ColorRGBA(0.0, 255.0, 0.0, 1.0));
    }

    /**
	 * Initialises the lights for the scene
	 * @param gl
	 */
	private void lights(GL2 gl) {
		// lighting stuff
		float ambient[] = { 0.3f, 0.3f, 0.3f, 1 };
		float diffuse[] = { 1f, 1f, 1f, 1 };
		float specular[] = { 1, 1, 1, 1 };
		
		float[] ambientLight = { 0.1f, 0.1f, 0.1f, 1f };  // weak RED (this is really a white light?) ambient 
		gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_AMBIENT, ambientLight, 0); 
		
		float position0[] = { 5, 5, 5, 0 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
		
		float position1[] = { -10, -10, -10, 0 };
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specular, 0);
		
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);
	
		//lets use use standard color functions
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		//normalise the surface normals for lighting calculations
		gl.glEnable(GL2.GL_NORMALIZE);
		
	}

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
		//height = (height == 0) ? 1 : height;

		// avoid a divide by zero error when calculating aspect ratio
		height = (height <= 0) ? 1 : height;
        final float h = (float) width / (float) height;
        Settings.windowHeight = height;
        Settings.windowWidth = width;
        Main.settings.updateResolution();

		// specify the affine transformation of x and y from normalized device
		// coordinates to window coordinates

		gl.glViewport(0, 0, width, height);

		// switch to projection mode
		gl.glMatrixMode(GL2.GL_PROJECTION);
		// Reset the current matrix to the "identity"
		gl.glLoadIdentity();
		// gluPerspective(FOV, aspect ratio, near, far)
		glu.gluPerspective(45.0f, h, 1.0, 40.0);
		// finished modifying projection matrix switch back to model_view matrix
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// Reset the current matrix to the "identity"
		gl.glLoadIdentity();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

}