package com.tjirm.solitaire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.Card;
import com.tjirm.solitaire.cards.CardDeck;
import com.tjirm.solitaire.cards.CardStack;
import com.tjirm.solitaire.cards.CardStack.DraggableCards;
import com.tjirm.solitaire.cards.CardStack.RevealedCards;
import com.tjirm.solitaire.cards.CardType;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.cards.dragndrop.CardHolderLinker;
import com.tjirm.solitaire.preferences.Preferences;

public class GameScreen implements Screen {
    Stage stage;
    CardHolderLinker cardHolders;
    
    private final CardStack[] fannedPiles = new CardStack[7];
    private final CardDeck stock;
    private final CardStack waste;
    private final CardStack[] foundations = new CardStack[4];
    
    public GameScreen() {
        stage = new Stage(new ExtendViewport(Solitaire.preferences.getScreenWidth(), Solitaire.preferences.getScreenHeight()));
        cardHolders = new CardHolderLinker(stage, true);
        cardHolders.setOnCardsMove(this::checkIfWon);
        
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
        
        reset();
    }
    
    public void reset() {
        stock.clearCards();
        waste.clearCards();
        for(CardStack fannedPile : fannedPiles) fannedPile.clearCards();
        for(CardStack foundation : foundations) foundation.clearCards();
        
        Array<Card> cards = new Array<>(52);
        for(int i = 0; i < CardType.Suit.values().length - 1; i++)
            for(int j = 0; j < CardType.CardFace.values().length - 1; j++)
                cards.add(new Card(new CardType(CardType.Suit.values()[i], CardType.CardFace.values()[j])));
        cards.shuffle();
        
        for(CardStack cardStack : fannedPiles)
            cardStack.setRevealedCards(RevealedCards.top);
        
        for(int i = 0; i < fannedPiles.length; i++)
            for(int j = 0; j <= i; j++)
                fannedPiles[i].addCard(cards.pop());
        
        for(CardStack cardStack : fannedPiles)
            cardStack.setRevealedCards(RevealedCards.onRemove);
        
        stock.addCards(cards.toArray(Card.class));
    }
    
    private void checkIfWon(CardHolder form, CardHolder to) {
        if(stock.getSize() != 0 || waste.getSize() != 0)
            return;
        for(CardStack cardStack : fannedPiles)
            for(int i = 0; i < cardStack.getSize(); i++)
                if(!cardStack.getCard(i).isRevealed())
                    return;
        
        gameWon();
        moveCardsToFoundations();
    }
    
    private void moveCardsToFoundations() {
        Array<Card>[] foundationsTemp = new Array[foundations.length];
        for(int i = 0; i < foundations.length; i++)
            foundationsTemp[i] = new Array<>(foundations[i].getCards());
        for(int i = 0; moveTopCardsToFoundations(i, foundationsTemp); i++);
        for(CardStack cardStack : foundations)
            cardStack.toBack();
    }
    
    private boolean moveTopCardsToFoundations(int depth, Array<Card>[] foundationsTemp) {
        boolean cardsRemaining = false;
        Array<Card>[] foundationsTempCapture = new Array[foundationsTemp.length];
        for(int i = 0; i < foundationsTemp.length; i++)
            foundationsTempCapture[i] = new Array<>(foundationsTemp[i]);
        for(CardStack fannedPile : fannedPiles)
            if(fannedPile.getTopCard().isPresent()) {
                cardsRemaining = true;
                Card card = fannedPile.getTopCard().get();
                CardStack matchingFoundation = getMatchingCardStack(card.getCardType(), foundationsTempCapture);
                if(matchingFoundation == null)
                    continue;
                Vector2 from = card.localToStageCoordinates(new Vector2());
                Vector2 to = matchingFoundation.localToStageCoordinates(new Vector2());
                matchingFoundation.setDraggable(false);
                fannedPile.removeCard(card);
                stage.addActor(card);
                getMatchingFoundation(card.getCardType(), foundationsTemp).add(card);
                card.toBack();
                card.setPosition(from.x, from.y);
                card.addAction(     Actions.sequence(
                                    Actions.delay(Preferences.MOVE_TO_DURATION * (depth)),
                                    Actions.moveTo(to.x, to.y, Preferences.MOVE_TO_DURATION),
                                    Actions.run(() -> matchingFoundation.addCard(card)))
                );
            }
        return cardsRemaining;
    }
    
    private Array<Card> getMatchingFoundation(CardType cardType, Array<Card>[] foundationsTemp) {
        for(Array<Card> cards : foundationsTemp)
            if(cards.isEmpty() || CardHolder.DEFAULT_FOUNDATIONS.test(cards.get(cards.size - 1).getCardType(), cardType))
                return cards;
        return null;
    }
    private CardStack getMatchingCardStack(CardType cardType, Array<Card>[] foundationsTemp) {
        for(int i = 0; i < foundationsTemp.length; i++)
            if(        foundationsTemp[i].isEmpty() && cardType.getCardFace().isSame(CardType.CardFace.ace) ||
                       !foundationsTemp[i].isEmpty() && CardHolder.DEFAULT_FOUNDATIONS.test(foundationsTemp[i].get(foundationsTemp[i].size - 1).getCardType(), cardType))
                return foundations[i];
        return null;
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