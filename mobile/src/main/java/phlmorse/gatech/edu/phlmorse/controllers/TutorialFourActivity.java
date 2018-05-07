package phlmorse.gatech.edu.phlmorse.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import phlmorse.gatech.edu.phlmorse.R;

/**
 * Created by sanjanakadiveti on 5/6/18.
 */

public class TutorialFourActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks{
    Button next;
    Button dot;
    Button dash;
    static Node mNode;
    private static GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial4);
        next = findViewById(R.id.tt4finish);
        dot = findViewById(R.id.ttdot);
        dash = findViewById(R.id.ttdash);
        initGoogleApiClient();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorialFourActivity.this, ApplicationActivity.class);
                intent.putExtra("Username", getIntent().getStringExtra("Username"));
                startActivity(intent);
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("tutorialDot");
            }
        });
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("tutorialDash");
            }
        });
    }
    public void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
        learn("");
    }
    public static void learn(String s) {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult connectedNodes) {
                for (Node connectedNode : connectedNodes.getNodes()) {
                    mNode = connectedNode;
                    sendMessage(s);
                }
            }
        });
    }
    private static void sendMessage(String message) {
        if(googleApiClient != null &&
                googleApiClient.isConnected() &&
                mNode != null) {
            Wearable.MessageApi.sendMessage(googleApiClient, mNode.getId(),message, message.getBytes())
                    .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                            if(sendMessageResult.getStatus().isSuccess()) {
                            } else {
                            }
                        }
                    });
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
