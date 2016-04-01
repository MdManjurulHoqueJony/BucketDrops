package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.R;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;

import io.realm.Realm;
import io.realm.RealmResults;

public class DropsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final int ITEM = 0;
    public static final int FOOTER = 1;

    private LayoutInflater mLayoutInflater = null;
    private RealmResults<Drop> mResults = null;
    private AddListener mAddListener = null;
    private Realm mRealm = null;

    public DropsAdapter(Context context, Realm realm, RealmResults<Drop> results) {
        mLayoutInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }

    public DropsAdapter(Context context, Realm realm, RealmResults<Drop> results, AddListener listener) {
        this(context, realm, results);
        mAddListener = listener;
    }

    public void update(RealmResults<Drop> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mResults == null || position < mResults.size()) {
            return ITEM;
        } else {
            return FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM) {
            View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.tvWhat.setText(drop.getWhat());
        }
    }

    @Override
    public int getItemCount() {
        if (mResults == null || mResults.isEmpty()) {
            return 0;
        } else {
            return mResults.size() + 1;
        }
    }

    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).removeFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder {
        TextView tvWhat = null;

        public DropHolder(View itemView) {
            super(itemView);
            tvWhat = (TextView) itemView.findViewById(R.id.tvWhat);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnFooter = null;
        AddListener mListener = null;

        public FooterHolder(View itemView) {
            super(itemView);
            btnFooter = (Button) itemView.findViewById(R.id.btnFooter);
            btnFooter.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener listener) {
            this(itemView);
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.add();
        }
    }
}
