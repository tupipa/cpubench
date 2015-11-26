package lab.ubu.hello2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * compute and find as many prime numbers as possible
 * .
 */
public class PrimeIntentService extends IntentService {

    public static int totalInstance=0;
    private int countInstance;
    //private long INTERVAL=864; //test
    //private long INTERVAL=600000; //test 10 miniutes
    //private long INTERVAL=3600000; //test 60 miniutes
    private long INTERVAL=Utility.INTERVAL; //using interval in Utility

    PowerManager pm;
    PowerManager.WakeLock wl;
    boolean useWakeLock=false;

    public PrimeIntentService() {
        super("PrimeIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            this.totalInstance++;
            this.countInstance=totalInstance;
            System.out.println("*lelema: computePrime("+countInstance+"): started");

            useWakeLock=intent.getBooleanExtra("useWakeLock",true);

            if(useWakeLock){
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag Prime WakeLock");
            wl.acquire();
            computePrime();
            wl.release();
            }else{
                computePrime();
            }

        }
    }
    public void computePrime(){
        long start, current, intervalStart;
        start=SystemClock.elapsedRealtime();
        intervalStart=start;
        double runTime;
        long i, num = 1, primes = 0;
        long UpBound=Long.MAX_VALUE;
        boolean reachend=false;
//        System.out.println("*lelema: computePrime() started*****************************");
        while (!reachend) {
            if(num > UpBound) {
                num=1;
                primes=0;
            }
            i = 2;
            while (i <= num) {
                if(num % i == 0)
                    break;
                i++;
            }

            if (i == num) {
                primes++;
                if(primes%1000==0) {
                    current=SystemClock.elapsedRealtime();
                    String log="*lelema: prime("+countInstance+"): wakelock="+useWakeLock+": "
                            + primes+" prime numbers calculated using additional\t"
                            +(current-intervalStart)+"\tmilliseconds; "
                            +Utility.currentDateTime()+"\n";
                    System.out.println(log);
                    appendLog(log);
                    if(current-start>INTERVAL){
                        reachend=true;
                        log="ruiqin:prime("+countInstance+") ends: prime has run more than " +INTERVAL+" milliseconds. "
                                + Utility.currentDateTime()+"\n";
                        System.out.println(log);
                        appendLog(log);
                    }
                    intervalStart=current;
                }
            }
            num++;
        }

    }

    public void appendLog(String text)

    {
      String logname="logPrime("+countInstance+")."+useWakeLock+".txt";

        Utility.appendLog(text,logname);

    }


}
