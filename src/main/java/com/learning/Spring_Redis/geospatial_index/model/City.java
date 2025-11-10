package com.learning.Spring_Redis.geospatial_index.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class City {

    @Id
    private int id;
    private String name;
    private int state_id;
    private String state_name;
    private int country_id;
    private String country_code;
    private String country_name;
    private long latitude;
    private long longitude;
    private String wikiDataId;
}
