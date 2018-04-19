package phlmorse.gatech.edu.phlmorse;


import android.os.Bundle;
import android.os.PowerManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.wearable.Wearable;

/**
 * Created by sanjanakadiveti on 3/31/18.
 */

public class MainActivity extends WearableActivity {
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = findViewById(R.id.title);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        Log.d("hello", "um");
        setContentView(R.layout.activity_main);
        Log.d("Hello", "hello1");
        setAmbientEnabled();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        updateDisplay();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            //title.setText("PHL MORSE - AMBIENT");
//
        } else {
            //title.setText("PHL MORSE - NOT AMBIENT");
//
        }
    }

}
