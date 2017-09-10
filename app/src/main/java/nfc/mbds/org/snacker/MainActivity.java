package nfc.mbds.org.snacker;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Button goToRead, goToWrite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToRead = (Button)findViewById(R.id.goToRead);
        goToWrite = (Button)findViewById(R.id.goToWrite);

        final Context that = this;
        goToWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(that, WriteActivity.class));
            }
        });
    }
}
