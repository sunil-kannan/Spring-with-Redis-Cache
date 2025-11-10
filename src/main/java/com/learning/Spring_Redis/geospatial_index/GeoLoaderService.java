package com.learning.Spring_Redis.geospatial_index;

import com.learning.Spring_Redis.geospatial_index.model.City;
import com.learning.Spring_Redis.geospatial_index.repository.CityRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeoLoaderService {

    private final RedisTemplate<String, String> redisTemplate;
    private final CityRepository cityRepository;

    public GeoLoaderService(RedisTemplate<String, String> redisTemplate, CityRepository cityRepository) {
        this.redisTemplate = redisTemplate;
        this.cityRepository = cityRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadGeoDataAfterStartup() {
        new Thread(this::loadAllCities).start(); // non-blocking
    }

    private void loadAllCities() {
        int page = 0;
        int size = 5000;

        Page<City> citiesPage;

        do {
            Pageable pageable = PageRequest.of(page, size);
            citiesPage = cityRepository.findAll(pageable);

            loadLocations(citiesPage.getContent());
            page++;
//            if(page > 3){
//                break;
//            }

        } while (citiesPage.hasNext());

    }

    public void loadLocations(List<City> locations) {
        String key = "locations";


        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (City loc : locations) {
                Point point = new Point(loc.getLongitude(), loc.getLatitude());
                connection.geoAdd(
                        key.getBytes(),
                        new RedisGeoCommands.GeoLocation<>(loc.getName().getBytes(), point)
                );
            }
            return null;
        });

        System.out.println("✅ Loaded " + locations.size() + " locations into Redis GEO.");
    }


    public GeoResults<RedisGeoCommands.GeoLocation<String>> findNearbyCities(double longitude, double latitude, double radiusKm) {
        Point center = new Point(longitude, latitude);
        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);
        System.out.println(LocalDateTime.now());
        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius("locations", new Circle(center, radius),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeCoordinates()
                                .includeDistance());

        System.out.println(LocalDateTime.now());
//        if (results != null) {
//            results.getContent().forEach(r -> {
//                System.out.println(
//                        r.getContent().getName() + " — " +
//                                r.getDistance().getValue() + " " + r.getDistance().getUnit()
//                );
//            });
//        }
        return results;
    }
}
