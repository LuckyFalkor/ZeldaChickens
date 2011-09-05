package com.icomeinpieces.ZeldaChickens;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class ZCplayer
{
    //public ZCEntityListener ZCE;
    public Player player;
    public ZCchicken[] chickens;
    public Integer chickenAttacks=0;
    public ZCplayer(ZCEntityListener instance, Player player)
    {
        //ZCE = instance;
        this.player = player;
    }

    public void summonChickens()
    {
        System.gc();
        Player[] playerList = player.getServer().getOnlinePlayers();
        Player victim=null;
        for (Player playerInList : playerList)
        {
            if (playerInList.getEntityId()==player.getEntityId())
            {
                ZeldaChickens.sendMessage(ZeldaChickens.config.getString("Global.playerWarning"), playerInList);
                victim = playerInList;
            }
            ZeldaChickens.sendMessage(ZeldaChickens.config.getString("Global.globalAnnouncement"), playerInList, victim);
        }
        Location locale = player.getLocation();
        locale.setY(locale.getY()+ZeldaChickens.config.getDouble(locale.getWorld().getName() + ".chickenSpawnHeight", ZeldaChickens.spawnHeight));
        int spawnCount = 0;
        int mobSize = ZeldaChickens.config.getInt(locale.getWorld().getName() + ".chickenMobSize", ZeldaChickens.mobSize);
        chickens = new ZCchicken[mobSize];
        while (spawnCount < mobSize)
        {
            Chicken chicken = (Chicken) locale.getWorld().spawnCreature(locale, CreatureType.CHICKEN);
            chickens[spawnCount]=new ZCchicken(player, chicken, this);
            Thread ZCC=new Thread(chickens[spawnCount]);
            ZCC.start();
            spawnCount++;
        }
        Thread ZCCDW=new Thread(new ZCchickenDeathWatcher(this));
        ZCCDW.start();
        chickenAttacks=0;
    }
    //TODO use this method
    protected void killChickens()
    {
        for(ZCchicken zcChickens:chickens)
        {
            zcChickens.chicken.remove();            
        }
        System.gc();
    }
}
