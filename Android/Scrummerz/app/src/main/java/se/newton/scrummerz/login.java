package se.newton.scrummerz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.newton.scrummerz.model.Student;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private String email, password;

    SharedPreferences studentInfo;

    Button loginBtn;
    EditText emailField, passwordField;
    TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRef = FirebaseDatabase.getInstance().getReference();

        studentInfo = PreferenceManager.getDefaultSharedPreferences(this);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        mStatusTextView = (TextView) findViewById(R.id.mStatusTextView);

        // Initiera Firebase-inloggning
        mAuth = FirebaseAuth.getInstance();
        // Slut på initiering


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailField.getText().toString(), passwordField.getText().toString());
            }
        });

    }

    // Kontrollera användaren
    @Override
    public void onStart() {
        super.onStart();
        // Kontrollera om användaren är inloggad (non-null). Om så är fallet, uppdatera UI.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // Slut på användarkontroll

    // Kör detta vid knapptryck
    private void signIn(String email, String password) {
        Log.d("inloggning", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // INLOGGNING MED E-POST
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inloggningen lyckades, uppdatera userUI med uppgifter
                            Log.d("inloggning", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            DatabaseReference localRef = mRef.child("users")
                                                             .child("students")
                                                             .child(uid)
                                                             .child("details");

                            localRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Student student = dataSnapshot.getValue(Student.class);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            })

                            updateUI(user);
                        } else {
                            // Om inloggningen misslyckades, meddela användaren
                            Log.w("Inloggning", "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Autensiering misslyckades.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // Om detta misslyckas
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        // Slut på misslyckande
                    }
                });
        // SLUT PÅ INLOGGNING MED E-POST
    }

    // Validera formuläret för inloggning
    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    // Hantera användaren vid inloggning
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Här hanterar vi vad som händer när man är inloggad
            Intent intent = new Intent(login.this, signedInStart.class);
            startActivity(intent);
        } else {
            // Här hanterar vi vad som händer om man inte är inloggad
            mStatusTextView.setText("Inte inloggad");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        studentInfo.edit().putString("studentName", student.getName()).apply();
//        studentInfo.edit().putString("studentClass", student.getmyClass()).apply();
//        studentInfo.edit().putString("studentPnr", student.getPnr()).apply();
//        studentInfo.edit().putString("studentUid",currentUser.getUid()).apply();
    }
}

}
