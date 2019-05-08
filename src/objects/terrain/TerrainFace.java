package objects.terrain;

import util.Vector3;

public class TerrainFace {
    public Vector3 v1, v2, v3, v4;

    public TerrainFace() {
        this.v1 = new Vector3();
        this.v2 = new Vector3();
        this.v3 = new Vector3();
        this.v4 = new Vector3();
    }

    public TerrainFace(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }
}