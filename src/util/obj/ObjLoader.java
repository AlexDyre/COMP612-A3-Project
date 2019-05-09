package util.obj;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import renderer.Main;
import renderer.Settings;
import shapes.Tri;
import util.Vector3;

/**
 * Imports an 3D model from Wavefront (.obj) for use in OpenGL
 * @author Jordan Carter - 1317225
 *
 */
public class ObjLoader {
	
	//private static boolean DEBUG = true;
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static ObjObject importModel(String path, String fileName, ObjObject object) {
		FileInputStream fs = null;
		DataInputStream in = null;
		BufferedReader br = null;
		
		try {
			fs = new FileInputStream(path + fileName);
			in = new DataInputStream(fs);
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			String material = "";
			boolean useMtl = false;

			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					String[] tokens = line.split(" ");
					
					if (tokens.length >= 1) {
						switch (tokens[0]) {
						case "v":
							object.vertices.add(parseVertex(tokens));
							break;
						case "f":
							//object.faces.add(parseFace(tokens, object, useMtl, material));
							addFace(parseFace(tokens, object, useMtl, material), object);
							break;
						case "vn":
							object.normals.add(parseNormal(tokens));
							break;
						case "mtllib":
							object.mtlLibrary = ObjMtlLoader.parseMtlLib(path, tokens[1]);
							break;
						case "usemtl":
							useMtl = true;
							material = tokens[1];
							break;
						default:
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// for debug purposes, prints the object
		if (Settings.DEBUG)
			System.out.println(object);
		return object;
	}
	
	private static Tri parseFace(String[] tokens, ObjObject object, boolean useMtl, String mtl) {
		Tri face = new Tri();
		int mtlIndex = 0;
		int count = 0;
		face.useMtl = useMtl;
		
		for (ObjMtl objMtl : object.mtlLibrary.materials) {
			if (objMtl.mtlName.equalsIgnoreCase(mtl)) {
				mtlIndex = count;
			}
			count++;
		}
		
		face.material = object.mtlLibrary.materials.get(mtlIndex);
		
		if (tokens.length == 4) {
			for (int i = 1; i <= 3; i++) {
				String[] faceTokens = tokens[i].split("/");
				// Need to minus one from the fetch value as obj arrays start at 1 not 0
				switch (i) {
				case 1:
					face.v1 = object.vertices.get(Integer.parseInt(faceTokens[0]) - 1);
					break;
				case 2:
					face.v2 = object.vertices.get(Integer.parseInt(faceTokens[0]) - 1);
					break;
				case 3:
					face.v3 = object.vertices.get(Integer.parseInt(faceTokens[0]) - 1);
					break;
				default:
					break;
				}
				face.normal = object.normals.get(Integer.parseInt(faceTokens[2]) - 1);
			}
		}
		
		return face;
	}

	private static void addFace(Tri face, ObjObject obj) {
		if (face.useMtl && face.material.transparency < 1.0) {
			obj.transparentFaces.add(face);
		} else {
			obj.faces.add(face);
		}
	}

	private static Vector3 parseVertex(String[] tokens) {
		Vector3 vert = null;
		
		if (tokens.length == 4) {
			vert = new Vector3();
			vert.x = Double.parseDouble(tokens[1]);
			vert.y = Double.parseDouble(tokens[2]);
			vert.z = Double.parseDouble(tokens[3]);
		}
		
		return vert;
	}
	
	private static Vector3 parseNormal(String[] tokens) {
		Vector3 normal = null;
		
		if (tokens.length == 4) {
			normal = new Vector3();
			normal.x = Double.parseDouble(tokens[1]);
			normal.y = Double.parseDouble(tokens[2]);
			normal.z = Double.parseDouble(tokens[3]);
		}
		
		return normal;
	}
}