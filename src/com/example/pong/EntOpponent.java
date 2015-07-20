package com.example.pong;

import android.graphics.PointF;

import com.examplo.game_engine.GEMundo;

public class EntOpponent extends EntPaddle {

	private float mSpeed;

	public EntOpponent(GEMundo mundo, PointF position, PointF dimensions) {
		super(mundo, GameModel.OPPONENT_ID, position, dimensions);
		mSpeed = 60.0f;
	}

	@Override
	public void step(float elapsedTimeInSeconds) {
		move(0, mSpeed * elapsedTimeInSeconds);
		/*PointF position = getPosition();
		PointF dimensions = getDimensions();
		GEMundo mundo = getMundo();

		if (position.y < 0) {
			setPosition(getPosition().x, 0);
			mSpeed = -mSpeed;
		} else if (position.y + dimensions.y >= mundo.getDimensions().y) {
			position.y = mundo.getDimensions().y - dimensions.y;
			mSpeed = -mSpeed;
		}*/
	}

	public float getSpeed() {
		return mSpeed;
	}

	public void setSpeed(float speed) {
		mSpeed = speed;
	}
}