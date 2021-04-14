package com.apptech.myapplication.other;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageChange {
    private static LanguageChange ourInstance = null;
    Locale myLocale;



    public LanguageChange(Context context, String lang) {
        languageset(lang, context);
    }

    public void setLocale(String lang, Context context) {
        myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    private void languageset(String lang, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(lang));
        } else {
            config.locale = new Locale(lang);
        }
        resources.updateConfiguration(config, dm);
    }

}

