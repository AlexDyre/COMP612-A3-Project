package objects;

import com.jogamp.opengl.GL2;

/**
 * Dimension Tool Debug Entity Class
 * @author Jordan Carter - 1317225
 */
public class DimensionTool extends Entity {
    private int displayList;
    
    /**
     * Constructor for a DimensionTool debug entity
     * @param gl
     */
    public DimensionTool(GL2 gl) {
        super();
        this.debug = false;
        //this.scale = new Vector3(0.01, 0.01, 0.01);
        complileTriList(gl);
    }

    private void complileTriList(GL2 gl) {
        // create the display list
        displayList = gl.glGenLists(1);
        // compile data in the display list
        gl.glNewList(displayList, GL2.GL_COMPILE);
            gl.glBegin(GL2.GL_LINES);
                // x-axis
                gl.glColor4d(1, 0, 0, 1);
                gl.glVertex3d(0, 0, 0);
                gl.glVertex3d(10, 0, 0);

                // y-axis
                gl.glColor4d(0, 1, 0, 1);
                gl.glVertex3d(0, 0, 0);
                gl.glVertex3d(0, 10, 0);

                // z-axis
                gl.glColor4d(0, 0, 1, 1);
                gl.glVertex3d(0, 0, 0);
                gl.glVertex3d(0, 0, 10);
            gl.glEnd();
        gl.glEndList();
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {}
    
    @Override
	public void drawObject(GL2 gl) {
        gl.glCallList(displayList);
    }
}