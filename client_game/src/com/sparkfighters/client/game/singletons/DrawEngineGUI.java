package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public enum DrawEngineGUI 
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
	
}
