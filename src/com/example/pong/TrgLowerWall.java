package com.example.pong;

import android.graphics.Point;
import android.graphics.PointF;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEMundo;
import com.examplo.game_engine.GETrigger;

public class TrgLowerWall extends GETrigger {
	public TrgLowerWall(GEMundo mundo, PointF position, PointF dimensions) {
		super(mundo, GameModel.TRG_LOWER_WALL_ID, position, dimensions);
	}
	@Override
	public void onHit(GEEntidade entity, float elapsedTimeInSeconds) {
		Point worldDimensions = getMundo().getDimensions();
		if (entity.getId() == GameModel.PLAYER_ID) {
			entity.setPosition(entity.getPosition().x, worldDimensions.y
							- entity.getDimensions().y);
		} else {// (entity.getId() == GameModel.BALL_ID) 
			EntBall ball = (EntBall) entity;
			ball.setPosition(ball.getPosition().x,
			worldDimensions.y - ball.getDimensions().y);
			ball.setVelocity(ball.getVelocity().x, -ball.getVelocity().y);
			ball.setHasCollided(true);
			ball.setCollisionState(EntBall.COLLISION_EDGE);
		}
	}
}
