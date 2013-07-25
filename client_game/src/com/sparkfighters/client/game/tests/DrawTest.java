package com.sparkfighters.client.game.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;

public class DrawTest 
{
	float stateTime;
	
	public DrawTest()
	{
		stateTime=0f;
	}
	public void runTest()
	{
		stateTime += Gdx.graphics.getDeltaTime(); 
		int x=20,y=20;
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.size();i++)
		{
			for(int j=0;j<ResourcesManager.INSTANCE.heroesData.get(i).animationsDrawable.size();j++)
			{
				TextureRegion currentFrame=ResourcesManager.INSTANCE.heroesData.get(i).animationsDrawable.get(j).getKeyFrame(stateTime, true);
				DrawEngine.INSTANCE.Draw(currentFrame, x,y);
				
				TextureRegion weapon=new TextureRegion(ResourcesManager.INSTANCE.weaponsData.get(0).texture);
				weapon.setRegion(
						(int)ResourcesManager.INSTANCE.weaponsData.get(0).right.x1, 
						(int)ResourcesManager.INSTANCE.weaponsData.get(0).right.y1, 
						(int)(ResourcesManager.INSTANCE.weaponsData.get(0).right.x2-ResourcesManager.INSTANCE.weaponsData.get(0).right.x1),
						(int)(ResourcesManager.INSTANCE.weaponsData.get(0).right.y2-ResourcesManager.INSTANCE.weaponsData.get(0).right.y1)
						);
				DrawEngine.INSTANCE.Draw(weapon,200,200);
				x+=200;
				if(x>1800)
				{
					x=20;
					y+=200;
				}
			}
		}
	}
}
