package ch.epfl.sweng.radius.browseProfiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;

public abstract class BrowseProfilesActivity extends AppCompatActivity {
    protected BrowseProfilesUtility profileActivityListener;
    protected String userUID;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        setUpUnblockButton(menu);
        return true;
    }

    private void setUpUnblockButton(Menu menu) {
        if (UserInfo.getInstance().getCurrentUser().getBlockedUsers().contains(userUID)) {
            menu.getItem(0).getSubMenu().getItem(0).setTitle("Unblock User");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_user:
                if (item.getTitle().toString().trim().equals("Block User")) {
                    profileActivityListener.blockUser();
                    item.setTitle("Unblock User");
                } else if (item.getTitle().toString().trim().equals("Unblock User")) {
                    profileActivityListener.unblockUser();
                    item.setTitle("Block User");
                }
                return true;
            case R.id.spam:
                profileActivityListener.reportUser("spam");
                return true;
            case R.id.language:
                profileActivityListener.reportUser("language");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
