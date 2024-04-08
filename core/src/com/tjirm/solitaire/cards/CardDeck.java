package com.tjirm.solitaire.cards;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tjirm.solitaire.Solitaire;
import com.tjirm.solitaire.cards.dragndrop.CardHolder;
import com.tjirm.solitaire.preferences.Preferences;

public class CardDeck extends CardStack {
    private CardHolder target;
    
    public CardDeck(RevealedCards visibleCards) {
        super(visibleCards);
        addListener(emptyListener);
    }
    public CardDeck(RevealedCards revealedCards, float xOffset, float yOffset) {
        super(revealedCards, xOffset, yOffset);
        addListener(emptyListener);
    }
    
    @Override
    public void addCard(Card card) {
        addActor(card);
        card.linkHolder(this);
        card.setPosition(getXOffset() * getSize(), getYOffset() * getSize());
        getCards().add(card);
        setWidth(Solitaire.preferences.getCardWidth() + getXOffset() * getSize());
        setHeight(Solitaire.preferences.getCardHeight() + getYOffset() * getSize());
        switch(getRevealedCards()) {
            case all -> card.setRevealed(true);
            case none -> card.setRevealed(false);
            case top ->  {
                card.setRevealed(true);
                if(getSize() > 1)
                    getCards().get(getSize() - 2).setRevealed(false);
            }
        }
        
        card.addListener(inputListener);
    }
    @Override
    public void removeCard(Card card) {
        removeActor(card);
        card.unlinkHolder(this);
        card.removeListener(inputListener);
        if(getRevealedCards() == RevealedCards.top && isTopCard(card))
            if(getSize() > 1)
                getCard(getSize() - 2).setRevealed(true);
        getCards().removeValue(card, true);
    }
    
    private boolean moving = false;
    
    private InputListener emptyListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(target.getTopCard().isEmpty() || getTopCard().isPresent() || getLinker().isEmpty())
                return false;
            if(moving)
                return false;
            moving = true;
            for(int i = 0; i < target.getSize(); i++) {
                Vector2 pos = target.stageToLocalCoordinates(localToStageCoordinates(new Vector2()));
                System.out.println(pos);
                target.getCard(target.getSize() - i - 1).removeListener(getLinker().get().getCardDragger());
                target.getCard(target.getSize() - i - 1).addAction(
                        Actions.sequence(   Actions.delay(Preferences.FAST_MOVE_TO_DURATION * i),
                                            Actions.moveTo(pos.x + getXOffset() * i, pos.y + getYOffset() * i, Preferences.FAST_MOVE_TO_DURATION),
                                            Actions.run(() -> {
                                                Card card = target.getTopCard().get();
                                                target.removeTopCard();
                                                addCard(card);
                                            })
                ));
            }
            Vector2 targetPos = stageToLocalCoordinates(target.localToStageCoordinates(new Vector2()));
            addAction(Actions.sequence( Actions.delay(Preferences.FAST_MOVE_TO_DURATION * (target.getSize() + 0.5F)),
                                        Actions.run(() -> {
                                            System.out.println("bad");
                                            getTopCard().get().moveTo(targetPos.x, targetPos.y, Preferences.FAST_MOVE_TO_DURATION);
                                        }),
                                        Actions.delay(Preferences.FAST_MOVE_TO_DURATION),
                                        Actions.run(() -> {
                                            Card card = getTopCard().get();
                                            removeTopCard();
                                            target.addCard(card);
                                            moving = false;
                                        })
            ));
            return true;
        }
    };
    
    private InputListener inputListener = new InputListener(){
        
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(target == null || !target.hasLinker() || moving)
                return false;
            moving = true;
            Vector2 targetPos = stageToLocalCoordinates(target.localToStageCoordinates(new Vector2()));
            getTopCard().get().moveTo(targetPos.x + target.getXOffset() * target.getSize(), targetPos.y + target.getYOffset() * target.getSize());
            addAction(Actions.sequence(Actions.delay(Preferences.MOVE_TO_DURATION), Actions.run(() -> {
                Card card = getTopCard().get();
                removeTopCard();
                target.addCard(card);
                moving = false;
            })));
            return true;
        }
    };
    
    public CardHolder getTarget() {
        return target;
    }
    public void setTarget(CardHolder target) {
        this.target = target;
    }
}