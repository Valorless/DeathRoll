package valorless.deathroll;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import valorless.valorlessutils.ValorlessUtils.Log;
import valorless.valorlessutils.ValorlessUtils.Utils;

public class CommandListener implements CommandExecutor {
	
	public static JavaPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	Log.Debug(plugin, "Sender: " + sender.getName());
    	Log.Debug(plugin, "Command: " + command.toString());
    	Log.Debug(plugin, "Label: " + label);
    	for(String a : args) {
    		Log.Debug(plugin, "Argument: " + a);
    	}
    	if(args.length >= 1) {
    		if(args[0].equalsIgnoreCase("reload")) { 
    			Main.config.Reload(); 
    			Lang.lang.Reload(); 
    			sender.sendMessage(Main.Name + " §aReloaded");
    			return true;
    		}
    	
    		if(!Main.config.GetBool("enabled")) return false;
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			if(!player.hasPermission("deathrolling.roll")) {
    				player.sendMessage(Lang.Get("no-permission"));
    			} else {
    				if(!Utils.IsStringNullOrEmpty(args[0])){
    					DeathRoll.Roll(player, args[0]);
    				}else {
    					player.sendMessage(Lang.Get("how-to"));
    				}
    			}
            	return true;
        	}
    	}else {
    		if (sender instanceof Player) {
    			sender.sendMessage(Lang.Get("how-to"));
            	return true;
        	}
    	}
    	
    	
    	
    	sender.sendMessage("Only players can Death Roll.");
        return false;
    }
}
