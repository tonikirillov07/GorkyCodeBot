package org.ds.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  <b>Yandex Maps API JSON-class</b>, place position in String
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Point {
    public String pos;
}
