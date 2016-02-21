package com.yumnote.yumnote.model;

/**
 * Created by jen on 2/19/16.
 */
public class ModelHolder<E> {
    private String key;
    private E value;

    ModelHolder(String key, E value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public E getValue() {
        return value;
    }
}
