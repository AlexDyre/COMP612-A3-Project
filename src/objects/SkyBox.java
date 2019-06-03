package objects;

import com.jogamp.opengl.GL2;

import util.Vector3;
import util.obj.TexturedObjObject;

/**
 * Class for a Skybox object
 * @author Jordan Carter - 1317225
 */
public class SkyBox extends TexturedObjObject {

    public Player player;
    /**
     * Constructor for a skybox object
     * @param path Path to the folder containing the skybox model
     * @param fileName name of the 3D skybox model
     * @param gl
     */
    public SkyBox(String path, String fileName, GL2 gl) {
        super(path, fileName, gl);
        this.scale = new Vector3(300, 300, 300);
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {
        pos = player.pos;
    }

    @Override
    public void drawObject(GL2 gl) {
        gl.glDepthMask(false);
            // We need to disable a section of GL enabled functions before rendering the skybox, this ensures the skybox is rendered correctly
            gl.glDisable(GL2.GL_DEPTH_TEST);
            gl.glDisable(GL2.GL_FOG);
            gl.glDisable(GL2.GL_LIGHTING);
                gl.glCallList(triDisplayList);
            gl.glEnable(GL2.GL_FOG);
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(true);
    }
}