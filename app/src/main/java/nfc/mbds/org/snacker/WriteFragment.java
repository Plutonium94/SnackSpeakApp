package nfc.mbds.org.snacker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WriteFragment extends MyFragment {

    private static final String LOG_NAME = WriteFragment.class.getSimpleName();
    private TextView machinesView;
    private TextView toWrite;

    private Button getMachinesButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View res =  inflater.inflate(R.layout.fragment_write, container, false);
        getMachinesButton = (Button)res.findViewById(R.id.getmachines);
        machinesView = (TextView)res.findViewById(R.id.machines);
        toWrite = (TextView)res.findViewById(R.id.toWrite);
        getMachinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    GetMachineTask gmt = new GetMachineTask();
                    String res= gmt.execute("/vending-machines").get();
                    Log.d(LOG_NAME, res);
                    machinesView.setText(res);

                } catch(InterruptedException ie) {
                    ie.printStackTrace();
                } catch(ExecutionException ee) {
                    ee.printStackTrace();
                }
            }
        });

        return res;
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

    public Button getGetMachinesButton() {
        return getMachinesButton;
    }

    public String getToBeWritten() {
        return toWrite.getText().toString();
    }





}
