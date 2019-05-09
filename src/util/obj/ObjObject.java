package util.obj;

import java.util.ArrayList;
import com.jogamp.opengl.GL2;

import objects.Entity;
import renderer.Main;
import renderer.Settings;
import shapes.Tri;
import util.ColorRGB;
import util.Vector2;
import util.Vector3;

/**
 * Class for an object, an .obj model defines the object model
 * @author Jordan Carter - 1317225
 */
public class ObjObject extends Entity {
	public ArrayList<Vector3> vertices, normals;
	public ArrayList<Vector2> textureVertices;
	public ArrayList<Tri> faces;
	public ArrayList<Tri> transparentFaces;
	public MtlLibrary mtlLibrary;
	
	protected String path, filename;
	protected int triDisplayList;

	/**
	 * Default constructor, initialises an empty object
	 */
	public ObjObject() {
		super();
		this.filename = "";
		this.path = "";
		this.vertices = new ArrayList<Vector3>();
		this.normals = new ArrayList<Vector3>();
		this.textureVertices = new ArrayList<Vector2>();
		this.faces = new ArrayList<Tri>();
		this.transparentFaces = new ArrayList<Tri>();
	}
	
	/**
	 * Constructor for an ObjObject from a specified file
	 * @param path path to .obj file for importing
	 * @param importer obj importer
	 */
	public ObjObject(String path, String fileName, GL2 gl) {
		this();
		this.filename = fileName;
		this.path = path;
		ObjLoader.importModel(path, fileName, this);
		// TODO: Re-reference
		this.triDisplayList = gl.glGenLists(1);

		compileTriList(gl);
	}
	
	/**
	 * Compiles the display list
	 * @param gl
	 * @param index
	 */
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
				gl.glVertex3d(face.v1.x, face.v1.y, face.v1.z);
				gl.glVertex3d(face.v2.x, face.v2.y, face.v2.z);
				gl.glVertex3d(face.v3.x, face.v3.y, face.v3.z);
			gl.glEnd();
		}

		if (transparentFaces.size() > 0) {
			gl.glDepthMask(false);
			for (Tri face : transparentFaces) {
				color = face.material.diffuse;
				gl.glColor4d(color.red, color.green, color.blue, face.material.transparency);
				
				gl.glBegin(GL2.GL_POLYGON);
					//need to set normal for the face first
					gl.glNormal3d(face.normal.x, face.normal.y, face.normal.y);
					gl.glVertex3d(face.v1.x, face.v1.y, face.v1.z);
					gl.glVertex3d(face.v2.x, face.v2.y, face.v2.z);
					gl.glVertex3d(face.v3.x, face.v3.y, face.v3.z);
				gl.glEnd();
			}

			gl.glDepthMask(true);
		}

		gl.glEndList();
	}
	
	@Override
	public String toString() {
		return "File: " + filename + " Vertices: " + vertices.size() + " Triangles: " + faces.size() + " Normals: " + normals.size() + " Materials: " + mtlLibrary + " Position: " + pos;
	}

	@Override
	public void animate(GL2 gl, double deltaTime) {}

	@Override
	public void drawObject(GL2 gl) {
		gl.glCallList(triDisplayList);
	}
	
}
