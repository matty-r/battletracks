package com.mattyr.battletracks.backend;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mattyr.battletracks.backend.Entity;

public class Weapon extends Entity {
	Texture texture;
	private TextureRegion region;

	private CentrePoint centrePoint;
	boolean isTurret;
	Vehicle ownerVehicle;

	public Weapon(float startX, float startY, Texture loadTexture, Vehicle vehicle){
		texture = loadTexture;
		ownerVehicle = vehicle;
		setRegion(new TextureRegion(texture));
		setWidth(texture.getWidth());
		setHeight(texture.getHeight());
		setX(startX);
		setY(startY);
		setDirection(0);
		setTurnSpeed(ownerVehicle.getTurnSpeed() * 3);
		setCentrePoint(new CentrePoint(12,9));
		setPOIs();
	}
	
	public void turn(boolean swap){
		setDirection(getDirection() + ((swap) ? getTurnSpeed() : -getTurnSpeed()));
		
		if(getDirection() >= 360)
			setDirection(0);
		else if(getDirection() < 0)
			setDirection(359);

		setPOIs();
	}
	
	
	
	public void setPOIs(){
		getCentrePoint().setXY();
		setX(ownerVehicle.getHardPoint().getX() - getCentrePoint().getRelativeX());
		setY(ownerVehicle.getHardPoint().getY() - getCentrePoint().getRelativeY());
	}

	public CentrePoint getCentrePoint() {
		return centrePoint;
	}

	public void setCentrePoint(CentrePoint centrePoint) {
		this.centrePoint = centrePoint;
	}

	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}
}
