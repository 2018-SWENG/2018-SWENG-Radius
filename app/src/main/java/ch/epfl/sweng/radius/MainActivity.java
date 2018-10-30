package ch.epfl.sweng.radius;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

/**
 *  The main activity of the whole app, just launches the app by navigating
 *  to the login activity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
