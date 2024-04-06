package com.tjirm.solitaire;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.function.Consumer;

public class Options implements Json.Serializable {
    public static final float CARD_WIDTH_NORMAL = 169;
    public static final float CARD_HEIGHT_NORMAL = 240;
    public static final float SCREEN_WIDTH_NORMAL = 800;
    public static final float SCREEN_HEIGHT_NORMAL = 600;
    
    private final ListenableField<Float> cardSize = new ListenableField<>();
    private final ListenableField<Float> screenSize = new ListenableField<>();
    
    protected Options() {
        this(1, 1);
    }
    public Options(float cardSize, float screenSize) {
        this.cardSize.set(cardSize);
        this.screenSize.set(screenSize);
    }
    
    public void addCardListener(Consumer<Float> listener) {
        cardSize.addListener(listener);
    }
    public void removeCardListener(Consumer<Float> listener) {
        cardSize.removeListener(listener);
    }
    
    public void addScreenListener(Consumer<Float> listener) {
        screenSize.addListener(listener);
    }
    public void removeScreenListener(Consumer<Float> listener) {
        screenSize.removeListener(listener);
    }
    
    public float getCardWidth() {
        return cardSize.get() * CARD_WIDTH_NORMAL;
    }
    public float getCardHeight() {
        return cardSize.get() * CARD_HEIGHT_NORMAL;
    }
    
    public int getScreenWidth() {
        return (int) (screenSize.get() * SCREEN_WIDTH_NORMAL);
    }
    public int getScreenHeight() {
        return (int) (screenSize.get() * SCREEN_HEIGHT_NORMAL);
    }
    
    public static Options getDefaultOptions() {
        return new Options(1, 1);
    }
    
    public float getCardSize() {
        return cardSize.get();
    }
    public float getScreenSize() {
        return screenSize.get();
    }
    
    public void setCardSize(float cardSize) {
        this.cardSize.set(cardSize);
    }
    public void setScreenSize(float screenSize) {
        this.screenSize.set(screenSize);
    }
    
    @Override
    public void write(Json json) {
        json.writeValue("cardSize", cardSize.get(), Float.class);
        json.writeValue("screenSize", screenSize.get(), Float.class);
    }
    
    @Override
    public void read(Json json, JsonValue jsonData) {
        cardSize.set(jsonData.child().asFloat());
        screenSize.set(jsonData.child().next().asFloat());
    }
    
    @Override
    public String toString() {
        return "Options{" +
                       "cardSize=" + cardSize +
                       ", screenSize=" + screenSize +
                       '}';
    }
}
