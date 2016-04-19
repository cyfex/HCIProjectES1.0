package chenyoufu.hciprojectes10;

/**
 * Created by Chen Youfu on 18/04/2016.
 */
public class Tuple3 {
    float x,y,z;

    public Tuple3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tuple3(HEVert vert) {
        this.x = vert.x;
        this.y = vert.y;
        this.z = vert.z;
    }

    public Tuple3 mul(float a) {
        return new Tuple3(a*x,a*y,a*z);
    }
};