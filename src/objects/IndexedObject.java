package objects;

import com.jogamp.opengl.GL2;

public class IndexedObject extends Entity {

    public int modelIndex;

    public IndexedObject (int modelIndexValue) {
        super();
        this.modelIndex = modelIndexValue;
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {

    }

    @Override
    public void drawObject(GL2 gl) {
        gl.glCallList(modelIndex);
    }

}