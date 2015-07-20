package com.example.pong;

import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;

import com.examplo.game_engine.GEActivity;
import com.examplo.game_engine.GEInputAssinante;
import com.examplo.game_engine.GEInputPublicador;
import com.examplo.game_engine.GEPreferences;

public class GameActivity extends GEActivity implements GEInputAssinante {

	private GameView mView;

	private GameController mController;

	private GameModel mModel;

	private GEInputPublicador mInputPublicador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enableFullScreen();
		enableKeepScreenOn();
		// setContentView(R.layout.activity_game);
		GEPreferences preferences = getPreferences();
		if (preferences.getInt("first_time", -1) == -1) {
			preferences.begin().putInt("first_time", 1).putInt("high_score", 4)
					.end();
		}

		// mView = new GameView(this);
		// setContentView(mView);

		Point worldDimensions = new Point(800, 480);
		mModel = new GameModel(worldDimensions);
		mView = new GameView(this, mModel);
		setContentView(mView);
		/*
		 * mInputPublicador = new GEInputPublicador(this);
		 * mInputPublicador.registerAssinante(this);
		 * setInputPublicador(mInputPublicador);
		 */
		GEInputPublicador inputPublicador = new GEInputPublicador(this);
		mController = new GameController(mModel);
		inputPublicador.registerAssinante(mController);
		setInputPublicador(inputPublicador);

	}

	@Override
	public void onDown(MotionEvent event) {
	}

	@Override
	public void onScroll(MotionEvent downEvent, MotionEvent moveEvent,
			float distanceX, float distanceY) {
		mView.movePlayer(-distanceX, -distanceY);
	}

	@Override
	public void onUp(MotionEvent event) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mView.getMusicPlayer().release();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mView.getMusicPlayer().pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mView.getMusicPlayer().resume();
	}

}
