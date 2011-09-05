
package com.icomeinpieces.ZeldaChickens;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
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
 *  globalAnnouncement for displaying when a swarm is summoned, supports the :player: variable
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
    protected final Logger log = Logger.getLogger("Minecraft");
    protected final ZCEntityListener zcEntityListner = new ZCEntityListener(this);
    protected final ZCPlayerListener zcPlayerListener = new ZCPlayerListener(this);
    protected final ZCcommands zcCommands = new ZCcommands(this);

    protected static final String pluginName = "ZeldaChicken V. 2.02: ";
    protected static final String publicMessage = ":player: has incurred the wrath of the chicken goddess!";
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
    protected PermissionHandler permissionHandler;
    protected PluginManager pm;
    protected Boolean permissionsEnabaled = false;
    protected static Configuration config;

    public void onDisable()
    {
        config.save();
        log.info(pluginName + "disabled");
    }

    public void onEnable()
    {
        log.info(pluginName + "starting");
        pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.zcEntityListner,
                Event.Priority.Normal, this);
         pm.registerEvent(Event.Type.ENTITY_DEATH, this.zcEntityListner,
         Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, this.zcPlayerListener, Event.Priority.Lowest, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.zcPlayerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, this.zcPlayerListener,
                Event.Priority.Lowest, this);
        permissionsEnabaled = setupPermissions();
        Reload();
        log.info(pluginName + "enabled");
    }

    public void Reload()
    {
        List<World> worlds = getServer().getWorlds();
        File configFile = new File(getDataFolder() + "\\config.yml");
        if (configFile.canRead())
        {
            log.info("config file found at: "+configFile.getPath());
        }
        else
        {
            log.info(configFile.getPath() + " config file not found, creating file now");
            try
            {
                configFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            config = new Configuration(configFile);
            config.setHeader("#Settings file for "+pluginName);
            for(World world: worlds) 
            {
                //TODO for each world output a config setting and write to file as you go
                config.setProperty(world.getName() + ".enabled", state);
                config.setProperty(world.getName() + ".poofAfterDeath", deathAfterDeath);
                config.setProperty(world.getName() + ".catchUpEnabled", catchUp);
                config.setProperty(world.getName() + ".swarmFeatherDrops", swarmDrops);
                config.setProperty(world.getName() + ".chickenMobSize", mobSize);
                config.setProperty(world.getName() + ".chickenDamage", mobDamage);
                config.setProperty(world.getName() + ".chickenHitsTrigger", chickenAttacksTrigger);
                config.setProperty(world.getName() + ".chickenHealth", chickHealth);
                config.setProperty(world.getName() + ".chickenAttackRange", damageDistance);
                config.setProperty(world.getName() + ".chickenSpawnHeight", spawnHeight);
                config.setProperty(world.getName() + ".maxOutRunDistance", maxDistance);
            }//TODO: finish up manual settings
            config.save();
            config.getBoolean("Global.displayPluginName", true);
            config.getString("Global.globalAnnouncement", publicMessage);
            config.getString("Global.playerWarning", privateMessage);
            config.getString("Global.victoryMessage", winningMessage);
            config.save();
            
        }
        config = getConfiguration();
        config.load();
//        if ((config.getHeader()==null)||(!config.getHeader().equals("#Settings file for "+pluginName)))
//        {
//            config.setHeader("#Settings file for "+pluginName);
//        }        
        for(World world: worlds) 
        {
            int temp1;
            double temp2;
            config.getBoolean(world.getName() + ".enabled", state);
            config.getBoolean(world.getName() + ".poofAfterDeath", deathAfterDeath);
            config.getBoolean(world.getName() + ".catchUpEnabled", catchUp);
            config.getBoolean(world.getName() + ".swarmFeatherDrops", swarmDrops);
            temp1=config.getInt(world.getName() + ".chickenMobSize", mobSize);
            if (temp1 <1)config.setProperty(world.getName() + ".chickenMobSize", mobSize);
            temp1=config.getInt(world.getName() + ".chickenDamage", mobDamage);
            if (temp1 <0)config.setProperty(world.getName() + ".chickenDamage", mobDamage);
            temp1=config.getInt(world.getName() + ".chickenHitsTrigger", chickenAttacksTrigger);
            if (temp1 <1)config.setProperty(world.getName() + ".chickenHitsTrigger", chickenAttacksTrigger);
            temp1=config.getInt(world.getName() + ".chickenHealth", chickHealth);
            if (temp1 <1)config.setProperty(world.getName() + ".chickenHealth", chickHealth);
            temp2=config.getDouble(world.getName() + ".chickenAttackRange", damageDistance);
            if (temp2 <0.5)config.setProperty(world.getName() + ".chickenAttackRange", damageDistance);
            temp2=config.getDouble(world.getName() + ".chickenSpawnHeight", spawnHeight);
            if (temp2 <0.5)config.setProperty(world.getName() + ".chickenSpawnHeight", spawnHeight);
            temp2=config.getDouble(world.getName() + ".maxOutRunDistance", maxDistance);
            if (temp2 <0.5)config.setProperty(world.getName() + ".maxOutRunDistance", maxDistance);
        }
        config.getBoolean("Global.displayPluginName", true);
        config.getString("Global.globalAnnouncement", publicMessage);
        config.getString("Global.playerWarning", privateMessage);
        config.getString("Global.victoryMessage", winningMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args)
    {
        ConsoleCommandSender console = null;
        Player player = null;
        if (sender instanceof ConsoleCommandSender)
        {
            console = (ConsoleCommandSender) sender;
            ZCcommands.consoleCommand(console, commandLabel, args);
        }
        if (sender instanceof Player)
        {
            player = (Player) sender;
            zcCommands.playerCommand(player, commandLabel, args);
        }
        return true;
    }

    private boolean setupPermissions()
    {
        Plugin permissionsPlugin = this.getServer().getPluginManager()
                .getPlugin("Permissions");
        if (permissionHandler == null)
        {
            if (permissionsPlugin != null)
            {
                permissionHandler = ((Permissions) permissionsPlugin)
                        .getHandler();
                log.info(pluginName + "Permission system detected");
                return true;
            }
            else
            {
                log.info(pluginName + "Permission system not detected");
                return false;
            }
        }
        log
                .info(pluginName
                        + "critical error in detecting Permissions. please advise author");
        return false;
    }

    protected List<World> getWorlds()
    {
        return getServer().getWorlds();
    }
    protected static void sendMessage(String inputMessage, CommandSender messagee)
    {
        String message=inputMessage;
        if (message.equalsIgnoreCase("false"))
        {
            return;
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
        if (message.equalsIgnoreCase("false"))
        {
            return;
        }
        message=message.replaceAll(":player:", victim.getDisplayName());
        if (config.getBoolean("Global.displayPluginName", true))
        {
            message=pluginName+message;
        }
        messagee.sendMessage(message);
    }
}
