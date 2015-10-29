package com.mattyr.battletracks;


import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Vehicle;
import com.mattyr.battletracks.backend.Weapon;

public class TankTurret extends Weapon {


	public TankTurret(float startX, float startY, Texture loadTexture, Vehicle vehicle, String name){
		super(startX, startY, loadTexture, vehicle, name, 500);
		setTurnSpeed(vehicle.getTurnSpeed() * 3);
		centrePoint.setRelativeX(12);
		centrePoint.setRelativeY(9);
		bulletPoint = new POI(this,this.getWidth(),this.getHeight()/2);
		setPOIs();
	}
}
