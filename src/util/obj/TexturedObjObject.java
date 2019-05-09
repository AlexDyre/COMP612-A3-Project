package util.obj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import shapes.Tri;
import util.ColorRGB;
import util.Vector2;
import util.Vector3;

public class TexturedObjObject extends ObjObject {

    //private Texture[] textures;

    private ArrayList<Texture> textures;
    private ArrayList<String> loadedTextures;
    
    public TexturedObjObject(String path, String fileName, GL2 gl) {
        super();
        this.path = path;
        this.filename = fileName;
        this.textures = new ArrayList<Texture>();
        ObjLoader.importModel(path, fileName, this);
        // load textures

        loadTextures(path);
        System.out.println("Texure path is: " + this.path);

        this.triDisplayList = gl.glGenLists(1);
        // Call overrided compile
        compileTriList(gl);
        
    }

    private void loadTextures(String path) {
        for (ObjMtl mat : mtlLibrary.materials) {
            if (mat.textured) {
                try {
                    //System.out.println("ASDSADASD");
                    textures.add(TextureIO.newTexture(new File(path + mat.map_Kd), true));
                    //textures.get(0).toString();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }

        System.out.println("Textures loaded");
    }

    @Override
    protected void compileTriList(GL2 gl) {
		System.out.println("Compiling tri list");
		gl.glNewList(triDisplayList, GL2.GL_COMPILE);
		
		// set a default bright red color as default
		gl.glColor4d(1.0, 0.0, 0.0, 1.0);
			
		ColorRGB color;
		for (Tri face : faces) {
			color = face.material.diffuse;
			gl.glColor4d(color.red, color.green, color.blue, face.material.transparency);
			
			gl.glBegin(GL2.GL_POLYGON);
				//need to set normal for the face first
                gl.glNormal3d(face.normal.x, face.normal.y, face.normal.y);
                // bind the texture
                gl.glBindTexture(GL2.GL_TEXTURE_2D, face.textureID);
                gl.glTexCoord2d(face.vt1.x, face.vt1.y);
                gl.glVertex3d(face.v1.x, face.v1.y, face.v1.z);
                gl.glTexCoord2d(face.vt2.x, face.vt2.y);
                gl.glVertex3d(face.v2.x, face.v2.y, face.v2.z);
                gl.glTexCoord2d(face.vt3.x, face.vt3.y);
				gl.glVertex3d(face.v3.x, face.v3.y, face.v3.z);
			gl.glEnd();
		}

		gl.glEndList();
	}
}