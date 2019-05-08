package util.obj;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import renderer.Settings;
import util.ColorRGB;

/**
 * Class defining a loader to parse an .obj .mtl file
 * 
 * @author Jordan Carter - 1317225
 */
public class ObjMtlLoader {

	public static ObjMtl parseMaterial(String name, String material) {
		BufferedReader br = null;

		ObjMtl mat = new ObjMtl(name);

		try {
			br = new BufferedReader(new StringReader(material));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					String[] tokens = line.split(" ");
					if (tokens.length > 1) {
						switch (tokens[0]) {
						case "Ka":
							mat.ambient = parseMtlRGBComponent(tokens);
							break;
						case "Kd":
							mat.diffuse = parseMtlRGBComponent(tokens);
							break;
						case "Ks":
							mat.specular = parseMtlRGBComponent(tokens);
							break;
						case "Ns":
							mat.specularExponent = Double.parseDouble(tokens[1]);
							break;
						case "Ke":
							break;
						case "Ni":
							break;
						case "d":
							mat.transparency = Double.parseDouble(tokens[1]);
							break;
						case "illum":
							mat.illuminationModel = Integer.parseInt(tokens[1]);
							break;
						default:
							break;
						}
					}
				}
			}

		} catch (Exception e) {

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return mat;
	}

	private static ColorRGB parseMtlRGBComponent(String[] tokens) {
		ColorRGB rgb = new ColorRGB();

		rgb.red = Double.parseDouble(tokens[1]);
		rgb.green = Double.parseDouble(tokens[2]);
		rgb.blue = Double.parseDouble(tokens[3]);

		return rgb;
	}

	public static MtlLibrary parseMtlLib(String path) {
		FileInputStream fs = null;
		DataInputStream in = null;
		BufferedReader br = null;

		if (Settings.DEBUG)
			System.out.println(path);
		MtlLibrary library = new MtlLibrary(path);

		try {
			fs = new FileInputStream("resources/" + path);
			in = new DataInputStream(fs);
			br = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					String[] tokens = line.split(" ");

					if (tokens.length == 2) {
						if (tokens[0].equalsIgnoreCase("newmtl")) {
							String material = "";
							for (int i = 0; i < 8; i++) {
								material += br.readLine() + "\n";
							}
							library.materials.add(parseMaterial(tokens[1], material));
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
			if (in != null) {
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

		return library;
	}

	@Override
	public String toString() {
		return "ObjMtlLoader []";
	}
}