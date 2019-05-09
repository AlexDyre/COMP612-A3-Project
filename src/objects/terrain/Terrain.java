package objects.terrain;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import objects.Entity;
import renderer.Settings;
import util.ColorRGBA;
import util.Vector3;

public class Terrain extends Entity {
    public int size;
    public double gridSquareSize;
    private ArrayList<TerrainFace> terrain;
    private ColorRGBA terrainColor;
    private int displayList;

    public Terrain(int size, double gridSquareSize, ColorRGBA terrainColor) {
        super();
        this.size = size;
        this.gridSquareSize = gridSquareSize;
        this.terrain = new ArrayList<TerrainFace>();
        this.terrainColor = terrainColor;
        System.out.println(terrainColor);
        generateTerrain();
    }

    private void generateTerrain() {
        GL2 gl = Settings.gl;
        gridSquareSize = 1;

        // We will use 0.0 for the y-axis by default, to render a completely flat terrain across the x-z axis
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Vector3 v1 = new Vector3((double) j, 0.0, (double) i); // BL
                Vector3 v2 = new Vector3((double) j, 0.0, (double) i + gridSquareSize); // TL 
                Vector3 v3 = new Vector3((double) j + gridSquareSize, 0.0, (double) i); // BR
                Vector3 v4 = new Vector3((double) j + gridSquareSize, 0.0, (double) i + gridSquareSize); // TR
                terrain.add(new TerrainFace(v1, v2, v3, v4));
            }
        }
        
        System.out.println("Terrain faces size: " + terrain.size());

        generateDisplayList(gl);
    }

	@Override
	public void animate(GL2 gl, double deltaTime) {}

	@Override
	public void drawObject(GL2 gl) {
		gl.glCallList(displayList);
	}

    private void generateDisplayList(GL2 gl) {
        displayList = Settings.gl.glGenLists(1);

        gl.glNewList(displayList, GL2.GL_COMPILE);
            terrainColor.set();
            for (TerrainFace face : terrain) {
                
                gl.glBegin(GL2.GL_TRIANGLE_STRIP);
                    gl.glVertex3d(face.v1.x, face.v1.y, face.v1.z);
                    gl.glVertex3d(face.v2.x, face.v2.y, face.v2.z);
                    gl.glVertex3d(face.v3.x, face.v3.y, face.v3.z);
                    gl.glVertex3d(face.v4.x, face.v4.y, face.v4.z);
                gl.glEnd();
            }
        gl.glEndList();
    }
}