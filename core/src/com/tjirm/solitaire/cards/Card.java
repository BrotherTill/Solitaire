package com.tjirm.solitaire.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tjirm.solitaire.Solitaire;

public class Card extends Actor {
    private Drawable front;
    private Drawable back;
    private boolean revealed = true;
    private boolean draggable = true;
    
    private CardHolder holder;
    
    public Card() {
        front = Solitaire.sprites.getDrawable("card_front");
        back = Solitaire.sprites.getDrawable("card_back");
        updateSize(0);
        Solitaire.options.addCardListener(this::updateSize);
    }
    
    public void updateSize(float newSize) {
        setSize(Solitaire.options.getCardWidth(), Solitaire.options.getCardHeight());
    }
    
    protected Rectangle getBounds() {
        float x = getX();
        float y = getY();
        setPosition(0, 0);
        Vector2 pos = localToStageCoordinates(new Vector2(getX(), getY()));
        setPosition(x, y);
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