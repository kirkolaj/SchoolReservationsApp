package com.example.schoolreservationsapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reservations implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("fromTime")
    private int fromTime;
    @SerializedName("toTime")
    private int toTime;
    @SerializedName("userId")
    private String userId;
    @SerializedName("purpose")
    private String purpose;
    @SerializedName("roomId")
    private int roomId;

    public Reservations(int id, int fromTime, int toTime, String userId, String purpose, int roomId) {
        this.id = id;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.userId = userId;
        this.purpose = purpose;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {

        return "from time: " + fromTime + " to time: " + toTime + " RoomId: " + roomId + " Puspose: " + purpose;
    }

}


