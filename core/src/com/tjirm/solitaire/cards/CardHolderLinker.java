package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CardHolderLinker {
    private final LinkedList<CardHolder> cardHolders = new LinkedList<>();
    private CardDecoy cardDecoy;
    private final CardDragger cardDragger;
    
    private final boolean dragNDrop;
    private final Stage stage;
    
    public CardHolderLinker(Stage stage, boolean dragNDrop) {
        this.stage = stage;
        this.dragNDrop = dragNDrop;
        this.cardDragger = new CardDragger(this);
    }
    
    protected boolean beginDrag(Card origin) {
        if(!dragNDrop)
            return false;
        cardDecoy = new CardDecoy(origin.getCardHolder(), origin);
        stage.addActor(cardDecoy);
        return true;
    }
    
    protected void endDrag(CardHolder originHolder) {
        CardHolder closest = null;
        float leastDistance = Float.MAX_VALUE;
        for(CardHolder cardHolder : cardHolders) {
            if(getDistance(cardDecoy.getBounds(), cardHolder.getCardBounds()) >= leastDistance)
                continue;
            leastDistance = getDistance(cardDecoy.getBounds(), cardHolder.getCardBounds());
            closest = cardHolder;
        }
        if(closest == null || !closest.getCardBounds().overlaps(cardDecoy.getBounds()))
            originHolder.addCards(cardDecoy.getCards());
        else if(closest.getTopCard().isEmpty() || closest.getTopCard().get().getCardType().isEmpty())
            closest.addCards(cardDecoy.getCards());
        else if(cardDecoy.goesOn(closest.getTopCard().get().getCardType().get()))
                closest.addCards(cardDecoy.getCards());
        else
            originHolder.addCards(cardDecoy.getCards());
    }
    
    private float getDistance(Rectangle first, Rectangle second) {
        return first.getCenter(new Vector2()).dst(second.getCenter(new Vector2()));
    }
    
    public CardHolder linkCardHolder(CardHolder cardHolder) {
        cardHolders.add(cardHolder);
        cardHolder.setLinker(this);
        return cardHolder;
    }
    
    public CardHolder getCardHolder(int index) {
        return cardHolders.get(index);
    }
    
    public void forEach(Consumer<CardHolder> action) {
        cardHolders.forEach(action);
    }
    
    public List<CardHolder> getCardHolders() {
        return cardHolders;
    }
    
    protected CardDragger getCardDragger() {
        return cardDragger;
    }
    protected CardDecoy getCardDecoy() {
        return cardDecoy;
    }
}