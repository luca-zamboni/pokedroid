package it.pokefundroid.pokedroid.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class LocationUtils {
	public enum ErrorType { 
		DISABLED, ENABLED
	}

	public enum LocationType {
		NETWORK, GPS
	}

	public interface ILocation {
		public void onLocationChaged(Location l);

		public void onErrorOccured(ErrorType ex, String provider);

		public void onStatusChanged(String provider, boolean isActive);
	}

	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private ILocation mLocationInterface;
	private LocationType mType;
	private long mTimeInterval = 1000;
	private float mMinDistance = 10;

	public LocationUtils(Context ctx, ILocation ilocation, LocationType type) {
		this.mLocationInterface = ilocation;
		this.mType = type;
		initializeLocationService(ctx);
	}
	
	public LocationUtils(Context ctx, ILocation ilocation) {
		this.mLocationInterface = ilocation;
		this.mType = LocationType.GPS;
		initializeLocationService(ctx);
	}

	public LocationUtils(Context ctx, ILocation ilocation, LocationType type,
			long timeInterval, float minDistance) {
		super();
		this.mLocationInterface = ilocation;
		this.mType = type;
		this.mTimeInterval = timeInterval;
		this.mMinDistance = minDistance;
		mLocationInterface = ilocation;
		initializeLocationService(ctx);
	}

	public long getPollingTimeInterval() {
		return mTimeInterval;
	}

	/**
	 * Replace the interval between gps's calls This number has an huge impact
	 * on battery life.
	 * 
	 * @param mTimeInterval
	 */
	public void setPollingTimeInterval(long mTimeInterval) {
		this.mTimeInterval = mTimeInterval;
		updateLocationUpdates();
	}

	public float getMinDistance() {
		return mMinDistance;
	}

	/**
	 * Replace the minimum valid distance to look for this number should be
	 * between (inclusive) 10 and 100
	 * 
	 * @param minDistance
	 * @return if the operation is valid
	 */
	public boolean setMinDistance(float minDistance) {
		this.mMinDistance = minDistance;
		if (mMinDistance >= 10 && mMinDistance <= 100) {
			updateLocationUpdates();
			return true;
		}
		return false;
	}

	private void updateLocationUpdates() {
		mLocationManager.removeUpdates(mLocationListener);
		activateUpdates();
	}

	private void initializeLocationService(Context ctx) {
		mLocationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new MyLocationListener();
		activateUpdates();
	}

	private void activateUpdates() {
		if (mType == LocationType.NETWORK) {

			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, mTimeInterval,
					mMinDistance, mLocationListener);
		} else {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, mTimeInterval, mMinDistance,
					mLocationListener);
		}
	}

	public void close() {
		mLocationManager.removeUpdates(mLocationListener);
		mLocationListener = null;
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location.getAccuracy() >= mMinDistance) {
				mLocationInterface.onLocationChaged(location);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			mLocationInterface.onErrorOccured(ErrorType.DISABLED, provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			mLocationInterface.onErrorOccured(ErrorType.ENABLED, provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			mLocationInterface.onStatusChanged(provider,
					status == LocationProvider.AVAILABLE);
		}

	}
}
