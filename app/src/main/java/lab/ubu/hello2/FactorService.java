package lab.ubu.hello2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

/**
 * compute factor recursively
 */
public class FactorService extends IntentService {

    public static int totalInstance=0;
    private int countInstance=0;

    //private long INTERVAL=864; //test
    //private long INTERVAL=600000; //test 10 miniutes
    //private long INTERVAL=3600000; //test 60 miniutes

    private long INTERVAL=Utility.INTERVAL; //ten days in milliseconds; using
    private int LOGINTERVAL=Utility.LOGINTERVAL; //interval time for periodically writing logs.


    private  PowerManager pm;
    private PowerManager.WakeLock wl;
    private boolean useWakeLock=true;

    public FactorService() {
        super("FactorService");
    }

    /**
     * control weaklock for FactorService
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            this.totalInstance++;
            this.countInstance=totalInstance;


            String log="*lelema:computeFactor("+countInstance+"): started."+Utility.currentDateTime()+"\n";
            System.out.println(log);
            appendLog(log);


            useWakeLock=intent.getBooleanExtra("useWakeLock",true);

            if(useWakeLock){
                pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag Factor WakeLock");
                wl.acquire();
                computeFactor();
                wl.release();
            }else{
                computeFactor();
            }
        }
    }

    /**
     * control time and appendLog() for Factor().
     */
    private void computeFactor(){

        long start, current, intervalStart;

        start= SystemClock.elapsedRealtime();
        intervalStart=start;
        double runTime;
        long i, num = 1, primes = 0;
        long UpBound=Long.MAX_VALUE;
        boolean reachend=false;

        int testfactor=10000;
        int countRounds=0;

        while (!reachend) {

            long factors = factor(testfactor);

            countRounds++;

//            current=SystemClock.elapsedRealtime();
//
//            if(current-intervalStart>LOGINTERVAL){
//
//                String log="lelema: factor("+countInstance+")."+useWakeLock
//                        +" compute factor("+testfactor+")=" + factors
//                        + " for\t"+countRounds+"\trounds in\t" +LOGINTERVAL+"\tms; "
//                        + Utility.currentDateTime()+"\n";
//
//                System.out.println(log);
//                appendLog(log);
//
//                if(current-start>INTERVAL){
//                    reachend=true;
//                    log="lelema: factor("+countInstance+") ends: totally run for " + (current-start) +" milliseconds. "
//                            + Utility.currentDateTime()+"\n";
//                    System.out.println(log);
//                    appendLog(log);
//                }
//
//                //start another LOGINTERVAL to do logging after this interval.
//                intervalStart=current;
//                countRounds=0;
//
//            }
        }
    }

    public long factor(int n){
        if (n==0) return 1;
        if (n==1) return 1;
        return n*factor(n-1);
    }
    private void appendLog(String text){
        String filename="logFactor("+countInstance+")."+useWakeLock+".txt";
        Utility.appendLog(text,filename);
    }

}
