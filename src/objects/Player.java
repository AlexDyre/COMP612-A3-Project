package objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;

import renderer.Settings;
import renderer.TrackballCamera;
import renderer.TrackingCamera;
import util.Vector3;
import util.obj.ObjObject;

/**
 * Player class acts as a parent object for all player components to attach to
 */
public class Player extends Entity {

    public SkyBox skyBox;
    public ObjObject plane;
    public TrackingCamera cam;

    private Vector3 cameraOffset = new Vector3(0, 3, -12);
    private Vector3 cameraOffsetFirstPerson = new Vector3(0, 2, 2 );

    private Vector3 forward = new Vector3(0, 1, 0);
    private Vector3 zero = new Vector3(0,0,0);
    private boolean initialised = false;

    public Player(TrackingCamera cam) {
        super();
        this.rotation.y = 0.0;
        this.cam = cam;
        this.cam.player = this;
        
        // Create and assign the skybox as a child object to the player
        pos = new Vector3(10,10,5);
        this.addChild(this.skyBox = new SkyBox("resources\\SkyBox\\", "SkyBox.obj", Settings.gl));
        this.addChild(this.plane = new ObjObject("resources\\", "sc.obj", Settings.gl));

        //cam.pos.x -= 10;

        animated = true;
        this.cam.cameraOffset = cameraOffset;
        this.cam.update();
        initialised = true;
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {

        if (initialised) {
            
        }

        rotation.y += 10 * deltaTime;

        //pos.x += 10 *deltaTime;
        //System.out.println("Player: " + pos);
        //System.out.println(cam.rotation);
        //System.out.println("Amount: " + rotationAmount);

        //rotation.y += 1 * deltaTime;

        System.out.println(rotation.y);
        System.out.println("r: " + rotation + "\ndelta time: " + deltaTime + "\nspeed: " + Settings.speedModifier);

        // make sure the skybox is not rotating on any axis
        //skyBox.rotation = zero;
    }

    @Override
    public void drawObject(GL2 gl) {
        // Update the camera
        //cam.setLookAt(pos.x, pos.y, pos.z);
    }
}