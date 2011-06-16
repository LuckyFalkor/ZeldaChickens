package com.icomeinpieces.ZeldaChickens;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
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
	    if (event.getPlayer() instanceof Player)
	    {
	        Player player = event.getPlayer();
    		if(ZCP.zcEntityListner.getZCplayer(player).chickens != null)
    		{
    			for (int x=0; x < ZCP.zcEntityListner.getZCplayer(player).chickens.length; x++)
    			{
    				if (ZCP.zcEntityListner.getZCplayer(player).chickens[x] != null)
    				{
    					ZCP.zcEntityListner.getZCplayer(player).chickens[x].chicken.remove();
    				}
    			}
    		}
    		//TODO: correct to use methods in ZCP
    		ZCP.zcEntityListner.players.remove(ZCP.zcEntityListner.getZCplayer(event.getPlayer()));
	    }
	}

    @Override
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (event.getPlayer() instanceof Player)
        {
            ZCP.zcEntityListner.players.add(new ZCplayer(ZCP.zcEntityListner, event.getPlayer()));
        }
    }

    @Override
	public void onPlayerRespawn(PlayerRespawnEvent event) 
	{
        if(event.getPlayer() instanceof Player)
        {
            Player player = event.getPlayer();
    		if(ZCP.deathAfterDeath)
    		{
    			if(ZCP.zcEntityListner.getZCplayer(player).chickens != null)
    			{
    				for (int x=0; x < ZCP.zcEntityListner.getZCplayer(player).chickens.length; x++)
    				{
    					if (ZCP.zcEntityListner.getZCplayer(player).chickens[x] != null)
    					{
    						ZCP.zcEntityListner.getZCplayer(player).chickens[x].chicken.remove();
    					}
    				}
    			}
    		}
    		else
    		{
    		    if(ZCP.zcEntityListner.getZCplayer(player).chickens != null)
                {
        			for (int x=0; x < ZCP.zcEntityListner.getZCplayer(player).chickens.length; x++)
        			{
        				if (ZCP.zcEntityListner.getZCplayer(player).chickens[x] != null)
        				{
        					ZCP.zcEntityListner.getZCplayer(player).chickens[x].player = event.getPlayer();
        				}
        			}
    			}
    		}
        }
	}
	
}
