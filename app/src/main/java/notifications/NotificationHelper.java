package notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;

import static Utilities.Constants.GROUP_KEY_NOTIFICATIONS;

public class NotificationHelper extends ContextWrapper {

    private static final String TAG = "NotificationHelper";

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    private String CHANNEL_NAME = "WGU Surfer Notification";
    private String CHANNEL_ID = "dev.alvatech.c196_wgusurfer_ericbsmith" + CHANNEL_NAME;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("WGU Surfer notification channel.");
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager) getSystemService
                (Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }

    public void sendHighPriorityNotification(String title, String text, Class activityName) {
        Intent intent = new Intent(this, activityName);
        int random = (int)System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, random, intent, 0);

        Notification notification = new NotificationCompat.Builder(this,
                CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManagerCompat.from(this).notify(random, notification);

    }

}
