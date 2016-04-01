package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.AddListener;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.Divider;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.DropsAdapter;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.SimpleTouchCallback;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private Button mBtnAdd=null;
    private ImageView ivBackground = null;
    private BucketRecyclerView recyclerView = null;
    private Realm mRealm = null;
    private RealmResults<Drop> mResults = null;
    private DropsAdapter mAdapter = null;
    private View emptyDrops = null;

    private View.OnClickListener mBtnAddListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();
        }
    };

    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            mAdapter.update(mResults);
        }
    };

    private AddListener mAddListener=new AddListener() {
        @Override
        public void add() {
            showDialog();
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
        emptyDrops = findViewById(R.id.emptyDrops);
        mBtnAdd=(Button) findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(mBtnAddListener);
        setSupportActionBar(toolbar);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        Glide.with(this).load(R.drawable.background).centerCrop().into(ivBackground);
        recyclerView = (BucketRecyclerView) findViewById(R.id.rvDrops);
        recyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hideIfEmpty(toolbar);
        recyclerView.showIfEmpty(emptyDrops);
        mAdapter = new DropsAdapter(this,mRealm, mResults,mAddListener);
        recyclerView.setAdapter(mAdapter);
        SimpleTouchCallback callback=new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void showDialog() {
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
