package com.mattyr.battletracks.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Weapon;
import com.mattyr.battletracks.TankTurret;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.POI;

public class Vehicle extends Entity {
	private Weapon turret;
	public POI hardPoint;
	
	public Vehicle(float startX, float startY, Texture loadTexture, String name){
		super(startX, startY, loadTexture, name);
	}
	
	public void AddTurret(String name){
		setTurret(new TankTurret(getX(), getY(), new Texture(Gdx.files.internal("tankTurret.png")), this, name));
	}

	public void setPOIs(){
		super.setPOIs();		
		
		if(hardPoint != null){		
			hardPoint.setX((float) (((getX() + hardPoint.getRelativeX()) - centrePoint.getX()) * Math.cos(Math.toRadians(getDirection())) - ((getY() + hardPoint.getRelativeY()) - centrePoint.getY()) * Math.sin(Math.toRadians(getDirection())) + centrePoint.getX()));
			hardPoint.setY((float) (((getY() + hardPoint.getRelativeY()) - centrePoint.getY()) * Math.cos(Math.toRadians(getDirection())) + ((getX() + hardPoint.getRelativeX()) - centrePoint.getX()) * Math.sin(Math.toRadians(getDirection())) + centrePoint.getY()));
		}
		if(turret != null){
			getTurret().setPOIs();
		}
	}
	

	public Weapon getTurret() {
		return turret;
	}

	public void setTurret(Weapon turret) {
		this.turret = turret;
	}

	@Override
	public String toString(){
		return super.toString();
	}
}
