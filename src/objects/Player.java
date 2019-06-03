package objects;

import java.util.ArrayList;

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

    public float gunDamage = 60f;
    public float gunFireRate = 0.015f;
    public float bulletSpeed = 120f;
    public int bulletIndex;
    public double nextFireTime = 0;
    public ArrayList<Bullet> bullets;
    public ArrayList<Bullet> oldBullets;

    private boolean outside = false;
    private boolean fixingRotation = false;
    private boolean bellowGround = false;
    private boolean aboveArea = false;

    private Vector3 cameraOffset = new Vector3(0, 6, 12);
    //private Vector3 cameraOffsetFirstPerson = new Vector3(0, 2, 2 );

    //private double planeSpeed = 60;
    private double planeSpeed = 0;
    private double playAreaSize = 100;

    public Player(TrackingCamera cam) {
        super();
        this.rotation.y = 0.0;
        this.cam = cam;
        this.cam.player = this;
        this.bullets = new ArrayList<Bullet>();
        this.oldBullets = new ArrayList<Bullet>();
       
        
        // Create and assign the skybox as a child object to the player
        pos = new Vector3(10,5,5);
        //this.addChild(this.skyBox = new SkyBox("resources\\SkyBox\\", "SkyBox.obj", Settings.gl));
        this.addChild(this.plane = new ObjObject("resources\\", "sc.obj", Settings.gl));
        this.addChild(this.planeProp = new ObjObject("resources\\", "sc_prop.obj", Settings.gl));
        // plane prop height on plane = 1.49468m (distance unit from 3D model)
        this.planeProp.pos.y = 1.49468;
        

        // Attach an axis tool for debugging
        this.addChild(new DimensionTool(Settings.gl));

        animated = true;
        this.planeSpeed /= 3.6; // Convert the speed of the plane from km/h to m/s
        this.cam.cameraOffset = cameraOffset;
        this.cam.pos = new Vector3(0,10,10);
         // Load the bullet modelIndex
        this.bulletIndex = new ObjObject("resources\\", "bullet.obj", Settings.gl).triDisplayList;
        Bullet.player = this;
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
    public void drawObject(GL2 gl) {}

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

    public void clearOldBullets() {
        for (Bullet b : oldBullets) {
            bullets.remove(b);
        }

        oldBullets.clear();
    }

    public void drawBullets(GL2 gl) {
        clearOldBullets();

        for (Bullet b : bullets) {
            b.draw(gl);
        }
    }
    
    public void fireGun() {
        // create a bullet
        Bullet bullet = new Bullet(bulletIndex, bulletSpeed);
        bullet.pos = new Vector3(this.pos);
        // Bullet height from gun 2.01465m
        bullet.pos.y += 2.01465;
        bullet.rotation = new Vector3(rotation);
        bullet.enable();
        bullets.add(bullet);
    }

    public void removeBullet(Bullet b) {
        oldBullets.add(b);
    }
}