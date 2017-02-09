package com.echo.develop.echo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class WallActivity extends AppCompatActivity {
//    MediaPlayer mp;
    MediaPlayer mp = new MediaPlayer();
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        playButton = (ImageButton) findViewById(R.id.imageButton31);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playAudioLocal("audio_msg2.3gp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        playButton = (ImageButton) findViewById(R.id.imageButton12);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playAudioFirebase();
            }
        });
    }

    private void playAudioLocal(String fileName) throws IOException {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Echowall/" + fileName;
        File appFile = new File(filePath);
        if (appFile.exists()) {
            mp.reset();
            mp = new MediaPlayer();
//            mp.release();
            mp.setDataSource(filePath);
            mp.prepare();
            mp.start();
        }
    }

    private void playAudioFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("audio_path2");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference gsReference = storage.getReferenceFromUrl(value);

                String localPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Echowall/";
                File newFile = new File(localPath);
                try {
                    File localFile = File.createTempFile("audio_msg2", "3gp", newFile);

                    gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                            String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Echowall/audio_msg2.3gp";
                            File tempFile = new File(tempPath);
                            if (tempFile.exists()) {
                                mp.reset();
                                mp = new MediaPlayer();
//                                mp.release();
                                try {
                                    mp.setDataSource(tempPath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mp.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mp.start();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void playMessage(View view) {
//        String filename = Environment.getExternalStorageDirectory().getAbsolutePath();
//        filename += "/audiorecordtest.3gp";
//        try {
//            mp.setDataSource(filename);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        mp.reset();
        mp = new MediaPlayer();
//        mp.release();
        mp = MediaPlayer.create(this, R.raw.tsuan01);
        mp.start();
    }

    public void playMusic(View view) {
        mp.reset();
        mp = new MediaPlayer();
//        mp.release();
        mp = MediaPlayer.create(this, R.raw.blowing03);
        mp.start();
    }

    public void makePostcard(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }
}
