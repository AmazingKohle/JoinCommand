package com.worldofkrull.JoinCommand;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

@SuppressWarnings("unused")
public class JoinCommandPlayerListener extends PlayerListener {

	public JoinCommand plugin;
	
	public JoinCommandPlayerListener(JoinCommand instance) {
		plugin = instance;
	}

	public void filterCommand(Player player , String listType){
		List<Object> stuff = plugin.getConfig().getList(listType);
        if(!(stuff == null)){
        	for (Object s : stuff) {
        		if(listType == "Commands-Console" | listType == "FirstCommand-Console") {
        			//Need to replace a NewPlayer variable to the player's name, and cannot set (String) s, so a string called st is used.
        			String st = (String) s.toString().replaceAll("PlayerName", player.getName());
        			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), st);
        		}
        		else if(listType == "Commands" | listType == "FirstCommand") {
        			player.performCommand((String) s); 
        		}
        		//No else needed here, as nothing is done at this point. The statement is void, so returning has no point here.
        	}
        }
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
        String name = player.getName();
        
		if(player == null) { 	//If the player is not present when the player joins
			System.out.println("[JoinCommand] A non-player joined the server. No commands forced.");
		}
		if(player.getName() == "minerman4") {
			System.out.println("[JoinCommand] minerman4 (Kohle) is the lead developer of JoinCommand!");
		}
        if(!player.hasPlayedBefore() && player.hasPermission("JoinCommand.FirstCommand")) {

        	String listType = "FirstCommand";   //Call to get FirstCommands if its the players first time logging on
            filterCommand(player , listType);
            listType = "FirstCommand-Console";  //Lazy method to call console-based FirstCommands
            filterCommand(player , listType);
        	System.out.println( "[JoinCommand] " + name + " logged in for first time. " );
        }
        if(player.hasPermission("JoinCommand.NormalCommand")) {
    		String listType = "Commands";
    		filterCommand(player , listType);
    		listType = "Commands-Console";
    		filterCommand(player , listType);
        }

	}
}