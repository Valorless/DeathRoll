package valorless.deathroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DeathRoll {

	public static void Roll(Player player, String value) {
		Random rand = new Random();
		// nextInt as provided by Random is exclusive of the top value so you need to add 1 
		Integer num = Integer.parseInt(value);
		if(num <= 1) {
			player.sendMessage(Lang.Get("value-error"));
			return;
		}
		Integer min = 1;
		Integer max = num;
		Integer randomNum = rand.nextInt((max - min) + 1) + min;
		List<Entity> nbe = new ArrayList<Entity>();
		nbe = player.getNearbyEntities(100, 100, 100);
		List<Entity> nearbyPlayers = new ArrayList<Entity>();
		nearbyPlayers.add(player);
		for(Entity ent : nbe) {
			if(ent instanceof Player) {
				nearbyPlayers.add(ent);
			}
		}
		
		for(Entity p : nearbyPlayers) {
			if(p.getWorld() == player.getWorld()) {
			}
			p.sendMessage(Lang.Get("roll", player.getName(), randomNum.toString(), value));
			if(randomNum == 1) {
				p.sendMessage(Lang.Get("loser", player.getName()));
			}
		}
		
		if(randomNum == 1) {
			SFX.Play(Main.config.GetString("loss-sound"),
					Main.config.GetFloat("loss-volume").floatValue(),
					Main.config.GetFloat("loss-pitch").floatValue(),
					player);
		}else {
			SFX.Play(Main.config.GetString("roll-sound"),
					Main.config.GetFloat("roll-volume").floatValue(),
					Main.config.GetFloat("roll-pitch").floatValue(),
					player);
		}
		
	}
}
