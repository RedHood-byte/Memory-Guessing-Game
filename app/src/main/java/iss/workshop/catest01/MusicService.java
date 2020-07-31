package iss.workshop.catest01;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    MediaPlayer player1;
    MediaPlayer player2;
    MediaPlayer end;
    MediaPlayer correct;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {
        createForegroundService();
        player1 =MediaPlayer.create(this, R.raw.main_music); //select music file
        player1.setLooping(true); //set looping

        player2 = MediaPlayer.create(this, R.raw.gamemusic); //select music file
        player2.setLooping(true); //set looping

        end= MediaPlayer.create(this, R.raw.victory); //select music file
        end.setLooping(false); //set looping

        correct= MediaPlayer.create(this, R.raw.correct); //select music file
        correct.setLooping(false); //set looping
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getStringExtra("class").equals("main")){
            player1.start();
            return Service.START_NOT_STICKY;

        }
        else if (intent.getStringExtra("class").equals("game")) {
            player2.start();
            return Service.START_NOT_STICKY;
        }
        else if (intent.getStringExtra("class").equals("victory")) {
            end.start();
            return Service.START_NOT_STICKY;
        }

        else if (intent.getStringExtra("class").equals("correct")) {
            correct.start();
            return Service.START_NOT_STICKY;
        }

        return Service.START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createForegroundService() {
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel channel = new NotificationChannel("1", "Background Music",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification not = new NotificationCompat.Builder(this, "1").
                setContentTitle(getText(R.string.app_name)).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentIntent(pendingIntent).build();

        startForeground(1, not);
    }

    public void onDestroy() {

        player1.stop();
        player1.release();

        player2.stop();
        player2.release();

        end.stop();
        end.release();

        correct.stop();
        correct.release();

        stopSelf();
        super.onDestroy();
    }
}
