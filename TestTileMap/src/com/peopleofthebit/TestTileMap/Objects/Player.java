package com.peopleofthebit.TestTileMap.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.peopleofthebit.TestTileMap.Game.Assets;
import com.peopleofthebit.TestTileMap.Utilities.UtilityMethods;

public class Player extends AbstractGameObject {
	
	private static final int SPRITE_SPEED = 300;
	
	private enum SpriteDirection {
		n, s, w, e
	}
	private enum State {
		Idle, Walking
	}
	
	Sprite sprite;
	private Vector2 spriteVector = new Vector2();
	private SpriteDirection spriteDirection = SpriteDirection.n;
	private State spriteState = State.Idle;
	private TextureRegion currentFrame;
	private float stateTime = 0;
	TiledMapTileLayer collisionLayer;
	
	public Player(MapLayer mapLayer) {
		init(mapLayer);
	}
	
	private void init(MapLayer objectLayer) {
		try{
			sprite = new Sprite(Assets.instance.player.getNorthAnimation().getKeyFrame(0, false));
			sprite.scale(1.25f);
			sprite.setOrigin(0.0f, 0.0f);
			
			collisionLayer =  (TiledMapTileLayer) Assets.instance.world.getMap().getLayers().get(0);
		
			for(MapObject obj : objectLayer.getObjects()){
				if(obj instanceof EllipseMapObject){
					Ellipse ellipse = ((EllipseMapObject) obj).getEllipse();
					position = UtilityMethods.mapToScreenCoordinate(new Vector2(ellipse.x, ellipse.y));
				}
			}	
			sprite.setPosition(position.x, position.y);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void render(Batch batch) {
		Vector2 newPosition = UtilityMethods.moveCoordinate(new Vector2(sprite.getX(), sprite.getY()), spriteVector.x * Gdx.graphics.getDeltaTime(), spriteVector.y * Gdx.graphics.getDeltaTime());
		
		if(isCellBlocked(newPosition.x, newPosition.y))
			return;
		
		sprite.setPosition(newPosition.x, newPosition.y);
		position.x = newPosition.x;
		position.y = newPosition.y;
		
		drawPlayer(batch);
	}
	
	public void movePlayer(Touchpad touchpad) {
		float touchX = touchpad.getKnobPercentX();
		float touchY = touchpad.getKnobPercentY();
		
		if(touchX < -0.1 && touchX < -Math.abs(touchY)){
			spriteVector.y = 0;
			spriteVector.x = -SPRITE_SPEED;
			spriteDirection = SpriteDirection.e;
		}
		else if(touchX > 0.1 && touchX > Math.abs(touchY)){
			spriteVector.y = 0;
			spriteVector.x = SPRITE_SPEED;
			spriteDirection = SpriteDirection.w;
		}
		else if(touchY < -0.1 && touchY <= -Math.abs(touchX)){
			spriteVector.x = 0;
			spriteVector.y = -SPRITE_SPEED;
			spriteDirection = SpriteDirection.s;
		}
		else if(touchY > 0.1 && touchY >= Math.abs(touchX)){
			spriteVector.x = 0;
			spriteVector.y = SPRITE_SPEED;
			spriteDirection = SpriteDirection.n;
		}
		else if(touchX <= 0.1 && touchX >= -0.1 && touchY <= 0.1 && touchY >= -0.1){
			spriteVector.x = 0;
			spriteVector.y = 0;
		}
		
		if(spriteVector.x == 0 && spriteVector.y == 0)
			spriteState = State.Idle;
		else
			spriteState = State.Walking;
	}
	
	private boolean isCellBlocked(float x, float y) {
		Vector2 newCoords = UtilityMethods.screenToMapCoordinate(new Vector2(x , y ));
		//System.out.println("Player: x" + sprite.getX() + " y " + sprite.getY());
		//System.out.println("Tile: x" + newCoords.x + " y " + newCoords.y);
		//sprite.setPosition(newCoords.x, newCoords.y);
		Cell cell = collisionLayer.getCell((int) (newCoords.x / collisionLayer.getTileWidth()), (int) (newCoords.y / collisionLayer.getTileHeight()));
		//System.out.println(cell);
		return  cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}
	
	public void drawPlayer(Batch batch) {
		if(spriteState.equals(State.Walking)){
			stateTime += Gdx.graphics.getDeltaTime();
			switch(spriteDirection){
			case n: currentFrame = Assets.instance.player.getNorthAnimation().getKeyFrame(stateTime, true);
				break;
			case s: currentFrame = Assets.instance.player.getSouthAnimation().getKeyFrame(stateTime, true);
				break;
			case w: currentFrame = Assets.instance.player.getWestAnimation().getKeyFrame(stateTime, true);
				break;
			case e: currentFrame = Assets.instance.player.getEastAnimation().getKeyFrame(stateTime, true);
				break;
			default:
				break;
			}
		}
		else {
			switch(spriteDirection){
			case n: currentFrame = Assets.instance.player.getNorthAnimation().getKeyFrame(0);
				break;
			case s: currentFrame = Assets.instance.player.getSouthAnimation().getKeyFrame(0);
				break;
			case w: currentFrame = Assets.instance.player.getWestAnimation().getKeyFrame(0);
				break;
			case e: currentFrame = Assets.instance.player.getEastAnimation().getKeyFrame(0);
				break;
			default:
				break;
			
			}
		}
		sprite.setRegion(currentFrame);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
}
