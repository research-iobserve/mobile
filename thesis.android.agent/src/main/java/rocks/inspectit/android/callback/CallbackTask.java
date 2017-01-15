package rocks.inspectit.android.callback;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.inspectit.android.ExternalConfiguration;
import rocks.inspectit.android.callback.data.HelloResponse;
import rocks.inspectit.android.util.DependencyInjector;

/**
 * Created by David on 23.10.16.
 */

public class CallbackTask extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = ExternalConfiguration.getLogTag();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String callbackUrl;
    private CallbackManager callbackManager = DependencyInjector.getCallbackManager();

    public CallbackTask(String url) {
        this.callbackUrl = url;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 1 ) {
            return postRequest(callbackUrl, params[0]);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null && !result.isEmpty()) {
            try {
                HelloResponse resp = objectMapper.readValue(result, HelloResponse.class);
                if (resp.getSessionId() != null && resp.getSessionId().length() > 0) {
                    callbackManager.applySessionId(resp.getSessionId());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Couldn't read the response to the session request.");
                return;
            }
        }
    }

    private String postRequest(String rawUrl, String data) {
        Log.i(LOG_TAG, "Sending back beacon to '" + rawUrl + "'.");
        try {
            URL url = new URL(rawUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            String all = null;
            while ((output = br.readLine()) != null) {
                all = all == null ? output : all + "\n" + output;
            }
            conn.disconnect();

            return all;
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed URL while sending the data to the CMR.");
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't send the data back to the CMR because of an IOException.");
            return null;
        }
    }
}
