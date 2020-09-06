package com.example.virusapp.ui.news;

public class News {
    private String text;
    private int image_id;

    public News(String text, int image_id)
    {
        this.text = text;
        this.image_id = image_id;
    }

    public String getText()
    {
        return text;
    }

    public int getImage_id() {
        return image_id;
    }
}
