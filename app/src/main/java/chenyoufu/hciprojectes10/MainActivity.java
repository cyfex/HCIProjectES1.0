package chenyoufu.hciprojectes10;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        super.onPause();
        mGLView.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

    @Override
    protected void onResume() {
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        super.onResume();
        mGLView.onResume();
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        boolean isFirst=true;
        float[] gDir=new float[3];
        float factorRot = 1f;
        final float alpha = 0.8f;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(isFirst){
                gDir[0]=event.values[0];
                gDir[1]=event.values[1];
                gDir[2]=event.values[2];
                isFirst=false;
            }
            else {
                float[] axis = Global.crossProductNV(gDir,event.values);
                float cita = (float)Math.acos(Global.innerProduct(event.values,gDir)/Global.norm(event.values)/Global.norm(gDir));
                float[][] transform = Global.genRotationMatrix(axis,cita);
                Global.cameraEye = Global.fArrayAdd(Global.vMulLeftMatrix(transform,Global.fArraySub(Global.cameraEye,Global.cameraCentre)),Global.cameraCentre);
                gDir[0]=event.values[0];
                gDir[1]=event.values[1];
                gDir[2]=event.values[2];

                /*float xTemp = event.values[0]-gDir[0];
                float yTemp = gDir[1]-event.values[1];
                gDir[0]=event.values[0];
                gDir[1]=event.values[1];
                gDir[2]=event.values[2];

                float cos_cita_x = (float)Math.cos(xTemp*factorRot);
                float sin_cita_x = (float)Math.sin(xTemp*factorRot);
                float temp_camera_eye_x = cos_cita_x*Global.cameraEye[0]-sin_cita_x*Global.cameraEye[2];
                Global.cameraEye[2] = sin_cita_x*Global.cameraEye[0] + cos_cita_x*Global.cameraEye[2];
                Global.cameraEye[0] = temp_camera_eye_x;

                float cos_cita_y = (float)Math.cos(yTemp*factorRot);
                float sin_cita_y = (float)Math.sin(yTemp*factorRot);
                float origin_camera_eye_x_z = (float)Math.sqrt((Global.cameraEye[0]-Global.cameraCentre[0])*(Global.cameraEye[0]-Global.cameraCentre[0])
                        +(Global.cameraEye[2]-Global.cameraCentre[2])*(Global.cameraEye[2]-Global.cameraCentre[2]));
                float temp_camera_eye_y = cos_cita_y*Global.cameraEye[1]-sin_cita_y*origin_camera_eye_x_z;
                float temp_camera_eye_x_z = sin_cita_y*Global.cameraEye[1] + cos_cita_y*origin_camera_eye_x_z;
                Global.cameraEye[0] *= temp_camera_eye_x_z/origin_camera_eye_x_z;
                Global.cameraEye[2] *= temp_camera_eye_x_z/origin_camera_eye_x_z;
                Global.cameraEye[1] = temp_camera_eye_y;*/

                /*gDir[0] = alpha * gDir[0] + (1 - alpha) * event.values[0];
                gDir[1] = alpha * gDir[1] + (1 - alpha) * event.values[1];
                gDir[2] = alpha * gDir[2] + (1 - alpha) * event.values[2];*/

                mGLView.requestRender();
            }
        }
    };

}