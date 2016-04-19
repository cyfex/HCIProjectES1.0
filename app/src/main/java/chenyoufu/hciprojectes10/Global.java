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
    public static float moveFactor = 1000f;

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

    public static float[] crossProductNV (float[] a, float[] b){
        float[] cp= new float[]{a[1]*b[2]-a[2]*b[1],a[2]*b[0]-a[0]*b[2],a[0]*b[1]-a[1]*b[0]};
        float area = (float) Math.sqrt(cp[0]*cp[0]+cp[1]*cp[1]+cp[2]*cp[2]);
        cp[0]/=area;
        cp[1]/=area;
        cp[2]/=area;
        return cp;
    }

    public static float innerProduct (float[]a, float[]b) {
        return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];
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

    public static float[] rotation (float[] vector, float[] axis, float cita){
        float axisLength = (float)Math.sqrt(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2]);
        float u = axis[0]/axisLength;
        float v = axis[1]/axisLength;
        float w = axis[2]/axisLength;
        float x = vector[0];
        float y = vector[1];
        float z = vector[2];

        float xPrime = (float)
                (u*(u*x + v*y + w*z)*(1d - Math.cos(cita))
                + x*Math.cos(cita)
                + (-w*y + v*z)*Math.sin(cita));
        float yPrime = (float)
                (v*(u*x + v*y + w*z)*(1d - Math.cos(cita))
                + y*Math.cos(cita)
                + (w*x - u*z)*Math.sin(cita));
        float zPrime = (float)
                (w*(u*x + v*y + w*z)*(1d - Math.cos(cita))
                + z*Math.cos(cita)
                + (-v*x + u*y)*Math.sin(cita));
        return new float[]{xPrime,yPrime,zPrime};
    }

    public static float[][] genRotationMatrix (float[] axis, float cita){
        float[][] m = new float[][]{
            {(float)(Math.cos(cita)+axis[0]*axis[0]*(1-Math.cos(cita))), (float)(axis[0]*axis[1]*(1-Math.cos(cita))-axis[2]*Math.sin(cita)), (float)(axis[0]*axis[2]*(1-Math.cos(cita))+axis[1]*Math.sin(cita))},
            {(float)(axis[1]*axis[0]*(1-Math.cos(cita))+axis[2]*Math.sin(cita)), (float)(Math.cos(cita)+axis[1]*axis[1]*(1-Math.cos(cita))), (float)(axis[1]*axis[2]*(1-Math.cos(cita))-axis[0]*Math.sin(cita))},
            {(float)(axis[2]*axis[0]*(1-Math.cos(cita))-axis[1]*Math.sin(cita)), (float)(axis[2]*axis[1]*(1-Math.cos(cita))+axis[0]*Math.sin(cita)), (float)(Math.cos(cita)+axis[2]*axis[2]*(1-Math.cos(cita)))}
        };
        return m;
    }

    public static float norm (float[] v){
        return (float)Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
    }

    public static float[] vMulLeftMatrix (float[][] m, float[] v){
        float[] c = new float[]{0f,0f,0f};
        for(int i=0;i<3;i++) {
            for(int j=0; j<3; j++) {
                c[i]+=m[i][j]*v[j];
            }
        }
        return c;
    }
}
