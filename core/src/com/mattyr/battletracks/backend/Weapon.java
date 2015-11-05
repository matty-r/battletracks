package com.mattyr.battletracks.backend;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.POI;

public class Weapon extends Entity {
	public POI bulletPoint;
	Vehicle ownerVehicle;
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	long reloadDelayMillis;
	long lastShotTime = 0;
	Sound weaponFire;
	
	public Weapon(float startX, float startY, Texture loadTexture, Vehicle vehicle, String name, long reloadDelay, int healthValue){
		super(startX, startY, loadTexture, name, healthValue);
		weaponFire = Gdx.audio.newSound(Gdx.files.internal("tankFiring.mp3"));
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
	//TODO This should probably be able to be controlled by the tank turret class
	public void makeBullet(){
		if(System.currentTimeMillis() - lastShotTime >= reloadDelayMillis){	
			
			Projectile bullet = new Projectile(bulletPoint.getX(),bulletPoint.getY(),new Texture(Gdx.files.internal("bullet.png")),ownerVehicle,"bullet",15f, 10);
			weaponFire.play();
			lastShotTime = System.currentTimeMillis();
			projectiles.add(bullet);


			bullet.setRotation(getRotation() + ((float) Math.random() * 5));
			bullet.setX(bulletPoint.getX());
			bullet.setY(bulletPoint.getY());
			bullet.setPOIs();
		}
	}
	
	public long getReloadDelayMillis() {
		return reloadDelayMillis;
	}

	public void setReloadDelayMillis(long reloadDelayMillis) {
		this.reloadDelayMillis = reloadDelayMillis;
	}

	@Override
	public String toString(){
		return super.toString()+"\nProjectiles="+projectiles.size();
	}
	
	
}
