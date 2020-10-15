package com.abelsalcedo.mgworlapp.Model;

public class Cliente {
    String id;
    String name;
    String ape;
    String telf;
    String email;
    private String status;
    private String imageURL;
    private String pedido;

    public Cliente() {
    }

    public Cliente(String id, String name, String ape, String telf, String email, String status, String imageURL, String pedido) {
        this.id = id;
        this.name = name;
        this.ape = ape;
        this.telf = telf;
        this.email = email;
        this.status = status;
        this.imageURL = imageURL;
        this.pedido = pedido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPedido(){return pedido;}

    public void setPedido(String pedido){this.pedido = pedido;}
}