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
        spades
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
        king
    }
    
    public CardType(Suit suit, CardFace cardFace) {
        this.suit = suit;
        this.cardFace = cardFace;
    }
    
    public void updateSkin(String newSkin) {
        cardSkinSetter.setSkin( Solitaire.sprites.getDrawable(newSkin + '_' + suit.name() + '_' + cardFace.name()),
                                Solitaire.sprites.getDrawable(newSkin + "_back"));
    }
    
    public boolean goesOn(CardType cardType) {
        switch(suit) {
            case clubs, spades:
                if(cardType.suit == Suit.clubs || cardType.suit == Suit.spades)
                    return false;
            break;
            case hearts, diamonds:
                if(cardType.suit == Suit.hearts || cardType.suit == Suit.diamonds)
                    return false;
            break;
        }
        return cardType.cardFace.ordinal() == cardFace.ordinal() + 1;
    }
    
    protected void linkCard(Card card, CardSkinSetter cardSkinSetter) {
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