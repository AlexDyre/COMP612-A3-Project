package util.obj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import shapes.Tri;
import util.ColorRGB;

public class TexturedObjObject extends ObjObject {

    private ArrayList<Texture> textures;
    
    public TexturedObjObject(String path, String fileName, GL2 gl) {
        super();
        this.path = path;
        this.modelFilename = fileName;
        this.textures = new ArrayList<Texture>();
        ObjLoader.importModel(path, fileName, this);
        // load textures

        loadTextures(path);
        // Apply face textures
        applyFaceTextureReferences();

        this.triDisplayList = gl.glGenLists(1);
        // Call overrided compile
        compileTriList(gl);
        
    }

    /**
     * Loads the textures from the material library
     * @param path
     */
    private void loadTextures(String path) {
        int texCount = 0;
        for (ObjMtl mat : mtlLibrary.materials) {
            if (mat.textured) {
                try {
                    Texture tex = TextureIO.newTexture(new File(path + mat.map_Kd), true);
                    textures.add(tex);
                    mat.textureID = 0 + texCount;
                    texCount++;
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }

    /**
     * Applys the loaded texture ids to the faces for the object
     */
    private void applyFaceTextureReferences() {
        for (Tri face : faces) {
            face.setTextureID();
        }
    }

    @Override
    protected void compileTriList(GL2 gl) {
		gl.glNewList(triDisplayList, GL2.GL_COMPILE);
		
		// set a default bright red color as default
		gl.glColor4d(1.0, 0.0, 0.0, 1.0);
			
		ColorRGB color;
		for (Tri face : faces) {
			color = face.material.diffuse;
            gl.glColor4d(color.red, color.green, color.blue, face.material.transparency);
            
			// bind and enable the texture
            gl.glEnable(GL2.GL_TEXTURE_2D);
            Texture tex = textures.get(face.textureID);
            tex.bind(gl);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
            
			gl.glBegin(GL2.GL_POLYGON);
                //need to set normal for the face first
                gl.glNormal3d(face.normal.x, face.normal.y, face.normal.y);
                gl.glTexCoord2d(face.vt1.x, face.vt1.y);
                gl.glVertex3d(face.v1.x, face.v1.y, face.v1.z);
                gl.glTexCoord2d(face.vt2.x, face.vt2.y);
                gl.glVertex3d(face.v2.x, face.v2.y, face.v2.z);
                gl.glTexCoord2d(face.vt3.x, face.vt3.y);
                gl.glVertex3d(face.v3.x, face.v3.y, face.v3.z);
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
		}

		gl.glEndList();
	}
}