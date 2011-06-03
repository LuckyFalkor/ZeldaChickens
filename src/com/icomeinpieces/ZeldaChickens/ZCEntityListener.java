package com.icomeinpieces.ZeldaChickens;

import java.util.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;


public class ZCEntityListener extends EntityListener
{
	public ZeldaChickens ZCP;
	public Vector<ZCchickens> chickens = new Vector<ZCchickens>();
	private int chickenAttacks=0;
	public ZCEntityListener(ZeldaChickens instance)
	{
	    ZCP = instance;
	}

	public void onEntityDamage(EntityDamageEvent event)
	{
		if(!event.isCancelled())
		{
				EntityDamageByEntityEvent EDBEevent = null;
				Player player = null;
				boolean passedChecks = true;
				if (event instanceof EntityDamageByEntityEvent)
				{
					EDBEevent = (EntityDamageByEntityEvent) event;
				}
				if (EDBEevent != null)
				{
					if (EDBEevent.getDamager() instanceof Player)
					{
						player = (Player) EDBEevent.getDamager();
					}
					else
					{
						passedChecks=false;
					}
					if(!(EDBEevent.getEntity() instanceof Chicken)) passedChecks=false;
				}
				else
				{
					passedChecks=false;
				}
				if(passedChecks && (ZCP).permissionHandler.has(player, "zc.chickenswarm"))
				{
					if (chickens.isEmpty()) chickenAttacks++;
					if (chickenAttacks >= ZCP.chickenAttacksTrigger) summonChickens(player);
				}
		}
	}
	
	private void summonChickens(Player player)
	{
		Location locale = player.getLocation();
		locale.setY(locale.getY()+ZCP.spawnHeight);
		int spawnCount = 0;
		while (spawnCount < ZCP.mobSize)
		{
			Chicken chicken = (Chicken) locale.getWorld().spawnCreature(locale, CreatureType.CHICKEN);
			chickens.add(new ZCchickens(chicken, this));
			chickens.elementAt(spawnCount).chicken.setTarget(player);
			//chickens[spawnCount]
			Thread t=new Thread(chickens.elementAt(spawnCount));
			t.start();
			spawnCount++;
		}
		chickenAttacks=0;
	}
}
