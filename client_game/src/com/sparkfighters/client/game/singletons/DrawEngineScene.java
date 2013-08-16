package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
/**
 * Singleton to manage draw data.
 * @author Kamil Iwiñski
 *
 */
public enum DrawEngineScene 
{
	INSTANCE;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shape;
	private float zoom=0.0f;
	/**
	 * Function to configure DrawEngine parameters
	 */
	public void Init()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameEngine.INSTANCE.orginal_width, GameEngine.INSTANCE.orginal_height);
			
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);	
		
		
		shape=new ShapeRenderer();
		shape.setProjectionMatrix(camera.combined);
	}
	/**
	 * x,y center of screen
	 * @param x
	 * @param y
	 */
	public void setPositionCamera(float x, float y)
	{
		camera.position.set(x, y, 0f);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);	
		shape.setProjectionMatrix(camera.combined);	
	}
	
	/**
	 * Function clear screen 
	 */
	public void ClearScreen()
	{
		//Gdx.gl.glClearColor(1, 1, 1, 1);
		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	/**
	 * Function draw TextureRegion in position x, y
	 * @param textureRegion
	 * @param x
	 * @param y
	 * @param rotate degrees
	 */
	public void Draw(TextureRegion textureRegion, int x, int y, float rotate)
	{	
		Sprite sprite=new Sprite(textureRegion);
		sprite.rotate(rotate);
		sprite.setX(x);
		sprite.setY(y);
		batch.begin();
		sprite.draw(batch);
		//batch.draw(textureRegion, x, y);
		batch.end();
	}
	/**
	 * Function draw Texture in position x, y
	 * @param texture
	 * @param x
	 * @param y
	 */
	public void Draw(Texture texture, int x, int y)
	{	
		batch.begin();
		batch.draw(texture, x, y);		
		batch.end();
	}
	
	/**
	 * Function draw rectangle
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param size
	 * @param color
	 */
	public void DrawRectangle(int x1,int y1,int x2,int y2,int size, Color color)
	{
		//Gdx.gl10.glLineWidth(size);
		shape.begin(ShapeType.Line);
		shape.setColor(color);
		shape.rect(x1, y1, x2-x1, y2-y1);
		shape.end();
	}
	/**
	 * Function draw point in position x, y
	 * @param x
	 * @param y
	 * @param size
	 * @param color
	 */
	public void DrawPoint(int x,int y, int size, Color color)
	{
		//Gdx.gl10.glPointSize(size);
		shape.begin(ShapeType.Point);
		shape.setColor(color);
		shape.point(x, y, 0);
		shape.end();
	}
	/**
	 * Function draw line
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param size
	 * @param color
	 */
	public void DrawLine(int x,int y,int x2,int y2, int size, Color color)
	{
		//Gdx.gl10.glLineWidth(size);
		shape.begin(ShapeType.Line);
		shape.setColor(color);
		shape.line(x, y, x2, y2);
		shape.end();
	}

}
