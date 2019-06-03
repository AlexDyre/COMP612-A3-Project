package objects;

import com.jogamp.opengl.GL2;

import objects.terrain.Terrain;
import renderer.Settings;
import util.Vector3;
import util.obj.TexturedObjObject;

public class Target extends TexturedObjObject {

    private Player player;
    private double terrainSize;
    private static double boundingSphereSize = 8.0;
    private float spotlightRotation = 0;
    private float spotlightRadius = 5f;

    public Target(String path, String fileName, GL2 gl, Player player, Terrain terrain) {
        super(path, fileName, gl);
        this.player = player;
        this.animated = true;
        this.terrainSize = terrain.gridSquareSize * (double) terrain.size;
        // Generate a random position to start
        //movePosition();
    }

    public void checkCollisions() {
        for (Bullet b : player.bullets) {
            if (pos.distance(b.pos) <= boundingSphereSize + Bullet.boundingSphereSize) {
                movePosition();
                break;
            }
        }
    }

    public void movePosition() {
        double x = (Math.random() * terrainSize) - (terrainSize/2);
        double y = 0 - pos.y;
        double z = (Math.random() * terrainSize) - (terrainSize/2);
        pos = new Vector3(x, y, z);
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        checkCollisions();

        spotlightRotation += 4.0f;

        if (spotlightRotation >= 360.0f)
            spotlightRotation = 0.0f;

    }

    public void updateLightPosition() {
        float[] position = {(float) pos.x, 4f, (float) pos.z, 1};
        double rad = Math.toRadians(spotlightRotation);
        float xPos = (float) pos.x + (spotlightRadius * (float) Math.cos(rad));
        float zPos = (float) pos.x + (spotlightRadius * (float) Math.sin(rad));
        float spotLightDirection[] = {xPos, -2, zPos};
        // Update the light
        Settings.gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, position, 0);
        Settings.gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, spotLightDirection, 0);
    }
}