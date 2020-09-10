package com.example.virusapp.data;

class DailyData{
    public int confirmed,suspected,cured,dead;

    @Override
    public String toString(){
        return "DailyData{" +
                "confirmed='" + confirmed + '\'' +
                ", suspected='" + suspected + '\'' +
                ", cured='" + cured+ '\'' +
                ", dead='" + dead + '\'' +
                '}';
    }
}
