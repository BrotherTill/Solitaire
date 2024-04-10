package com.tjirm.solitaire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
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
import com.tjirm.solitaire.preferences.Preferences;

import java.time.Duration;
import java.time.Instant;

public class GameScreen implements Screen {
    Stage stage;
    CardHolderLinker cardHolders;
    
    private final CardStack[] fannedPiles = new CardStack[7];
    private final CardStack[] foundations = new CardStack[4];
    private final CardStack waste;
    private final CardDeck stock;
    
    private Instant start;
    private final Stack gameOverPopup;
    private final Label finishTime;
    private final ImageTextButton restart;
    
    private final SolitaireWinDetector winDetector;
    private final SolitaireGameGenerator gameGenerator;
    
    public GameScreen() {
        stage = new Stage(new ExtendViewport(Solitaire.preferences.getScreenWidth(), Solitaire.preferences.getScreenHeight()));
        cardHolders = new CardHolderLinker(stage, true);
        
        for(int i = 0; i < fannedPiles.length; i++) {
            stage.addActor(cardHolders.linkCardHolder(fannedPiles[i] = new CardStack(RevealedCards.custom, 0, -30, DraggableCards.revealed, (a,b) -> true)));
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
        
        stage.addActor(winDetector = new SolitaireWinDetector(fannedPiles, foundations, waste, stock));
        winDetector.setOnGameWon(this::gameWon);
        gameGenerator = new SolitaireGameGenerator(fannedPiles, foundations, waste, stock);
        
        
        stage.addActor(restart = new ImageTextButton("Restart", Solitaire.UI));
        restart.clearChildren();
        restart.stack(restart.getImage(), restart.getLabel());
        restart.setWidth(120);
        restart.setPosition(20,20);
        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameGenerator.reset();
            }
        });
        
        stage.addActor(gameOverPopup  = new Stack());
        gameOverPopup.addActor(new Image(Solitaire.UI.getDrawable("GameOver_Popup")));
        gameOverPopup.setSize(250, 170);
        gameOverPopup.setPosition(stage.getWidth() / 2, -50 , Align.top);
        VerticalGroup verticalGroup = new VerticalGroup();
        gameOverPopup.add(new Container<>(verticalGroup).center());
        verticalGroup.addActor(new Container<>(new Label("You Win!", Solitaire.UI, "head")).pad(20));
        finishTime = new Label("", Solitaire.UI, "time");
        verticalGroup.addActor(new Container<>(finishTime).padBottom(30));
        verticalGroup.getChild(1).setHeight(verticalGroup.getChild(1).getHeight() + 30);
        ImageTextButton pain = new ImageTextButton("Restart", Solitaire.UI);
        pain.clearChildren();
        pain.stack(pain.getImage(), pain.getLabel());
        verticalGroup.addActor(pain);
        pain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MoveToAction moveToAction = new MoveToAction();
                moveToAction.setPosition(stage.getWidth() / 2, -50 , Align.top);
                moveToAction.setDuration(Preferences.MOVE_TO_DURATION);
                gameOverPopup.addAction(moveToAction);
                restart.addAction(Actions.moveTo(20, 20, Preferences.MOVE_TO_DURATION));
                gameGenerator.reset();
            }
        });
        
        gameGenerator.reset();
        start = Instant.now();
    }
    
    private void gameWon() {
        Duration duration = Duration.between(start, Instant.now());
        finishTime.setText((duration.getSeconds() / 60) + ":" + (duration.getSeconds() % 60));
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        moveToAction.setDuration(Preferences.MOVE_TO_DURATION);
        gameOverPopup.addAction(moveToAction);
        restart.addAction(Actions.moveTo(20, -50, Preferences.MOVE_TO_DURATION));
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
        
        if(     Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) &&
                Gdx.input.isKeyPressed(Input.Keys.NUM_1) &&
                Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            gameWon();
        }
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