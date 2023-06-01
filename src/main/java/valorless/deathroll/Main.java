package valorless.deathroll;

import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.config.Config;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
	public static JavaPlugin plugin;
	//public static ItemMerge merger;
	public static String Name = "§7[§4Death§6Roll§7]§r";
	public static Config config;
    
    public String[] commands = {
    		"deathroll", "roll", "dr"
    };
	
	public void onLoad() {
		plugin = this;
		
		config = new Config(this, "config.yml");
		
		Lang.lang = new Config(this, "lang.yml");
		
		CommandListener.plugin = this;
	}
	
	@Override
    public void onEnable() {
		Log.Debug(plugin, "DeathRolling Debugging Enabled!");
		
		//Config
		config.AddValidationEntry("debug", "false");
		config.AddValidationEntry("enabled", "true");
		config.AddValidationEntry("loss-sound", "ENTITY_VILLAGER_NO");
		config.AddValidationEntry("loss-volume", 1);
		config.AddValidationEntry("loss-pitch", 1);
		config.AddValidationEntry("roll-sound", "ENTITY_PLAYER_LEVELUP");
		config.AddValidationEntry("roll-volume", 1);
		config.AddValidationEntry("roll-pitch", 1);
		Log.Debug(plugin, "Validating config.yml");
		config.Validate();
		
		//Lang
		Lang.lang.AddValidationEntry("prefix", "&7[&4Death&6Rolling&7]&r");
		Lang.lang.AddValidationEntry("no-permission", "{prefix} &cSorry, you do not have permission to do this.");
		Lang.lang.AddValidationEntry("how-to", "{prefix} /deathroll <value>");
		Lang.lang.AddValidationEntry("value-error", "{prefix} value cannot be 1 or lower!");
		Lang.lang.AddValidationEntry("roll", "{prefix} &e%s&r rolls &e%s &8(1-%s)");
		Lang.lang.AddValidationEntry("loser", "{prefix} &e%s&r lost!");
		Log.Debug(plugin, "Validating lang.yml");
		Lang.lang.Validate();
				
		RegisterCommands();
    }
    
    @Override
    public void onDisable() {
    }
    
    public void RegisterCommands() {
    	 for (int i = 0; i < commands.length; i++) {
    		Log.Debug(plugin, "Registering Command: " + commands[i]);
    		getCommand(commands[i]).setExecutor(new CommandListener());
    	}
    }
}
