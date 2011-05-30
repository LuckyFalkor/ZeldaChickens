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
			if(xDistance < 1 && yDistance < 1 && zDistance < 1) player.damage(1, chicken);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ZCE.chickens.remove(chicken);
	}
}
