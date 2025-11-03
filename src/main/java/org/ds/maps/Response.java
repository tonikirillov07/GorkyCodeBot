package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ds.service.maps.MapsService;

/**
 * <b>Yandex Maps API JSON-class</b>, that contains response of YM API Geocoder
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    public GeoObjectCollection GeoObjectCollection;
}
