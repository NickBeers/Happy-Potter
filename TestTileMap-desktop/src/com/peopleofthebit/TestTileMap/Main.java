package com.peopleofthebit.TestTileMap;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TestTileMap";
		cfg.width = 1300;
		cfg.height = 800;
		
		new LwjglApplication(new HappyPotterMain(), cfg);
	}
}
