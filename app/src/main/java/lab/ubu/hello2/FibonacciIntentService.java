package lab.ubu.hello2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.*;
import android.os.PowerManager.*;
/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FibonacciIntentService extends IntentService {


    public FibonacciIntentService() {
        super("FibonacciIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
//        wakeLock.acquire();

        if (intent != null) {
            int i=40;
            while (i<50000) {
                int fib = fibnacci(i);
                //i+=5;
                System.out.println("lelema: fib("+i+")="+fib);
            }
        }
//        wakeLock.release();
    }
    public int fibnacci(int a){

        if (a==0) return 0;
        if (a==1) return 1;
        return fibnacci(a-1)+fibnacci(a-2);
    }

}
