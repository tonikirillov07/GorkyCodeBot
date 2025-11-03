package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <b>Yandex Maps API JSON-class</b>, that contains position (lan & lon) of a place
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoObject {
    public Point Point;
}
