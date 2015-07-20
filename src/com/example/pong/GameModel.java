package com.example.pong;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEMundo;
import com.examplo.game_engine.GETimer;

public class GameModel extends GEMundo {
	public static final int BALL_ID = 0;
	public static final int OPPONENT_ID = 1;
	public static final int PLAYER_ID = 2;
	public static final int BALL_SIZE = 16;
	public static final int DISTANCE_FROM_EDGE = 16;
	public static final int PADDLE_HEIGHT = 85;
	public static final int PADDLE_WIDTH = 64;

	public static final int STATE_RESTART = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_GAME_OVER = 2;
	public static final int STATE_GOAL = 3;
	public static final int STATE_PAUSED = 4;

	private int mCurrentState;
	private int mOpponentScore;
	private int mPlayerScore;
	private StringBuilder mStringBuilder = new StringBuilder();
	private Random mRandom = new Random();

	public final static int PADDLE_BBOX_PADDING = 3;

	private EntBall mBall;
	private EntPlayer mPlayer;

	public static final int TRG_GAP_ID = 3;
	public static final int TRG_LEFT_GOAL_ID = 4;
	public static final int TRG_LOWER_WALL_ID = 5;
	public static final int TRG_RIGHT_GOAL_ID = 6;
	public static final int TRG_UPPER_WALL_ID = 7;

	public final static int GOAL_WIDTH = 64;
	public final static int WALL_HEIGHT = 64;

	private TrgLeftGoal mLeftGoal;
	private TrgLowerWall mLowerWall;
	private TrgRightGoal mRightGoal;
	private TrgUpperWall mUpperWall;
	private GETimer mGoalTimer;
	private GETimer mRestartTimer;
	private ArrayList<GEEntidade> mEntities = new ArrayList<GEEntidade>();

	public GameModel(Point worldDimensions) {
		super(worldDimensions);
	}

	public void setup() {
		mCurrentState = STATE_RESTART;
		Point worldDimensions = getDimensions();
		// Bola
		PointF tempPosition = new PointF((worldDimensions.x / 2) - (BALL_SIZE / 2),
				(worldDimensions.y / 2) - (BALL_SIZE / 2));
		PointF tempDimensions = new PointF(BALL_SIZE, BALL_SIZE);
		mBall = new EntBall(this, tempPosition, tempDimensions);
		mEntities.add(mBall);
		
		// Player
		tempPosition.set(DISTANCE_FROM_EDGE, (worldDimensions.y / 2) - (PADDLE_HEIGHT / 2));
		tempDimensions.set(PADDLE_WIDTH, PADDLE_HEIGHT);
		mPlayer = new EntPlayer(this, tempPosition, tempDimensions);
		RectF bboxPadding = new RectF(0, 0, PADDLE_BBOX_PADDING, PADDLE_BBOX_PADDING);
		mPlayer.setBBoxPadding(bboxPadding);
		mEntities.add(mPlayer);

		// Meta do jogador
		tempPosition.set(-GOAL_WIDTH, 0);
		tempDimensions.set(GOAL_WIDTH - BALL_SIZE, worldDimensions.y);
		mLeftGoal = new TrgLeftGoal(this, tempPosition, tempDimensions);
		mLeftGoal.addObservedEntity(mBall);
		mEntities.add(mLeftGoal);

		// Meta do oponente
		tempPosition.set(worldDimensions.x + BALL_SIZE, 0);
		tempDimensions.set(GOAL_WIDTH - BALL_SIZE, worldDimensions.y);
		mRightGoal = new TrgRightGoal(this, tempPosition, tempDimensions);
		mRightGoal.addObservedEntity(mBall);
		mEntities.add(mRightGoal);

		// Parede inferior
		tempPosition.set(0, worldDimensions.y);
		tempDimensions.set(worldDimensions.x, WALL_HEIGHT);
		mLowerWall = new TrgLowerWall(this, tempPosition, tempDimensions);
		mLowerWall.addObservedEntity(mBall);
		mLowerWall.addObservedEntity(mPlayer);
		mEntities.add(mLowerWall);

		// Parede superior
		tempPosition.set(0, -WALL_HEIGHT);
		tempDimensions.set(worldDimensions.x, WALL_HEIGHT);
		mUpperWall = new TrgUpperWall(this, tempPosition, tempDimensions);
		mUpperWall.addObservedEntity(mBall);
		mEntities.add(mUpperWall);

		// Timer de reinício
		mRestartTimer = new GETimer(0.8f);

		// Timer de marcação de ponto
		mGoalTimer = new GETimer(1.2f);

	}

