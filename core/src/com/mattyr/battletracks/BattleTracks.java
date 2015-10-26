package com.mattyr.battletracks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mattyr.battletracks.BattleTracks.Tank.*;

public class BattleTracks extends ApplicationAdapter {
	SpriteBatch batch;
	Texture groundTxt;
	Texture bulletTxt;
	Rectangle tankShape;
	Tank player1; 
	OrthographicCamera camera;
	
	@Override
	public void create () {
		
		batch = new SpriteBatch();
		groundTxt = new Texture(Gdx.files.internal("ground.png"));
		player1 = new Tank(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		//bulletTxt = new Texture(Gdx.files.internal("bullet.png"));
		//tankTxt = 
		
		camera = new OrthographicCamera();
	    camera.setToOrtho(true, 1920, 1080);
	    
	    
	}

	public static class Tank extends Rectangle{
		/** Creates the tank
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Texture texture;
		TextureRegion region;
		float forwardSpeed = 2.0f;
		float reverseSpeed = 0.5f;
		float direction = 0f;
		CentrePoint centre;

		public Tank(float startX, float startY){
			texture = new Texture(Gdx.files.internal("tank.png"));
			region = new TextureRegion(texture);
			this.width = texture.getWidth();
			this.height = texture.getHeight();
			this.x = startX - (this.width / 2);
			this.y = startY - (this.height / 2);
		}
		
		@Override
		public Rectangle setX(float x){
			if(!(x <= 0)){
				if(!(x > Gdx.graphics.getWidth() - this.getWidth()))
					this.x = x;
				else
					this.x = Gdx.graphics.getWidth() - this.width;
			} else {
				this.x = 0;
			}
			return this;
		}
		
		@Override
		public Rectangle setY(float y){
			if(!(y <= 0)){
				if(!(y > Gdx.graphics.getHeight() - this.getHeight()))
					this.y = y;
				else
					this.y = Gdx.graphics.getHeight() - this.getHeight();
			} else {
				this.y = 0;
			}
			return this;
		}
		
		public void turn(boolean swap){
			direction += (swap) ? 1 : -1; 
		}
		
		
		public void driveForward(){
			float velocityX;
			float velocityY;
			float directionX;
			float directionY;
			
			directionX = (float) Math.cos(Math.toRadians(this.direction));			
			directionY = (float) Math.sin(Math.toRadians(this.direction));
			
			velocityX = directionX * forwardSpeed;
			velocityY = directionY * forwardSpeed;
			
			this.x += velocityX;
			this.y += velocityY;
		}
		
		public void driveBackward(){
			float velocityX;
			float velocityY;
			float directionX;
			float directionY;
			
			directionX = (float) Math.cos(Math.toRadians(this.direction));			
			directionY = (float) Math.sin(Math.toRadians(this.direction));
			
			velocityX = directionX * reverseSpeed;
			velocityY = directionY * reverseSpeed;
			
			this.x -= velocityX;
			this.y -= velocityY;
		}
		
		private class CentrePoint{
			float centreX;
			float centreY;
			
			private CentrePoint(){
				centreX = Tank.this.width / 2;
				centreY = Tank.this.height / 2;
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
		batch.draw(player1.region, player1.x, player1.y, player1.width /2 , player1.height /2 ,player1.width, player1.height, 1f, 1f, player1.direction);
		batch.end();
	}
	
    public boolean getKeyPressed() {
    	if(Gdx.input.isKeyPressed(Input.Keys.S)){
        	player1.driveBackward();
    		if(Gdx.input.isKeyPressed(Input.Keys.A))
    			player1.turn(false);
    		else
    			if(Gdx.input.isKeyPressed(Input.Keys.D))
        			player1.turn(true);
    	} else {
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			player1.driveForward();
    	
    	if(Gdx.input.isKeyPressed(Input.Keys.A))
			player1.turn(true);
    	if(Gdx.input.isKeyPressed(Input.Keys.D))
			player1.turn(false);
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
