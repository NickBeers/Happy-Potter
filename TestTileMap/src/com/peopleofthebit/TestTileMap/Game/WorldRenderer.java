package com.peopleofthebit.TestTileMap.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.peopleofthebit.TestTileMap.Utilities.Constants;

public class WorldRenderer implements Disposable{
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private IsometricTiledMapRenderer renderer;
	private WorldController worldController;
	private Stage stage;
	
	public WorldRenderer(WorldController worldController){
		this.worldController = worldController;
		init();
	}
	
	private void init(){
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();
		
		renderer = new IsometricTiledMapRenderer(Assets.instance.world.getMap());
		
		//Create a Stage and add TouchPad
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, renderer.getSpriteBatch());
        stage.addActor(worldController.touchpad);        
        Gdx.input.setInputProcessor(stage);
	}
	
	public void render(){
		renderWorld();
	}
	
	public void resize(int width, int height){
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		camera.update();
		
		cameraGUI.viewportHeight = height;
		cameraGUI.viewportWidth = width;
		cameraGUI.update();
	}

	@Override
	public void dispose() {
	}
	
	public void renderWorld(){
		renderer.setView(camera);
		renderer.getSpriteBatch().setProjectionMatrix(camera.combined);
		
		renderer.render();
		worldController.player.render(renderer.getSpriteBatch());
		stage.act(Gdx.graphics.getDeltaTime());        
        stage.draw();
        
        worldController.cameraHelper.applyTo(camera);
	}
	

}
