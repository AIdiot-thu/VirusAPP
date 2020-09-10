package com.example.virusapp.data;

import java.util.ArrayList;

public class LocalData {
    String country="",province="",county="";
    int yearBegin,mouthBegin,dayBegin;
    DailyData lastDayData;

    @Override
    public String toString() {
        return "LocalData{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", county='" + county + '\'' +
                ", yearBegin=" + yearBegin +
                ", mouthBegin=" + mouthBegin +
                ", dayBegin=" + dayBegin +
                ", lastDayData='" + lastDayData + '\'' +
                '}';
    }
}
