package com.mattyr.battletracks;

import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;

public class Tank extends Vehicle {


	public Tank(float startX, float startY, Texture loadTexture,String name) {
		super(startX, startY, loadTexture, name);
		setTurnSpeed(1f);
		setForwardSpeed(2.0f);
		setReverseSpeed(0.5f);
		hardPoint = new POI(this, 15, getHeight() /2);
		setPOIs();
	}
}