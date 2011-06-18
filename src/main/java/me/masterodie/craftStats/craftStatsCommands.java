package me.masterodie.craftStats;


import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class craftStatsCommands implements CommandExecutor {
	
	public craftStats plugin;
	
	public craftStatsCommands(craftStats plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		int itemId = ((Player) sender).getItemInHand().getTypeId();
		Material mat = Material.getMaterial(itemId);
		short durability = mat.getMaxDurability();
		durability -= 2;
		
		if(label.equalsIgnoreCase("dmg")) {
	    	if(sender instanceof Player) {   
	    		
	    		((Player) sender).getItemInHand().setDurability(durability) ;
	    	}
	    	
	    	return true;
	    }
		return false;
	}
}
