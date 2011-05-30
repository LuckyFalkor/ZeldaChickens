package com.icomeinpieces.ZeldaChickens;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class ZeldaChickens extends JavaPlugin
{
	public final Logger log = Logger.getLogger("Minecraft");
	private final String pluginName = "ZeldaChicken V.1.00";
	public final ZCEntityListener zcEntityListner = new ZCEntityListener(this);
	public final ZCPlayerListener zcPlayerListener = new ZCPlayerListener(this);
	public static PermissionHandler permissionHandler;
	private PluginManager pm;
	//private String filePath = "/ZeldaChicken.cfg";
	@Override
	public void onDisable() 
	{
		log.info(pluginName + ": disabled");
	}

	@Override
	public void onEnable() 
	{
		log.info(pluginName + ": starting");
		pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.zcEntityListner, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, this.zcEntityListner, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this.zcEntityListner, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.zcPlayerListener, Event.Priority.Normal, this);
		setupPermissions();
		log.info(pluginName + ": enabled");
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
