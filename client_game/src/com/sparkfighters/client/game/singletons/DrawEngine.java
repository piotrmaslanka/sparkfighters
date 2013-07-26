package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public enum DrawEngine 
{
	INSTANCE;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shape;
	
	public void Init()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameEngine.INSTANCE.orginal_width, GameEngine.INSTANCE.orginal_height);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);	
		
		shape=new ShapeRenderer();
		shape.setProjectionMatrix(camera.combined);
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
	
	public void DrawRectangle(int x1,int y1,int x2,int y2, Color c)
	{
		shape.begin(ShapeType.Line);
		shape.setColor(c);
		shape.rect(x1, y1, x2-x1, y2-y1);
		shape.end();
	}
	
	public void DrawPoint(int x,int y, int size, Color c)
	{
		Gdx.gl10.glPointSize(size);
		shape.begin(ShapeType.Point);
		shape.setColor(c);
		shape.point(x, y, 0);
		shape.end();
	}
	
	public void DrawDebugInfo()
	{	
		int w=GameEngine.INSTANCE.orginal_width;
		int h=GameEngine.INSTANCE.orginal_height;
		
		ResourcesManager.INSTANCE.font.setColor(Color.GREEN);
		batch.begin();
		//here debug info on screen	
		ResourcesManager.INSTANCE.font.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 0,h);
		batch.end();
	}
}
