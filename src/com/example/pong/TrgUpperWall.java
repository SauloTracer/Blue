package com.example.pong;

import android.graphics.PointF;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEMundo;
import com.examplo.game_engine.GETrigger;

public class TrgUpperWall extends GETrigger {
    public TrgUpperWall(GEMundo mundo, PointF position, PointF dimensions) 
    {
           super(mundo, GameModel.TRG_UPPER_WALL_ID, position, dimensions);
    }

    @Override
    public void onHit(GEEntidade entidade, float elapsedTimeInSeconds) 
    {
         EntBall ball = (EntBall) entidade;
         PointF ballVelocity = ball.getVelocity();
         ball.setPosition(ball.getPosition().x, 0);
         ball.setVelocity(ballVelocity.x, -ballVelocity.y);
         ball.setHasCollided(true);
     	 ball.setCollisionState(EntBall.COLLISION_EDGE);

   }
}
