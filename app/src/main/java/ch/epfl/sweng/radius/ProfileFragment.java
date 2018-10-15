package ch.epfl.sweng.radius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import ch.epfl.sweng.radius.database.User;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    
    ImageView userPhoto;
    ImageButton changeProfilePictureButton;
    TextView userNickname;
    TextView userStatus;
    TextInputEditText statusInput;
    TextInputEditText nicknameInput;
    SeekBar radiusBar;
    TextView radiusValue;
    MaterialButton saveButton;

    private Uri profilePictureUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // set a change listener on the SeekBar
        radiusBar = view.findViewById(R.id.radiusBar);
        radiusBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = radiusBar.getProgress();
        radiusValue = view.findViewById(R.id.radiusValue);
        radiusValue.setText(progress + " Km");

        userPhoto = view.findViewById(R.id.userPhoto);

        if (profilePictureUri != null) {
            userPhoto.setImageURI(profilePictureUri);
        }

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Profile Picture"), 1);
            }
        });

        userNickname = view.findViewById(R.id.userNickname);
        userStatus = view.findViewById(R.id.userStatus);

        // Inflate the layout for this fragment
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            profilePictureUri = intent.getData();
            userPhoto.setImageURI(profilePictureUri);
        }
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            radiusValue.setText(progress + " Km");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (profilePictureUri != null) {
            outState.putParcelable("profilePictureUri", profilePictureUri);
        }
        if (userNickname != null) {
            outState.putCharSequence("userNickname", userNickname.getText());
        }
        if (userStatus != null) {
            outState.putCharSequence("userStatus", userStatus.getText());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            profilePictureUri = (Uri) savedInstanceState.getSerializable("profilePictureUri");
            if (userNickname != null) {
                userNickname.setText(savedInstanceState.getCharSequence("userNickname",
                        ""));
            }
            if (userStatus != null) {
                userNickname.setText(savedInstanceState.getCharSequence("userStatus",
                        ""));
            }
        }
    }
}
