package org.ds.maps.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapsConfig {
    private static final Log log = LogFactory.getLog(MapsConfig.class);
    @Value("${yandex_map.apiKey}")
    private String apiKey;

    @Bean("mapsAPIKey")
    public String mapsAPIKey() {
        return apiKey;
    }
}
