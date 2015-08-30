package com.dsv.tourism.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.dsv.tourism.activities.MainActivity;
import com.dsv.tourism.activities.SettingsActivity;

import java.util.Locale;

/**
 * Created by Vince on 30.08.2015.
 */
public class LocalisationHelper {

    public static void setLocale(Context c, String locale) {
        Resources res = c.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale =  new Locale(locale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(c, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(refresh);

        //Activity activity = (Activity) c;
        //activity.finish();
    }
}
