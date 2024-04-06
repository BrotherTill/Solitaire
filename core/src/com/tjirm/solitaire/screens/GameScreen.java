package com.tjirm.solitaire.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tjirm.solitaire.cards.Card;

public class GameScreen implements Screen {
    Stage stage;
    
    public GameScreen() {
        stage = new Stage(new ExtendViewport(800, 600));
        
        Card card = new Card();
        card.setPosition(50, 50);
        stage.addActor(card);
    }
    
    @Override
    public void show() {
    
    }
    
    @Override
    public void render(float delta) {
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
    
    @Override
    public void pause() {
    
    }
    
    @Override
    public void resume() {
    
    }
    
    @Override
    public void hide() {
    
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
}