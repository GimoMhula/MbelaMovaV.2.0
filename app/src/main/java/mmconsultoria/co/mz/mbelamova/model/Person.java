package mmconsultoria.co.mz.mbelamova.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import androidx.annotation.NonNull;
import mmconsultoria.co.mz.mbelamova.cloud.ImageHolder;
import mmconsultoria.co.mz.mbelamova.util.DateManipulator;

public class Person implements Parcelable, ImageHolder {
    private String firstName;
    private String id;
    private String email;
    private String phoneNumber;
    private String photoUri;
    private String driverId;
    private Place[] places;
    private String lastName;
    private long dateOfSignUp;
    private DateManipulator dateOfBirth;


    public Person() {
    }

    protected Person(Parcel in) {
        firstName = in.readString();
        id = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        photoUri = in.readString();
        driverId = in.readString();
        places = in.createTypedArray(Place.CREATOR);
        lastName = in.readString();
        dateOfSignUp = in.readLong();
        dateOfBirth = in.readParcelable(DateManipulator.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(photoUri);
        dest.writeString(driverId);
        dest.writeTypedArray(places, flags);
        dest.writeString(lastName);
        dest.writeLong(dateOfSignUp);
        dest.writeParcelable(dateOfBirth, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @NonNull
    @Override
    public String toString() {
        return "People{" + "name='" + firstName + '\'' + ", id='" + id + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", photoUri='" + photoUri + '\'' + ", driverId='" + driverId + '\'' + ", places=" + Arrays.toString(places) + '}';
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getDateOfSignUp() {
        return dateOfSignUp;
    }

    public void setDateOfSignUp(long dateOfSignUp) {
        this.dateOfSignUp = dateOfSignUp;
    }

    public DateManipulator getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateManipulator dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return firstName +" "+lastName;
    }
}
