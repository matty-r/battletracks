package com.mattyr.battletracks.backend;

import com.badlogic.gdx.graphics.Texture;

public class Item extends Entity {
	long spawnTime = System.currentTimeMillis();
	long timeToLive = 10000; //Millis
	long despawnTime = spawnTime + timeToLive;
	
	public Item(float startX,float startY,Texture loadTexture,String name){
		super(startX, startY, loadTexture, name, 9001);
	}
	
	public String itemAction(Vehicle actionOn){
		if(getName().toLowerCase().contains("fire rate up")){
			actionOn.getTurret().setReloadDelayMillis((long)( ((float) actionOn.getTurret().getReloadDelayMillis()) * 0.9));
			return "Improved reload speed of " + actionOn.getName();
		}else if(getName().toLowerCase().contains("speed up")){
			actionOn.setForwardSpeed((float) (actionOn.getForwardSpeed() * 1.1));
			actionOn.setReverseSpeed((float) (actionOn.getReverseSpeed() * 1.1));
			return "Improved speed of " + actionOn.getName();
		} else if(getName().toLowerCase().contains("health up")){
			actionOn.setCurrentHealthValue(actionOn.getCurrentHealthValue() * 2);
			return "Improved health of " + actionOn.getName();
		} else if(getName().toLowerCase().contains("accuracy up")){
			actionOn.getTurret().setAccuracy((float) (actionOn.getTurret().getAccuracy() * 1.1));
			return "Improved accuracy of " + actionOn.getName();
		}
		
		return "No valid actions!";
	}
}
