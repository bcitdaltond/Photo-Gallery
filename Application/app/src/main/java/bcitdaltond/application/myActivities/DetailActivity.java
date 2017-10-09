package bcitdaltond.application.myActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import bcitdaltond.application.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    public void back(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

}
