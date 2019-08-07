package com.example.dan.googlevisionqr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {
    private TextView qrcode;
    private EditText qrtext;
    private ImageView qrimage;
    private ProgressBar progressBar;
    public static final int REQUEST_SCAN = 1;
    public static final int REQUEST_CAMERA_ACCESS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button scanButton;
        Button genButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button) findViewById(R.id.scanbutton);
        genButton = (Button) findViewById(R.id.generatebutton);
        qrtext = (EditText) findViewById(R.id.qrtext);
        qrcode = (TextView) findViewById(R.id.textview);
        qrimage = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_ACCESS);
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent intent = new Intent(MainActivity.this, QRScan.class);
                                              startActivityForResult(intent, REQUEST_SCAN);
                                          }
                                      }
        );
        genButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if (TextUtils.isEmpty(qrtext.getText().toString())) {
                                                 qrtext.setError("Please enter text you want to encode into a QR code");
                                             } else {
                                                 //remove any previous QR code being displayed
                                                 qrimage.setImageBitmap(null);
                                                 //pre
                                                 qrtext.setEnabled(false);
                                                 //run time consuming tasks on new thread so app doesn't freeze
                                                 new Thread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         try {
                                                             //time consuming task
                                                             //encode the entered text into QR Code
                                                             final Bitmap qr = generateQRCode(qrtext.getText().toString());
                                                             qrimage.post(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     runOnUiThread(new Runnable() {
                                                                         @Override
                                                                         public void run() {
                                                                             progressBar.setVisibility(View.INVISIBLE);
                                                                             qrtext.setEnabled(true);
                                                                         }
                                                                     });
                                                                     qrimage.setImageBitmap(qr);
                                                                 }
                                                             });
                                                         } catch (Exception e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 }).start();
                                                 qrimage.setVisibility(view.VISIBLE);

                                             }
                                         }
                                     }
        );
    }

    //decodes QR Code
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (data != null) {
            final Barcode qr = data.getParcelableExtra("QR code");
            qrcode.setText(qr.displayValue);
            Toast.makeText(MainActivity.this, (qr.displayValue), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    // generates QR Code
    private Bitmap generateQRCode(String answer) throws Exception {
        BitMatrix qrCode = null;
        try {
            qrCode = new MultiFormatWriter().encode(answer, BarcodeFormat.QR_CODE, 700, 700);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to encode QR code", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
        int width = qrCode.getWidth();
        int height = qrCode.getHeight();
        progressBar.setMax(height);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < height; y++) {
            progressBar.setProgress(y);
            for (int x = 0; x < width; x++) {
                //for each pixel in the bitmatrix if its 1 then set that pixel colour in the bitmap to black else set it to white
                bitmap.setPixel(x, y, qrCode.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bitmap;
    }
}

