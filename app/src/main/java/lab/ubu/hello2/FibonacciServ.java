package lab.ubu.hello2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FibonacciServ extends Service {
    public FibonacciServ() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
