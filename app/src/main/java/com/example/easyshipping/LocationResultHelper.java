package com.example.easyshipping;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.easyshipping.Model.UserLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LocationResultHelper {

    private static String encEmail;
    private String email = ConfigLocationActivity.userEmail;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static final String KEY_LOCATION_RESULTS = "key-location-results";
    private Context mContext;
    private List<Location> mLocationList;

    public LocationResultHelper(Context mContext, List<Location> mLocationList) {
        this.mContext = mContext;
        this.mLocationList = mLocationList;
    }

    public String getLocationResultText() {

        if (mLocationList.isEmpty()) {
            return "Location not received";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Location location : mLocationList) {
                //sb.append("(");
                sb.append(location.getLatitude());
                sb.append(", ");
                sb.append(location.getLongitude());
                //sb.append(")");
                //sb.append("\n");

                encodeUserEmail(email);
                String LatLng = sb.toString();
                UserLocation userLoc = new UserLocation(
                        encEmail,
                        LatLng
                );

                FirebaseDatabase.getInstance().getReference("UserLocs").child(encEmail)
                        .setValue(userLoc).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //.setValue(location.getLatitude() + "," + location.getLongitude()) create an object to store email and LatLng, get email passed through the intents
                    //FirebaseAuth.getInstance().getCurrentUser().getUid()
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            System.out.println("loc got");
                        }
                        else {
                            System.out.println("loc not got");                        }
                    }
                });
                break;
            }
            return sb.toString();

        }

    }

    private CharSequence getLocationResultTitle() {

        String result = mContext.getResources().getQuantityString
                (R.plurals.num_locations_reported, mLocationList.size(), mLocationList.size());

        return result + " : " + DateFormat.getDateTimeInstance().format(new Date());
    }

    public void showNotification() {

        Intent notificationIntent = new Intent(mContext, ConfigLocationActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(NotMainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(mContext,
                App.CHANNEL_ID)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        getNotificationManager().notify(0, notificationBuilder.build());

    }

    private NotificationManager getNotificationManager() {

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;

    }

    public void saveLocationResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_RESULTS, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply();
    }

    public static String getSavedLocationResults(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_RESULTS, "default value");

    }

    public static String encodeUserEmail(String userEmail) {
        encEmail = userEmail.replace(".", ",");
        return userEmail.replace(".", ",");
    }

}