package com.firstapp.jangerhard.proximitydemo;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

/**
 * Created by JanGerhard on 12.10.2015.
 */
public class MyApplication extends Application {

    public Activity currentActivity = null;
    private BeaconManager beaconManager;
    private String beaconID_Jani = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private int jani_major = 16246, jani_minor = 59757;
    private Region region_jani;
    private TextView tvBeacon;

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());

        initialize();

        region_jani = new Region(
                "Jan's",
                UUID.fromString(beaconID_Jani),
                jani_major,
                jani_minor);

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener(){


            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                //tvBeacon = (TextView) getCurrentActivity().findViewById(R.id.tvBeacon);
                //tvBeacon.setText("Yes!");
                showNotification("Welcome!", "Entered region of " + region.getIdentifier() + " beacon!");
            }
            @Override
            public void onExitedRegion(Region region) {
                //tvBeacon = (TextView) getCurrentActivity().findViewById(R.id.tvBeacon);
                //tvBeacon.setText("Exited..!");
                showNotification("Where you going?","Exited the region of " + region.getIdentifier() + " beacon!");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region_jani);
            }
        });

    }

    private void initialize() {



    }

    public Activity getCurrentActivity(){
        return currentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.currentActivity = mCurrentActivity;
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}