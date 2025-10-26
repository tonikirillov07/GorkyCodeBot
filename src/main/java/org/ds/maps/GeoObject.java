package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ds.Test;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoObject(Point point) {
}
