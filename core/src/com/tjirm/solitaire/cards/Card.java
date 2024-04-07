package com.tjirm.solitaire.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tjirm.solitaire.Solitaire;

import java.util.Optional;

public class Card extends Actor {
    private Drawable front;
    private Drawable back;
    private boolean revealed = true;
    private boolean draggable = true;
    
    private CardHolder holder;
    private final CardType cardType;
    
    public Card() {
        this(null);
        front = Solitaire.sprites.getDrawable("card_front");
        back = Solitaire.sprites.getDrawable("card_back");
    }
    public Card(CardType cardType) {
        this.cardType = cardType;
        if(cardType != null)
            cardType.linkCard(this, this::setSkin);
        updateSize(0);
        Solitaire.preferences.getCardSize().addListener(this::updateSize);
    }
    
    private void setSkin(Drawable front, Drawable back) {
        this.front = front;
        this.back = back;
    }
    public void updateSize(float newSize) {
        setSize(Solitaire.preferences.getCardWidth(), Solitaire.preferences.getCardHeight());
    }
    
    protected Rectangle getBounds() {
        Vector2 pos = localToStageCoordinates(new Vector2());
        return new Rectangle(pos.x, pos.y, getWidth(), getHeight());
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(revealed)
            front.draw(batch, getX(), getY(), getWidth(), getHeight());
        else
            back.draw(batch, getX(), getY(), getWidth(), getHeight());
        
    }
    
    public boolean isRevealed() {
        return revealed;
    }
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
    
    public boolean isDraggable() {
        return draggable;
    }
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
    public Optional<CardType> getCardType() {
        return Optional.ofNullable(cardType);
    }
    
    protected void linkHolder(CardHolder cardHolder) {
        holder = cardHolder;
        if(holder.getLinker().isPresent())
            addListener(holder.getLinker().get().getCardDragger());
    }
    protected CardHolder getCardHolder() {
        return holder;
    }
    protected void unlinkHolder(CardHolder cardHolder) {
        if(holder != cardHolder)
            return;
        if(holder.getLinker().isPresent())
            removeListener(holder.getLinker().get().getCardDragger());
        holder = null;
    }
}