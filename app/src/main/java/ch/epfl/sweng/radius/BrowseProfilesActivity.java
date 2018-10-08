package ch.epfl.sweng.radius;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BrowseProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        Intent i = getIntent();
        int clickedPic = i.getIntExtra("Clicked Picture",1);
        String clickedName = i.getStringExtra("Clicked Name");

        ImageView imageView = findViewById(R.id.clickedPic);
        imageView.setImageResource(clickedPic);
        TextView textViewName = findViewById(R.id.clickedName);
        textViewName.setText(clickedName);
    }
}
