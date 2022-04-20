package com.myroutine.myroutine;

import java.io.Serializable;

/**
 * Created by basmatebe on 4/12/17.
 */

public class Task implements Serializable {
    private int id;
    private String name, day, time;

    public Task(String name, String day, String time){
        this.name = name;
        this.day = day;
        this.time = time;
    }
    public  Task(){}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

