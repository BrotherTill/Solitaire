package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.cards.dragndrop.CardHolderLinker;
import com.tjirm.solitaire.cards.dragndrop.CardTypeTarget;

import java.util.Optional;

public class CardStack extends CardHolder {
    private RevealedCards revealedCards;
    private float xOffset;
    private float yOffset;
    private boolean onlyTop;
    private final CardTypeTarget cardTypeTarget;
    
    private final Array<Card> cards = new Array<>(8);
    private CardHolderLinker linker;
    
    public enum RevealedCards {
        none,
        all,
        top,
        custom
    }
    
    public CardStack(RevealedCards revealedCards, float xOffset, float yOffset, boolean onlyTop, CardTypeTarget cardTypeTarget) {
        this.revealedCards = revealedCards;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.onlyTop = onlyTop;
        this.cardTypeTarget = cardTypeTarget;
        setWidth(Solitaire.preferences.getCardWidth() + xOffset * cards.size);
        setHeight(Solitaire.preferences.getCardHeight() + yOffset * cards.size);
    }
    public CardStack(RevealedCards visibleCards, float xOffset, float yOffset, boolean onlyTop) {
        this(visibleCards, xOffset, yOffset, onlyTop, new CardTypeTarget());
    }
    public CardStack(RevealedCards visibleCards, float xOffset, float yOffset, CardTypeTarget cardTypeTarget) {
        this(visibleCards, xOffset, yOffset, false, cardTypeTarget);
    }
    public CardStack(RevealedCards visibleCards, float xOffset, float yOffset) {
        this(visibleCards, xOffset, yOffset, false, new CardTypeTarget());
    }
    public CardStack(RevealedCards visibleCards) {
        this(visibleCards, 0, 0, false, new CardTypeTarget());
    }
    
    public void addCard(Card card) {
        addActor(card);
        card.linkHolder(this);
        if(onlyTop && getTopCard().isPresent())
            getTopCard().get().removeListener(linker.getCardDragger());
        card.addListener(linker.getCardDragger());
        card.setPosition(xOffset * cards.size, yOffset * cards.size);
        cards.add(card);
        setWidth(Solitaire.preferences.getCardWidth() + xOffset * cards.size);
        setHeight(Solitaire.preferences.getCardWidth() + yOffset * cards.size);
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
        card.removeListener(linker.getCardDragger());
        if(cards.size > 1) {
            if(revealedCards == RevealedCards.top && isTopCard(card))
                cards.get(cards.size - 2).setRevealed(true);
            if(onlyTop)
                cards.get(cards.size - 2).addListener(linker.getCardDragger());
        }
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
    public Array<Card> getCards() {
        return cards;
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
    public Optional<CardHolderLinker> getLinker() {
        return Optional.ofNullable(linker);
    }
    public boolean hasLinker() {
        return linker != null;
    }
    
    @Override
    public CardTypeTarget getCardTypeTarget() {
        return cardTypeTarget;
    }
}
