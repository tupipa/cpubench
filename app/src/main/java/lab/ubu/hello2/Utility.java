package lab.ubu.hello2;

import android.os.Environment;

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
 * Created by smeller on 11/26/15.
 */
public class Utility {

    //public static long INTERVAL=864; //for test
    //public static long INTERVAL=600000; // test 10 minutes
    //public static long INTERVAL=3600000; //test 60 miniutes
    public static long INTERVAL=864000000; //ten days in milliseconds
    public static int LOGINTERVAL=10000; //interval time for periodically writing logs.

    public static String currentDateTime(){

        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();

        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");

        return date.format(currentLocalTime);


    }

    public static void appendLog(String text, String logFileName)

    {
        String logname=logFileName;
        //String logname="logPrime"+useWakeLock+".txt";
        File sdcard = Environment.getExternalStorageDirectory();
        File logFile = new File(sdcard,logname);

        if (!logFile.exists())
        {
            System.out.println("*lelema(main):cannot read "+sdcard+"/"+logname+" **************************");
            //System.exit(0);
            try
            {
                logFile.createNewFile();

                System.out.println("*lelema(main): create "+sdcard+"/"+logname+" succeed**************************");
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


//            System.out.println("*lelema(main): start to streaming for "+sdcard+"/"+logname+"**************************");
            outputStream = new FileOutputStream(sdcard+"/"+logname,true);
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