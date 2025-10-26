package org.ds.service.maps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class MapsService {
    private static final Log log = LogFactory.getLog(MapsService.class);

    private final String apiKey;

    public MapsService(@Qualifier("mapsAPIKey") String apiKey) {
        this.apiKey = apiKey;
    }

    public @Nullable String getCoordinatesByAddress(@NotNull String address) throws IOException {
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            URL url = new URI("https://geocode-maps.yandex.ru/v1/?apikey=%s&geocode=%s&format=json"
                    .formatted(apiKey, encodedAddress)).toURL();

            URLConnection urlConnection = url.openConnection();

            inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder result = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            log.error("Exception occurred: %s".formatted(e.toString()));
        } finally {
            if (inputStream !=  null)
                inputStream.close();

            if (reader != null)
                reader.close();
        }

        return null;
    }
}
