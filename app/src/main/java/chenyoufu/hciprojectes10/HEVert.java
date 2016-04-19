package chenyoufu.hciprojectes10;

/**
 * Created by Chen Youfu on 18/04/2016.
 */
public class HEVert {
    float x, y, z; // the vertex coordinates
    HEEdge edge; // one of the half-edges emanating from the vertex

    public HEVert(float x, float y, float z, HEEdge edge) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.edge = edge;
    }
};