package com.mattyr.battletracks.backend;

import com.mattyr.battletracks.backend.Entity;

public class POI {
	private float x;
	private float y;
	private float relativeX;
	private float relativeY;
	private Entity owner;
	
	public POI(Entity owner){
		this.owner = owner;
		setRelativeX(owner.getWidth() /2);
		setRelativeY(owner.getHeight() /2);
		setXY();
		this.owner.allPOI.add(this);
	}
	
	public POI(Entity owner,float offsetX, float offsetY){
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
	
	void setXY2(){
		setX((float) (((owner.getX() + getRelativeX()) - owner.centrePoint.getX()) * Math.cos(Math.toRadians(owner.getDirection())) - ((owner.getY() + getRelativeY()) - owner.centrePoint.getY()) * Math.sin(Math.toRadians(owner.getDirection())) + owner.centrePoint.getX()));
		setY((float) (((owner.getY() + getRelativeY()) - owner.centrePoint.getY()) * Math.cos(Math.toRadians(owner.getDirection())) + ((owner.getX() + getRelativeX()) - owner.centrePoint.getX()) * Math.sin(Math.toRadians(owner.getDirection())) + owner.centrePoint.getY()));
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
		String finalString = "\nPOI: X="+getX()+" Y="+getY();
		return finalString;
	}
}
