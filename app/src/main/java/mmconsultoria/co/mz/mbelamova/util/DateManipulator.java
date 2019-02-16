package mmconsultoria.co.mz.mbelamova.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

import io.reactivex.Observable;
import timber.log.Timber;

public class DateManipulator implements Parcelable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;


    public static DateManipulator from(Timestamp time) {
        DateManipulator dateManipulator = new DateManipulator();

        Observable.just(time)
                .subscribe(timestamp -> {

                }, throwable -> {
                    Timber.d(throwable);
                });

        return dateManipulator;
    }

    private DateManipulator() {

    }

    private DateManipulator(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    protected DateManipulator(Parcel in) {
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DateManipulator> CREATOR = new Creator<DateManipulator>() {
        @Override
        public DateManipulator createFromParcel(Parcel in) {
            return new DateManipulator(in);
        }

        @Override
        public DateManipulator[] newArray(int size) {
            return new DateManipulator[size];
        }
    };


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "DateManipulator{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
