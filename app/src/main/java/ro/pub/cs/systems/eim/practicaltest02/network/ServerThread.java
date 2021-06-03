package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import androidx.constraintlayout.solver.Cache;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02.BitcoinInfo;
import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.Utilities;

public class ServerThread extends Thread {

    private boolean isRunning;
    private HashMap<String, Double> cache; // currency & rate
    private CacheUpdater cacheUpdater;

    private ServerSocket serverSocket;
    int serverPort;


    public ServerThread(int serverPort) {
        this.serverPort = serverPort;
        cache = new HashMap<>();
        cache.put(Constants.CURRENCY_USD, 0.0);
        cache.put(Constants.CURRENCY_EUR, 0.0);
        cacheUpdater = new CacheUpdater();
    }

    public void startServer() {
        if (isRunning)
            return;
        isRunning = true;
        cacheUpdater.start();
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

//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        bla bla
//                    }
//                }, 2000);
                try{
                    Log.v(Constants.TAG, "---updating cache--------------------");
                    BitcoinInfo info = RequestResolver.getInfo();
                    if (info != null) {
                        cache.put(Constants.CURRENCY_USD, info.getToUSD());
                        cache.put(Constants.CURRENCY_EUR, info.getToEUR());
                    } else  {
                        Log.e(Constants.TAG, "error getting info");
                    }
                    ServerThread.sleep(30*1000);
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
                    Double usd, eur;
                    usd = cache.get(Constants.CURRENCY_USD);
                    eur = cache.get(Constants.CURRENCY_EUR);
                    RequestResolver communicationThread = new RequestResolver(socket, usd, eur);
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
