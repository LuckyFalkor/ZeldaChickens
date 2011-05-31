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
 * 1.01 - added config file
 * 1.00 - initial release
 */

public class ZeldaChickens extends JavaPlugin
{
	public final Logger log = Logger.getLogger("Minecraft");
	private final String pluginName = "ZeldaChicken V.1.01";
	public final ZCEntityListener zcEntityListner = new ZCEntityListener(this);
	public final ZCPlayerListener zcPlayerListener = new ZCPlayerListener(this);
	public static PermissionHandler permissionHandler;
	private PluginManager pm;
	private String filePath = "/ZeldaChickens.cfg";
	
	public Integer mobSize = 10;
	public Integer mobDamage = 1;
	public Integer chickenAttacksTrigger = 2;
	public Double damageDistance = 1.0;
	public Double spawnHeight=1.0;

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
	        	 out.println("#(valid values between 1-10) default: 1(or half heart)");
	        	 out.println("chickenDamage="+mobDamage);
	        	 out.println("#how many attacks on chicken(s) should it take before a mob spawns?");
	        	 out.println("#(valid values, anything above 0) default: 2");
	        	 out.println("chickenHitsTrigger="+chickenAttacksTrigger);
	        	 out.println("#how close do chickens have to be to do damage?");
	        	 out.println("#(valid values are and above 0.0) default: 1");
	        	 out.println("chickenAttackRange=" + damageDistance);
	        	 out.println("#how far above the player should the chickens fall from onto unspecting players?");
	        	 out.println("#(valid values, anything above 0) default: 1");
	        	 out.println("chickenSpawnHeight="+spawnHeight);	        	 
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

	      if (ZeldaChickens.permissionHandler == null) {
	          if (permissionsPlugin != null) {
	        	  ZeldaChickens.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
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
