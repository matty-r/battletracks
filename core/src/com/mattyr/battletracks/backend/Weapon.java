package com.mattyr.battletracks.backend;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.POI;

public class Weapon extends Entity {
	public POI bulletPoint;
	Vehicle ownerVehicle;
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	long reloadDelayMillis;
	long lastShotTime = 0;

	public Weapon(float startX, float startY, Texture loadTexture, Vehicle vehicle, String name, long reloadDelay){
		super(startX, startY, loadTexture, name);
		this.ownerVehicle = vehicle; 
		this.reloadDelayMillis = reloadDelay;
	}

	public ArrayList<Projectile> getBulletObjects() {
		return projectiles;
	}

	public void setBulletObjects(ArrayList<Projectile> bulletObjects) {
		projectiles = bulletObjects;
	}

	public void setPOIs(){
		super.setPOIs();
		
		if(ownerVehicle != null){		
			if(ownerVehicle.hardPoint != null && centrePoint != null){
				setX(ownerVehicle.hardPoint.getX() - centrePoint.getRelativeX());
				setY(ownerVehicle.hardPoint.getY() - centrePoint.getRelativeY());
			}
			if(bulletPoint != null){		
				bulletPoint.setXY2();
			}
		}
		
	}
	
	public void makeBullet(){
		if(System.currentTimeMillis() - lastShotTime >= reloadDelayMillis){	
			Projectile bullet = new Projectile(bulletPoint.getX(),bulletPoint.getY(),new Texture(Gdx.files.internal("bullet.png")),ownerVehicle,"poop");
			lastShotTime = System.currentTimeMillis();
			projectiles.add(bullet);

			bullet.setForwardSpeed(15f);
			bullet.setDirection(getDirection());
			bullet.setX(bulletPoint.getX());
			bullet.setY(bulletPoint.getY());
			bullet.setPOIs();
		}
	}
	
	@Override
	public String toString(){
		return super.toString();
	}
	
	public void faceMouse(){
		float mouseAngle = (float) Math.toDegrees(Math.atan2((1080 - Gdx.input.getY()) - centrePoint.getY(), Gdx.input.getX() - centrePoint.getX()));
		if(mouseAngle < 0)
			mouseAngle = 360 - (-mouseAngle);
		
		setDirection(mouseAngle);
		setPOIs();
	}
}
