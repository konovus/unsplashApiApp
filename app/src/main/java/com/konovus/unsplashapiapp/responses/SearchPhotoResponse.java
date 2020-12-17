package com.konovus.unsplashapiapp.responses;

import com.konovus.unsplashapiapp.models.Photo;

import java.util.List;

public class SearchPhotoResponse {

    private int total;
    private int total_pages;
    private List<Photo> results;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Photo> getResults() {
        return results;
    }

    public void setResults(List<Photo> results) {
        this.results = results;
    }
}
