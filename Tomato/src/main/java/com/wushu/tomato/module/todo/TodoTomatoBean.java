package com.wushu.tomato.module.todo;

import java.io.Serializable;

/**
 * 要做任务实体Bean
 */
public class TodoTomatoBean implements Serializable {
    private String todoName;
    private Float tomatoNum;
    //单位
    private int unit;
    //进度 单位ms
    private float progress;
    //进度最大的值 progressMax = tomatoNum * unit
    private float progressMax;
    //显示的内容 progressDes =DateUtil.msToDate((int) progress)
    private String progressDes;

    public void setProgressMax(float progressMax) {
        this.progressMax = progressMax;
    }

    public void setProgressDes(String progressDes) {
        this.progressDes = progressDes;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getProgressMax() {
        return progressMax;
    }

    public String getProgressDes() {
        return progressDes;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public TodoTomatoBean(String todoName, float rating) {
        this.todoName = todoName;
        this.tomatoNum = rating;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public Float getTomatoNum() {
        return tomatoNum;
    }

    public void setTomatoNum(Float tomatoNum) {
        this.tomatoNum = tomatoNum;
    }

    @Override
    public String toString() {
        return "TodoTomatoBean{" +
                "todoName='" + todoName + '\'' +
                ", tomatoNum=" + tomatoNum +
                ", unit=" + unit +
                ", progress=" + progress +
                ", progressMax=" + progressMax +
                ", progressDes='" + progressDes + '\'' +
                '}';
    }
}
