package com.peopleofthebit.TestTileMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TestTileMap extends InputAdapter implements ApplicationListener, InputProcessor, GestureListener{
	private static final float MAX_ZOOM_IN = 0.5f;
	private static final float MAX_ZOOM_OUT = 1.0f;
	private static final int SPRITE_SPEED = 200;
	private static final float WALKING_FRAME_DURATION = 0.125f;

	
	private Vector2 cameraPosition;
	private float cameraZoom;
	private TiledMap map;
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Sprite sprite;
	private Vector2 spritePosition = new Vector2();
	private Vector2 spriteVector = new Vector2();
	
	//Animation
	float stateTime = 0;
	public enum SpriteDirection {
		n, s, w, e
	}
	public enum State {
		Idle, Walking
	}
	private SpriteDirection spriteDirection = SpriteDirection.n;
	private State spriteState = State.Idle;
	private TextureRegion currentFrame;
	private TextureRegion idleNorth;
	private TextureRegion idleSouth;
	private TextureRegion idleWest;
	private TextureRegion idleEast;
	
	private Animation walkNorthAnimation;
	private Animation walkSouthAnimation;
	private Animation walkWestAnimation;
	private Animation walkEastAnimation;
	
	private void LoadTextures(){
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("TestCastle/Wizzard.pack"));
		idleNorth = atlas.findRegion("n0");
		idleSouth = atlas.findRegion("s0");
		idleWest = atlas.findRegion("w0");
		idleEast = atlas.findRegion("e0");
		
		TextureRegion[] walkNorthFrames = new TextureRegion[7];
		TextureRegion[] walkSouthFrames = new TextureRegion[7];
		TextureRegion[] walkWestFrames = new TextureRegion[7];
		TextureRegion[] walkEastFrames = new TextureRegion[7];
		
		for(int i = 0; i < 7; i++){
			walkNorthFrames[i] = atlas.findRegion("n" + (i+1));
			walkSouthFrames[i] = atlas.findRegion("s" + (i+1));
			walkWestFrames[i] = atlas.findRegion("w" + (i+1));
			walkEastFrames[i] = atlas.findRegion("e" + (i+1));
		}
		
		walkNorthAnimation = new Animation(WALKING_FRAME_DURATION, walkNorthFrames);
		walkSouthAnimation = new Animation(WALKING_FRAME_DURATION, walkSouthFrames);
		walkWestAnimation = new Animation(WALKING_FRAME_DURATION, walkWestFrames);
		walkEastAnimation = new Animation(WALKING_FRAME_DURATION, walkEastFrames);
	}

	
	@Override
	public void create() {
		try{
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(this);
		
		LoadTextures();

		Gdx.input.setInputProcessor(im);
        
		map = new TmxMapLoader().load("TestCastle/HappyPotter.tmx");
		
		renderer = new IsometricTiledMapRenderer(map);
		
		cameraZoom = 1.0f;
		camera = new OrthographicCamera();
		sprite = new Sprite(idleNorth);
		sprite.scale(1.25f);
		sprite.setOrigin(0.0f, 0.0f);
		
		for(MapObject obj : map.getLayers().get("spawn").getObjects()){
			if(obj instanceof EllipseMapObject){
				Ellipse ellipse = ((EllipseMapObject) obj).getEllipse();
				spritePosition = screenToMapCoordinate(new Vector2(ellipse.x, ellipse.y));
			}
		}	
		sprite.setPosition(spritePosition.x, spritePosition.y);
		cameraPosition = new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private void handleDebugInput(){
		//camera move
		float cameraZoomSpeed = 0.02f;

		if(Gdx.input.isKeyPressed(Keys.PERIOD)){
			cameraZoom -= cameraZoomSpeed;
			if(cameraZoom < MAX_ZOOM_IN)
				cameraZoom = MAX_ZOOM_IN;
		}
		else if(Gdx.input.isKeyPressed(Keys.COMMA)){
			cameraZoom += cameraZoomSpeed;
			if(cameraZoom > MAX_ZOOM_OUT)
				cameraZoom = MAX_ZOOM_OUT;
		}
		else if(Gdx.input.isKeyPressed(Keys.A)){
			spriteVector.y = 0;
			spriteVector.x = -SPRITE_SPEED;
			spriteDirection = SpriteDirection.e;
		}
		else if(Gdx.input.isKeyPressed(Keys.D)){
			spriteVector.y = 0;
			spriteVector.x = SPRITE_SPEED;
			spriteDirection = SpriteDirection.w;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)){
			spriteVector.x = 0;
			spriteVector.y = -SPRITE_SPEED;
			spriteDirection = SpriteDirection.s;
		}
		else if(Gdx.input.isKeyPressed(Keys.W)){
			spriteVector.x = 0;
			spriteVector.y = SPRITE_SPEED;
			spriteDirection = SpriteDirection.n;
		}
		else if(Gdx.input.isKeyPressed(Keys.Q)){
			spriteVector.x = 0;
			spriteVector.y = 0;
		}
		
		if(spriteVector.x == 0 && spriteVector.y == 0)
			spriteState = State.Idle;
		else
			spriteState = State.Walking;
		
		if(spriteVector.x > SPRITE_SPEED)
			spriteVector.x = SPRITE_SPEED;
		if(spriteVector.x < -SPRITE_SPEED)
			spriteVector.x = -SPRITE_SPEED;
		if(spriteVector.y > SPRITE_SPEED)
			spriteVector.y = SPRITE_SPEED;
		if(spriteVector.y < -SPRITE_SPEED)
			spriteVector.y = -SPRITE_SPEED;
	}
	
	public void updateCamera(Sprite sprite){
		handleDebugInput();
		camera.position.set(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, 0);
		camera.zoom = cameraZoom;
		
		camera.update();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.setView(camera);
		
		Vector2 newPosition = moveCoordinate(new Vector2(sprite.getX(), sprite.getY()), spriteVector.x * Gdx.graphics.getDeltaTime(), spriteVector.y * Gdx.graphics.getDeltaTime());
		sprite.setPosition(newPosition.x, newPosition.y);
		updateCamera(sprite);
		 
		int[] background = {0, 1, 3, 4, 5, 7};
		int[] foreground = {2, 6, 8};
		
		renderer.render(background);
		DrawSprite();
		renderer.render(foreground);
	}
	
	private void DrawSprite(){
		
		
		if(spriteState.equals(State.Walking)){
			stateTime += Gdx.graphics.getDeltaTime();
			switch(spriteDirection){
			case n: currentFrame = walkNorthAnimation.getKeyFrame(stateTime, true);
				break;
			case s: currentFrame = walkSouthAnimation.getKeyFrame(stateTime, true);
				break;
			case w: currentFrame = walkWestAnimation.getKeyFrame(stateTime, true);
				break;
			case e: currentFrame = walkEastAnimation.getKeyFrame(stateTime, true);
				break;
			default:
				break;
			}
		}
		else {
			switch(spriteDirection){
			case n: currentFrame = idleNorth;
				break;
			case s: currentFrame = idleSouth;
				break;
			case w: currentFrame = idleWest;
				break;
			case e: currentFrame = idleEast;
				break;
			default:
				break;
			
			}
		}
		
		sprite.setRegion(currentFrame);
		renderer.getSpriteBatch().begin();
		sprite.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
	}
	
	private Vector2 screenToMapCoordinate(Vector2 vector){
		return vector.rotate(-45).scl(1.0f, 0.5f).scl(1.415f);
	}
	
	private Vector2 moveCoordinate(Vector2 vector, float x, float y){
		vector = vector.scl(1.0f, 2.0f);
		vector = vector.rotate(45);
		vector.x += x;
		vector.y += y;
		vector = vector.rotate(-45);
		vector = vector.scl(1.0f, 0.5f);
		return vector;
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		
		//Vector3 coord = new Vector3(x, y, 0);
		//camera.unproject(coord);
		
		System.out.println("x:" + sprite.getX() + " y:" + sprite.getY());
		System.out.println(x + " " + y + " ");
		System.out.println(screenToMapCoordinate(new Vector2(x, y)));
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		cameraPosition.x -= deltaX;
		cameraPosition.y += deltaY;
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return true;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		cameraZoom += initialDistance < distance ? (distance/30000) : -(distance/30000);
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
