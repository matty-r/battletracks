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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mattyr.battletracks.backend.Entity;
import com.mattyr.battletracks.backend.Item;
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
	ArrayList<Item> items = new ArrayList<Item>();
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
	Texture fireRateUpTexture;
	Texture speedUpTexture;
	Texture healthUpTexture;
	ShapeRenderer healthBar;
	private Boolean debugEnable = false;
	long spawnDelayMillis = 1000;
	long lastSpawnTime = 0;
	Vector2 mouseVector;	

	@Override
	public void create () {
		batch = new SpriteBatch();
		screenText = new BitmapFont();
		screenText.setColor(Color.RED);
		gameText = new BitmapFont();
		gameText.setColor(Color.RED);
		fireRateUpTexture = new Texture(Gdx.files.internal("fireRateUp.png"));
		speedUpTexture = new Texture(Gdx.files.internal("speedUp.png"));
		healthUpTexture = new Texture(Gdx.files.internal("healthUp.png"));
		groundTxt = new Texture(Gdx.files.internal("ground2.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true,true); 
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
	    
	    mouseVector = new Vector2(Gdx.input.getX(), Gdx.input.getX());
	}

	public static class SpawnPoint{
		int x;
		int y;
		
		public SpawnPoint(boolean random, boolean onScreen){
			if(random){
				if(onScreen) {
					x = randomX();
					y = randomY();
				} else {
					offScreenXY();
				}
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
		
		public void offScreenXY(){
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
	    	
	    	x = (int) finalX;
	    	y = (int) finalY;
		}
	}
	
	
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
        batch.setProjectionMatrix(camera.combined);
        
		batch.begin();
		mouseVector.x = Gdx.input.getX();
		mouseVector.y = Gdx.input.getY();
		
		if((Math.random() * 100) > 99.8f){
			spawnEntity(new Tank(0,0,""));
		}
		if((Math.random() * 100) > 99.8f){
			int tempRandom = (int) (Math.random() * 100);
			if(tempRandom < 50)
				spawnEntity(new Item(0, 0, healthUpTexture, "health up"));
			else if(tempRandom >= 50 && tempRandom < 75)
				spawnEntity(new Item(0, 0, fireRateUpTexture, "fire rate up"));
			else if(tempRandom >= 75 && tempRandom <= 100)
				spawnEntity(new Item(0, 0, speedUpTexture, "speed up"));
		}
		getKeyPressed();
		

		
		try{
			batch.draw(groundTxt, 0,0);
			ArrayList<Vehicle> tempVehicles = new ArrayList<Vehicle>();
			for(Vehicle tempVehicle : vehicles)
				tempVehicles.add(tempVehicle);
			
			for(Item item : items){
				item.draw(batch, 0);
				//batch.draw(item.getRegion(), item.getX(), item.getY(), item.centrePoint.getX(), item.centrePoint.getY(), item.getWidth(), item.getHeight(), 1f, 1f, item.getRotation());
				if(debugEnable)
					for(POI eachPOI : item.allPOI){
						gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
						gunDotSprite.draw(batch);
					}
			}
			
		itemCollision();
		for(Vehicle vehicle : tempVehicles){
			projectileCollision(vehicle.getTurret().getBulletObjects());
				
			if(vehicles.contains(vehicle)){
			vehicle.draw(batch, 0);
				
			//batch.draw(vehicle.getRegion(), vehicle.getX(), vehicle.getY(), vehicle.centrePoint.getRelativeX() , vehicle.centrePoint.getRelativeY() ,vehicle.getWidth(), vehicle.getHeight(), 1f, 1f, vehicle.getRotation());
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
			batch.draw(vehicle.getTurret().getRegion(), vehicle.getTurret().getX() ,vehicle.getTurret().getY(), vehicle.getTurret().centrePoint.getRelativeX() , vehicle.getTurret().centrePoint.getRelativeY() ,vehicle.getTurret().getWidth(), vehicle.getTurret().getHeight(), 1f, 1f, vehicle.getTurret().getRotation());
			for(Projectile bullet : vehicle.getTurret().getBulletObjects()){
				bullet.drive(false);
				bullet.draw(batch, 0);
				//batch.draw(bullet.getRegion(), bullet.getX(), bullet.getY(), bullet.centrePoint.getRelativeX() , bullet.centrePoint.getRelativeY() ,bullet.getWidth(), bullet.getHeight(), 1f, 1f, bullet.getRotation());
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
		int projectileCount = 0;
		for(Vehicle vehicle : vehicles)
			projectileCount += vehicle.getTurret().projectiles.size();
		
		return  "Screen Activity:"+
				"\nVehicles:"+vehicles.size()+
				"\nProjectiles:"+projectileCount+
				"\nFPS="+Gdx.graphics.getFramesPerSecond() +
				"\nMouse> x="+ Gdx.input.getX() + " y="+Gdx.input.getY()+
				"\n"+player1.toString()+ "\n" +
				player1.getTurret().toString();
				
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
    		if(System.currentTimeMillis() - lastSpawnTime >= spawnDelayMillis){
    			spawnEntity(new Tank(0,0,""));
    			lastSpawnTime = System.currentTimeMillis();
    		}
    	} 
    	if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
    		if(System.currentTimeMillis() - lastSpawnTime >= spawnDelayMillis){
    			spawnEntity(new Item(0, 0, fireRateUpTexture, "fire rate up"));
    			spawnEntity(new Item(0, 0, speedUpTexture, "speed up"));
    			spawnEntity(new Item(0, 0, healthUpTexture, "health up"));
    		}
			lastSpawnTime = System.currentTimeMillis();
    	}

    	
        return true;
    }
    
    private void spawnEntity(Entity entityType){
    	SpawnPoint newSpawn;
    	
    	if (entityType instanceof Tank) {
    		newSpawn = new SpawnPoint(true, false);
			Tank newEntity = (Tank) new Tank(newSpawn.x, newSpawn.y, "Player "+(Math.round(Math.random()*100)));	
			newEntity.addTurret(newEntity.getName()+" Turret");
			newEntity.setPOIs();
			newEntity.getTurret().setReloadDelayMillis((long) (newEntity.getTurret().getReloadDelayMillis() * 1.25f));
			newEntity.setForwardSpeed(newEntity.getForwardSpeed() * 0.75f);
			vehicles.add((Vehicle) newEntity);
		} else if(entityType instanceof Item){
			newSpawn = new SpawnPoint(true, true);
			Item newEntity = (Item) new Item(newSpawn.x, newSpawn.y, entityType.getRegion().getTexture(), entityType.getName());
			newEntity.setPOIs();
			items.add(newEntity);
		}

    	
    	
    }
    
    private boolean itemCollision(){
		ArrayList<Item> removeItems = new ArrayList<Item>();
		for(Item item : items){			
			if(getEntityCollision(item,player1)){
				item.setCurrentHealthValue(0);
				gameTextString = item.itemAction(player1);
			}

				if(item.getCurrentHealthValue() <= 0)
					if(!removeItems.contains(item))
						removeItems.add(item);
			}
		
		for(Item item : removeItems){
				item.destroy();
				items.remove(item);
				return true;
		}
		return false;
    }
    
    private boolean projectileCollision(ArrayList<Projectile> arrayList){
		ArrayList<Projectile> removePOI = new ArrayList<Projectile>();
		ArrayList<Entity> removeVeh = new ArrayList<Entity>();
		for(Projectile projectile : arrayList){			
				if(projectile.getX() > Gdx.graphics.getWidth() || projectile.getY() > Gdx.graphics.getHeight() || projectile.getX() < 0 || projectile.getY() < 0){
					projectile.setCurrentHealthValue(0);
				}
					
				for(Entity testEntity : vehicles){
					if(testEntity != projectile.getOwnerVehicle()){
					if(getEntityCollision(projectile,testEntity)){
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
    private boolean getEntityCollision(Entity src,Entity dst){
    	
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
    
    /**
     * Used
     * TODO improve/remove
     * @param src
     * @param dst
     * @return
     */
    /*private boolean getEntityOverlap(float srcX, float srcY, Entity dst){
    	Vector2 sourcePoint = new Vector2(srcX, srcY);
    	
    	if(sourcePoint.x <)
    	
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
    */
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
