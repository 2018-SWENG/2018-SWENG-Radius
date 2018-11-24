package ch.epfl.sweng.radius.storage;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;

public class FirebaseStorageUtility extends Storage{

    private StorageReference storageReference;
    protected StorageTask mUploadTask;

    public FirebaseStorageUtility() {
        storageReference = FirebaseStorage.getInstance().getReference(StorageFile.PROFILE_PICTURES.toString());
    }

    private String getFileExtension(Uri uri, Activity activity) {
        ContentResolver cR = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile(Uri mImageUri, Activity activity) {
        if (mImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri, activity));

            mUploadTask = fileReference.putFile(mImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uploadUrl = taskSnapshot.getUploadSessionUri().toString();//getDownloadUrl().toString();
                            User currentUser = UserInfo.getInstance().getCurrentUser();
                            currentUser.setUrlProfilePhoto(uploadUrl);
                            Database.getInstance().writeInstanceObj(currentUser, Database.Tables.USERS);
                        }
                    });
        }
    }
}
