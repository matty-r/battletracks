package com.mattyr.battletracks.backend;

import com.badlogic.gdx.graphics.Texture;

public class Projectile extends Entity{
	Vehicle owner;

	public Projectile(float startX, float startY, Texture loadTexture, Vehicle owner ,String name){
		super(startX, startY, loadTexture, name);
		this.owner = owner;
	}
	
	@Override
	public String toString(){
		return super.toString()+"\nOwner Name:"+owner.getName();
	}
}
