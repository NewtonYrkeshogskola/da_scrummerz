package se.newton.scrummerz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.newton.scrummerz.model.Student;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Intent signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lagrar data lokalt på mobilen när mobilen är offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        signedIn = new Intent(MainActivity.this, signedInStart.class);

        // Initiera Firebase-inloggning
        mAuth = FirebaseAuth.getInstance();

        // Om ingen är inloggad, hantera knapptryck för att logga in
        Button signIn = (Button) findViewById(R.id.goToLogin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
    }


    // Hantera användaren vid inloggning
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Användaren är redan inloggad, skicka vidare
            startActivity(signedIn);
        } else {
            // Användaren är inte inloggad, så gör inget
        }
    }

    // Kontrollera om användaren redan är inloggad
    @Override
    public void onStart() {
        super.onStart();

        // Kontrollera om användaren är inloggad (non-null).
        // Om så är fallet, flytta direkt till inloggat läge.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
}


//TODO: Bättre felhantering vid inloggningsproblem (t.ex. ingen internetanslutning)