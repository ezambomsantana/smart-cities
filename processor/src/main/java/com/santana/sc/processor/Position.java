package com.santana.sc.processor;

import java.util.Comparator;
import java.util.Date;

import scala.Serializable;

public class Position implements Serializable, Comparable<Position> {
	
	private float latitude;
	private float longitude;
	
	private Date date;

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Position(float latitude, float longitude, Date date) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "" + latitude + " " + longitude;
	}

	@Override
	public int compareTo(Position o) {
		if (this.getDate().before(o.getDate())) {
			return -1;
		} else if (this.getDate().after(o.getDate())) {
			return 1;
		} else {
			return 0;
		}
	}
	

}
