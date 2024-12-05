package com.pasynekmichal.service;

import com.pasynekmichal.model.Rate;

import java.util.List;

public class NbpResponse {
    private String table;
    private String no;
    private String effectiveDate;
    private List<Rate> rates;

    public NbpResponse() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
