package org.ds;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ds.service.maps.MapsService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Objects;

public class Test {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.ds");
        MapsService mapsService = context.getBean(MapsService.class);

        JsonElement jsonElement = JsonParser.parseString(Objects.requireNonNull(mapsService.getCoordinatesByAddress("Стрелка, Нижний Новгород, Россия")));
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject response = jsonObject.getAsJsonObject("response");
        JsonObject collection = response.getAsJsonObject("GeoObjectCollection");
        System.out.println(response);

        JsonArray jsonArray = collection.getAsJsonArray("featureMember");
        jsonArray.asList().forEach(element -> System.out.println(element));
    }
}
