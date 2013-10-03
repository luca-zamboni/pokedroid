package it.pokefundroid.pokedroid.viewUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableMonster implements Parcelable{
	private String id;
	private double[] location;
	
	public ParcelableMonster() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}
	private ParcelableMonster(Parcel in) {
		id = in.readString();
		location = new double[2];
		in.readDoubleArray(location);
	}
	
	public static final Parcelable.Creator<ParcelableMonster> CREATOR = new Parcelable.Creator<ParcelableMonster>() {
		public ParcelableMonster createFromParcel(Parcel in) {
             return new ParcelableMonster(in);
         }

		@Override
		public ParcelableMonster[] newArray(int size) {
			return new ParcelableMonster[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeDoubleArray(location);
	}
	
}
