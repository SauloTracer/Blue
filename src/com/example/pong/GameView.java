package com.example.pong;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEImage;
import com.examplo.game_engine.GEImageFactory;
import com.examplo.game_engine.GERenderer;
import com.examplo.game_engine.GETileset;
import com.examplo.game_engine.GEView;
import com.examplo.game_engine.GEAnimacao;

public class GameView extends GEView {

	Point paint = new Point();

	private final static int TAMANHO_BOLA = 16;
	private final static int DISTANCE_FROM_EDGE = 16;
	private final static int LARGURA_PADDLE = 23;
	private final static int ALTURA_PADDLE = 98;

	private RectF mPosicaoBola = new RectF();
	private RectF mPosicaoOponente = new RectF();
	private RectF mPosicaoPlayer = new RectF();

	private Paint mTempPaint = new Paint();

	private final static int VELOCIDADE_BOLA = 120;
	private final static int VELOCIDADE_OPONENTE = 120;

	private boolean mBallMoveRight = true;

	private GEAnimacao mAnimBall;
	private GEAnimacao mAnimOpponent;
	private GEAnimacao mAnimPlayer;

	/*
	 * private GEImage mBallImage; private GEImage mOpponentImage; private
	 * GEImage mPlayerImage;
	 */

	private GETileset mTsetBall;
	private GETileset mTsetOpponent;
	private GETileset mTsetPlayer;

	private Rect mTempImageSource = new Rect();

	private GameModel mModel;
	private Rect mTempSrcRect = new Rect();

	public final static int SFX_COLLISION_EDGE = 0;
	public final static int SFX_COLLISION_PADDLE = 1;
	private GEMusicPlayer mMusicPlayer;
	private GESoundPool mSoundPool;
	private int mSounds[] = new int[2];

	public GameView(Context context) {
		super(context);
	}

	public GameView(Context context, GameModel model) {
		super(context);
		mModel = model;
		mSoundPool = new GESoundPool(context);
		mMusicPlayer = new GEMusicPlayer(context);

	}

	@Override
	protected void setup() {
		mModel.setup();
		GEImageFactory imageFactory = getImageFactory();

		// Descrição da bola
		GEImage ballImage = imageFactory.createImage("ball.png");
		mTsetBall = new GETileset(ballImage, new Point(4, 2), null);
		int[] ballTiles = { 0, 1, 2, 3 };
		mAnimBall = new GEAnimacao(ballTiles, 0.1f);

		// Paddle do oponente
		GEImage opponentImage = imageFactory.createImage("opponent.png");
		mTsetOpponent = new GETileset(opponentImage, new Point(8, 2),
				new Rect(0, 0, GameModel.PADDLE_WIDTH, GameModel.PADDLE_HEIGHT));
		int[] oponentTiles = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		mAnimOpponent = new GEAnimacao(oponentTiles, 0.1f);

		// Paddle do jogador
		GEImage playerImage = imageFactory.createImage("blue.png");
		mTsetPlayer = new GETileset(playerImage, new Point(4, 3),
				new Rect(0, 0, GameModel.PADDLE_WIDTH, GameModel.PADDLE_HEIGHT));
		int[] playerTiles = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		mAnimPlayer = new GEAnimacao(playerTiles, 0.1f);

		// Efeitos sonoros
		mSounds[SFX_COLLISION_EDGE] = mSoundPool.loadSound("collision_edge.wav");
		mSounds[SFX_COLLISION_PADDLE] = mSoundPool.loadSound("collision_paddle.wav");

		// Música
		mMusicPlayer.loadMusic("bgm.ogg");
		mMusicPlayer.play(true, 1.0f, 1.0f);

	}

