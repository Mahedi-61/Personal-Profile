package au.org.ipdc.model;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateFormat;

import java.util.Calendar;

import au.org.ipdc.personalprofile.LoginActivity;
import au.org.ipdc.personalprofile.R;

public class AlarmReceiver extends BroadcastReceiver {

    private PendingIntent alarmIntent;
    private AlarmManager alarmMgr;
    private String dayId = "";
    DatabaseHelper helper;
    private Calendar month;
    private SharedPreferences profile;

    private void buildNotification(Context context) {
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent resultIntent = new Intent(context, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        alarmIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT );
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Personal Development Profile")
                .setContentText("You may forget to keep your profile !!")
                .setTicker("keep your profile")
                .setContentIntent(alarmIntent)
                .setSound(alarmSound)
                .setAutoCancel(true);

        notificationmanager.notify(0, builder.build());
    }

    public void cancelAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
        alarmMgr.cancel(alarmIntent);
        ComponentName componentName = new ComponentName(context, SampleBootReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(componentName, 2, 1);
    }

    public void onReceive(Context context, Intent intent) {
        //if report is not kept any day show notification
        month = Calendar.getInstance();
        helper = new DatabaseHelper(context);
        dayId = DateFormat.format("EE, dd MMMM yyyy", month).toString();
        if (helper.getWhetherProfileKeepsOrNotFromDayId(dayId) == 0) {
            buildNotification(context);
        }
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
        profile = context.getSharedPreferences("profile", 0);

         Calendar time= Calendar.getInstance();
         time.setTimeInMillis(System.currentTimeMillis());
         time.set(Calendar.HOUR_OF_DAY, profile.getInt("hour", 0));
         time.set(Calendar.MINUTE, profile.getInt("minute", 0));
         alarmMgr.setInexactRepeating(0, time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        ComponentName component = new ComponentName(context, SampleBootReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(component, 1, 1);
    }
}
