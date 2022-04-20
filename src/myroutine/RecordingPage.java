package com.myroutine.myroutine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.myroutine.myroutine.R.string.play;

public class RecordingPage extends AppCompatActivity {

    TextView msg;
    private Button record, stop, play, donebtn;
    private MediaRecorder mr;
    private String outputFile, filename;
    File audioFile, path;
    String AudioSavePathInDevice = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorcoding_page);
        if (getIntent().hasExtra("editMode")) {
            AudioSavePathInDevice = getIntent().getExtras().getString("editMode");
        }
        if (getIntent().hasExtra("playback")) {
            AudioSavePathInDevice = getIntent().getExtras().getString("playback");
        }


        msg = (TextView) findViewById(R.id.loadrec);
        donebtn = (Button) findViewById(R.id.donerecbtnbtn);
        donebtn.setTextColor(ContextCompat.getColor(RecordingPage.this, R.color.lavendergrey));
        donebtn.setEnabled(false);
        record = (Button) findViewById(R.id.recordbtn);
        play = (Button) findViewById(R.id.playrecbtn);
        stop = (Button) findViewById(R.id.stopbtn);

        if (AudioSavePathInDevice != null) {
            stop.setEnabled(false);
            play.setEnabled(true);
        } else {
            stop.setEnabled(false);
            play.setEnabled(false);
        }



        SimpleDateFormat fmt = new SimpleDateFormat("MM-dd_HH:mm:ss");
        filename = fmt.format(new Date());
        //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/recording"+filename+".3gp";
        path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/files/");
        path.mkdirs();


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (AudioSavePathInDevice != null) {
                    File x = new File(AudioSavePathInDevice);
                    x.delete();
                    AudioSavePathInDevice = null;
                }


                try {
                    audioFile = File.createTempFile("recording" + filename, ".3gp", path);
                    AudioSavePathInDevice = audioFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MediaRecorderReady();

                try {
                    mr.prepare();
                    mr.start();

                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msg.setText("Recording...");
                stop.setEnabled(true);
                record.setEnabled(false);
                play.setEnabled(false);
                //Toast.makeText(getApplicationContext(), "recording started", Toast.LENGTH_LONG).show();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mr.stop();
                mr.release();
                mr = null;
                stop.setEnabled(false);
                record.setEnabled(false);
                play.setEnabled(true);
                donebtn.setTextColor(ContextCompat.getColor(RecordingPage.this, R.color.black));
                donebtn.setEnabled(true);
                msg.setText("Stopped!");
                //Toast.makeText(getApplicationContext(), "recorded successfully", Toast.LENGTH_LONG).show();

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(AudioSavePathInDevice);
                    mp.prepare();
                    Toast.makeText(getApplicationContext(), "recording playing", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();
                msg.setText("Playing...");
                record.setEnabled(true);
                stop.setEnabled(false);


            }
        });

    }

    public void MediaRecorderReady() {

        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mr.setOutputFile(AudioSavePathInDevice);

    }


    public void backFunction(View view) {
        Intent x = new Intent(this, NewStep.class);
        if (getIntent().hasExtra("editMode")) {
            String prev = getIntent().getExtras().getString("editMode");
            if(prev != null){
                if (AudioSavePathInDevice.equals(prev)) {
                    finish();
                } else {
                    audioFile.delete();
                    finish();
                }
            }else{
                finish();
            }


        } else {
            if (AudioSavePathInDevice != null) {
                String playback = getIntent().getExtras().getString("playback");
                if (AudioSavePathInDevice.equals(playback)) {
                    finish();
                } else {
                    audioFile.delete();
                    finish();
                }

            } else {
                finish();
            }

        }


    }


    public void doneFunction(View view) {
        //Toast.makeText(this,outputFile,Toast.LENGTH_LONG).show();
        /*Intent x = new Intent(this, NewStep.class);
        x.putExtra("audio", outputFile);
        startActivity(x);*/
        Intent intent = new Intent();
        intent.putExtra("audio", AudioSavePathInDevice);
        setResult(RESULT_OK, intent);
        finish();
    }


}
