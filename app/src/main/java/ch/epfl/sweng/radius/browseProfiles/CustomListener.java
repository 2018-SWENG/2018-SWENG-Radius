package ch.epfl.sweng.radius.browseProfiles;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesActivity;

public class CustomListener {

    public static void setCustomOnClick(View view, ImageView imageView, final int clickedPic,
                                        final String clickedName, final Context context){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BrowseProfilesActivity.class);
                intent.putExtra("Clicked Picture", clickedPic);
                intent.putExtra("Clicked Name", clickedName);
                context.startActivity(intent);
            }
        });
    }

}
