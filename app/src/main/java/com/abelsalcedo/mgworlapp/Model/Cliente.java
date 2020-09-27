package com.abelsalcedo.mgworlapp.Model;

public class Cliente {
    String id;
    String username;
    String ape;
    String telf;
    String email;
    String status;
    String search;

    public Cliente(String id, String status, String username, String ape, String telef) {
    }

    public Cliente(String id, String username, String ape, String telf, String email, String status, String search) {
        this.id = id;
        this.username = username;
        this.ape = ape;
        this.telf = telf;
        this.email = email;
        this.status = status;
        this.search = search;
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

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
