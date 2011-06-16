package com.icomeinpieces.ZeldaChickens;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class ZCplayer
{
    public ZCEntityListener ZCE;
    public Player player;
    public ZCchickens[] chickens;
    public Integer chickenAttacks=0;
    public ZCplayer(ZCEntityListener instance, Player player)
    {
        ZCE = instance;
        this.player = player;
    }

    public void summonChickens()
    {
        Player[] playerList = player.getServer().getOnlinePlayers();
        for (Player playerInList : playerList)
        {
            if (playerInList.getEntityId()!=player.getEntityId())
            {
                playerInList.sendMessage(player.getDisplayName() + "has incurred the wrath of the chicken goddess!");
            }
            else
            {
                playerInList.sendMessage(ZCE.ZCP.pluginName + "Warning incoming swarm");
            }
        }
        Location locale = player.getLocation();
        locale.setY(locale.getY()+ZCE.ZCP.spawnHeight);
        int spawnCount = 0;
        chickens = new ZCchickens[ZCE.ZCP.mobSize];
        while (spawnCount < ZCE.ZCP.mobSize)
        {
            Chicken chicken = (Chicken) locale.getWorld().spawnCreature(locale, CreatureType.CHICKEN);
            chickens[spawnCount]=new ZCchickens(player, chicken, this);
            Thread ZCC=new Thread(chickens[spawnCount]);
            ZCC.start();
            spawnCount++;
        }
        Thread ZCCDW=new Thread(new ZCchickenDeathWatcher(this));
        ZCCDW.start();
        chickenAttacks=0;
    }
}
