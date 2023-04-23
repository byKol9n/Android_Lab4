package ru.mirea.korolev.player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import ru.mirea.korolev.player.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Handler handler = new Handler();
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton playButton = binding.play;
        ImageButton pauseButton = binding.pause;
        ImageButton UpButton = binding.imageButtonUp;
        ImageButton DownButton = binding.imageButtonDown;
        final SeekBar bar = binding.seekBar;
        mediaPlayer = MediaPlayer.create(this, R.raw.tyagi);
        bar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        mediaPlayer.pause();
        binding.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer.start();
                    startPlayProgressUpdater(bar, playButton);
                } catch (IllegalStateException e) {
                    mediaPlayer.pause();
                }
            }
        });
        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer.pause();
                    startPlayProgressUpdater(bar, playButton);
                } catch (IllegalStateException e) {
                    mediaPlayer.start();
                }
            }
        });
    }

    private void seekChange(View v) {
        if (mediaPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) v;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

    public void startPlayProgressUpdater(SeekBar bar, ImageButton playButton) {
        bar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater(bar, playButton);
                }
            };
            handler.postDelayed(notification, 1000);
        } else {
            mediaPlayer.pause();
            bar.setProgress(0);
        }
    }
}