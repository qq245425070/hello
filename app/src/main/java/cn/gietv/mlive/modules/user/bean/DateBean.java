package cn.gietv.mlive.modules.user.bean;

/**
 * author：steven
 * datetime：15/10/13 17:01
 *
 */
public class DateBean {
    private int date;
    private int seconds;
    private int hours;
    private int month;
    private int year;
    private int timezoneOffset;
    private int minutes;
    private long time;
    private int day;

    public void setDate(int date) {
        this.date = date;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDate() {
        return date;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public int getMinutes() {
        return minutes;
    }

    public long getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }
}
