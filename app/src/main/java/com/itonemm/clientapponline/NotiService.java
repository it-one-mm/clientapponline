package com.itonemm.clientapponline;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import me.leolin.shortcutbadger.ShortcutBadger;

public class NotiService extends FirebaseMessagingService {
    public NotiService() {
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            new SendNotification().execute(title,body,image);
        }
    };
    public static int NotificationId=1;
    String title;
    String body;
    String image;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            title=remoteMessage.getNotification().getTitle();
            body=remoteMessage.getNotification().getBody();
            image=remoteMessage.getNotification().getImageUrl().toString();


        } else{
            title=remoteMessage.getData().get("title");
            body=remoteMessage.getData().get("body");
            image=remoteMessage.getData().get("image");


        }
        handler.postDelayed(runnable,0);

    }

    private class  SendNotification extends AsyncTask<String,Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {

            String titile=strings[0];
            String body=strings[1];
            String imagelink=strings[2];

            try {
                URL url=new URL(imagelink);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();;

                Bitmap image= BitmapFactory.decodeStream(connection.getInputStream());
                connection.disconnect();
                return  image;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            NotificationManager ntfmanager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            String channelid="Movie Client App";
            String channelname="Movie Client App";
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent intent1=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
            int importance=NotificationManager.IMPORTANCE_HIGH;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel channel=new NotificationChannel(channelid,channelname,importance);
                ntfmanager.createNotificationChannel(channel);
                NotificationCompat.Builder noti=new NotificationCompat.Builder(getApplicationContext(),channelid);
                noti.setContentTitle(title)
                        .setContentText(body)
                        .setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setSmallIcon(R.drawable.ic_noti)
                        .setColor(Color.BLUE)
                        .setContentIntent(intent1);


                ntfmanager.notify(NotificationId,noti.build());

                NotificationId++;

            }
            else
            {
                NotificationCompat.Builder noti=new NotificationCompat.Builder(getApplicationContext());
                noti.setContentTitle(title)
                        .setContentText(body)
                        .setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setSmallIcon(R.drawable.ic_noti)
                        .setColor(Color.BLUE)
                        .setContentIntent(intent1);
                ntfmanager.notify(NotificationId,noti.build());
                NotificationId++;
            }
            ShortcutBadger.applyCount(getApplicationContext(),NotificationId); //for 1.1.4+
          //for 1.1.3
        }
    }
}
