package com.peopleofthebit.TestTileMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class HappyPotterActivity extends Activity {

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_happy_potter);
	        
	    }
	    
	    public void sendMessage(View view) {
	    	Intent i = new Intent(HappyPotterActivity.this, MainActivity.class);
	        startActivity(i);
	    }
	    
	    public void exitApp(View view) {
	    	Intent intent = new Intent(Intent.ACTION_MAIN);
	    	intent.addCategory(Intent.CATEGORY_HOME);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(intent);
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.happy_potter, menu);
	        return true;
	    }

}
