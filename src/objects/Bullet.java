package objects;

import com.jogamp.opengl.GL2;

import renderer.Settings;

public class Bullet extends IndexedObject {

    private float speed;
    public static Entity target;
    public static double targetSize;
    public static double bulletSize;
    public static double lifeTime = 1.0;
    public static Player player;
    public double endLifeTime = 0.0;

    public Bullet(int modelIndexValue, float speed) {
        super(modelIndexValue);
        this.speed = speed;
        this.animated = true;
        this.endLifeTime = lifeTime + (System.currentTimeMillis() / 1000);
    }

    public void move() {
        double verticalDistance = speed * Math.sin(Math.toRadians(rotation.z));
        double horizontalDistance = speed * Math.cos(Math.toRadians(rotation.z));

        double theta = rotation.y + 90;
        pos.x += horizontalDistance * Math.sin(Math.toRadians(theta)) * Settings.deltaTime;
        pos.z += horizontalDistance * Math.cos(Math.toRadians(theta)) * Settings.deltaTime;
        pos.y += verticalDistance * Settings.deltaTime;
    }

    public void checkCollisions() {
        boolean collided = false;

        if (pos.y <= 0.0)
            collided = true;
        
        if (collided) {
            System.out.println("Bullet collided with an object");
        }


    }

    public void checkLifeTime() {
        if (System.currentTimeMillis() / 1000 > endLifeTime) {
            // Bullet has reached end of life, delete it
            //player.bullets.remove(this);
            player.removeBullet(this);
        }
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        move();
        checkCollisions();
        checkLifeTime();
    }
}