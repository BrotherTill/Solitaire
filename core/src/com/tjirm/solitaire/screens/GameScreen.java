package com.tjirm.solitaire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tjirm.solitaire.cards.Card;
import com.tjirm.solitaire.cards.CardStack;
import com.tjirm.solitaire.cards.CardStack.RevealedCards;

public class GameScreen implements Screen {
    Stage stage;
    CardStack cardStack;
    
    public GameScreen() {
        stage = new Stage(new ExtendViewport(800, 600));
    }
    
    @Override
    public void show() {
        cardStack = new CardStack(RevealedCards.top, 0, -30);
        cardStack.setPosition(10, 200);
        stage.addActor(cardStack);
        cardStack.addCard(new Card());
    }
    
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        stage.draw();
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            cardStack.addCard(new Card());
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE))
            cardStack.removeTopCard();
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