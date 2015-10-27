package com.mattyr.battletracks.backend;

public class Entity {
	private float forwardSpeed;
	private float reverseSpeed;
	private float turnSpeed;
	private float direction;
	private float width;
	private float height;
	private float x;
	private float y;
	private float realX;
	private float realY;
	
	public void setTurnSpeed(float speed){
		turnSpeed = speed;
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
	
	public float getRealX() {
		return realX;
	}

	public void setRealX(float realX) {
		this.realX = realX;
	}

	public float getRealY() {
		return realY;
	}

	public void setRealY(float realY) {
		this.realY = realY;
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
	
	public class CentrePoint{
		private float x;
		private float y;
		private float relativeX;
		private float relativeY;
		
		CentrePoint(){
			setRelativeX(Entity.this.getWidth() /2);
			setRelativeY(Entity.this.getHeight() /2);
			setXY();
		}
		
		CentrePoint(float offsetX, float offsetY){
			setRelativeX(offsetX);
			setRelativeY(offsetY);
			setXY();
		}
		
		void setXY(){
			setX(Entity.this.getX() + this.getRelativeX());
			setY(Entity.this.getY() + this.getRelativeY());
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
	}
}
