package com.icomeinpieces.ZeldaChickens;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

public class ZCchickens implements Runnable 
{
	Chicken chicken=null;
	ZCEntityListener ZCE;
	public ZCchickens(Chicken chicken, ZCEntityListener instance) 
	{
		this.ZCE = instance; 
		this.chicken = chicken;
	}

	@Override
	public void run() 
	{
		Player player = (Player) chicken.getTarget();
		while(!chicken.isDead())
		{
			
			Double xDistance = Math.abs(player.getLocation().getX() - chicken.getLocation().getX());
			Double yDistance = Math.abs(player.getLocation().getY() - chicken.getLocation().getY());
			Double zDistance = Math.abs(player.getLocation().getZ() - chicken.getLocation().getZ());
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
