package objects;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import renderer.Settings;
import util.Quaternion;
import util.Vector3;

/**
 * Debug Entity Class (Not fully implemented yet)
 * Allows the use of objects as debugging tools
 * Seperated from standard entities to allow for use of debug entities inside of normal entities without causing a stack overflow.
 * @author Jordan Carter - 1317225
 */
public abstract class DebugEntity {
	public Vector3 pos, rotation, pivotRotation, scale, pivotPoint;
	public Quaternion rot, pivotRot;
	public Entity parent;
	public ArrayList<Entity> children;
	public boolean animated, pivot;

	public boolean debug = true;
	
	/**
	 * Constructor for a debug entity
	 */
	public DebugEntity() {
		this.pos = new Vector3(); // every object will have a position
		this.pivotPoint = new Vector3();
		this.rotation = new Vector3(); // Euler angle rotation representation, every object starts with no rotation
		this.pivotRotation = new Vector3();
		this.scale = new Vector3(1.0, 1.0, 1.0); // every object starts with a scale of 1 for each axis
		this.rot = new Quaternion(); // every object has a quaternion to represent rotation from the euler angle rotation vector
		this.pivotRot = new Quaternion(); // 
		this.parent = null; // not every object has a parent
		this.children = null; // not every object will have children
		this.animated = false;
		this.pivot = false;
	}
	
	/**
	 * Updates and animates and object
	 * Updates an object to move/rotate/scale with the parent object
	 */
	public void update(GL2 gl) {
		// Update rotation quaternion before applying any transforms
		rot.rotateEuler(rotation);
		if (pivot) {
			pivotRot.rotateEuler(pivotRotation);
		}
		// Transform the object
		transform(gl);
		// If the object is flagged as animated, animate the object first
		if (animated) {
			animate(gl, Settings.deltaTime * Settings.speedModifier);
		}
	}
	
	/**
	 * transforms the object based of it's parent
	 * @param gl
	 */
	public void transform(GL2 gl) {
		// Translate the object to it's position from 0,0,0
		//gl.glTranslated(pos.x, pos.y, pos.z);
		// if a pivot point is set for the object, we need to rotate around that point
		if (pivot) {
			// to rotate around a pivot point, we need to move the object from 0,0,0 to the pivot point, apply rotation, then move back to 0,0,0 ready to apply normal translation
			gl.glTranslated(pivotPoint.x, pivotPoint.y, pivotPoint.z);
				gl.glRotated(pivotRot.r, pivotRot.x, pivotRot.y, pivotRot.z);
			gl.glTranslated(-pivotPoint.x, -pivotPoint.y, -pivotPoint.z);
		}

		gl.glTranslated(pos.x, pos.y, pos.z); // translate the object from 0,0,0
		gl.glRotated(rot.r, rot.x, rot.y, rot.z); // rotate the object around the centre of its translated position (non pivot rotate)
		gl.glScaled(scale.x, scale.y, scale.z); // scale the object
	}
	
	/**
	 * Defines animations for the object
	 */
	abstract void animate(GL2 gl, double deltaTime);
	
	/**
	 * Defines how the object is drawn
	 * Calls to draw display lists and create shapes are in here
	 * @param gl gl renderer
	 */
	abstract void drawObject(GL2 gl);
	
	/**
	 * Draws the object
	 * @param gl
	 */
	public void draw(GL2 gl) {
		gl.glPushMatrix();
			update(gl); // update the object before drawing
			drawObject(gl);
		
			// if the entity has children entities, request for them draw
			if (children != null) {
				for (Entity obj : children) {
					obj.draw(gl);
				}
			}
		gl.glPopMatrix();
	}
}
