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

    public static int totalInstance=0;
    private int countInstance=0;

    //private long INTERVAL=864; //test
    //private long INTERVAL=600000; //test 10 miniutes
    //private long INTERVAL=3600000; //test 60 miniutes

    private long INTERVAL=Utility.INTERVAL; //ten days in milliseconds; using
    private int LOGINTERVAL=Utility.LOGINTERVAL; //interval time for periodically writing logs.


    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private boolean useWakeLock=true;

    public FibonacciIntentService() {
        super("FibonacciIntentService");
    }

    /**
     * control weaklock for FibonacciIntentService
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
//        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
//        wakeLock.acquire();



//        wakeLock.release();


            this.totalInstance++;
            this.countInstance=totalInstance;


            String log="*ruiqin:computeFibonacci("+countInstance+"): started."+Utility.currentDateTime();
            System.out.println(log);
            appendLog(log);


            useWakeLock=intent.getBooleanExtra("useWakeLock",true);

            if(useWakeLock){
                pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag Fibonacci WakeLock");
                wl.acquire();
                computeFibonacci();
                wl.release();
            }else{
                computeFibonacci();
            }
        }
    }

    /**
     * control time and appendLog() for Fibonacci().
     */
    private void computeFibonacci(){
        long start = System.currentTimeMillis();
        long intervalStart = start;
        int i = 60;
        int countRounds = 0;
        boolean reachend = false;

        while(!reachend){
            long fib = fibonacci(i);
            //i+=5;
            countRounds++;
            long current = System.currentTimeMillis();
            long currentIntervalStart=current - intervalStart;
            if(currentIntervalStart>LOGINTERVAL ){
                String log = "ruiqin: fibonacci(" + countInstance+ ")." + useWakeLock
                        + "compute fibonacci(" + i + ")=" +fib
                        + "for \t" + countRounds+ "\t rounds in" + currentIntervalStart+"\tms; "
                        + Utility.currentDateTime()+"\n";

                System.out.println(log);
                appendLog(log);

                if((current - start) > INTERVAL){
                    reachend = true;
                    log="ruiqin: fibonacci("+countInstance+") ends: totally run for " + (current-start) +" milliseconds. "
                            + Utility.currentDateTime()+"\n";
                    System.out.println(log);
                    appendLog(log);
                }

                intervalStart = current;
                countRounds = 0;
            }

        }


    }

    public int fibonacci(int a){

        if (a==0) return 0;
        if (a==1) return 1;
        return fibonacci(a - 1)+fibonacci(a - 2);

    }

    private void appendLog(String text){
        String filename="logFibonacci("+countInstance+")."+useWakeLock+".txt";
        Utility.appendLog(text,filename);
    }
}
