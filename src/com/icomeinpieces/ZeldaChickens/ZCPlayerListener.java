package com.icomeinpieces.ZeldaChickens;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ZCPlayerListener implements Listener
{
	private ZeldaChickens ZCP;
	public ZCPlayerListener(ZeldaChickens instance) 
	{
		ZCP = instance;
	}

	@EventHandler(priority=EventPriority.MONITOR)
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
    		ZCP.zcEntityListner.players.remove(ZCP.zcEntityListner.getZCplayer(event.getPlayer()));
	    }
	}

	@EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (event.getPlayer() instanceof Player)
        {
            ZCP.zcEntityListner.players.add(new ZCplayer(ZCP.zcEntityListner, event.getPlayer()));
        }
    }

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event) 
	{
        if(event.getPlayer() instanceof Player)
        {
            Player player = event.getPlayer();
    		if(ZeldaChickens.config.getBoolean(player.getWorld().getName()+".poofAfterDeath", ZeldaChickens.deathAfterDeath))
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
