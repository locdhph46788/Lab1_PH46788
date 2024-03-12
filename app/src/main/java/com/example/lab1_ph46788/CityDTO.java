package com.example.lab1_ph46788;

public class CityDTO {
    private String id;
    private String name;
    private int population;
    private String country;

    public CityDTO() {
    }

    public CityDTO(String id, String name, int population, String country) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
