package objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import util.obj.TexturedObjObject;

public class SkyBox extends TexturedObjObject {
    /**
     * 
     * @param path
     * @param fileName
     * @param gl
     */
    public SkyBox(String path, String fileName, GL2 gl) {
        super(path, fileName, gl);
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {

    }

    @Override
    public void drawObject(GL2 gl) {
        gl.glCallList(triDisplayList);
    }
}