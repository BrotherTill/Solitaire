package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.Solitaire;

public class CardStack extends Group {
    private RevealedCards revealedCards;
    private float xOffset;
    private float yOffset;
    
    private final Array<Card> cards = new Array<>();
    
    public enum RevealedCards {
        none,
        all,
        top,
        custom
    }
    
    public CardStack(RevealedCards revealedCards, float xOffset, float yOffset) {
        this.revealedCards = revealedCards;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    public CardStack(RevealedCards visibleCards) {
        this(visibleCards, 0, 0);
    }
    
    public void addCard(Card card) {
        addActor(card);
        card.setPosition(xOffset * cards.size, yOffset * cards.size);
        cards.add(card);
        switch(revealedCards) {
            case all -> card.setRevealed(true);
            case top ->  {
                card.setRevealed(true);
                if(cards.size > 1)
                    cards.get(cards.size - 2).setRevealed(false);
            }
            default -> card.setRevealed(false);
        }
    }
    
    public void removeCard(Card card) {
        removeActor(card);
        if(revealedCards == RevealedCards.top && isTopCard(card))
            cards.get(cards.size - 2).setRevealed(true);
        cards.removeValue(card, true);
    }
    public void removeTopCard() {
        removeCard(cards.get(cards.size - 1));
    }
    
    public void clearCards() {
        for(Card card : cards)
            removeActor(card);
        cards.clear();
    }
    
    public boolean isTopCard(Card card) {
        return cards.get(cards.size - 1) == card;
    }
    
    public Card getCard(int index) {
        return cards.get(index);
    }
    public Card getTopCard() {
        return getCard(cards.size - 1);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), Solitaire.options.getCardWidth() + xOffset * cards.size, Solitaire.options.getCardHeight() + yOffset * cards.size);
    }
    
    public Rectangle getCardBounds(int index) {
        Rectangle rectangle = cards.get(index).getBounds();
        rectangle.x += getX();
        rectangle.y += getY();
        return rectangle;
    }
    public Rectangle getTopCardBounds() {
        return getCardBounds(cards.size - 1);
    }
    
    private void updateCardPositions() {
        for(int i = 0; i < cards.size; i++)
            cards.get(i).setPosition(xOffset * i, yOffset * i);
    }
    
    public RevealedCards getRevealedCards() {
        return revealedCards;
    }
    public void setRevealedCards(RevealedCards revealedCards) {
        this.revealedCards = revealedCards;
    }
    
    public float getXOffset() {
        return xOffset;
    }
    public float getYOffset() {
        return yOffset;
    }
    
    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
        updateCardPositions();
    }
    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
        updateCardPositions();
    }
}