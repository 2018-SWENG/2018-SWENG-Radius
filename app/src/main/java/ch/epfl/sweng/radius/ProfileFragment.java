package ch.epfl.sweng.radius;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.InputStream;
import java.util.ArrayList;

import java.util.Scanner;



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

        int progress = radiusBar.getProgress();
        radiusValue.setText(progress + "Km");

        selectableLanguages = readLanguagesFromFile();
        //spokenLanguages = new ArrayList<Integer>();
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
        // Inflate the layout for this fragment
        return view;
    }

    private void createBuilder() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Pick the languages you speak");
            setBuilderMultiChoiceItems(builder);
            builder.setCancelable(false);
            setBuilderPositiveButton(builder);
            setBuilderNegativeButton(builder);
            setBuilderNeutralButton(builder);

            AlertDialog languageDialog = builder.create();
            languageDialog.show();
    }

    private void setBuilderMultiChoiceItems(AlertDialog.Builder builder) {
        builder.setMultiChoiceItems(selectableLanguages
                .toArray(new String[ProfileFragment.this.selectableLanguages.size()])
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
}
