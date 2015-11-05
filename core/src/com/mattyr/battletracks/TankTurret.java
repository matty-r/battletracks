package com.mattyr.battletracks;


import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;
import com.mattyr.battletracks.backend.Weapon;

public class TankTurret extends Weapon {
	private static int RELOAD_DELAY = 700;
	private static int TURN_SPEED_FACTOR = 3;
	private static int CENTRE_RELATIVE_X = 12;
	private static int CENTRE_RELATIVE_Y = 9;

	public TankTurret(float startX, float startY, Texture loadTexture, Vehicle vehicle, String name){
		super(startX, startY, loadTexture, vehicle, name, RELOAD_DELAY, vehicle.getCurrentHealthValue());
		//Doesn't affect mouse movements
		setTurnSpeed(vehicle.getTurnSpeed() * TURN_SPEED_FACTOR);
		centrePoint.setRelativeX(CENTRE_RELATIVE_X);
		centrePoint.setRelativeY(CENTRE_RELATIVE_Y);
		bulletPoint = new POI(this,this.getWidth(),this.getHeight()/2, vehicle.getName()+" Bullet Point");
		setPOIs();
	}
	
	
}
