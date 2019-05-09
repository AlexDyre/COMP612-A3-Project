package util.obj;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TexturedObjObject extends ObjObject {

    //private Texture[] textures;

    private ArrayList<Texture> textures;
    
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
                    textures.add(TextureIO.newTexture(new File(path + mat.map_Kd), true));
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
}