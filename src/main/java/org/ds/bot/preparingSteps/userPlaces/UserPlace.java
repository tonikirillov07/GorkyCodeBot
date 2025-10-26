package org.ds.bot.preparingSteps.userPlaces;

public record UserPlace(String name,
                        String description,
                        String distance,
                        String duration,
                        Float lat,
                        Float lon) {
}
