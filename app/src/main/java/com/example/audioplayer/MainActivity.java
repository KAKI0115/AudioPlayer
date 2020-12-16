package com.example.audioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    TextView playerPosition,playerDuration,songTitle,songArtist;
    SeekBar seekBar;
    ImageView btPlay,btNext,btPre,artwork;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    AudioManager mAudioManager;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btPlay = findViewById(R.id.bt_play);
        btNext = findViewById(R.id.bt_next);
        btPre = findViewById(R.id.bt_pre);
        songTitle = findViewById(R.id.text_song_title);
        songArtist = findViewById(R.id.text_song_artist);
        artwork = findViewById(R.id.image_artwork);

        //Creating an ArrayList to Store our songs
        ArrayList<Integer> songs = new ArrayList<>();

        songs.add(0,R.raw.music000);
        songs.add(1,R.raw.music001);
        songs.add(2,R.raw.music002);
        songs.add(3,R.raw.music003);
        songs.add(4,R.raw.music004);

        //Initialize media player
        //mediaPlayer = MediaPlayer.create(this,R.raw.cooking);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));

        //Initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // Set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Handler post delay for 0.5 secvond
                handler.postDelayed(this,500);
            }
        };

        //Get duration of media player
        int duration = mediaPlayer.getDuration();
        //Convert millisecond to minute and second
        String sDuration = convertFormat(duration);
        //Set duration on text view
        playerDuration.setText(sDuration);
        PlaySong();

        btPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    PauseSong();
                } else {
                    PlaySong();
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null){
                    //Show pause button
                    btPlay.setImageResource(R.drawable.icon_pause);
                }

                if (currentIndex < songs.size() - 1){
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mediaPlayer.start();
                SongNames();
            }
        });

        btPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null){
                    //Show pause button
                    btPlay.setImageResource(R.drawable.icon_pause);
                }

                if (currentIndex > 0){
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mediaPlayer.start();
                SongNames();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Check condition
                if (fromUser){
                    //When drag the seek bar
                    //Setprogress on seek bar
                    mediaPlayer.seekTo(progress);
                }
                //Set current position on text view
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //Show play button
                btPlay.setImageResource(R.drawable.btn_play);
                //Set media player to initial position
                mediaPlayer.seekTo(0);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 画面上下のバーを非表示
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    private void SongNames(){
        if (currentIndex == 0){
            songTitle.setText("Cooking Song");
            songArtist.setText("Jean Traihdnman");
            artwork.setImageResource(R.drawable.artwork00);
        }
        if (currentIndex == 1){
            songTitle.setText("Tsuki to hayabusa");
            songArtist.setText("maoudamashii");
            artwork.setImageResource(R.drawable.artwork01);
        }
        if (currentIndex == 2){
            songTitle.setText("iro_wo_nakushita_amethyst");
            songArtist.setText("maoudamashii");
            artwork.setImageResource(R.drawable.artwork02);
        }
        if (currentIndex == 3){
            songTitle.setText("Sample Song 003");
            songArtist.setText("maoudamashii");
            artwork.setImageResource(R.drawable.artwork03);
        }
        if (currentIndex == 4){
            songTitle.setText("Song for the Forth");
            songArtist.setText("maoudamashii");
            artwork.setImageResource(R.drawable.artwork04);
        }
    }

    private void PlaySong(){
        //Show pause button
        btPlay.setImageResource(R.drawable.btn_pause);
        //Start media player
        mediaPlayer.start();
        //Set max on seek bar
        seekBar.setMax(mediaPlayer.getDuration());
        //Start Handler
        handler.postDelayed(runnable,0);
        SongNames();
    }

    private void PauseSong(){
        //Show play button
        btPlay.setImageResource(R.drawable.btn_play);
        //Pause media player
        mediaPlayer.pause();
        //stop hander
        handler.removeCallbacks(runnable);
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return  String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}