package me.masterodie.craftStats;


//import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
//import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent; 

public class craftStatsEntityListener extends EntityListener {
	
	private static  craftStats plugin;
	private craftStatsSqlHandler sql;
	
	public craftStatsEntityListener(craftStats instance) {
		plugin = instance;
	}

	public void onEntityDamage(EntityDamageEvent event) {

		Entity damaged = event.getEntity();
		Entity damager = event.getEntity().getLastDamageCause().getEntity();

		sql = new craftStatsSqlHandler(plugin);
		
		if(damaged instanceof Player) {
			Player p = (Player)damaged;
		
			sql.mySqlInsertQuery(p.getName(), event.getDamage(), "damage_taken", "player");
			sql.mySqlInsertQuery(p.getName(), p.getHealth(), "health", "player");
		}
		
		if(damaged instanceof Entity && !(damaged instanceof Player) && damager instanceof Player)
		{
			Player p = (Player)damager;
			
			sql.mySqlInsertQuery(p.getName(), event.getDamage(), "damage_dealt", "player");

			short damage = p.getItemInHand().getDurability();

			Material mat = Material.getMaterial(p.getItemInHand().getTypeId());
			short maxDurability = mat.getMaxDurability();
			
			boolean insert = true;
			
			if (damage < maxDurability)
				insert = false;
			if (p.getItemInHand().getTypeId() < 256)
				insert =  false;
			
			if(insert) {
				plugin.log.info("IteminHand: " + damage);
				sql = new craftStatsSqlHandler(plugin);
				sql.mySqlInsertQuery(p.getName(), 1, "destroyed_" + p.getItemInHand().getTypeId(), "item");
			}
		}
	}
	
//	public void onEntityDeath(EntityDeathEvent event) {
//		Entity damaged = event.getEntity();
//		Entity damager = event.getEntity().getLastDamageCause().getEntity();
//		
//		if(damager instanceof Player) {
//			UUID entityId = damaged.getUniqueId();
//			String playerName = ((Player)damager).getName();
//		
//			plugin.log.info("Damager = " + damager);
//			plugin.log.info("Damaged = " + damaged);
//			
//			plugin.log.info(playerName + " killed Entity with UUID" + entityId);
//		}
//	}
}

