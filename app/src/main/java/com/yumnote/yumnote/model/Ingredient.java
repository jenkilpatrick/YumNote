package com.yumnote.yumnote.model;

/**
 * Created by jen on 2/15/16.
 */
public class Ingredient {
    // e.g., 2
    private int value;

    // e.g., cups
    private String measure;

    // e.g., flour
    private String item;

    public Ingredient(int value, String measure, String item) {
        this.value = value;
        this.measure = measure;
        this.item = item;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
