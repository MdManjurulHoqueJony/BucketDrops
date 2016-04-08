package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.AddListener;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.CompleteListener;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.Divider;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.DropsAdapter;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.Filter;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.MarkListener;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.SimpleTouchCallback;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private Button mBtnAdd = null;
    private ImageView ivBackground = null;
    private BucketRecyclerView recyclerView = null;
    private Realm mRealm = null;
    private RealmResults<Drop> mResults = null;
    private DropsAdapter mAdapter = null;
    private View emptyDrops = null;

    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {
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

    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialog();
        }
    };

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            mAdapter.markComplete(position);
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
        int filterOption = load();
        loadResults(filterOption);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        emptyDrops = findViewById(R.id.emptyDrops);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
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
        mAdapter = new DropsAdapter(this, mRealm, mResults, mAddListener, mMarkListener);
        recyclerView.setAdapter(mAdapter);
        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void showDialog() {
        AddDialogFragment addDialogFragment = new AddDialogFragment();
        addDialogFragment.show(getSupportFragmentManager(), "Add");
    }

    private void showDialogMark(int position) {
        DialogMark dialogMark = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialogMark.setArguments(bundle);
        dialogMark.setCompleteListener(mCompleteListener);
        dialogMark.show(getSupportFragmentManager(), "Mark");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;
        switch (id) {
            case R.id.actionAdd:
                showDialog();
                break;
            case R.id.actionSortAscendingDate:
                filterOption=Filter.LEAST_TIME_LEFT;
                save(filterOption);
                break;
            case R.id.actionSortDescendingDate:
                filterOption=Filter.MOST_TIME_LEFT;
                save(filterOption);
                break;
            case R.id.actionShowComplete:
                filterOption=Filter.COMPLETE;
                save(filterOption);
                break;
            case R.id.actionShowIncomplete:
                filterOption=Filter.INCOMPLETE;
                save(filterOption);
                break;
            default:
                handled = false;
                break;
        }
        loadResults(filterOption);
        return handled;
    }

    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("isCompleted", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("isCompleted", false).findAllAsync();
                break;
            default:
                break;
        }
        mResults.addChangeListener(realmChangeListener);
    }

    private void save(int filterOption) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("filter", filterOption);
        editor.apply();
    }

    private int load() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int filterOption = preferences.getInt("filter", Filter.NONE);
        return filterOption;
    }
}
