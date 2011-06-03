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
				ZCP.zcEntityListner.chickens.firstElement().chicken.remove();
				ZCP.zcEntityListner.chickens.removeElementAt(0);
			}
		}
	}

	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) 
	{
		ZCP.log.info("respawn triggered");
		if(ZCP.deathAfterDeath)
		{
			if(ZCP.zcEntityListner.chickens.capacity()>0)
			{
				while(!ZCP.zcEntityListner.chickens.isEmpty())
				{
					ZCP.zcEntityListner.chickens.firstElement().chicken.remove();
					ZCP.zcEntityListner.chickens.removeElementAt(0);
				}
			}
		}
		else
		{
			ZCP.log.info("respawn, retarget triggered");
			for (ZCchickens ZCchicken:ZCP.zcEntityListner.chickens)
			{
				//ZCP.log.info("chicken target: " + ZCchicken.chicken.g);
				ZCchicken.player = event.getPlayer();
				ZCchicken.chicken.setTarget(ZCchicken.player);
			}
		}
	}
	
}
