package com.abelsalcedo.mgworlapp.pojo;

public class Pedidos {

    private String email;
    private String gustos;
    private String extras;

    public Pedidos() {
    }

    public Pedidos(String email, String gustos, String extras) {
        this.email = email;
        this.gustos = gustos;
        this.extras = extras;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGustos() {
        return gustos;
    }

    public void setGustos(String gustos) {
        this.gustos = gustos;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }
}
