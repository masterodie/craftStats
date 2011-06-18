package me.masterodie.craftStats;


import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import com.alta189.sqlLibrary.MySQL.mysqlCore;

public class craftStats extends JavaPlugin {
	
	public String logPrefix = "[craftStats] ";
	public mysqlCore manageMySql; // MySQL handler
	public File propFolder = new File("plugins" + File.separator + "craftStats");
	public craftStatsSettingsHandler settings;
	private craftStatsSqlHandler sql;
	
	public String mySqlHost = null;
	public String mySqlUser = null;
	public String mySqlPasswd = null;
	public String mySqlDb = null;
	public String mySqlTablePrefix = null;
	public String mySqlUserTable = "users";	
	public String mySqlBlockTable = "blocks";
	public String mySqlItemTable = "items";	

	Logger log = Logger.getLogger("Minecraft");
	
	private final craftStatsBlockListener blockListener = new craftStatsBlockListener(this);
	private final craftStatsPlayerListener playerListener = new craftStatsPlayerListener(this);
	private final craftStatsEntityListener entityListener = new craftStatsEntityListener(this);
		
	public craftStatsCommands commandExec = new craftStatsCommands(this);
	
	public void onEnable() {
		this.log.info(this.logPrefix + "has been Enabled");
	
		settings = new craftStatsSettingsHandler("settings.properties", propFolder.getPath() + File.separator + "MySQL.properties");
		
		settings.load();
		
		if(settings.file.exists()) {
			
			if (this.settings.isValidProperty("host")) this.mySqlHost = this.settings.getPropertyString("host");
			if (this.settings.isValidProperty("database")) this.mySqlDb = this.settings.getPropertyString("database");
			if (this.settings.isValidProperty("username")) this.mySqlUser = this.settings.getPropertyString("username");
			if (this.settings.isValidProperty("password")) this.mySqlPasswd = this.settings.getPropertyString("password");
			if (this.settings.isValidProperty("table_prefix")) this.mySqlTablePrefix = this.settings.getPropertyString("table_prefix");
		} else {
			this.log.warning(this.logPrefix + "Check MySQL.properties");
		}
		
		sql = new craftStatsSqlHandler(this);
		sql.mySqlConnect();
		
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
		
		getCommand("dmg").setExecutor(commandExec);
	}
	
	public void onDisable() {
		log.info("craftStats has been Disabled");
	}
}
