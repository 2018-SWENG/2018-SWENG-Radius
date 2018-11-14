package ch.epfl.sweng.radius.utils.profileFragmentUtils;

import android.app.Activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import ch.epfl.sweng.radius.R;

public class TextFileReader {

    public static ArrayList<String> readLanguagesFromFile(Activity activity) {
        try {

            InputStream inputStream = activity.getResources().openRawResource(R.raw.languages);
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
}
