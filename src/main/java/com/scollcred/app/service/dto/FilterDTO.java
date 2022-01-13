package com.scollcred.app.service.dto;

public class FilterDTO {
    String cliente;
    String mutual;

    public String getCliente() {
        return cliente;
    }

    public String getMutual() {
        return mutual;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setMutual(String mutual) {
        this.mutual = mutual;
    }

    @Override
    public String toString() {
        return "FilterDTO{" +
            "cliente='" + cliente + '\'' +
            ", mutual='" + mutual + '\'' +
            '}';
    }
}
