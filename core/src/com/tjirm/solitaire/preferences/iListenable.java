package com.tjirm.solitaire.preferences;

import java.util.function.Consumer;

public interface iListenable<V> {
    void addListener(Consumer<V> listener);
    void removeListener(Consumer<V> listener);
    V get();
    void set(V value);
}
