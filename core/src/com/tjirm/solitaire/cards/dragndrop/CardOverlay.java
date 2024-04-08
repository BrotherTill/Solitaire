package com.tjirm.solitaire.cards.dragndrop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.Card;
import com.tjirm.solitaire.cards.CardStack;
import com.tjirm.solitaire.cards.CardType;
import com.tjirm.solitaire.preferences.Preferences;

public class CardOverlay extends Group {
    private final CardHolder cardHolder;
    private final Card[] cards;
    
    private float xOffset;
    private float yOffset;
    
    protected CardOverlay(CardHolder cardHolder, Card origin) {
        this.cardHolder = cardHolder;
        this.cards = new Card[cardHolder.getSize() - cardHolder.getCardIndex(origin)];
        fillCards(origin);
        initializeOffset();
    }
    
    private void initializeOffset() {
        if(cardHolder instanceof CardStack cardStack) {
            xOffset = cardStack.getXOffset();
            yOffset = cardStack.getYOffset();
        } else {
            xOffset = 0;
            yOffset = 0;
        }
    }
    
    private void fillCards(Card origin) {
        int start = cardHolder.getCardIndex(origin);
        for(int i = 0; cardHolder.getSize() > start; i++) {
            cards[i] = cardHolder.getCard(start);
            Vector2 pos = cards[i].localToStageCoordinates(new Vector2());
            cards[i].setPosition(pos.x, pos.y);
            addActor(cards[i]);
            cards[i].setLagDuration(Preferences.LAG_TO_DURATION + 0.01F * i);
            cardHolder.removeCard(cards[i]);
        }
    }
    
    public void lagTo(float x, float y) {
        for(int i = 0; i < cards.length; i++)
            cards[i].lagTo(x + xOffset * i, y + yOffset * i);
    }
    public void moveTo(float x, float y) {
        for(int i = 0; i < cards.length; i++)
            cards[i].moveTo(x + xOffset * i, y + yOffset * i, Preferences.MOVE_TO_DURATION + 0.05F * i);
    }
    
    public void moveCardsToHolder(CardHolder holder) {
        clearActions();
        if(cardHolder != holder && cardHolder.isRevealOnRemove())
            cardHolder.revealTopCard();
        Vector2 pos = holder.localToStageCoordinates(new Vector2());
        pos.x += holder.getSize() * holder.getXOffset();
        pos.y += holder.getSize() * holder.getYOffset();
        moveTo(pos.x, pos.y);
        addAction(  Actions.sequence(
                    Actions.delay(Preferences.MOVE_TO_DURATION + 0.05F * (cards.length - 1)),
                    Actions.run(() -> holder.addCards(cards)))
        );
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    
    protected Rectangle getBounds() {
        float x = cards[cards.length - 1].getX();
        float y = cards[cards.length - 1].getY();
        float width = Solitaire.preferences.getCardWidth() + xOffset * (cards.length - 1);
        float height = Solitaire.preferences.getCardHeight() + yOffset * (cards.length - 1);
        
        if(xOffset < 0)
            x -= xOffset * (cards.length - 1);
        if(yOffset < 0)
            y -= yOffset * (cards.length - 1);
            
        return new Rectangle(x, y, width, height);
    }
    protected Rectangle getCardBounds() {
        return cards[0].getBounds();
    }
    
    public CardType getCardType() {
        return cards[0].getCardType();
    }
    
    public Card[] getCards() {
        return cards;
    }
}
