package com.tjirm.solitaire.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;

public class Preferences implements Json.Serializable {
    public static final float CARD_WIDTH_NORMAL = 169;
    public static final float CARD_HEIGHT_NORMAL = 240;
    public static final float SCREEN_WIDTH_NORMAL = 800;
    public static final float SCREEN_HEIGHT_NORMAL = 600;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private final ListenableField<Float> cardSize = new ListenableField<>();
    private final ListenableField<Float> screenSize = new ListenableField<>();
    private final ListenableField<String> skin = new ListenableField<>();
    
    public Preferences(float cardSize, float screenSize, String skin) {
        this.cardSize.set(cardSize);
        this.screenSize.set(screenSize);
        this.skin.set(skin);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private Preferences() {
        this(0, 0, "");
    }
    
    public static Preferences load() {
        Preferences preferences = null;
        try {
            Json json = new Json();
            preferences = json.fromJson(Preferences.class, Gdx.files.local("data/options.yaml"));
        } catch(SerializationException e) {
            Throwable cause = e.getCause();
            if(cause.getMessage().startsWith("Error parsing file") || cause.getCause() == null)
                Gdx.app.error("initialization", "error parsing Options json reverting to default");
            else if(cause.getCause().getMessage().startsWith("File not found"))
                Gdx.app.error("initialization", "couldn't find Options file reverting to default");
            else
                Gdx.app.error("initialization", "unexpected error reading Options reverting to default");
        }
        return preferences == null ? getDefault() : preferences;
    }
    
    public static Preferences getDefault() {
        return new Preferences(1, 1, "wii");
    }
    
    public static void save(Preferences preferences) {
        Json json = new Json();
        Gdx.files.local("data/options.yaml").writeString(json.prettyPrint(preferences), false);
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
    
    public ListenableField<Float> getCardSize() {
        return cardSize;
    }
    public ListenableField<Float> getScreenSize() {
        return screenSize;
    }
    public ListenableField<String> getSkin() {
        return skin;
    }
    
    @Override
    public void write(Json json) {
        json.writeValue("cardSize", cardSize.get(), Float.class);
        json.writeValue("screenSize", screenSize.get(), Float.class);
        json.writeValue("skin", skin.get(), String.class);
    }
    
    @Override
    public void read(Json json, JsonValue jsonData) {
        cardSize.set(jsonData.child().asFloat());
        screenSize.set(jsonData.child().next().asFloat());
        skin.set(jsonData.child().next().next().asString());
    }
    
    @Override
    public String toString() {
        return "Preferences{" +
                       "cardSize=" + cardSize +
                       ", screenSize=" + screenSize +
                       ", skin=" + skin +
                       '}';
    }
}
