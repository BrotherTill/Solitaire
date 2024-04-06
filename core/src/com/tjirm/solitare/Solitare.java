package com.tjirm.solitare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tjirm.solitare.screens.GameScreen;

public class Solitare extends Game {
	public static Skin sprites;
	public static Skin UI;
	
	@Override
	public void create () {
		sprites = new Skin(Gdx.files.internal("packed/sprites.json"));
		UI = new Skin(Gdx.files.internal("packed/UI.json"));
		
		setScreen(new GameScreen());
	}
	@Override
	public void dispose () {
	}
}