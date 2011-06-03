package com.icomeinpieces.ZeldaChickens;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/*
 * Changelog
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
	private final String pluginName = "ZeldaChicken V.1.05";
	public final ZCEntityListener zcEntityListner = new ZCEntityListener(this);
	public final ZCPlayerListener zcPlayerListener = new ZCPlayerListener(this);
	public PermissionHandler permissionHandler;
	private PluginManager pm;
	private String filePath = "/ZeldaChickens.cfg";
	
	public Integer mobSize = 10;
	public Integer mobDamage = 1;
	public Integer chickenAttacksTrigger = 2;
	public Double damageDistance = 1.0;
	public Double spawnHeight=1.0;
	public Double maxDistance = 4.0;
	public Boolean catchUp= true;
	public Boolean deathAfterDeath = false;

	public void onDisable() 
	{
		writeConfigFile();
		log.info(pluginName + ": disabled");
	}

	public void onEnable() 
	{
		log.info(pluginName + ": starting");
		pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.zcEntityListner, Event.Priority.Normal, this);
		//pm.registerEvent(Event.Type.ENTITY_DEATH, this.zcEntityListner, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.zcPlayerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.zcPlayerListener, Event.Priority.Lowest, this);
		setupPermissions();
		Reload();
		log.info(pluginName + ": enabled");
	}
	
	public void Reload()
	{
		File configFile = new File(getDataFolder()+ filePath);
		if (configFile.exists())
	    {
			Properties config = new Properties();
	        try
	        {
	        FileInputStream in = new FileInputStream(configFile);
	        config.load(in);
	        mobSize = Integer.parseInt(config.getProperty("chickenMobSize"));
	        if (mobSize == null || mobSize<=0) mobSize=10;
	        mobDamage = Integer.parseInt(config.getProperty("chickenDamage"));
	        if (mobDamage == null || mobDamage <= 0 || mobDamage >= 11) mobDamage=1;
	        chickenAttacksTrigger = Integer.parseInt(config.getProperty("chickenHitsTrigger"));
	        if (chickenAttacksTrigger == null || chickenAttacksTrigger <=0) chickenAttacksTrigger=2;
	        damageDistance = Double.parseDouble(config.getProperty("chickenAttackRange"));
	        if (damageDistance == null || damageDistance <=0.0) damageDistance =1.0;
	        spawnHeight = Double.parseDouble(config.getProperty("chickenSpawnHeight"));
	        if (spawnHeight == null || spawnHeight <=0.0) spawnHeight=1.0;
	        catchUp = Boolean.parseBoolean(config.getProperty("catchUpEnabled"));
	        maxDistance = Double.parseDouble(config.getProperty("maxOutRunDistance"));
	        if (maxDistance == null || maxDistance <=0) maxDistance=8.0;
	        deathAfterDeath = Boolean.parseBoolean(config.getProperty("poofAfterDeath"));
	        }
	        catch (IOException e)
	        {
	        	log.warning(pluginName + " unable to read the config file, restoring to defaults");
	        	writeConfigFile();
	        }
	     }
	     else
	     {
	    	 log.warning(pluginName + " config file does not exist, ignore this if this is the first start. writing config file");
	    	 writeConfigFile();
	     }
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
	        	 PrintWriter out = new PrintWriter(new FileWriter(getDataFolder()+filePath));
	        	 out.println("#This is the configuration file for " + pluginName);
	        	 out.println("#");
	        	 out.println("#how many chickens should spawn?");
	        	 out.println("#(valid values, anything above 0) default: 10");
	        	 out.println("chickenMobSize="+mobSize);
	        	 out.println("#how much damage should a chicken do?");
	        	 out.println("#(valid values between 1-10) default: 2(1 = half heart)");
	        	 out.println("chickenDamage="+mobDamage);
	        	 out.println("#how many attacks on chicken(s) should it take before a mob spawns?");
	        	 out.println("#(valid values, anything above 0) default: 2");
	        	 out.println("chickenHitsTrigger="+chickenAttacksTrigger);
	        	 out.println("#how close do chickens have to be to do damage? (1 = one block");
	        	 out.println("#(valid values are and above 0.0) default: 1.0");
	        	 out.println("chickenAttackRange=" + damageDistance);
	        	 out.println("#how far above the player should the chickens fall from onto unspecting players?");
	        	 out.println("#(valid values, anything above 0.0) default: 1.0");
	        	 out.println("chickenSpawnHeight="+spawnHeight);
	        	 out.println("#are chickens allowed to 'catch-up' to a player if they are getting outrun?");
	        	 out.println("#(valid values, true or false only) default: true, anythign but true or True will result in false");
	        	 out.println("catchUpEnabled="+catchUp);
	        	 out.println("#how far can a player outrun a chicken before it catches up? (respawns above player using chickenSpawnHeight.");
	        	 out.println("#(valid values, anything above 0.0) default: 4.0");
	        	 out.println("maxOutRunDistance="+maxDistance);
	        	 out.println("#should chickens go poof after a player dies? (or retarget)");
	        	 out.println("#(valid values, true or false only) default: false (meaning the chickens will go after a same target once he/she respawns)");
	        	 out.println("poofAfterDeath="+deathAfterDeath);
	        	 out.println("#");
	        	 out.println("#permission nodes:");
	        	 out.println("zc.chickenswarm <-- people with this permission will be suceptable to attacks. those without are immune");
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

	private boolean setupPermissions() {
	      Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

	      if (permissionHandler == null) {
	          if (permissionsPlugin != null) {
	        	  permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	              log.info(pluginName + ": Permission system detected");
	              return true;
	          } else {
	              log.info(pluginName + ": Permission system not detected");
	              return true;
	          }
	      }
	      log.info("critical error in detecting Permissions. please advise author");
		return false;
	  }
}
