package com.example.dan.googlevisionqr;

/**
 * Created by dan on 06/03/2017.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.IOException;

public class QRScan extends AppCompatActivity {
    private SurfaceView scanView;
    private BarcodeDetector qr;
    private CameraSource camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        scanView = (SurfaceView) findViewById(R.id.surfaceView);
        qr = new BarcodeDetector.Builder(QRScan.this).setBarcodeFormats(Barcode.QR_CODE).build();
        if (qr.isOperational()) {
            camera = new CameraSource.Builder(QRScan.this, qr).setFacing(0).setAutoFocusEnabled(true).build();
        }

        scanView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                startCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                startCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                camera.stop();
            }
        });

        qr.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if (qrCode.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("QR code", qrCode.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void startCamera() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                camera.start(scanView.getHolder());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
