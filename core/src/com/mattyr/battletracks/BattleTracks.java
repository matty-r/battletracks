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
import com.mattyr.battletracks.backend.Vehicle;

public class BattleTracks extends ApplicationAdapter {
	SpriteBatch batch;
	Texture groundTxt;
	Texture bulletTxt;
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
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		screenText = new BitmapFont();
		screenText.setColor(Color.RED);
		groundTxt = new Texture(Gdx.files.internal("ground.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true); 
		player1 = new Tank(playerSpawn.x, playerSpawn.y, new Texture(Gdx.files.internal("tankBody.png")));
		player1.AddTurret();
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
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		camera.update();
        batch.setProjectionMatrix(camera.combined);

		batch.begin();
		getKeyPressed();
		batch.draw(groundTxt, 0,0);
		for(Vehicle vehicle : vehicles){
		batch.draw(vehicle.getRegion(), vehicle.getX(), vehicle.getY(), vehicle.getCentrePoint().getRelativeX() , vehicle.getCentrePoint().getRelativeY() ,vehicle.getWidth(), vehicle.getHeight(), 1f, 1f, vehicle.getDirection());
		tankDotSprite.setPosition(vehicle.getHardPoint().getX(), vehicle.getHardPoint().getY());
		tankDotSprite.draw(batch);
		tankDotSprite.setPosition(vehicle.getRealX(), vehicle.getRealY());
		tankDotSprite.draw(batch);
		tankDotSprite.setPosition(vehicle.getX(), vehicle.getY());
		tankDotSprite.draw(batch);
		tankDotSprite.setPosition(vehicle.getCentrePoint().getX(), vehicle.getCentrePoint().getY());
		tankDotSprite.draw(batch);
		
		
		tankDotSprite.draw(batch);
		if(vehicle.getTurret() != null)
			batch.draw(vehicle.getTurret().getRegion(), vehicle.getTurret().getX() ,vehicle.getTurret().getY(), vehicle.getTurret().getCentrePoint().getRelativeX() , vehicle.getTurret().getCentrePoint().getRelativeY() ,vehicle.getTurret().getWidth(), vehicle.getTurret().getHeight(), 1f, 1f, vehicle.getTurret().getDirection());
			
			gunDotSprite.setPosition(vehicle.getTurret().getRealX(), vehicle.getTurret().getRealY());
			gunDotSprite.draw(batch);
			gunDotSprite.setPosition(vehicle.getTurret().getX(), vehicle.getTurret().getY());
			gunDotSprite.draw(batch);
			gunDotSprite.setPosition(vehicle.getTurret().getCentrePoint().getX(), vehicle.getTurret().getCentrePoint().getY());
			gunDotSprite.draw(batch);
		}
		screenText.draw(batch, debugText(), 10, 1080);
		batch.end();
	}
	
	public String debugText(){
		return "Player 1:\nx="+player1.getX()+"\ny="+player1.getY()+"\nDirection="+player1.getDirection()+"\nWidth="+player1.getWidth()+"\nHeight="+player1.getHeight()+"\nHardpoint.X="+player1.getHardPoint().getX() +"\nHardpoint.Y="+player1.getHardPoint().getY()+"\nCentrePoint.x="+player1.getCentrePoint().getX()+"\nCentrePoint.y="+player1.getCentrePoint().getY()+
				"\n\nFPS="+Gdx.graphics.getFramesPerSecond() +
				"\n\nPlayer 1 Gun:\nx="+player1.getTurret().getX()+"\ny="+player1.getTurret().getY()+"\nDirection="+player1.getTurret().getDirection()+"\nWidth="+player1.getTurret().getWidth()+"\nHeight="+player1.getTurret().getHeight()+"\nCentre Point x="+player1.getTurret().getCentrePoint().getX()+"\nCentre Point y="+player1.getTurret().getCentrePoint().getY()
				;
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
    	//if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
    		
    	}
    	

    	if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			player1.getTurret().turn(true);
    	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			player1.getTurret().turn(false);
    	
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
