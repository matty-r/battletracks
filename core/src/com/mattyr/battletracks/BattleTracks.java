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
	static private Boolean DEBUG_ENABLE = true;
	

	@Override
	public void create () {
		batch = new SpriteBatch();
		screenText = new BitmapFont();
		screenText.setColor(Color.RED);
		gameText = new BitmapFont();
		gameText.setColor(Color.RED);
		
		groundTxt = new Texture(Gdx.files.internal("ground2.png"));
		tankBody = new Texture(Gdx.files.internal("tankBody.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true); 
		player1 = new Tank(playerSpawn.x, playerSpawn.y, tankBody, "Player 1");
		player1.addTurret("Player 1 Turret");
		vehicles.add(player1);
		
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
		if((Math.random() * 5000) > 4990){
			spawnEnemy();
		}
		getKeyPressed();
		batch.draw(groundTxt, 0,0);
		for(Vehicle vehicle : vehicles){
			projectileCollision(vehicle.getTurret().getBulletObjects());
			if(vehicle != null && vehicles.contains(vehicle)){
			batch.draw(vehicle.getRegion(), vehicle.getX(), vehicle.getY(), vehicle.centrePoint.getRelativeX() , vehicle.centrePoint.getRelativeY() ,vehicle.getWidth(), vehicle.getHeight(), 1f, 1f, vehicle.getDirection());
		if(DEBUG_ENABLE)
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
				if(DEBUG_ENABLE){
					for(POI eachPOI : bullet.allPOI){
						gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
						gunDotSprite.draw(batch);
					}
				}
			}
			
			if(DEBUG_ENABLE)
				for(POI eachPOI : vehicle.getTurret().allPOI){
					gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
					gunDotSprite.draw(batch);
				}
			}
		}
		
		if(DEBUG_ENABLE)
			screenText.draw(batch, debugText(), 10, 1080);
		
		gameText.draw(batch, gameTextString, Gdx.graphics.getWidth() /2, 1080);
		batch.end();
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
    	
    	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
    		player1.getTurret().makeBullet();
    	/**
    	 * Used for testing purposes only
    	 */
    	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
    		if(vehicles.contains(player2)){
    			player2.getTurret().destroy();
    			player2.destroy();
    			
    			player2.setX((float)(Math.random() * -300));
    			player2.setY((float)(Math.random() * -300));
    			player2.setPOIs();
    			player2.addTurret("Player 2 Turret");
    		} else {
    			player2 = new Tank((float)(Math.random() * -300), (float)(Math.random() * -300), tankBody, "Player "+(Math.round(Math.random()*100)));
        		player2.addTurret("Player 2 Turret");
    			vehicles.add(player2);
    		}
    	}
    	
   	
        return true;
    }
    
    private void spawnEnemy(){
    	Vehicle randomEnemy = new Tank((float)(Math.random() * -300), (float)(Math.random() * -300), tankBody, "Player "+(Math.round(Math.random()*100)));
    	randomEnemy.addTurret(randomEnemy.getName()+" Turret");
    	randomEnemy.setX((float)(Math.random() * -300));
    	randomEnemy.setY((float)(Math.random() * -300));
    	randomEnemy.setPOIs();
    	vehicles.add(randomEnemy);
    }
    
    private boolean projectileCollision(ArrayList<Projectile> arrayList){
		ArrayList<Projectile> removePOI = new ArrayList<Projectile>();
		ArrayList<Entity> removeVeh = new ArrayList<Entity>();
		for(Projectile projectile : arrayList){			
			//for(POI firstPOI : projectile.allPOI){
				if(projectile.getX() > Gdx.graphics.getWidth() || projectile.getY() > Gdx.graphics.getHeight() || projectile.getX() < 0 || projectile.getY() < 0){
					projectile.setHealthValue(0);
				}
					
				for(Entity testEntity : vehicles){
					if(testEntity != projectile.getOwnerVehicle()){
					if(getInsideHeavy(projectile,testEntity)){
						testEntity.setHealthValue(testEntity.getHealthValue() - projectile.getHealthValue());
						gameTextString = projectile.getOwnerVehicle().getName() + " hit " + testEntity.getName() + " for " + projectile.getHealthValue() + " points of damage. " + testEntity.getHealthValue() +" remaining.";
						projectile.setHealthValue(0);
						if(testEntity.getHealthValue() <= 0){
							if(!removeVeh.contains(testEntity)){
								removeVeh.add(testEntity);
							}
						}
					}
					}
				}
				if(projectile.getHealthValue() <= 0)
					if(!removePOI.contains(projectile))
						removePOI.add(projectile);
			}
		
		for(Projectile poi : removePOI){
			poi.destroy();
			arrayList.remove(arrayList.indexOf(poi));
		}
		for(Entity vehicle : removeVeh){
			if(vehicle == player1){
			
			} else {
				vehicle.destroy();
				vehicles.remove(vehicle);
				return true;
			}
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
