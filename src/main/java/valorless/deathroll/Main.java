package valorless.deathroll;

import valorless.deathroll.hooks.PlaceholderAPIHook;
import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.config.Config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
	public static JavaPlugin plugin;
	//public static ItemMerge merger;
	public static String Name = "§7[§4Death§6Roll§7]§r";
	public static Config config;
	Boolean uptodate = true;
	int newupdate = 9999999;
	String newVersion = null;
    
    public List<String> commands = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{ 
		add("deathroll"); 
		}
	};
	
	public void onLoad() {
		plugin = this;
		
		config = new Config(this, "config.yml");
		
		Lang.lang = new Config(this, "lang.yml");
		
		CommandListener.plugin = this;
	}
	
	@SuppressWarnings("unused")
	boolean ValorlessUtils() {
		Log.Debug(plugin, "Checking ValorlessUtils");
		
		int requiresBuild = 173;
		
		String ver = Bukkit.getPluginManager().getPlugin("ValorlessUtils").getDescription().getVersion();
		//Log.Debug(plugin, ver);
		String[] split = ver.split("[.]");
		int major = Integer.valueOf(split[0]);
		int minor = Integer.valueOf(split[1]);
		int hotfix = Integer.valueOf(split[2]);
		int build = Integer.valueOf(split[3]);
		
		if(build < requiresBuild) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
        		public void run() {
        			Log.Error(plugin, String.format("DeathRoll requires ValorlessUtils build %s or newer, found %s. (%s)", requiresBuild, build, ver));
        			Log.Error(plugin, "https://www.spigotmc.org/resources/valorlessutils.109586/");
        			Bukkit.getPluginManager().disablePlugin(plugin);
        		}
    		}, 10);
			return false;
		}
		else return true;
	}
	
	@Override
    public void onEnable() {
		Log.Debug(plugin, "DeathRolling Debugging Enabled!");
		
		// Check if a correct version of ValorlessUtils is in use, otherwise don't run the rest of the code.
		if(!ValorlessUtils()) return;
		
		PlaceholderAPIHook.Hook();
		
		//Config
		config.AddValidationEntry("debug", false);
		config.AddValidationEntry("enabled", true);
		config.AddValidationEntry("check-updates", true);
		config.AddValidationEntry("death", false);
		config.AddValidationEntry("global", false);
		config.AddValidationEntry("range", 100);
		config.AddValidationEntry("max-roll", 10000);
		config.AddValidationEntry("loss-sound", "ENTITY_VILLAGER_NO");
		config.AddValidationEntry("loss-volume", 1);
		config.AddValidationEntry("loss-pitch", 1);
		config.AddValidationEntry("roll-sound", "ENTITY_PLAYER_LEVELUP");
		config.AddValidationEntry("roll-volume", 1);
		config.AddValidationEntry("roll-pitch", 1);
		config.AddValidationEntry("blacklist", new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{ add("world_name"); add("world_name_nether"); add("world_name_end"); add("another_world"); }} );
		Log.Debug(plugin, "Validating config.yml");
		config.Validate();
		
		//Lang
		Lang.lang.AddValidationEntry("prefix", "&7[&4Death&6Roll&7]&r");
		Lang.lang.AddValidationEntry("no-permission", "%prefix% &cSorry, you do not have permission to do this.");
		Lang.lang.AddValidationEntry("how-to", "%prefix% /deathroll <value>");
		Lang.lang.AddValidationEntry("value-error", "%prefix% value cannot be 1 or lower!");
		Lang.lang.AddValidationEntry("roll", "%prefix% &e%player%&r rolls &e%roll% &8(1-%roll-out-of%)");
		Lang.lang.AddValidationEntry("loser", "%prefix% &e%player%&r lost!");
		Lang.lang.AddValidationEntry("no-access", "%prefix% &cSorry, DeathRolling is disabled in this world.");
		Lang.lang.AddValidationEntry("death-message", "%player% paid the consequences..");
		Log.Debug(plugin, "Validating lang.yml");
		Lang.lang.Validate();
				
		RegisterCommands();
		
		if(config.GetBool("check-updates") == true) {
			Log.Info(plugin, "Checking for updates..");
			new UpdateChecker(this, 114882).getVersion(version -> {

				newVersion = version;
				String update = version.replace(".", "");
				newupdate = Integer.parseInt(update);
				String current = getDescription().getVersion().replace(".", "");;
				int v = Integer.parseInt(current);
				

				//if (!getDescription().getVersion().equals(version)) {
				if (v < newupdate) {
						Log.Warning(plugin, String.format("An update has been found! (v%s, you are on v%s) \n", version, getDescription().getVersion()) + 
							"This could be bug fixes or additional features.\n" + 
							"Please update DeathRoll at https://www.spigotmc.org/resources/114882/");
					
					uptodate = false;
				}else {
					Log.Info(plugin, "Up to date.");
				}
			});
		}
		
		Log.Debug(plugin, "Registering DeathRoll");
		getServer().getPluginManager().registerEvents(new DeathRoll(), this);
    }
    
    @Override
    public void onDisable() {
    }
    
    public void RegisterCommands() {

		for(String cmd : config.GetStringList("commands")) {
			commands.add(cmd);
		}
    	 for (int i = 0; i < commands.size(); i++) {
    		Log.Debug(plugin, "Registering Command: " + commands.get(i));
    		getCommand(commands.get(i)).setExecutor(new CommandListener());
    	}
    }
}
