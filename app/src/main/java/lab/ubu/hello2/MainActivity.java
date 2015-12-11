package lab.ubu.hello2;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.os.Environment;

import java.io.*;
import java.util.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.regex.Pattern;

/************************************
 * version 6 TODO: add memory usage monitor
 *
 * version 6.01
 *
 *  1, along with the battery info, add memory usage written to logBattery.txt.
 *
 *  2, add method:
 *      getMemoryUsedMegs()---return used memory size in MB
 *      getMemoryUsedPercentage()---return mem usage in %
 *      getMemoryTotoal()---return total memory
 *
 *
 *  3, unfinished methods: read memory info using exec-'dumpsys meminfo' in Utility().
 *          problem: permission denied?
 *
 *
 * version 5.12
 *
 *  1. avoid periodically logging in Prime, Fib2, factor; Only log start point of them.
 *  2, log periodically for Fib(41) as well as the start point.
 *  3, In Prime, Fib, Fib2, Factor, each has only one instance of IntentService. Because multiple instances
 *      for the same IntentServices are executed sequentially rather than concurrently.
 *
 * version 5.11
 *
 *  1. add WAKE_LOCK to Fibonacci and Fibonacci2.
 *  2. avoid logging in Prime, Fib2. Only log in Fib(60), Factor
 *  3. add readLog to Utility.
 *  4, clean mainactivity.appendLog() useless comments.
 *  5, trigger Prime, Fib2, Fib(60), Factor; each with 2 IntentServices.
 *
 * version 5.10: add utility class with appenLog() and currentDataTime() method. Deploy wakelock in FactorService.
 *
 *  1.1, create utility class Utility(). It has static methods(vars) which all other classes could use them directly.
 *  This allows reusing methods across different classes, avoiding duplicated methods.
 *
 *  1.2, in Utility: add 'void appendLog(text, filename)' to write 'text' to file 'filename' on sdcard.
 *
 *  1.3, in Utility: add 'String currentDataTime()' to get the current date and time in a String.
 *
 *  1.4, in Utility: add INTERVAL to control total run time for each service;
 *                   add LOGINTERVAL to periodically logging for each service;
 *
 *  2.1, in FactorService: deploy wakelock.
 *
 *  2.2, in FactorService: use appendLog() and currentDataTime().
 *
 *  2.3, in FactorService: add INTERVAL to control total run time, add LOGINTERVAL to periodically logging.
 *
 *  2.4, in FactorService: clean unused IntentService interface in FactorService.
 *
 *  3.1, in MaiinActivity.logBatteryAndCPUFreq(): add currentDataTime to logBattery.txt.
 *
 *
 * version 5.02: add current time to log in prime service.
 *  -use Calendar.getInstance(); change prime Instance.
 *
 * version 5.01: test version: WAKE_LOCK for prime (and onDestroy for main activity)
 *  1, use WAKE_LOCK in prime services
 *
 *  2, add onDestroy() method to MainActivity and prime services. Release receiver in main and the lock in prime.
 *
 *  3, instead of while true loop, use 10 days/60 minutes timer to make the prime services running for a long time.
 *
 *  4, countInstance in prime, add appendLog() function for prime "logPrime.txt". For debug.
 *
 *  5, pass parameter to IntentService (prime, useWakeLock)
 *
 *  6, change code format
 *      -- clear unused IntentService interface in prime
 *
 * version4
 *
 * -read frequency via "/sys/devices/system/cpu/"
 *      --readCpuFreq()
 *
 * -change code layout: wrapped codes into methods:
 *      --"logBatteryAndCpuFreq() " to log battery status and cpu freq
 *      --"startCPUbench()" to reach 100% cpu load
 *      --clear log format to be portable to excel format.
 *
 *
 * version3
 *
 * mxplayer launch automatically
 *
 *
 * version2
 *
 * log information to sdcard. logBattery.txt
 * -appendLog(String loginfo);
 *
 *
 * version1
 *
 * provide fibonacci, fibonacci2, prime,factor function.
 *
 */

