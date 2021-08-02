package com.example.stop_and_flight.model;

import androidx.core.app.NavUtils;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private String depart_time;
    private String arrive_time;
    private String date;
    private String todo;
    private String wait;
    private int id;
    private Map<String, Boolean> stars = new HashMap<>();

    public Ticket() {
        this.depart_time = null;
        this.arrive_time = null;
        this.date = null;
        this.todo = null;
        this.id = 0;
        this.wait = "true";
    }

    public Ticket(String depart_time, String arrive_time, String todo, int id, String wait) {
        this.depart_time = depart_time;
        this.arrive_time = arrive_time;
        this.todo = todo;
        this.id = id;
        this.wait = wait;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWait() {
        return wait;
    }

    public void setWait(String wait) {
        this.wait = wait;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("depart_time", depart_time);
        result.put("arrive_time", arrive_time);
        result.put("id", id);
        result.put("wait", wait);
        result.put("todo", todo);
        result.put("stars", stars);

        return result;
    }
}