package com.cms.helpdesk.common.reuse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConvertDate {
    public static Date setTimeToZero(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String dateString = sdf.format(date);
            return sdf.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Date setTimeToLastSecond(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return calendar.getTime();
        }
        return null;
    }

    public static String formatISO8601(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return sdf.format(date);
    }

    public static String indonesianFormat(Date dt) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
        if (dt != null) {
            String dateToString = df.format(dt);
            return dateToString;
        }
        return null;
    }
}
