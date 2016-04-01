package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class DialogMark extends DialogFragment {

    private ImageButton mBtnClose = null;
    private Button mBtnCompleted = null;
    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCompleted:
                    //TODO Handle Mark Completed Action
                    break;
            }
            dismiss();
        }
    };

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
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);

        Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("POSITION");
            Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();
        }
    }
}
