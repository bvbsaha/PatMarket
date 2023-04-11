package com.example.patmarket.security;


public class LinkDto {

    public LinkDto(String namePage, String link) {
        this.namePage = namePage;
        this.link = link;
        url = "no data";
    }

    public LinkDto(String namePage, String link, String url) {
        this.namePage = namePage;
        this.link = link;
        this.url = url;
    }

    public LinkDto() {
    }

    private String namePage;
    private String link;
    private String url;
}
