package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.services;

import android.app.IntentService;
import android.content.Intent;

import com.wordpress.jonyonandroidcraftsmanship.bucketdrops.beans.Drop;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("isCompleted", false).findAll();
            for (Drop current : results) {
                if (isNotificationNeeded(current.getAdded(), current.getWhen())) {

                }
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private boolean isNotificationNeeded(long added, long when) {
        long now = System.currentTimeMillis();
        if (now > when) {
            return false;
        } else {
            long difference90 = (long) (0.9 * (when - added));
            return (now > (added + difference90)) ? true : false;
        }
    }
}
