package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response-object of YM API Geocoder
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexGeocodeResponse {
    public Response response;
}
