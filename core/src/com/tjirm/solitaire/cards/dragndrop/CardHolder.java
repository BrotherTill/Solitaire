package com.tjirm.solitaire.cards.dragndrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.cards.Card;
import com.tjirm.solitaire.cards.CardType;

import java.util.Optional;
import java.util.function.BiPredicate;

public abstract class CardHolder extends Group {
    public abstract void addCard(Card card);
    public abstract void addCards(Card[] cards);
    public abstract boolean accepts(CardType cardType);
    public abstract Card getCard(int index);
    public abstract Optional<Card> getTopCard();
    public abstract int getCardIndex(Card card);
    public abstract int getSize();
    public abstract void removeCard(Card card);
    public abstract void removeTopCard();
    protected abstract Rectangle getBounds();
    protected abstract Rectangle getCardBounds();
    public abstract Array<Card> getCards();
    public abstract boolean isRevealOnRemove();
    public abstract void revealTopCard();
    public abstract float getXOffset();
    public abstract float getYOffset();
    
    protected abstract void setLinker(CardHolderLinker cardStackLinker);
    public abstract Optional<CardHolderLinker> getLinker();
    public abstract boolean hasLinker();
    
    
    public static final BiPredicate<CardType, CardType> DEFAULT_SOLITAIRE =
            (topCardType, cardType) -> topCardType.isOppositeColor(cardType) && topCardType.isPreviousFace(cardType);
    public static final BiPredicate<CardType, CardType> DEFAULT_FOUNDATIONS =
            (topCardType, cardType) -> topCardType.isSameSuit(cardType) && topCardType.isNextFace(cardType);
}
