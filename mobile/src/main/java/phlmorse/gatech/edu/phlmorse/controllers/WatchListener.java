package phlmorse.gatech.edu.phlmorse.controllers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Locale;

public class WatchListener extends WearableListenerService {
    public static String SERVICE_CALLED_WEAR = "WearListClicked";
    //private int MY_DATA_CHECK_CODE = 0;
    //String message;
    //private int MY_DATA_CHECK_CODE = 0;
    private static TextToSpeech myTTS;
    static String[] messageArr;
    static String message;
    public WatchListener() {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("HELLOOOOOOOO", "WHAT");
        messageArr = messageEvent.getPath().split("--");
        message = messageArr[1];

        if (messageArr[0].equals(SERVICE_CALLED_WEAR)) {
            //LearningActivity.speakString(message);
            LearningActivity.learn();
        }
    }

}
