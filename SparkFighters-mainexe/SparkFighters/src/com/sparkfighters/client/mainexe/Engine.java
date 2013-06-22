package com.sparkfighters.client.mainexe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Engine {
	INSTANCE;
	
	public final int orginal_width=1920;
	public final int orginal_height=1080;
	public int window_width;
	public int window_height;
	public float stateTime;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	
	public void Create(int window_width,int window_height)
	{
		this.window_width=window_width;
		this.window_height=window_height;
	}
	
	public void InitEngine()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, orginal_width, orginal_height);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		stateTime = 0f;
	}
	
	public void ProcessData()
	{
		/*if(Gdx.input.isKeyPressed(Keys.Q))
		{
			temp=0;
		}
		
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			temp=1;
		}
		
		if(Gdx.input.isKeyPressed(Keys.E))
		{
			temp=2;
		}*/
	}
	
	public void Draw()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		stateTime += Gdx.graphics.getDeltaTime(); 
		int x=20,y=20;
		for(Animation a:ResourcesManager.INSTANCE.HeroesData.get(0).Animations)
		{
			TextureRegion currentFrame=a.getKeyFrame(stateTime, true);
			batch.draw(currentFrame, x,y);
			x+=200;
		}
		batch.end();

	}
}
