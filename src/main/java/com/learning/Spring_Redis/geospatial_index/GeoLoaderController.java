package com.learning.Spring_Redis.geospatial_index;

import com.learning.Spring_Redis.geospatial_index.model.City;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("geo")
@RequiredArgsConstructor
public class GeoLoaderController {

    private final GeoLoaderService geoLoaderService;

    @GetMapping("location")
    public ResponseEntity<?> findNearbyCities(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam int radius){
        return ResponseEntity.ok(geoLoaderService.findNearbyCities(longitude,latitude,radius));
    }

}
