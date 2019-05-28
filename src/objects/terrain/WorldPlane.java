package objects.terrain;

import com.jogamp.opengl.GL2;

import objects.Entity;
import objects.Player;
import renderer.Settings;
import util.ColorRGBA;

public class WorldPlane extends Entity {

    private int displayList;
    private ColorRGBA terrainColor;
    private double size;
    private double yHeight;
    private Player player;

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