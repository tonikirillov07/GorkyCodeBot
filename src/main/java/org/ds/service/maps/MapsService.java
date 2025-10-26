package org.ds.service.maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ds.maps.CoordinatesResponse;
import org.ds.maps.FeatureMember;
import org.ds.maps.YandexGeocodeResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

@Service
public class MapsService {
    private final String apiKey;

    public MapsService(@Qualifier("mapsAPIKey") String apiKey) {
        this.apiKey = apiKey;
    }

    public @Nullable CoordinatesResponse getCoordinatesByAddress(@NotNull String placeName) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String address = URLEncoder.encode(placeName, StandardCharsets.UTF_8);
        String url = "https://geocode-maps.yandex.ru/1.x?geocode=" + address + "&format=json&apikey=" + apiKey;

        HttpResponse<String> response = getResponse(client, url);

        if (response.statusCode() != 200) {
            return new CoordinatesResponse(false, response.body());
        }

        String json = response.body();
        ObjectMapper mapper = new ObjectMapper();
        YandexGeocodeResponse yandexGeocodeResponse = mapper.readValue(json, YandexGeocodeResponse.class);

        if (!checkYandexGeocodeResponse(yandexGeocodeResponse))
            return new CoordinatesResponse(false, "Failed to get coordinated from request");

        FeatureMember member = yandexGeocodeResponse.response().geoObjectCollection().featureMember()[0];
        if (checkGeoObject(member)) {
            String[] coords = member.geoObject().point().position().split(" ");

            if (coords.length == 2) {
                float lat = Float.parseFloat(coords[1]);
                float lon = Float.parseFloat(coords[0]);

                return new CoordinatesResponse(true, null, lat, lon);
            }

            return new CoordinatesResponse(false, "Coordinates array shorter than 2. Coords: %s".formatted(Arrays.toString(coords)));
        }

        return new CoordinatesResponse(false, "Feature member is invalid");
    }

    private boolean checkGeoObject(@NotNull FeatureMember featureMember) {
        return featureMember.geoObject() != null && featureMember.geoObject().point() != null;
    }

    private boolean checkYandexGeocodeResponse(YandexGeocodeResponse yandexGeocodeResponse) {
        return yandexGeocodeResponse != null &&
                yandexGeocodeResponse.response() != null &&
                yandexGeocodeResponse.response().geoObjectCollection() != null &&
                yandexGeocodeResponse.response().geoObjectCollection().featureMember() != null &&
                yandexGeocodeResponse.response().geoObjectCollection().featureMember().length > 0;
    }

    private HttpResponse<String> getResponse(@NotNull HttpClient client, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
