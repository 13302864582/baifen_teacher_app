package com.ucuxin.ucuxin.tec.function.homework.view.tag;

import java.io.Serializable;

/**
 * 标签modle
 */
public class TagModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2684657309332033242L;


    private String color;
    private String content;
    private int type;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
