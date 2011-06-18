package me.masterodie.craftStats;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
//import org.bukkit.Material;

import java.util.logging.Logger;

public class craftStatsBlockListener extends BlockListener {

	public static craftStats plugin;
	private craftStatsSqlHandler sql;
	
	public craftStatsBlockListener(craftStats instance) {
		plugin = instance;
	}
	
	public craftStats stats;
	
	Logger log = Logger.getLogger("Minecraft");
		
	public void onBlockPlace(BlockPlaceEvent event) {
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		int matId = block.getTypeId();
		String row = "placed_" + matId;
		
		//einfügen in MySQL
		
		sql = new craftStatsSqlHandler(plugin);
		sql.mySqlInsertQuery(player.getName(), 1, row, "block");
	} 	

	
	public void onBlockBreak(BlockBreakEvent event) {
		
		Player p = event.getPlayer();
		Block block = event.getBlock();
		int matId = block.getTypeId();
		String row = "removed_" + matId;
		
		sql = new craftStatsSqlHandler(plugin);
		sql.mySqlInsertQuery(p.getName(), 1, row, "block");
		
		short damage = p.getItemInHand().getDurability();

		Material mat = Material.getMaterial(p.getItemInHand().getTypeId());
		short maxDurability = mat.getMaxDurability();
		
		boolean insert = false;
		
		if (damage >= maxDurability || damage < 0)
			insert = true;
		if (p.getItemInHand().getTypeId() < 256)
			insert =  false;
		
		if(insert) {
			plugin.log.info("IteminHand: " + damage);
			sql = new craftStatsSqlHandler(plugin);
			sql.mySqlInsertQuery(p.getName(), 1, "destroyed_" + p.getItemInHand().getTypeId(), "item");
		}

	}
	
}
