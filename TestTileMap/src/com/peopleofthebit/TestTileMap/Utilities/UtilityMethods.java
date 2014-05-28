package com.peopleofthebit.TestTileMap.Utilities;

import com.badlogic.gdx.math.Vector2;

public final class UtilityMethods {
	
	//Make class non-initializable
	private UtilityMethods() {}
	
	public static Vector2 mapToScreenCoordinate(Vector2 vector){
		return vector.rotate(-45).scl(1.0f, 0.5f).scl(1.415f);
	}
	
	public static Vector2 screenToMapCoordinate(Vector2 vector) {
		return vector.scl(.707f, .707f).rotate(45).scl(1.0f, 2.0f).sub(0, 99.5f*0.5f).add(99.5f*16.0f,0);
	}
	
	public static Vector2 moveCoordinate(Vector2 vector, float x, float y){
		vector = vector.scl(1.0f, 2.0f).rotate(45);
	//	vector = vector.rotate(45);
		vector.x += x;
		vector.y += y;
		vector = vector.rotate(-45).scl(1.0f, 0.5f);
		//vector = vector.scl(1.0f, 0.5f);
		return vector;
	}

}
