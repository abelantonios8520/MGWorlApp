package com.abelsalcedo.mgworlapp.Model;

public class Cliente {
    private String id;
    private String username;
    private String userape;
    private String usernumber;
    private String imageURL;
    private String status;
    private String search;
    private String bio;

    public Cliente() {
    }

    public Cliente(String id, String username, String userape, String usernumber, String imageURL, String status, String search, String bio) {
        this.id = id;
        this.username = username;
        this.userape = userape;
        this.usernumber = usernumber;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserape() {
        return userape;
    }

    public void setUserape(String userape) {
        this.userape = userape;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
