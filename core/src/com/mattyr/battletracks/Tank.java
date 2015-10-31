package com.mattyr.battletracks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;

public class Tank extends Vehicle {
	final static int HEALTH = 50;
	final static float FORWARD_SPEED = 2f;
	final static float REVERSE_SPEED = 2f;
	final static float TURN_SPEED = 3f;
		
	public Tank(float startX, float startY, String name) {
		super(startX, startY, new Texture(Gdx.files.internal("tankBody.png")), name, HEALTH);
		setTurnSpeed(TURN_SPEED);
		setForwardSpeed(FORWARD_SPEED);
		setReverseSpeed(REVERSE_SPEED);
		hardPoint = new POI(this, 15, getHeight() /2, "Hard Point");
		setPOIs();
	}
	
	@Override
	public void addTurret(String name){
		setTurret(new TankTurret(getX(), getY(), new Texture(Gdx.files.internal("tankTurret.png")), this, name));
	}
}