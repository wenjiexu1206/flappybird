package com.flappybird.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class AudioManager {
    private MediaPlayer backgroundPlayer;
    private boolean soundEffectsEnabled = true;
    private boolean backgroundMusicEnabled = true;
    private boolean gameInProgress = false;
    private String[] backgroundTracks = {"background1.mp3","background2.mp3"}; // 添加更多背景音乐
    private int currentTrackIndex = 0;

    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public boolean isBackgroundMusicEnabled() {
        return backgroundMusicEnabled;
    }

    public void toggleSoundEffects(boolean enable) {
        soundEffectsEnabled = enable;
        // 实现对音效的控制
    }

    public void toggleBackgroundMusic(boolean enable) {
        backgroundMusicEnabled = enable;
        if (backgroundPlayer != null) {
            if (enable && gameInProgress) {
                playBackgroundMusic();
            } else {
                backgroundPlayer.pause();
            }
        }
    }

    public void playSoundEffect(String fileName) {
        if (!soundEffectsEnabled) {
            return;
        }
        Media sound = new Media(new File("resources/sounds/" + fileName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void playBackgroundMusic() {
        try {
            if (backgroundPlayer != null) {
                backgroundPlayer.stop();
                backgroundPlayer.dispose();  // 释放旧的播放器资源
            }
            String path = getClass().getResource("/sounds/" + backgroundTracks[currentTrackIndex]).toURI().toString();
            Media sound = new Media(path);
            backgroundPlayer = new MediaPlayer(sound);
            backgroundPlayer.setOnEndOfMedia(this::nextBackgroundTrack);
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            if (backgroundMusicEnabled) {
                backgroundPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();  // 更好的错误处理
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.pause();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }
    }

    public void nextBackgroundTrack() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }
        currentTrackIndex = (currentTrackIndex + 1) % backgroundTracks.length;
        playBackgroundMusic();
    }

}