package com.icomeinpieces.ZeldaChickens;

import java.util.Vector;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
//    public void onItemSpawn(ItemSpawnEvent event)
//    {
//	    if(event.isCancelled())
//	    {
//	        return;
//	    }
//	    if(ZCP.swarmDrops)
//	    {
//            return;
//	    }
//	    else
//	    {
//	        if (locale != null && event.getEntity() instanceof Item)
//	        {
//	            Location itemLocale = event.getLocation();
//                Item item = (Item) event.getEntity();
//	            if (item.getItemStack().getType() == Material.FEATHER && locationMatch(locale, itemLocale))
//	            {
//	                event.setCancelled(true);
//	            }
//	        }
//	    }
//    }



    public void onEntityDamage(EntityDamageEvent event)
	{
	    ZCplayer zcPlayer = null;
		if(!event.isCancelled() && ZCP.state)
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
						    ZCP.log.info(ZCP.pluginName + "issue retrieving player from somewhere, please advise author");
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
					if (ZCP.permissionsEnabaled)
					{
						if((ZCP).permissionHandler.has(player, "zc.chickenswarm"))
						{
							if (zcPlayer.chickens == null) zcPlayer.chickenAttacks++;
							if (zcPlayer.chickenAttacks >= ZCP.chickenAttacksTrigger) summonChickensOnPlayer(zcPlayer);
						}
					}
					else
					{
						if (zcPlayer.chickens == null) zcPlayer.chickenAttacks++;
						if (zcPlayer.chickenAttacks >= ZCP.chickenAttacksTrigger) summonChickensOnPlayer(zcPlayer);
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
//    private boolean locationMatch(Location chicken2check, Location item2Check)
//    {
//        double x = Math.abs(chicken2check.getX() - item2Check.getX());
//        double y = Math.abs(chicken2check.getY() - item2Check.getY());
//        double z = Math.abs(chicken2check.getZ() - item2Check.getZ());
//        if (x<=1 && y<=1 && z<=1)
//        {
//            return true;
//        }
//        return false;
//    }
}
