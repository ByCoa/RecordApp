package com.example.ecoa.recordapp.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.example.ecoa.recordapp.R;

public class RecordActivity extends AppCompatActivity {

    Button btn_startRecord, btn_stopRecord; //buttonPlayLastRecordAudio,buttonStopPlayingRecording ;
    Chronometer chronometer;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);
        findViewsById();
        setListeners();
    }

    public void findViewsById(){
        btn_startRecord = (Button) findViewById(R.id.btn_record);
        btn_stopRecord = (Button) findViewById(R.id.btn_stop_record);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        btn_stopRecord.setEnabled(false);
        btn_stopRecord.setAlpha(.5f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, RecordListAcitivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public void setListeners (){
        random = new Random();
        btn_startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission() && createFolder()) {
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.voice_records) + File.separator + CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                    MediaRecorderReady();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    btn_startRecord.setEnabled(false);
                    btn_stopRecord.setEnabled(true);
                    btn_startRecord.setAlpha(.5f);
                    btn_stopRecord.setAlpha(1);
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    Toast.makeText(RecordActivity.this, "Recording started", Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
            }
        });

        btn_stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                chronometer.stop();
                btn_stopRecord.setEnabled(false);
                btn_startRecord.setEnabled(true);
                btn_stopRecord.setAlpha(.5f);
                btn_startRecord.setAlpha(1);
                Toast.makeText(RecordActivity.this, "Recording Completed ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean createFolder (){
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getString(R.string.voice_records));
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            return true;
        }
        return false;
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecordActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}