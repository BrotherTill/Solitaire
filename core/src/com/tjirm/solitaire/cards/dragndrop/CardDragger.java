package com.tjirm.solitaire.cards.dragndrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.tjirm.solitaire.cards.Card;

public class CardDragger extends DragListener {
    private final CardHolderLinker linker;
    
    private float xOffset;
    private float yOffset;
    private CardHolder holder;
    
    protected CardDragger(CardHolderLinker linker) {
        this.linker = linker;
    }
    
    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {
        if((event.getTarget() instanceof Card card)) {
            if(card.isDraggable()) {
                Gdx.app.log(getClass().getSimpleName(), "dragging");
                
                xOffset = x;
                yOffset = y;
                holder = card.getCardHolder();
                if(!linker.beginDrag(card))
                    cancel();
                return;
            }
        }
        cancel();
    }
    
    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        linker.getCardOverlay().lagTo(event.getStageX() - xOffset, event.getStageY() - yOffset);
    }
    
    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        if(!(event.getTarget() instanceof Card))
            return;
        linker.endDrag(holder);
    }
    
}
