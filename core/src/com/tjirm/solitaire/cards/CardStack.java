package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.cards.dragndrop.CardHolderLinker;

import java.util.Optional;
import java.util.function.BiPredicate;

public class CardStack extends CardHolder {
    private RevealedCards revealedCards;
    private DraggableCards draggableCards;
    private boolean draggable = true;
    private float xOffset;
    private float yOffset;
    private BiPredicate<CardType, CardType> cardAcceptor;
    
    private final Array<Card> cards = new Array<>(8);
    private CardHolderLinker linker;
    
    public enum RevealedCards {
        none,
        all,
        top,
        onRemove,
        custom
    }
    
    public enum DraggableCards {
        none,
        all,
        top,
        revealed,
        custom
    }
    
    public CardStack(RevealedCards revealedCards, float xOffset, float yOffset, DraggableCards draggableCards, BiPredicate<CardType, CardType> cardAcceptor) {
        this.revealedCards = revealedCards;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.draggableCards = draggableCards;
        this.cardAcceptor = cardAcceptor;
        setWidth(Solitaire.preferences.getCardWidth() + xOffset * cards.size);
        setHeight(Solitaire.preferences.getCardHeight() + yOffset * cards.size);
    }
    public CardStack(RevealedCards visibleCards, float xOffset, float yOffset, DraggableCards draggableCards) {
        this(visibleCards, xOffset, yOffset, draggableCards, ((a, b) -> true));
    }
    public CardStack(RevealedCards visibleCards, float xOffset, float yOffset, BiPredicate<CardType, CardType> cardAcceptor) {
        this(visibleCards, xOffset, yOffset, DraggableCards.all, cardAcceptor);
    }
    public CardStack(RevealedCards visibleCards, float xOffset, float yOffset) {
        this(visibleCards, xOffset, yOffset, DraggableCards.all, ((a, b) -> true));
    }
    public CardStack(RevealedCards visibleCards) {
        this(visibleCards, 0, 0, DraggableCards.all, ((a, b) -> true));
    }
    
    public void addCard(Card card) {
        addActor(card);
        card.linkHolder(this);
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
        switch(draggableCards) {
            case all -> card.setDraggable(true);
            case none -> card.setDraggable(false);
            case revealed -> {
                card.setDraggable(card.isRevealed());
                if(revealedCards != RevealedCards.top || cards.size <= 1)
                    break;
                cards.get(cards.size - 2).setDraggable(false);
            }
            case top ->  {
                card.setDraggable(true);
                if(cards.size > 1)
                    cards.get(cards.size - 2).setDraggable(false);
            }
        }
    }
    public void addCards(Card[] cards) {
        for(Card card : cards)
            addCard(card);
    }
    
    @Override
    public boolean accepts(CardType cardType) {
        if(cardAcceptor == null)
            return true;
        return cardAcceptor.test(getTopCard().orElseGet(Card::new).getCardType(), cardType);
    }
    
    public void removeCard(Card card) {
        removeActor(card);
        card.unlinkHolder(this);
        if(cards.size > 1) {
            if(revealedCards == RevealedCards.top && isTopCard(card))
                revealTopCard();
            if(draggableCards == DraggableCards.top && isTopCard(card))
                cards.get(cards.size - 2).setDraggable(true);
        }
        cards.removeValue(card, true);
    }
    public Card removeTopCard() {
        if(getTopCard().isEmpty())
            return null;
        Card card = getTopCard().get();
        removeCard(card);
        return card;
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
    
    @Override
    public boolean isDraggable() {
        return draggable;
    }
    @Override
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
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
    
    @Override
    public boolean isRevealOnRemove() {
        return revealedCards == RevealedCards.onRemove;
    }
    @Override
    public void revealTopCard() {
        if(getTopCard().isEmpty())
            return;
        getTopCard().get().setRevealed(true);
        if(draggableCards == DraggableCards.revealed)
            getTopCard().get().setDraggable(true);
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
    
    /**
     * @param cardAcceptor (topCard, cardInQuestion)
     */
    public void setCardAcceptor(BiPredicate<CardType, CardType> cardAcceptor) {
        this.cardAcceptor = cardAcceptor;
    }
}
