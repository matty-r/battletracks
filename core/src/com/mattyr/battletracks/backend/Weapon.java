package com.mattyr.battletracks.backend;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.POI;

public class Weapon extends Entity {
	//public POI bulletPoint;
	private Vehicle ownerVehicle;
	public ArrayList<POI> bulletPoints = new ArrayList<POI>();
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	long reloadDelayMillis;
	long lastShotTime = 0;
	Sound shotSound;
	float maximumSpread;
	//If accuracy is 100 (Percent) then getWeaponSpread() return 0
	//If accuracy is 50 (Percent) and maximumSpread is 10, return 5;
	float accuracy;
	float maximumRotation;
	
	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		if(accuracy <= 100)
			this.accuracy = accuracy;
	}

	public Weapon(float startX, float startY, Texture loadTexture, Vehicle vehicle, String name, long reloadDelay, int healthValue, float maximumSpread, float accuracy, Sound shotSound,float maximumRotation){
		super(startX, startY, loadTexture, name, healthValue);
		this.maximumRotation = maximumRotation;
		this.maximumSpread = maximumSpread;
		this.accuracy = accuracy;
		this.setOwnerVehicle(vehicle); 
		this.reloadDelayMillis = reloadDelay;
		this.shotSound = shotSound;
	}

	public long getLastShotTime() {
		return lastShotTime;
	}

	public void setLastShotTime(long lastShotTime) {
		this.lastShotTime = lastShotTime;
	}

	public float getMaximumSpread() {
		return maximumSpread;
	}

	public void setMaximumSpread(float maximumSpread) {
		this.maximumSpread = maximumSpread;
	}

	public ArrayList<Projectile> getBulletObjects() {
		return projectiles;
	}

	public void setBulletObjects(ArrayList<Projectile> bulletObjects) {
		projectiles = bulletObjects;
	}

	public void setPOIs(){
		super.setPOIs();
		
		if(getOwnerVehicle() != null){		
			if(getOwnerVehicle().hardPoint != null && centrePoint != null){
				setX(getOwnerVehicle().hardPoint.getX() - centrePoint.getRelativeX());
				setY(getOwnerVehicle().hardPoint.getY() - centrePoint.getRelativeY());
			}
			for(POI bulletPoint : bulletPoints)		
				bulletPoint.setXY2();
		}
		
	}

	public void firePrimary(){
		if(System.currentTimeMillis() - getLastShotTime() >= getReloadDelayMillis()){	
			for(POI bulletPoint : bulletPoints){
				Projectile bullet = new Projectile(bulletPoint.getX(),bulletPoint.getY(),new Texture(Gdx.files.internal("bullet.png")),getOwnerVehicle(),"bullet",15f, 10, false);
				shotSound.play();
				setLastShotTime(System.currentTimeMillis());
				projectiles.add(bullet);
		
				bullet.setRotation(getRotation() + ((float) (Math.random() * getWeaponSpread())));
				bullet.setX(bulletPoint.getX());
				bullet.setY(bulletPoint.getY());
				bullet.setPOIs();
			}
		}
	}
	
	public void fireSecondary(){
		if(System.currentTimeMillis() - getLastShotTime() >= getReloadDelayMillis()){	
			for(POI bulletPoint : bulletPoints){
				Projectile bullet = new Projectile(bulletPoint.getX(),bulletPoint.getY(),new Texture(Gdx.files.internal("homingMissile.png")),getOwnerVehicle(),"bullet",15f, 10, true);
				shotSound.play();
				setLastShotTime(System.currentTimeMillis());
				projectiles.add(bullet);
		
				bullet.setRotation(getRotation() + ((float) (Math.random() * getWeaponSpread())));
				bullet.setX(bulletPoint.getX());
				bullet.setY(bulletPoint.getY());
				bullet.setPOIs();
			}
		}
	}
	
	@Override
	public void faceMouse(){
		float targetAngle = (float) Math.toDegrees(Math.atan2((Gdx.graphics.getHeight() - Gdx.input.getY()) - centrePoint.getY(), Gdx.input.getX() - centrePoint.getX()));
		if(targetAngle < 0)
			targetAngle = 360 - (-targetAngle);
		
		float angleMin = getOwnerVehicle().getRotation() - targetAngle;

		if(angleMin >= (-maximumRotation) && angleMin <= maximumRotation){
			setRotation(targetAngle);
			setPOIs();
		} else {
			setRotation(getOwnerVehicle().getRotation());
			setPOIs();
		}
	}
	
	
	
	public float getWeaponSpread(){
		return maximumSpread - ((maximumSpread / 100) * accuracy);
	}
	
	public long getReloadDelayMillis() {
		return reloadDelayMillis;
	}

	public void setReloadDelayMillis(long reloadDelayMillis) {
		this.reloadDelayMillis = reloadDelayMillis;
	}

	@Override
	public String toString(){
		return super.toString()+"\nProjectiles="+projectiles.size()+
				"\nAccuracy="+getAccuracy()+"%";
	}

	public Vehicle getOwnerVehicle() {
		return ownerVehicle;
	}

	public void setOwnerVehicle(Vehicle ownerVehicle) {
		this.ownerVehicle = ownerVehicle;
	}
	
	
}
