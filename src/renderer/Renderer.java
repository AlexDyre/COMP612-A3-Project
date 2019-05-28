package renderer;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import objects.DimensionTool;
import objects.Entity;
import objects.IndexedObject;
import objects.Player;
import objects.SkyBox;
import objects.terrain.Terrain;
import util.ColorRGBA;
import util.Vector3;
import util.obj.ObjObject;

public class Renderer implements GLEventListener {

	public boolean wireframe;

	private GLCanvas canvas;
	private InputController ic;
    private TrackingCamera camera;
	private GL2 gl;
	
	// Keep a register of all the objects in the scene
	private ArrayList<Entity> sceneEntityList;

	// World Objects
	Player player;

    // Test objects
    ObjObject testCube, plane;
	Terrain terrain;
	SkyBox skyBox;
	DimensionTool dTool;
	IndexedObject tree;

    public Renderer(GLCanvas canvas) {
		this.canvas = canvas;
		this.sceneEntityList = new ArrayList<Entity>();
		this.wireframe = false;
		Settings.prevTick = -1;
    }

    /**
     * Updates time
     */
    public void update() {
		
		if (Settings.prevTick == -1) {
			Settings.prevTick = System.currentTimeMillis() / 1000.0;
		}

		double _tick = System.currentTimeMillis() / 1000.0;
		Settings.deltaTime = _tick - Settings.prevTick;
		Settings.prevTick = _tick;
		//System.out.println("Delta Time: " + Settings.deltaTime);
	}

	/**
	 * Toggles wireframe mode
	 */
	public void toggleWireframe() {
		if (Settings.DEBUG)
			System.out.println("Wireframe toggled");
		wireframe = (wireframe) ? false : true;
	}

	@Override
    public void display(GLAutoDrawable drawable) {
        // Update time parameters
		update();
		
		// select and clear the model-view matrix
        GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_LINE_SMOOTH);
		gl.glEnable(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glCullFace(GL2.GL_BACK);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		if (wireframe) {
			gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
		} else {
			gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		}

        
        // Lights
		lights(gl);
		// Camera
		camera.draw(gl);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		skyBox.draw(gl);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		// Draw the player, and all player objects
		
		testCube.draw(gl);
		//plane.draw(gl);
		tree.draw(gl);

        //terrain
		terrain.draw(gl);



		player.draw(gl);
		

		// Clear the depth buffer to render the axis/dimension tool ontop of everything else
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		dTool.draw(gl);

		// Flush before ending frame\
        gl.glFlush();
    }

	private void setupFog(GL2 gl) {
		float fogColor[] = {1, 1, 1, 1};
		gl.glEnable(GL2.GL_FOG);
		//gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);
		
		gl.glFogfv(GL2.GL_FOG_COLOR, fogColor, 0);
		//gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
		//gl.glFogf(GL2.GL_FOG_START, 1.0f);
		//gl.glFogf(GL2.GL_FOG_END, 10.0f);
		//gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_EXP);
		gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP2);
		gl.glFogf(GL2.GL_FOG_DENSITY, 0.01f);
		gl.glFogf(GL2.GL_FOG_START, 150.0f);
		gl.glFogf(GL2.GL_FOG_END, 200.0f);
	}
    

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        Settings.gl = gl;
		ic = new InputController(canvas, this);
        // Enable V-Sync
        gl.setSwapInterval(1);
        
        // Setup the drawing area and shading
		gl.glClearColor(0.9f, 0.9f, 0.9f, 1f);
        gl.glShadeModel(GL2.GL_SMOOTH);
        
        // Enable depth testing
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		// Enable textures
		gl.glEnable(GL2.GL_TEXTURE_2D);

        // intialise the camera
		this.camera = new TrackingCamera(canvas);
		
		// TODO: Fog should be re-enabled
		setupFog(gl);

        //use the lights
		this.lights(gl);

		
		sceneEntityList.add(dTool = new DimensionTool(gl));
		sceneEntityList.add(testCube = new ObjObject("resources\\", "colorcube.obj", gl));
		testCube.pos.y = 0.5;
		skyBox = new SkyBox("resources\\SkyBox\\", "SkyBox.obj", Settings.gl);
		skyBox.animated = true;
		
		sceneEntityList.add(player = new Player(camera));
		sceneEntityList.add(terrain = new Terrain(200, 10.0, new ColorRGBA(61.0, 118.0, 40.0, 1.0), player));
		skyBox.player = player;
		skyBox.enable();
		
		sceneEntityList.add(tree = new IndexedObject(new ObjObject("resources\\", "tree.obj", Settings.gl).triDisplayList));
		tree.pos.x = 1;
		tree.pos.z = 1;
		enableScene();
	}
	
	private void enableScene() {
		
		double waitTime = 5.0 + (System.currentTimeMillis() / 1000.0);

		// Wait for a brief period so the entities can initialise before enabling
		while (System.currentTimeMillis() < waitTime) {}

		for (Entity e : sceneEntityList) {
			e.enable();
		}
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