package com.yumnote.yumnote.model;

import java.util.Objects;

/**
 * Created by jen on 2/15/16.
 */
public class Ingredient {
    // e.g., 2
    private int value;

    // TODO: denormalize singular/plural
    // e.g., cups
    private String measure;

    // TODO: denormalize singular/plural
    // e.g., flour
    private String item;

    public Ingredient() { }

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

    public boolean canAdd(Ingredient other) {
        return Objects.equals(item, other.getItem()) && Objects.equals(measure, other.getMeasure());
    }

    public void add(Ingredient other) {
        assert canAdd(other);
        this.value = this.value + other.getValue();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", value, measure, item);
    }
}
