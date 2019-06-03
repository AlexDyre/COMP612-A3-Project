package objects;

import com.jogamp.opengl.GL2;

import renderer.Settings;

/**
 * Class for a bullet object
 * @author Jordan Carter - 1317225
 */
public class Bullet extends IndexedObject {

    private float speed;
    public static Entity target;
    public static double targetSize;
    public static double boundingSphereSize = 0.1;
    public static double lifeTime = 1.0;
    public static Player player;
    public double endLifeTime = 0.0;

    /**
     * Constructor for a bullet object
     * @param modelIndexValue
     * @param speed
     */
    public Bullet(int modelIndexValue, float speed) {
        super(modelIndexValue);
        this.speed = speed;
        this.animated = true;
        this.endLifeTime = lifeTime + (System.currentTimeMillis() / 1000);
    }

    /**
     * Moves a bullet forward
     */
    public void move() {
        double verticalDistance = speed * Math.sin(Math.toRadians(rotation.z));
        double horizontalDistance = speed * Math.cos(Math.toRadians(rotation.z));

        double theta = rotation.y + 90;
        pos.x += horizontalDistance * Math.sin(Math.toRadians(theta)) * Settings.deltaTime;
        pos.z += horizontalDistance * Math.cos(Math.toRadians(theta)) * Settings.deltaTime;
        pos.y += verticalDistance * Settings.deltaTime;
    }

    /**
     * Checks collisions for the bullet
     */
    public void checkCollisions() {
        boolean collided = false;

        if (pos.y <= 0.0)
            collided = true;
        
        if (collided) {
            removeBullet();
        }
    }

    /**
     * Checks the bullets lifetime
     * If the bullet is too old, it will queue itself to be removed
     */
    public void checkLifeTime() {
        if (System.currentTimeMillis() / 1000 > endLifeTime) {
            // Bullet has reached end of life, delete it
            removeBullet();
        }
    }

    /**
     * Queues the bullet to be removed
     */
    private void removeBullet() {
        player.removeBullet(this);
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        move();
        checkCollisions();
        checkLifeTime();
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
        gl.glRotated(rotation.x, 1, 0, 0);
        gl.glRotated(rotation.y, 0, 1, 0);
        gl.glRotated(rotation.z, 0, 0, 1);
        gl.glScaled(scale.x, scale.y, scale.z); // scale the object
	}
}