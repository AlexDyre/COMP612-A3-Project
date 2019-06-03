package objects;

import com.jogamp.opengl.GL2;

import objects.terrain.Terrain;
import util.Vector3;
import util.obj.TexturedObjObject;

public class Target extends TexturedObjObject {

    private Player player;
    private double terrainSize;
    private static double boundingSphereSize = 8.0;
    private float spotlightRotation = 0;

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
        double x = (Math.random() * terrainSize) - (terrainSize/2) - pos.x;
        double y = 0 - pos.y;
        double z = (Math.random() * terrainSize) - (terrainSize/2) - pos.z;
        pos = new Vector3(x, y, z);
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        checkCollisions();

        spotlightRotation += 2.0f;

        if (spotlightRotation >= 360.0f)
            spotlightRotation = 0.0f;
        
        //System.out.println(spotlightRotation);

        float[] position = {6f, 1f, 6f, 0};
        float[] spotRotation = {0.0f, 0.0f, 0.0f};
        // Update the light
        //gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, position, 0);
        //gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, spotRotation, 0);

    }
}