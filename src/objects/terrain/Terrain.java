package objects.terrain;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import objects.Entity;
import objects.IndexedObject;
import objects.Player;
import renderer.Settings;
import util.ColorRGBA;
import util.Vector3;
import util.obj.ObjObject;

public class Terrain extends Entity {
    public int size;
    public double gridSquareSize;
    private ArrayList<TerrainFace> terrain;
    private ColorRGBA terrainColor;
    private int displayList;
    private int numTrees = 100;

    public Terrain(int size, double gridSquareSize, ColorRGBA terrainColor, Player player) {
        super();
        this.size = size;
        this.gridSquareSize = gridSquareSize;
        this.terrain = new ArrayList<TerrainFace>();
        this.terrainColor = terrainColor;
        this.pos = new Vector3(-100, 0, -100);
        this.addChild(new WorldPlane(300.0, -0.01, player, terrainColor));
        generateTerrain();
        animated = true;
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
        ObjObject treeModel = new ObjObject("resources\\", "tree.obj", Settings.gl);
        double terrainSize = gridSquareSize * (double) size;
        System.err.println("Terrain size: " + terrainSize);

        for (int i = 0; i < numTrees; i++) {
            IndexedObject tree = new IndexedObject(treeModel.triDisplayList);
            double x = (Math.random() * terrainSize) - (terrainSize/2) - pos.x;
            double y = 0 - pos.y;
            double z = (Math.random() * terrainSize) - (terrainSize/2) - pos.z;
            tree.pos = new Vector3(x, y, z);
            System.out.println("Generated Tree Pos: " + tree.pos);
            this.addChild(tree);
        }

        generateDisplayList(gl);
    }

	@Override
	public void animate(GL2 gl, double deltaTime) {
        
    }

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
                    gl.glNormal3d(0, 1, 0);
                    gl.glVertex3d(face.v1.x, face.v1.y, face.v1.z);
                    gl.glVertex3d(face.v2.x, face.v2.y, face.v2.z);
                    gl.glVertex3d(face.v3.x, face.v3.y, face.v3.z);
                    gl.glVertex3d(face.v4.x, face.v4.y, face.v4.z);
                gl.glEnd();
            }
        gl.glEndList();
    }
}