package com.mattyr.battletracks;


import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Vehicle;
import com.mattyr.battletracks.backend.Weapon;

public class TankTurret extends Weapon {


	public TankTurret(float startX, float startY, Texture loadTexture, Vehicle vehicle){
		super(startX, startY, loadTexture, vehicle);
	}
	
}
