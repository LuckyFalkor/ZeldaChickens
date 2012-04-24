
package com.icomeinpieces.ZeldaChickens;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//import org.bukkit.command.Command;
//import org.bukkit.command.ConsoleCommandSender;
//import org.bukkit.event.Event;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.util.config.Configuration;
//TODO make commands multi-world aware. (almost done needs zcstate tweaking)<-- finished? requires testing.

/*TODO future things to work on
 *setup a command to kill all summoned chickens
 *plan for new worlds on the fly? might need a new registered event.
 *finish up state switching (add chicken killing)
 */

/*
 * Changelog
 * 2.02 changed config system over to YAML style.
 * added enabled option for turning the plugin on or off for each world
 * added swarmFeatherDrops option to prevent farming of chicken swarms(normal chickens still drop feathers)
 * added multi world support for all settings
 * added customisable messages for the following situations (use false to disable the message): 
 *  globalAnnouncement for displaying when a swarm is summoned, supports the PLAYER variable
 *  playerWarning for the player whom a chickenswarm is summoned after (no variable support at this time)
 *  victoryMessage for the player who has defeated the chicken swarm (no variable support at this time)
 * added option to enable or disable the plugin name prefix to messages from ZeldaChickens
 * fixed a bug that prevented chickens from crossing worlds (like when using a portal). <-- experimental
 * 2.01 <--fix for null pointer exception when poofafterdeath option is set to true
 * version 2.0
 * fixed bug that only allowed one swarm at a time on a server
 * added commands (check the plugin.yml for full list) including help section
 * added permission nodes for all commands
 * made the chickens a little more aggressive when being outrun, they now will spawn under any blocks that may be above you.
 * made chickens follow you under water and not drown. however due to a glitch that i believe to be from the minecraft AI concerning chickens, chickens will
 *      only follow by teleporting. it seems they do not know what to do while under water and just don't move at all at this point.
 * admin permission nodes will default to OP in the absence of a permissions plugin.
 * permissions requires 3.x now
 * requires CB 860
 * added config option to disable feather drops from chickens this plugin spawns defaults to false.
 * various code clean ups (still loads to do, expect more updates as i clean up code, updates of this nature will be optional)
 * various small performance tweaks.
 * added various messages such as global messages for when a player angers the swarm, rate my messages and let me know what you think, pretty please ;)
 * fixed bug that allowed players to contribute to a "pool" of chicken attacks and the last player to make a hit suffers the swarm (basically hot potato)
 * added a few more info messages to help debugging for rare cases that don't actually case the plugin to error out (print stacks)
 * 
 * 1.06 added chicken health to the config some code clean ups
 * 1.05 added chicken retargeting, if a player under attack dies and respawns the remainder of the chicken mob resumes the attack.
 * 1.04 changed how the permissions worked
 * 1.03 added catch up functionality for chickens
 * 1.02 - added permissions support
 * 1.01 - added config file
 * 1.00 - initial release
 */

public class ZeldaChickens extends JavaPlugin
{
    protected Logger log;
    protected ZCEntityListener zcEntityListner;
    protected ZCPlayerListener zcPlayerListener;
    //protected final ZCcommands zcCommands = new ZCcommands(this);

    protected static final String pluginName = "ZeldaChicken V. 2.02: ";
    protected static final String publicMessage = "PLAYER has incurred the wrath of the chicken goddess!";
    protected static final String privateMessage = "Warning incoming swarm!";
    protected static final String winningMessage = "The chicken swarm is dead, your safe for now.";
    protected static final Integer mobSize = 10;
    protected static final Integer mobDamage = 1;
    protected static final Integer chickenAttacksTrigger = 2;
    protected static final Double damageDistance = 1.0;
    protected static final Double spawnHeight = 1.0;
    protected static final Double maxDistance = 4.0;
    protected static final Boolean catchUp = true;
    protected static final Boolean deathAfterDeath = false;
    protected static final Integer chickHealth = 4;
    protected static final Boolean state = true;
    protected static final Boolean swarmDrops = false;
    
//    private  static final String filePath = "/ZeldaChickens.cfg";
    protected PluginManager pm;
    protected Boolean permissionsEnabaled = false;
    protected static FileConfiguration config;
    private String filePath = "\\config.yml";
    
    public void onDisable()
    {
        log.info(pluginName + "disabled");
    }
    
    public void onEnable()
    {   
        //make assignments
        log = Logger.getLogger("Minecraft");
        zcEntityListner = new ZCEntityListener(this);
        zcPlayerListener = new ZCPlayerListener(this);
        
        log.info(pluginName + "starting");
        pm = getServer().getPluginManager();
        pm.registerEvents(zcEntityListner, this);
        pm.registerEvents(zcPlayerListener, this);
        //permissionsEnabaled = setupPermissions();
        Reload();
        log.info(pluginName + "enabled");
    }

