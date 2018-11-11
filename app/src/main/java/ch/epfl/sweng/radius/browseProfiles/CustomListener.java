package ch.epfl.sweng.radius.browseProfiles;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class CustomListener {
    private int clickedPic;
    private String clickedName;

    public CustomListener(int clickedPic, String clickedName){
        this.clickedPic = clickedPic;
        this.clickedName = clickedName;
    }

    public void setCustomOnClick(ImageView imageView, final Context context){
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
