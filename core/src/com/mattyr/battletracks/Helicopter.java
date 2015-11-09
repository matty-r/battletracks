package com.mattyr.battletracks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;
import com.mattyr.battletracks.backend.Weapon;

public class Helicopter extends Vehicle {
	final static int HEALTH = 30;
	final static float FORWARD_SPEED = 4f;
	final static float REVERSE_SPEED = 2f;
	final static float TURN_SPEED = 4f;
		
	public Helicopter(float startX, float startY, String name) {
		super(startX, startY, new Texture(Gdx.files.internal("Helicopter.png")), name, HEALTH);
		setTurnSpeed(TURN_SPEED);
		setForwardSpeed(FORWARD_SPEED);
		setReverseSpeed(REVERSE_SPEED);
		hardPoint = new POI(this, (getWidth() /2) + 7 , getHeight() /2, "Hard Point");
		setTurret(new Turret(getX(), getY(), this, name));
		setPOIs();
	}
	
	class Turret extends Weapon {
		private static final int RELOAD_DELAY = 700;
		private static final int TURN_SPEED_FACTOR = 3;
		private static final int CENTRE_RELATIVE_X = 12;
		private static final int CENTRE_RELATIVE_Y = 9;
		private static final float MAXIMUM_ROTATION = 20;
		private static final float MAXIMUM_SPREAD = 10;
		private static final float ACCURACY = 50;
		
		public Turret(float startX, float startY, Vehicle vehicle, String name){
			super(startX, startY, null, vehicle, name, RELOAD_DELAY, vehicle.getCurrentHealthValue(), MAXIMUM_SPREAD, ACCURACY, Gdx.audio.newSound(Gdx.files.internal("tankFiring.mp3")), MAXIMUM_ROTATION);
			//Doesn't affect mouse movements
			setTurnSpeed(vehicle.getTurnSpeed() * TURN_SPEED_FACTOR);
			centrePoint.setRelativeX(CENTRE_RELATIVE_X);
			centrePoint.setRelativeY(CENTRE_RELATIVE_Y);
			bulletPoints.add(new POI(vehicle,vehicle.getWidth()-25,(vehicle.getHeight()/2)-10, getName()+" Bullet Point1"));
			bulletPoints.add(new POI(vehicle,vehicle.getWidth()-25,(vehicle.getHeight()/2)+10, getName()+" Bullet Point2"));
			setPOIs();
			
		}
		
		@Override
		public void faceMouse(){
			float targetAngle = (float) Math.toDegrees(Math.atan2((Gdx.graphics.getHeight() - Gdx.input.getY()) - centrePoint.getY(), Gdx.input.getX() - centrePoint.getX()));
			if(targetAngle < 0)
				targetAngle = 360 - (-targetAngle);
			setRotation(targetAngle);
			setPOIs();
			Helicopter.this.faceMouse();
		}
	}
}