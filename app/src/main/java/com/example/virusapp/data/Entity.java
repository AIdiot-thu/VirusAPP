package com.example.virusapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    public String label;
    public double hot;
    public String info;//baidu or wiki
    HashMap<String,String> properties;
    ArrayList<Relation> relations;
    @Override
    public String toString(){
        return "Entity{" +
                "label='" + label + '\'' +
                ", hot='" + hot + '\'' +
                '}';
    }
}
