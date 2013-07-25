package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.sparkfighters.client.game.enums.Debug;

public enum GameEngine 
{
	INSTANCE;
	
	public final int orginal_width=1920;
	public final int orginal_height=1080;
	public int window_width;
	public int window_height;
	public Debug debug=Debug.ALLMETHODS;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public float stateTime;
	
	
	public void InitEngine(int window_width,int window_height)
	{
		this.window_width=window_width;
		this.window_height=window_height;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, orginal_width, orginal_height);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		stateTime = 0f;	
	}
	
	public void ProcessData()
	{
		
	}
	
	public void Draw()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		stateTime += Gdx.graphics.getDeltaTime(); 
		int x=20,y=20;
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.size();i++)
		{
			for(int j=0;j<ResourcesManager.INSTANCE.heroesData.get(i).animationsDrawable.size();j++)
			{
				TextureRegion currentFrame=ResourcesManager.INSTANCE.heroesData.get(i).animationsDrawable.get(j).getKeyFrame(stateTime, true);
				batch.draw(currentFrame, x,y);
				TextureRegion weapon=new TextureRegion(ResourcesManager.INSTANCE.weaponsData.get(0).texture);
				weapon.setRegion(
						(int)ResourcesManager.INSTANCE.weaponsData.get(0).right.x1, 
						(int)ResourcesManager.INSTANCE.weaponsData.get(0).right.y1, 
						(int)(ResourcesManager.INSTANCE.weaponsData.get(0).right.x2-ResourcesManager.INSTANCE.weaponsData.get(0).right.x1),
						(int)(ResourcesManager.INSTANCE.weaponsData.get(0).right.y2-ResourcesManager.INSTANCE.weaponsData.get(0).right.y1)
						);
				batch.draw(weapon,200,200);
				x+=200;
				if(x>1800)
				{
					x=20;
					y+=200;
				}
			}
		}
		
		batch.end();

	}
}
