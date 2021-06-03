package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.network.ClientTask;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private ServerThread serverThread;
    private EditText portText;
    private ClientTask clientTask;
    private Integer currentPort = null;

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.startServerButton) {
                Integer port = Integer.parseInt(portText.getText().toString());
                if (port == null)
                    return;
                Log.i(Constants.TAG, "Starting server on port"+ port);
                if (currentPort == null) {
                    currentPort = port;
                } else if (!currentPort.equals(port)) {
                    serverThread.stopServer();
                } else {
                    return;
                }
                serverThread = new ServerThread(port);
                serverThread.startServer();
                Log.v(Constants.TAG, "Started server");
            } else if (v.getId() == R.id.requestDataButton) {
                Log.e(Constants.TAG, "requesting currency");
                clientTask.start();
            } else {
                Log.e(Constants.TAG, "invalid button");
            }
        }
    }

    public static void updateResource(TextView textView, String s) {
        textView.setText(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        portText = findViewById(R.id.portField);
        clientTask = new ClientTask(portText,
                findViewById(R.id.result),
                findViewById(R.id.currency));
        ButtonListener listener = new ButtonListener();
        findViewById(R.id.requestDataButton).setOnClickListener(listener);
        findViewById(R.id.startServerButton).setOnClickListener(listener);
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}