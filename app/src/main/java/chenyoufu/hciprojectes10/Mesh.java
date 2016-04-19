package chenyoufu.hciprojectes10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Chen Youfu on 18/04/2016.
 */
public class Mesh {

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer normalBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[];/* = {
            // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f,// top
            -0.5f, -0.311004243f, 0.0f,// bottom left
            0.5f, -0.311004243f, 0.0f // bottom right
    };*/
    static float normalCoords[];

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Mesh() {
        triangleCoords = new float[Global.facesCount*9];
        for (int i=0; i<Global.facesCount; i++) {
            HEEdge tempEdge = Global.faces.get(i).edge;
            for (int j = 0; j < 3; j++) {
                HEVert tempVert = tempEdge.vert;
                triangleCoords[i * 9 + j * 3] = tempVert.x;
                triangleCoords[i * 9 + j * 3 + 1] = tempVert.y;
                triangleCoords[i * 9 + j * 3 + 2] = tempVert.z;
                tempEdge = tempEdge.next;
                //System.out.println(triangleCoords[i*9+j*3+2]);
            }
        }
        normalCoords = new float[Global.facesCount*9];
        for (int i=0; i<Global.facesCount; i++) {
            HEEdge tempEdge = Global.faces.get(i).edge;
            for (int j = 0; j < 3; j++) {
                HEVert tempVert = tempEdge.vert;
                normalCoords[i * 9 + j * 3] = Global.vertexNormals.get(tempVert).x;
                normalCoords[i * 9 + j * 3 + 1] = Global.vertexNormals.get(tempVert).y;
                normalCoords[i * 9 + j * 3 + 2] = Global.vertexNormals.get(tempVert).z;
                tempEdge = tempEdge.next;
            }
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);


        ByteBuffer aa = ByteBuffer.allocateDirect(normalCoords.length*4);
        aa.order(ByteOrder.nativeOrder());
        normalBuffer = aa.asFloatBuffer();
        normalBuffer.put(normalCoords);
        normalBuffer.position(0);

        /*for(int i=0; i< normalCoords.length; i++){
            System.out.println(aa.getFloat(i));
        }*/
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param gl - The OpenGL ES context in which to draw this shape.
     */
    public void draw(GL10 gl) {

        // Since this shape uses vertex arrays, enable them
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        // draw the shape
        gl.glColor4f(       // set color:
                color[0], color[1],
                color[2], color[3]);

        gl.glVertexPointer( // point to vertex data:
                COORDS_PER_VERTEX,
                GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT,0,normalBuffer);

        /*for(int i=0; i<Global.facesCount; i++) {
            gl.glNormal3f(Global.faceInfos.get(Global.faces.get(i)).normal.x,Global.faceInfos.get(Global.faces.get(i)).normal.y,Global.faceInfos.get(Global.faces.get(i)).normal.z);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 3*i, 3);
        }*/

        gl.glDrawArrays(    // draw shape:
                GL10.GL_TRIANGLES, 0,
                triangleCoords.length / COORDS_PER_VERTEX);

        // Disable vertex array drawing to avoid
        // conflicts with shapes that don't use it
        gl.glDisable(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
