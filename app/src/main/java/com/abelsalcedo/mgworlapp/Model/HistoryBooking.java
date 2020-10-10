package com.abelsalcedo.mgworlapp.Model;

public class HistoryBooking {

    String idHistoryBooking;
    String idCliente;
    String idColaborador;
    String destination;
    String origin;
    String time;
    String km;
    String status;
    double originLat;
    double originLng;
    double destinationLat;
    double destinationLng;
    double calificationCliente;
    double calificationColaborador;
    long timestamp;

    public HistoryBooking() {

    }

    public HistoryBooking(String idHistoryBooking, String idCliente, String idColaborador, String destination, String origin, String time, String km, String status, double originLat, double originLng, double destinationLat, double destinationLng) {
        this.idHistoryBooking = idHistoryBooking;
        this.idCliente = idCliente;
        this.idColaborador = idColaborador;
        this.destination = destination;
        this.origin = origin;
        this.time = time;
        this.km = km;
        this.status = status;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdHistoryBooking() {
        return idHistoryBooking;
    }

    public void setIdHistoryBooking(String idHistoryBooking) {
        this.idHistoryBooking = idHistoryBooking;
    }

    public double getCalificationCliente() {
        return calificationCliente;
    }

    public void setCalificationCliente(double calificationCliente) {
        this.calificationCliente = calificationCliente;
    }

    public double getCalificationColaborador() {
        return calificationColaborador;
    }

    public void setCalificationColaborador(double calificationColaborador) {
        this.calificationColaborador = calificationColaborador;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(String idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }
}

