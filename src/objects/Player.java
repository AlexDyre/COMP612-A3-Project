package objects;

import com.jogamp.opengl.GL2;

import renderer.Settings;
import renderer.TrackingCamera;
import util.Vector3;
import util.obj.ObjObject;

/**
 * Player class acts as a parent object for all player components to attach to
 */
public class Player extends Entity {

    //public SkyBox skyBox;
    public ObjObject plane;
    public ObjObject planeProp;
    public TrackingCamera cam;

    private boolean outside = false;
    private boolean fixingRotation = false;
    private boolean bellowGround = false;
    private boolean aboveArea = false;

    private Vector3 cameraOffset = new Vector3(0, 6, 12);
    //private Vector3 cameraOffsetFirstPerson = new Vector3(0, 2, 2 );

    private Vector3 forward = new Vector3(0, 0, 1);
    //private Vector3 zero = new Vector3(0,0,0);
    private double lastSecond;
    private double startPos;

    private double planeSpeed = 60;
    private double playAreaSize = 100;

    public Player(TrackingCamera cam) {
        super();
        this.rotation.y = 0.0;
        this.cam = cam;
        this.cam.player = this;
        
        // Create and assign the skybox as a child object to the player
        pos = new Vector3(10,5,5);
        //this.addChild(this.skyBox = new SkyBox("resources\\SkyBox\\", "SkyBox.obj", Settings.gl));
        this.addChild(this.plane = new ObjObject("resources\\", "sc.obj", Settings.gl));
        this.addChild(this.planeProp = new ObjObject("resources\\", "sc_prop.obj", Settings.gl));
        //planeProp.addChild(new DimensionTool(Settings.gl));
        // plane prop height on plane = 1.49468m
        this.planeProp.pos.y = 1.49468;
        

        // Attach an axis tool for debugging
        this.addChild(new DimensionTool(Settings.gl));

        //cam.pos.x -= 10;

        animated = true;
        this.planeSpeed /= 3.6; // Convert the speed of the plane from km/h to m/s
        this.cam.cameraOffset = cameraOffset;
        this.cam.pos = new Vector3(0,10,10);
        lastSecond = System.currentTimeMillis() / 1000;
        startPos = pos.z;
        //this.cam.update();
    }

    private void checkBounds() {
        if (pos.x > playAreaSize || pos.x < -playAreaSize || pos.z > playAreaSize || pos.z < -playAreaSize) {
            outside = true;
        } else if (outside && fixingRotation) {
            outside = false;
            fixingRotation = false;
        }

        if (pos.y <= 0.1) {
            bellowGround = true;
        } else if (pos.y > playAreaSize) {
            aboveArea = true;
        }
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {

        // Check the player is still inside bounds
        checkBounds();
        // If the player is outide the bounds, reverse direction
        if (outside && !fixingRotation) {
            fixingRotation = true;
            rotation.y += 180;
        }

        if (bellowGround) {
            pos.y = 0.5;
            rotation.z = 45;
            bellowGround = false;
        } else if (aboveArea) {
            rotation.z = -45;
            aboveArea = false;
        }

        // Rotate the planes propeller
        planeProp.rotation.x  += 10;
        // Move the plane fowards
        move(0.0, planeSpeed);
    }

    @Override
    public void transform(GL2 gl) {
		// if a pivot point is set for the object, we need to rotate around that point
		if (pivot) {
			// to rotate around a pivot point, we need to move the object from 0,0,0 to the pivot point, apply rotation, then move back to 0,0,0 ready to apply normal translation
			gl.glTranslated(pivotPoint.x, pivotPoint.y, pivotPoint.z);
				gl.glRotated(pivotRot.r, pivotRot.x, pivotRot.y, pivotRot.z);
			gl.glTranslated(-pivotPoint.x, -pivotPoint.y, -pivotPoint.z);
		}

		gl.glTranslated(pos.x, pos.y, pos.z); // translate the object from 0,0,0
		//gl.glRotated(rot.r, rot.x, rot.y, rot.z); // rotate the object around the centre of its translated position (non pivot rotate)
        gl.glRotated(rotation.x, 1, 0, 0);
        gl.glRotated(rotation.y, 0, 1, 0);
        gl.glRotated(rotation.z, 0, 0, 1);
        gl.glScaled(scale.x, scale.y, scale.z); // scale the object
	}

    @Override
    public void drawObject(GL2 gl) {
    }

    /**
     * Moves an object fowards in the direction of rotation + offset
     * @param rotOffset rotation offset
     * @param distance distance to travel
     */
    private void move(double rotOffset, double distance) {

        double verticalDistance = distance * Math.sin(Math.toRadians(rotation.z));
        double horizontalDistance = distance * Math.cos(Math.toRadians(rotation.z));

        double theta = rotation.y + 90;
        pos.x += horizontalDistance * Math.sin(Math.toRadians(theta)) * Settings.deltaTime;
        pos.z += horizontalDistance * Math.cos(Math.toRadians(theta)) * Settings.deltaTime;
        pos.y += verticalDistance * Settings.deltaTime;
	}
}