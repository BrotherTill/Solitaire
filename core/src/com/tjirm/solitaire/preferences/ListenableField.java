package com.tjirm.solitaire.preferences;

import java.util.LinkedList;
import java.util.function.Consumer;

public class ListenableField<T> implements iListenable<T>{
    private T value;
    private final LinkedList<Consumer<T>> listeners = new LinkedList<>();
    
    @Override
    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
    
     private void triggerListeners() {
        for(Consumer<T> listener : listeners) listener.accept(value);
    }
    
    @Override
    public T get() {
        return value;
    }
    
    @Override
    public void set(T value) {
        this.value = value;
        triggerListeners();
    }
    
    @Override
    public String toString() {
        return "(value=" + value + ')';
    }
}
