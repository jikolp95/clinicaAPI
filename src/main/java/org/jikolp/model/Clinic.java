package org.jikolp.model;

import java.util.*;
/**
 *Описание шаблона клиники
 */
public class Clinic {
    //айди клиники
    private int clinic_id;
    //название клиники
    private String name;
    //описание
    private String description;
    //контактный номер
    private String phone;
    //адрес
    private String address;
    //широта
    private float lat;
    //долгота
    private float lon;
    //список фотографий клиники
    private List<Image> images;
    //список докторов клиники
    private List<Doctor> doctors;
    private int city_id;


    public Clinic() {
    }

    public Clinic(int clinic_id, String name, String description, String phone, String address, float lat, float lon, List<Image> images, List<Doctor> doctors, int city_id) {
        this.clinic_id = clinic_id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.images = images;
        this.doctors = doctors;
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public int getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }
}
