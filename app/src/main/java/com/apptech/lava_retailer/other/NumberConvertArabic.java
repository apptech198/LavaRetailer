package com.apptech.lava_retailer.other;

import android.util.Log;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public  class NumberConvertArabic {



    public  String  NumberConvertArabic(double nub) throws NumberFormatException{
        NumberFormat nf1 = NumberFormat.getInstance(new Locale("ar","AE"));
        return  nf1.format(nub);
    }

    public  String  arabicNumberCovert (double nub) throws NumberFormatException{
        NumberFormat nf1 = NumberFormat.getInstance(new Locale("eb","US"));
        return  nf1.format(nub);
    }

    public String GetCurreny(String Country_Locale){

        String symbol = "";
        try {
            Locale locale = new Locale("EN", Country_Locale);
            Currency currency= Currency.getInstance(locale);
            symbol = currency.getSymbol();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            Locale locale = new Locale("EN", "GB");
            Currency currency= Currency.getInstance(locale);
            symbol = currency.getSymbol();
        }
        return  symbol;
    }


}
