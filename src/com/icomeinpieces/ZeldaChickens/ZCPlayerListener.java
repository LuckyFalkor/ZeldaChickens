package com.icomeinpieces.ZeldaChickens;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ZCPlayerListener extends PlayerListener
{
	private ZeldaChickens ZCP;
	public ZCPlayerListener(ZeldaChickens instance) 
	{
		ZCP = instance;
	}

	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if(ZCP.zcEntityListner.chickens.capacity()>0)
		{
			while(!ZCP.zcEntityListner.chickens.isEmpty())
			{
				ZCP.zcEntityListner.chickens.firstElement().remove();
				ZCP.zcEntityListner.chickens.removeElementAt(0);
			}
		}
	}

	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) 
	{
		ZCP.log.info("respawn triggered");
		if(ZCP.zcEntityListner.chickens.capacity()>0)
		{
			while(!ZCP.zcEntityListner.chickens.isEmpty())
			{
				ZCP.zcEntityListner.chickens.firstElement().remove();
				ZCP.zcEntityListner.chickens.removeElementAt(0);
			}
		}
	}
	
}
