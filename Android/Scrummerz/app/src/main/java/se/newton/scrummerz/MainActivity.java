package se.newton.scrummerz;

import android.os.Bundle;
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

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email, password;

    // Butterknife
    //@BindView(R.id.emailField)      EditText emailField;
    //@BindView(R.id.passwordField)   EditText passwordField;
    //@BindView(R.id.loginBtn)        Button loginBtn;
    //@BindView(R.id.mStatusTextView) TextView mStatusTextView;

    Button loginBtn;
    EditText emailField, passwordField;
    TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        // Kontrollera om användaren är inloggad (non-null). Om så är fallet, gör något.
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
                            updateUI(user);
                        } else {
                            // Om inloggningen misslyckades, meddela användaren
                            Log.w("Inloggning", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Autensiering misslyckades.",
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
            mStatusTextView.setText("INLOGGAD!");
        } else {
            // Här hanterar vi vad som händer om man inte är inloggad
        }
    }

}
