package chenyoufu.hciprojectes10;

/**
 * Created by Chen Youfu on 18/04/2016.
 */
public class HEEdge {
    HEVert vert; // vertex at the end of the half-edge
    HEEdge pair; // oppositely oriented half-edge
    HEFace face; // the incident face
    HEEdge prev; // previous half-edge around the face
    HEEdge next; // next half-edge around the face

    public HEEdge(HEVert vert, HEEdge pair, HEFace face, HEEdge prev, HEEdge next) {
        this.vert = vert;
        this.pair = pair;
        this.face = face;
        this.prev = prev;
        this.next = next;
    }
};