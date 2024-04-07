package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Optional;

public abstract class CardHolder extends Group {
    public abstract void addCard(Card card);
    public abstract void addCards(Card[] cards);
    public abstract Card getCard(int index);
    public abstract Optional<Card> getTopCard();
    public abstract int getCardIndex(Card card);
    public abstract int getSize();
    public abstract void removeCard(Card card);
    public abstract void removeTopCard();
    protected abstract Rectangle getBounds();
    protected abstract Rectangle getCardBounds();
    protected abstract void updateCardPositions();
    protected abstract void setLinker(CardHolderLinker cardStackLinker);
    protected abstract Optional<CardHolderLinker> getLinker();
    protected abstract boolean hasLinker();
}
