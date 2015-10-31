package com.mattyr.battletracks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;

public class Helicopter extends Vehicle {
	final static int HEALTH = 30;
	final static float FORWARD_SPEED = 4f;
	final static float REVERSE_SPEED = 1f;
	final static float TURN_SPEED = 4f;
		
	public Helicopter(float startX, float startY, Texture loadTexture,String name) {
		super(startX, startY, loadTexture, name, HEALTH);
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