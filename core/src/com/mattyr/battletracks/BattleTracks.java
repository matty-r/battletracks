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
import com.mattyr.battletracks.backend.POI;
import com.mattyr.battletracks.backend.Projectile;
import com.mattyr.battletracks.backend.Vehicle;

public class BattleTracks extends ApplicationAdapter {
	SpriteBatch batch;
	Texture groundTxt;
	Texture bulletTxt;
	Texture tankBody;
	Vehicle player1; 
	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	OrthographicCamera camera;
	BitmapFont screenText;
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
		groundTxt = new Texture(Gdx.files.internal("ground.png"));
		tankBody = new Texture(Gdx.files.internal("tankBody.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true); 
		player1 = new Tank(playerSpawn.x, playerSpawn.y, tankBody, "Player 1");
		player1.AddTurret("Player 1 Turret");
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
		float x;
		float y;
		
		public SpawnPoint(boolean random){
			if(random){
				x = (float) (Math.random() * Gdx.graphics.getWidth());
				y = (float) (Math.random() * Gdx.graphics.getHeight());
			} else {
				x = Gdx.graphics.getWidth() /2;
				y = Gdx.graphics.getHeight() /2;
			}
		}
	}
	
	
	
	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		camera.update();
        batch.setProjectionMatrix(camera.combined);

		batch.begin();
		getKeyPressed();
		batch.draw(groundTxt, 0,0);
		for(Vehicle vehicle : vehicles){
		batch.draw(vehicle.getRegion(), vehicle.getX(), vehicle.getY(), vehicle.centrePoint.getRelativeX() , vehicle.centrePoint.getRelativeY() ,vehicle.getWidth(), vehicle.getHeight(), 1f, 1f, vehicle.getDirection());
		if(DEBUG_ENABLE)
			for(POI eachPOI : vehicle.allPOI){
				tankDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
				tankDotSprite.draw(batch);
			}
		
		if(vehicle.getTurret() != null)
			vehicle.getTurret().faceMouse();
			batch.draw(vehicle.getTurret().getRegion(), vehicle.getTurret().getX() ,vehicle.getTurret().getY(), vehicle.getTurret().centrePoint.getRelativeX() , vehicle.getTurret().centrePoint.getRelativeY() ,vehicle.getTurret().getWidth(), vehicle.getTurret().getHeight(), 1f, 1f, vehicle.getTurret().getDirection());
			for(Projectile bullet : vehicle.getTurret().getBulletObjects()){
				bullet.drive(false);
				batch.draw(bullet.getRegion(), bullet.getX(), bullet.getY(), bullet.centrePoint.getRelativeX() , bullet.centrePoint.getRelativeY() ,bullet.getWidth(), bullet.getHeight(), 1f, 1f, bullet.getDirection());
				for(POI eachPOI : bullet.allPOI){
					gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
					gunDotSprite.draw(batch);
				}
			}
			if(DEBUG_ENABLE)
				for(POI eachPOI : vehicle.getTurret().allPOI){
					gunDotSprite.setPosition(eachPOI.getX(), eachPOI.getY());
					gunDotSprite.draw(batch);
				}
		}
		
		if(DEBUG_ENABLE)
			screenText.draw(batch, debugText(), 10, 1080);
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
				"\nMouse.y"+Gdx.input.getY()+"\n"+bulletsString;
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
    	if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
    		Vehicle player2 = new Vehicle(Gdx.input.getX(), 1080 - Gdx.input.getY(), tankBody, "PLAYER 0");
    		player2.AddTurret("Player 2 Turret");
    		vehicles.add(player2);
    	}
    	
   	
        return true;
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
