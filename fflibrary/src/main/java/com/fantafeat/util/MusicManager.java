package com.fantafeat.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.fantafeat.R;


public class MusicManager {

    private static MusicManager sManager;
    public MediaPlayer mPlayer,clockPlayer;
    public MediaPlayer mPlayerGame;
    private int mLength = 0;

    public static MusicManager getInstance() {
        if (sManager == null) {
            sManager = new MusicManager();
        }
        return sManager;
    }

    public void playEventSuccess(Context context){
       /* mPlayer = MediaPlayer.create(context, R.raw.correct);
        mPlayer.setLooping(false);
        mPlayer.setVolume(49,49);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.start();*/
    }

    public void play(Context context) {
       /* mPlayer = MediaPlayer.create(context, R.raw.game_background_music);
        mPlayer.setLooping(true);
        mPlayer.setVolume(49,49);//GameDataHelper.getSoundVolume(), GameDataHelper.getSoundVolume()
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.start();*/
    }
    public void playGame(Context context) {
       /* mPlayerGame = MediaPlayer.create(context, R.raw.game_background_music);
        mPlayerGame.setLooping(true);
        mPlayerGame.setVolume(49,49);//GameDataHelper.getSoundVolume(), GameDataHelper.getSoundVolume()
        mPlayerGame.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayerGame.start();*/
    }
    public void setMusicVolume(float volume) {
        if (mPlayer != null) {
            mPlayer.setVolume(volume, volume);
        }
    }

    /**
     * Pauses Music
     */
    public void pauseMusic() {
        try {
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
                mLength = mPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            // Handle exception here
        }
    }

    public void pauseMusicGame() {
        try {
            if (mPlayerGame != null && mPlayerGame.isPlaying()) {
                mPlayerGame.pause();
                mLength = mPlayerGame.getCurrentPosition();
            }
        } catch (Exception e) {
            // Handle exception here
        }
    }

    /**
     * Resumes Music
     */
    public void resumeMusic(Context context) {
        try {
            if (mPlayer != null && !mPlayer.isPlaying()) {
                mPlayer.seekTo(mLength);
                mPlayer.start();
            }/*else {
                play(context);
            }*/
        } catch (Exception e) {
            // Handle exception here
        }
    }

    public void resumeMusicGame(Context context) {
        try {
            if (mPlayerGame != null && !mPlayerGame.isPlaying()) {
                mPlayerGame.seekTo(mLength);
                mPlayerGame.start();
            }/*else {
                play(context);
            }*/
        } catch (Exception e) {
            // Handle exception here
        }
    }

    public boolean isMusicPlaying(){
        if (mPlayer != null && !mPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    /**
     * Stops Music
     */
    public void stopMusic() {
        try {
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (Exception e) {
            // Handle exception here
        }
    }

    public void stopMusicGame() {
        try {
            if (mPlayerGame != null && mPlayerGame.isPlaying()) {
                mPlayerGame.stop();
                mPlayerGame.release();
                mPlayerGame = null;
            }
        } catch (Exception e) {
            // Handle exception here
        }
    }

    public void stopClockMusic() {
        try {
            if (clockPlayer != null && clockPlayer.isPlaying()) {
                clockPlayer.stop();
                clockPlayer.release();
                clockPlayer = null;
            }
        } catch (Exception e) {
            // Handle exception here
        }
    }
}
