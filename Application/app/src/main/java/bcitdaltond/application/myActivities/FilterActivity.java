package bcitdaltond.application.myActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker; // "ui" is not resolved

import java.util.Calendar;

import bcitdaltond.application.R;

public class FilterActivity extends AppCompatActivity implements LocationListener {

    public static String EXTRA_CAPTION = "bcitdaltond.application.CAPTION";
    public static String EXTRA_DATE = "bcitdaltond.application.DATE";
    public static String EXTRA_STARTLOCATION = "bcitdaltond.application.STARTLOCATION";
    public static String EXTRA_ENDLOCATON = "bcitdaltond.application.ENDLOCATION";
    private int PLACE_START_PICKER_REQUEST = 1;
    private int PLACE_END_PICKER_REQUEST = 2;
    protected LocationManager locationManager;

    private EditText editCaption;
    private Button startLocationButton;
    private Button endLocationButton;

    private TextView startLocationText;
    private TextView endLocationText;
    private TextView editDate;

    private double sLongitude;
    private double sLatitude;
    private double eLongitude;
    private double eLatitude;

    private int mYear;
    private int mMonth;
    private int mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        editCaption = (EditText) findViewById(R.id.caption);
        editCaption.setEnabled(false);
        startLocationButton = (Button) findViewById(R.id.slocationbutton);
        startLocationButton.setEnabled(false);
        endLocationButton = (Button)  findViewById(R.id.elocationbutton);
        endLocationButton.setEnabled(false);

        startLocationText = (TextView)findViewById(R.id.startlocation);
        startLocationText.setText("0.0,0.0");
        startLocationText.setEnabled(false);
        endLocationText = (TextView)findViewById(R.id.endlocation);
        endLocationText.setText("0.0,0.0");
        endLocationText.setEnabled(false);
        editDate = (TextView) findViewById(R.id.dateText);
        editDate.setEnabled(false);

        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

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

                DatePickerDialog mDatePicker = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        editDate.setText(("" + (mMonth + 1) + "-" + mDay + "-" + mYear));
    }

    public void setDate(int year, int month, int day) {
        //EXAMPLE Date Format:
        //"9-11-2017"
        mYear = year;
        mMonth = month;
        mDay = day;
        editDate.setText(("" + (month + 1) + "-" + day + "-" + year));
    }


    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "Current Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
        //locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        Log.d("Location: ", location.getLatitude() + ", " + location.getLongitude());
        double latitude = round(location.getLatitude(), 2);
        double longitude = round(location.getLongitude(), 2);

        sLatitude = latitude;
        sLongitude = longitude;
        startLocationText.setText((sLatitude + "," + sLongitude));

        eLatitude = latitude;
        eLongitude = longitude;
        endLocationText.setText((eLatitude + "," + eLongitude));

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


    //Google Api Key: AIzaSyC_Vir7mv92A3kE7iY5QocaXH5TLiJkIZM
    public void pickStartLocation(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_START_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void pickEndLocation(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_END_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PLACE_START_PICKER_REQUEST) {
                onSelectStartLocation(data);
            } else if (requestCode == PLACE_END_PICKER_REQUEST) {
                onSelectEndLocation(data);
            }
        }
    }

    private void onSelectStartLocation(Intent data) {
        Place place = PlacePicker.getPlace(data, this);
        Log.d("LOCATION!", "" + place.getLatLng());

        double latitude = round(place.getLatLng().latitude, 2);
        double longitude = round(place.getLatLng().longitude, 2);
        sLatitude = latitude;
        sLongitude = longitude;
        startLocationText.setText(("" + latitude + "," + longitude));
    }

    private void onSelectEndLocation(Intent data) {
        Place place = PlacePicker.getPlace(data, this);
        Log.d("LOCATION!", "" + place.getLatLng());

        double latitude = round(place.getLatLng().latitude, 2);
        double longitude = round(place.getLatLng().longitude, 2);
        eLatitude = latitude;
        sLongitude = longitude;
        endLocationText.setText(("" + latitude + "," + longitude));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public boolean checkFilters() {
        String date = "0-0-0";
        String caption = "";
        String location = "";
        return true;
    }

//    public String getDate() {
//        DatePicker picker = (DatePicker) findViewById(R.id.datePicker);
//        return "" + (picker.getMonth() + 1) + "-" + picker.getDayOfMonth() + "-" + picker.getYear();
//    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.captionCheck:
                editCaption.setEnabled(checked);
                Log.d("CHECKBOX", "" + checked);
                break;
            case R.id.locationCheck:
                startLocationButton.setEnabled(checked);
                startLocationText.setEnabled(checked);
                endLocationButton.setEnabled(checked);
                endLocationText.setEnabled(checked);
                break;
            case R.id.dateCheck:
                editDate.setEnabled(checked);
                break;
        }
    }

    public void removeFilters() {
        getIntent().removeExtra(EXTRA_CAPTION);
        getIntent().removeExtra(EXTRA_DATE);
        getIntent().removeExtra(EXTRA_STARTLOCATION);
        getIntent().removeExtra(EXTRA_ENDLOCATON);
    }

    public void applyFilters(View view) {
        removeFilters();
        Intent intent = new Intent(this, GalleryActivity.class);
        if (editCaption.isEnabled()) {
            String caption = editCaption.getText().toString();
            if (!caption.isEmpty() || !caption.equals("")) {
                intent.putExtra(EXTRA_CAPTION, caption);
                Log.d("Caption: ", "INFO: " + caption);
                Log.d("EXTRA: ", "Caption Added To Intent");
            }
        }
        if (startLocationButton.isEnabled() || endLocationButton.isEnabled()) {
            intent.putExtra(EXTRA_STARTLOCATION, "" + sLatitude + "," + sLongitude);
            intent.putExtra(EXTRA_ENDLOCATON, "" + eLatitude + "," + eLongitude);
            Log.d("EXTRA: ", "Location Added To Intent");
        }
        if (editDate.isEnabled()) {
            intent.putExtra(EXTRA_DATE, "" + (mMonth + 1) + "-" + mDay + "-" + mYear);
            Log.d("EXTRA: ", "Date Added To Intent");
        }
        startActivity(intent);
    }
}
