package com.tjirm.solitaire.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.cards.Card;
import com.tjirm.solitaire.cards.CardDeck;
import com.tjirm.solitaire.cards.CardStack;
import com.tjirm.solitaire.cards.CardType;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.cards.dragndrop.CardHolderLinker;
import com.tjirm.solitaire.preferences.Preferences;

import java.util.function.BiConsumer;

public class SolitaireWinDetector extends Group {
    private final CardStack[] fannedPiles;
    private final CardStack[] foundations;
    private final CardHolder waste;
    private final CardDeck stock;
    
    private Runnable onGameWon = null;
    
    public SolitaireWinDetector(CardStack[] fannedPiles, CardStack[] foundations, CardHolder waste, CardDeck stock) {
        this.fannedPiles = fannedPiles;
        this.foundations = foundations;
        this.waste = waste;
        this.stock = stock;
        
        if(stock.getLinker().isPresent())
            stock.getLinker().get().setOnCardsMove(this::checkIfWon);
    }
    
    public void updateOnCardMove(CardHolderLinker linker) {
        linker.setOnCardsMove(this::checkIfWon);
    }
    
    public BiConsumer<CardHolder, CardHolder> getOnCardMove() {
        return this::checkIfWon;
    }
    
    private void checkIfWon(CardHolder form, CardHolder to) {
        if(stock.getSize() != 0 || waste.getSize() != 0)
            return;
        for(CardStack cardStack : fannedPiles)
            for(int i = 0; i < cardStack.getSize(); i++)
                if(!cardStack.getCard(i).isRevealed())
                    return;
        
        if(onGameWon != null)
            onGameWon.run();
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
                addActor(card);
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
    
    public Runnable getOnGameWon() {
        return onGameWon;
    }
    
    public void setOnGameWon(Runnable onGameWon) {
        this.onGameWon = onGameWon;
    }
}
