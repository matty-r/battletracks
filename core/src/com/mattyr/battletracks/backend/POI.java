package com.mattyr.battletracks.backend;

import com.badlogic.gdx.math.Vector2;
import com.mattyr.battletracks.backend.Entity;

public class POI extends Vector2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 969476707029396237L;
	private float relativeX;
	private float relativeY;
	private Entity owner;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public POI(Entity owner, String name){
		this.name = name;
		this.owner = owner;
		setRelativeX(owner.getWidth() /2);
		setRelativeY(owner.getHeight() /2);
		setXY();
		this.owner.allPOI.add(this);
	}
	
	public POI(Entity owner,float offsetX, float offsetY, String name){
		this.name = name;
		this.owner = owner;
		setRelativeX(offsetX);
		setRelativeY(offsetY);
		setXY();
		this.owner.allPOI.add(this);
	}
	
	void setXY(){
			setX(owner.getX() + getRelativeX());
			setY(owner.getY() + getRelativeY());
	}
	
	/**
	 * Uses the rotationangle and relative points to set the X,Y position
	 */
	void setXY2(){
		setX((float) (((owner.getX() + getRelativeX()) - owner.centrePoint.getX()) * Math.cos(Math.toRadians(owner.getRotation())) - ((owner.getY() + getRelativeY()) - owner.centrePoint.getY()) * Math.sin(Math.toRadians(owner.getRotation())) + owner.centrePoint.getX()));
		setY((float) (((owner.getY() + getRelativeY()) - owner.centrePoint.getY()) * Math.cos(Math.toRadians(owner.getRotation())) + ((owner.getX() + getRelativeX()) - owner.centrePoint.getX()) * Math.sin(Math.toRadians(owner.getRotation())) + owner.centrePoint.getY()));
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

	public float getRelativeX() {
		return relativeX;
	}

	public void setRelativeX(float relativeX) {
		this.relativeX = relativeX;
	}

	public float getRelativeY() {
		return relativeY;
	}

	public void setRelativeY(float relativeY) {
		this.relativeY = relativeY;
	}
	
	@Override
	public String toString(){
		String finalString = "\n"+name+"> POI: X="+getX()+" Y="+getY();
		return finalString;
	}
}
