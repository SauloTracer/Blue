package com.example.pong;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEMundo;

import android.graphics.PointF;

public class EntBall extends GEEntidade {
	// private float mSpeed;
	public PointF mVelocity = new PointF();

	public static final int COLLISION_NONE = 0;
	public static final int COLLISION_EDGE = 1;
	public static final int COLLISION_PADDLE = 2;

	private int mCollisionState;
	private boolean mHasCollided;

	public EntBall(GEMundo mundo, PointF position, PointF dimensions) {
		super(mundo, GameModel.BALL_ID, "ball", position, dimensions);
		mVelocity.set(90.0f, 90.0f);
	}

	@Override
	public void step(float intervaloTempo) {
		move(mVelocity.x * intervaloTempo, mVelocity.y * intervaloTempo);
		GameModel world = (GameModel) getMundo();
		PointF position = getPosition();
		PointF dimensions = getDimensions();
		EntPlayer player = world.getPlayer();
		if (world.collisionTest(getBoundingBox(), player.getBoundingBox())) {
			setPosition(player.getBoundingBox().right, position.y);
			mVelocity.x = -mVelocity.x;
			mHasCollided = true;
			mCollisionState = COLLISION_PADDLE;
		}
		if (mHasCollided) {
			mHasCollided = false;
		} else {
			mCollisionState = COLLISION_NONE;
		}
	}

	public PointF getVelocity() {
		return mVelocity;
	}

	public void setVelocity(float speedX, float speedY) {
		mVelocity.set(speedX, speedY);
	}

	public int getCollisionState() {
		return mCollisionState;
	}

	public boolean hasCollided() {
		return mHasCollided;
	}

	public void setCollisionState(int collisionState) {
		mCollisionState = collisionState;
	}

	public void setHasCollided(boolean hasCollided) {
		mHasCollided = hasCollided;
	}
}