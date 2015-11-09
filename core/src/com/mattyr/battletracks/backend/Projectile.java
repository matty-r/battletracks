package com.mattyr.battletracks.backend;

import com.badlogic.gdx.graphics.Texture;

public class Projectile extends Entity{
	Vehicle ownerVehicle;
	private boolean trackTarget;
	Entity target;
	
	public Projectile(float startX, float startY, Texture loadTexture, Vehicle owner ,String name, float speed, int healthValue,boolean trackTarget){
		super(startX, startY, loadTexture, name, healthValue);
		this.setTrackTarget(trackTarget);
		setForwardSpeed(speed);
		setOwnerVehicle(owner);
	}
	
	public Vehicle getOwnerVehicle() {
		return ownerVehicle;
	}

	public void drive(boolean swap, Entity entity){
		super.drive(swap);
		if(target == null && entity != null)
			target = entity;
			
		if(target != null)
			faceEntity(entity);
	}
	
	public void setOwnerVehicle(Vehicle ownerVehicle) {
		this.ownerVehicle = ownerVehicle;
	}

	@Override
	public String toString(){
		return super.toString()+"\nOwner Name:"+ownerVehicle.getName();
	}
	
	@Override
	public boolean isCollidedWith(Entity targetPOI){
	   if(ownerVehicle != targetPOI)
    	return super.isCollidedWith(targetPOI);
	   else return false;
    }

	public boolean isTrackTarget() {
		return trackTarget;
	}

	public void setTrackTarget(boolean trackTarget) {
		this.trackTarget = trackTarget;
	}
}
