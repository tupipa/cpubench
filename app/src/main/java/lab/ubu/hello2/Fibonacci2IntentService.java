package lab.ubu.hello2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Fibonacci2IntentService extends IntentService {
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

    public Fibonacci2IntentService() {
        super("Fibonacci2IntentService");
    }

    /**
     * control weaklock for Fibonacci2IntentService
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            this.totalInstance++;
            this.countInstance=totalInstance;


            String log="*ruiqin:computeFibonacci2("+countInstance+"): started."+Utility.currentDateTime()+"\n";
            System.out.println(log);
            appendLog(log);


            useWakeLock=intent.getBooleanExtra("useWakeLock",true);

            if(useWakeLock){
                pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag Fibonacci2 WakeLock");
                wl.acquire();
                computeFibonacci2();
                wl.release();
            }else{
                computeFibonacci2();
            }
        }
    }

    /**
     * control time and appendLog() for fibonacci().
     */
    private void computeFibonacci2(){

        long start, current, intervalStart;

        start= SystemClock.elapsedRealtime();
        intervalStart=start;
        double runTime;
        long i, num = 1, primes = 0;
        long UpBound=Long.MAX_VALUE;
        boolean reachend=false;

        int testfibonacci2=10000;
        int countRounds=0;

        while (!reachend) {

            long fib2result = fibonacci2(testfibonacci2);

//            countRounds++;
//
//            current=SystemClock.elapsedRealtime();
//
//            if(current-intervalStart>LOGINTERVAL){
//
//                String log="ruiqin: fibonacci2("+countInstance+")."+useWakeLock
//                        +" compute fibonacci2("+testfibonacci2+")=" + fib2result
//                        + " for\t"+countRounds+"\trounds in\t" +LOGINTERVAL+"\tms; "
//                        + Utility.currentDateTime()+"\n";
//
//                System.out.println(log);
//                appendLog(log);
//
//                if(current-start>INTERVAL){
//                    reachend=true;
//                    log="ruiqin: fibonacci2("+countInstance+") ends: totally run for " + (current-start) +" milliseconds. "
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

    public int fibonacci2(int a){
        int fib1=0;
        int fib2=1;
        int temp = fib1;
        for (int i =1; i<= a;i++){
            temp = fib1 + fib2;
            fib1 = fib2;
            fib2 = temp;
        }
//        if (a==0) return 0;
//        if (a==1) return 1;
//        return fibnacci(a-1)+fibnacci(a-2);
        return fib2;
    }

    private void appendLog(String text){
        String filename="logFibonacci2("+countInstance+")."+useWakeLock+".txt";
        Utility.appendLog(text,filename);
    }
}
