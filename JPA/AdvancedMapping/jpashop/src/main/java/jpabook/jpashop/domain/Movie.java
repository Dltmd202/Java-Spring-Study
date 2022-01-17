package jpabook.jpashop.domain;

import javax.persistence.Entity;

@Entity
public class Movie extends Item{

    private String director;
    private String authr;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAuthr() {
        return authr;
    }

    public void setAuthr(String authr) {
        this.authr = authr;
    }
}
