package com.peopleofthebit.TestTileMap.Game;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.peopleofthebit.TestTileMap.Objects.Player;
import com.peopleofthebit.TestTileMap.Utilities.CameraHelper;

public class WorldController {
	public CameraHelper cameraHelper;
	public Level level;
	public Player player;
	public Touchpad touchpad;
    
	
	public WorldController() {
		init();
	}
	
	private void init() {
		cameraHelper = new CameraHelper();
		player = new Player(Assets.instance.world.getMap().getLayers().get("spawn"));
		cameraHelper.setTarget(player);
		
		initTouchpad();
	}
	
	private void initTouchpad() {
		TouchpadStyle touchpadStyle;
	    Drawable touchBackground;
	    Drawable touchKnob;
	    
		//Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = Assets.instance.touchpad.getSkin().getDrawable("touchBackground");
        touchKnob = Assets.instance.touchpad.getSkin().getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);
	}
	
	public void update(float deltaTime) {
		player.movePlayer(touchpad);
		cameraHelper.update(deltaTime);
	}
}
