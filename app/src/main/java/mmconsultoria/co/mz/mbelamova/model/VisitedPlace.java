package mmconsultoria.co.mz.mbelamova.model;

import android.os.Parcel;
import android.os.Parcelable;

import mmconsultoria.co.mz.mbelamova.cloud.ImageHolder;

public class VisitedPlace implements Parcelable, ImageHolder {
    public static final Creator<VisitedPlace> CREATOR = new Creator<VisitedPlace>() {
        @Override
        public VisitedPlace createFromParcel(Parcel in) {
            return new VisitedPlace(in);
        }

        @Override
        public VisitedPlace[] newArray(int size) {
            return new VisitedPlace[size];
        }
    };
    private String visitedPlace;
    private String driverName;
    private String driverId;
    private String driverPhotoUri;
    private String clientName;
    private String clientId;
    private String clientPhotoUri;
    private Place place;

    protected VisitedPlace(Parcel in) {
        place = in.readParcelable(Place.class.getClassLoader());
        visitedPlace = in.readString();
        driverName = in.readString();
        driverId = in.readString();
        driverPhotoUri = in.readString();
        clientName = in.readString();
        clientId = in.readString();
        clientPhotoUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(place, flags);
        dest.writeString(visitedPlace);
        dest.writeString(driverName);
        dest.writeString(driverId);
        dest.writeString(driverPhotoUri);
        dest.writeString(clientName);
        dest.writeString(clientId);
        dest.writeString(clientPhotoUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverPhotoUri() {
        return driverPhotoUri;
    }

    public void setDriverPhotoUri(String driverPhotoUri) {
        this.driverPhotoUri = driverPhotoUri;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientPhotoUri() {
        return clientPhotoUri;
    }

    public void setClientPhotoUri(String clientPhotoUri) {
        this.clientPhotoUri = clientPhotoUri;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getVisitedPlace() {
        return visitedPlace;
    }

    public void setVisitedPlace(String visitedPlace) {
        this.visitedPlace = visitedPlace;
    }

    @Override
    public String getPhotoUri() {
        return null;
    }

    @Override
    public void setPhotoUri(String photoUri) {

    }
}
