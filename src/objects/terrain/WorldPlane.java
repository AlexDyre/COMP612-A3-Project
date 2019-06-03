package objects.terrain;

import com.jogamp.opengl.GL2;

import objects.Entity;
import objects.Player;
import renderer.Settings;
import util.ColorRGBA;

/**
 * Class for a world plane object
 * @author Jordan Carter - 1317225
 */
public class WorldPlane extends Entity {

    private int displayList;
    private ColorRGBA terrainColor;
    private double size;
    private double yHeight;
    private Player player;

    /**
     * Constructor for a world plane
     * @param size
     * @param yHeight
     * @param player
     * @param terrainColor
     */
    public WorldPlane (double size, double yHeight, Player player, ColorRGBA terrainColor) {
        super();
        this.size = size;
        this.yHeight = yHeight;
        this.player = player;
        this.terrainColor = terrainColor;
        generateDisplayList(Settings.gl);
        animated = true;
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        pos.x = player.pos.x;
        pos.z = player.pos.z;
    }

    @Override
	public void drawObject(GL2 gl) {
		gl.glCallList(displayList);
	}

    /**
     * Generates the display list
     * @param gl
     */
    private void generateDisplayList(GL2 gl) {
        displayList = Settings.gl.glGenLists(1);

        gl.glNewList(displayList, GL2.GL_COMPILE);
            terrainColor.set();
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
                    gl.glNormal3d(0, 1, 0);
                    gl.glVertex3d(-size, yHeight, -size);
                    gl.glVertex3d(-size, yHeight, size);
                    gl.glVertex3d(size, yHeight, -size);
                    gl.glVertex3d(size, yHeight, size);
            gl.glEnd();
        gl.glEndList();
    }
}