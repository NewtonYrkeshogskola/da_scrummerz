package se.newton.scrummerz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanner extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private static ZXingScannerView mainScanner;
    public static ProgressBar mProgressBar;
    private int REQUEST_CAMERA;


    // Initiate view and add scanner
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        mProgressBar = (ProgressBar) findViewById(R.id.scanProgress);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        // ZXing camera view for marking presence
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mainScanner = new ZXingScannerView(this);
        contentFrame.addView(mainScanner);

        // Check if camera permissions is set to allow, otherwise ask
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

    }

    // If access to camera is denied, toast and tell that it is needed
    @Override
    public void onRequestPermissionsResult(int cameraAllowed,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Du behöver ge tillgång till kameran för att scannern ska fungera", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mainScanner.setResultHandler(this);
        mainScanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainScanner.stopCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // This section runs after a successful scan
    @Override
    public void handleResult(Result scanResult) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        String scanResultString = scanResult.getText();

        Toast.makeText(this, scanResultString, Toast.LENGTH_SHORT).show();
//        mProgressBar = (ProgressBar) findViewById(R.id.scanProgress);
//        ScanResult.Result(cleanResultFromScanner, this);
    }

}