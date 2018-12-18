package ch.epfl.sweng.radius.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.DBUserObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.storage.Storage;
import ch.epfl.sweng.radius.utils.profileFragmentUtils.TextFileReader;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements DBUserObserver {
    private final int MAX_SIZE_USERNAME = 20;
    private final int MAX_SIZE_STATUS = 50;
    private final int MAX_SIZE_INTERESTS = 100;
    private static int userRadius;

    CircleImageView userPhoto;
    TextView userNickname;
    TextView userStatus;
    TextInputEditText statusInput;
    TextInputEditText nicknameInput;
    SeekBar radiusBar;
    TextView radiusValue;
    MaterialButton saveButton;
    TextView userInterests;
    TextInputEditText interestsInput;
    TextView spokenLanguages;

    private Button selectLanguagesButton;
    private static ArrayList<String> selectableLanguages;
    private static boolean[] checkedLanguages;
    private static ArrayList<Integer> spokenLanguagesList;
    private static String languagesText;
    private static Uri mImageUri;

    public ProfileFragment() {
        spokenLanguagesList = new ArrayList<Integer>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() { // currently useless
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get the UI elements
        radiusBar = view.findViewById(R.id.radiusBar);
        radiusValue = view.findViewById(R.id.radiusValue);
        spokenLanguages =  view.findViewById(R.id.spokenLanguages);
        selectLanguagesButton = view.findViewById(R.id.languagesButton);
        userStatus = view.findViewById(R.id.userStatus);
        userNickname = view.findViewById(R.id.userNickname);
        userInterests = view.findViewById(R.id.userInterests);
        nicknameInput = view.findViewById(R.id.nicknameInput);
        statusInput = view.findViewById(R.id.statusInput);
        interestsInput = view.findViewById(R.id.interestsInput);
        saveButton = view.findViewById(R.id.saveButton);
        userPhoto = view.findViewById(R.id.userPhoto);

        // set a change listener on the SeekBar
        radiusBar.setOnSeekBarChangeListener(seekBarChangeListener);

        // Load the selectableLanguages
        selectableLanguages = TextFileReader.readLanguagesFromFile(getActivity());

        // Languages selection button
        checkedLanguages = new boolean[selectableLanguages.size()];
        selectLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBuilder();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSaveButton();
            }
        });

        // Load the users info and display
        setUpInfos();

        // Listen to changes in the DB
        UserInfo.getInstance().addUserObserver(this);

        // Inflate the layout for this fragment
        return view;
    }

    public void setUpInfos(){
        //Get the current user
        MLocation current_user = UserInfo.getInstance().getCurrentPosition();

        // Fill the labels with the user info
        userNickname.setText(current_user.getTitle());
        userStatus.setText(current_user.getMessage());
        userInterests.setText(current_user.getInterests());
        languagesText = current_user.getSpokenLanguages();
        spokenLanguages.setText(languagesText);
        radiusValue.setText(current_user.getRadius() + "m");
        radiusBar.setProgress((int) UserInfo.getInstance().getCurrentPosition().getRadius());

        setUpProfilePhoto();
    }

    private void createBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Pick the languages you speak");
        setBuilderMultiChoiceItems(builder);
        builder.setCancelable(true);
        setBuilderPositiveButton(builder);
        setBuilderNegativeButton(builder);
        setBuilderNeutralButton(builder);

        AlertDialog languageDialog = builder.create();
        languageDialog.show();
    }

    private void setBuilderMultiChoiceItems(AlertDialog.Builder builder) {
        builder.setMultiChoiceItems(selectableLanguages
                        .toArray(new String[selectableLanguages.size()])
                , checkedLanguages, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!spokenLanguagesList.contains(position)) {
                                spokenLanguagesList.add(new Integer(position));
                            }
                        } else if (spokenLanguagesList.contains(position)) {
                            spokenLanguagesList.remove(new Integer(position));
                        }
                    }
                });
    }

    private void setUpProfilePhoto() {
        MLocation current_user = UserInfo.getInstance().getCurrentPosition();

        if (current_user.getUrlProfilePhoto() != null && !current_user.getUrlProfilePhoto().equals("") && !Database.DEBUG_MODE) { // puts the image from database into the circle
            Picasso.get().load(current_user.getUrlProfilePhoto()).into(userPhoto);
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
    }

    private void setBuilderPositiveButton(AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String languagesText = UserInfo.getInstance().getCurrentPosition().getSpokenLanguages();
                for (int i = 0; i < spokenLanguagesList.size() ; i++) {
                    if (!languagesText.contains(selectableLanguages.get(spokenLanguagesList.get(i)))) {
                        languagesText = languagesText + " " + selectableLanguages.get(spokenLanguagesList.get(i));
                        UserInfo.getInstance().getCurrentPosition()
                                .addLanguage(selectableLanguages.get(spokenLanguagesList.get(i)));
                        if (i != spokenLanguagesList.size() - 1) {
                            languagesText = languagesText + " ";
                        }
                    }
                }
                spokenLanguages.setText(languagesText);
            }
        });
    }

    private void setBuilderNegativeButton(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void setBuilderNeutralButton(AlertDialog.Builder builder) {
        builder.setNeutralButton(R.string.clearAll_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkedLanguages.length; i++) {
                    checkedLanguages[i] = false;
                }
                spokenLanguagesList.clear();
                languagesText = "";
                spokenLanguages.setText(languagesText);
            }
        });
    }

    private String truncateText(String text, int maxSize){
        if(text.length()>maxSize){
            return text.substring(0,maxSize-1);
        }
        return text;
    }


    private void onClickSaveButton() { // use upload file here
        String nicknameString = getDataFromTextInput(nicknameInput);
        String statusString = getDataFromTextInput(statusInput);
        String interestsString = getDataFromTextInput(interestsInput);


        nicknameString = nicknameString.replaceAll("[^A-Za-z0-9_]", "");
        nicknameString = truncateText(nicknameString,MAX_SIZE_USERNAME);
        statusString = truncateText(statusString,MAX_SIZE_STATUS);
        interestsString = truncateText(interestsString,MAX_SIZE_INTERESTS);

        if (!nicknameString.isEmpty()) {
            UserInfo.getInstance().getCurrentPosition().setTitle(nicknameString);
            userNickname.setText(nicknameString);
        }
        if (!statusString.isEmpty()) {
            UserInfo.getInstance().getCurrentPosition().setMessage(statusString);
            userStatus.setText(statusString);
        }

        if (!interestsString.isEmpty()) {
            UserInfo.getInstance().getCurrentPosition().setInterests(interestsString);
            userInterests.setText("Interests: " + interestsString);
        }

        if (Storage.getInstance().getStorageTask() == null || !Storage.getInstance().getStorageTask().isInProgress()) { // Upload the photo and its uri to storage and db
            Storage.getInstance().uploadFile( mImageUri, this.getActivity());
        }

        UserInfo.getInstance().getCurrentPosition().setRadius(userRadius);
        UserInfo.getInstance().getCurrentPosition().setRadius(userRadius);
        UserInfo.getInstance().getCurrentPosition().setSpokenLanguages(languagesText);
        spokenLanguages.setText(languagesText);
        //Write to DB
        UserInfo.getInstance().updateUserInDB();UserInfo.getInstance().updateLocationInDB();
    }

    private String getDataFromTextInput(TextInputEditText input) {
        if (input != null) {
            Editable inputText = input.getText();
            if (inputText != null) {
                return inputText.toString();
            }
        }
        return "";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            mImageUri = intent.getData();

            try {
                Picasso.get().load(mImageUri).into(userPhoto);
            }// this is where we change the image - so use upload file method here
            catch (IllegalArgumentException e){
                Log.e("FirebaseStorage", " Image not found !");
            }
        }
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            radiusValue.setText(progress + " m");
            userRadius = progress;
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

    public String getLanguagesText() {
        return languagesText;
    }

    public void onUserChange(String id){
        setUpInfos();
    }
}
