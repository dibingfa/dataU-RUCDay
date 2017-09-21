package com.flash.dataU.test.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月19日 15时39分
 */
@Entity
@Table(name = "city",
    schema = "world",
    catalog = "")
public class CityEntity {
    private int id;
    private String name;
    private String district;
    private int population;

    @Id
    @Column(name = "ID",
        nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name",
        nullable = false,
        length = 35)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "District",
        nullable = false,
        length = 20)
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Basic
    @Column(name = "Population",
        nullable = false)
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CityEntity entity = (CityEntity)o;

        if (id != entity.id)
            return false;
        if (population != entity.population)
            return false;
        if (name != null ? !name.equals(entity.name) : entity.name != null)
            return false;
        if (district != null ? !district.equals(entity.district) : entity.district != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + population;
        return result;
    }
}
