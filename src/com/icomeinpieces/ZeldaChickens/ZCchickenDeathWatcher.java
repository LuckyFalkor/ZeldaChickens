package com.icomeinpieces.ZeldaChickens;

public class ZCchickenDeathWatcher implements Runnable 
{
	public ZCplayer ZCPlayer;
	private boolean isempty = false;
	public ZCchickenDeathWatcher(ZCplayer instance)
	{
		this.ZCPlayer = instance;
	}
	
	public void run() 
	{
		while (!isempty)
		{
			isempty = true;
			for (int x=0; x < ZCPlayer.chickens.length; x++)
			{
				if (ZCPlayer.chickens[x] != null)
				{
					isempty = false;
					if (ZCPlayer.chickens[x].chicken.isDead())
					{
                        ZCPlayer.chickens[x]=null;
					}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) 
			{

			}
		}
		ZCPlayer.chickens=null;
		ZeldaChickens.sendMessage(ZeldaChickens.config.getString("Global.victoryMessage"), ZCPlayer.player);
	}

}
