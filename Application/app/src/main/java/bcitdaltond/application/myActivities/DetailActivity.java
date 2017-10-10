package bcitdaltond.application.myActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import bcitdaltond.application.R;
import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.Image;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "bcitdaltond.application.image.ID";

    private int id;
    private ImageView imageView;
    private TextView captionText;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Getting Intents
        Intent intent = getIntent();
        if (intent == null) {
            //failed to receive intent
            IOException e = new IOException();
            try {
                throw e;
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }
        id = intent.getIntExtra(EXTRA_MESSAGE, 0) + 1;
        Image image = DBHelper.getInstance(this).getImage("" + id);

        imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(image.getUri()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(thumbnail);

        captionText = (TextView) findViewById(R.id.captionText);
        captionText.setText(image.getCaption());

        dateText = (TextView) findViewById(R.id.dateText);
        String values[] = image.getDate().split("-");
        String month = getMonth(Integer.parseInt(values[0]));
        dateText.setText("" + month + ", " + values[1] + " " + values[2]);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GalleryActivity.class);
        getIntent().removeExtra(FilterActivity.EXTRA_MESSAGE);
        startActivity(intent);
        //super.onBackPressed();
    }

    private String getMonth(int number) {
        switch (number) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Undefined";
        }
    }

    public void removeDialog(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        removeImage();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    public void removeImage() {
        DBHelper.getInstance(this).removeImage("" + id);
        Intent intent = new Intent(this, GalleryActivity.class);
        getIntent().removeExtra(FilterActivity.EXTRA_MESSAGE);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        getIntent().removeExtra(FilterActivity.EXTRA_MESSAGE);
        startActivity(intent);
    }
}