	@Override
	public void step(Canvas canvas, float intervaloTempo) {
		mModel.step(intervaloTempo);

		GERenderer renderer = getRenderer();
		renderer.beginDrawing(canvas, Color.BLACK);
		if (mModel.getCurrentState() == GameModel.STATE_RUNNING) {
			switch (mModel.getBall().getCollisionState()) {
			case EntBall.COLLISION_EDGE:
				mSoundPool.playSound(mSounds[SFX_COLLISION_EDGE], 1, 1, 0, 1);
				break;
			case EntBall.COLLISION_PADDLE:
				mSoundPool.playSound(mSounds[SFX_COLLISION_PADDLE], 1, 1, 0, 1);
				break;
			}
		}

		ArrayList<GEEntidade> entities = mModel.getEntities();
		for (GEEntidade currentEntity : entities) {
			if (currentEntity.getCategory() != "trigger") {
				GETileset tileset;
				PointF position = currentEntity.getPosition();
				PointF dimensions = currentEntity.getDimensions();

				if (currentEntity.getId() == GameModel.PLAYER_ID) {
					tileset = mTsetPlayer;
					int tileIndex;
					GEAnimacao animacaoAtual = mAnimPlayer;
					if (mModel.getCurrentState() == GameModel.STATE_RUNNING) {
						animacaoAtual.start(-1);
						tileIndex = animacaoAtual.step(intervaloTempo);
					} else {
						tileIndex = animacaoAtual.getCurrentTile();
					}
					Rect drawingArea = mTsetPlayer.getTile(tileIndex);
					renderer.drawImage(mTsetPlayer.getImage(), drawingArea, position, dimensions);
				} else if (currentEntity.getId() == GameModel.OPPONENT_ID) {
					tileset = mTsetOpponent;
					int tileIndex;
					GEAnimacao animacaoAtual = mAnimOpponent;
					if (mModel.getCurrentState() == GameModel.STATE_RUNNING) {
						animacaoAtual.start(-1);
						tileIndex = animacaoAtual.step(intervaloTempo);
					} else {
						tileIndex = animacaoAtual.getCurrentTile();
					}
					Rect drawingArea = mTsetOpponent.getTile(tileIndex);
					renderer.drawImage(mTsetOpponent.getImage(), drawingArea, position, dimensions);
				} else {// (currentEntity.getId() == GameModel.BALL_ID)
					tileset = mTsetBall;
					int tileIndex;
					GEAnimacao animacaoAtual = mAnimBall;
					if (mModel.getCurrentState() == GameModel.STATE_RUNNING) {
						animacaoAtual.start(-1);
						tileIndex = animacaoAtual.step(intervaloTempo);
					} else {
						tileIndex = animacaoAtual.getCurrentTile();
					}
					Rect drawingArea = mTsetBall.getTile(tileIndex);
					renderer.drawImage(mTsetBall.getImage(), drawingArea, position, dimensions);
				}
				// PointF position = currentEntity.getPosition();
				// PointF dimensions = currentEntity.getDimensions();
				// Rect drawingArea = tileset.getTile(0);
				// renderer.drawImage(tileset.getImage(), drawingArea, position,
				// dimensions);
			}
		}
		/*
		 * mTempSrcRect.set(0, 0, GameModel.PADDLE_WIDTH,
		 * GameModel.PADDLE_HEIGHT); GEEntidade opponent = mModel.getOpponent();
		 * renderer.drawImage(mImagemOponente, mTempSrcRect,
		 * opponent.getPosition(), opponent.getDimensions()); GEEntidade player
		 * = mModel.getPlayer(); renderer.drawImage(mImagemPlayer, mTempSrcRect,
		 * player.getPosition(), player.getDimensions()); GEEntidade ball =
		 * mModel.getBall(); mTempSrcRect.set(0, 0, GameModel.BALL_SIZE,
		 * GameModel.BALL_SIZE); renderer.drawImage(mImagemBola, mTempSrcRect,
		 * ball.getPosition(),ball.getDimensions());
		 */
		/*
		 * for (GEEntidade currentEntity : entities) { if
		 * (currentEntity.getCategory() != "trigger") { if
		 * (currentEntity.getId() == GameModel.PLAYER_ID) { mTempSrcRect.set(0,
		 * 0, GameModel.PADDLE_WIDTH, GameModel.PADDLE_HEIGHT);
		 * renderer.drawImage(mImagemPlayer, mTempSrcRect,
		 * currentEntity.getPosition(), currentEntity.getDimensions()); } else
		 * if (currentEntity.getId() == GameModel.OPPONENT_ID) {
		 * mTempSrcRect.set(0, 0, GameModel.PADDLE_WIDTH,
		 * GameModel.PADDLE_HEIGHT); renderer.drawImage(mImagemOponente,
		 * mTempSrcRect, currentEntity.getPosition(),
		 * currentEntity.getDimensions()); } else { // (currentEntity.getId() ==
		 * GameModel.BALL_ID) mTempSrcRect.set(0, 0, GameModel.BALL_SIZE,
		 * GameModel.BALL_SIZE); renderer.drawImage(mImagemBola, mTempSrcRect,
		 * currentEntity.getPosition(), currentEntity.getDimensions()); } } }
		 */

		renderer.endDrawing();
		// moveBall(intervaloTempo);
		// moveOpponent (intervaloTempo);]
		/*
		 * GERenderer renderer = getRenderer(); renderer.beginDrawing(canvas,
		 * Color.BLACK); mTempImageSource.set(0, 0, TAMANHO_BOLA, TAMANHO_BOLA);
		 * renderer.drawImage(mImagemBola, mTempImageSource, mPosicaoBola);
		 * mTempImageSource.set(0, 0, LARGURA_PADDLE, ALTURA_PADDLE);
		 * renderer.drawImage(mImagemPlayer, mTempImageSource, mPosicaoPlayer);
		 * mTempImageSource.set(0, 0, LARGURA_PADDLE, ALTURA_PADDLE);
		 * renderer.drawImage(mImagemOponente, mTempImageSource,
		 * mPosicaoOponente); renderer.endDrawing();
		 */

		/*
		 * moveBall (intervaloTempo); //moveOpponent (intervaloTempo);
		 * mTempPaint.setColor(Color.RED);
		 * mTempImageSource.set(0,0,LARGURA_PADDLE, ALTURA_PADDLE);
		 * canvas.drawBitmap(mImagemPlayer.getBitmap(), mTempImageSource,
		 * mPosicaoPlayer, mTempPaint);
		 * canvas.drawBitmap(mImagemOponente.getBitmap(), mTempImageSource,
		 * mPosicaoOponente, mTempPaint);
		 * canvas.drawBitmap(mImagemBola.getBitmap(), mTempImageSource,
		 * mPosicaoBola, mTempPaint);
		 */
	}

