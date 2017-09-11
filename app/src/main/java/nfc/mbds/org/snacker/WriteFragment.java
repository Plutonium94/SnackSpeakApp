package nfc.mbds.org.snacker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WriteFragment extends MyFragment {

    private static final String LOG_NAME = WriteFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_write, container, false);
    }

    /*
    private void writeToNfc(Ndef ndef, String message) {
        if(ndef != null) {
            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain",message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
            } catch(IOException | FormatException e) {
                Log.e(this.getClass().getSimpleName(), "problem writing to tag");
                e.printStackTrace();
            }
        }
    }*/







}
