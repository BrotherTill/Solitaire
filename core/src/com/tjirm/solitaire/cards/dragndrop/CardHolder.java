package com.tjirm.solitaire.cards.dragndrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.cards.Card;

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
    public abstract Array<Card> getCards();
    protected abstract void updateCardPositions();
    protected abstract void setLinker(CardHolderLinker cardStackLinker);
    public abstract Optional<CardHolderLinker> getLinker();
    public abstract boolean hasLinker();
    public abstract CardTypeTarget getCardTypeTarget();
    public abstract float getXOffset();
    public abstract float getYOffset();
}
