package ro.pub.cs.systems.eim.practicaltest02.network;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.PracticalTest02MainActivity;
import ro.pub.cs.systems.eim.practicaltest02.Utilities;

public class ClientTask extends Thread {
    private TextView result;
    private TextView currency;
    private TextView serverPort;

    public ClientTask(TextView serverPort, TextView result, TextView currency) {
        this.result = result;
        this.currency = currency;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            Log.v(Constants.TAG + "client", "starting running client");
            String serverAddress = "localhost";
            Integer port = Integer.parseInt(serverPort.getText().toString());
            if (port == null)
                return;
            socket = new Socket(serverAddress, port);
            if (socket == null) {
                return;
            }
            Log.v(Constants.TAG + "client", "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(currency.getText().toString());
            printWriter.flush();

            BufferedReader bufferedReader = Utilities.getReader(socket);
            String currentLine;
            if ((currentLine = bufferedReader.readLine()) != null) {
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(currentLine);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}

