package com.tjirm.solitaire.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tjirm.solitaire.Solitaire;

public class Card extends Actor {
    private Drawable front;
    private Drawable back;
    private boolean revealed = true;
    
    public Card() {
        front = Solitaire.sprites.getDrawable("card_front");
        back = Solitaire.sprites.getDrawable("card_back");
        updateSize(0);
        Solitaire.options.addCardListener(this::updateSize);
    }
    
    public void updateSize(float newSize) {
        setSize(Solitaire.options.getCardWidth(), Solitaire.options.getCardHeight());
    }
    
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), Solitaire.options.getCardWidth(), Solitaire.options.getCardHeight());
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
}