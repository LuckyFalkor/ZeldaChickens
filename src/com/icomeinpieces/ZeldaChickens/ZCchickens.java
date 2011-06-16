package com.icomeinpieces.ZeldaChickens;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

public class ZCchickens implements Runnable 
{
	public Chicken chicken=null;
	public ZCplayer ZCPs;
	public Player player;
	boolean spawned = true;
	public ZCchickens(Player player, Chicken chicken, ZCplayer instance) 
	{
		this.ZCPs = instance; 
		this.chicken = chicken;
		this.player = player;
		chicken.setHealth(ZCPs.ZCE.ZCP.chickHealth);
	}

	@Override
	public void run() 
	{
		chicken.setTarget(player);
		while(!chicken.isDead())
		{
			if(chicken.getTarget() != null)
			{
				Double xDistance = Math.abs(player.getLocation().getX() - chicken.getLocation().getX());
				Double yDistance = Math.abs(player.getLocation().getY() - chicken.getLocation().getY());
				Double zDistance = Math.abs(player.getLocation().getZ() - chicken.getLocation().getZ());
				if ((xDistance > ZCPs.ZCE.ZCP.maxDistance || zDistance > ZCPs.ZCE.ZCP.maxDistance) && ZCPs.ZCE.ZCP.catchUp)
				{
					teleportChicken();
				}
				if (yDistance > ZCPs.ZCE.ZCP.maxDistance && !spawned)
				{
					teleportChicken();
				}
				if(spawned && (player.getLocation().getY() - chicken.getLocation().getY())>0) spawned=false;
				if(spawned && (yDistance > (ZCPs.ZCE.ZCP.spawnHeight)+0.5)) spawned=false;
				if(xDistance < ZCPs.ZCE.ZCP.damageDistance && yDistance < ZCPs.ZCE.ZCP.damageDistance && zDistance < ZCPs.ZCE.ZCP.damageDistance) player.damage(ZCPs.ZCE.ZCP.mobDamage, chicken);
			}
			else
			{
				chicken.setTarget(player);
			}
			chicken.setRemainingAir(20);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) 
			{

			}
		}
	}
	   
    private void teleportChicken()
    {
        Location locale = player.getLocation();
        boolean check = true;
        locale.setY(locale.getY()+1);
        while (check)
        {
            if ((locale.getBlock().getType() == Material.WATER))
            {
                check = false;
                locale.setY(locale.getY());
            }
            else if (locale.getBlock().getType() == Material.AIR && locale.getY()<= player.getLocation().getY()+ZCPs.ZCE.ZCP.spawnHeight+1)
            {
                locale.setY(locale.getY()+1);
            }
            else
            {
                check = false;
                locale.setY(locale.getY()-1);
            }
        }
        chicken.teleport(locale);
        spawned = true;
    }
}
