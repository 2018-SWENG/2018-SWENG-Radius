package ch.epfl.sweng.radius.storage;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.StorageTask;

public abstract class Storage {
    protected static boolean DEBUG_MODE = false;
    private static Storage storage = null;
    protected StorageTask mUploadTask;

    //The files images will be uploaded to
    public enum StorageFile{
        PROFILE_PICTURES("profilePictures");

        private String name = "";

        StorageFile(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static Storage getInstance() {
        Log.e("DEBUG", "Value of debug is " + DEBUG_MODE);
        if (storage == null)
            return new FirebaseStorageUtility();

        return storage;
    }

    public static void activateDebugMode() {
        storage = new FakeFirebaseStorageUtility(); DEBUG_MODE = true;
    }

    public StorageTask getStorageTask() {
        return mUploadTask;
    }

    public abstract void uploadFile(Uri uri, Activity activity);
}
