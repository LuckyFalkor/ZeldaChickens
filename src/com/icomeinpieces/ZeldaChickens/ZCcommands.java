package com.icomeinpieces.ZeldaChickens;

import java.util.List;

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
            console.sendMessage(ZCP.pluginName + "configuration reloaded");
            ZCP.Reload();
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcSwarmPlayer"))
        {
            if (args==null || args.length < 1)
            {
                console.sendMessage(ZCP.pluginName + "you did not provide a player name");
                return;
            }
            if (args.length > 1)
            {
                console.sendMessage(ZCP.pluginName + "You only need one argument (playername) for this command");
                console.sendMessage(ZCP.pluginName + "Swarming mutiple people will come in a later version");
                return;
            }
            if (args[0].equalsIgnoreCase("*"))
            {
                console.sendMessage(ZCP.pluginName + "Swarming mutiple people will come in a later version");
                return;
            }
            List<Player> players = console.getServer().matchPlayer(args[0]);
            if (players.size()==1)
            {
                Player player2 = players.get(0);
                if (ZCP.zcEntityListner.getZCplayer(player2).chickens==null)
                {
                    console.sendMessage(ZCP.pluginName + "You have sent a Chicken Swarm after " + player2.getDisplayName() + ".");
                    player2.sendMessage(ZCP.pluginName + "a system admin has sent a Chicken Swarm after you , defend yourself.");
                    ZCP.zcEntityListner.summonChickensOnPlayer(ZCP.zcEntityListner.getZCplayer(player2));
                }
                else
                {
                    console.sendMessage(ZCP.pluginName + player2.getDisplayName() + " is currently fighting a swarm, please wait a moment.");
                }
            }
            else if (players.size()>1)
            {
                console.sendMessage(ZCP.pluginName + "Sorry there is more then 1 player matching the given argument");
            }
            else if (players.size()<1)
            {
                console.sendMessage(ZCP.pluginName + "Sorry there was no players matching the given argument");
            }
            else
            {
                console.sendMessage(ZCP.pluginName + "There was some sort of an error in finding players, please advise author");
            }
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcswarmthyself"))
        {
            console.sendMessage(ZCP.pluginName + "your the console, you can't swarm yourself");
            return;
        }
        if (commandLabel.equalsIgnoreCase("zc"))
        {
            if (args==null || args.length < 1)
            {
                if (ZCP.state)
                {
                    ZCP.state = false;
                    console.sendMessage(ZCP.pluginName + "swarms turned off");
                }
                else
                {
                    ZCP.state = true;
                    console.sendMessage(ZCP.pluginName + "swarms turned on");
                }
                return;
            }
            if (args.length > 1)
            {
                console.sendMessage(ZCP.pluginName + "You only need one argument [on:off] for this command");
                return;
            }
            String state = args[0];
            if (state.equalsIgnoreCase("on"))
            {
                ZCP.state = true;
                console.sendMessage(ZCP.pluginName + "swarms turned on");
                return;
            }
            if (state.equalsIgnoreCase("off"))
            {
                ZCP.state = false;
                console.sendMessage(ZCP.pluginName + "swarms turned off");
                return;
            }
            console.sendMessage(ZCP.pluginName + "Please specify state, zc [on:off]");
        }
        if (commandLabel.equalsIgnoreCase("zeldachickens"))
        {
            console.sendMessage("plugin details for "+ ZCP.pluginName);
            if (ZCP.state)
            {
                console.sendMessage("plugin functionality is enabled");
            }
            else
            {
                console.sendMessage("plugin functionality is disabled");
            }
            console.sendMessage("This plugin is designed to allow chickens to fight back like they do in the Zelda games.");
            console.sendMessage("for more info type zchelp on commands you are allowed to use");
        }
        if (commandLabel.equalsIgnoreCase("zchelp"))
        {
            console.sendMessage(ZCP.pluginName + "The current commands that you are allowed to use:");
            console.sendMessage("zeldachicken - brief info to plugin");
            console.sendMessage("zchelp - command help");
            console.sendMessage("zcstate - allows the user to toggle the plugin on or off");
            console.sendMessage("zcstate on/off specifically sets this plugin's state");
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
                player.sendMessage(ZCP.pluginName + "configuration reloaded");
            }
            else
            {
                player
                        .sendMessage(ZCP.pluginName
                                + "you are not allowed to use the zcreload command");
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
                    player.sendMessage(ZCP.pluginName + "you did not provide a player name");
                    player.sendMessage(ZCP.pluginName + "To swarm yourself use the command /zcswarmthyself");
                    return;
                }
                if (args.length > 1)
                {
                    player.sendMessage(ZCP.pluginName + "You only need one argument (playername) for this command");
                    player.sendMessage(ZCP.pluginName + "Swarming mutiple people will come in a later version");
                    return;
                }
                if (args[0].equalsIgnoreCase("*"))
                {
                    player.sendMessage(ZCP.pluginName + "Swarming mutiple people will come in a later version");
                    return;
                }
                List<Player> players = player.getServer().matchPlayer(args[0]);
                if (players.size()==1)
                {
                    Player player2 = players.get(0);
                    if (ZCP.zcEntityListner.getZCplayer(player2).chickens==null)
                    {
                        player.sendMessage(ZCP.pluginName + "You have sent a Chicken Swarm after " + player2.getDisplayName() + ".");
                        player2.sendMessage(ZCP.pluginName + player.getDisplayName() + " has sent a Chicken Swarm after you , defend yourself.");
                        ZCP.zcEntityListner.summonChickensOnPlayer(ZCP.zcEntityListner.getZCplayer(player2));
                    }
                    else
                    {
                        player.sendMessage(ZCP.pluginName + player2.getDisplayName() + " is currently fighting a swarm, please wait a moment.");
                    }
                }
                else if (players.size()>1)
                {
                    player.sendMessage(ZCP.pluginName + "Sorry there is more then 1 player matching the given argument");
                }
                else if (players.size()<1)
                {
                    player.sendMessage(ZCP.pluginName + "Sorry there was no players matching the given argument");
                }
                else
                {
                    player.sendMessage(ZCP.pluginName + "There was some sort of an error in finding players, please advise author");
                }
            }
            else
            {
                player
                        .sendMessage(ZCP.pluginName
                                + "you are not allowed to use the zcswarmplayer command");
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
                        player.sendMessage(ZCP.pluginName + "You have summoned a Chicken Swarm on yourself");
                        ZCP.zcEntityListner.summonChickensOnPlayer(ZCP.zcEntityListner.getZCplayer(player));
                    }
                    else
                    {
                        player.sendMessage(ZCP.pluginName + player.getDisplayName() + " are you crazy, finish your current fight :).");
                    }
                }
                else
                {
                    player.sendMessage(ZCP.pluginName + "You do not need any arguements for this command");
                }
            }
            else
            {
                player.sendMessage(ZCP.pluginName
                                + "you are not allowed to use the zcswarmthyself command");
            } 
            return;
        }
        if (commandLabel.equalsIgnoreCase("zcstate"))
        {
            if (player.isOp() || (ZCP.permissionsEnabaled && ZCP.permissionHandler.has(player, "zc.admin.zcstate")))
            {
                if (args==null || args.length < 1)
                {
                    if (ZCP.state)
                    {
                        ZCP.state = false;
                        player.sendMessage(ZCP.pluginName + "swarms turned off");
                    }
                    else
                    {
                        ZCP.state = true;
                        player.sendMessage(ZCP.pluginName + "swarms turned on");
                    }
                    return;
                }
                if (args.length > 1)
                {
                    player.sendMessage(ZCP.pluginName + "You only need one argument [on:off] for this command");
                    return;
                }
                String state = args[0];
                if (state.equalsIgnoreCase("on"))
                {
                    ZCP.state = true;
                    player.sendMessage(ZCP.pluginName + "swarms turned on");
                    return;
                }
                if (state.equalsIgnoreCase("off"))
                {
                    ZCP.state = false;
                    player.sendMessage(ZCP.pluginName + "swarms turned off");
                    return;
                }
                player.sendMessage(ZCP.pluginName + "Please specify state, zc [on:off]");
            }
            else
            {
                player.sendMessage(ZCP.pluginName
                                + "you are not allowed to use the zc command");
            } 
        }
        if (commandLabel.equalsIgnoreCase("zeldachickens"))
        {
            player.sendMessage("plugin details for "+ ZCP.pluginName);
            if (ZCP.state)
            {
                player.sendMessage("plugin functionality is enabled");
            }
            else
            {
                player.sendMessage("plugin functionality is disabled");
            }
            player.sendMessage("This plugin is designed to allow chickens to fight back like they do in the Zelda games.");
            player.sendMessage("for more info type zchelp on commands you are allowed to use");
        }
        if (commandLabel.equalsIgnoreCase("zchelp"))
        {
            player.sendMessage(ZCP.pluginName + "The current commands that you are allowed to use:");
            player.sendMessage("zeldachicken - brief info to plugin");
            player.sendMessage("zchelp - command help");
            if (ZCP.permissionsEnabaled)
            {
                if(ZCP.permissionHandler.has(player, "zc.admin.zcstate"))
                {
                    player.sendMessage("zcstate - allows the user to toggle the plugin on or off");
                    player.sendMessage("zcstate on/off specifically sets this plugin's state");
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
                    player.sendMessage("zcstate - allows the user to toggle the plugin on or off");
                    player.sendMessage("zcstate on/off specifically sets this plugin's state");
                    player.sendMessage("zcswarmthyself - summons a swarm on you");
                    player.sendMessage("zcswarmplayer [player] - summons swarm on target player");
                    player.sendMessage("zcreload - reloads the config file");
                    
                }
            }
        }
    }
}
