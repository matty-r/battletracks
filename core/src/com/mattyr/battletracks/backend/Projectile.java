package com.mattyr.battletracks.backend;

import com.badlogic.gdx.graphics.Texture;

public class Projectile extends Entity{
	Vehicle ownerVehicle;

	public Projectile(float startX, float startY, Texture loadTexture, Vehicle owner ,String name, float speed, int healthValue){
		super(startX, startY, loadTexture, name, healthValue);
		setForwardSpeed(speed);
		setOwnerVehicle(owner);
	}
	
	public Vehicle getOwnerVehicle() {
		return ownerVehicle;
	}

	public void setOwnerVehicle(Vehicle ownerVehicle) {
		this.ownerVehicle = ownerVehicle;
	}

	@Override
	public String toString(){
		return super.toString()+"\nOwner Name:"+ownerVehicle.getName();
	}
}
