package com.example.pong;

import android.graphics.PointF;

import com.examplo.game_engine.GEMundo;

public class EntPlayer extends EntPaddle {
	
    public EntPlayer (GEMundo mundo, PointF position, PointF dimensions){
    	super(mundo, GameModel.PLAYER_ID, position, dimensions);
    }
    
    @Override
    public void step (float intervaloTempo){
    }
}
