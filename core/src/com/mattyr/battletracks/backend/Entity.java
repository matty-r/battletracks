package com.mattyr.battletracks.backend;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity {
	private float forwardSpeed;
	private float reverseSpeed;
	private float turnSpeed;
	private float direction;
	private float width;
	private float height;
	private float x;
	private float y;
	public ArrayList<POI> allPOI = new ArrayList<POI>();
	public POI centrePoint;
	public POI bLeft;
	public POI tLeft;
	public POI bRight;
	public POI tRight;
	private String name;
	Texture texture;
	private TextureRegion region;
	private int healthValue;
	
	Entity(float startX, float startY, Texture loadTexture, String name, int healthValue){
		setName(name);
		this.setHealthValue(healthValue);
		texture = loadTexture;
		setRegion(new TextureRegion(texture));
		setWidth(texture.getWidth());
		setHeight(texture.getHeight());
		setX(startX);
		setY(startY);
		setDirection(0);
		centrePoint = new POI(this, "Centre Point");
		centrePoint.setXY();
		bLeft = new POI(this, 0f, 0f, "Bottom Left");
		tLeft = new POI(this, 0f, this.getHeight(), "Top Left");
		bRight = new POI(this, this.getWidth(), 0f, "Bottom Right");
		tRight = new POI(this, this.getWidth(), this.getHeight(), "Top Right");
	}
	
	public void setTurnSpeed(float speed){
		turnSpeed = speed;
	}
	
	public void turn(boolean swap){
		setDirection(getDirection() + ((swap) ? getTurnSpeed() : -getTurnSpeed()));
		if(getDirection() >= 360)
			setDirection(0);
		else if(getDirection() < 0)
			setDirection(359);
		setPOIs();
	}
	
	public void destroy(){
		for(x = allPOI.size() -1; x > -1; x--){
			allPOI.remove(x);
		}
	}
	
	public void faceMouse(){
		float targetAngle = (float) Math.toDegrees(Math.atan2((1080 - Gdx.input.getY()) - centrePoint.getY(), Gdx.input.getX() - centrePoint.getX()));
		if(targetAngle < 0)
			targetAngle = 360 - (-targetAngle);
		
		setDirection(targetAngle);
		setPOIs();
	}
	
	public void faceVehicle(Vehicle target){
		float targetAngle = (float) Math.toDegrees(Math.atan2(target.centrePoint.getY() - centrePoint.getY(), target.centrePoint.getX() - centrePoint.getX()));
		if(targetAngle < 0)
			targetAngle = 360 - (-targetAngle);
		
		setDirection(targetAngle);
		setPOIs();
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
		setY(getY() + velocityY);	
		} else {
			velocityX = directionX * getReverseSpeed();
			velocityY = directionY * getReverseSpeed();
			setX(getX() - velocityX);			
			setY(getY() - velocityY);
		}
		setPOIs();
	}
	
	public void setPOIs(){
		centrePoint.setXY();
		bLeft.setXY2();
		tLeft.setXY2();
		bRight.setXY2();
		tRight.setXY2();
		
		
	}
	
	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}
	
	public void setForwardSpeed(float speed){
		forwardSpeed = speed;
	}
	
	public void setReverseSpeed(float speed){
		reverseSpeed = speed;
	}
	
	public float getTurnSpeed(){
		return turnSpeed;
	}
	
	public float getForwardSpeed(){
		return forwardSpeed;
	}
	
	public float getReverseSpeed(){
		return reverseSpeed;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDirection() {
		return direction;
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		String finalString = getName()+":\nx="+getX()+
				"\ny="+getY()+
				"\nDirection="+getDirection()+
				"\nWidth="+getWidth()+
				"\nHeight="+getHeight()+
				"\nTurn Speed="+getTurnSpeed()+
				"\nForward Speed="+getForwardSpeed()+
				"\nReverse Speed="+getReverseSpeed();
		
		for(POI poiString : allPOI)
			finalString += poiString.toString();
		
		return "\n"+finalString;
	}

	public int getHealthValue() {
		return healthValue;
	}

	public void setHealthValue(int healthValue) {
		this.healthValue = healthValue;
	}
}
