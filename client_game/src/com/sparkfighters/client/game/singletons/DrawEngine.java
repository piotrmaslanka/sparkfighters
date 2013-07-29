package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	
	public void DrawRectangle(int x1,int y1,int x2,int y2,int size, Color color)
	{
		Gdx.gl10.glLineWidth(size);
		shape.begin(ShapeType.Line);
		shape.setColor(color);
		shape.rect(x1, y1, x2-x1, y2-y1);
		shape.end();
	}
	
	public void DrawPoint(int x,int y, int size, Color color)
	{
		Gdx.gl10.glPointSize(size);
		shape.begin(ShapeType.Point);
		shape.setColor(color);
		shape.point(x, y, 0);
		shape.end();
	}
	
	public void DrawLine(int x,int y,int x2,int y2, int size, Color color)
	{
		Gdx.gl10.glLineWidth(size);
		shape.begin(ShapeType.Line);
		shape.setColor(color);
		shape.line(x, y, x2, y2);
		shape.end();
	}
	
	public void DrawText(int x, int y, Color color,BitmapFont font, String text)
	{
		font.setColor(color);
		batch.begin();
		font.draw(batch, text, x, y);
		batch.end();
	}
	
	
	public void DrawDebugInfo()
	{	
		//int w=GameEngine.INSTANCE.orginal_width;
		int h=GameEngine.INSTANCE.orginal_height;
		BitmapFont font=ResourcesManager.INSTANCE.font;
		Color color=Color.GREEN;
		int space_h=40;
		
		//FPS COUNTER
		DrawText(0,h,color,font,"FPS: "+Gdx.graphics.getFramesPerSecond());
		h-=space_h;
		//Input
		Input.INSTANCE.DrawDebugInfo(0, h, font, color);
		h-=space_h;
		//debug about map
		GameEngine.INSTANCE.mapFragment.DrawDebugInfo(0,h,font,color);
		h-=space_h;
		//debug about heroes
		for(int i=0;i<GameEngine.INSTANCE.actors.size();i++)
		{
			GameEngine.INSTANCE.actors.get(i).DrawDebugInfo(0,h,font,color);
			h-=space_h;
		}
			

	}
}
