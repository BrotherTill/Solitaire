package com.tjirm.solitaire.cards;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.preferences.Preferences;

import java.util.Optional;

public class Card extends Actor {
    private Drawable front;
    private Drawable back;
    private boolean revealed = true;
    private boolean draggable = true;
    
    private CardHolder holder;
    private final CardType cardType;
    
    MoveToAction lagToAction = new MoveToAction();
    
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
        lagToAction.setDuration(Preferences.LAG_TO_DURATION);
        lagToAction.setInterpolation(Interpolation.sineOut);
    }
    
    private void setSkin(Drawable front, Drawable back) {
        this.front = front;
        this.back = back;
    }
    public void updateSize(float ignoredNewSize) {
        setSize(Solitaire.preferences.getCardWidth(), Solitaire.preferences.getCardHeight());
    }
    
    public Rectangle getBounds() {
        Vector2 pos = localToStageCoordinates(new Vector2());
        return new Rectangle(pos.x, pos.y, getWidth(), getHeight());
    }
    public void lagTo(float x, float y) {
        lagToAction.setPosition(x, y);
        lagToAction.restart();
        removeAction(lagToAction);
        addAction(lagToAction);
    }
    public void setLagDuration(float duration) {
        lagToAction.setDuration(duration);
    }
    public void moveTo(float x, float y) {
        addAction(Actions.moveTo(x, y, Preferences.MOVE_TO_DURATION, Interpolation.sineOut));
    }
    public void moveTo(float x, float y, float duration) {
        addAction(Actions.moveTo(x, y, duration, Interpolation.sineOut));
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
    public CardHolder getCardHolder() {
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