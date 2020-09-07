package com.example.virusapp.ui.news;

public class News {
    private String title;
    private  String text;
    private int image_id;

    public News(String title, String text, int image_id)
    {
        this.title = title;
        this.text = text;
        this.image_id = image_id;
    }

    public String getText()
    {
        return text;
    }

    public String getTitle() { return title; }

    public int getImage_id() {
        return image_id;
    }
}
