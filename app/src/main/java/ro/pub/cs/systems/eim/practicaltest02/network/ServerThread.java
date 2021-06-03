package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import androidx.constraintlayout.solver.Cache;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02.Constants;

public class ServerThread extends Thread {

    private boolean isRunning;
    private HashMap<String, Integer> cache; // currency & rate
    private CacheUpdater cacheUpdater;

    private ServerSocket serverSocket;
    int serverPort;


    public ServerThread(int serverPort) {
        this.serverPort = serverPort;
        cacheUpdater = new CacheUpdater();
        cache = new HashMap<>();
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    private class CacheUpdater extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                try{
                    sleep(60*1000);
                }
                catch (Exception e){
                    Log.e(Constants.TAG, "error sleep");
                }
            }
        }
    }
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort, 50, InetAddress.getByName("0.0.0.0"));

            while (isRunning) {
                Socket socket = serverSocket.accept();
                Log.v(Constants.TAG, "accept()-ed: " + socket.getInetAddress());
                if (socket != null) {
                    RequestResolver communicationThread = new RequestResolver(socket);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }


}
