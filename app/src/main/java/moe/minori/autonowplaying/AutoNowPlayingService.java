package moe.minori.autonowplaying;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by minori on 15. 8. 19.
 */
public class AutoNowPlayingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("AutoNowPlayingService", "Started Service");

        NowPlayingModule.register(getApplicationContext());

        // register listener

        NowPlayingModule.setListener(new TrackChangedListener() {
            @Override
            public void trackChanged(String artist, String album, String track) {

                if (artist != null && album != null && track != null) {

                    if (artist.length() + album.length() + track.length() + 30 > 140) {
                        // truncate album
                        album = "...";
                        if (artist.length() + album.length() + track.length() + 30 > 140) {
                            // truncate artist
                            artist = "";
                            if (artist.length() + album.length() + track.length() + 30 > 140) {
                                // too long.. :(
                                track = track.substring(0, 90) + "...";
                            }
                        }
                    }


                    String tempText = artist + " - " + album + " - " + track + " #AutoNowPlaying";

                    sendBroadcast(new Intent(Consts.REQ_WRITE_TWEET)
                            .putExtra("DATA", tempText), Consts.EXTERNAL_BROADCAST_API);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("AutoNowPlayingService", "Stopped Service");

        NowPlayingModule.unRegister(getApplicationContext());
        NowPlayingModule.setListener(null);
    }
}
