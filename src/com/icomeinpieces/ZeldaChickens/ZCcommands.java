package com.icomeinpieces.ZeldaChickens;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ZCcommands
{
    private static ZeldaChickens ZCP;
    public ZCcommands(ZeldaChickens instance)
    {
        ZCP = instance;
    }
    public static void consoleCommand(ConsoleCommandSender console, String commandLabel, String[] args)
    {
        if (commandLabel.equalsIgnoreCase("zcreload"))
        {
            ZeldaChickens.sendMessage("configuration reloaded", console);
            ZCP.Reload();
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcSwarmPlayer"))
        {
            if (args==null || args.length < 1)
            {
                ZeldaChickens.sendMessage("you did not provide a player name", console);
                return;
            }
            if (args.length > 1)
            {
                ZeldaChickens.sendMessage("You only need one argument (playername) for this command", console);
                ZeldaChickens.sendMessage("Swarming mutiple people will come in a later version", console);
                return;
            }
            if (args[0].equalsIgnoreCase("*"))
            {
                ZeldaChickens.sendMessage("Swarming mutiple people will come in a later version", console);
                return;
            }
            List<Player> players = console.getServer().matchPlayer(args[0]);
            if (players.size()==1)
            {
                Player player2 = players.get(0);
                if (ZCP.zcEntityListner.getZCplayer(player2).chickens==null)
                {
                    ZeldaChickens.sendMessage("You have sent a Chicken Swarm after " + player2.getDisplayName() + ".", console);
                    ZeldaChickens.sendMessage("a system admin has sent a Chicken Swarm after you , defend yourself.", player2);
                    ZCP.zcEntityListner.summonChickensOnPlayer(ZCP.zcEntityListner.getZCplayer(player2));
                }
                else
                {
                    ZeldaChickens.sendMessage(player2.getDisplayName() + " is currently fighting a swarm, please wait a moment.", console);
                }
            }
            else if (players.size()>1)
            {
                ZeldaChickens.sendMessage("Sorry there is more then 1 player matching the given argument", console);
            }
            else if (players.size()==0)
            {
                ZeldaChickens.sendMessage("Sorry there was no players matching the given argument", console);
            }
            else
            {
                ZeldaChickens.sendMessage( "There was some sort of an error in finding players, please advise author", console);
            }
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcswarmthyself"))
        {
            ZeldaChickens.sendMessage("your the console, you can't swarm yourself", console);
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcstate"))//TODO fix to accommodate multi word server names
        {
            if (args==null || args.length < 1 || !(args[0].equalsIgnoreCase("on")||args[0].equalsIgnoreCase("off")))
            {
                ZeldaChickens.sendMessage("Syntax is zc [on:off] to effect all worlds", console);
                ZeldaChickens.sendMessage("You can also use zc [on:off] [worldName] to effect a single world", console);
                return;
            }
            if (args.length == 1)
            {
                List<World> worlds = ZCP.getWorlds();
                String state = args[0];
                if (state.equalsIgnoreCase("on"))
                {
                    for(World world: worlds) 
                    {
                        ZeldaChickens.config.setProperty(world.getName() + ".enabled", true);
                    }
                    ZeldaChickens.config.save();
                    ZeldaChickens.sendMessage("swarms turned on for all worlds.", console);
                }
                else if (state.equalsIgnoreCase("off"))
                {
                    for(World world: worlds) 
                    {
                        ZeldaChickens.config.setProperty(world.getName() + ".enabled", false);
                    }
                    ZeldaChickens.config.save();
                    ZeldaChickens.sendMessage("swarms turned off for all worlds.", console);
                }
                else
                {
                    ZeldaChickens.sendMessage("If providing one argument, syntax is zcstate [on:off] to effect all worlds", console);
                }
                return;
            }
            if (args.length >= 2)
            {
                List<World> worlds = ZCP.getWorlds();
                World worldName=null;
                String state = args[0];
                String worldStringName = args[1];
                for(int x = 2; x < args.length; x++)
                {
                    worldStringName = worldStringName + " " + args[x];
                }
                for(World world: worlds) 
                {
                    if (worldStringName.equals(world.getName())) worldName = world;
                }
                if (worldName==null)
                {
                    console.sendMessage("Available worlds (caps Sensitive):");
                    for (World world:worlds)
                    {
                        console.sendMessage(world.getName());
                    }
                    return;
                }
                if (state.equalsIgnoreCase("on"))
                {
                    ZeldaChickens.config.setProperty(worldName.getName() + ".enabled", true);
                    ZeldaChickens.sendMessage("swarms turned on for " + worldName.getName() + ".", console);
                }
                if (state.equalsIgnoreCase("off"))
                {
                    ZeldaChickens.config.setProperty(worldName.getName() + ".enabled", false);
                    ZeldaChickens.sendMessage("swarms turned off for " + worldName.getName() + ".", console);
                }
                ZeldaChickens.config.save();
                return;
            }
        }
        if (commandLabel.equalsIgnoreCase("zeldachickens"))
        {
            console.sendMessage("plugin details for "+ ZeldaChickens.pluginName);
            List<World> worlds = ZCP.getWorlds();
            for(World world: worlds) 
            {
                if (ZeldaChickens.config.getBoolean(world.getName(), true))
                {
                    console.sendMessage("plugin functionality is enabled for " + world.getName());
                }
                else
                {
                    console.sendMessage("plugin functionality is disabled for " + world.getName());
                }
            }
            console.sendMessage("This plugin is designed to allow chickens to fight back like they do in the Zelda games.");
            console.sendMessage("for more info type zchelp for commands you are able to use");
        }
        if (commandLabel.equalsIgnoreCase("zchelp"))
        {
            console.sendMessage(ZeldaChickens.pluginName + "The current commands that you are allowed to use:");
            console.sendMessage("zeldachicken - brief info to plugin");
            console.sendMessage("zchelp - command help");
            console.sendMessage("zcstate [on:off] specifically sets this plugin's state, for all worlds");
            console.sendMessage("zcstate [on:off] [worldName] specifically sets this plugin's state, for [worldName]");
            console.sendMessage("zcswarmthyself - summons a swarm on you");
            console.sendMessage("zcswarmplayer [player] - summons swarm on target player");
        }
    }
    
    
    public void playerCommand(Player player, String commandLabel, String[] args)
    {
        if (commandLabel.equalsIgnoreCase("zcreload"))
        {
            if (player.isOp() || (ZCP.permissionsEnabaled
                    && ZCP.permissionHandler.has(player, "zc.admin.reload")))
            {
                ZCP.Reload();
                ZeldaChickens.sendMessage("configuration reloaded", player);
            }
            else
            {
                ZeldaChickens.sendMessage("you are not allowed to use the zcreload command", player);
            }
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcSwarmPlayer"))
        {
            if (player.isOp() || (ZCP.permissionsEnabaled
                        && ZCP.permissionHandler.has(player, "zc.admin.swarmplayer")))
            {
                if (args==null || args.length < 1)
                {
                    ZeldaChickens.sendMessage("you did not provide a player name", player);
                    ZeldaChickens.sendMessage("To swarm yourself use the command /zcswarmthyself", player);
                    return;
                }
                if (args.length > 1)
                {
                    ZeldaChickens.sendMessage("You only need one argument (playername) for this command", player);
                    ZeldaChickens.sendMessage("Swarming mutiple people will come in a later version", player);
                    return;
                }
                if (args[0].equalsIgnoreCase("*"))
                {
                    ZeldaChickens.sendMessage("Swarming mutiple people will come in a later version", player);
                    return;
                }
                List<Player> players = player.getServer().matchPlayer(args[0]);
                if (players.size()==1)
                {
                    Player player2 = players.get(0);
                    if (ZCP.zcEntityListner.getZCplayer(player2).chickens==null)
                    {
                        ZeldaChickens.sendMessage("You have sent a Chicken Swarm after " + player2.getDisplayName() + ".", player);
                        ZeldaChickens.sendMessage(" has sent a Chicken Swarm after you , defend yourself.", player2);
                        ZCP.zcEntityListner.summonChickensOnPlayer(ZCP.zcEntityListner.getZCplayer(player2));
                    }
                    else
                    {
                        ZeldaChickens.sendMessage(player2.getDisplayName() + " is currently fighting a swarm, please wait a moment.", player);
                    }
                }
                else if (players.size()>1)
                {
                    ZeldaChickens.sendMessage("Sorry there is more then 1 player matching the given argument", player);
                }
                else if (players.size()<1)
                {
                    ZeldaChickens.sendMessage("Sorry there was no players matching the given argument", player);
                }
                else
                {
                    ZeldaChickens.sendMessage("There was some sort of an error in finding players, please advise author", player);
                }
            }
            else
            {
                ZeldaChickens.sendMessage("you are not allowed to use the zcswarmplayer command", player);
            }
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcswarmthyself"))
        {
            if (player.isOp() || (ZCP.permissionsEnabaled && ZCP.permissionHandler.has(player, "zc.admin.swarmthyself")))
            {
                if (args.length == 0)
                {
                    if (ZCP.zcEntityListner.getZCplayer(player).chickens==null)
                    {
                        ZeldaChickens.sendMessage("You have summoned a Chicken Swarm on yourself", player);
                        ZCP.zcEntityListner.summonChickensOnPlayer(ZCP.zcEntityListner.getZCplayer(player));
                    }
                    else
                    {
                        ZeldaChickens.sendMessage(player.getDisplayName() + " are you crazy, finish your current fight :).", player);
                    }
                }
                else
                {
                    ZeldaChickens.sendMessage("You do not need any arguements for this command", player);
                }
            }
            else
            {
                ZeldaChickens.sendMessage("you are not allowed to use the zcswarmthyself command", player);
            } 
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcstate")) //TODO fix to accommodate multi word server names
        {
            if (player.isOp() || (ZCP.permissionsEnabaled && ZCP.permissionHandler.has(player, "zc.admin.zcstate")))
            {
                if (args==null || args.length < 1 || !(args[0].equalsIgnoreCase("on")||args[0].equalsIgnoreCase("off")))
                {
                    ZeldaChickens.sendMessage("Syntax is zc [on:off] to effect all worlds", player);
                    ZeldaChickens.sendMessage("You can also use zc [on:off] [worldName] to effect a single world", player);
                    return;
                }
                if (args.length == 1)
                {
                    List<World> worlds = ZCP.getWorlds();
                    String state = args[0];
                    if (state.equalsIgnoreCase("on"))
                    {
                        for(World world: worlds) 
                        {
                            ZeldaChickens.config.setProperty(world.getName() + ".enabled", true);
                        }
                        ZeldaChickens.config.save();
                        ZeldaChickens.sendMessage("swarms turned on for all worlds.", player);
                    }
                    else if (state.equalsIgnoreCase("off"))
                    {
                        for(World world: worlds) 
                        {
                            ZeldaChickens.config.setProperty(world.getName() + ".enabled", false);
                        }
                        ZeldaChickens.config.save();
                        ZeldaChickens.sendMessage("swarms turned off for all worlds.", player);
                    }
                    else
                    {
                        ZeldaChickens.sendMessage("If providing one argument, syntax is zcstate [on:off] to effect all worlds", player);
                    }
                    return;
                }
                if (args.length >= 2)
                {
                    List<World> worlds = ZCP.getWorlds();
                    World worldName=null;
                    String state = args[0];
                    String worldStringName = args[1];
                    for(int x = 2; x < args.length; x++)
                    {
                        worldStringName = worldStringName + " " + args[x];
                    }
                    for(World world: worlds) 
                    {
                        if (worldStringName.equals(world.getName())) worldName = world;
                    }
                    if (worldName==null)
                    {
                        player.sendMessage("Available worlds (caps Sensitive):");
                        for (World world:worlds)
                        {
                            player.sendMessage(world.getName());
                        }
                        return;
                    }
                    if (state.equalsIgnoreCase("on"))
                    {
                        ZeldaChickens.config.setProperty(worldName.getName() + ".enabled", true);
                        ZeldaChickens.sendMessage("swarms turned on for " + worldName.getName() + ".", player);
                    }
                    if (state.equalsIgnoreCase("off"))
                    {
                        ZeldaChickens.config.setProperty(worldName.getName() + ".enabled", false);
                        ZeldaChickens.sendMessage("swarms turned off for " + worldName.getName() + ".", player);
                    }
                    ZeldaChickens.config.save();
                    return;
                }
            }
            else
            {
                ZeldaChickens.sendMessage("you are not allowed to use the zc command", player);
            } 
        }
        if (commandLabel.equalsIgnoreCase("zeldachickens"))
        {
            player.sendMessage("plugin details for "+ ZeldaChickens.pluginName);
            List<World> worlds = ZCP.getWorlds();
            for(World world: worlds) 
            {
                if (ZeldaChickens.config.getBoolean(world.getName(), true))
                {
                    player.sendMessage("plugin functionality is enabled for " + world.getName());
                }
                else
                {
                    player.sendMessage("plugin functionality is disabled for " + world.getName());
                }
            }
            player.sendMessage("This plugin is designed to allow chickens to fight back like they do in the Zelda games.");
            player.sendMessage("for more info type zchelp for commands you are allowed to use");
        }
        if (commandLabel.equalsIgnoreCase("zchelp"))
        {
            ZeldaChickens.sendMessage("The current commands that you are allowed to use:", player);
            player.sendMessage("zeldachicken - brief info to plugin");
            player.sendMessage("zchelp - command help");
            if (ZCP.permissionsEnabaled)
            {
                if(ZCP.permissionHandler.has(player, "zc.admin.zcstate"))
                {
                    player.sendMessage("zcstate [on:off] specifically sets this plugin's state, for all worlds");
                    player.sendMessage("zcstate [on:off] [worldName] specifically sets this plugin's state, for [worldName]");
                }
                if(ZCP.permissionHandler.has(player, "zc.admin.swarmthyself"))
                {
                    player.sendMessage("zcswarmthyself - summons a swarm on you");
                }
                if(ZCP.permissionHandler.has(player, "zc.admin.swarmplayer"))
                {
                    player.sendMessage("zcswarmplayer [player] - summons swarm on target player");
                }
                if(ZCP.permissionHandler.has(player, "zc.admin.reload"))
                {
                    player.sendMessage("zcreload - reloads the config file");
                }
                
            }
            else
            {
                if(player.isOp())
                {
                    player.sendMessage("zcstate [on:off] specifically sets this plugin's state, for all worlds");
                    player.sendMessage("zcstate [on:off] [worldName] specifically sets this plugin's state, for [worldName]");
                    player.sendMessage("zcswarmthyself - summons a swarm on you");
                    player.sendMessage("zcswarmplayer [player] - summons swarm on target player");
                    player.sendMessage("zcreload - reloads the config file");
                    
                }
            }
        }
    }
}
