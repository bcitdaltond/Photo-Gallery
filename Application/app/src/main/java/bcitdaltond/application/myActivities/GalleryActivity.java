package bcitdaltond.application.myActivities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bcitdaltond.application.R;
import bcitdaltond.application.myClasses.CreateList;
import bcitdaltond.application.myClasses.MyAdapter;
import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.Image;
import bcitdaltond.application.testActivities.MainActivity;

public class GalleryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String image_titles = "Default";
    private final Integer image_ids = R.drawable.icon;

//    private final String image_titles[] = {
//            "Img1",
//            "Img2",
//            "Img3",
//            "Img4",
//            "Img5",
//            "Img6",
//            "Img7",
//            "Img8",
//            "Img9",
//            "Img10",
//            "Img11",
//            "Img12",
//            "Img13",
//    };
//
//    private final Integer image_ids[] = {
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//            R.drawable.icon,
//    };
//
//    private final String image_dates[] = {
//            "9-11-2017",
//            "9-12-2017",
//            "9-12-2017",
//            "9-13-2017",
//            "9-14-2017",
//            "9-15-2017",
//            "9-16-2017",
//            "9-18-2017",
//            "9-19-2017",
//            "9-20-2017",
//            "9-20-2017",
//            "9-22-2017",
//            "9-23-2017",
//    };

    private String date = null;
    private static final int REQUEST_RUNTIME_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        checkPermissions();

        //Getting Intents
        Intent intent = getIntent();
        if (intent != null) {
            date = intent.getStringExtra(FilterActivity.EXTRA_MESSAGE);
        }

        //Image Gallery
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Dialog Box
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            // Ignored
        } else if (id == R.id.nav_filter) {
            Intent intent = new Intent(this, FilterActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Uses the hard coded images to create image gallery
     *
     * @return Image Array List
     */
    private ArrayList<CreateList> prepareData(){
//        images = dbHelper.getAllImages();
//        assertEquals(10, images.size());
//
//        images = dbHelper.getDateImages("9-11-2017");
//        Log.d("SIZE", "" + images.size());

        ArrayList<CreateList> theimage = new ArrayList<>();
        ArrayList<Image> images = DBHelper.getInstance(this).getAllImages();
        if (images.size() == 0) {
            CreateList createList = new CreateList();
            createList.setImage_title(image_titles);
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                    image_ids);
            createList.setImage_ID(icon);
            theimage.add(createList);
        } else {
            for(int i = 0; i < images.size(); i++){
                Log.d("DATE HERE:","    DATE: " + date);
                if (date != null) {
                    if (images.get(i).getDate().equals("" + date)) {
                        CreateList createList = new CreateList();
                        //Caption Name
                        createList.setImage_title(images.get(i).getCaption());
                        //Image
                        Uri image = Uri.parse(images.get(i).getUri());

                        Bitmap thumbnail = null;
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        createList.setImage_ID(thumbnail);
                        theimage.add(createList);
                    }
                } else {
                     CreateList createList = new CreateList();
                    //Caption Name
                    createList.setImage_title(images.get(i).getCaption());
                    //Image
                    Uri image = Uri.parse(images.get(i).getUri());

                    Bitmap thumbnail = null;
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    createList.setImage_ID(thumbnail);
                    theimage.add(createList);
                }
            }
        }

//
//        for(int i = 0; i< image_titles.length; i++){
//            //DBHelper.getInstance(this).addImage(new Image(0,image_ids[i], "caption","description", "date"));
//            Log.d("DATE HERE:","    DATE: " + date);
//            if (date != null) {
//                if (image_dates[i].equals("" + date)) {
//                    CreateList createList = new CreateList();
//                    createList.setImage_title(image_titles[i]);
//                    createList.setImage_ID(image_ids[i]);
//                    theimage.add(createList);
//                }
//            } else {
//                CreateList createList = new CreateList();
//                createList.setImage_title(image_titles[i]);
//                createList.setImage_ID(image_ids[i]);
//                theimage.add(createList);
//            }
//        }
        return theimage;
    }

    void checkPermissions() {
        //select which permission you want
        final String permission = Manifest.permission.CAMERA;
        //final String permission = Manifest.permission.Storage;
        // if in fragment use getActivity()
        if (ContextCompat.checkSelfPermission(GalleryActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this, permission)) {

            } else {
                ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            // you have permission go ahead
            //capturarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    // you have permission go ahead
                    //capturarFoto();
                }else{
                    // you dont have permission show toast
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}