    public void Reload()
    {
//        List<World> worlds = getServer().getWorlds();
        File configFile = new File(getDataFolder() + "\\config.yml");
        if (configFile.canRead())
        {
            log.info("config file found at: "+configFile.getPath());
        }
        else
        {
            log.info(configFile.getPath() + " config file not found, creating file now");
            writeConfigFile();
//            config.addDefault("Global.displayPluginName", true);
//            config.addDefault("Global.globalAnnouncement", publicMessage);
//            config.addDefault("Global.playerWarning", privateMessage);
//            config.addDefault("Global.victoryMessage", winningMessage);
//            for(World world: worlds) 
//            {
//            	config.addDefault(world.getName() + ".enabled", state);
//                config.addDefault(world.getName() + ".enabled", state);
//                config.addDefault(world.getName() + ".poofAfterDeath", deathAfterDeath);
//                config.addDefault(world.getName() + ".catchUpEnabled", catchUp);
//                config.addDefault(world.getName() + ".swarmFeatherDrops", swarmDrops);
//                config.addDefault(world.getName() + ".chickenMobSize", mobSize);
//                config.addDefault(world.getName() + ".chickenDamage", mobDamage);
//                config.addDefault(world.getName() + ".chickenHitsTrigger", chickenAttacksTrigger);
//                config.addDefault(world.getName() + ".chickenHealth", chickHealth);
//                config.addDefault(world.getName() + ".chickenAttackRange", damageDistance);
//                config.addDefault(world.getName() + ".chickenSpawnHeight", spawnHeight);
//                config.addDefault(world.getName() + ".maxOutRunDistance", maxDistance);
//            }
//            this.saveConfig();
//            
        }
        config = this.getConfig();
    }
    

