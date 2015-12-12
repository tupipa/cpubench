package lab.ubu.hello2;

import android.app.ActivityManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by smeller on 11/26/15.
 */
public class Utility {

    //public static long INTERVAL = 864; //for test
    //public static long INTERVAL=600000; // test 10 minutes
    //public static long INTERVAL=3600000; //test 60 miniutes
    public static long INTERVAL=864000000; //ten days in milliseconds
    public static int LOGINTERVAL = 10000; //interval time for periodically writing logs.

    public static String currentDateTime() {

        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();

        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");

        return date.format(currentLocalTime);

    }

    public static void appendLog(String text, String logFileName)

    {
        String logname = logFileName;
        //String logname="logPrime"+useWakeLock+".txt";
        File sdcard = Environment.getExternalStorageDirectory();
        File logFile = new File(sdcard, logname);

        if (!logFile.exists()) {
            System.out.println("*lelema(main):cannot read " + sdcard + "/" + logname + " **************************");
            //System.exit(0);
            try {
                logFile.createNewFile();

                System.out.println("*lelema(main): create " + sdcard + "/" + logname + " succeed**************************");
                //System.exit(0);
            } catch (IOException e) {
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

        OutputStream outputStream = null;
        OutputStreamWriter out = null;

        try {


//            System.out.println("*lelema(main): start to streaming for "+sdcard+"/"+logname+"**************************");
            outputStream = new FileOutputStream(sdcard + "/" + logname, true);
            //outputStream = openFileOutput(sdcard+"/"+logname,Context.MODE_APPEND);


//            System.out.println("*lelema(main): done creating FileOutputStream "+sdcard+"/"+logname+" succeed **************************");

            out = new OutputStreamWriter(outputStream);

//            System.out.println("*lelema(main): done creating outstream"+sdcard+"/"+logname+" succeed **************************");

            out.write(text);

            System.out.println("*lelema(main): done writing "
                    + sdcard + "/" + logname + ": "
                    + text.trim() + " **************************");

            //out.flush();
            out.close();

            //BufferedWriter for performance, true to set append to file flag
//            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//            System.out.println("write to the log in Nexus 4");
//            buf.append(text);
//            buf.newLine();
//            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("*lelema(main): failed to write " + sdcard + "/" + logname + " **************************");
            System.exit(0);

        }
//        finally {
//            if (out != null) {
//                out.close();
//            }
//        }

    }


    public static String readLog(String logFileName) {
        String logString = null;
        String logname = logFileName;
        File sdcard = Environment.getExternalStorageDirectory();

        //trying opening the myFavourite.txt
        try {
            // opening the file for reading
            InputStream instream = new FileInputStream(sdcard + "/" + logname);
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
                        logString = line;
                        System.out.println("*ruiqin(main): read from " + sdcard + "/" + logname + " **************************");
                        System.out.println("*ruiqin(main): get: " + line + " **************************");
                        System.exit(0);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("*ruiqin(main): read from " + sdcard + "/" + logname + " failed !!! **************************");
                    System.exit(0);
                }

            }

            // closing the file again
            try {
                instream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("*ruiqin(main): close " + sdcard + "/" + logname + " failed !!! **************************");
            }
        } catch (java.io.FileNotFoundException e) {

            // ding something if the myFavourite.txt does not exits
            e.printStackTrace();
            System.out.println("*ruiqin(main): open " + sdcard + "/" + logname + " failed !!! **************************");
        }

        return logString;
    }


    //run Linux command
    //$ cat f
    public static String cmdCat(String f) {

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


//    public static long getMemoryUsagePercentage(){
//        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        activityManager.getMemoryInfo(mi);
//        long availableMegs = mi.availMem / 1048576L;
//
//        //Percentage can be calculated for API 16+
//        long percentAvail = mi.availMem / mi.totalMem;
//
//        return percentAvail;
//    }

    //run Linux command
    //$ dumpsys meminfo lab.ubu.hello2
    public static String cmdDumpsysMeminfo(String packageName){

       // String[] command = {"dumpsys meminfo ", packageName};
        String[] command = {"dumpsys", "meminfo lab.ubu.hello2"};
        StringBuilder cmdReturn = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            int c;

            while ((c = inputStream.read()) != -1) {
                cmdReturn.append((char) c);
                System.out.println("lelema-mem: read from command:"+c);
            }

            System.out.println("lelema-mem: read from command:"+cmdReturn);

            return cmdReturn.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "Something Wrong";
        }

    }

    //run Linux command
    //$ dumpsys procstats lab.ubu.hello2
    public static String cmdDumpsysProcstats(String timeIntervalPattern){

        // String[] command = {"dumpsys ", " procstats "+timeIntervalPattern};
        String[] command = {"dumpsys ", " procstats --hours 3"};
        StringBuilder cmdReturn = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            int c;

            while ((c = inputStream.read()) != -1) {
                cmdReturn.append((char) c);
                System.out.println("lelema-mem: read from command:" + c);
            }

            System.out.println("lelema-mem: read from command:"+cmdReturn);

            return cmdReturn.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "Something Wrong";
        }

    }


}
