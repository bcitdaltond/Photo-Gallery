package bcitdaltond.application.myActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Filter;

import bcitdaltond.application.R;
import bcitdaltond.application.myClasses.CreateList;
import bcitdaltond.application.myClasses.MyAdapter;
import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.Image;


/**
 * Jenkins!
 */
public class GalleryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean filter = false;
    private String date = "";
    private String caption = "";
    private boolean locationFilter = false;
    private double halfdistanceLat = 0;
    private double halfdistanceLong = 0;
    private double middleLat = 0;
    private double middleLong = 0;

    private static final int REQUEST_RUNTIME_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //DBHelper.getInstance(this).dropAllTables();

        //Checks Permissions of the Application
        checkPermissions();

        //Getting Intents
        Intent intent = getIntent();
        if (intent != null) {
            //TODO: REMOVE MESSAGE
            if (intent.hasExtra(FilterActivity.EXTRA_DATE)){
                date = intent.getStringExtra(FilterActivity.EXTRA_DATE);
                Log.d("EXTRA", "FOUND DATE: " + date);
                filter = true;
            }
            if (intent.hasExtra(FilterActivity.EXTRA_CAPTION)) {
                caption = intent.getStringExtra(FilterActivity.EXTRA_CAPTION);
                Log.d("EXTRA", "FOUND CAPTION: " + caption);
                filter = true;
            }
            if (intent.hasExtra(FilterActivity.EXTRA_STARTLOCATION) && intent.hasExtra(FilterActivity.EXTRA_ENDLOCATON)) {
                String startLocation = intent.getStringExtra(FilterActivity.EXTRA_STARTLOCATION);
                String endLocation = intent.getStringExtra(FilterActivity.EXTRA_ENDLOCATON);
                Log.d("EXTRA", "FOUND LOCATION: " + startLocation + "-" + endLocation);

                //Parse Locations
                String[] location;

                location = startLocation.split(",");

                double sLatitude = Double.parseDouble(location[0]);
                double sLongitude = Double.parseDouble(location[1]);

                location = endLocation.split(",");
                double eLatitude = Double.parseDouble(location[0]);
                double eLongitude = Double.parseDouble(location[1]);

                //Half of the distance between the start and end points
                halfdistanceLat = (Math.abs(sLatitude - eLatitude))/2;
                halfdistanceLong = (Math.abs(sLongitude - eLongitude))/2;
                //Middle point between start and end
                middleLat = (sLatitude + eLatitude)/2;
                middleLong = (sLongitude + eLongitude)/2;
                if (middleLat == 0 && middleLong == 0) {
                    middleLat = sLatitude;
                    middleLong = sLongitude;
                }

                filter = true;
                locationFilter = true;
            }
        }

        //Image Gallery
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating Email Button
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
//        if (id == R.id.action_settings) {
//            return true;
//        }

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
            //Dummy Picture and Title Name
            final String image_titles = "Default";
            final Integer image_ids = R.drawable.icon;

            CreateList createList = new CreateList();
            createList.setImage_id(-1);
            createList.setImage_title(image_titles);
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                    image_ids);
            createList.setImage_bitmap(icon);
            theimage.add(createList);
        } else {
            if (!filter) {
                for(int i = 0; i < images.size(); i++) {
                    Log.d("DATE HERE","    DATE: " + date);
                    theimage.add(createThumbnailImage(i, images.get(i)));
                }
            } else {
                for(int i = 0; i < images.size(); i++) {
                    Log.d("DATE HERE","    DATE: " + date);
                    if (checkFilters(images.get(i))) {
                        theimage.add(createThumbnailImage(i, images.get(i)));
                    }
                }
            }
        }
        if (theimage.size() == 0) {
            //Dummy Picture and Title Name
            final String image_titles = "No Images Found";
            //final Integer image_ids = R.drawable.icon;

            CreateList createList = new CreateList();
            createList.setImage_id(-2);
            createList.setImage_title(image_titles);
            //Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
            //        image_ids);
            //createList.setImage_bitmap(icon);
            theimage.add(createList);
        }
        return theimage;
    }

    private CreateList createThumbnailImage(int id, Image i) {
        CreateList createList = new CreateList();
        //ID
        createList.setImage_id(id);
        //Caption Name
        createList.setImage_title(i.getCaption());
        //Image
        Uri image = Uri.parse(i.getUri());

        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        createList.setImage_bitmap(thumbnail);
        return createList;
    }

    private boolean checkFilters(Image i) {
        if (!date.isEmpty()) {
            if (!i.getDate().equals(date)) {
                return false;
            }
        }
        if (!caption.isEmpty()) {
            Log.d("Image Caption",i.getCaption());
            Log.d("Image Caption",caption);
            if (!i.getCaption().equals(caption)) {
                return false;
            }
        }
        if (locationFilter) {
            //Parse Location
            String[] location = i.getLocation().split(",");
            double latitude = Double.parseDouble(location[0]);
            double longitude = Double.parseDouble(location[1]);
            double distanceLat = Math.abs(latitude - middleLat);
            double distanceLong = Math.abs(longitude - middleLong);
            //Distance between middle and point must be within half the distance of start and end.
            if ((distanceLat > halfdistanceLat ) || (distanceLong > halfdistanceLong)) {
                return false;
            }
        }
        Log.d("Filter", "Image Passed");
        return true;
    }

    private void checkPermissions() {
        //select which permission you want
        final String permission = Manifest.permission.CAMERA;
        //final String permission = Manifest.permission.Storage;
        // if in fragment use getActivity()
        if (ContextCompat.checkSelfPermission(GalleryActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this, permission)) {

            } else {
                ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            Log.d("Permissions", "PASSED");
            // you have permission go ahead
            // ignored
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
                    // ignored
                }else{
                    // you dont have permission show toast
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}


//                else {
//                    CreateList createList = new CreateList();
//                    //ID
//                    createList.setImage_id(i);
//                    //Caption Name
//                    createList.setImage_title(images.get(i).getCaption());
//                    //Image
//                    Uri image = Uri.parse(images.get(i).getUri());
//
////                    Bitmap thumbnail = null;
////                    try {
////                        thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                    Bitmap thumbnail = null;
//                    try {
//                        InputStream image_stream = getContentResolver().openInputStream(image);
//                        thumbnail = BitmapFactory.decodeStream(image_stream );
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//                    createList.setImage_bitmap(thumbnail);
//                    theimage.add(createList);
//                }

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