    private void writeConfigFile()
    {
        File configFile = new File(getDataFolder()+ filePath);
        File dir = new File(getDataFolder().toString());
        if (!dir.exists())
        {
            dir.mkdir();
            log.info(pluginName + " data directory created");
        }
        if (configFile.exists())
        {
            configFile.delete();
        }
        try
        {
            configFile.createNewFile();
            try
            {
                List<World> worlds = getServer().getWorlds();
                PrintWriter out = new PrintWriter(new FileWriter(getDataFolder()+filePath));
                out.println("#This is the configuration file for " + pluginName);
                out.println("#");
                out.println("#Global settings");
                out.println("Global:");
                out.println("    #Global announcement when a player has triggered a swarm. " +
                		"use PLAYER to specify where you want the player name to show up");
                out.println("    globalAnnoucement: " + publicMessage);
                out.println("    #The meassage a player gets when the swarm spawns after that player");
                out.println("    playerWarning: " + privateMessage);
                out.println("    #The message a player gets when they finally defeat all " +
                		"the chickens that were after that player");
                out.println("    victoryMessage: " + winningMessage);
                out.println("    #A simple boolean setting if the plugin name should display " +
                		"as a prefix to any messages generated in game from this plugin");
                out.println("    displayPluginName: " + "true");
                
                for(World world: worlds) 
                {
                    String worldName = world.getName();
                    out.println("#For World " + worldName);
                    out.println(worldName+":");
                    out.println("#should the plugin be enabled?");
                    out.println("#(valid values, true or false only) default: true, anythign but true or True will result in false");
                    out.println("    enabled: "+state);
                    out.println("#how many chickens should spawn?");
                    out.println("#(valid values, anything above 0) default: 10");
                    out.println("    chickenMobSize: "+mobSize);
                    out.println("#how much damage should a chicken do?");
                    out.println("#(valid values between 1-10) default: 1(or half heart)");
                    out.println("    chickenDamage: "+mobDamage);
                    out.println("#how many attacks on chicken(s) should it take before a mob spawns?");
                    out.println("#(valid values, anything above 0) default: 2");
                    out.println("    chickenHitsTrigger: "+chickenAttacksTrigger);
                    out.println("#how close do chickens have to be to do damage?");
                    out.println("#(valid values are and above 0.0) default: 1");
                    out.println("    chickenAttackRange: " + damageDistance);
                    out.println("#how far above the player should the chickens fall from onto unspecting players?");
                    out.println("#(valid values, anything above 0) default: 1");
                    out.println("    chickenSpawnHeight: "+spawnHeight);
                    out.println("#are chickens allowed to 'catch-up' to a player if they are getting outrun?");
                    out.println("#(valid values, true or false only) default: true, anythign but true or True will result in false");
                    out.println("    catchUpEnabled: "+catchUp);
                    out.println("#how far can a player outrun a chicken before it catches up? (respawns above player using chickenSpawnHeight.");
                    out.println("#(valid values, anything above 0.0) default: 4.0");
                    out.println("    maxOutRunDistance: "+maxDistance);
                    out.println("#should chickens go poof after a player dies? (or retarget)");
                    out.println("#(valid values, true or false only) default: false (meaning the chickens will go after a same target once he/she respawns)");
                    out.println("    poofAfterDeath: "+deathAfterDeath);
                    out.println("#how much health should chickens that 'spawn' should have?");
                    out.println("#(valid values, 1 to 20) default: 4");
                    out.println("    chickenHealth: " + chickHealth); 
                    out.println("#should chickens summoned by this plugin be allowed to drop feathers upon death?");
                    out.println("#(valid values, true or false only) default: false (no drops from summoned chickens)");
                    out.println("    chickenSwarmDrops: " + swarmDrops); 
                    out.println("#");
                }
                out.println("#permission nodes:");
                out.println("#zc.chickenswarm <-- people with this permission (or *) will be suceptable to attacks. those without are immune");
                out.println("#zc.admin.reload <-- people with this permission have acces to the zcreload command");
                out.println("#zc.admin.swarmplayer <-- allows the use of the swarmplayer command");
                out.println("#zc.admin.swarmthyself <-- allows the use of the swarmthyself command");
                out.println("#zc.admin.zcstate <-- allows the use of the zcstate command");
                out.println("#");
                out.println("#commands:");
                out.println("#zcswarmplayer <-- force a swarm attack on someone. warning bypasses the chickenswarm permission node");
                out.println("#zcswarmthyself <-- force a swarm attack on yourself. warning bypasses the chickenswarm permission node");
                out.println("#zcreload <-- reloads the configuration file");
                out.println("#zcstate <-- toggles the plugin's state between on and off, you can use zcstate [on:off] as well");
                out.println("#zchelp <-- displays the commands a player is allowed to use and how to use them");
                out.println("#zeldachickens <-- displays basic info about the plugin");
                out.close();
                log.info(pluginName + " config file written to " + filePath);
            }
            catch (IOException e)
            {
                log.warning(pluginName + " unable to write to file at " + filePath);
            }
        }
        catch(IOException ioe)
        {
            log.warning(pluginName + " unable to create config file");
        }
    }
    

//    public boolean onCommand(CommandSender sender, Command cmd,
//            String commandLabel, String[] args)
//    {
//        ConsoleCommandSender console = null;
//        Player player = null;
//        if (sender instanceof ConsoleCommandSender)
//        {
//            console = (ConsoleCommandSender) sender;
//            ZCcommands.consoleCommand(console, commandLabel, args);
//        }
//        if (sender instanceof Player)
//        {
//            player = (Player) sender;
//            zcCommands.playerCommand(player, commandLabel, args);
//        }
//        return true;
//    }

//    private boolean setupPermissions()
//    {
//        Plugin permissionsPlugin = this.getServer().getPluginManager()
//                .getPlugin("Permissions");
//        if (permissionHandler == null)
//        {
//            if (permissionsPlugin != null)
//            {
//                permissionHandler = ((Permissions) permissionsPlugin)
//                        .getHandler();
//                log.info(pluginName + "Permission system detected");
//                return true;
//            }
//            else
//            {
//                log.info(pluginName + "Permission system not detected");
//                return false;
//            }
//        }
//        log
//                .info(pluginName
//                        + "critical error in detecting Permissions. please advise author");
//        return false;
//    }

    protected List<World> getWorlds()
    {
        return getServer().getWorlds();
    }
    
    protected static void sendMessage(String inputMessage, CommandSender messagee)
    {
        String message=inputMessage;
        if (message == null)
        {
            message="problem detecting message, null recieved";
        }
        if (message.equalsIgnoreCase("false"))
        {
            message="problem detecting message, false recieved";
        }
        if (config.getBoolean("Global.displayPluginName", true))
        {
            message=pluginName+message;
        }
        messagee.sendMessage(message);
    }

    protected static void sendMessage(String inputMessage, CommandSender messagee,
            Player victim)
    {
        String message=inputMessage;
        if (message == null)
        {
            message="problem detecting message, null recieved";
        }
        if (message.equalsIgnoreCase("false"))
        {
            message="problem detecting message, false recieved";
        }
        message=message.replaceAll("PLAYER", victim.getDisplayName());
        if (config.getBoolean("Global.displayPluginName", true))
        {
            message=pluginName+message;
        }
        messagee.sendMessage(message);
    }
}
