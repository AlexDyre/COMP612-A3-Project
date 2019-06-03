package objects;

import com.jogamp.opengl.GL2;

import objects.terrain.Terrain;
import renderer.Settings;
import util.obj.TexturedObjObject;

/**
 * Class for a target object
 */
public class Target extends TexturedObjObject {

    private Player player;
    private double terrainSize;
    private static double boundingSphereSize = 8.0;
    private float spotlightRotation = 0;
    private float spotlightRadius = 5f;

    /**
     * Constructor for a target object
     * @param path
     * @param fileName
     * @param gl
     * @param player
     * @param terrain
     */
    public Target(String path, String fileName, GL2 gl, Player player, Terrain terrain) {
        super(path, fileName, gl);
        this.player = player;
        this.animated = true;
        this.terrainSize = terrain.gridSquareSize * (double) terrain.size;
    }

    /**
     * Checks for collisions against this object
     */
    public void checkCollisions() {
        for (Bullet b : player.bullets) {
            if (pos.distance(b.pos) <= boundingSphereSize + Bullet.boundingSphereSize) {
                movePosition();
                break;
            }
        }
    }

    /**
     * Moves the target to a new position in the game area
     */
    public void movePosition() {
        pos.x = (Math.random() * terrainSize) - (terrainSize/2);
        pos.y = 0 - pos.y;
        pos.z = (Math.random() * terrainSize) - (terrainSize/2);
        updateLightPosition();
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        checkCollisions();

        spotlightRotation += 1.0f;

        if (spotlightRotation >= 360.0f)
            spotlightRotation = 0.0f;
    }

    /**
     * Updates the animated spotlight
     */
    public void updateLightPosition() {
        double rad = Math.toRadians(spotlightRotation);
        float xPos = (float) (spotlightRadius * Math.cos(rad));
        float zPos = (float) (spotlightRadius * Math.sin(rad));
        
        float spotLightDirection[] = {xPos, -1.5f, zPos}; // direction is a vector, not actual coordinates, however the values do not need clamping (normalising)
        float[] position = {(float) pos.x, 4.5f, (float) pos.z, 1};
        // Update the light
        Settings.gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, position, 0);
        Settings.gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, spotLightDirection, 0);
    }
}