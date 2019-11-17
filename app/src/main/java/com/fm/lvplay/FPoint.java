package com.fm.lvplay;

public class FPoint {
	public float x;
	public float y;

	public FPoint() {
	}

	public FPoint(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Set the point's x and y coordinates
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
