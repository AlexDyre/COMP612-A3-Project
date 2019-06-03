package objects;

import com.jogamp.opengl.GL2;

/**
 * Defines an indexed object.
 * Object does not define or import it's own model, but uses a predefined display list
 * @author Jordan Carter - 1317225
 */
public class IndexedObject extends Entity {

    public int modelIndex;

    /**
     * IndexedObject constructor
     * @param modelIndexValue
     */
    public IndexedObject (int modelIndexValue) {
        super();
        this.modelIndex = modelIndexValue;
    }

    @Override
    public void animate(GL2 gl, double deltaTime) {}

    @Override
    public void drawObject(GL2 gl) {
        gl.glCallList(modelIndex);
    }

}