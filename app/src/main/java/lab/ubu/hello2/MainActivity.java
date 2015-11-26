package lab.ubu.hello2;


import android.app.Activity;
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
 * version 5
 * -TODO: Rui branch: add WAKE_LOCK to prevent sleep while screen locked.
 * -TODO: when start cpu bench: log the real time format at start; 
 * 	add /n to make the start message one line;
 * -TODO: add clock for logBattery options: when battery broadcast 
 * 	rarely happens for a long time, still periodically log the 
 * 	frequency and battery.
 * -TODO: 
 * -TODO:
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



    private Intent intentFib;
    private Intent intentFib2;

    private Intent intentPri;
    private Intent intentPri2;
    private Intent intentPri3;
    private Intent intentPri4;
    private Intent intentPri5;

    private Intent fibnacci1;
    private Intent fibnacci2;
    private Intent fibnacci3;
    private Intent fibnacci4;

    private Intent fibnacci21;
    private Intent fibnacci22;
    private Intent fibnacci23;
    private Intent fibnacci24;

    private Intent factor1;
    private Intent factor2;
    private Intent factor3;
    private Intent factor4;

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
            System.out.println("System.currentTimeMillis()*" + System.currentTimeMillis());


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
        String log="\n*ruiqin: MainActivity of Hello2 is now in onDestroy()... running for "+ interval;
        System.out.println(log);
        appendLog(log);
       // wl.release();
       // System.out.println("*ruiqin: onDestroy(): wake lock realsed **************************");
        this.unregisterReceiver(this.mBatInfoReceiver);
        System.out.println("*ruiqin: onDestroy(): the battery receiver unregistered**************************");
        super.onDestroy();
    }



    public void startCPUbench(){


        time_old=System.currentTimeMillis();
        appendLog("CPUbench started at " + time_old);

        intentPri= new Intent(this, PrimeIntentService.class);
        intentPri2= new Intent(this, PrimeIntentService.class);
        intentPri3= new Intent(this, PrimeIntentService.class);
        intentPri4= new Intent(this, PrimeIntentService.class);
        intentPri5= new Intent(this, PrimeIntentService.class);

        fibnacci1 = new Intent(this, FibonacciIntentService.class);
        fibnacci2 = new Intent(this, FibonacciIntentService.class);
        fibnacci3 = new Intent(this, FibonacciIntentService.class);
        fibnacci4 = new Intent(this, FibonacciIntentService.class);

        fibnacci21 = new Intent(this, Fibonacci2IntentService.class);
        fibnacci22 = new Intent(this, Fibonacci2IntentService.class);
        fibnacci23 = new Intent(this, Fibonacci2IntentService.class);
        fibnacci24 = new Intent(this, Fibonacci2IntentService.class);

        factor1 = new Intent(this, FactorService.class);
        factor2 = new Intent(this, FactorService.class);
        factor3 = new Intent(this, FactorService.class);
        factor4 = new Intent(this, FactorService.class);

        System.out.println("*lelema:*start 1st prime **************************");
//        intentPri.putExtra("countInstance",1);
        startService(intentPri);

        System.out.println("*lelema:*start 2nd prime **************************");
//        intentPri2.putExtra("countInstance",2);
        intentPri2.putExtra("useWakeLock",true);
        startService(intentPri2);
//        System.out.println("*lelema:*start 3rd prime **************************");
//        startService(intentPri3);
//        startService(intentPri4);
//        startService(intentPri5);
//
//        startService(fibnacci1);
//        startService(fibnacci2);
////        startService(fibnacci3);
////        startService(fibnacci4);
//
//        startService(fibnacci21);
//        startService(fibnacci22);
////        startService(fibnacci23);
////        startService(fibnacci24);
//
//        startService(factor1);
//        startService(factor2);
////        startService(factor3);
////        startService(factor4);


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


        logStr= builder.toString();


        batteryTxt.setText(logStr);
        System.out.println("*ruiqin: batinfo and all cpu frequencies:" + logStr + " ***********************");
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

    //run Linux command
    //$ cat f
    private String cmdCat(String f){

        String[] command = {"cat", f};
        StringBuilder cmdReturn = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            int c;

            while ((c = inputStream.read()) != -1) {
                cmdReturn.append((char) c);
//                System.out.println("ruiqin: read from command:"+c);
            }

//            System.out.println("ruiqin: read from command:"+cmdReturn);

            return cmdReturn.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "Something Wrong";
        }

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
//        String logname="abc.txt";
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

        OutputStream outputStream;
        OutputStreamWriter out;

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

    public String readLog(){
        String logString=null;

//        String logname="abc.txt";
        File sdcard = Environment.getExternalStorageDirectory();

        //trying opening the myFavourite.txt
        try {
            // opening the file for reading
            InputStream instream = new FileInputStream(sdcard+"/"+logname);
            // InputStream instream = openFileInput(sdcard+"/"+logname);

            // if file the available for reading
            if (instream != null) {
                // prepare the file for reading
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line;

                // reading every line of the file into the line-variable, on line at the time
                try {
                    while ((line = buffreader.readLine()) != null) {
                        logString=line;
                        System.out.println("*lelema(main): read from "+sdcard+"/"+logname+" **************************");
                        System.out.println("*lelema(main): get: "+line+" **************************");
                        System.exit(0);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("*lelema(main): read from " + sdcard + "/" + logname + " failed !!! **************************");
                    System.exit(0);
                }

            }

            // closing the file again
            try {
                instream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("*lelema(main): close " + sdcard + "/" + logname + " failed !!! **************************");
            }
        } catch (java.io.FileNotFoundException e) {

            // ding something if the myFavourite.txt does not exits
            e.printStackTrace();
            System.out.println("*lelema(main): open " + sdcard + "/" + logname + " failed !!! **************************");
        }

        return logString;
    }
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
