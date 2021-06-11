package com.apptech.lava_retailer.list.country;

public class Country_list {

    String id;
    String name;
    String name_ar;
    String name_fr;
    String time;
    String currency;
    String currency_symbol;
    String active;
    String flag;

    public Country_list(String id, String name, String name_ar, String name_fr, String time, String currency, String currency_symbol, String active, String flag) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
        this.time = time;
        this.currency = currency;
        this.currency_symbol = currency_symbol;
        this.active = active;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getName_fr() {
        return name_fr;
    }

    public String getTime() {
        return time;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public String getActive() {
        return active;
    }

    public String getFlag() {
        return flag;
    }


    @Override
    public String toString() {
        return "Country_list{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", name_ar='" + name_ar + '\'' +
                ", name_fr='" + name_fr + '\'' +
                ", time='" + time + '\'' +
                ", currency='" + currency + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", active='" + active + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}

