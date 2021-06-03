package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.practicaltest02.BitcoinInfo;
import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.Utilities;

public class RequestResolver extends Thread {
    private Socket socket;

    public RequestResolver(Socket socket) {
        this.socket = socket;
    }

    public static BitcoinInfo getInfo() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Constants.BITCOIN_URL);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            String content = httpClient.execute(httpGet, responseHandler);
            JSONObject result = new JSONObject(content);
            JSONObject bpi = result.getJSONObject("bpi");
            JSONObject usd = bpi.getJSONObject("USD");
            JSONObject eur = bpi.getJSONObject("EUR");

            return new BitcoinInfo(
                    usd.getDouble("rate_float"),
                    eur.getDouble("rate_float"));
        } catch (JSONException jsonException) {
            Log.e(Constants.TAG, jsonException.getMessage());
            if (Constants.DEBUG) {
                jsonException.printStackTrace();
            }
        } catch (ClientProtocolException clientProtocolException) {
            Log.e(Constants.TAG, clientProtocolException.getMessage());
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort()+ " from " + socket.getInetAddress());
            PrintWriter printWriter = Utilities.getWriter(socket);
            BitcoinInfo info = getInfo();
            if (info != null) {
                printWriter.println(info.getToUSD());
                printWriter.println(info.getToEUR());
            } else {
                printWriter.println("oh shit");
            }
            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
