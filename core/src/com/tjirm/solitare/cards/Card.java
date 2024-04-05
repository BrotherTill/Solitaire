package com.tjirm.solitare.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tjirm.solitare.Solitare;

public class Card extends Actor {
    Drawable texture;
    
    public Card() {
        texture = Solitare.sprites.getDrawable("card");
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        texture.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
}
