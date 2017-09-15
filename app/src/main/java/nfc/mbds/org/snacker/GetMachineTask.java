package nfc.mbds.org.snacker;

import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


/**
 * Created by user on 11/09/17.
 */

public class GetMachineTask extends AsyncTask<String, Void, String> {

    private static final String LOG_NAME = "GetMachineTask";

    @Override
    protected  void onPreExecute() {
        Log.d(LOG_NAME,"some");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(LOG_NAME,"started");
        String res = "";
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(CurrentMode.URL + params[0])).openConnection();
            connection.setConnectTimeout(7000);
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            Scanner sc = new Scanner(in).useDelimiter("\\A");
            res = (sc.hasNext())?sc.next():"";
        } catch(MalformedURLException mue) {
            mue.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return res;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
