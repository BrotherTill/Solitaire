package com.tjirm.solitaire.preferences;

import java.util.LinkedList;
import java.util.function.Consumer;

public class ListenableField<V> implements iListenable<V>{
    private V value;
    private final LinkedList<Consumer<V>> listeners = new LinkedList<>();
    
    @Override
    public void addListener(Consumer<V> listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeListener(Consumer<V> listener) {
        listeners.remove(listener);
    }
    
     private void triggerListeners() {
        for(Consumer<V> listener : listeners) listener.accept(value);
    }
    
    @Override
    public V get() {
        return value;
    }
    
    @Override
    public void set(V value) {
        this.value = value;
        triggerListeners();
    }
    
    @Override
    public String toString() {
        return "(value=" + value + ')';
    }
}
