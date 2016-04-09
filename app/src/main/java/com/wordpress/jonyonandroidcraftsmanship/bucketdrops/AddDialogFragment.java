package com.wordpress.jonyonandroidcraftsmanship.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;

import java.util.Calendar;

import io.realm.Realm;

public class AddDialogFragment extends DialogFragment {

    private ImageButton ibtnClose = null;
    private EditText etDrop = null;
    private DatePicker bpvDate = null;
    private Button btnAdd = null;
    private MyClickListener myClickListener = null;

    public AddDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myClickListener = new MyClickListener();
        ibtnClose = (ImageButton) view.findViewById(R.id.ibtnClose);
        etDrop = (EditText) view.findViewById(R.id.etDrop);
        bpvDate = (DatePicker) view.findViewById(R.id.bpvDate);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        ibtnClose.setOnClickListener(myClickListener);
        btnAdd.setOnClickListener(myClickListener);
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnAdd:
                    addAction();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    //TODO Process Date
    private void addAction() {
        String what = etDrop.getText().toString();
        String date = bpvDate.getDayOfMonth() + "/" + bpvDate.getMonth() + "/" + bpvDate.getYear();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,bpvDate.getDayOfMonth());
        calendar.set(Calendar.MONTH,bpvDate.getMonth());
        calendar.set(Calendar.YEAR,bpvDate.getYear());
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        long now = System.currentTimeMillis();

        Realm realm = Realm.getDefaultInstance();

        Drop drop = new Drop(what, now, calendar.getTimeInMillis(), false);
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }
}
