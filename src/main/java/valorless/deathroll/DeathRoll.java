package valorless.deathroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import valorless.valorlessutils.sound.SFX;
import valorless.valorlessutils.utils.Utils;

public class DeathRoll {

	public static void Roll(Player player, String value) {
		Random rand = new Random();
		// nextInt as provided by Random is exclusive of the top value so you need to add 1 
		Integer num = Integer.parseInt(value);
		num = Utils.Clamp(num, 0, Utils.Clamp(Main.config.GetInt("max-roll"), 0, Integer.MAX_VALUE));
		if(num <= 1) {
			player.sendMessage(Lang.Get("value-error"));
			return;
		}
		Integer min = 1;
		Integer max = num;
		Integer randomNum = rand.nextInt((max - min) + 1) + min;
		List<Entity> nbe = new ArrayList<Entity>();
		int range = Main.config.GetInt("range");
		nbe = player.getNearbyEntities(range, range, range);
		List<Entity> nearbyPlayers = new ArrayList<Entity>();
		if(Main.config.GetBool("global")) {
			for(Player pl : Bukkit.getOnlinePlayers()) {
				nearbyPlayers.add(pl);
			}
		}else {
			nearbyPlayers.add(player);
			for(Entity ent : nbe) {
				if(ent instanceof Player) {
					nearbyPlayers.add(ent);
				}
			}
		}
		
		Placeholders ph = new Placeholders();
		ph.player = player;
		ph.roll = randomNum;
		ph.rollOutOf = num;
		Lang.SetPlaceholders(ph);
		
		for(Entity p : nearbyPlayers) {
			if(p.getWorld() == player.getWorld()) {
			}
			p.sendMessage(Lang.Get("roll"));
			if(randomNum == 1) {
				p.sendMessage(Lang.Get("loser"));
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
