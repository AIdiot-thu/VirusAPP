package com.example.virusapp.data;

public class NewsSumData {
    public String id,type,title,lang;
    public int timeYear,timeMouth,timeDay;

    @Override
    public String toString() {
        return "NewsSumData{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", lang='" + lang + '\'' +
                ", timeYear=" + timeYear +
                ", timeMouth=" + timeMouth +
                ", timeDay=" + timeDay +
                '}';
    }
}
