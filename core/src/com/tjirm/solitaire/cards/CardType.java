package com.tjirm.solitaire.cards;

import com.tjirm.solitaire.Solitaire;

public class CardType {
    private final Suit suit;
    private final CardFace cardFace;
    
    private Card card;
    private CardSkinSetter cardSkinSetter;

    public enum Suit {
        clubs,
        diamonds,
        hearts,
        spades,
        any;
        
        public boolean isOppositeColor(Suit suit) {
            boolean out = true;
            switch(this) {
                case clubs, spades -> out = suit == hearts || suit == diamonds;
                case hearts, diamonds -> out = suit == clubs || suit == spades;
            }
            return out;
        }
        public boolean isSameColor(Suit suit) {
            boolean out = true;
            switch(this) {
                case clubs, spades -> out = suit == clubs || suit == spades;
                case hearts, diamonds -> out = suit == hearts || suit == diamonds;
            }
            return out;
        }
        public boolean isSame(Suit suit) {
            return this == any || suit == any || this == suit;
        }
    }
    
    public enum CardFace {
        ace,
        n2,
        n3,
        n4,
        n5,
        n6,
        n7,
        n8,
        n9,
        n10,
        jack,
        queen,
        king,
        any;
        
        public CardFace next() {
            return values()[ordinal() < 12 ? ordinal() + 1 : 0];
        }
        public boolean isSame(CardFace cardFace) {
            return this == any || cardFace == any || this == cardFace;
        }
    }
    
    public boolean isOppositeColor(CardType cardType) {
        return getSuit().isOppositeColor(cardType.getSuit());
    }
    public boolean isSameColor(CardType cardType) {
        return getSuit().isSameColor(cardType.getSuit());
    }
    public boolean isSameSuit(CardType cardType) {
        return getSuit().isSame(cardType.getSuit());
    }
    
    public boolean isNextFace(CardType cardType) {
        if(cardType.getCardFace() == CardFace.any)
            return true;
        if(getCardFace() == CardFace.any && cardType.getCardFace() == CardFace.ace)
            return true;
        return getCardFace().next() == cardType.getCardFace();
    }
    public boolean isPreviousFace(CardType cardType) {
        if(cardType.getCardFace() == CardFace.any)
            return true;
        if(getCardFace() == CardFace.any && cardType.getCardFace() == CardFace.king)
            return true;
        return cardType.getCardFace().next() == getCardFace();
    }
    public CardFace nextFace() {
        return getCardFace().next();
    }
    public boolean isSmeFace(CardType cardType) {
        return getCardFace().isSame(cardType.getCardFace());
    }
    
    public CardType() {
        this(Suit.any, CardFace.any);
    }
    
    public CardType(Suit suit, CardFace cardFace) {
        this.suit = suit;
        this.cardFace = cardFace;
    }
    
    public void updateSkin(String newSkin) {
        if(suit == Suit.any || cardFace == CardFace.any)
            return;
        cardSkinSetter.setSkin( Solitaire.sprites.getDrawable(newSkin + '_' + suit.name() + '_' + cardFace.name()),
                                Solitaire.sprites.getDrawable(newSkin + "_back"));
    }
    
    public void linkCard(Card card, CardSkinSetter cardSkinSetter) {
        this.card = card;
        this.cardSkinSetter = cardSkinSetter;
        updateSkin(Solitaire.preferences.getSkin().get());
        Solitaire.preferences.getSkin().addListener(this::updateSkin);
    }
    
    public Suit getSuit() {
        return suit;
    }
    public CardFace getCardFace() {
        return cardFace;
    }
}