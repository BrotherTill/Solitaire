package com.tjirm.solitaire.cards.dragndrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tjirm.solitaire.cards.Card;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CardHolderLinker {
    private final LinkedList<CardHolder> cardHolders = new LinkedList<>();
    private CardOverlay cardOverlay;
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
        cardOverlay = new CardOverlay(origin.getCardHolder(), origin);
        stage.addActor(cardOverlay);
        return true;
    }
    
    protected void endDrag(CardHolder originHolder) {
        CardHolder closest = null;
        float leastDistance = Float.MAX_VALUE;
        for(CardHolder cardHolder : cardHolders) {
            if(getDistance(cardOverlay.getBounds(), cardHolder.getCardBounds()) >= leastDistance)
                continue;
            leastDistance = getDistance(cardOverlay.getBounds(), cardHolder.getCardBounds());
            closest = cardHolder;
        }
        if(closest == null || !closest.getCardBounds().overlaps(cardOverlay.getBounds()))
            cardOverlay.moveCardsToHolder(originHolder);
        else if(closest.getTopCard().isEmpty() || closest.getTopCard().get().getCardType().isEmpty())
            cardOverlay.moveCardsToHolder(closest);
        else if(cardOverlay.goesOn(closest.getTopCard().get().getCardType().get()))
            cardOverlay.moveCardsToHolder(closest);
        else
            cardOverlay.moveCardsToHolder(originHolder);
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
    
    public CardDragger getCardDragger() {
        return cardDragger;
    }
    protected CardOverlay getCardOverlay() {
        return cardOverlay;
    }
}