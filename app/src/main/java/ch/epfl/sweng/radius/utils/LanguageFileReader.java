package ch.epfl.sweng.radius.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LanguageFileReader {

    public static ArrayList<String> readLanguageFile(String file) {
        try {
            ArrayList<String> languages = new ArrayList<String>();
            File f = new File(file);
            Scanner scan = new Scanner(f);

            while (scan.hasNextLine()) {
                languages.add(scan.nextLine().trim());
            }

            return languages;
        } catch (Exception e) {
            return null;
        }
    }
}
