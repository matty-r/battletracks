package com.mattyr.battletracks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;
import com.mattyr.battletracks.backend.Weapon;

public class Tank extends Vehicle {
	final static int HEALTH = 50;
	final static float FORWARD_SPEED = 2f;
	final static float REVERSE_SPEED = 2f;
	final static float TURN_SPEED = 1f;
		
	public Tank(float startX, float startY, String name) {
		super(startX, startY, new Texture(Gdx.files.internal("tankBody.png")), name, HEALTH);
		setTurnSpeed(TURN_SPEED);
		setForwardSpeed(FORWARD_SPEED);
		setReverseSpeed(REVERSE_SPEED);
		hardPoint = new POI(this, 15, getHeight() /2, "Hard Point");
		addTurret("Tank Turret");
		setPOIs();
	}
	
	public void addTurret(String name){
		setTurret(new Turret(getX(), getY(), this, name));
	}
	
	@Override
	public void turn(boolean swap){
		super.turn(swap);
	}
	
	class Turret extends Weapon {
		private static final int RELOAD_DELAY = 700;
		private static final int TURN_SPEED_FACTOR = 3;
		private static final int CENTRE_RELATIVE_X = 12;
		private static final int CENTRE_RELATIVE_Y = 9;
		private static final float MAXIMUM_ROTATION = 360;
		private static final float MAXIMUM_SPREAD = 10;
		private static final float ACCURACY = 50;
		
		public Turret(float startX, float startY, Vehicle vehicle, String name){
			super(startX, startY, new Texture(Gdx.files.internal("tankTurret.png")), vehicle, name, RELOAD_DELAY, vehicle.getCurrentHealthValue(), MAXIMUM_SPREAD, ACCURACY, Gdx.audio.newSound(Gdx.files.internal("tankFiring.mp3")), MAXIMUM_ROTATION);
			//Doesn't affect mouse movements
			setTurnSpeed(vehicle.getTurnSpeed() * TURN_SPEED_FACTOR);
			centrePoint.setRelativeX(CENTRE_RELATIVE_X);
			centrePoint.setRelativeY(CENTRE_RELATIVE_Y);
			bulletPoints.add(new POI(this,this.getWidth(),this.getHeight()/2, vehicle.getName()+" Bullet Point"));
			setPOIs();
		}
		
		
	}
}