	public void movePlayer(float x, float y) {
		mPlayer.move(x, y);
		PointF playerPosition = mPlayer.getPosition();
		PointF playerDimensions = mPlayer.getDimensions();
		Point worldDimensions = getDimensions();
		RectF tempBoundingBox = mPlayer.getBoundingBox();
		if (playerPosition.y < 0) {
			mPlayer.setPosition(playerPosition.x, 0);
		} else if (playerPosition.y + playerDimensions.y > worldDimensions.y) {
			mPlayer.setPosition(playerPosition.x, worldDimensions.y - (playerDimensions.y - PADDLE_BBOX_PADDING));
		}
		//@TODO completar validações de posicionamento do player
	}

	@Override
	public void step(float elapsedTimeInSeconds) {
		
		if (elapsedTimeInSeconds > 1.0f) {
			elapsedTimeInSeconds = 0.1f;
		}
		
		if (mCurrentState == STATE_RUNNING) {
			for (GEEntidade currentEntity : mEntities) {
				currentEntity.step(elapsedTimeInSeconds);
			}
		} else if (mCurrentState == STATE_GOAL) {
			if (!mGoalTimer.hasStarted()) {
				mBall.setHasCollided(false);
				mBall.setCollisionState(EntBall.COLLISION_NONE);
				mGoalTimer.start();
			}
			if (mGoalTimer.step(elapsedTimeInSeconds) == true) {
				mGoalTimer.stopAndReset();
				if (collisionTest(mBall.getBoundingBox(), mPlayer.getBoundingBox())) {
					mCurrentState = STATE_GAME_OVER;
				} else {
					mCurrentState = STATE_RESTART;
				}
			}
		} else if (mCurrentState == STATE_RESTART) {
			if (!mRestartTimer.hasStarted()) {
				mRestartTimer.start();
				resetWorld();
			}
			if (mRestartTimer.step(elapsedTimeInSeconds) == true) {
				mRestartTimer.stopAndReset();
				mCurrentState = STATE_RUNNING;
			}
		} else if (mCurrentState == STATE_GAME_OVER) {
		}
	}

	public EntBall getBall() {
		return mBall;
	}

	public EntPlayer getPlayer() {
		return mPlayer;
	}

	public void resetWorld() {
		float halfWorldWidth = getDimensions().x / 2;
		float halfWorldHeight = getDimensions().y / 2;
		float halfBallWidth = mBall.getDimensions().x / 2;
		float halfBallHeight = mBall.getDimensions().y / 2;
		float halfPaddleHeight = mPlayer.getDimensions().y / 2;

		float ballPositionX = halfWorldWidth - halfBallWidth;
		float ballPositionY = halfWorldHeight - halfBallHeight;
		float paddlePositionY = halfWorldHeight - halfPaddleHeight;

		mBall.setPosition(ballPositionX, ballPositionY);
		mPlayer.setPosition(mPlayer.getPosition().x, paddlePositionY);
		if (mRandom.nextInt(2) == 0) {
			mBall.setVelocity(-90.0f, 90.0f);
		} else {
			mBall.setVelocity(90.0f, -90.0f);
		}
	}

	public int getCurrentState() {
		return mCurrentState;
	}

	public int getOpponentScore() {
		return mOpponentScore;
	}

	public int getPlayerScore() {
		return mPlayerScore;
	}

	public void setCurrentState(int state) {
		mCurrentState = state;
	}

	public void increaseOpponentScore() {
		mOpponentScore++;
	}

	public void increasePlayerScore() {
		mPlayerScore++;
	}

	public ArrayList<GEEntidade> getEntities() {
		return mEntities;
	}

}
