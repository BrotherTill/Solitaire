package com.tjirm.solitaire.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tjirm.solitaire.Solitaire;

public class Card extends Actor {
    Drawable texture;
    
    public Card() {
        texture = Solitaire.sprites.getDrawable("card");
        updateSize(0);
        Solitaire.options.addCardListener(this::updateSize);
    }
    
    public void updateSize(float newSize) {
        setSize(Solitaire.options.getCardWidth(), Solitaire.options.getCardHeight());
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        texture.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
}