package com.icomeinpieces.ZeldaChickens;

import java.util.Vector;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;


public class ZCEntityListener extends EntityListener
{
	public ZeldaChickens ZCP;
	public Vector<ZCplayer> players = new Vector<ZCplayer>();
	public Location locale=null;
	
	public ZCEntityListener(ZeldaChickens instance)
	{
	    ZCP = instance;
	}

    @Override
    public void onEntityDeath(EntityDeathEvent event)
    {
        if(ZeldaChickens.config.getBoolean(event.getEntity().getLocation().getWorld().getName() + ".swarmFeatherDrops", ZeldaChickens.swarmDrops))
        {
            return;
        }
        else
        {
            if(event.getEntity()instanceof Chicken)
            {
                Chicken chicken = (Chicken) event.getEntity();
                if (chicken.getTarget()!=null && chicken.getTarget() instanceof Player)
                {
                    Player player = (Player) chicken.getTarget();
                    ZCchicken[] chickens = getZCplayer(player).chickens.clone();
                    for(ZCchicken zcChicken:chickens)
                    {
                        if (zcChicken!=null)
                        {
                            if(chicken.getEntityId() == zcChicken.chicken.getEntityId())
                            {
                                event.getDrops().clear();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event)
	{
	    ZCplayer zcPlayer = null;
		if(!event.isCancelled() && ZeldaChickens.config.getBoolean(event.getEntity().getWorld().getName() + ".enabled", ZeldaChickens.state))
		{
				EntityDamageByEntityEvent EDBEevent = null;
				Player player = null;
				boolean passedChecks = true;
				if (event instanceof EntityDamageByEntityEvent)
				{
					EDBEevent = (EntityDamageByEntityEvent) event;
					if (EDBEevent.getDamager() instanceof Player && EDBEevent.getEntity() instanceof Chicken)
					{
						player = (Player) EDBEevent.getDamager();
						zcPlayer = getZCplayer(player);
						if (zcPlayer == null)
						{
						    passedChecks=false;
						    ZCP.log.info(ZeldaChickens.pluginName + "issue retrieving player from somewhere, please advise author");
						}
					}
					else
					{
						passedChecks=false;
					}
				}
				else
				{
					passedChecks=false;
				}
				if (passedChecks)
				{
				    int chickenAttacksTrigger = ZeldaChickens.config.getInt(event.getEntity().getWorld().getName()+".chickenHitsTrigger", ZeldaChickens.chickenAttacksTrigger);
					if (ZCP.permissionsEnabaled)
					{
						if((ZCP).permissionHandler.has(player, "zc.chickenswarm"))
						{
							if (zcPlayer.chickens == null) zcPlayer.chickenAttacks++;
							if (zcPlayer.chickenAttacks >= chickenAttacksTrigger) summonChickensOnPlayer(zcPlayer);
						}
					}
					else
					{
						if (zcPlayer.chickens == null) zcPlayer.chickenAttacks++;
						if (zcPlayer.chickenAttacks >= chickenAttacksTrigger) summonChickensOnPlayer(zcPlayer);
					}
				}
		}
	}
	
	public ZCplayer getZCplayer(Player player)
    {
	    ZCplayer zcPlayer = null;
	    for (ZCplayer zcPlayerTemp : players)
	    {
	        if (zcPlayerTemp.player.getDisplayName() == player.getDisplayName())
	        {
	            zcPlayer = zcPlayerTemp;
	            break;
	        }
	    }
	    return zcPlayer;
    }

    public void summonChickensOnPlayer(ZCplayer zcPlayer)
	{
        zcPlayer.summonChickens();
	}
}
