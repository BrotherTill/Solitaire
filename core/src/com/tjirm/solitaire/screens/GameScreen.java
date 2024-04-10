package com.tjirm.solitaire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.CardDeck;
import com.tjirm.solitaire.cards.CardStack;
import com.tjirm.solitaire.cards.CardStack.DraggableCards;
import com.tjirm.solitaire.cards.CardStack.RevealedCards;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.cards.dragndrop.CardHolderLinker;
import com.tjirm.solitaire.logic.SolitaireGameGenerator;
import com.tjirm.solitaire.logic.SolitaireWinDetector;

public class GameScreen implements Screen {
    Stage stage;
    CardHolderLinker cardHolders;
    
    private final CardStack[] fannedPiles = new CardStack[7];
    private final CardStack[] foundations = new CardStack[4];
    private final CardStack waste;
    private final CardDeck stock;
    
    private final SolitaireWinDetector winDetector;
    private final SolitaireGameGenerator gameGenerator;
    
    public GameScreen() {
        stage = new Stage(new ExtendViewport(Solitaire.preferences.getScreenWidth(), Solitaire.preferences.getScreenHeight()));
        cardHolders = new CardHolderLinker(stage, true);
        
        for(int i = 0; i < fannedPiles.length; i++) {
            stage.addActor(cardHolders.linkCardHolder(fannedPiles[i] = new CardStack(RevealedCards.custom, 0, -30, DraggableCards.revealed, CardHolder.DEFAULT_SOLITAIRE)));
            fannedPiles[i].setPosition(20 + (Solitaire.preferences.getCardWidth() + 20) * i, stage.getHeight() - 40 - Solitaire.preferences.getCardHeight() * 2);
        }
        for(int i = 0; i < foundations.length; i++) {
            stage.addActor(cardHolders.linkCardHolder(foundations[i] = new CardStack(RevealedCards.all, 0, 0, CardHolder.DEFAULT_FOUNDATIONS)));
            foundations[i].setPosition(20 + (Solitaire.preferences.getCardWidth() + 20) * i, stage.getHeight() - 20 - Solitaire.preferences.getCardHeight());
        }
        
        stage.addActor(cardHolders.linkCardHolder(waste = new CardStack(RevealedCards.all, 0, 0, CardDeck.DEFAULT_TARGET)));
        waste.setPosition(20 + (Solitaire.preferences.getCardWidth() + 20) * (foundations.length + 1), stage.getHeight() - 20 - Solitaire.preferences.getCardHeight());
        stage.addActor(cardHolders.linkCardHolder(stock = new CardDeck(RevealedCards.none, 0, 0)));
        stock.setPosition(20 + (Solitaire.preferences.getCardWidth() + 20) * (foundations.length + 2), stage.getHeight() - 20 - Solitaire.preferences.getCardHeight());
        stock.setTarget(waste);
        
        winDetector = new SolitaireWinDetector(fannedPiles, foundations, waste, stock);
        winDetector.setOnGameWon(this::gameWon);
        gameGenerator = new SolitaireGameGenerator(fannedPiles, foundations, waste, stock);
        
        gameGenerator.reset();
    }
    
    private void gameWon() {
        Gdx.app.log(getClass().getSimpleName(), "You Win");
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
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
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