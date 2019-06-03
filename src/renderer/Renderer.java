package renderer;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import objects.DimensionTool;
import objects.Entity;
import objects.Player;
import objects.SkyBox;
import objects.Target;
import objects.terrain.Terrain;
import util.ColorRGBA;

/**
 * Renderer class for the project
 * 1 GL unit = 1 Real World meter
 * @author Jordan Carter - 1317225
 */
public class Renderer implements GLEventListener {

	public boolean wireframe;

	private GLCanvas canvas;
	private InputController ic;
    private TrackingCamera camera;

	public float spotlightPosition[] = {6.0f, 1.0f, 6.0f, 1};
	
	// Keep a register of all the objects in the scene
	private ArrayList<Entity> sceneEntityList;

	// World Objects
	Player player;
	Terrain terrain;
	SkyBox skyBox;
	Target target;

	// Debug Objects
	DimensionTool dTool;

	/**
	 * Constructor for renderer
	 * @param canvas
	 */
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
		Settings.deltaTime = (_tick - Settings.prevTick) * Settings.speedModifier;
		Settings.prevTick = _tick;
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

		// Execute any player controls
		ic.triggerActions();

		// Camera
		camera.draw(gl);
		// Lights
		target.updateLightPosition();

		skyBox.draw(gl);
		terrain.draw(gl);
		target.draw(gl);
		player.draw(gl);
		player.drawBullets(gl);
		
		if (Settings.DEBUG) {
			// Clear the depth buffer to render the axis/dimension tool ontop of everything else
			gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
			dTool.draw(gl);
		}

		// Flush before ending frame
        gl.glFlush();
    }

	private void setupFog(GL2 gl) {
		float fogColor[] = {0.4f, 0.4f, 0.4f, 1f};
		gl.glEnable(GL2.GL_FOG);
		
		gl.glFogfv(GL2.GL_FOG_COLOR, fogColor, 0);
		gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP2);
		gl.glFogf(GL2.GL_FOG_DENSITY, 0.01f);
		gl.glFogf(GL2.GL_FOG_START, 250.0f);
		gl.glFogf(GL2.GL_FOG_END, 300.0f);
	}
    

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        Settings.gl = gl;
		ic = new InputController(canvas, this);
        // Enable V-Sync
        gl.setSwapInterval(1);
        
        // Setup the drawing area and shading
		gl.glClearColor(0.4f, 0.4f, 0.4f, 1f);
        gl.glShadeModel(GL2.GL_SMOOTH);
        
        // Enable depth testing
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		// Enable textures
		gl.glEnable(GL2.GL_TEXTURE_2D);

        // intialise the camera
		this.camera = new TrackingCamera(canvas);

		setupFog(gl);

		//use the lights
		this.lights(gl);
		
		sceneEntityList.add(dTool = new DimensionTool(gl));
		skyBox = new SkyBox("resources\\SkyBox\\", "SkyBox.obj", Settings.gl);
		skyBox.animated = true;
		
		sceneEntityList.add(player = new Player(camera));
		sceneEntityList.add(terrain = new Terrain(200, 10.0, new ColorRGBA(61.0, 118.0, 40.0, 1.0), player));
		skyBox.player = player;
		skyBox.enable();

		sceneEntityList.add(target = new Target("resources\\Target\\", "target.obj", Settings.gl, player, terrain));
		enableScene();

		// Use Texture Mip-Mapping
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
	}
	
	/**
	 * Enables all object in the scene list
	 */
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
		//normalise the surface normals for lighting calculations
		gl.glEnable(GL2.GL_NORMALIZE);
		
		float[] ambientLight = { 0.1f, 0.1f, 0.1f, 1f };  // dark gray ambient light
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambientLight, 0); 

		float spotLightAmbient[] = {1.0f, 0f, 0f, 1 };
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, spotLightAmbient, 0);
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, 30.0f);
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_EXPONENT, 60.0f);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_LIGHT2);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);
	}

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();

		// avoid a divide by zero error when calculating aspect ratio
		height = (height <= 0) ? 1 : height;
        final float h = (float) width / (float) height;
        Settings.windowHeight = height;
        Settings.windowWidth = width;
		Main.settings.updateResolution();
		
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