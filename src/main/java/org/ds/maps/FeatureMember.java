package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <b>Yandex Maps API JSON-class</b>, that contains info about a place
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureMember {
    public GeoObject GeoObject;
}
