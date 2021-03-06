package com.mattyr.battletracks.backend;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mattyr.battletracks.backend.Weapon;
import com.mattyr.battletracks.Helicopter;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.POI;

public class Vehicle extends Entity {
	private Weapon turret;
	public POI hardPoint;
	
	public Vehicle(float startX, float startY, Texture loadTexture, String name,int healthValue){
		super(startX, startY, loadTexture, name, healthValue);
	}
	
	/*public void addTurret(String name){
		setTurret(new TankTurret(getX(), getY(), new Texture(Gdx.files.internal("tankTurret.png")), this, name));
	}*/	
	
	public void getKeyPressed(){
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
        	drive(true);
    		if(Gdx.input.isKeyPressed(Input.Keys.A))
    			if (this instanceof Helicopter) {
					strafe(false);
					
				} else
					turn(false);
    		else
    			if(Gdx.input.isKeyPressed(Input.Keys.D))
    				if (this instanceof Helicopter) {
    					strafe(true);
    				} else {
    					turn(true);
    				}
    	} else {
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			drive(false);
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.A))
    		if (this instanceof Helicopter) {
				strafe(false);
				
			} else 
			turn(true);
    	if(Gdx.input.isKeyPressed(Input.Keys.D))
    		if (this instanceof Helicopter) {
				strafe(true);
				
			} else 
			turn(false);
    	if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
    		Gdx.app.exit();
   		
    	}
    	
    	
    	
    	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
    		getTurret().firePrimary();
    	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
    		getTurret().fireSecondary();
	}

	public void setPOIs(){
		super.setPOIs();		
		
		if(hardPoint != null){		
			hardPoint.setX((float) (((getX() + hardPoint.getRelativeX()) - centrePoint.getX()) * Math.cos(Math.toRadians(getRotation())) - ((getY() + hardPoint.getRelativeY()) - centrePoint.getY()) * Math.sin(Math.toRadians(getRotation())) + centrePoint.getX()));
			hardPoint.setY((float) (((getY() + hardPoint.getRelativeY()) - centrePoint.getY()) * Math.cos(Math.toRadians(getRotation())) + ((getX() + hardPoint.getRelativeX()) - centrePoint.getX()) * Math.sin(Math.toRadians(getRotation())) + centrePoint.getY()));
		}
		if(turret != null){
			getTurret().setPOIs();
		}
	}
	

	public Weapon getTurret() {
		return turret;
	}
	
	@Override
	public void destroy(){
		turret.destroy();
		super.destroy();
	}

	public void setTurret(Weapon turret) {
		this.turret = turret;
	}

	@Override
	public String toString(){
		return super.toString();
	}
}
