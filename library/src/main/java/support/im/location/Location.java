package support.im.location;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

  public double latitude;
  public double longitude;
  public String title;
  public String snippet;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
    dest.writeString(this.title);
    dest.writeString(this.snippet);
  }

  public Location() {
  }

  protected Location(Parcel in) {
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
    this.title = in.readString();
    this.snippet = in.readString();
  }

  public static final Creator<Location> CREATOR = new Creator<Location>() {
    @Override public Location createFromParcel(Parcel source) {
      return new Location(source);
    }

    @Override public Location[] newArray(int size) {
      return new Location[size];
    }
  };
}
