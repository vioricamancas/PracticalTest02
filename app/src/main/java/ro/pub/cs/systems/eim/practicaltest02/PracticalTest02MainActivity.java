package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private ServerThread serverThread;
    private EditText portText;

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.startServerButton) {
                Integer port = Integer.parseInt(portText.getText().toString());
                if (port == null)
                    return;
                Log.i(Constants.TAG, "Starting server on port"+ port);
                serverThread = new ServerThread(port);
                serverThread.startServer();
                Log.v(Constants.TAG, "Started server");
            } else if (v.getId() == R.id.requestDataButton) {
                Log.e(Constants.TAG, "sending ");

            } else {
                Log.e(Constants.TAG, "invalid button");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        portText = findViewById(R.id.portField);
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