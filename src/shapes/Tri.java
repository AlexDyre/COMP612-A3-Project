package shapes;

import util.obj.ObjObject;
import util.Vector2;
import util.Vector3;
import util.obj.ObjMtl;

/**
 * Defines a triangle face, used by the .obj model importer for every face.
 * @author Jordan Carter - 1317225
 */
public class Tri {
	public Vector3 v1, v2, v3, normal;
	public Vector2 vt1, vt2, vt3;
	public int[] vertexPos;
	public int textureID;
	public ObjMtl material;
	public boolean useMtl, textured;
	@SuppressWarnings("unused") // Field is referenced outside of local context
	private ObjObject obj;
	
	/**
	 * Default constructor for a tris face, creates an empty tri face
	 */
	public Tri() {
		this.vertexPos = new int[3];
		this.obj = null;
		this.useMtl = false;
		this.material = null;
		this.textured = false;
	}
	
	/**
	 * Constructor for a tris face
	 * @param vertexPosition
	 * @param obj
	 */
	public Tri(int[] vertexPosition, ObjObject obj) {
		this.vertexPos = vertexPosition;
		this.obj = obj;
		this.useMtl = false;
		this.material = null;
	}

	/**
	 * Sets a faces texture ID
	 */
	public void setTextureID() {
		textureID = material.textureID;
	}
}