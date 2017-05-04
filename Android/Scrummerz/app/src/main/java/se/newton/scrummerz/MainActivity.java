package se.newton.scrummerz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.emailField)      EditText emailField;
    @BindView(R.id.passwordField)   EditText passwordField;
    @BindView(R.id.loginBtn)        Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        @BindView(R.id.loginBtn)        Button loginBtn;
//        Button button = (Button) findViewById(R.id.emailField);

//        En annan bra sak med butterknife är att man kan initiera buttons utanför onCreate metoden.
//        Om man inte kör butterknife så kan man endast deklarera utanför onCreate.


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
