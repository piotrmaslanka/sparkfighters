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
public enum DrawEngine 
{
	INSTANCE;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shape;
	/**
	 * Function to configure DrawEngine parameters
	 */
	public void Init()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameEngine.INSTANCE.orginal_width, GameEngine.INSTANCE.orginal_height);
		
		//camera.zoom=5.0f;
		//camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);	
		
		
		shape=new ShapeRenderer();
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
	/**
	 * Function draw text in position x, y
	 * @param x
	 * @param y
	 * @param color
	 * @param font
	 * @param text
	 */
	public void DrawText(int x, int y, Color color,BitmapFont font, String text)
	{
		font.setColor(color);
		batch.begin();
		font.draw(batch, text, x, y);
		batch.end();
	}
	
	/**
	 * Function draw debug info on screen from all classes
	 */
	public void DrawDebugInfo()
	{	
		//int w=GameEngine.INSTANCE.orginal_width;
		int h=GameEngine.INSTANCE.orginal_height;
		BitmapFont font=ResourcesManager.INSTANCE.debugFont;
		Color color=Color.GREEN;
		int space_h=40;
		
		//FPS COUNTER
		DrawText(0,h,color,font,"FPS: "+Gdx.graphics.getFramesPerSecond());
		h-=space_h;
		//Input
		Input.INSTANCE.DrawDebugInfo(0, h, font, color);
		h-=space_h;
		//debug about map
		WorldManager.INSTANCE.mapFragment.DrawDebugInfo(0,h,font,color);
		h-=space_h;
		//debug about heroes
		for(int i=0;i<WorldManager.INSTANCE.actors.size();i++)
		{
			WorldManager.INSTANCE.actors.get(i).DrawDebugInfo(0,h,font,color);
			h-=space_h;
		}
			

	}
}
