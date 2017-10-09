package bcitdaltond.application.testActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import bcitdaltond.application.R;
import bcitdaltond.application.myActivities.GalleryActivity;
import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.User;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_MESSAGE = "com.example.runej.testdrivenlab.MESSAGE";

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DBHelper.getInstance(this);
    }

    public void addUser() {
        db = DBHelper.getInstance(this);
        User a = new User(0,"bob", "bob123", "bob", "bobby");
        db.addUser(a);
    }

    public String getUser() {
        User b = db.getUser("0");
        return b.getFirstname();
    }

    public void sendMessage(View view) {
//        Intent intent = new Intent(this, MessageViewActivity.class);
//        EditText editText = (EditText)  findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);

    }
}
