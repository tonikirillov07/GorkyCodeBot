package org.ds.service.maps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.exceptions.GettingResponseException;
import org.ds.exceptions.JSONProcessingException;
import org.ds.maps.CoordinatesResponse;
import org.ds.maps.FeatureMember;
import org.ds.maps.YandexGeocodeResponse;
import org.jetbrains.annotations.NotNull;
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
    private static final Log log = LogFactory.getLog(MapsService.class);
    private final String apiKey;

    public MapsService(@Qualifier("mapsAPIKey") String apiKey) {
        this.apiKey = apiKey;
    }

    public @NotNull CoordinatesResponse getCoordinatesByAddress(@NotNull String placeName) {
        log.info("Finding coordinates for address: %s".formatted(placeName));

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String address = URLEncoder.encode(placeName, StandardCharsets.UTF_8);
        String url = "https://geocode-maps.yandex.ru/1.x?geocode=" + address + "&format=json&apikey=" + apiKey;

        HttpResponse<String> response;
        try {
            response = getResponse(client, url);
        } catch (IOException | InterruptedException e) {
            throw new GettingResponseException(e);
        }

        if (response.statusCode() != 200)
            return new CoordinatesResponse(false, response.body());

        String json = response.body();
        ObjectMapper mapper = new ObjectMapper();
        YandexGeocodeResponse yandexGeocodeResponse;

        try {
            yandexGeocodeResponse = mapper.readValue(json, YandexGeocodeResponse.class);
        } catch (JsonProcessingException e) {
            throw new JSONProcessingException(e);
        }

        if (!checkYandexGeocodeResponse(yandexGeocodeResponse))
            return new CoordinatesResponse(false, "Failed to get coordinates from request");

        FeatureMember member = yandexGeocodeResponse.response.GeoObjectCollection.featureMember[yandexGeocodeResponse.response.GeoObjectCollection.featureMember.length - 1];
        if (checkGeoObject(member)) {
            String[] coords = member.GeoObject.Point.pos.split(" ");

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
        return featureMember.GeoObject != null && featureMember.GeoObject.Point != null;
    }

    private boolean checkYandexGeocodeResponse(YandexGeocodeResponse yandexGeocodeResponse) {
        return yandexGeocodeResponse != null &&
                yandexGeocodeResponse.response != null &&
                yandexGeocodeResponse.response.GeoObjectCollection != null &&
                yandexGeocodeResponse.response.GeoObjectCollection.featureMember != null &&
                yandexGeocodeResponse.response.GeoObjectCollection.featureMember.length > 0;
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
