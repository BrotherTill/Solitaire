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
import com.tjirm.solitaire.cards.dragndrop.CardHolderLinker;
import com.tjirm.solitaire.cards.CardType;

import static com.tjirm.solitaire.cards.CardType.Suit.*;
import static com.tjirm.solitaire.cards.CardType.CardFace.*;

public class GameScreen implements Screen {
    Stage stage;
    CardHolderLinker cardHolders;
    
    public GameScreen() {
        stage = new Stage(new ExtendViewport(800, 600));
        cardHolders = new CardHolderLinker(stage, true);
        stage.addActor(cardHolders.linkCardHolder(new CardStack(RevealedCards.all, 0, -30)));
        cardHolders.getCardHolder(0).setPosition(10, 200);
        cardHolders.getCardHolder(0).addCard(new Card(new CardType(hearts, n10)));
        stage.addActor(cardHolders.linkCardHolder(new CardStack(RevealedCards.all, 0, -30)));
        cardHolders.getCardHolder(1).setPosition(210, 200);
        cardHolders.getCardHolder(1).addCard(new Card(new CardType(spades, jack)));
        cardHolders.getCardHolder(1).addCard(new Card(new CardType(spades, n9)));
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        stage.act(delta);
        ScreenUtils.clear(Color.WHITE);
        stage.draw();
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            cardHolders.getCardHolder(0).addCard(new Card());
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE))
            cardHolders.getCardHolder(0).removeTopCard();
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
        Gdx.input.setInputProcessor(null);
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
}