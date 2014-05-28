package com.peopleofthebit.TestTileMap.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.peopleofthebit.TestTileMap.Utilities.Constants;

public class Assets implements Disposable, AssetErrorListener{
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public Player player;
	public TiledWorld world;
	public HPTouchpad touchpad;
	
	//Singleton, make constructor private
	private Assets(){}
	
	public void init(AssetManager assetManager){
		this.assetManager = assetManager;
		
		//set assetManager error handler
		assetManager.setErrorListener(this);
		
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_PLAYER, TextureAtlas.class);
		
		//Load asset and wait until finished
		assetManager.finishLoading();
		
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for(String a : assetManager.getAssetNames()){
			Gdx.app.debug(TAG, "asset: " + a);
		}
		
		TextureAtlas playerAtlas = new TextureAtlas(Constants.TEXTURE_ATLAS_PLAYER);
		
		//Use texture filtering for pixel smoothing
		for(Texture t : playerAtlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		//Create game resources
		player = new Player(playerAtlas);
		world = new TiledWorld(Constants.WORLD_TMX);
		touchpad = new HPTouchpad();
		
	}
	
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}
	
	
	//Internal asset classes
	public class Player {
		private static final int NUM_FRAMES_IN_ANIMATION = 8;
		private static final float WALKING_FRAME_DURATION = 0.125f;
		
		private Animation walkNorthAnimation;
		private Animation walkSouthAnimation;
		private Animation walkWestAnimation;
		private Animation walkEastAnimation;
		
		TextureRegion[] walkNorthFrames = new TextureRegion[NUM_FRAMES_IN_ANIMATION];
		TextureRegion[] walkSouthFrames = new TextureRegion[NUM_FRAMES_IN_ANIMATION];
		TextureRegion[] walkWestFrames = new TextureRegion[NUM_FRAMES_IN_ANIMATION];
		TextureRegion[] walkEastFrames = new TextureRegion[NUM_FRAMES_IN_ANIMATION];
		
		public Player(TextureAtlas atlas){
			for(int i = 0; i < NUM_FRAMES_IN_ANIMATION; i++){
				walkNorthFrames[i] = atlas.findRegion("n" + (i));
				walkSouthFrames[i] = atlas.findRegion("s" + (i));
				walkWestFrames[i] = atlas.findRegion("w" + (i));
				walkEastFrames[i] = atlas.findRegion("e" + (i));
			}
			
			walkNorthAnimation = new Animation(WALKING_FRAME_DURATION, walkNorthFrames);
			walkSouthAnimation = new Animation(WALKING_FRAME_DURATION, walkSouthFrames);
			walkWestAnimation = new Animation(WALKING_FRAME_DURATION, walkWestFrames);
			walkEastAnimation = new Animation(WALKING_FRAME_DURATION, walkEastFrames);
		}
		
		public Animation getNorthAnimation(){
			return walkNorthAnimation;
		}
		public Animation getSouthAnimation(){
			return walkSouthAnimation;
		}
		public Animation getWestAnimation(){
			return walkWestAnimation;
		}
		public Animation getEastAnimation(){
			return walkEastAnimation;
		}
	}
	
	public class TiledWorld{
		private TiledMap map;
		
		public TiledWorld(String tmxFilePath) {
			map =  new TmxMapLoader().load(tmxFilePath);
		}
		
		public TiledMap getMap() {
			return map;
		}

	}
	
	public class HPTouchpad {
		 private Skin touchpadSkin;
		 
		 public HPTouchpad() {
			 init();
		 }
		 
		 public void init() {
			//Create a touchpad skin    
		    touchpadSkin = new Skin();
		    //Set background image
		    touchpadSkin.add("touchBackground", new Texture("HappyPotter/touchBackground.png"));
		    //Set knob image
		    touchpadSkin.add("touchKnob", new Texture("HappyPotter/touchKnob.png"));
		    
		 }
		 
		 public Skin getSkin() {
			 return touchpadSkin;
		 }
	}


}
