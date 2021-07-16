package com.example.opencvexample;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Favourites {
    private Map<Class<?>,Object> map = new HashMap<>();

    public <T> void putFavourite(Class<T> type, T object){
        map.put(Objects.requireNonNull(type),object);
    }

    public <T> T getFavourites(Class<T> type){
        return type.cast(map.get(type));
    }
}
