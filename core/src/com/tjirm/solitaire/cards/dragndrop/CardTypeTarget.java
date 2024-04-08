package com.tjirm.solitaire.cards.dragndrop;

import com.tjirm.solitaire.cards.CardType;

public class CardTypeTarget {
    private boolean placeable;
    
    public CardTypeTarget() {
        this(true);
    }
    public CardTypeTarget(boolean placeable) {
        this.placeable = placeable;
    }
    
    public boolean takes(CardType cardType) {
        return placeable;
    }
    
    public boolean isPlaceable() {
        return placeable;
    }
    
    public void setPlaceable(boolean placeable) {
        this.placeable = placeable;
    }
}