public class MainActivity extends AppCompatActivity { //
    private TextView batteryTxt;
    private TextView batteryinfo;
    private TextView timestamp;
    private int batteryPercent;
    private int batteryVoltage;
    private int batteryScale;
    private int batteryStatus;
    private int batteryTemp;

    private String batteryvalue = null, batteryString=null, timeString=null;

    private int TIME_INTERVAL=6000;
    private long time_old;



//    private Intent intentFib;
//    private Intent intentFib2;

    private Intent intentPri;
//    private Intent intentPri2;
//    private Intent intentPri3;
//    private Intent intentPri4;
//    private Intent intentPri5;

    private Intent fibnacci1;
//    private Intent fibnacci2;
//    private Intent fibnacci3;
//    private Intent fibnacci4;

    private Intent fibnacci21;
//    private Intent fibnacci22;
//    private Intent fibnacci23;
//    private Intent fibnacci24;

    private Intent factor1;
//    private Intent factor2;
//    private Intent factor3;
//    private Intent factor4;

    private Intent MXplayer;

    private String logname="logBattery.txt";
    Thread timerThread = null;
   // PowerManager.WakeLock wl;
    //PowerManager pm;


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryPercent=level;
            batteryVoltage=intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            batteryScale=intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            batteryStatus=intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            batteryTemp=intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            batteryvalue = String.valueOf(level) + "%";

            long currentTimeMills=System.currentTimeMillis();

           // time_old=SystemClock.elapsedRealtime();

            timeString=String.valueOf(System.currentTimeMillis());

//            batteryTxt.setText(batteryvalue);
//            System.out.println("System.currentTimeMillis()*" + System.currentTimeMillis());


            batteryString="\nBattery Left:\t" + batteryvalue +"\t Voltage:\t"+batteryVoltage
                    +"\t Temp:\t"+batteryTemp+"\tStatus:\t"+batteryStatus + "\ttime:\t" + timeString;


            // timestamp.setText(timeString);

            // batteryinfo.setText(batteryString);


//for(int i=0;i<10;i++){

            logBatteryAndCpuFreq();
//}

            System.out.println("mBatInfoReceiver:" + batteryString);
            // appendLog("BatInfoReceiver: " + batteryvalue + "-" + timestamp.getText());

            // readLog();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        batteryTxt = (TextView) this.findViewById(R.id.batteryTxt);
        //  batteryinfo =  (TextView) this.findViewById(R.id.batteryinfo);
        //  timestamp =  (TextView) this.findViewById(R.id.timestamp);


       // pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
       // wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
       // wl.acquire();

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //startMXPlayer();
        startCPUbench();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                System.out.println("*lelema:************************** mail button Clicked **************************");
//                Snackbar.make(view, "The Battery Left: "+batteryTxt.getText()+"; Voltage:"+batteryVoltage
//                        +"; Scale: "+batteryScale+"; Status: "+batteryStatus, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                System.out.println("*lelema:*The Battery left:" + batteryTxt.getText() + " ***********************");
//
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        long interval=System.currentTimeMillis()-time_old;
        String log="\n*ruiqin: MainActivity is now in onDestroy() after running for "
                + interval+"ms; Time is "+Utility.currentDateTime()+"\n";
        System.out.println(log);
        appendLog(log);
       // wl.release();
       // System.out.println("*ruiqin: onDestroy(): wake lock realsed **************************");
        this.unregisterReceiver(this.mBatInfoReceiver);
        log="*ruiqin: onDestroy(): the battery receiver unregistered at "+Utility.currentDateTime()+"\n";
        System.out.println(log);
        appendLog(log);
        super.onDestroy();
    }



    public void startCPUbench(){

        time_old=System.currentTimeMillis();
        String log="\nCPUbench started at " + time_old
                    +"\tDate&Time:\t"+Utility.currentDateTime()
                    +"\tTotal Memory(MB)\t"+getMemoryTotal()
                    +"\n";
        System.out.println(log);
        appendLog(log);


        intentPri= new Intent(this, PrimeIntentService.class);
//        intentPri2= new Intent(this, PrimeIntentService.class);
//        intentPri3= new Intent(this, PrimeIntentService.class);
//        intentPri4= new Intent(this, PrimeIntentService.class);
//        intentPri5= new Intent(this, PrimeIntentService.class);

//        System.out.println("*lelema:*start 1st prime **************************");
//        intentPri.putExtra("countInstance",1);
        intentPri.putExtra("useWakeLock", true);
        startService(intentPri);

//        System.out.println("*lelema:*start 2nd prime **************************");
//        intentPri2.putExtra("useWakeLock",true);
//        startService(intentPri2);
//        System.out.println("*lelema:*start 3rd prime **************************");
//        startService(intentPri3);
//        startService(intentPri4);
//        startService(intentPri5);


        factor1 = new Intent(this, FactorService.class);
        factor1.putExtra("useWakeLock", true);
        startService(factor1);

//        factor2 = new Intent(this, FactorService.class);
//        factor3 = new Intent(this, FactorService.class);
//        factor4 = new Intent(this, FactorService.class);


//        factor2.putExtra("useWakeLock", false);
//        startService(factor2);
//        startService(factor3);
//        startService(factor4);





        fibnacci1 = new Intent(this, FibonacciIntentService.class);
        fibnacci1.putExtra("useWakeLock", true);
        startService(fibnacci1);

//        fibnacci2 = new Intent(this, FibonacciIntentService.class);
//        fibnacci3 = new Intent(this, FibonacciIntentService.class);
//        fibnacci4 = new Intent(this, FibonacciIntentService.class);

        fibnacci21 = new Intent(this, Fibonacci2IntentService.class);
        fibnacci21.putExtra("useWakeLock", true);
        startService(fibnacci21);
//        fibnacci22 = new Intent(this, Fibonacci2IntentService.class);
//        fibnacci23 = new Intent(this, Fibonacci2IntentService.class);
//        fibnacci24 = new Intent(this, Fibonacci2IntentService.class);

//        fibnacci2.putExtra("useWakeLock", true);
//        startService(fibnacci2);
////        startService(fibnacci3);
////        startService(fibnacci4);

//        fibnacci22.putExtra("useWakeLock", true);
//        startService(fibnacci22);
////        startService(fibnacci23);
////        startService(fibnacci24);

    }

    public void logBatteryAndCpuFreq() {


//        if (! batteryTxt.getText().toString().equals(batteryvalue)){

        StringBuilder builder = new StringBuilder();

        String logStr;

        builder.append(batteryString);

        builder.append("\t");
        builder.append("CPU freqencies:\t");

        String[] freqs=readCpuFreq();

//            String freqStr=Arrays.toString(freqs);
        for(String s : freqs) {
//                s.trim();
            builder.append(s);
            builder.append("\t");
//                System.out.println("*ruiqin: per freqency cpu" + s + " ***********************");
        }

        builder.append(Utility.currentDateTime()+"\t");
        //builder.append("MemUsage:\t"+Utility.getUsedMemorySize()+"\t");
        builder.append("MemUsed(MB):\t"+getMemoryUsedMegs()+"\t");
        builder.append("MemUsagePercentage:\t"+getMemoryUsedPercentage()+"\t");
        //builder.append("\n"+Utility.cmdDumpsysMeminfo("lab.ubu.hello2"));
        //builder.append("\n"+Utility.cmdDumpsysProcstats("--hours 3"));

        logStr= builder.toString();

        batteryTxt.setText(logStr);

        System.out.println("*ruiqin: batinfo and all cpu frequencies:" + logStr);
        appendLog(logStr);

        // System.exit(0);

//        }


    }

    public void startMXPlayer(){

        MXplayer = new Intent(this, MXplayerIntentService.class);

        startService(MXplayer);

    }


    private String[] readCpuFreq(){

        String cpuMaxFreq = "";

        //http://android-er.blogspot.com/2015/03/read-cpu-frequency-using-linux-command.html

        Runtime runtime = Runtime.getRuntime();
        int availableProcessors = runtime.availableProcessors();

        File[] cpuFiles = getCPUs();
//        System.out.println("ruiqin: number of cpu: " + cpuFiles.length);
//        textNumOfCpu.setText("number of cpu: " + cpuFiles.length);

        String[] freqList = new String[cpuFiles.length];

        for(int i=0; i<cpuFiles.length; i++) {

            String path_scaling_cur_freq =
                    cpuFiles[i].getAbsolutePath() + "/cpufreq/scaling_cur_freq";

            String scaling_cur_freq = cmdCat(path_scaling_cur_freq);
            if (scaling_cur_freq.equals("")) {
                scaling_cur_freq = "0";
            }
//            System.out.println("ruiqin: read freq: cpu" + i + ":" + scaling_cur_freq + "--------------------------------");
            freqList[i] = scaling_cur_freq;
        }

        return freqList;
//        textMsg.setText(strFileList);


    }

    public double getMemoryUsedMegs(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double usedMegs = (double)(mi.totalMem-mi.availMem) / 1048576.0;

        //Percentage can be calculated for API 16+
        //long percentAvail = mi.availMem / mi.totalMem;

        return usedMegs;
    }
    public double getMemoryTotal(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double totalMegs = (double)mi.totalMem / 1048576.0;

        //Percentage can be calculated for API 16+
        //long percentAvail = mi.availMem / mi.totalMem;

        return totalMegs;
    }

    public double getMemoryUsedPercentage(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        //Percentage can be calculated for API 16+
        double percentAvail = (double)mi.availMem /(double)mi.totalMem;

        return (1-percentAvail);
    }

    //run Linux command
    //$ cat f
    private String cmdCat(String f){

//        String[] command = {"cat", f};
//        StringBuilder cmdReturn = new StringBuilder();
//
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder(command);
//            Process process = processBuilder.start();
//
//            InputStream inputStream = process.getInputStream();
//            int c;
//
//            while ((c = inputStream.read()) != -1) {
//                cmdReturn.append((char) c);
////                System.out.println("ruiqin: read from command:"+c);
//            }
//
////            System.out.println("ruiqin: read from command:"+cmdReturn);
//
//            return cmdReturn.toString().trim();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Something Wrong";
//        }

        return Utility.cmdCat(f);
    }

    /*
     * Get file list of the pattern
     * /sys/devices/system/cpu/cpu[0..9]
     */
    private File[] getCPUs(){

        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        File dir = new File("/sys/devices/system/cpu/");
        return dir.listFiles(new CpuFilter());
//        return files;
    }

    public void appendLog(String text)

    {
        Utility.appendLog(text,logname);
    }

    public String readLog() {
        return Utility.readLog(logname);
    }

