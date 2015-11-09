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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
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
	//Used to buffer out the offscreen edge, used in spawning enemies
	private static int OFFSCREEN_BUFFER = 300;
	ArrayList<Entity> allEntities = new ArrayList<Entity>();
	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	ArrayList<Item> items = new ArrayList<Item>();
	OrthographicCamera camera;
	BitmapFont screenText;
	BitmapFont gameText;
	String gameTextString = "";
	Pixmap poiMarker;
	Texture poiMarkerTexture;
	Sprite poiMarkerSprite; 
	Texture fireRateUpTexture;
	Texture accuracyUpTexture;
	Texture speedUpTexture;
	Texture healthUpTexture;
	ShapeRenderer healthBar;
	private Boolean debugEnable = false;
	long spawnDelayMillis = 1000;
	long lastSpawnTime = 0;
	Vector2 mouseVector;
	Entity mouseOver;
	
	public Entity getMouseOver() {
		for(Vehicle tempVehicle : vehicles){
			if(tempVehicle.isOverlapping(mouseVector)){
				return tempVehicle;
			}
		}
		for(Item tempItem : items){
			if(tempItem.isOverlapping(mouseVector)){
				return tempItem;
			}
		}
		
		return null;
	}

	public void setMouseOver(Vehicle mouseOver) {
		this.mouseOver = mouseOver;
	}

	//Used in drawTextToMouse
	GlyphLayout textLayout = new GlyphLayout();

	@Override
	public void create () {
		batch = new SpriteBatch();
		screenText = new BitmapFont();
		screenText.setColor(Color.RED);
		gameText = new BitmapFont();
		gameText.setColor(Color.RED);
		fireRateUpTexture = new Texture(Gdx.files.internal("fireRateUp.png"));
		accuracyUpTexture = new Texture(Gdx.files.internal("accuracyUp.png"));
		speedUpTexture = new Texture(Gdx.files.internal("speedUp.png"));
		healthUpTexture = new Texture(Gdx.files.internal("healthUp.png"));
		groundTxt = new Texture(Gdx.files.internal("ground2.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true,true); 
		player1 = new Tank(playerSpawn.x, playerSpawn.y, "Player 1");
		vehicles.add(player1);
		healthBar = new ShapeRenderer();
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 1920, 1080);
	    poiMarker = new Pixmap(2,2,Pixmap.Format.RGBA8888);
	    poiMarker.setColor(Color.RED);
	    poiMarker.fill();
	    poiMarker.drawCircle(1, 1, 1);
	    poiMarkerTexture = new Texture(poiMarker);
	    poiMarker.dispose();
	    poiMarkerSprite = new Sprite(poiMarkerTexture);
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
			float minXY = (float) (Math.random() * (OFFSCREEN_BUFFER)) * -1;
	    	float minX = (float) (Math.random() * (Gdx.graphics.getWidth() + OFFSCREEN_BUFFER));
	    	float minY = (float) (Math.random() * (Gdx.graphics.getHeight() + OFFSCREEN_BUFFER));
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
	    			finalX += (Gdx.graphics.getWidth() - finalX) + OFFSCREEN_BUFFER;
	        	else
	        		finalY += (Gdx.graphics.getHeight() - finalY) + OFFSCREEN_BUFFER;
	    	
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
		mouseVector.y = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		if((Math.random() * 100) > 99.8f){
			spawnEntity(new Tank(0,0,""));
		}
		if((Math.random() * 100) > 99.8f){
			int tempRandom = (int) (Math.random() * 100);
			if(tempRandom <= 25)
				spawnEntity(new Item(0, 0, accuracyUpTexture, "accuracy up"));
			else if(tempRandom > 25 && tempRandom < 50)
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
			
			for(Item item : items)
				item.draw(batch, 0);
			
			itemCollision();
			
			for(Vehicle vehicle : tempVehicles){
				projectileCollision(vehicle.getTurret().getBulletObjects());
				
				if(vehicles.contains(vehicle)){
				vehicle.draw(batch, 0);
				
					if(vehicle.getTurret() != null && vehicle == player1)
						player1.getTurret().turnToFacePoint(mouseVector);
				
					if(vehicle != player1){
					vehicle.getTurret().turnToFaceEntity(player1);
					vehicle.turnToFaceEntity(player1);
					if(vehicle.turnToFaceEntity(player1))
						vehicle.getTurret().firePrimary();
					
					vehicle.drive(false);
					}
				
					vehicle.getTurret().draw(batch, 0);
					for(Projectile bullet : vehicle.getTurret().getBulletObjects()){
						if(bullet.isTrackTarget() && bullet.getOwnerVehicle() == player1)
							bullet.drive(false, getMouseOver());
						else
							bullet.drive(false);
						bullet.draw(batch, 0);
						}
				}	
			}
		} finally {		
	
		
		gameText.draw(batch, gameTextString, Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
		batch.end();
		for(Vehicle tempVehicle : vehicles){
			healthBar.begin(ShapeType.Filled);
			healthBar.setColor(Color.RED);
			float healthBarValue = (float) (tempVehicle.getWidth()/100) * (((float) (tempVehicle.getCurrentHealthValue() ) / tempVehicle.getMaxHealthValue())*100);
			healthBar.rect(tempVehicle.getX(),tempVehicle.getY(),healthBarValue ,1);
			healthBar.end();
		}
		debugShow();
	}
	}
	
	
	public void debugShow(){
		if(debugEnable){
			batch.begin();
			gameText.draw(batch, debugText(), 2, Gdx.graphics.getHeight());
			batch.end();
			for(Vehicle tempVehicle : vehicles){
				batch.begin();
				if(getMouseOver() == tempVehicle){
					for(POI eachPOI : tempVehicle.allPOI){
						poiMarkerSprite.setPosition(eachPOI.getX(), eachPOI.getY());
						poiMarkerSprite.draw(batch);
					}
					//setMouseOver(tempVehicle);
				drawTextToMouse(tempVehicle.toString());
				}
				
				if(getMouseOver() == tempVehicle.getTurret()){
					for(POI eachPOI : tempVehicle.getTurret().allPOI){
						poiMarkerSprite.setPosition(eachPOI.getX(), eachPOI.getY());
						poiMarkerSprite.draw(batch);
					}
					drawTextToMouse(tempVehicle.getTurret().toString());
				}
				batch.end();	
			}
				
		
			for(Item tempItem : items){
				if(getMouseOver() == tempItem){
				batch.begin();
				for(POI eachPOI : tempItem.allPOI){
					poiMarkerSprite.setPosition(eachPOI.getX(), eachPOI.getY());
					poiMarkerSprite.draw(batch);
				}

				drawTextToMouse(tempItem.toString());
				batch.end();
				}
			}
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
				"\nMouse> x="+ Gdx.input.getX() + " y="+Gdx.input.getY();
				
	}
	/**
	 * Ensures that when text is draw, the text doesn't fall off the screen.
	 * @param text
	 */
	public void drawTextToMouse(String text){
		
		textLayout.setText(gameText,text);
		float width = textLayout.width;// contains the width of the current set text
		float height = textLayout.height; // contains the height of the current set text
		float textY = mouseVector.y + height;
		float textX = mouseVector.x + width;
		
		if(textY > Gdx.graphics.getHeight())
			textY = Gdx.graphics.getHeight();
		if(textX > Gdx.graphics.getWidth())
			textX = Gdx.graphics.getWidth() - width;
		else 
			textX = mouseVector.x;
			
		gameText.draw(batch,text, textX,textY);
	}
	
    public void getKeyPressed() {
    	player1.getKeyPressed();
    	
    	if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
    		debugEnable = !debugEnable;
    	/**
    	 * Used for testing purposes only
    	 */
    	if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
   			spawnEntity(new Tank(0,0,""));
    	} 
    	if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
			spawnEntity(new Item(0, 0, fireRateUpTexture, "fire rate up"));
			spawnEntity(new Item(0, 0, speedUpTexture, "speed up"));
			spawnEntity(new Item(0, 0, healthUpTexture, "health up"));
			spawnEntity(new Item(0, 0, accuracyUpTexture, "accuracy up"));
    	}
    }
    
    private void spawnEntity(Entity entityType){
    	SpawnPoint newSpawn;
    	
    	if (entityType instanceof Tank) {
			newSpawn = new SpawnPoint(true, false);
			
			//newSpawn.x= (int) mouseVector.x;
    		//newSpawn.y= (int) mouseVector.y;
			Tank newEntity = (Tank) new Tank(newSpawn.x, newSpawn.y, "Player "+(Math.round(Math.random()*100)));
			newEntity.addTurret(newEntity.getName()+" Turret");
			newEntity.setPOIs();
			newEntity.getTurret().setReloadDelayMillis((long) (newEntity.getTurret().getReloadDelayMillis() * 1.25f));
			newEntity.setForwardSpeed(newEntity.getForwardSpeed() * 0.75f);
			vehicles.add((Vehicle) newEntity);
		} else if(entityType instanceof Item){
			newSpawn = new SpawnPoint(true, true);
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
	    		newSpawn.x= (int) mouseVector.x;
	    		newSpawn.y= (int) mouseVector.y;
			}
			Item newEntity = (Item) new Item(newSpawn.x, newSpawn.y, entityType.getRegion().getTexture(), entityType.getName());
			newEntity.setPOIs();
			items.add(newEntity);
		}

    	
    	
    }
    
    private boolean itemCollision(){
		ArrayList<Item> removeItems = new ArrayList<Item>();
		for(Item item : items){			
			if(item.isCollidedWith(player1)){
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
					if(projectile.isCollidedWith(testEntity)){
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
