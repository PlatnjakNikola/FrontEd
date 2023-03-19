package com.example.springsocial.model;

import javax.persistence.*;

@Entity
@Table(name = "mockData")
public class WebSites {
    @Id
    private Long id;

    private String domainName;

    private int rating;

    public Long getId() {
        return id;
    }

    public String getDomainName() {
        return domainName;
    }

    public int getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
