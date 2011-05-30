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
	//private ZeldaChickens ZCP;
	public Vector<Chicken> chickens = new Vector<Chicken>();
	private int deadChickens=0;
	public ZCEntityListener(ZeldaChickens instance)
	{
	    //ZCP = instance;
	}

	public void onEntityDamage(EntityDamageEvent event)
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
		if(passedChecks)
		{
			if (chickens.isEmpty()) deadChickens++;
			if (deadChickens >= 2) summonChickens(player);
		}
	}
	
	private void summonChickens(Player player)
	{
		Location locale = player.getLocation();
		locale.setY(locale.getY()+1);
		int spawnCount = 0;
		while (spawnCount <10)
		{
		chickens.add((Chicken) locale.getWorld().spawnCreature(locale, CreatureType.CHICKEN));
		chickens.elementAt(spawnCount).setTarget(player);
		//chickens[spawnCount]
		Thread t=new Thread(new ZCchickens(chickens.elementAt(spawnCount), this));
		t.start();
		spawnCount++;
		}
		deadChickens=0;
	}
}
