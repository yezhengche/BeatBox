package com.kaixuan.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixuan on 2017/2/20.
 */

public class BeatBox {
    private final static String TAG = "BeatBox";
    private final static String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssetManager;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAssetManager = context.getAssets();
        AudioAttributes arributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(arributes)
                .setMaxStreams(MAX_SOUNDS)
                .build();

        loadSounds();
    }

    private void loadSounds(){
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            for (String fileName: soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.e(TAG, "Could not list assets", e);
        }

    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    private void load(Sound sound) throws IOException{
        AssetFileDescriptor assetFileDescriptor = mAssetManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(assetFileDescriptor, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId != null) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void release() {
        mSoundPool.release();
    }
}
