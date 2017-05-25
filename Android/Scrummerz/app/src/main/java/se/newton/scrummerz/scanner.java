package se.newton.scrummerz;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanner extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private static ZXingScannerView mainScanner;
    public static ProgressBar mProgressBar;
    private int REQUEST_CAMERA;


    String uid, name, classRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mRoot;
    DatabaseReference addPresence;
    Boolean active;

    SharedPreferences studentInfo;


    // Initiate view and add scanner
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        mProgressBar = (ProgressBar) findViewById(R.id.scanProgress);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        uid = currentUser.getUid();
        studentInfo = PreferenceManager.getDefaultSharedPreferences(this);
        classRef = studentInfo.getString("studentClass", "");
        name = studentInfo.getString("studentName", "");


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
        addPresence = mRoot.child("coursesByClass/").child(classRef).child(scanResultString);

        addPresence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                active = dataSnapshot.child("active").getValue().equals(true);
                if (!active) {
                    Toast.makeText(scanner.this, "Detta lektionstillfälle finns inte, eller är inte aktivt", Toast.LENGTH_LONG).show();
                } else {
                    addPresence.child(uid).setValue(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(scanner.this, "Kunde inte hämta från databasen", Toast.LENGTH_SHORT).show();
            }
        });
    }
}