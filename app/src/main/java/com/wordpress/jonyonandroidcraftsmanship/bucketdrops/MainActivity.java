package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private ImageView ivBackground = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        Glide.with(this).load(R.drawable.background).centerCrop().into(ivBackground);
    }

    public void showDialog(View view) {
        AddDialogFragment addDialogFragment=new AddDialogFragment();
        addDialogFragment.show(getSupportFragmentManager(),"Add");
    }
}
