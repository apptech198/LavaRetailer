package com.apptech.myapplication.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.MessageShowActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";


    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


//        Shownotification("title", "msg");

        Log.e(TAG, "onMessageReceived: " + remoteMessage.toString());

        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "onMessageReceived: " + remoteMessage.toString());
            Log.e(TAG, "onMessageReceived: " + remoteMessage.getData().get("data"));

            String data = remoteMessage.getData().get("data");

            try {
                JSONObject jsonObject = new JSONObject(data);
                String image = jsonObject.getString("image");
                String title = jsonObject.getString("title");
                String message = jsonObject.getString("message");
                if (image.isEmpty()) {
                    Shownotification(title, message);
                    return;
                }
//                new LongTask(getApplicationContext(), title, message, image).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class LongTask extends AsyncTask<String, Void, String> {

        String title, msg, imgUrl;
        Context context;
        Bitmap bitmap;

        private LongTask(Context context, String title, String msg, String img) {
            this.context = context;
            this.title = title;
            this.msg = msg;
            this.imgUrl = img;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Bitmap bitmaps = getBitmapfromUrl(imgUrl);
            bitmap = bitmaps;
        }

        @Override
        protected String doInBackground(String... strings) {
            Bitmap bitmapfromUrl = getBitmapfromUrl(imgUrl);
            this.bitmap = bitmapfromUrl;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ShownotificationWithImage(title, msg, bitmap);
        }


    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


    private void ShownotificationWithImage(String titile, String massage, Bitmap img) {


//        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring);
//        try {
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(this, MessageShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("global", "global", importance);
            channel.enableLights(true);
            channel.setDescription(massage);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);


            // channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "global")
                .setContentTitle(titile)
                .setContentText(massage)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(ContextCompat.getColor(this, R.color.black))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(img))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(false);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        // notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring);
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;

        notificationManager.notify(10, notification);
    }

    private void Shownotification(String titile, String massage) {


//        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring);
//        try {
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(this, MessageShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("global", "global", importance);
            channel.enableLights(true);
            channel.setDescription(massage);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);


            // channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "global")
                .setContentTitle(titile)
                .setContentText(massage)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(ContextCompat.getColor(this, R.color.black))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(false);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        // notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring);
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;

        notificationManager.notify(10, notification);
    }


}
















































