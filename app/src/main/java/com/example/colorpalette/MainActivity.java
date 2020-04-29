package com.example.colorpalette;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import androidx.palette.graphics.Palette;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int PICK_IMAGE_REQUEST = 1;


    private TextView vibrantView;
    private TextView vibrantLightView;
    private TextView vibrantDarkView;
    private TextView mutedView;
    private TextView mutedLightView;
    private TextView mutedDarkView;
    private ImageView imageView;

    private ListView mListView;
    private SwatchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectImage = findViewById(R.id.uploadImage);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.image_view);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void get_color(View v)
    {
        initViews();
        paintTextBackground();

    }
    private void initViews() {

        imageView = findViewById(R.id.image_view);
        mListView = (ListView) findViewById( R.id.list );

    }
    public void setViewSwatch( TextView view, Palette.Swatch swatch ) {
        if( swatch != null ) {
            view.setTextColor( swatch.getTitleTextColor() );
            view.setBackgroundColor( swatch.getRgb() );
            view.setVisibility( View.VISIBLE );
        } else {
            view.setVisibility( View.GONE );
        }
    }

    private void paintTextBackground() {

        mAdapter = new SwatchAdapter( this );
        mListView.setAdapter( mAdapter );

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Palette.from(bitmap).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //work with the palette here
                for( Palette.Swatch swatch : palette.getSwatches() ) {
                    mAdapter.add(swatch);
                }

                mAdapter.sortSwatches();
                mAdapter.notifyDataSetChanged();

            }
        });

    }
}