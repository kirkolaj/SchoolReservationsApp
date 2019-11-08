package com.example.schoolreservationsapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rooms implements Serializable {
    @SerializedName("id") // Name of JSON attribute. Used for Gson de-serialization
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("capacity")
    private int capacity;
    @SerializedName("remarks")
    private String remarks;


    public Rooms(){

    }

    public Rooms(int id, String name, String description, int capacity, String remarks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return name +" "+ description +" Amount of people: "+capacity;
    }
}
