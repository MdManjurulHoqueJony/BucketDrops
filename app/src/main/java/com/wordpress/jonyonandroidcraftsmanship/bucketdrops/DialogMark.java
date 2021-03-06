package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.adapters.CompleteListener;

public class DialogMark extends DialogFragment {

    private ImageButton mBtnClose = null;
    private Button mBtnCompleted = null;
    private CompleteListener mCompleteListener = null;

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCompleted:
                    //TODO Handle Mark Completed Action
                    markAsComplete();
                    break;
            }
            dismiss();
        }
    };

    private void markAsComplete() {
        Bundle arguments = getArguments();
        if (mCompleteListener != null && arguments != null) {
            int position = arguments.getInt("POSITION");
            mCompleteListener.onComplete(position);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnCompleted = (Button) view.findViewById(R.id.btnCompleted);
        AppBucketDrops.setRalewayRegular(getActivity(), mBtnCompleted);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);
    }

    public void setCompleteListener(CompleteListener completeListener) {
        mCompleteListener = completeListener;
    }
}
