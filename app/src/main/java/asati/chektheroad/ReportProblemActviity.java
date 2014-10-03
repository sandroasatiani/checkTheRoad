package asati.chektheroad;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ani110ani on 10/2/14.
 */
public class ReportProblemActviity extends Activity {

    public static final int PROBLEM_TYPE_DAMAGED_ROAD = 0;
    public static final int PROBLEM_TYPE_PIPELINE = 1;
    public static final int PROBLEM_TYPE_GARBAGE = 2;
    public static final int PROBLEM_TYPE_OTHER = 3;
    public static final String EXTRA_PROBLEM_TYPE = BuildConfig.PACKAGE_NAME + ".extra.PROBLEM_TYPE";

    private static final int REQUEST_CODE_MAKE_CAMERA_PICTURE = 1;
    private static final String STATE_PICTURE_FILE = "file";
    public static final String EXTRA_LOCATION = "extra_location";

    @InjectView(R.id.report_problem_photo)
    ImageView problemImage;

    private File mCameraPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        ButterKnife.inject(this);
        int problemType = getIntent().getIntExtra(EXTRA_PROBLEM_TYPE, PROBLEM_TYPE_OTHER);
        if (problemType == PROBLEM_TYPE_DAMAGED_ROAD) {
            // your logic here
        } else if (problemType == PROBLEM_TYPE_PIPELINE) {
            // your logic here
        } // else ...

        if (savedInstanceState != null) {
            mCameraPictureFile = (File) savedInstanceState.getSerializable(STATE_PICTURE_FILE);
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mCameraPictureFile != null) {
            problemImage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCameraPicture();
                }
            }, 5000);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_PICTURE_FILE, mCameraPictureFile);
    }

    @OnClick(R.id.report_problem_photo)
    public void onClickAddPhoto() {
        dispatchTakePictureIntent();
    }

    @OnClick(R.id.button)
    public void onSendButtonClicked() {
        int problemType = getIntent().getIntExtra(EXTRA_PROBLEM_TYPE, PROBLEM_TYPE_OTHER);
        LatLng location = getIntent().getParcelableExtra(EXTRA_LOCATION);
        Database.putToMap(location, Database.Type.values()[problemType]);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MAKE_CAMERA_PICTURE) {
            // handle camera picture here
            setCameraPicture();
        }
    }

    private void setCameraPicture() {
        Picasso.with(this).load(mCameraPictureFile).centerCrop().resize(problemImage.getWidth(), problemImage.getHeight()).into(problemImage);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                mCameraPictureFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraPictureFile));
                startActivityForResult(takePictureIntent, REQUEST_CODE_MAKE_CAMERA_PICTURE);
            } catch (IOException ex) {
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
