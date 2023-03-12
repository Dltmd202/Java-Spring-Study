package com.hello.java.test;

import lombok.Getter;

@Getter
public class Study {
    private StudyStatus status = StudyStatus.DRAFT;

    private int limit;

    private String name;

    public Study(int limit) {
        if(limit < 0)
            throw new IllegalArgumentException();
        this.limit = limit;
    }

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    public int getLimit(){
        return limit;
    }
    public StudyStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }
}
