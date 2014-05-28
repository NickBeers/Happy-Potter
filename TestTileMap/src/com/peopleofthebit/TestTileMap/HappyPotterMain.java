package com.peopleofthebit.TestTileMap;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.peopleofthebit.TestTileMap.Game.Assets;
import com.peopleofthebit.TestTileMap.Game.WorldController;
import com.peopleofthebit.TestTileMap.Game.WorldRenderer;

public class HappyPotterMain implements ApplicationListener{
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	
	@Override
	public void create() {
		//Set Libgdx log level
		//********************************Change to LOG_INFO or LOG_NONE before deployment
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//Load assets
		Assets.instance.init(new AssetManager());
		
		//initialize controller and renderer
		worldController = new WorldController();
		worldRenderer= new WorldRenderer(worldController);
		
		//The world is not paused on start
		paused = false;
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void render() {
		//Do not update the world if paused
		if(!paused){
			//Update the game world with time since last rendered frame
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		
		//Set ClearColor to default color
		Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		//Clear the screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Render the game world to the screen
		worldRenderer.render();
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
		paused = false;
	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
		Assets.instance.dispose();
	}

}
