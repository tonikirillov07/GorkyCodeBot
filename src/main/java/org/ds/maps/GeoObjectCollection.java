package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ds.service.maps.MapsService;

/**
 *  <b>Yandex Maps API JSON-class</b>, that contains main collections of geo objects
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoObjectCollection {
    public FeatureMember[] featureMember;
}
