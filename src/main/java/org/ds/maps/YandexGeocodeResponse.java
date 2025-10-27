package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ds.service.maps.MapsService;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexGeocodeResponse {
    public Response response;
}
