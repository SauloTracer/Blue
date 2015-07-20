package com.example.pong;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

public class GESoundPool {
	private static final int MAX_SOUNDS = 16;
	private Context mContext = null;
	private SoundPool mSoundPool = new SoundPool(MAX_SOUNDS,
			AudioManager.STREAM_MUSIC, 0);

	public GESoundPool(Context context) {
		mContext = context;
	}

	public int loadSound(int resourceId) {
		return mSoundPool.load(mContext, resourceId, 1);
	}

	public int loadSound(String filename) {
		try {
			AssetFileDescriptor descriptor;
			descriptor = mContext.getAssets().openFd(filename);
			return mSoundPool.load(descriptor, 1);
		} catch (IOException e) {
			return -1;
		}
	}

	public void playSound(int soundId, float volumeLeft, float volumeRight,
			int loop, float rate) {
		mSoundPool.play(soundId, volumeLeft, volumeRight, 0, loop, rate);
	}

	public void unloadSound(int soundId) {
		mSoundPool.unload(soundId);
	}

}
