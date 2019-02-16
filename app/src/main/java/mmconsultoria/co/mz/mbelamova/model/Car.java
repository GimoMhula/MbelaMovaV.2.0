package mmconsultoria.co.mz.mbelamova.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {
    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
    private String Maker;
    private String model;
    private String color;
    private String year;
    private String lot;
    private String type;
    private String licencePlate;
    private String[] images;

    protected Car(Parcel in) {
        Maker = in.readString();
        model = in.readString();
        color = in.readString();
        year = in.readString();
        lot = in.readString();
        type = in.readString();
        licencePlate = in.readString();
        images = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Maker);
        dest.writeString(model);
        dest.writeString(color);
        dest.writeString(year);
        dest.writeString(lot);
        dest.writeString(type);
        dest.writeString(licencePlate);
        dest.writeStringArray(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMaker() {
        return Maker;
    }

    public void setMaker(String maker) {
        Maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
