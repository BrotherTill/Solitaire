package com.tjirm.solitaire.logic;

import com.badlogic.gdx.utils.Array;
import com.tjirm.solitaire.cards.Card;
import com.tjirm.solitaire.cards.CardDeck;
import com.tjirm.solitaire.cards.CardStack;
import com.tjirm.solitaire.cards.CardType;
import com.tjirm.solitaire.cards.CardType.CardFace;
import com.tjirm.solitaire.cards.CardType.Suit;

import java.util.Random;

public class SolitaireGameGenerator {
    private final CardStack[] fannedPiles;
    private final CardStack[] foundations;
    private final CardStack waste;
    private final CardDeck stock;
    
    public SolitaireGameGenerator(CardStack[] fannedPiles, CardStack[] foundations, CardStack waste, CardDeck stock) {
        this.fannedPiles = fannedPiles;
        this.stock = stock;
        this.waste = waste;
        this.foundations = foundations;
    }
    
    public void reset() {
        stock.clearCards();
        waste.clearCards();
        for(CardStack fannedPile : fannedPiles) fannedPile.clearCards();
        for(CardStack foundation : foundations) foundation.clearCards();
        
        Array<Card> cards = new Array<>(52);
        for(int i = 0; i < CardType.Suit.values().length - 1; i++)
            for(int j = 0; j < CardType.CardFace.values().length - 1; j++)
                cards.add(new Card(new CardType(CardType.Suit.values()[i], CardType.CardFace.values()[j])));
        cards.shuffle();
        
        for(CardStack cardStack : fannedPiles)
            cardStack.setRevealedCards(CardStack.RevealedCards.top);
        
        for(int i = 0; i < fannedPiles.length; i++)
            for(int j = 0; j <= i; j++)
                fannedPiles[i].addCard(cards.pop());
        
        for(CardStack cardStack : fannedPiles)
            cardStack.setRevealedCards(CardStack.RevealedCards.onRemove);
        
        stock.addCards(cards.toArray(Card.class));
    }
    
    @Deprecated
    public void generateWinnableGame() {
        Random random = new Random();
        
        fillFoundations();
        while(!foundationsEmpty()) {
            System.out.print("stuck at " + foundations[0].getSize() + foundations[1].getSize() + foundations[2].getSize() + foundations[3].getSize());
            switch(random.nextInt(4)) {
                case 0 ->   moveOutOfFoundation(random);
                case 1 ->   moveAroundFannedPiles(random);
                case 2 ->   moveToStock(random);
                case 3 ->   moveOutOfStock(random);
            }
            System.out.println("   generating");
        }
        System.out.println("generated");
        
        balanceStock();
        sortFannedPiles();
    }
    
    private void moveOutOfFoundation(Random random) {
        CardStack[] filledFoundations = getFilledFoundations();
        Card card = filledFoundations[random.nextInt(filledFoundations.length)].removeTopCard();
        CardStack[] acceptingFannedPiles = getAcceptingFannedPiles(card);
        if(acceptingFannedPiles.length > 0)
            acceptingFannedPiles[random.nextInt(acceptingFannedPiles.length)].addCard(card);
        else
            getAcceptingFoundation(card).addCard(card);
    }
    
    private void moveAroundFannedPiles(Random random) {
        CardStack[] filledFannedPiles = getFilledFannedPiles();
        if(filledFannedPiles.length == 0)
            return;
        int index = random.nextInt(filledFannedPiles.length);
        Card card = filledFannedPiles[index].removeTopCard();
        fannedPiles[random.nextInt(7)].addCard(card);
    }
    
    private void moveToStock(Random random) {
        CardStack[] filledFannedPiles = getFilledFannedPiles();
        if(filledFannedPiles.length == 0)
            return;
        stock.addCard(filledFannedPiles[random.nextInt(filledFannedPiles.length)].removeTopCard());
    }
    
    private void moveOutOfStock(Random random) {
        if(stock.getSize() == 0)
            return;
        Card card = stock.getCard(random.nextInt(stock.getSize()));
        stock.removeCard(card);
        CardStack[] acceptingFannedPiles = getAcceptingFannedPiles(card);
        if(acceptingFannedPiles.length > 0)
            acceptingFannedPiles[random.nextInt(acceptingFannedPiles.length)].addCard(card);
        else
            stock.addCard(card);
    }
    
    
    private void fillFoundations() {
        for(int i = 0; i < foundations.length; i++)
            for(int j = 0; j < CardFace.values().length - 1; j++)
                foundations[i].addCard(new Card(new CardType(Suit.values()[i], CardFace.values()[j])));
    }
    
    private boolean foundationsEmpty() {
        for(CardStack foundation : foundations)
            if(foundation.getSize() > 0)
                return false;
        return true;
    }
    
    private CardStack[] getFilledFoundations() {
        Array<CardStack> filledFoundations = new Array<>(4);
        for(CardStack foundation : foundations)
            if(foundation.getSize() > 0)
                filledFoundations.add(foundation);
        return filledFoundations.toArray(CardStack.class);
    }
    private CardStack[] getFilledFannedPiles() {
        Array<CardStack> filledFannedPiles = new Array<>(4);
        for(CardStack fannedPile : fannedPiles)
            if(fannedPile.getSize() > 0)
                filledFannedPiles.add(fannedPile);
        return filledFannedPiles.toArray(CardStack.class);
    }
    
    private CardStack[] getAcceptingFannedPiles(Card card) {
        Array<CardStack> acceptingFannedPiles = new Array<>(4);
        for(CardStack fannedPile : fannedPiles)
            if(fannedPile.accepts(card.getCardType()))
                acceptingFannedPiles.add(fannedPile);
        return acceptingFannedPiles.toArray(CardStack.class);
    }
    private CardStack getAcceptingFoundation(Card card) {
        for(CardStack foundation : foundations)
            if(foundation.accepts(card.getCardType()))
                return foundation;
        return null;
    }
    
    private void balanceStock() {
        if(stock.getSize() > 24)
            while(stock.getSize() != 24)
                moveOutOfStock(new Random());
        else if(stock.getSize() < 24)
            while(stock.getSize() != 24)
                moveToStock(new Random());
    }
    private void sortFannedPiles() {/*
        for(int i = 0; i < fannedPiles.length; i++) {
            if(fannedPiles[i].getSize() > i + 1)
                while(fannedPiles[i].getSize() != i + 1) {
                    get
                }
                
        }*/
    }
    
}
