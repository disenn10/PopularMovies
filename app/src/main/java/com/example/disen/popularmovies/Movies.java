package com.example.disen.popularmovies;

/**
 * Created by disen on 10/18/2017.
 */

public class Movies {

    String title;
    String image;
    String rate = "";
    String releaseDate = "";
    String overview = "";
    String id = "";
    String key = "";
    String author="";
    String content="";

    public Movies(String title, String cover, String vote, String releaseDate, String sypnosis, String movieId, String key,String author, String content){
        this.title = title;
        this.image = cover;
        this.rate = vote;
        this.releaseDate = releaseDate;
        this.overview = sypnosis;
        this.id = movieId;
        this.key = key;
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getTheContent() {
        return content;
    }

    public String getKey() {
        return key;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getRate() {
        return rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }
}
