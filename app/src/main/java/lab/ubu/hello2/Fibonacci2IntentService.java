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
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "lab.ubu.hello2.action.FOO";
    private static final String ACTION_BAZ = "lab.ubu.hello2.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "lab.ubu.hello2.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "lab.ubu.hello2.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Fibonacci2IntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Fibonacci2IntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public Fibonacci2IntentService() {
        super("Fibonacci2IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }

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
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
