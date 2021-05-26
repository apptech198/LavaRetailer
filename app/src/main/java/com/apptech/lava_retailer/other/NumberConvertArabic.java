package com.apptech.lava_retailer.other;

import java.text.NumberFormat;
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

}
