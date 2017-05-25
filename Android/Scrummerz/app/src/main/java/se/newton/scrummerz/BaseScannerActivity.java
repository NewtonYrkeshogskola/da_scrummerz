package se.newton.scrummerz;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

@SuppressLint("Registered")
public class BaseScannerActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
