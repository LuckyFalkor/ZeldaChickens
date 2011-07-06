package com.icomeinpieces.ZeldaChickens;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class ZCchicken implements Runnable 
{
	public Chicken chicken=null;
	//public ZCplayer ZCPs;
	public Player player;
	boolean spawned = true;
	public ZCchicken(Player player, Chicken chicken, ZCplayer instance) 
	{
		//this.ZCPs = instance; 
		this.chicken = chicken;
		this.player = player;
		chicken.setHealth(ZeldaChickens.config.getInt(player.getWorld().getName()+".chickenHealth", ZeldaChickens.chickHealth));
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
				double maxDistance = ZeldaChickens.config.getDouble(player.getWorld().getName()+".maxOutRunDistance", ZeldaChickens.maxDistance);
				boolean catchUp = ZeldaChickens.config.getBoolean(player.getWorld().getName()+".catchUpEnabled", ZeldaChickens.catchUp);
				double damageDistance = ZeldaChickens.config.getDouble(player.getWorld().getName()+".chickenAttackRange", ZeldaChickens.damageDistance);
				int mobDamage = ZeldaChickens.config.getInt(player.getWorld().getName()+".chickenDamage", ZeldaChickens.mobDamage);
				if ((xDistance > maxDistance || zDistance > maxDistance) && catchUp)
				{
					teleportChicken();
				}
				if (yDistance > maxDistance && !spawned)
				{
					teleportChicken();
				}
				if(spawned && (player.getLocation().getY() - chicken.getLocation().getY())>0) spawned=false;
				if(spawned && (yDistance > (ZeldaChickens.config.getDouble(player.getWorld().getName() + ".chickenSpawnHeight", ZeldaChickens.spawnHeight)+0.5))) spawned=false;
				if(xDistance < damageDistance && yDistance < damageDistance && zDistance < damageDistance) player.damage(mobDamage, chicken);
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
        double spawnHeight = ZeldaChickens.config.getDouble(player.getWorld().getName()+".chickenSpawnHeight", ZeldaChickens.spawnHeight);
        while (check)
        {
            if ((locale.getBlock().getType() == Material.WATER))
            {
                check = false;
                locale.setY(locale.getY());
            }
            else if (locale.getBlock().getType() == Material.AIR && locale.getY()<= player.getLocation().getY()+spawnHeight+1)
            {
                locale.setY(locale.getY()+1);
            }
            else
            {
                check = false;
                locale.setY(locale.getY()-1);
            }
        }
        if(chicken.getLocation().getWorld() != locale.getWorld())
        {
            Chicken temp = chicken;
            chicken=(Chicken) locale.getWorld().spawnCreature(locale, CreatureType.CHICKEN);
            chicken.setHealth(temp.getHealth());
            temp.remove();
        }
        chicken.teleport(locale);
        spawned = true;
    }
}
