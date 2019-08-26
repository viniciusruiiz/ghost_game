package com.learning.world;

public class Camera {
	public static int x;
	public static int y;
	
	public static int clamp(int xNow, int xMin, int xMax) {
		if(xNow < xMin) {
			xNow = xMin;
		} if (xNow > xMax) {
			xNow = xMax;
		}
		
		return xNow;
	}

}
