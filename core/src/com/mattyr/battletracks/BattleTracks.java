package com.mattyr.battletracks;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Projectile;
import com.mattyr.battletracks.backend.Vehicle;


public class BattleTracks extends ApplicationAdapter {
	SpriteBatch batch;
	Texture groundTxt;
	Texture bulletTxt;
	Texture tankBody;
	Vehicle player1; 
	Vehicle player2;
	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	OrthographicCamera camera;
	BitmapFont screenText;
	BitmapFont gameText;
	String gameTextString = "";
	Pixmap tankDot;
	Pixmap gunDot;
	Texture tankDotTexture;
	Sprite tankDotSprite; 
	Texture gunDotTexture;
	Sprite gunDotSprite;
	ShapeRenderer healthBar;
	private Boolean debugEnable = false;
	

	@Override
	public void create () {
		batch = new SpriteBatch();
		screenText = new BitmapFont();
		screenText.setColor(Color.RED);
		gameText = new BitmapFont();
		gameText.setColor(Color.RED);
		
		groundTxt = new Texture(Gdx.files.internal("ground2.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true); 
		//player1 = new Helicopter(playerSpawn.x, playerSpawn.y, new Texture(Gdx.files.internal("Helicopter.png")), "Player 1");
		player1 = new Tank(playerSpawn.x, playerSpawn.y, "Player 1");
		player1.addTurret("Player 1 Turret");
		vehicles.add(player1);
		healthBar = new ShapeRenderer();
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 1920, 1080);
	    tankDot = new Pixmap(2,2,Pixmap.Format.RGBA8888);
	    tankDot.setColor(Color.RED);
	    tankDot.fill();
	    tankDot.drawCircle(1, 1, 1);
	    gunDot = new Pixmap(2,2,Pixmap.Format.RGBA8888);
	    gunDot.setColor(Color.PINK);
	    gunDot.fill();
	    gunDot.drawCircle(1, 1, 1);
	    tankDotTexture = new Texture(tankDot);
	    tankDot.dispose();
	    
	    tankDotSprite = new Sprite(tankDotTexture);
	    
	    gunDotTexture = new Texture(gunDot);
	    gunDot.dispose();
	    
	    gunDotSprite = new Sprite(gunDotTexture);
	}

	public static class SpawnPoint{
		int x;
		int y;
		
		public SpawnPoint(boolean random){
			if(random){
				x = randomX();
				y = randomY();
			} else {
				x = Gdx.graphics.getWidth() /2;
				y = Gdx.graphics.getHeight() /2;
			}
		}
		
		public int randomX(){
			return (int) Math.round((Math.random() * Gdx.graphics.getWidth()));
		}
		
		public int randomY(){
			return (int) Math.round((Math.random() * Gdx.graphics.getHeight()));
		}
	}
	
	
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
        batch.setProjectionMatrix(camera.combined);
        
		batch.begin();
				
		if((Math.random() * 100) > 99.8f){
			spawnEnemy();
		}
		getKeyPressed();
		
		try{
			batch.draw(groundTxt, 0,0);
			ArrayList<Vehicle> tempVehicles = new ArrayList<Vehicle>();
			for(Vehicle tempVehicle : vehicles)
				tempVehicles.add(tempVehicle);
			
		for(Vehicle vehicle : tempVehicles){
			projectileCollision(vehicle.getTurret().getBulletObjects());
				
			if(vehicles.contains(vehicle)){
			batch.draw(vehicle.getRegion(), vehicle.getX(), vehicle.getY(), vehicle.centrePoint.getRelativeX() , vehicle.centrePoint.getRelativeY() ,vehicle.getWidth(), vehicle.getHeight(), 1f, 1f, vehicle.getDirection());
		if(debugEnable)
			for(POI eachPOI : vehicle.allPOI){
				tankDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
				tankDotSprite.draw(batch);
			}
		
		if(vehicle.getTurret() != null)
			player1.getTurret().faceMouse();
			if(vehicle != player1){
				vehicle.getTurret().faceVehicle(player1);
				vehicle.faceVehicle(player1);
				vehicle.getTurret().makeBullet();
				vehicle.drive(false);
			}
			batch.draw(vehicle.getTurret().getRegion(), vehicle.getTurret().getX() ,vehicle.getTurret().getY(), vehicle.getTurret().centrePoint.getRelativeX() , vehicle.getTurret().centrePoint.getRelativeY() ,vehicle.getTurret().getWidth(), vehicle.getTurret().getHeight(), 1f, 1f, vehicle.getTurret().getDirection());
			for(Projectile bullet : vehicle.getTurret().getBulletObjects()){
				bullet.drive(false);
				batch.draw(bullet.getRegion(), bullet.getX(), bullet.getY(), bullet.centrePoint.getRelativeX() , bullet.centrePoint.getRelativeY() ,bullet.getWidth(), bullet.getHeight(), 1f, 1f, bullet.getDirection());
				if(debugEnable){
					for(POI eachPOI : bullet.allPOI){
						gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
						gunDotSprite.draw(batch);
					}
				}
			}
			
			if(debugEnable)
				for(POI eachPOI : vehicle.getTurret().allPOI){
					gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
					gunDotSprite.draw(batch);
				}
			}
		}
		} finally {		
	
		if(debugEnable)
			screenText.draw(batch, debugText(), 2, Gdx.graphics.getHeight());
		gameText.draw(batch, gameTextString, Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
		batch.end();
		
		
		healthBar.begin(ShapeType.Filled);
		healthBar.setColor(Color.RED);
		for(Vehicle tempVehicle : vehicles){
			float healthBarValue = (float) (tempVehicle.getWidth()/100) * (((float) (tempVehicle.getCurrentHealthValue() ) / tempVehicle.getMaxHealthValue())*100);
			healthBar.rect(tempVehicle.getX(),tempVehicle.getY()- 20,healthBarValue ,1);
		}
		healthBar.end();
		}
	}
	
	public String debugText(){
		String bulletsString = "";
		for(Projectile bullet :player1.getTurret().getBulletObjects())
			bulletsString += bullet.toString();
		
		return  player1.toString()+
				"\n\nFPS="+Gdx.graphics.getFramesPerSecond() + "\n\n" +
				player1.getTurret().toString() +
				"\n\nMouse.x"+ Gdx.input.getX() +
				"\nMouse.y"+Gdx.input.getY()+
				"\nVehicles:"+vehicles.size()+
				"\n"+bulletsString;
	}
	
    public boolean getKeyPressed() {
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.S)){
        	player1.drive(true);
    		if(Gdx.input.isKeyPressed(Input.Keys.A))
    			player1.turn(false);
    		else
    			if(Gdx.input.isKeyPressed(Input.Keys.D))
        			player1.turn(true);
    		
    	} else {
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			player1.drive(false);
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.A))
			player1.turn(true);
    	if(Gdx.input.isKeyPressed(Input.Keys.D))
			player1.turn(false);
    	if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
    		Gdx.app.exit();
   		
    	}
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
    		debugEnable = !debugEnable;
    	
    	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
    		player1.getTurret().makeBullet();
    	/**
    	 * Used for testing purposes only
    	 */
    	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
    		spawnEnemy();
    	}
    		
    	
        return true;
    }
    
    private void spawnEnemy(){
    	float minXY = (float) (Math.random() * (0 + 300)) * -1;
    	float minX = (float) (Math.random() * 2220);
    	float minY = (float) (Math.random() * 1380);
    	float finalX = 0;
    	float finalY = 0;
    	
    	if(Math.random() * 1 > .5f)
    		finalX = minXY;
    	else
    		finalX = minX;
    	
    	if(Math.random() * 1 > .5f)
    		finalY = minXY;
    	else
    		finalY = minY;
    	
    	if(finalX < Gdx.graphics.getWidth() && finalX > 0 && finalY < Gdx.graphics.getHeight() && finalY > 0)
    		if(Math.random() * 1 > .5f)
    			finalX += (Gdx.graphics.getWidth() - finalX) + 300;
        	else
        		finalY += (Gdx.graphics.getHeight() - finalY) + 300;
    		
    	
    	Vehicle randomEnemy = new Tank(finalX, finalY, "Player "+(Math.round(Math.random()*100)));
    	randomEnemy.addTurret(randomEnemy.getName()+" Turret");
    	randomEnemy.setPOIs();
    	vehicles.add(randomEnemy);
    }
    
    private boolean projectileCollision(ArrayList<Projectile> arrayList){
		ArrayList<Projectile> removePOI = new ArrayList<Projectile>();
		ArrayList<Entity> removeVeh = new ArrayList<Entity>();
		for(Projectile projectile : arrayList){			
			//for(POI firstPOI : projectile.allPOI){
				if(projectile.getX() > Gdx.graphics.getWidth() || projectile.getY() > Gdx.graphics.getHeight() || projectile.getX() < 0 || projectile.getY() < 0){
					projectile.setCurrentHealthValue(0);
				}
					
				for(Entity testEntity : vehicles){
					if(testEntity != projectile.getOwnerVehicle()){
					if(getInsideHeavy(projectile,testEntity)){
						testEntity.setCurrentHealthValue(testEntity.getCurrentHealthValue() - projectile.getCurrentHealthValue());
						gameTextString = projectile.getOwnerVehicle().getName() + " hit " + testEntity.getName() + " for " + projectile.getCurrentHealthValue() + " points of damage. " + testEntity.getCurrentHealthValue() +" remaining.";
						projectile.setCurrentHealthValue(0);
						if(testEntity.getCurrentHealthValue() <= 0){
							if(!removeVeh.contains(testEntity)){
								removeVeh.add(testEntity);
							}
						}
					}
					}
				}
				if(projectile.getCurrentHealthValue() <= 0)
					if(!removePOI.contains(projectile))
						removePOI.add(projectile);
			}
		
		for(Projectile poi : removePOI){
			poi.destroy();
			arrayList.remove(arrayList.indexOf(poi));
		}
		for(Entity vehicle : removeVeh){
				vehicle.destroy();
				vehicles.remove(vehicle);
				return true;
		}
		return false;
    }
	
    /**
     * This is very resource heavy collision detection 
     * TODO improve/remove
     * @param src
     * @param dst
     * @return
     */
    private boolean getInsideHeavy(Entity src,Entity dst){
    	
    	for(POI sp1 : src.allPOI)
    		for(POI sp2 : src.allPOI)
    			if(sp2 != sp1)
    				for(POI dp1 : dst.allPOI)
    					for(POI dp2 : dst.allPOI)
    						if(dp2 != dp1)
    							if(Intersector.intersectSegments(sp1, sp2, dp1, dp2, null))
    								return true;
    	return false;
    }
    
    @Override
    public void resize(int width, int height) {
    	
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
	
    @Override
    public void dispose() {
        batch.dispose();
    }
}
