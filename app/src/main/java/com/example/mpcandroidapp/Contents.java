package com.example.mpcandroidapp;

import java.util.ArrayList;

public class Contents {
    private final ArrayList<String> items = new ArrayList<>();
    private static final Contents instance = new Contents();
    public static Contents getInstance () {
        return instance;
    }

    public ArrayList<String> getItems(){
        return items;
    }

    private Contents(){
        items.add("adobe");
        items.add("bead");
        items.add("botanical");
        items.add("ceramics");
        items.add("charcoal");
        items.add("pipe");
        items.add("dendro_sample");
        items.add("eggshell");
        items.add("faunal_bone");
        items.add("figurine");
        items.add("fire_cracked_rock");
        items.add("flotation_sample");
        items.add("groundstone");
        items.add("historic_glass");
        items.add("historic_metal");

        items.add("human_bone");
        items.add("lithic");
        items.add("mineral");
        items.add("miscellaneous");
        items.add("misc_stone");
        items.add("pollen_sample");
        items.add("projectile_point");
        items.add("shell");
        items.add("stone_tool");
        items.add("turquoise");
        items.add("wood");
        items.add("worked_bone");
        items.add("worked_ceramic");
        items.add("worked_stone");
    }

}

