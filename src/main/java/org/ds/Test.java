package org.ds;

import org.ds.maps.CoordinatesResponse;
import org.ds.service.maps.MapsService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.ds");
        MapsService mapsService = context.getBean(MapsService.class);

        CoordinatesResponse coordinatesResponse = mapsService.getCoordinatesByAddress("Москва, Кремль");
        System.out.println(coordinatesResponse);
    }


}
