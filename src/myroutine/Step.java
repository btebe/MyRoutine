package com.myroutine.myroutine;

/**
 * Created by basmatebe on 4/12/17.
 */

public class Step {
    private int id;
    private int taskID;
    private String name, audio, timer;
    private String pic;

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }



    public Step(String name, String audio, String timer, String pic, int taskID) {

        this.taskID = taskID;
        this.name = name;
        this.audio = audio;
        this.timer = timer;
        this.pic = pic;
    }

    public Step() {}

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", taskID=" + taskID +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", audio='" + audio + '\'' +
                ", timer='" + timer + '\'' +
                '}';
    }
}
