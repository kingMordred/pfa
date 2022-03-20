package com.example.speechrecog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton imgbtn;
    EditText edttxt;
    int count =0;

    SpeechRecognizer spch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgbtn=findViewById(R.id.button);
        edttxt=findViewById(R.id.txt);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reff = database.getReference("message");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        spch=SpeechRecognizer.createSpeechRecognizer(this);

        final Intent spchint= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(count==0)
                {
                    imgbtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));

                    count=1;
                    spch.startListening(spchint);

                }
                else
                {
                    imgbtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                    spch.stopListening();
                    count=0;
                }
            }
        });

        spch.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> data = results.getStringArrayList(spch.RESULTS_RECOGNITION);

                edttxt.setText(data.get(0));
                reff.setValue(data.get(0));

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT);
            }
            else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT);
            }
        }
    }
}