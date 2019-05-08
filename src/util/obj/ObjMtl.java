package util.obj;

import util.ColorRGB;

/**
 * Class defining an .obj material
 * 
 * @author Jordan Carter - 1317225
 */
public class ObjMtl {
	
	public String mtlName;
	public ColorRGB ambient;
	public ColorRGB diffuse;
	public ColorRGB specular;
	public double transparency;
	public double specularExponent;
	public int illuminationModel;
	

	public ObjMtl(String mtlName) {
		this.mtlName = mtlName;
		this.ambient = new ColorRGB();
		this.diffuse = new ColorRGB();
		this.specular = new ColorRGB();
		this.specularExponent = 0.0;
		this.illuminationModel = 0;
	}
}