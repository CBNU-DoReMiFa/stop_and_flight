package com.doremifa.stop_and_flight.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CurTime {
    private Date date;
    private String Year;
    private String Month;
    private String Day;
    private String Hour;
    private String Minute;

    public CurTime()
    {
        long now = System.currentTimeMillis();
        date = new Date(now);

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

        Year = CurYearFormat.format(date);
        Month = CurMonthFormat.format(date);
        Day = CurDayFormat.format(date);
        Hour = CurHourFormat.format(date);
        Minute = CurMinuteFormat.format(date);
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getIntMonth() {
        return Integer.parseInt(Month);
    }

    public int getIntDay() {
        return Integer.parseInt(Day);
    }

    public int getIntHour() {
        return Integer.parseInt(Hour);
    }

    public int getIntMinute() {
        return Integer.parseInt(Minute);
    }

    public int getIntYear() {
        return Integer.parseInt(Year);
    }

    public String getWeekset() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public String getDateset() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    public ArrayList<String> getCurWeek(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();

        ArrayList<String> week = new ArrayList<>();

        for (int i = 2; i < 8; i++){
            c.set(Calendar.DAY_OF_WEEK, i);
            week.add(formatter.format(c.getTime()));
        }

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.DATE,7);
        week.add(formatter.format(c.getTime()));

        return week;
    }

    public static String getCurMonday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return formatter.format(c.getTime());
    }

    //현재 날짜 일요일
    public static String getCurSunday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.add(Calendar.DATE,7);

        return formatter.format(c.getTime());
    }

    //현재 날짜 주차
    public static String getWeek(){
        Calendar c = Calendar.getInstance();
        String week = String.valueOf(c.get(Calendar.WEEK_OF_MONTH));
        return week;
    }

    //특정 년,월,주 차에 월요일 구하기
    public static String getMonday(String yyyy,String mm, String wk){

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        int y=Integer.parseInt(yyyy);
        int m=Integer.parseInt(mm)-1;
        int w=Integer.parseInt(wk);

        c.set(Calendar.YEAR, y);
        c.set(Calendar.MONTH, m);
        c.set(Calendar.WEEK_OF_MONTH, w);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return formatter.format(c.getTime());
    }

    //특정 년,월,주 차에 일요일 구하기

    public static String getSunday(String yyyy,String mm, String wk){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();

        int y=Integer.parseInt(yyyy);
        int m=Integer.parseInt(mm)-1;
        int w=Integer.parseInt(wk);

        c.set(Calendar.YEAR, y);
        c.set(Calendar.MONTH, m);
        c.set(Calendar.WEEK_OF_MONTH, w);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.add(Calendar.DATE,7);
        return formatter.format(c.getTime());
    }


    public static List getDatesBetweenUsingJava7(Date startDate, Date endDate) {
        List datesInRange = new ArrayList<>();
        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(dateformat.format(result));
            calendar.add(Calendar.DATE, 1);
        }
        Date result = calendar.getTime();
        datesInRange.add(dateformat.format(result));

        return datesInRange;
    }

    private static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }


}