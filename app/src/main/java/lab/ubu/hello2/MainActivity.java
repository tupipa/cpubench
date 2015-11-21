package lab.ubu.hello2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
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
import java.util.Date;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.IOException;

/************************************
 * version3
 *
 * mxplayer launch automatically
 *
 * 
 * version2
 *
 * log information to sdcard. logBattery.txt
 *
 *
 * version1
 *
 * provide fibonacci, fibonacci2, prime,factor function.
 *
 */

public class MainActivity extends AppCompatActivity {
    private TextView batteryTxt;
    private TextView batteryinfo;
    private TextView timestamp;
    private int batteryPercent;
    private int batteryVoltage;
    private int batteryScale;
    private int batteryStatus;

    private String batteryvalue = "null";

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

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryPercent=level;
            batteryVoltage=intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            batteryScale=intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            batteryStatus=intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);

            batteryvalue = String.valueOf(level) + "%";
            batteryTxt.setText(batteryvalue);


            System.out.println("mBatInfoReceiver: System.currentTimeMillis()*" + System.currentTimeMillis());
            timestamp.setText(String.valueOf(System.currentTimeMillis()));

            appendLog("BatInfoReceiver: " + batteryvalue + "-" + timestamp.getText());
            readLog();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        batteryTxt = (TextView) this.findViewById(R.id.batteryTxt);
        batteryinfo =  (TextView) this.findViewById(R.id.batteryinfo);
        timestamp =  (TextView) this.findViewById(R.id.timestamp);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        appendLog("abcefergtfergrghrtgtrt");
        //readLog();
        //System.exit(0);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

      /*  intentFib= new Intent(this, FibonacciIntentService.class);
        intentFib2= new Intent(this, FibonacciIntentService.class);

        startService(intentFib);
        startService(intentFib2);
*/
        if (! batteryTxt.getText().toString().equals(batteryvalue)){
            batteryvalue = batteryTxt.getText().toString();
            batteryTxt.setText(batteryvalue);
            System.out.println("System.currentTimeMillis()*"+System.currentTimeMillis());
            timestamp.setText(String.valueOf(System.currentTimeMillis()));

//            try {
//                Process process = Runtime.getRuntime().exec("logcat -d");
//                BufferedReader bufferedReader = new BufferedReader(
//                        new InputStreamReader(process.getInputStream()));
//
//                StringBuilder log=new StringBuilder();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    log.append(line);
//                }
//                TextView tv = (TextView)findViewById(R.id.textView1);
//                tv.setText(log.toString());
//            } catch (IOException e) {
//            }

            appendLog("Initial"+batteryvalue + "-"+ timestamp.getText());

        }


//        batteryTxt.setText("The Battery Left: "+batteryTxt.getText() + "; Voltage:" + batteryVoltage
//                +"; Scale: "+batteryScale+"; Status: "+batteryStatus);


//        batteryinfo.setText("The Battery Left: "+batteryTxt.getText());


        System.out.println("*lelema:*start 1st prime **************************");
        intentPri= new Intent(this, PrimeIntentService.class);
        System.out.println("*lelema:*start 2nd prime **************************");
        intentPri2= new Intent(this, PrimeIntentService.class);
        System.out.println("*lelema:*start 3rd prime **************************");
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

        MXplayer = new Intent(this, MXplayerIntentService.class);

        startService(intentPri);
        startService(intentPri2);
//        startService(intentPri3);
//        startService(intentPri4);
//        startService(intentPri5);

        startService(fibnacci1);
        startService(fibnacci2);
//        startService(fibnacci3);
//        startService(fibnacci4);

        startService(fibnacci21);
        startService(fibnacci22);
//        startService(fibnacci23);
//        startService(fibnacci24);

        startService(factor1);
        startService(factor2);
//        startService(factor3);
//        startService(factor4);

        startService(MXplayer);
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

                System.out.println("*lelema(main): create "+sdcard+"/"+logname+"suceed**************************");
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
