package com.mattyr.battletracks.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mattyr.battletracks.backend.Weapon;
import com.mattyr.battletracks.backend.Entity;

public class Vehicle extends Entity {
	Texture texture;
	private TextureRegion region;
	

	private Weapon turret;
	private CentrePoint centrePoint;
	private HardPoint hardPoint;
	
	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	public Vehicle(float startX, float startY, Texture loadTexture){

		texture = loadTexture;
		setRegion(new TextureRegion(texture));
		setWidth(texture.getWidth());
		setHeight(texture.getHeight());
		setX(startX);
		setY(startY);
		setDirection(0);
		//forwardSpeed = 2.0f;
		//reverseSpeed = 0.5f;
		//turnSpeed = 1f;
		setHardPoint(new HardPoint(15, getHeight() /2));
		setCentrePoint(new CentrePoint());
		setPOIs();
	}
	

	
	public void AddTurret(){
		setTurret(new Weapon(getX(), getY(), new Texture(Gdx.files.internal("tankTurret.png")), this));
	}


	public void turn(boolean swap){
		setDirection(getDirection() + ((swap) ? getTurnSpeed() : -getTurnSpeed()));
		
		if(getDirection() >= 360)
			setDirection(0);
		else if(getDirection() < 0)
			setDirection(359);
		
		setPOIs();
		getTurret().setPOIs();
	}
	

	public void drive(boolean swap){
		float velocityX;
		float velocityY;
		float directionX;
		float directionY;

		directionX = (float) Math.cos(Math.toRadians(this.getDirection()));			
		directionY = (float) Math.sin(Math.toRadians(this.getDirection()));
		
		if(!swap){
		velocityX = directionX * getForwardSpeed();
		velocityY = directionY * getForwardSpeed();
		
		setX(getX() + velocityX);
		getTurret().setX(getTurret().getX() + velocityX);
		
		setY(getY() + velocityY);
		getTurret().setY(getTurret().getY() + velocityY);
		
		} else {
			velocityX = directionX * getReverseSpeed();
			velocityY = directionY * getReverseSpeed();
			
			setX(getX() - velocityX);
			getTurret().setX(getTurret().getX() - velocityX);
			
			setY(getY() - velocityY);
			getTurret().setY(getTurret().getY() - velocityY);
		}
		setPOIs();
		getTurret().setPOIs();
	}
	
	
	
	public void setPOIs(){
		
		getCentrePoint().setXY();
		setRealX((float) (((getX()) - getCentrePoint().getX()) * Math.cos(Math.toRadians(getDirection())) - ((getY()) - getCentrePoint().getY()) * Math.sin(Math.toRadians(getDirection())) + getCentrePoint().getX()));
		setRealY((float) (((getY()) - getCentrePoint().getY()) * Math.cos(Math.toRadians(getDirection())) + ((getX()) - getCentrePoint().getX()) * Math.sin(Math.toRadians(getDirection())) + getCentrePoint().getY()));
		if(getHardPoint() != null){		
			getHardPoint().setX((float) (((getX() + getHardPoint().relativeX) - getCentrePoint().getX()) * Math.cos(Math.toRadians(getDirection())) - ((getY() + getHardPoint().relativeY) - getCentrePoint().getY()) * Math.sin(Math.toRadians(getDirection())) + getCentrePoint().getX()));
			getHardPoint().setY((float) (((getY() + getHardPoint().relativeY) - getCentrePoint().getY()) * Math.cos(Math.toRadians(getDirection())) + ((getX() + getHardPoint().relativeX) - getCentrePoint().getX()) * Math.sin(Math.toRadians(getDirection())) + getCentrePoint().getY()));
		}
	}
	
	public CentrePoint getCentrePoint() {
		return centrePoint;
	}

	public void setCentrePoint(CentrePoint centrePoint) {
		this.centrePoint = centrePoint;
	}

	public HardPoint getHardPoint() {
		return hardPoint;
	}

	public void setHardPoint(HardPoint hardPoint) {
		this.hardPoint = hardPoint;
	}

	public Weapon getTurret() {
		return turret;
	}

	public void setTurret(Weapon turret) {
		this.turret = turret;
	}



	public class HardPoint{
		private float x;
		private float y;
		float relativeX;
		float relativeY;
		
		private HardPoint(float offsetX, float offsetY){
			this.relativeX = offsetX;
			this.relativeY = offsetY;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}
	}
}
