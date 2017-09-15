package nfc.mbds.org.snacker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.*;
import android.nfc.*;
import android.nfc.tech.MifareClassic;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_NAME = MainActivity.class.getSimpleName();

    private MyFragment readFragment, writeFragment;
    private TextView readTextView;

    private RadioGroup readWriteGroup;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;
    private String[][] techListArray;

    private TextToSpeech tts;

    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        readFragment = (MyFragment) fm.findFragmentById(R.id.read_fragment);
        writeFragment = (MyFragment) fm.findFragmentById(R.id.write_fragment);

        resultTextView = (TextView)findViewById(R.id.result_text);
        readTextView = (TextView)findViewById(R.id.readText);

        final Context that = this;
        readWriteGroup = (RadioGroup)findViewById(R.id.read_write);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


        Intent i = new Intent(this, getClass());
        i = i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            intentFilter.addDataType("*/*");
        } catch(IntentFilter.MalformedMimeTypeException mmte) {
            mmte.printStackTrace();
        }
        filters = new IntentFilter[]{intentFilter};

        techListArray = new String[][] { new String[]{ MifareClassic.class.getName()}};

        setFragmentVisibility();

        readWriteGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rgroup, int checkedId) {
                CurrentMode.admin = (rgroup.getCheckedRadioButtonId() == R.id.write_radio);
                setFragmentVisibility();
              }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.FRENCH);
            }
        });


    }

    public void setFragmentVisibility() {

        readFragment.show(!CurrentMode.admin);
        writeFragment.show(CurrentMode.admin);

    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techListArray);
    }

    private static byte[] toByte16Array(String s) {
        byte[] res = new byte[16];

        byte[] contient = s.getBytes(Charset.forName("UTF-8"));
        int length = contient.length;
        for(int i = 0; i < 16; i++) {
            if(i < length) {
                res[i] = contient[i];
            }

        }
        return res;
    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);



        Log.d(LOG_NAME," onNewIntent : " + intent.getAction());

        Log.d(LOG_NAME," onNewIntent : " + tag.getId());

        if(tag != null) {
            MifareClassic mfc = MifareClassic.get(tag);
            try {
                mfc.connect();
                boolean authA = mfc.authenticateSectorWithKeyA(1, MifareClassic.KEY_DEFAULT);
                Log.d(LOG_NAME,"x"+authA);
                //resultTextView.setText("Authentication " + (authA?"successfull":"unsuccessfull"));
                if(CurrentMode.admin) {
                    WriteFragment wf = (WriteFragment)writeFragment;
                    String tbw = wf.getToBeWritten().length() > 0 ? wf.getToBeWritten(): "";
                    Log.d(LOG_NAME,"tbw : " + tbw);
                    mfc.writeBlock(mfc.sectorToBlock(1), toByte16Array(tbw));
                } else {
                    byte[] b = mfc.readBlock(mfc.sectorToBlock(1));
                    String readText = new String(b, Charset.forName("UTF-8"));
                    Log.d(LOG_NAME," read " + readText);
                    readTextView.setText(readText + " " + bin2hex(tag.getId()));

                    GetMachineTask gmt = new GetMachineTask();
                    String toSay = "";
                    try {
                        gmt.execute("/vending-machine/"+readText);
                        toSay = gmt.get();
                    } catch(ExecutionException ee) {
                        ee.printStackTrace();
                    } catch(InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    tts.speak((toSay.equals("")?readText:toSay), TextToSpeech.QUEUE_FLUSH, null);

                }

                mfc.close();

            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }






    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }
}
