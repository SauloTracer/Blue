package com.example.pong;

import android.graphics.PointF;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEMundo;
import com.examplo.game_engine.GETrigger;

public class TrgGap extends GETrigger
{
	public static final int GAP_SIZE = 50;

	public TrgGap(GEMundo mundo, PointF position, PointF dimensions) {
		super(mundo, GameModel.TRG_GAP_ID, position, dimensions);
	}

	@Override
	public void onHit(GEEntidade entity, float elapsedTimeInSeconds) {
//		entity.setPosition(entity.getPosition().x, GAP_SIZE);
//		if (entity.getId() == GameModel.OPPONENT_ID) {
//			EntOpponent opponent = (EntOpponent) entity;
//			opponent.setSpeed(-opponent.getSpeed());
//		}
	}
}
