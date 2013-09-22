/*
 * Copyright (C) 2013 BeyondAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beyondar.android.world.objects;

import android.location.Location;

import com.beyondar.android.util.Constants;
import com.beyondar.android.util.math.Distance;
import com.google.android.maps.GeoPoint;

public class GeoObject extends BeyondarObject {

	private double mLongitude;
	private double mLatitude;
	private double mAltitude;

	protected GeoPoint mGeoPoint;

	/**
	 * Create an instance of a {@link GeoObject} with an unique ID
	 * 
	 * @param id
	 *            Unique ID
	 */
	public GeoObject(long id) {
		super(id);
		setVisibile(true);
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public double getAltitude() {
		return mAltitude;
	}

	public void setAltitude(double altitude) {
		mAltitude = altitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;

	}

	public int getLatitudeE6() {
		return (int) (mLatitude * Constants.E6);
	}

	public int getLongitudeE6() {
		return (int) (mLongitude * Constants.E6);
	}

	public void setLocation(Location location) {

		if (location == null) {
			return;
		}
		setLatitude(location.getLatitude());
		setLongitude(location.getLongitude());
		setAltitude(location.getAltitude());
	}

	public double calculateDistanceMeters(GeoObject geo) {
		return calculateDistanceMeters(geo.getLongitude(), geo.getLatitude());
	}

	public double calculateDistanceMeters(double longitude, double latitude) {
		return Distance.calculateDistanceMeters(getLongitude(), getLatitude(), longitude, latitude);
	}

	public GeoPoint getGeoPoint() {
		if (mGeoPoint == null) {
			mGeoPoint = new GeoPoint(getLatitudeE6(), getLongitudeE6());
			return mGeoPoint;
		}

		if (getLatitudeE6() == mGeoPoint.getLatitudeE6() && getLongitudeE6() == mGeoPoint.getLongitudeE6()) {
			return mGeoPoint;
		}
		mGeoPoint = new GeoPoint(getLatitudeE6(), getLongitudeE6());
		return mGeoPoint;
	}

}