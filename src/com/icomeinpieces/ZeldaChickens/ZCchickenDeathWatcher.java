package com.icomeinpieces.ZeldaChickens;

public class ZCchickenDeathWatcher implements Runnable 
{
	public ZCplayer ZCPs;
	private boolean isempty = false;
	public ZCchickenDeathWatcher(ZCplayer instance)
	{
		this.ZCPs = instance;
	}
	
	public void run() 
	{
		while (!isempty)
		{
			isempty = true;
			for (int x=0; x < ZCPs.chickens.length; x++)
			{
				if (ZCPs.chickens[x] != null)
				{
					isempty = false;
					if (ZCPs.chickens[x].chicken.isDead())
					{
                        ZCPs.chickens[x]=null;
					}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) 
			{

			}
		}
		ZCPs.chickens=null;
	}

}
