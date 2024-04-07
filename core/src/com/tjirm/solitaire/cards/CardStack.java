package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.Solitaire;

import java.util.Optional;

public class CardStack extends CardHolder {
    private RevealedCards revealedCards;
    private float xOffset;
    private float yOffset;
    
    private final Array<Card> cards = new Array<>(8);
    private CardHolderLinker linker;
    
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
        card.linkHolder(this);
        card.setPosition(xOffset * cards.size, yOffset * cards.size);
        cards.add(card);
        switch(revealedCards) {
            case all -> card.setRevealed(true);
            case none -> card.setRevealed(false);
            case top ->  {
                card.setRevealed(true);
                if(cards.size > 1)
                    cards.get(cards.size - 2).setRevealed(false);
            }
        }
    }
    public void addCards(Card[] cards) {
        for(Card card : cards)
            addCard(card);
    }
    
    public void removeCard(Card card) {
        removeActor(card);
        card.unlinkHolder(this);
        if(revealedCards == RevealedCards.top && isTopCard(card))
            if(cards.size > 1)
                cards.get(cards.size - 2).setRevealed(true);
        cards.removeValue(card, true);
    }
    public void removeTopCard() {
        removeCard(cards.get(cards.size - 1));
    }
    
    public void clearCards() {
        for(Card card : cards) {
            removeActor(card);
            card.unlinkHolder(this);
        }
        cards.clear();
    }
    
    public boolean isTopCard(Card card) {
        return cards.get(cards.size - 1) == card;
    }
    
    public Card getCard(int index) {
        return cards.get(index);
    }
    public Optional<Card> getTopCard() {
        if(cards.isEmpty())
            return Optional.empty();
        return Optional.of(getCard(cards.size - 1));
    }
    
    public int getCardIndex(Card card) {
        return cards.indexOf(card, true);
    }
    public int getSize() {
        return cards.size;
    }
    
    protected Rectangle getBounds() {
        return new Rectangle(getX(), getY(), Solitaire.preferences.getCardWidth() + xOffset * cards.size, Solitaire.preferences.getCardHeight() + yOffset * cards.size);
    }
    protected Rectangle getCardBounds() {
        if(getTopCard().isEmpty())
            return getBounds();
        return getTopCard().get().getBounds();
    }
    
    public Rectangle getCardBounds(int index) {
        if(cards.size >= index || index < 0)
            return null;
        Rectangle rectangle = cards.get(index).getBounds();
        rectangle.x += getX();
        rectangle.y += getY();
        return rectangle;
    }
    public Rectangle getTopCardBounds() {
        if(cards.isEmpty())
            return null;
        return getCardBounds(cards.size - 1);
    }
    
    protected void updateCardPositions() {
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
    
    protected void setLinker(CardHolderLinker linker) {
        this.linker = linker;
        
    }
    protected Optional<CardHolderLinker> getLinker() {
        return Optional.ofNullable(linker);
    }
    protected boolean hasLinker() {
        return linker != null;
    }
}
