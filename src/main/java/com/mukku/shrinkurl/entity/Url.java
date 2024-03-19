package com.mukku.shrinkurl.entity;

import jakarta.persistence.*;    

@Entity(name = "url")
@Table(name = "url")
public class Url {

    @Id
    @Column(name = "user")
    private String user;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "usage")
    private int usage;

    @Column(name ="id")
    private long id;

    public Url() {}

    public Url(Long id, String user, String fullUrl, String shortUrl, int usage) {
        this.user = user;
        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
        this.usage = usage;
        this.id = id; 
    }

    public Url(String fullUrl) {
        this.fullUrl = fullUrl;
    }
    
    public long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + user +
                ", fullUrl='" + fullUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                '}';
    }
}
