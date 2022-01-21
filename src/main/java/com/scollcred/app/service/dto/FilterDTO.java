package com.scollcred.app.service.dto;

public class FilterDTO {
    private String cliente;
    private String mutual;
    private int page;
    private int limit = 10;

    public FilterDTO(){
        this.cliente = "";
        this.mutual = "";
        this.page = 0;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMutual() {
        return mutual;
    }

    public void setMutual(String mutual) {
        this.mutual = mutual;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "FilterDTO{" +
            "cliente='" + cliente + '\'' +
            ", mutual='" + mutual + '\'' +
            ", page=" + page +
            ", limit=" + limit +
            '}';
    }
}
