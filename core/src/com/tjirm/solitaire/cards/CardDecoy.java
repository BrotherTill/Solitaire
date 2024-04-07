package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tjirm.solitaire.Solitaire;

public class CardDecoy extends Group {
    private final CardHolder cardHolder;
    private final Card[] cards;
    
    private float xOffset;
    private float yOffset;
    
    protected CardDecoy(CardHolder cardHolder, Card origin) {
        this.cardHolder = cardHolder;
        this.cards = new Card[cardHolder.getSize() - cardHolder.getCardIndex(origin)];
        initializeOffset();
        fillCards(origin);
        setPosition(cardHolder.getX(), cardHolder.getY());
        updateCardPositions();
    }
    
    private void initializeOffset() {
        if(cardHolder instanceof CardStack cardStack) {
            xOffset = cardStack.getXOffset();
            yOffset = cardStack.getYOffset();
        } else {
            xOffset = 0;
            yOffset = 0;
        }
    }
    
    private void fillCards(Card origin) {
        int start = cardHolder.getCardIndex(origin);
        for(int i = 0; cardHolder.getSize() > start; i++) {
            cards[i] = cardHolder.getCard(start);
            addActor(cards[i]);
            cardHolder.removeCard(cards[i]);
        }
    }
    
    private void updateCardPositions() {
        for(int i = 0; i < cards.length; i++)
            cards[i].setPosition(xOffset * i, yOffset * i);
    }
    
    protected Rectangle getBounds() {
        return new Rectangle(getX(), getY(), Solitaire.preferences.getCardWidth() + xOffset * cards.length, Solitaire.preferences.getCardHeight() + yOffset * cards.length);
    }
    protected Rectangle getCardBounds() {
        return cards[0].getBounds();
    }
    public boolean goesOn(CardType cardType) {
        if(cards[0].getCardType().isEmpty())
            return true;
        return cards[0].getCardType().get().goesOn(cardType);
    }
    
    public Card[] getCards() {
        return cards;
    }
}