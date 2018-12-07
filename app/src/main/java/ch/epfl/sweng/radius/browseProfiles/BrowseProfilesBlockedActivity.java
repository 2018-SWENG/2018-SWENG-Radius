package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;

public class BrowseProfilesBlockedActivity extends BrowseProfilesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles_blocked);
        Intent intent = getIntent();

        userUID = intent.getStringExtra("UID");
        profileActivityListener = new BrowseProfilesUtility(userUID);
        toolbar = findViewById(R.id.toolbar);
    }
}
