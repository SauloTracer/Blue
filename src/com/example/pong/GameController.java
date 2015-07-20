package com.example.pong;

import android.view.MotionEvent;

import com.examplo.game_engine.GEInputAssinante;

public class GameController implements GEInputAssinante {
	private GameModel mModel;

	public GameController(GameModel model) {
		mModel = model;
	}

	@Override
	public void onDown(MotionEvent event) {
	}

	@Override
	public void onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
		if (mModel.getCurrentState() == GameModel.STATE_RUNNING) {
			mModel.movePlayer(-distanceX, -distanceY);
		}
	}

	@Override
	public void onUp(MotionEvent event) {
	}

	public GameModel getModel() {
		return mModel;
	}
}
