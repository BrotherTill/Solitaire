package com.tjirm.solitare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.tjirm.solitare.screens.GameScreen;

import java.util.Optional;

public class Solitare extends Game {
	public static Skin sprites;
	public static Skin UI;
	public static Options options;
	
	@Override
	public void create () {
		sprites = new Skin(Gdx.files.internal("packed/sprites.json"));
		UI = new Skin(Gdx.files.internal("packed/UI.json"));
		options = readOptions().orElseGet(Options::getDefaultOptions);
		updateScreenSize(0);
		options.addScreenListener(this::updateScreenSize);
		
		setScreen(new GameScreen());
	}
	
	public void updateScreenSize(float newSize) {
		if(!Gdx.graphics.setWindowedMode(options.getScreenWidth(), options.getScreenHeight()))
			Gdx.app.log(getClass().getSimpleName(), "couldn't resize Window");
	}
	
	private Optional<Options> readOptions() {
		Options options = null;
		try {
			Json json = new Json();
			options = json.fromJson(Options.class, Gdx.files.local("data/options.yaml"));
		} catch(SerializationException e) {
			Throwable cause = e.getCause();
			if(cause.getMessage().startsWith("Error parsing file") || cause instanceof NullPointerException)
				Gdx.app.log("initialization", "error parsing Options json reverting to default");
			else if(cause.getCause().getMessage().startsWith("File not found"))
				Gdx.app.log("initialization", "couldn't find Options file reverting to default");
			else
				Gdx.app.log("initialization", "unexpected error reading Options reverting to default");
		}
		return Optional.ofNullable(options);
	}
	
	private void saveOptions() {
		Json json = new Json();
		Gdx.files.local("data/options.yaml").writeString(json.prettyPrint(options), false);
	}
	
	@Override
	public void dispose () {
		saveOptions();
	}
}