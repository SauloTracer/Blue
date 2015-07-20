package com.example.pong;

import android.graphics.PointF;

import com.examplo.game_engine.GEEntidade;
import com.examplo.game_engine.GEMundo;

public class EntPaddle extends GEEntidade {
 	public EntPaddle  (GEMundo mundo, int id, PointF position, PointF dimensions){
		super(mundo, id, "paddle", position, dimensions);
 	}
}
