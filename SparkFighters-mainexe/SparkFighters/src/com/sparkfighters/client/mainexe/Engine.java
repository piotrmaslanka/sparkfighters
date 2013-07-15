package com.sparkfighters.client.mainexe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.sparkfighters.client.ResourcesManager.ResourcesManager;

public enum Engine {
	INSTANCE;
	
	public final int orginal_width=1920;
	public final int orginal_height=1080;
	public int window_width;
	public int window_height;
	public float stateTime;
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	public Array<Animation> animations=new Array<Animation>();
	
	
	public void InitEngine(int window_width,int window_height)
	{
		this.window_width=window_width;
		this.window_height=window_height;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, orginal_width, orginal_height);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		stateTime = 0f;
		
		
		for(int i=0; i<ResourcesManager.INSTANCE.heroesData.size();i++)
		{
			for(int j=0;j<ResourcesManager.INSTANCE.heroesData.get(i).Animations.size();j++)
			{
				TextureRegion[] frames=new TextureRegion[ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).frames.size()];
				
				for(int k=0;k<ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).frames.size();k++)
				{
					int x=(int)ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).frames.get(k).x1;
				    int y=(int)ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).frames.get(k).y1;
				    int w=(int)ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).frames.get(k).x2-x;
				    int h=(int)ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).frames.get(k).y2-y;
					TextureRegion tr=new TextureRegion(ResourcesManager.INSTANCE.heroesData.get(i).texture);
					tr.setRegion(x,y, w, h);
					frames[k]=tr;
				}
				Animation a=new Animation(ResourcesManager.INSTANCE.heroesData.get(i).Animations.get(j).speedOfAnimation,frames);
				animations.add(a);
			}
		}
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
		for(Animation a:animations)
		{
			TextureRegion currentFrame=a.getKeyFrame(stateTime, true);
			batch.draw(currentFrame, x,y);
			x+=200;
			if(x>1800)
			{
				x=20;
				y+=200;
			}
		}
		batch.end();

	}
}
