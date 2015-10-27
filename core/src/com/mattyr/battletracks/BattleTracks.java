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

public class BattleTracks extends ApplicationAdapter {
	SpriteBatch batch;
	Texture groundTxt;
	Texture bulletTxt;
	Vehicle player1; 
	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	OrthographicCamera camera;
	BitmapFont screenText;
	Pixmap hardpointDot;
	Texture dotTexture;
	Sprite sprite; 
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		screenText = new BitmapFont();
		screenText.setColor(Color.RED);
		groundTxt = new Texture(Gdx.files.internal("ground.png"));
		SpawnPoint playerSpawn = new SpawnPoint(true); 
		player1 = new Vehicle(playerSpawn.x, playerSpawn.y, new Texture(Gdx.files.internal("tankBody.png")), false);
		player1.AddTurret();
		vehicles.add(player1);
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 1920, 1080);
	    hardpointDot = new Pixmap(2,2,Pixmap.Format.RGBA8888);
	    hardpointDot.setColor(Color.RED);
	    hardpointDot.fill();
	    hardpointDot.drawCircle(1, 1, 1);
	    dotTexture = new Texture(hardpointDot);
	    hardpointDot.dispose();
	    
	    sprite = new Sprite(dotTexture);
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
		batch.draw(vehicle.region, vehicle.x, vehicle.y, vehicle.centrePoint.relativeX , vehicle.centrePoint.relativeY ,vehicle.width, vehicle.height, 1f, 1f, vehicle.direction);
		sprite.setPosition(vehicle.hardPoint.x, vehicle.hardPoint.y);
		sprite.draw(batch);
		sprite.setPosition(vehicle.realX, vehicle.realY);
		sprite.draw(batch);
		sprite.setPosition(vehicle.x, vehicle.y);
		sprite.draw(batch);
		sprite.setPosition(vehicle.centrePoint.x, vehicle.centrePoint.y);
		sprite.draw(batch);
		//if(vehicle.gun != null)
			//batch.draw(vehicle.gun.region, vehicle.gun.x, vehicle.gun.y, vehicle.gun.centrePoint.relativeX , vehicle.gun.centrePoint.relativeY ,vehicle.gun.width, vehicle.gun.height, 1f, 1f, vehicle.gun.direction);
		}
		screenText.draw(batch, debugText(), 10, 1080);
		batch.end();
	}
	
	public String debugText(){
		return "Player 1:\nx="+player1.x+"\ny="+player1.y+"\nDirection="+player1.direction+"\nWidth="+player1.width+"\nHeight="+player1.height+"\nHardpoint.X="+player1.hardPoint.x +"\nHardpoint.Y="+player1.hardPoint.y+"\nCentrePoint.x="+player1.centrePoint.x+"\nCentrePoint.y="+player1.centrePoint.y+
				"\n\nFPS="+Gdx.graphics.getFramesPerSecond() +
				"\n\nPlayer 1 Gun:\nx="+player1.gun.x+"\ny="+player1.gun.y+"\nDirection="+player1.gun.direction+"\nWidth="+player1.gun.width+"\nHeight="+player1.gun.height+"\nCentre Point x="+player1.gun.centrePoint.x+"\nCentre Point y="+player1.gun.centrePoint.y
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
			player1.gun.turn(true);
    	if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			player1.gun.turn(false);
    	
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
