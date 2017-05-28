package se.newton.scrummerz;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by Giovanni on 2017-05-28.
 * © Giovanni Palusa 2017
 */

public class Notification_reciever extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent startActivity = new Intent(context, signedInStart.class);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        // day starts on sunday (1) and goes to 7 for saturday
        if (day >1 && day <7) {
            startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = (PendingIntent.getActivity(context, 100, startActivity, PendingIntent.FLAG_UPDATE_CURRENT));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.cool)
                    .setContentTitle(context.getString(R.string.dontForget))
                    .setContentText(context.getString(R.string.giveDailyFeedback))
                    .setVibrate(new long[]{1000, 1000})
                    .setAutoCancel(true);

            notificationManager.notify(100, builder.build());
        }
    }
}
