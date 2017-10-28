package bcitdaltond.application.myActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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

public class UploadActivity extends AppCompatActivity implements LocationListener {

    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private final int PLACE_PICKER_REQUEST = 2;
    protected LocationManager locationManager;

    private ImageView ivImage = null;
    private EditText editCaption = null;
    private TextView editDate = null;
    private TextView editLocation = null;

    private String dir = null;
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

        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        //Display location
        editLocation = (TextView) findViewById(R.id.locationText);
        editLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pickLocation();
                return false;
            }
        });
        editLocation.setText("0.0,0.0");
        getLocation();

        editDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //To show current date in the date picker

                //NOTE: Sets the initial date to today
                //Calendar mcurrentDate = Calendar.getInstance();
                //mYear = mcurrentDate.get(Calendar.YEAR);
                //mMonth = mcurrentDate.get(Calendar.MONTH);
                //mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

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
        editDate.setText("" + (mMonth + 1) + "-" + mDay + "-" + mYear);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "Current Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
        //locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        Log.d("Location: ", location.getLatitude() + ", " + location.getLongitude());
        double latitude = round(location.getLatitude(), 2);
        double longitude = round(location.getLongitude(), 2);
        editLocation.setText((latitude + "," + longitude));
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public void pickLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GalleryActivity.class);
        //TODO: REMOVE MESSAGES
        //getIntent().removeExtra(FilterActivity.EXTRA_MESSAGE);
        startActivity(intent);
        //super.onBackPressed();
    }

    public void setDate(int year, int month, int day) {
        //EXAMPLE Date Format:
        //"9-11-2017"
        mYear = year;
        mMonth = month;
        mDay = day;
        editDate.setText("" + (month + 1) + "-" + day + "-" + year  );
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
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("image/*");
//
//        //OLD:
//        //startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
//        //NEW:
//        startActivityForResult(intent, SELECT_FILE);

        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FILE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FILE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == PLACE_PICKER_REQUEST)
                onSelectLocation(data);
        }
    }

    private void onSelectLocation(Intent data) {
        Place place = PlacePicker.getPlace(data, this);
        Log.d("LOCATION!", "" + place.getLatLng());

        double latitude = round(place.getLatLng().latitude, 2);
        double longitude = round(place.getLatLng().longitude, 2);
        editLocation.setText(("" + latitude + "," + longitude));
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();

        //MUST HAVE PERMISSION!
        this.grantUriPermission(this.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        currentURI = uri;

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
        if (currentURI == null) {
            new AlertDialog.Builder(UploadActivity.this)
                    .setTitle("Missing Photo")
                    .setMessage("Must upload a photo!")
                    .setCancelable(true)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //ignored
                        }
                    })
                    .show();
        } else if (editCaption.getText().toString().equals("")) {
            new AlertDialog.Builder(UploadActivity.this)
                    .setTitle("Missing Input")
                    .setMessage("Uploaded photo's must have a Caption")
                    .setCancelable(true)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //ignored
                        }
                    })
                    .show();
        } else {
            DBHelper.getInstance(this).addImage(
                    new Image(
                            0,
                            currentURI.toString(),
                            editCaption.getText().toString(),
                            "" + (mMonth + 1) + "-" + mDay + "-" + mYear,
                            editLocation.getText().toString())
            );
            Intent intent = new Intent(this, GalleryActivity.class);
            //TODO: REMOVE MESSAGE
            //getIntent().removeExtra(FilterActivity.EXTRA_MESSAGE);
            startActivity(intent);
        }
    }
}
