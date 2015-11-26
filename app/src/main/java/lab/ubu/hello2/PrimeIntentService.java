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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PrimeIntentService extends IntentService {

    public static int totalInstance=0;
    private int countInstance;
    //private long INTERVAL=864; //test
    //private long INTERVAL=600000; //test 10 miniutes
    private long INTERVAL=3600000; //test 60 miniutes
    //private long INTERVAL=864000000; //ten days in milliseconds
    PowerManager pm;
    PowerManager.WakeLock wl;
    boolean useWakeLock=false;

    public PrimeIntentService() {
        super("PrimeIntentService");
    }
//    public PrimeIntentService(boolean useWakeLock) {
//        super("PrimeIntentService");
//        this.useWakeLock=useWakeLock;
//        countInstance++;
//        System.out.println("*lelema:("+countInstance+"): computePrime() started");
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            this.totalInstance++;
            this.countInstance=totalInstance;
            System.out.println("*lelema:("+countInstance+"): computePrime() started");

            useWakeLock=intent.getBooleanExtra("useWakeLock",false);

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
        long start, current;
        start=SystemClock.elapsedRealtime();
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
                    String log="*lelema: prime("+countInstance+"): wakelock="+useWakeLock+": "+ primes+" prime numbers calculated using "+(current-start)+"\tmilliseconds\n";
                    System.out.println(log);
                    appendLog(log);
                    if(current-start>INTERVAL){
                        reachend=true;
                        log="ruiqin:prime("+countInstance+") ends: prime has run for " +INTERVAL+" milliseconds. Now going to ends\n";
                        System.out.println(log);
                        appendLog(log);
                    }
                }
            }
            num++;
        }

    }
    public void appendLog(String text)

    {
      String logname="logPrime"+useWakeLock+".txt";
        File sdcard = Environment.getExternalStorageDirectory();
        File logFile = new File(sdcard,logname);

        if (!logFile.exists())
        {
            System.out.println("*lelema(main):cannot read "+sdcard+"/"+logname+" **************************");
            //System.exit(0);
            try
            {
                logFile.createNewFile();

                System.out.println("*lelema(main): create "+sdcard+"/"+logname+"succeed**************************");
                //System.exit(0);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        else{
//
////            System.out.println("*lelema(main): read " + sdcard + "/" + logname + " succeed **************************");
//            //System.exit(0);
//
//        }

        OutputStream outputStream=null;
        OutputStreamWriter out =null;

        try
        {


            System.out.println("*lelema(main): start to streaming for "+sdcard+"/"+logname+"**************************");
            outputStream = new FileOutputStream(sdcard+"/"+logname,true);
            //outputStream = openFileOutput(sdcard+"/"+logname,Context.MODE_APPEND);


//            System.out.println("*lelema(main): done creating FileOutputStream "+sdcard+"/"+logname+" succeed **************************");

            out = new OutputStreamWriter(outputStream);

//            System.out.println("*lelema(main): done creating outstream"+sdcard+"/"+logname+" succeed **************************");

            out.write(text);

            System.out.println("*lelema(main): done writing " + sdcard + "/" + logname + " **************************");
            System.out.println("*lelema(main): done writing " + text + " **************************");

            //out.flush();
            out.close();

            //BufferedWriter for performance, true to set append to file flag
//            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//            System.out.println("write to the log in Nexus 4");
//            buf.append(text);
//            buf.newLine();
//            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("*lelema(main): failed to write "+sdcard+"/"+logname+" **************************");
            System.exit(0);

        }
//        finally {
//            if (out != null) {
//                out.close();
//            }
//        }

    }


}
