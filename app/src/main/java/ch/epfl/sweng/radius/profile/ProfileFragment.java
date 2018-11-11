package ch.epfl.sweng.radius.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.home.HomeFragment;
import ch.epfl.sweng.radius.utils.UserInfos;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;



public class ProfileFragment extends Fragment {

    private static Uri profilePictureUri;
    private static String userNicknameString;
    private static String userStatusString;

    CircleImageView userPhoto;
    ImageButton changeProfilePictureButton;
    TextView userNickname;
    TextView userStatus;
    TextInputEditText statusInput;
    TextInputEditText nicknameInput;
    SeekBar radiusBar;
    TextView radiusValue;
    MaterialButton saveButton;

    private Button selectLanguagesButton;
    private static ArrayList<String> selectableLanguages;
    private static boolean[] checkedLanguages;
    private static ArrayList<Integer> spokenLanguages;
    private static TextView selectedLanguages;
    private static String languagesText;

    public ProfileFragment() {
        spokenLanguages = new ArrayList<Integer>();
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

        System.out.println(readLanguagesFromFile().size());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // set a change listener on the SeekBar
        radiusBar = view.findViewById(R.id.radiusBar);
        radiusBar.setOnSeekBarChangeListener(seekBarChangeListener);
        radiusValue = view.findViewById(R.id.radiusValue);
        selectedLanguages =  view.findViewById(R.id.spokenLanguages);
        selectLanguagesButton = view.findViewById(R.id.languagesButton);


        //Get the instance of current user & attributes
        User currentUser = UserInfos.getCurrentUser();
        userNicknameString = currentUser.getNickname();
        userStatusString = currentUser.getStatus();
        languagesText = currentUser.getSpokenLanguages();

        int progress = radiusBar.getProgress();
        radiusValue.setText(progress + "Km");

        selectableLanguages = readLanguagesFromFile();

        if (languagesText == null) {
            languagesText = "";
        }
        selectedLanguages.setText(languagesText);

        checkedLanguages = new boolean[selectableLanguages.size()];
        selectLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBuilder();
            }
        });

        HomeFragment.newInstance(radiusBar.getProgress());
        radiusValue = view.findViewById(R.id.radiusValue);
        radiusValue.setText(progress + " Km");

        setUpProfilePhoto(view);
        setUpUserNickname(view);
        setUpUserStatus(view);
        setUpDataInput(view);

        // Inflate the layout for this fragment
        return view;
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
                            if (!spokenLanguages.contains(position)) {
                                spokenLanguages.add(new Integer(position));
                            }
                        } else if (spokenLanguages.contains(position)) {
                            spokenLanguages.remove(new Integer(position));
                        }
                    }
                });
    }

    private void setUpProfilePhoto(View view) {
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
    }

    private void setBuilderPositiveButton(AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < spokenLanguages.size() ; i++) {
                    if (!languagesText.contains(selectableLanguages.get(spokenLanguages.get(i)))) {
                        languagesText = languagesText + " " +selectableLanguages.get(spokenLanguages.get(i));
                        if (i != spokenLanguages.size() - 1) {
                            languagesText = languagesText + " ";
                        }
                    }
                }

                selectedLanguages.setText(languagesText);
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
                spokenLanguages.clear();
                languagesText = "";
                selectedLanguages.setText(languagesText);
            }
        });
    }

    private void setUpUserNickname(View view) {
        userNickname = view.findViewById(R.id.userNickname);

        if (userNicknameString != null) {
            userNickname.setText(userNicknameString);
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String defaultUserName = user.getDisplayName();
                userNickname.setText(defaultUserName);
            }
        }
    }

    private void setUpUserStatus(View view) {
        userStatus = view.findViewById(R.id.userStatus);

        if (userStatusString != null) {
            userStatus.setText(userStatusString);
        }
    }

    private void setUpDataInput(final View mainView) {
        nicknameInput = mainView.findViewById(R.id.nicknameInput);
        statusInput = mainView.findViewById(R.id.statusInput);
        saveButton = mainView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nicknameString = getDataFromTextInput(nicknameInput);
                String statusString = getDataFromTextInput(statusInput);
                if (!nicknameString.isEmpty()) {
                    userNicknameString = nicknameString;
                    setUpUserNickname(mainView);
                }
                if (!statusString.isEmpty()) {
                    userStatusString = statusString;
                    setUpUserStatus(mainView);
                }
            }
        });
    }

    private ArrayList<String> readLanguagesFromFile() {
        try {

            InputStream inputStream = getActivity().getResources().openRawResource(R.raw.languages);
            ArrayList<String> languages = new ArrayList<String>();

            Scanner scan = new Scanner(inputStream);

            while (scan.hasNext()) {
                languages.add(scan.nextLine().trim());
            }

            return languages;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
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
            profilePictureUri = intent.getData();
            userPhoto.setImageURI(profilePictureUri);
        }
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            radiusValue.setText(progress + " Km");

            HomeFragment.newInstance(radiusBar.getProgress());
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
            CharSequence toSet = savedInstanceState.getCharSequence("userNickname",
                    "");
            userNicknameString = toSet.toString();
            toSet = savedInstanceState.getCharSequence("userNickname",
                    "");
            userStatusString = toSet.toString();
        }
    }
}