package bcitdaltond.application.myClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import bcitdaltond.application.R;
import bcitdaltond.application.myActivities.DetailActivity;
import bcitdaltond.application.myActivities.GalleryActivity;
import bcitdaltond.application.myActivities.UploadActivity;

/**
 * Created by runej on 2017-10-02.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<CreateList> galleryList;
    private Context context; //unused

    public MyAdapter(Context context, ArrayList<CreateList> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int i) {
        final int current_image = i;
        viewHolder.title.setText(galleryList.get(i).getImage_title());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //Change~!
        //viewHolder.img.setImageResource((galleryList.get(i).getImage_ID()));
        //------------------------
        //TO:
        viewHolder.img.setImageBitmap(galleryList.get(i).getImage_bitmap());
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateList createList = galleryList.get(current_image);
                if (createList.getImage_id() == -1) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Default")
                            .setMessage("Please upload a picture before viewing details")
                            .setCancelable(true)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ignored
                                }
                            })
                            .show();
                } else if (createList.getImage_id() == -2) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Filter")
                            .setMessage("No Images were found using current filters")
                            .setCancelable(true)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ignored
                                }
                            })
                            .show();
                } else {
                    String EXTRA_MESSAGE = "bcitdaltond.application.image.ID";
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, createList.getImage_id());
                    v.getContext().startActivity(intent);

                    //TESTING Click:
                    //Toast.makeText(context,"" + galleryList.get(current_image).getImage_id(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;

        ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}