package com.scollcred.app.service.dto;

import com.scollcred.app.domain.Creditos;

import java.util.List;

public class CreditosDTO {
    List<Creditos> credtios;
    int length;

    public CreditosDTO(){}

    public CreditosDTO(List<Creditos> credtios, int length) {
        this.credtios = credtios;
        this.length = length;
    }

    public List<Creditos> getCredtios() {
        return credtios;
    }

    public void setCredtios(List<Creditos> credtios) {
        this.credtios = credtios;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "CreditosDTO{" +
            "credtios=" + credtios +
            ", length=" + length +
            '}';
    }
}