	public void moveBall(float intervaloTempo) {
		Point viewDimensions = getDimensions();
		if (mBallMoveRight == true) {
			mPosicaoBola.left += VELOCIDADE_BOLA * intervaloTempo;
			mPosicaoBola.right += VELOCIDADE_BOLA * intervaloTempo;
			if (mPosicaoBola.right >= viewDimensions.x) {
				mPosicaoBola.left = viewDimensions.x - TAMANHO_BOLA;
				mPosicaoBola.right = viewDimensions.x;
				mBallMoveRight = false;
			}
		} else {
			mPosicaoBola.left -= VELOCIDADE_BOLA * intervaloTempo;
			mPosicaoBola.right -= VELOCIDADE_BOLA * intervaloTempo;
			if (mPosicaoBola.right < 0) {
				mPosicaoBola.left = 0;
				mPosicaoBola.right = TAMANHO_BOLA;
				mBallMoveRight = true;
			}
		}
	}

	public void movePlayer(float x, float y) {
		Point viewDimension = getDimensions();
		mPosicaoPlayer.top += y;
		mPosicaoPlayer.bottom += y;
		if (mPosicaoPlayer.top < 0) {
			mPosicaoPlayer.top = 0;
			mPosicaoPlayer.bottom = ALTURA_PADDLE;
		} else if (mPosicaoPlayer.bottom > viewDimension.y) {
			mPosicaoPlayer.top = viewDimension.y - ALTURA_PADDLE;
			mPosicaoPlayer.top = viewDimension.y;
		}
	}

	public GEMusicPlayer getMusicPlayer() {
		return mMusicPlayer;
	}

}
