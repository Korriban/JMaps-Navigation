
package de.cirrus.jmaps.gsonclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance_ {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Distance_ withText(String text) {
        this.text = text;
        return this;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Distance_ withValue(int value) {
        this.value = value;
        return this;
    }

}
