package com.icomeinpieces.ZeldaChickens;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

public class ZCchickens implements Runnable 
{
	public Chicken chicken=null;
	public ZCEntityListener ZCE;
	public Player player;
	boolean spawned = true;
	public ZCchickens(Chicken chicken, ZCEntityListener instance) 
	{
		this.ZCE = instance; 
		this.chicken = chicken;
	}
	
	private void teleportChicken(Player player)
	{
		Location locale = player.getLocation();
		locale.setY(player.getLocation().getY()+ZCE.ZCP.spawnHeight);
		chicken.teleport(locale);
		spawned = true;
	}

	@Override
	public void run() 
	{
		player = (Player) chicken.getTarget();
		while(!chicken.isDead())
		{
			Double xDistance = Math.abs(player.getLocation().getX() - chicken.getLocation().getX());
			Double yDistance = Math.abs(player.getLocation().getY() - chicken.getLocation().getY());
			Double zDistance = Math.abs(player.getLocation().getZ() - chicken.getLocation().getZ());
			
			if ((xDistance > ZCE.ZCP.maxDistance || zDistance > ZCE.ZCP.maxDistance) && ZCE.ZCP.catchUp)
			{
				teleportChicken(player);
			}
			if (yDistance > ZCE.ZCP.maxDistance && !spawned)
			{
				teleportChicken(player);
			}
			if(spawned && (player.getLocation().getY() - chicken.getLocation().getY())>0) spawned=false;
			if(spawned && (yDistance > (ZCE.ZCP.spawnHeight)+0.5)) spawned=false;
			if(xDistance < ZCE.ZCP.damageDistance && yDistance < ZCE.ZCP.damageDistance && zDistance < ZCE.ZCP.damageDistance) player.damage(ZCE.ZCP.mobDamage, chicken);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) 
			{

			}
		}
		ZCE.chickens.remove(chicken);
	}
}
