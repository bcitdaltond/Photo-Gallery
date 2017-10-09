package bcitdaltond.application.myActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bcitdaltond.application.R;
import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.Image;

public class UploadActivity extends AppCompatActivity {

    private String dir = null;
    private ImageView ivImage = null;
    private EditText editCaption = null;
    private TextView editDate = null;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;

    private Uri currentURI = null;

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //Ignores URI Issue
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //Makes Directory if does not exist
        dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/PICTURES/";
        File newDirectory = new File(dir);
        newDirectory.mkdirs();

        //Get Views
        ivImage = (ImageView) findViewById(R.id.image);
        editCaption = (EditText) findViewById(R.id.captionText);
        editDate = (TextView) findViewById(R.id.dateText);
        editDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //To show current date in the date picker
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(UploadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        /*Your code to get date and times*/
                        setDate(year,month,day);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

                return false;
            }
        });
    }

    public void setDate(int year, int month, int day) {
        //"9-11-2017"
        editDate.setText("" + month + "-" + day + "-" + year  );
    }

    public void uploadOptions(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload");
        builder.setItems(new CharSequence[]
                        {"Take Picture", "Select Picture", "Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                takePicture();
                                break;
                            case 1:
                                getPicture();
                                break;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Creating file for image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(dir, "IMAGE_" + timeStamp + ".jpg");
        currentURI = Uri.fromFile(image);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentURI);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void getPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        currentURI = data.getData();
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), currentURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), currentURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
    }

    public void uploadPicture(View view) {
        //Do some database work here...
        DBHelper db = DBHelper.getInstance(this);
        DBHelper.getInstance(this).addImage(
                new Image(
                        0,
                        currentURI.toString(),
                        editCaption.getText().toString(),
                        "description",
                        "" + mMonth + "-" + mDay + "-" + mYear)
        );
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }
}
