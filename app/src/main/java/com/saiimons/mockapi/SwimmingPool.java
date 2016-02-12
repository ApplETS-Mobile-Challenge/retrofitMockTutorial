package com.saiimons.mockapi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by saiimons on 16-02-11.
 */
public class SwimmingPool {
    public String name;
    public String address;

    public SwimmingPool(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public static class Parser implements JsonDeserializer<SwimmingPool> {

        @Override
        public SwimmingPool deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject properties = json.getAsJsonObject().getAsJsonObject("properties");
            return new SwimmingPool(
                    properties.get("NOM").getAsString(),
                    properties.get("ADRESSE").getAsString()
            );
        }
    }
}
