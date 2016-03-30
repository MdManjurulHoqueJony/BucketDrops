package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.DropsAdapter;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private ImageView ivBackground = null;
    private RecyclerView recyclerView = null;
    private Realm mRealm = null;
    private RealmResults<Drop> mResults = null;
    private DropsAdapter dropsAdapter = null;

    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            dropsAdapter.update(mResults);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        Glide.with(this).load(R.drawable.background).centerCrop().into(ivBackground);
        recyclerView = (RecyclerView) findViewById(R.id.rvDrops);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        dropsAdapter=new DropsAdapter(this, mResults);
        recyclerView.setAdapter(dropsAdapter);
    }

    public void showDialog(View view) {
        AddDialogFragment addDialogFragment = new AddDialogFragment();
        addDialogFragment.show(getSupportFragmentManager(), "Add");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(realmChangeListener);
    }
}
