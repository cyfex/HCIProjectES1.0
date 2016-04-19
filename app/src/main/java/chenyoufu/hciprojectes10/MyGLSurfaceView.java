/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chenyoufu.hciprojectes10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.Vector;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;
    private ScaleGestureDetector mScaleDetector;

    private void readMesh(){
        try {
            InputStream meshIS = getResources().openRawResource(R.raw.eight);
            BufferedReader meshReader = new BufferedReader(new InputStreamReader(meshIS, "UTF-8"));
            String line;
            boolean isVertex=false;
            boolean isFace=false;
            while ((line=meshReader.readLine())!=null) {
                if(!isVertex&&!isFace) {
                    if (line.charAt(0)=='V') {
                        Global.verticesCount++;
                        isVertex=true;
                    }
                }
                else if (isFace) {
                    Global.facesCount++;
                }
                else {
                    if(line.charAt(0)=='F') {
                        Global.facesCount++;
                        isFace=true;
                    }
                    else {
                        Global.verticesCount++;
                    }
                }
            }
            meshReader.close();
            meshIS.close();

            Global.edgesCount = 3*Global.facesCount;

            //vertices.setSize(verticesCount);
            //faces.setSize(facesCount);
            //edges.setSize(edgesCount);
            for(int i=0; i<Global.edgesCount; i++)
                Global.edges.add(new HEEdge(null,null,null,null,null));

            // main loop, to assign elements to lists.
            //int vertexNum=0; // count from 1
            int faceNum=0; // count from 1
            //int edgeNum=0; // count from 1
            // restore loop state
            isVertex=false;
            isFace=false;

            meshIS = getResources().openRawResource(R.raw.eight);
            meshReader = new BufferedReader(new InputStreamReader(meshIS, "UTF-8"));
            while ((line=meshReader.readLine())!=null) {
                if(!isFace&&!isVertex) {
                    if (line.charAt(0)=='V') {
                        float x=0,y=0,z=0;
                        // fist vertex
                        String[] split = line.split("\\s+");
                        x=Float.parseFloat(split[2]);
                        y=Float.parseFloat(split[3]);
                        z=Float.parseFloat(split[4]);
                        Global.vertices.add(new HEVert(x, y, z, null));
                        // loop
                        isVertex=true;
                    }
                }
                else if (isFace) {
                    int v1=0,v2=0,v3=0;
                    String[] split = line.split("\\s+");
                    faceNum=Integer.parseInt(split[1]);
                    v1=Integer.parseInt(split[2]);
                    v2=Integer.parseInt(split[3]);
                    v3=Integer.parseInt(split[4]);

                    Global.faces.add(new HEFace(Global.edges.get(faceNum*3-3)));

                    Global.vertices.get(v1-1).edge = Global.edges.get(faceNum*3-3);
                    Global.vertices.get(v2-1).edge = Global.edges.get(faceNum*3-2);
                    Global.vertices.get(v3-1).edge = Global.edges.get(faceNum*3-1);

                    Global.edges.get(faceNum*3-3).vert=Global.vertices.get(v1-1);
                    Global.edges.get(faceNum*3-3).face=Global.faces.get(faceNum-1);
                    Global.edges.get(faceNum*3-3).prev=Global.edges.get(faceNum*3-1);
                    Global.edges.get(faceNum*3-3).next=Global.edges.get(faceNum*3-2);

                    Global.edges.get(faceNum*3-2).vert=Global.vertices.get(v2-1);
                    Global.edges.get(faceNum*3-2).face=Global.faces.get(faceNum-1);
                    Global.edges.get(faceNum*3-2).prev=Global.edges.get(faceNum*3-3);
                    Global.edges.get(faceNum*3-2).next=Global.edges.get(faceNum*3-1);

                    Global.edges.get(faceNum*3-1).vert=Global.vertices.get(v3-1);
                    Global.edges.get(faceNum*3-1).face=Global.faces.get(faceNum-1);
                    Global.edges.get(faceNum*3-1).prev=Global.edges.get(faceNum*3-2);
                    Global.edges.get(faceNum*3-1).next=Global.edges.get(faceNum*3-3);

                    Global.edgesTemp.put((v1-1)+"-"+(v2-1),faceNum*3-3);
                    Global.edgesTemp.put((v2-1)+"-"+(v3-1),faceNum*3-2);
                    Global.edgesTemp.put((v3-1)+"-"+(v1-1),faceNum*3-1);

                    // dealing with pair edge

                    Integer value = Global.edgesTemp.get(""+(v2-1)+"-"+(v1-1));
                    if (value!=null){
                        Global.edges.get(faceNum*3-3).pair=Global.edges.get(value);
                        Global.edges.get(value).pair=Global.edges.get(faceNum*3-3);
                    }
                    value = Global.edgesTemp.get(""+(v3-1)+"-"+(v2-1));
                    if (value!=null){
                        Global.edges.get(faceNum*3-2).pair=Global.edges.get(value);
                        Global.edges.get(value).pair=Global.edges.get(faceNum*3-2);
                    }
                    value = Global.edgesTemp.get(""+(v1-1)+"-"+(v3-1));
                    if (value!=null){
                        Global.edges.get(faceNum*3-1).pair=Global.edges.get(value);
                        Global.edges.get(value).pair=Global.edges.get(faceNum*3-1);
                    }
                }
                else {  // is_vertex
                    if(line.charAt(0)=='F') {
                        // first face
                        int v1=0,v2=0,v3=0;
                        String[] split = line.split("\\s+");
                        faceNum=Integer.parseInt(split[1]);
                        v1=Integer.parseInt(split[2]);
                        v2=Integer.parseInt(split[3]);
                        v3=Integer.parseInt(split[4]);

                        Global.faces.add(new HEFace(Global.edges.get(faceNum*3-3)));

                        Global.vertices.get(v1-1).edge = Global.edges.get(faceNum*3-3);
                        Global.vertices.get(v2-1).edge = Global.edges.get(faceNum*3-2);
                        Global.vertices.get(v3-1).edge = Global.edges.get(faceNum*3-1);

                        Global.edges.get(faceNum*3-3).vert=Global.vertices.get(v1-1);
                        Global.edges.get(faceNum*3-3).face=Global.faces.get(faceNum-1);
                        Global.edges.get(faceNum*3-3).prev=Global.edges.get(faceNum*3-1);
                        Global.edges.get(faceNum*3-3).next=Global.edges.get(faceNum*3-2);

                        Global.edges.get(faceNum*3-2).vert=Global.vertices.get(v2-1);
                        Global.edges.get(faceNum*3-2).face=Global.faces.get(faceNum-1);
                        Global.edges.get(faceNum*3-2).prev=Global.edges.get(faceNum*3-3);
                        Global.edges.get(faceNum*3-2).next=Global.edges.get(faceNum*3-1);

                        Global.edges.get(faceNum*3-1).vert=Global.vertices.get(v3-1);
                        Global.edges.get(faceNum*3-1).face=Global.faces.get(faceNum-1);
                        Global.edges.get(faceNum*3-1).prev=Global.edges.get(faceNum*3-2);
                        Global.edges.get(faceNum*3-1).next=Global.edges.get(faceNum*3-3);

                        Global.edgesTemp.put((v1-1)+"-"+(v2-1),faceNum*3-3);
                        Global.edgesTemp.put((v2-1)+"-"+(v3-1),faceNum*3-2);
                        Global.edgesTemp.put((v3-1)+"-"+(v1-1),faceNum*3-1);
                        // loop
                        isFace=true;
                    }
                    else {
                        float x=0,y=0,z=0;
                        String[] split = line.split("\\s+");
                        x=Float.parseFloat(split[2]);
                        y=Float.parseFloat(split[3]);
                        z=Float.parseFloat(split[4]);
                        Global.vertices.add(new HEVert(x, y, z, null));
                    }
                }
            }
            meshReader.close();
            meshIS.close();

            // Calculate bounding box
            float tempMinX=Float.MAX_VALUE,tempMinY=Float.MAX_VALUE,tempMinZ=Float.MAX_VALUE;
            float tempMaxX=Float.MIN_VALUE,tempMaxY=Float.MIN_VALUE,tempMaxZ=Float.MIN_VALUE;
            for(int i=0; i< Global.verticesCount; i++){
                if (Global.vertices.get(i).x>tempMaxX)
                    tempMaxX=Global.vertices.get(i).x;
                if (Global.vertices.get(i).y>tempMaxY)
                    tempMaxY=Global.vertices.get(i).y;
                if (Global.vertices.get(i).z>tempMaxZ)
                    tempMaxZ=Global.vertices.get(i).z;
                if (Global.vertices.get(i).x<tempMinX)
                    tempMinX=Global.vertices.get(i).x;
                if (Global.vertices.get(i).y<tempMinY)
                    tempMinY=Global.vertices.get(i).y;
                if (Global.vertices.get(i).z<tempMinZ)
                    tempMinZ=Global.vertices.get(i).z;
            }
            Global.maxX=tempMaxX;
            Global.minX=tempMinX;
            Global.maxY=tempMaxY;
            Global.minY=tempMinY;
            Global.maxZ=tempMaxZ;
            Global.minZ=tempMinZ;

            // Normalize the vertex coordinates (in the range of 0~2)
            float centroidX = (Global.maxX+Global.minX)/2;
            float rangeX = Global.maxX - centroidX;
            float centroidY = (Global.maxY+Global.minY)/2;
            float rangeY = Global.maxY - centroidY;
            float centroidZ = (Global.maxZ+Global.minZ)/2;
            float rangeZ = Global.maxZ - centroidZ;
            float tempRange;
            float maxRatio = (tempRange=rangeX>rangeY?rangeX:rangeY)>rangeZ?tempRange:rangeZ;
            for(int i=0; i<Global.verticesCount; i++){
                Global.vertices.get(i).x = (Global.vertices.get(i).x-(Global.minX+Global.maxX)/2)/maxRatio;
                Global.vertices.get(i).y = (Global.vertices.get(i).y-(Global.minY+Global.maxY)/2)/maxRatio;
                Global.vertices.get(i).z = (Global.vertices.get(i).z-(Global.minZ+Global.maxZ)/2)/maxRatio;
            }

            /*for (int i=0; i< Global.verticesCount; i++) {
                System.out.println(i+" "+Global.vertices.get(i).edge.face.edge.vert.x);
            }*/

            /*Global.maxX=(Global.maxX-Global.minX)/maxRatio;Global.minX=0;
            Global.maxY=(Global.maxY-Global.minY)/maxRatio;Global.minY=0;
            Global.maxZ=(Global.maxZ-Global.minZ)/maxRatio;Global.minZ=0;*/

            // Compute face normals & vertex normals
            for (int i=0; i<Global.facesCount; i++) {
                Tuple3 v1 = new Tuple3(Global.faces.get(i).edge.vert);
                Tuple3 v2 = new Tuple3(Global.faces.get(i).edge.next.vert);
                Tuple3 v3 = new Tuple3(Global.faces.get(i).edge.prev.vert);
                Tuple3 e1 = new Tuple3(v2.x-v1.x, v2.y-v1.y, v2.z-v1.z);
                Tuple3 e2 = new Tuple3(v3.x-v1.x, v3.y-v1.y, v3.z-v1.z);
                Tuple3 tempNormal=new Tuple3(e1.y*e2.z-e1.z*e2.y, e1.z*e2.x-e1.x*e2.z, e1.x*e2.y-e1.y*e2.x);
                float tempArea=(float) Math.sqrt(tempNormal.x*tempNormal.x+tempNormal.y*tempNormal.y+tempNormal.z*tempNormal.z);
                Global.faceInfos.put(Global.faces.get(i),new FaceInfo(tempArea,tempNormal));
            }
            for (int i=0; i<Global.verticesCount; i++) {

                HEFace faceNow = Global.vertices.get(i).edge.face;
                float totalArea=Global.faceInfos.get(faceNow).area;
                Tuple3 tempNormal=Global.faceInfos.get(faceNow).normal.mul(totalArea);
                Global.vertexNormals.put(Global.vertices.get(i),tempNormal);
                HEEdge edgeNow = Global.vertices.get(i).edge;
                boolean isReverse=false;
                while (true) {
                    if (!isReverse) {
                        if (edgeNow.pair!=null) {
                            if (edgeNow.pair.next!=Global.vertices.get(i).edge) {
                                edgeNow=edgeNow.pair.next;
                                Global.vertexNormals.get(Global.vertices.get(i)).x+=Global.faceInfos.get(edgeNow.face).area*Global.faceInfos.get(edgeNow.face).normal.x;
                                Global.vertexNormals.get(Global.vertices.get(i)).y+=Global.faceInfos.get(edgeNow.face).area*Global.faceInfos.get(edgeNow.face).normal.y;
                                Global.vertexNormals.get(Global.vertices.get(i)).z+=Global.faceInfos.get(edgeNow.face).area*Global.faceInfos.get(edgeNow.face).normal.z;
                                totalArea+=Global.faceInfos.get(edgeNow.face).area;
                            }
                            else
                                break;
                        }
                        else {
                            isReverse=true;
                            edgeNow = Global.vertices.get(i).edge;
                        }
                    }
                    else {
                        if (edgeNow.prev.pair!=null) {
                            edgeNow=edgeNow.prev.pair;
                            Global.vertexNormals.get(Global.vertices.get(i)).x+=Global.faceInfos.get(edgeNow.face).area*Global.faceInfos.get(edgeNow.face).normal.x;
                            Global.vertexNormals.get(Global.vertices.get(i)).y+=Global.faceInfos.get(edgeNow.face).area*Global.faceInfos.get(edgeNow.face).normal.y;
                            Global.vertexNormals.get(Global.vertices.get(i)).z+=Global.faceInfos.get(edgeNow.face).area*Global.faceInfos.get(edgeNow.face).normal.z;
                            totalArea+=Global.faceInfos.get(edgeNow.face).area;
                        }
                        else
                            break;
                    }
                }
                Global.vertexNormals.get(Global.vertices.get(i)).x/=totalArea;
                Global.vertexNormals.get(Global.vertices.get(i)).y/=totalArea;
                Global.vertexNormals.get(Global.vertices.get(i)).z/=totalArea;

                //Global.normalize(Global.vertexNormals.get(Global.vertices.get(i)));
            }

            /*for (int i=0; i< Global.verticesCount;i++) {
                System.out.println(Math.sqrt(Global.vertexNormals.get(Global.vertices.get(i)).x*Global.vertexNormals.get(Global.vertices.get(i)).x+Global.vertexNormals.get(Global.vertices.get(i)).y*Global.vertexNormals.get(Global.vertices.get(i)).y+Global.vertexNormals.get(Global.vertices.get(i)).z*Global.vertexNormals.get(Global.vertices.get(i)).z));
            }*/
            /*for (int i=0; i< Global.verticesCount; i++){
                System.out.println(i+" "+Global.vertexNormals.get(Global.vertices.get(i)).x+" "+Global.vertexNormals.get(Global.vertices.get(i)).y+" "+Global.vertexNormals.get(Global.vertices.get(i)).z);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyGLSurfaceView(Context context) {
        super(context);

        readMesh();

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float sf = detector.getScaleFactor();
            Global.cameraEye[0]+=(sf-1)*(Global.cameraCentre[0]-Global.cameraEye[0]);
            Global.cameraEye[1]+=(sf-1)*(Global.cameraCentre[1]-Global.cameraEye[1]);
            Global.cameraEye[2]+=(sf-1)*(Global.cameraCentre[2]-Global.cameraEye[2]);

            invalidate();
            return true;
        }
    }

    private float xOri = -1;
    private float yOri = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);
        int index = MotionEventCompat.getActionIndex(event);

        if (event.getPointerCount() > 1) {
            if(action==MotionEvent.ACTION_POINTER_UP && event.getPointerCount()==2){
                xOri = MotionEventCompat.getX(event,1-index);
                yOri = MotionEventCompat.getY(event,1-index);
            }
            mScaleDetector.onTouchEvent(event);
        }
        else {
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    xOri = MotionEventCompat.getX(event,index);
                    yOri = MotionEventCompat.getY(event,index);

                    break;
                case MotionEvent.ACTION_MOVE:
                    float xNow = MotionEventCompat.getX(event,index);
                    float yNow = MotionEventCompat.getY(event,index);

                    float[] xMove = Global.fArrayMul(Global.getCamCrossProductNV(),(xOri-xNow)/Global.moveFactor);
                    Global.cameraEye=Global.fArrayAdd(Global.cameraEye,xMove);
                    Global.cameraCentre=Global.fArrayAdd(Global.cameraCentre,xMove);
                    float[] yMove = Global.fArrayMul(Global.cameraUp,(yNow-yOri)/Global.moveFactor);
                    Global.cameraEye=Global.fArrayAdd(Global.cameraEye,yMove);
                    Global.cameraCentre=Global.fArrayAdd(Global.cameraCentre,yMove);
                    xOri=xNow;
                    yOri=yNow;
                    break;
                default:
                    break;
            }
        }
        requestRender();
        return true;
    }

}
