package phlmorse.gatech.edu.phlmorse;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class WearMessageListenerService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks{
    static String message;
    static Timer timer;
    static TimerTask tTask;
    Node mNode;
    static Vibrator mVibrator;
    Handler mHandler;
    private final int REPETITION_MINUTES = 2;

    public WearMessageListenerService() {
        mHandler = new Handler();

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        message = messageEvent.getPath();
        if (message.equals("cancel")) {
            mVibrator.cancel();
            timer.cancel();
            tTask.cancel();
        } else {
            resolveNode();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        resolveNode();
    }
    private void resolveNode() {
        mHandler = new Handler();
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        try {
            List<Node> nodes = Tasks.await(Wearable.getNodeClient(this).getConnectedNodes());
            for (Node n : nodes) {
                mNode = n;
                if(!message.equals("REFRESHER")) {
                    play(message, false);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    public void sendMessage(String key) {
        Wearable.getMessageClient(this).sendMessage(mNode.getId(), "WearListClicked"+ "--" + key, key.getBytes());
    }
    public long play(final String word, boolean repeat) {
        long[] pattern;
        if (word.equals("tutorialDot")) {
            mVibrator.vibrate(250);
            return 0;
        } else if (word.equals("tutorialDash")) {
            mVibrator.vibrate(750);
            return 0;
        } else {
            MorseCode morseCode = new MorseCode(word);
            if (repeat) {
                // Vibration times for x minutes...
                pattern = morseCode.getVibrationPatternTimesForXMinutes(REPETITION_MINUTES);
            } else {
                // Vibration time for one sequence
                pattern = morseCode.getVibrationPatternTimes();
            }
            mVibrator.vibrate(pattern, -1);
            //sendMessage(word);

            timer = new Timer();
            //tTask = new TimerTask() {
            //public void run() {
            // sendMessage(word.substring(0,1));
            // }
            //};
            // timer.schedule(tTask, 1000);
            // Create future changes and speak during each change
            long delay = 1000;
            long[][] vibration_times = morseCode.getVibrationTime();

            if (repeat) {
                int i = 0;
                while (delay <= REPETITION_MINUTES * 60 * 1000) {
                    for (int j = 0; j < vibration_times[i % word.length()].length; j++) {
                        Log.d("vibrating", String.valueOf(vibration_times[i % word.length()][j]));
                        delay += vibration_times[i % word.length()][j];
                        delay += 500;
                    }
                    delay += 1000;
                    if ((i + 1) % word.length() == 0) {
                        delay += 3500;
                    }
                    final int index = (i + 1) % word.length();
                    tTask = new TimerTask() {
                        public void run() {
                            sendMessage(word.substring(index, index + 1));
                        }
                    };
                    timer.schedule(tTask, delay - 1000);
                    i++;
                }
            } else {
                for (int i = 0; i < vibration_times.length; i++) {
                    for (int j = 0; j < vibration_times[i].length; j++) {
                        delay += vibration_times[i][j];
                        delay += 500;
                        Log.d("length", String.valueOf(vibration_times.length));
                        Log.d("vibrating", String.valueOf(vibration_times[i][j]));
                    }
                    delay += 1000;
                    if (i + 1 == 0) {
                        delay += 1000;
                    }
                    tTask = new TimerTask() {
                        public void run() {
                            Log.d("reaching this", word);
                            sendMessage(word);
                        }
                    };
                    timer.schedule(tTask, delay - 1000);
                /*if(i != vibration_times.length - 1) {
                    final int index = (i + 1);
                    tTask = new TimerTask() {
                        public void run() {
                            Log.d("reaching this", "hi");
                            sendMessage(word.substring(index, index + 1));
                        }
                    };
                    timer.schedule(tTask, delay - 1000);
                }*/
                }
            }
            return delay;
        }
    }
}
