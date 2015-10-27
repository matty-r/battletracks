package com.mattyr.battletracks;

import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Vehicle;

public class Tank extends Vehicle {


	public Tank(float startX, float startY, Texture loadTexture) {
		super(startX, startY, loadTexture);
		this.setTurnSpeed(1f);
		this.setForwardSpeed(2.0f);
		this.setReverseSpeed(0.5f);
	}

	

}