//    public String readLog(){
//        String logString=null;
//
////        String logname="abc.txt";
//        File sdcard = Environment.getExternalStorageDirectory();
//
//        //trying opening the myFavourite.txt
//        try {
//            // opening the file for reading
//            InputStream instream = new FileInputStream(sdcard+"/"+logname);
//            // InputStream instream = openFileInput(sdcard+"/"+logname);
//
//            // if file the available for reading
//            if (instream != null) {
//                // prepare the file for reading
//                InputStreamReader inputreader = new InputStreamReader(instream);
//                BufferedReader buffreader = new BufferedReader(inputreader);
//
//                String line;
//
//                // reading every line of the file into the line-variable, on line at the time
//                try {
//                    while ((line = buffreader.readLine()) != null) {
//                        logString=line;
//                        System.out.println("*lelema(main): read from "+sdcard+"/"+logname+" **************************");
//                        System.out.println("*lelema(main): get: "+line+" **************************");
//                        System.exit(0);
//                    }
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    System.out.println("*lelema(main): read from " + sdcard + "/" + logname + " failed !!! **************************");
//                    System.exit(0);
//                }
//
//            }
//
//            // closing the file again
//            try {
//                instream.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                System.out.println("*lelema(main): close " + sdcard + "/" + logname + " failed !!! **************************");
//            }
//        } catch (java.io.FileNotFoundException e) {
//
//            // ding something if the myFavourite.txt does not exits
//            e.printStackTrace();
//            System.out.println("*lelema(main): open " + sdcard + "/" + logname + " failed !!! **************************");
//        }
//
//        return logString;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
