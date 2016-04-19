package chenyoufu.hciprojectes10;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    //private Triangle mTriangle;
    //private Square mSquare;
    private Mesh mMesh;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mMesh = new Mesh();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // Draw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LESS);
        gl.glShadeModel(GL10.GL_SMOOTH);

        // Set GL_MODELVIEW transformation mode
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();   // reset the matrix to its default state

        // When using GL_MODELVIEW, you must set the view point
        GLU.gluLookAt(gl, Global.cameraEye[0], Global.cameraEye[1], Global.cameraEye[2],
                Global.cameraCentre[0], Global.cameraCentre[1], Global.cameraCentre[2],
                Global.cameraUp[0], Global.cameraUp[1], Global.cameraUp[2]);

        mMesh.draw(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes
        // such as screen rotations
        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        Global.ar = (float) width / height;
        //float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        //gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
        gl.glFrustumf(-Global.ar, Global.ar, -1.0f, 1.0f, 3f, 10f);
        //gl.glOrthof(-2,2,-2,2,2,100);

        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_LESS);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LESS);
        gl.glEnable(GL10.GL_NORMALIZE);
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_AMBIENT,Global.lightAmbient,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_DIFFUSE,Global.lightDiffuse,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_SPECULAR,Global.lightSpecular,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_POSITION,Global.lightPosition,0);
        //gl.glShadeModel(GL10.GL_SMOOTH);

    }

}
