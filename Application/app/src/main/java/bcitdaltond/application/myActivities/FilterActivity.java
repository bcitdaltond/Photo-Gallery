package bcitdaltond.application.myActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import bcitdaltond.application.R;

public class FilterActivity extends AppCompatActivity {

    public static String EXTRA_MESSAGE = "bcitdaltond.application.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public String getDate() {
        DatePicker picker = (DatePicker) findViewById(R.id.datePicker);
        return "" + (picker.getMonth() + 1) + "-" + picker.getDayOfMonth() + "-" + picker.getYear();
    }

    public void applyFilters(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, getDate());
        startActivity(intent);
    }
}
