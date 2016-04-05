package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.R;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;
import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.extras.Util;

import io.realm.Realm;
import io.realm.RealmResults;

public class DropsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final int ITEM = 0;
    public static final int FOOTER = 1;

    private LayoutInflater mLayoutInflater = null;
    private RealmResults<Drop> mResults = null;
    private AddListener mAddListener = null;
    private MarkListener mMarkListener = null;
    private Realm mRealm = null;

    public DropsAdapter(Context context, Realm realm, RealmResults<Drop> results) {
        mLayoutInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }

    public DropsAdapter(Context context, Realm realm, RealmResults<Drop> results, AddListener listener, MarkListener markListener) {
        this(context, realm, results);
        mAddListener = listener;
        mMarkListener = markListener;
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
            return new DropHolder(view, mMarkListener);
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
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setBackground(drop.isCompleted());
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

    public void markComplete(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setIsCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvWhat = null;
        TextView tvWhen = null;
        MarkListener mMarkListener = null;
        Context mContext = null;
        View mItemView=null;
        public DropHolder(View itemView, MarkListener markListener) {
            super(itemView);
            mItemView=itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            tvWhat = (TextView) itemView.findViewById(R.id.tvWhat);
            tvWhen = (TextView) itemView.findViewById(R.id.tvWhen);
            mMarkListener = markListener;
        }

        public void setWhat(String what) {
            tvWhat.setText(what);
        }

        @Override
        public void onClick(View view) {
            mMarkListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            Drawable drawable=null;
            if (completed) {
                drawable= ContextCompat.getDrawable(mContext,R.color.bg_drop_complete);
            }else{
                drawable= ContextCompat.getDrawable(mContext,R.color.bg_drop_row_light);
            }
            Util.setBackground(mItemView,drawable);
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
