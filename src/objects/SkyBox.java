package objects;

import com.jogamp.opengl.GL2;

import util.Vector3;
import util.obj.TexturedObjObject;

public class SkyBox extends TexturedObjObject {

    public Player player;
    /**
     * 
     * @param path
     * @param fileName
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
            // We need to make sure fog is disabled when drawing the skybox, then re-enable after
            gl.glDisable(GL2.GL_FOG);
                gl.glCallList(triDisplayList);
            gl.glEnable(GL2.GL_FOG);
        gl.glDepthMask(true);
    }
}