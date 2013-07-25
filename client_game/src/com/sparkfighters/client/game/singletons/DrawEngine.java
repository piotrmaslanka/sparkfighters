package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum DrawEngine 
{
	INSTANCE;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public void Init()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameEngine.INSTANCE.orginal_width, GameEngine.INSTANCE.orginal_height);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);	
	}
	
	public void ClearScreen()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	public void Draw(TextureRegion t, int x, int y)
	{	
		batch.begin();
		batch.draw(t, x, y);		
		batch.end();
	}
	
	public void Draw(Texture t, int x, int y)
	{	
		batch.begin();
		batch.draw(t, x, y);		
		batch.end();
	}
}
