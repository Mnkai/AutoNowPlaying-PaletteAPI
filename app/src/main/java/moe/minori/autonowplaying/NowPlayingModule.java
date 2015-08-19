package moe.minori.autonowplaying;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by Minori on 2015-02-13.
 */
public class NowPlayingModule
{

    private static String artist = null;
    private static String album = null;
    private static String track = null;

    private static boolean isRegistered = false;

    private static BroadcastReceiver receiver;

    private static Context context;

    private static TrackChangedListener listener;

    public static void register(Context c)
    {
        if (isRegistered)
        {
            return;
        }

        context = c;
        receiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context arg0, Intent intent)
            {


                String action = intent.getAction();
                String cmd = intent.getStringExtra("command");
                //Log.d("NowPlayingModule", action + " / " + cmd);

                // try for sony devices
                artist = getFromIntent(intent, "ARTIST_NAME");
                album = getFromIntent(intent, "ALBUM_NAME");
                track = getFromIntent(intent, "TRACK_NAME");

                if ( artist == null && album == null && track == null )
                {
                    artist = getFromIntent(intent, "artist");
                    album = getFromIntent(intent, "album");
                    track = getFromIntent(intent, "track");

                }

                //Log.d("NowPlayingModule", artist + ":" + album + ":" + track);

                if ( listener != null )
                {
                    listener.trackChanged(artist, album, track);
                }

            }
        };

        IntentFilter iF = new IntentFilter();

        // stock android
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");

        // various
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.lge.music.metachanged");
        iF.addAction("com.pantech.app.music.metachanged");
        iF.addAction("com.neowiz.android.bugs.metachanged");
        iF.addAction("com.soundcloud.android.metachanged");
        iF.addAction("com.soundcloud.android.playback.playcurrent");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.spotify.music.metadatachanged");

        // sony walkman
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY");
        iF.addAction("com.sonyericsson.music.TRACK_COMPLETED");
        iF.addAction("com.sonyericsson.music.playbackcomplete");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");

        // xiaomi
        iF.addAction("soundbar.music.metachanged");


        c.registerReceiver(receiver, iF);
        isRegistered = true;
    }


    public static void setListener (TrackChangedListener li)
    {
        listener = li;
    }

    public static String[] getTrackInfo()
    {
        String[] toReturn = new String[3];

        if (artist == null && album == null && track == null)
        {
            // track is not changed yet, or failed to fetch info

            // trying to fetch media player notification from system notification listener
            return null;

        }
        toReturn[0] = artist;
        toReturn[1] = album;
        toReturn[2] = track;

        return toReturn;
    }

    public static void unRegister(Context c)
    {
        if (isRegistered)
        {
            c.unregisterReceiver(receiver);
            isRegistered = false;
        }

    }

    public static String getFromIntent(Intent intent, String name)
    {
        if (intent.getStringExtra(name) != null)
        {
            return intent.getStringExtra(name);
        }
        else
        {
            return null;
        }
    }
}
