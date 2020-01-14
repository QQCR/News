package com.example.news.models;

public class Save {
    private int id;
    private String title;
    private String source;
    private String urlToImage;
    private String url;

    public Save(int id,String title,String source, String urlToImage,String url){
        this.id = id;
        this.title = title;
        this.source = source;
        this.urlToImage = urlToImage;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Save{" +
                "id=" + id + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
