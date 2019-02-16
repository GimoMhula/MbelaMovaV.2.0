package mmconsultoria.co.mz.mbelamova.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Driver implements Parcelable {
    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };
    private String id;
    private String personId;
    private String personName;
    private String licenceNumber;
    private String licenceType;
    private Car car;

    protected Driver(Parcel in) {
        car = in.readParcelable(Car.class.getClassLoader());
        id = in.readString();
        personId = in.readString();
        personName = in.readString();
        licenceNumber = in.readString();
        licenceType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(car, flags);
        dest.writeString(id);
        dest.writeString(personId);
        dest.writeString(personName);
        dest.writeString(licenceNumber);
        dest.writeString(licenceType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }
}
