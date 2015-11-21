package lab.ubu.hello2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MXplayerIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "lab.ubu.hello2.action.FOO";
    private static final String ACTION_BAZ = "lab.ubu.hello2.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "lab.ubu.hello2.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "lab.ubu.hello2.extra.PARAM2";

    private String vedioname = "perfectworld.mkv";

    public static final String TAG					= "mxvp.intent.test";

    public static final String MXVP					= "com.mxtech.videoplayer.ad";
    public static final String MXVP_PRO				= "com.mxtech.videoplayer.pro";

    public static final String MXVP_PLAYBACK_CLASS		= "com.mxtech.videoplayer.ad.ActivityScreen";
    public static final String MXVP_PRO_PLAYBACK_CLASS	= "com.mxtech.videoplayer.ActivityScreen";

    public static final String RESULT_VIEW			= "com.mxtech.intent.result.VIEW";
    public static final String EXTRA_DECODE_MODE	= "decode_mode";	// (byte)
    public static final String EXTRA_VIDEO_LIST		= "video_list";
    public static final String EXTRA_SUBTITLES		= "subs";
    public static final String EXTRA_SUBTITLES_ENABLE = "subs.enable";
    public static final String EXTRA_TITLE			= "title";
    public static final String EXTRA_POSITION		= "position";
    public static final String EXTRA_RETURN_RESULT	= "return_result";
    public static final String EXTRA_HEADERS		= "headers";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MXplayerIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MXplayerIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public MXplayerIntentService() {
        super("MXplayerIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }

//            Uri videoUri = Uri.parse( "rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp" );

            File sdcard = Environment.getExternalStorageDirectory();
//            File logFile = new File(sdcard,vedioname);
            Uri videoUri = Uri.parse( "file://"+sdcard+vedioname );
            System.out.println(videoUri.toString()+"*******************vedio");

//            Uri videoUri = Uri.parse( "file:///storage/emulated/0/DCIM/s1.mkv" );
//        Uri subtitleUri1 = Uri.parse("file:///mnt/sdcard/747.ass");
//        Uri subtitleUri2 = Uri.parse("file:///mnt/sdcard/a.smi");

            Intent i = new Intent(Intent.ACTION_VIEW);

//        i.setData( videoUri );
            i.setDataAndType( videoUri, "application/*" );

            // s/w decoder
            i.putExtra( EXTRA_DECODE_MODE, (byte)2 );

            // play only given videos.
//        i.putExtra( EXTRA_VIDEO_LIST, new Parcelable[] { videoUri, Uri.parse( "file:///mnt/sdcard/Movies/747a.3gp") } );

            // with a subtitle.
//        i.putExtra( EXTRA_SUBTITLES, new Parcelable[] { subtitleUri1, subtitleUri2 }  );

            // explictly select first subtitle.
//        i.putExtra( EXTRA_SUBTITLES_ENABLE,  new Parcelable[] { subtitleUri1 } );

            // provides title text.
//        i.putExtra( EXTRA_TITLE, "Elephants Dream" );

            // starting from 10s.
//        i.putExtra( EXTRA_POSITION, 10000 );

            // request result
            i.putExtra( EXTRA_RETURN_RESULT, true );

            String[] headers = new String[] { "User-Agent", "MX Player Caller App/1.0", "Extra-Header", "911" };
            i.putExtra( EXTRA_HEADERS, headers );


//        try
//        {
//        	i.setPackage( MXVP_PRO );
//        	startActivityForResult( i, 0 );
//        	return;
//        }
//        catch( ActivityNotFoundException e )
//        {
//        }

            try
            {
                i.setPackage(MXVP);
                i.setClassName(MXVP, MXVP_PLAYBACK_CLASS);
//                startActivityForResult( i, 0 );
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.mxtech.videoplayer.ad");
                System.out.println(videoUri.toString()+"*******************vedio2");
                startActivity(launchIntent);

                return;
            }
            catch( ActivityNotFoundException e2 )
            {
            }

            Log.e( TAG, "Can't find MX Video Player." );
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
