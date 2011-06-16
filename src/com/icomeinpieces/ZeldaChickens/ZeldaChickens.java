
package com.icomeinpieces.ZeldaChickens;

/*
 * add some sort of check so that swarms cannot be simply summoned on someone without a confirmation.
 * add mutli world or at least investigate it.
 * check out yaml stuff
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/*
 * Changelog
 * 2.01 <-- possible fix for null pointer exception when poofafterdeath option is set to true
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
    public final Logger log = Logger.getLogger("Minecraft");
    public final String pluginName = "ZeldaChicken V. 2.01: ";
    public final ZCEntityListener zcEntityListner = new ZCEntityListener(this);
    public final ZCPlayerListener zcPlayerListener = new ZCPlayerListener(this);
    public final ZCcommands zcCommands = new ZCcommands(this);
    public PermissionHandler permissionHandler;
    public PluginManager pm;
    private String filePath = "/ZeldaChickens.cfg";

    public Integer mobSize = 10;
    public Integer mobDamage = 1;
    public Integer chickenAttacksTrigger = 2;
    public Double damageDistance = 1.0;
    public Double spawnHeight = 1.0;
    public Double maxDistance = 4.0;
    public Boolean catchUp = true;
    public Boolean deathAfterDeath = false;
    public Integer chickHealth = 4;
    public Boolean permissionsEnabaled = false;
    public Boolean state = true;
    public Boolean swarmDrops = false;

    public void onDisable()
    {
        writeConfigFile();
        log.info(pluginName + ": disabled");
    }

    public void onEnable()
    {
        log.info(pluginName + ": starting");
        pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.zcEntityListner,
                Event.Priority.Normal, this);
//        pm.registerEvent(Event.Type.ITEM_SPAWN, this.zcEntityListner,
//                Event.Priority.Normal, this);
        // pm.registerEvent(Event.Type.ENTITY_DEATH, this.zcEntityListner,
        // Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, this.zcPlayerListener, Event.Priority.Lowest, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.zcPlayerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, this.zcPlayerListener,
                Event.Priority.Lowest, this);
        permissionsEnabaled = setupPermissions();
        Reload();
        log.info(pluginName + ": enabled");
    }

    public void Reload()
    {
        File configFile = new File(getDataFolder() + filePath);
        if (configFile.exists())
        {
            Properties config = new Properties();
            try
            {
                FileInputStream in = new FileInputStream(configFile);
                config.load(in);
                mobSize = Integer
                        .parseInt(config.getProperty("chickenMobSize"));
                if (mobSize == null || mobSize <= 0)
                {
                    mobSize = 10;
                }
                mobDamage = Integer.parseInt(config
                        .getProperty("chickenDamage"));
                if (mobDamage == null || mobDamage <= 0 || mobDamage >= 11)
                {
                    mobDamage = 1;
                }
                chickenAttacksTrigger = Integer.parseInt(config
                        .getProperty("chickenHitsTrigger"));
                if (chickenAttacksTrigger == null || chickenAttacksTrigger <= 0)
                {
                    chickenAttacksTrigger = 2;
                }
                damageDistance = Double.parseDouble(config
                        .getProperty("chickenAttackRange"));
                if (damageDistance == null || damageDistance <= 0.0)
                {
                    damageDistance = 1.0;
                }
                spawnHeight = Double.parseDouble(config
                        .getProperty("chickenSpawnHeight"));
                if (spawnHeight == null || spawnHeight <= 0.0)
                {
                    spawnHeight = 1.0;
                }
                catchUp = Boolean.parseBoolean(config
                        .getProperty("catchUpEnabled"));
                if (!config.getProperty("catchUpEnabled").equalsIgnoreCase(
                        "true")
                        && !config.getProperty("catchUpEnabled")
                                .equalsIgnoreCase("false"))
                {
                    catchUp = true;
                }
                maxDistance = Double.parseDouble(config
                        .getProperty("maxOutRunDistance"));
                if (maxDistance == null || maxDistance <= 0)
                {
                    maxDistance = 8.0;
                }
                deathAfterDeath = Boolean.parseBoolean(config
                        .getProperty("poofAfterDeath"));
                if (!config.getProperty("poofAfterDeath").equalsIgnoreCase(
                        "true")
                        && !config.getProperty("poofAfterDeath")
                                .equalsIgnoreCase("false"))
                {
                    deathAfterDeath = false;
                }
                chickHealth = Integer.parseInt(config
                        .getProperty("chickenHealth"));
                if (chickHealth == null || chickHealth <= 0 || chickHealth > 20)
                {
                    chickHealth = 4;
                }
//                swarmDrops = Boolean.parseBoolean(config
//                        .getProperty("chickenSwarmDrops"));
//                if (!config.getProperty("poofAfterDeath").equalsIgnoreCase(
//                "true")
//                && !config.getProperty("poofAfterDeath")
//                        .equalsIgnoreCase("false"))
//                {
//                    swarmDrops=true;
//                }
            }
            catch (IOException e)
            {
                log
                        .warning(pluginName
                                + " unable to read the config file, restoring to defaults");
                writeConfigFile();
            }
        }
        else
        {
            log
                    .warning(pluginName
                            + " config file does not exist, ignore this if this is the first start. writing config file");
            writeConfigFile();
        }
    }

    private void writeConfigFile()
    {
        File configFile = new File(getDataFolder() + filePath);
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
                PrintWriter out = new PrintWriter(new FileWriter(
                        getDataFolder() + filePath));
                out
                        .println("#This is the configuration file for "
                                + pluginName);
                out.println("#");
                out.println("#how many chickens should spawn?");
                out.println("#(valid values, anything above 0) default: 10");
                out.println("chickenMobSize=" + mobSize);
                out.println("#how much damage should a chicken do?");
                out
                        .println("#(valid values between 1-10) default: 2(1 = half heart)");
                out.println("chickenDamage=" + mobDamage);
                out
                        .println("#how many attacks on chicken(s) should it take before a mob spawns?");
                out.println("#(valid values, anything above 0) default: 2");
                out.println("chickenHitsTrigger=" + chickenAttacksTrigger);
                out
                        .println("#how close do chickens have to be to do damage? (1 = one block");
                out.println("#(valid values are and above 0.0) default: 1.0");
                out.println("chickenAttackRange=" + damageDistance);
                out
                        .println("#how far above the player should the chickens fall from onto unspecting players?");
                out.println("#(valid values, anything above 0.0) default: 1.0");
                out.println("chickenSpawnHeight=" + spawnHeight);
                out
                        .println("#are chickens allowed to 'catch-up' to a player if they are getting outrun?");
                out
                        .println("#(valid values, true or false only) default: true, anythign but true or True will result in false");
                out.println("catchUpEnabled=" + catchUp);
                out
                        .println("#how far can a player outrun a chicken before it catches up? (respawns above player using chickenSpawnHeight.");
                out.println("#(valid values, anything above 0.0) default: 4.0");
                out.println("maxOutRunDistance=" + maxDistance);
                out
                        .println("#should chickens go poof after a player dies? (or retarget)");
                out
                        .println("#(valid values, true or false only) default: false (meaning the chickens will go after a same target once he/she respawns)");
                out.println("poofAfterDeath=" + deathAfterDeath);
                out
                        .println("#how much health should chickens that 'spawn' should have?");
                out.println("#(valid values, 1 to 20) default: 4");
                out.println("chickenHealth=" + chickHealth);
//                out.println("#should chickens summoned by this plugin be allowed to drop feathers upon death?");
//                out.println("#(valid values, true or false only) default: false (no drops from summoned chickens)");
//                out.println("chickenSwarmDrops=" + swarmDrops);
                out.println("#");
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
                log.warning(pluginName + " unable to write to file at "
                        + filePath);
            }
        }
        catch (IOException ioe)
        {
            log.warning(pluginName + " unable to create config file");
        }
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
}
