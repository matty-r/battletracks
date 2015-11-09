package com.mattyr.battletracks.backend;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Entity extends Actor {
	private float forwardSpeed;
	private float reverseSpeed;
	private float turnSpeed;
	public ArrayList<POI> allPOI = new ArrayList<POI>();
	public POI centrePoint;
	public POI bLeft;
	public POI tLeft;
	public POI bRight;
	public POI tRight;
	private String name;
	Texture texture;
	private TextureRegion region;
	private int maxHealthValue;
	private int currentHealthValue;
	//Used in collision detection to only start checking for collisions within the diameter (Intersection.dst())
	private int diameter;
	
	Entity(float startX, float startY, Texture loadTexture, String name, int healthValue){
		setName(name);
		setMaxHealthValue(healthValue);
		setCurrentHealthValue(healthValue);
		if(loadTexture != null){
			texture = loadTexture;
			setRegion(new TextureRegion(texture));
			setWidth(texture.getWidth());
			setHeight(texture.getHeight());
		}
		
		diameter =  (getWidth() > getHeight()) ? (int) getWidth() : (int) getHeight();
		setX(startX);
		setY(startY);
		setRotation(0);
		centrePoint = new POI(this, "Centre Point");
		centrePoint.setXY();
		bLeft = new POI(this, 0f, 0f, "Bottom Left");
		tLeft = new POI(this, 0f, this.getHeight(), "Top Left");
		bRight = new POI(this, this.getWidth(), 0f, "Bottom Right");
		tRight = new POI(this, this.getWidth(), this.getHeight(), "Top Right");
	}
	

	@Override
    public void draw (Batch batch, float parentAlpha) {
        try{
        	batch.draw(getRegion(), getX(), getY(), centrePoint.getRelativeX(), 
        		centrePoint.getRelativeY(), getWidth(), getHeight(), 1f, 1f, getRotation());
        } catch(Exception e) {
        	//
        }
    }
	
	public void setTurnSpeed(float speed){
		turnSpeed = speed;
	}
	
	public void turn(boolean swap){
		setRotation(getRotation() + ((swap) ? getTurnSpeed() : -getTurnSpeed()));
		
		
		if(getRotation() < 0){
			setRotation(360 - (-getRotation()));
		} else if(getRotation() >= 360){
			setRotation(360 - getRotation());
		}
		
		setPOIs();
	}
	
	
	public void strafe(boolean swap){
		float velocityX;
		float velocityY;
		float directionX;
		float directionY;

		
		directionX = (float) Math.cos(Math.toRadians(this.getRotation() + 90));			
		directionY = (float) Math.sin(Math.toRadians(this.getRotation() + 90));
		
		if(!swap){
		velocityX = (directionX) * getTurnSpeed();
		velocityY = (directionY) * getTurnSpeed();
		setX(getX() + velocityX);		
		setY(getY() + velocityY);	
		} else {
			velocityX = (directionX) * getTurnSpeed();
			velocityY = (directionY) * getTurnSpeed();
			setX(getX() - velocityX);			
			setY(getY() - velocityY);
		}
		setPOIs();
	}
	
	public void destroy(){
		for(Integer x = allPOI.size() -1; x > -1; x--){
			allPOI.remove(x);
		}
	}
	
	public void faceMouse(){
		float targetAngle = (float) Math.toDegrees(Math.atan2((Gdx.graphics.getHeight() - Gdx.input.getY()) - centrePoint.getY(), Gdx.input.getX() - centrePoint.getX()));
		if(targetAngle < 0)
			targetAngle = 360 - (-targetAngle);
		
		setRotation(targetAngle);
		setPOIs();
	}
	
	public void faceEntity(Entity target){
		if(target != null){
			float targetAngle = (float) Math.toDegrees(Math.atan2(target.centrePoint.getY() - centrePoint.getY(), target.centrePoint.getX() - centrePoint.getX()));
			if(targetAngle < 0)
				targetAngle = 360 - (-targetAngle);
			
			setRotation(targetAngle);
			setPOIs();
		}
	}
    
	/**
	 * This is a big disgusting mess.. but works. 
	 * @param target
	 */
  public boolean turnToFaceEntity(Entity target){
      if(target != null){
    	  //Get the angle from our centrePoint to our targets centrePoint
         float targetAngle = (float) Math.toDegrees(Math.atan2(target.centrePoint.getY() - centrePoint.getY(), target.centrePoint.getX() - centrePoint.getX()));
         float fixedAngle = 0;
         float currentAngle = getRotation();
         
         //Make sure nothing is 0 degrees, but is 360 instead
         if(currentAngle == 0)
        	 currentAngle = 360;
         
         //Make sure nothing is 0 degrees, but is 360 instead
         if(fixedAngle == 0)
        	 fixedAngle = 360;
         
         if(targetAngle < 0) {
        	 fixedAngle = 360 - (-targetAngle);
         } else {
        	 fixedAngle = targetAngle;
         }
        
         float relativeAngle = fixedAngle - currentAngle;
         
         //If the absolute relativeAngle is less than the turning speed, then we don't need to turn any more.
         if(Math.abs(relativeAngle) > getTurnSpeed()){
        	 //If the absolute relativeAngle is greater than 180, then it's quicker to rotating the opposite way
        	 if(Math.abs(relativeAngle) < 180){
        		 //if the relativeAngle is greater than 0, then we need to Increase the angle
            	if(relativeAngle < 0){
            		//But before we do, lets test to make sure it's actually going to get us closer
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(false);
            		} else {
            			turn(true);
            		}
            	} else if(relativeAngle > 0){
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(true);
            		} else {
            			turn(false);
            		}
            	}
        	 } else {
        		 if(relativeAngle < 0){
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(true);
            		} else {
            			turn(false);
            		}
            	} else if(relativeAngle > 0){
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(false);
            		} else {
            			turn(true);
            		}
            	} 
        	 }
         } else {
        	 setRotation(fixedAngle);
         }
         
         if(fixedAngle == currentAngle)
        	 return true;
         else 
        	 return false;
      }
      return false;
  }
	
  
  public boolean turnToFacePoint(Vector2 target){
      if(target != null){
    	  //Get the angle from our centrePoint to our targets centrePoint
         float targetAngle = (float) Math.toDegrees(Math.atan2(target.y - centrePoint.getY(), target.x - centrePoint.getX()));
         float fixedAngle = 0;
         float currentAngle = getRotation();
         
         //Make sure nothing is 0 degrees, but is 360 instead
         if(currentAngle == 0)
        	 currentAngle = 360;
         
         //Make sure nothing is 0 degrees, but is 360 instead
         if(fixedAngle == 0)
        	 fixedAngle = 360;
         
         if(targetAngle < 0) {
        	 fixedAngle = 360 - (-targetAngle);
         } else {
        	 fixedAngle = targetAngle;
         }
        
         
         float relativeAngle = fixedAngle - currentAngle;
         
         //If the absolute relativeAngle is less than the turning speed, then we don't need to turn any more.
         if(Math.abs(relativeAngle) > getTurnSpeed()){
        	 //If the absolute relativeAngle is greater than 180, then it's quicker to rotating the opposite way
        	 if(Math.abs(relativeAngle) < 180){
        		 //if the relativeAngle is greater than 0, then we need to Increase the angle
            	if(relativeAngle < 0){
            		//But before we do, lets test to make sure it's actually going to get us closer
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(false);
            		} else {
            			turn(true);
            		}
            	} else if(relativeAngle > 0){
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(true);
            		} else {
            			turn(false);
            		}
            	}
        	 } else {
        		 if(relativeAngle < 0){
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(true);
            		} else {
            			turn(false);
            		}
            	} else if(relativeAngle > 0){
            		if(fixedAngle - (currentAngle + 1) < relativeAngle){
            			turn(false);
            		} else {
            			turn(true);
            		}
            	} 
        	 }
         }else {
        	 setRotation(fixedAngle);
         }
         
         if(fixedAngle == currentAngle)
        	 return true;
         else 
        	 return false;
      }
      return false;
  }
   /**
    * Check if this.Entity is colliding with the targetEntity. Check only begins
    * if this.Entity is within the targetEntity.diameter.
    * @param targetPOI
    * @return
    */
   public boolean isCollidedWith(Entity targetEntity){
	   int distance = (int) centrePoint.dst(targetEntity.centrePoint);
	   if(distance < targetEntity.diameter)
    	for(POI sp1 : allPOI)
    		for(POI sp2 : allPOI)
    			if(sp2 != sp1)
    				for(POI dp1 : targetEntity.allPOI)
    					for(POI dp2 : targetEntity.allPOI)
    						if(dp2 != dp1)
    							if(Intersector.intersectSegments(sp1, sp2, dp1, dp2, null))
    								return true;
    	return false;
    }
   
   	/**
   	 * Same as 
   	 * @param target
   	 * @return
   	 */
   	public boolean isOverlapping(Entity target){
		for(POI targetPOI : target.allPOI){
			return isOverlapping(targetPOI);			
		}
   	return false;
   	}
   
    /**
     * Splits the Entity into four Triangles then detects
     * @param targetVector
     * @return boolean
     */
    public boolean isOverlapping(Vector2 targetVector){
    	if(centrePoint.dst(targetVector) < diameter){
	    	if(Intersector.isPointInTriangle(targetVector, bRight, centrePoint, bLeft))
				return true;
			else if(Intersector.isPointInTriangle(targetVector, bRight, centrePoint, tRight))
				return true;
			else if(Intersector.isPointInTriangle(targetVector, tRight, centrePoint, tLeft))
				return true;
			else if(Intersector.isPointInTriangle(targetVector, tLeft, centrePoint, bLeft))
				return true;
    	}
    	return false;
    }

	public void drive(boolean swap){
		float velocityX;
		float velocityY;
		float directionX;
		float directionY;

		directionX = (float) Math.cos(Math.toRadians(this.getRotation()));			
		directionY = (float) Math.sin(Math.toRadians(this.getRotation()));
		
		if(!swap){
		velocityX = directionX * getForwardSpeed();
		velocityY = directionY * getForwardSpeed();
		setX(getX() + velocityX);		
		setY(getY() + velocityY);	
		} else {
			velocityX = directionX * getReverseSpeed();
			velocityY = directionY * getReverseSpeed();
			setX(getX() - velocityX);			
			setY(getY() - velocityY);
		}
		setPOIs();
	}
	
	public void setPOIs(){
		centrePoint.setXY();
		bLeft.setXY2();
		tLeft.setXY2();
		bRight.setXY2();
		tRight.setXY2();
	}
	
	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}
	
	public void setForwardSpeed(float speed){
		forwardSpeed = speed;
	}
	
	public void setReverseSpeed(float speed){
		reverseSpeed = speed;
	}
	
	public float getTurnSpeed(){
		return turnSpeed;
	}
	
	public float getForwardSpeed(){
		return forwardSpeed;
	}
	
	public float getReverseSpeed(){
		return reverseSpeed;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		String finalString = getName()+":\nx="+getX()+
				"\ny="+getY()+
				"\nDiameter="+diameter+
				"\nDirection="+getRotation()+
				"\nWidth="+getWidth()+
				"\nHeight="+getHeight()+
				"\nTurn Speed="+getTurnSpeed()+
				"\nForward Speed="+getForwardSpeed()+
				"\nReverse Speed="+getReverseSpeed();
		
		for(POI poiString : allPOI)
			finalString += poiString.toString();
		
		return "\n"+finalString;
	}

	public int getCurrentHealthValue() {
		return currentHealthValue;
	}

	public void setCurrentHealthValue(int currentHealthValue) {
		if(currentHealthValue > getMaxHealthValue())
			currentHealthValue = getMaxHealthValue();
		
		this.currentHealthValue = currentHealthValue;
	}

	public int getMaxHealthValue() {
		return maxHealthValue;
	}

	public void setMaxHealthValue(int maxHealthValue) {
		this.maxHealthValue = maxHealthValue;
	}
}
