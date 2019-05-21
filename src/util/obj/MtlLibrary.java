package util.obj;

import java.util.ArrayList;

/**
 * Class defining an obj material library
 * @author Jordan Carter - 1317225
 */
public class MtlLibrary {

	public String name;
	public ArrayList<ObjMtl> materials;
	public ArrayList<Texture> textures;
	
	public MtlLibrary(String name) {
		this.name = name;
		this.materials = new ArrayList<ObjMtl>();
	}
	
	@Override
	public String toString() {
		String mtls = "";
		for (ObjMtl mtl : materials) {
			mtls += mtl.mtlName + ", ";
		}
		return "Materials: " + mtls;
	}
}
