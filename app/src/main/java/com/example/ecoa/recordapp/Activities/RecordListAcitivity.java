package com.example.ecoa.recordapp.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.ecoa.recordapp.Models.RecordObject;
import com.example.ecoa.recordapp.R;
import com.example.ecoa.recordapp.ViewsAdapter.RecordAdapter;
import com.example.ecoa.recordapp.ViewsAdapter.RecyclerItemClickListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ecoa on 6/26/2017.
 */

public class RecordListAcitivity extends AppCompatActivity {
    private RecyclerView rv_record;
    private RecyclerView.LayoutManager lnlout_manager;
    private RecordAdapter recordAdapter;
    private MediaPlayer mediaPlayer;
    private List recordObjects;
    private FloatingActionButton fab_addRecord;
    private boolean isSpeakButtonLongPressed = false;
    private ImageView imgv_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_recyclerview);
        findViewsById();
        setAdapters();
        setListeners();

    }

    public void findViewsById(){
        rv_record = (RecyclerView) findViewById(R.id.rv_record);
        fab_addRecord = (FloatingActionButton) findViewById(R.id.fab_add_record);
    }

    public void setAdapters(){
        getData();
        rv_record.setHasFixedSize(true);
        lnlout_manager = new LinearLayoutManager(this);
        rv_record.setLayoutManager(lnlout_manager);
        recordAdapter= new RecordAdapter(recordObjects);
        rv_record.setAdapter(recordAdapter);
    }
    public void getData (){
        if(createFolder()) {
            recordObjects = new ArrayList();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.voice_records);
            File directory = new File(path);
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                Date lastModDate = new Date(files[i].lastModified());
                recordObjects.add(new RecordObject( files[i].getName().split("\\.")[0], lastModDate.toString(),files[i].getName().split("\\.")[1],files[i].getAbsolutePath()));
            }
        }
    }

    private void playStoredAudioMusic(String voiceStoragePath){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void stopStoredAudioMusic (){
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }
    public void setListeners(){
        fab_addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordListAcitivity.this, RecordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rv_record.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rv_record,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        imgv_mic = (ImageView) view.findViewById(R.id.imgv_record);
                        imgv_mic.setImageResource(R.drawable.ic_mic);
                        RecordObject recordObject= recordAdapter.getChild(position);
                        playStoredAudioMusic(recordObject.getPath());
                        isSpeakButtonLongPressed = true;
                        Snackbar.make(view, "Audio "+recordObject.getTitle()+" is playing", Snackbar.LENGTH_LONG).show();
                    }
                })
        );

        rv_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Do something when user touch the screen
                        //(first touch event-before moving or releasing the finger)
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isSpeakButtonLongPressed) {
                            stopStoredAudioMusic();
                            imgv_mic.setImageResource(R.drawable.ic_mic_none_light_blue_500_48dp);
                            isSpeakButtonLongPressed=false;
                        }
                        break;
                }
                return false;
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
}
