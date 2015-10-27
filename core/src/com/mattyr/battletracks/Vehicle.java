package com.mattyr.battletracks;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Vehicle {
	Texture texture;
	TextureRegion region;
	float forwardSpeed;
	float reverseSpeed;
	float turnSpeed;
	float direction;
	float width;
	float height;
	float x;
	float y;
	float realX;
	float realY;
	Vehicle gun;
	CentrePoint centrePoint;
	HardPoint hardPoint;
	boolean isTurret;

	public Vehicle(float startX, float startY, Texture loadTexture, boolean isTurret){
		this.isTurret = isTurret;
		texture = loadTexture;
		region = new TextureRegion(texture);
		width = texture.getWidth();
		height = texture.getHeight();
		x = startX;
		y = startY;
		direction = 0;
		forwardSpeed = 2.0f;
		reverseSpeed = 0.5f;
		turnSpeed = 1f;
		if(isTurret){
			turnSpeed = turnSpeed * 3;
			centrePoint = new CentrePoint(12,9);
		}else {
			hardPoint = new HardPoint(15, height /2);
			centrePoint = new CentrePoint();
		}
		setPOIs();
	}
	
	public void AddTurret(){
		this.gun = new Vehicle(this.x, this.y, new Texture(Gdx.files.internal("tankTurret.png")), true);
		gun.forwardSpeed = forwardSpeed;
		gun.reverseSpeed = reverseSpeed;
		gun.direction = direction;
	}

	
	public void turn(boolean swap){
		direction += (swap) ? turnSpeed : -turnSpeed;
		
		if(direction >= 360)
			direction = 0;
		else if(direction < 0)
			direction = 359;
		
		setPOIs();
	}
	

	public void drive(boolean swap){
		float velocityX;
		float velocityY;
		float directionX;
		float directionY;
		setPOIs();
		
		directionX = (float) Math.cos(Math.toRadians(this.direction));			
		directionY = (float) Math.sin(Math.toRadians(this.direction));
		
		if(!swap){
		velocityX = directionX * forwardSpeed;
		velocityY = directionY * forwardSpeed;
		
		x += velocityX;
		y += velocityY;

		} else {
			velocityX = directionX * reverseSpeed;
			velocityY = directionY * reverseSpeed;
			
			x -= velocityX;
			y -= velocityY;
		}

		
	}
	
	public class CentrePoint{
		float x;
		float y;
		float relativeX;
		float relativeY;
		
		private CentrePoint(){
			relativeX = Vehicle.this.width /2;
			relativeY = Vehicle.this.height /2;
			setXY();
		}
		
		private CentrePoint(float offsetX, float offsetY){
			relativeX = offsetX;
			relativeY = offsetY;
			setXY();
		}
		
		private void setXY(){
			x = Vehicle.this.x + this.relativeX;
			y = Vehicle.this.y + this.relativeY;
		}
	}
	
	public void setPOIs(){
		realX = (float) (((x) - centrePoint.x) * Math.cos(Math.toRadians(direction)) - ((y) - centrePoint.y) * Math.sin(Math.toRadians(direction)) + centrePoint.x);
		realY = (float) (((y) - centrePoint.y) * Math.cos(Math.toRadians(direction)) + ((x) - centrePoint.x) * Math.sin(Math.toRadians(direction)) + centrePoint.y);
		
		centrePoint.setXY();
		
		if(hardPoint != null){		
			hardPoint.x = (float) (((x + hardPoint.relativeX) - centrePoint.x) * Math.cos(Math.toRadians(direction)) - ((y + hardPoint.relativeY) - centrePoint.y) * Math.sin(Math.toRadians(direction)) + centrePoint.x);
			hardPoint.y = (float) (((y + hardPoint.relativeY) - centrePoint.y) * Math.cos(Math.toRadians(direction)) + ((x + hardPoint.relativeX) - centrePoint.x) * Math.sin(Math.toRadians(direction)) + centrePoint.y);
		}
		//x = (float) (((x + hardPoint.relativeX) - centrePoint.x) * Math.cos(Math.toRadians(direction)) - ((y + hardPoint.relativeY) - centrePoint.y) * Math.sin(Math.toRadians(direction)) + centrePoint.x);
		//y = (float) (((y + hardPoint.relativeY) - centrePoint.y) * Math.cos(Math.toRadians(direction)) + ((x + hardPoint.relativeX) - centrePoint.x) * Math.sin(Math.toRadians(direction)) + centrePoint.y);
	}
	
	public class HardPoint{
		float x;
		float y;
		float relativeX;
		float relativeY;
		
		private HardPoint(float offsetX, float offsetY){
			this.relativeX = offsetX;
			this.relativeY = offsetY;
			//setPOIs();
		}
	}
}