package com.secretcamera.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Khatri on 2/3/2016.
 */
public class CameraService extends Service {

    // Variables
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mCameraParameters;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if(mCamera == null) {
            mCamera = Camera.open();
        }

        // Initialize camera
        SurfaceView surfaceView = new SurfaceView(getApplicationContext());
        try {
            mCamera.setPreviewDisplay(surfaceView.getHolder());
            mCameraParameters = mCamera.getParameters();
            // Set camera parameters
            mCamera.setParameters(mCameraParameters);
            mCamera.startPreview();
            mCamera.takePicture(null, null, mCall);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize surface holder
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * Camera picture callback
     */
    Camera.PictureCallback mCall = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // Stop camera preview
            if(mCamera != null) {
                mCamera.stopPreview();
            }
            // Save captured image
            FileOutputStream outStream = null;
            try {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                outStream = new FileOutputStream(root +  "/" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                outStream.write(data);
                outStream.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                // Release camera
                if(mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                    stopSelf();
                }
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
