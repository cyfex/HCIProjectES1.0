package chenyoufu.hciprojectes10;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Chen Youfu on 18/04/2016.
 */
public class Global {
    public static final float PI = 3.14159265f;
    public static int verticesCount=0;
    public static int facesCount=0;
    public static int edgesCount=0;
    public static Vector<HEVert> vertices = new Vector<HEVert>();
    public static Vector<HEFace> faces = new Vector<HEFace>();
    public static Vector<HEEdge> edges = new Vector<HEEdge>();
    public static HashMap<HEFace, FaceInfo> faceInfos = new HashMap<HEFace, FaceInfo>();
    public static HashMap<HEVert, Tuple3> vertexNormals = new HashMap<HEVert, Tuple3>();
    public static HashMap<String, Integer> edgesTemp = new HashMap<String, Integer>();
    public static float maxX,maxY,maxZ,minX,minY,minZ;
    public static float ar;
    public static float[] cameraEye = {0f, 0f, 4f};
    public static float[] cameraCentre = {0f, 0f, 0f};
    public static float[] cameraUp = {0f, 1.0f, 0.0f};
    public static float[] lightAmbient = { 1.0f, 0.0f, 0.0f, 1.0f };
    public static float[] lightDiffuse = { 0.0f, 1.0f, 0.0f, 1.0f };
    public static float[] lightSpecular = { 0.0f, 0.0f, 1.0f, 1.0f };
    public static float[] lightPosition = { 4.0f, 4.0f, 4.0f, 0.0f };

    public static void normalize(Tuple3 t){
        double factor = Math.sqrt(t.x*t.x+t.y*t.y+t.z*t.z);
        t.x/=factor;
        t.y/=factor;
        t.z/=factor;
    }

    public static float[] getCamCrossProductNV (){
        float[] e1=fArraySub(cameraCentre,cameraEye);
        float[] e2=cameraUp;

        return crossProductNV(e1,e2);
    }

    private static float[] crossProductNV (float[] a, float[] b){
        float[] cp= new float[]{a[1]*b[2]-a[2]*b[1],a[2]*b[0]-a[0]*b[2],a[0]*b[1]-a[1]*b[0]};
        float area = (float) Math.sqrt(cp[0]*cp[0]+cp[1]*cp[1]+cp[2]*cp[2]);
        cp[0]/=area;
        cp[1]/=area;
        cp[2]/=area;
        return cp;
    }

    public static float[] fArrayMul (float[] input, float factor) {
        float[] output = new float[3];
        for(int i=0; i<3; i++)
            output[i]=input[i]*factor;
        return output;
    }

    public static float[] fArrayAdd (final float[] a, final float[] b) {
        float[] c = new float[3];
        for(int i=0; i<3; i++)
            c[i]=a[i]+b[i];
        return c;
    }

    public static float[] fArraySub (float[] a, float[] b) {
        float[] c = new float[3];
        for(int i=0; i<3; i++)
            c[i]=a[i]-b[i];
        return c;
    }
}
