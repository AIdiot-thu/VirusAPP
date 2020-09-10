package com.example.virusapp.data;

public class NewsData{
    //since we can only get newsdata by news summary,attributes like id and type are not included
    public String time,title,source,content;

    @Override
    public String toString() {
        return "NewsData{" +
                "time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}