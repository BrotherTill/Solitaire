package com.tjirm.solitaire;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tjirm.solitaire.preferences.Preferences;
import com.tjirm.solitaire.screens.GameScreen;

public class Solitaire extends Game {
	public static Skin sprites;
	public static Skin UI;
	public static Preferences preferences;
	
	@Override
	public void create () {
		sprites = new Skin(Gdx.files.internal("packed/sprites.json"));
		UI = new Skin(Gdx.files.internal("packed/UI.json"));
		preferences = Preferences.load();
		updateScreenSize(0);
		preferences.getScreenSize().addListener(this::updateScreenSize);
		
		setScreen(new GameScreen());
	}
	
	public void updateScreenSize(float newSize) {
		if(!Gdx.graphics.setWindowedMode(preferences.getScreenWidth(), preferences.getScreenHeight()))
			Gdx.app.error(getClass().getSimpleName(), "couldn't resize Window");
	}
	
	@Override
	public void dispose () {
		Preferences.save(preferences);
	}
}