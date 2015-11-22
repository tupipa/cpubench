package lab.ubu.hello2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Fibonacci2IntentService extends IntentService {

    public Fibonacci2IntentService() {
        super("Fibonacci2IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            int i=500000000;

//            while (i<1000) {
                while (true) {
                    int fib = fibnacci(i);
//                i++;
                //Snackbar.make(view, "Fibnacci(1000):"+fib, Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                //sleep(500);
//                System.out.println("lelema: fib2("+i+")="+fib);

            }
        }
    }
    public int fibnacci(int a){
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

}
