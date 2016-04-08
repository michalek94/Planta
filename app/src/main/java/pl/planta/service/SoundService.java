package pl.planta.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import pl.planta.R;

public class SoundService extends Service implements MediaPlayer.OnErrorListener {

    private static final String TAG = SoundService.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private IBinder iBinder = new ServiceBinder();
    private int length = 0;

    public class ServiceBinder extends Binder{
        public SoundService getService() {
            return SoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.jingle);
        mediaPlayer.setOnErrorListener(this);

        if(mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(100, 100);
        }

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mediaPlayer, what, extra);
                    return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        /*if (mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(this, R.raw.jingle);
            mediaPlayer.setOnErrorListener(this);

            if(mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(100, 100);
            }

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    onError(mediaPlayer, what, extra);
                    return true;
                }
            });
        }*/
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(mediaPlayer != null){
            try{
                mediaPlayer.stop();
                mediaPlayer.release();
            }finally {
                mediaPlayer = null;
            }
        }
    }
    /**
     * Pause music and get position where music was paused
     */
    public void pauseMusic(){
        Log.d(TAG, "PauseMusic");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            length = mediaPlayer.getCurrentPosition();
        }
    }
    /**
     * Resume music from point where music was paused
     */
    public void resumeMusic(){
        Log.d(TAG, "ResumeMusic");
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }
    }
    /**
     * Stop music definitely
     */
    public void stopMusic(){
        Log.d(TAG, "StopMusic");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean onError(MediaPlayer mediaPlayer, int what, int extra){
        Log.d(TAG, "Music player failed");

        if(mediaPlayer != null){
            try{
                mediaPlayer.stop();
                mediaPlayer.release();
            }finally {
                mediaPlayer = null;
            }
        }
        return false;
    }
}
