package com.guncy.android.cardreader.model;

import android.content.Context;

import com.guncy.android.cardreader.lib.FeliCa;

/**
 * Created by dongri on 2016/12/18.
 */

public class Card {
    public String date;
    public String number;
    public String payment;
    public String kind;
    public String device;
    public String action;
    public String inLine;
    public String inStation;
    public String outLine;
    public String outStation;
    public String balance;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getInLine() {
        return inLine;
    }

    public void setInLine(String inLine) {
        this.inLine = inLine;
    }

    public String getInStation() {
        return inStation;
    }

    public void setInStation(String inStation) {
        this.inStation = inStation;
    }

    public String getOutLine() {
        return outLine;
    }

    public void setOutLine(String outLine) {
        this.outLine = outLine;
    }

    public String getOutStation() {
        return outStation;
    }

    public void setOutStation(String outStation) {
        this.outStation = outStation;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public static Card getCard(Context context, FeliCa felica) {
        Card card = new Card();
        card.setDate((2000 + felica.year) + "年" + felica.month + "月" + felica.day + "日");
        card.setNumber(String.valueOf(felica.seqNo));
        card.setPayment("");
        card.setKind(felica.kind);
        card.setDevice(felica.device);
        card.setAction(felica.action);
        Station in = Station.getStation(context, felica.inLine, felica.inStation);
        card.setInLine(in.getLineName());
        card.setInStation(in.getStationName());
        Station out = Station.getStation(context, felica.outLine, felica.outStation);
        card.setOutLine(out.getLineName());
        card.setOutStation(out.getStationName());
        card.setBalance(String.valueOf(felica.remain));
        return card;
    }

}
