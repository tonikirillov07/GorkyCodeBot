package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MapsResponse(GeoObjectCollection geoObjectCollection) {
}
