package com.sparkfighters;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.mainexe.Engine;
import com.sparkfighters.client.mainexe.ResourcesManager;

public class SparkFightersGame implements ApplicationListener 
{
	@Override
	public void create() 
	{		
		Engine.INSTANCE.InitEngine();
		ResourcesManager.INSTANCE.LoadResources();
	}

	@Override
	public void dispose() 
	{
		
	}

	@Override
	public void render() 
	{		
		Engine.INSTANCE.ProcessData();
		Engine.INSTANCE.Draw();
	}

	@Override
	public void resize(int width, int height) 
	{
	}

	@Override
	public void pause() 
	{
		
	}

	@Override
	public void resume() 
	{
		
	}
}
