/*
 * This file is part of JoinCommand.
 *
 * Copyright (c) 2014 AmazingKohle (http://kohle.co)
 * JoinCommand is licensed under the Mozilla Public License 2.0.
 *
 * JoinCommand is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License as published by
 * the Mozilla project.
 *
 * JoinCommand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY. See the license for more details.
 *
 * You should have received a copy of the Mozilla Public License 2.0
 * along with this program.  If not, see <https://www.mozilla.org/MPL/2.0/>.
 */

package co.kohle.JoinCommand.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.kohle.JoinCommand.JoinCommand;

public class Join implements Listener {
	
	public JoinCommand plugin;
	
	public Join(JoinCommand instance) {
		plugin = instance;
	}
	
	public void doCommands(Player player, String commandType) {
		List<?> list = plugin.getConfig().getList(commandType);
		if(!(list == null)) {
			for(Object l : list) {
				if(commandType == "every.console" | commandType == "first.console") {
					String commands = (String) l.toString().replaceAll("%player%", player.getName());
					plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), commands);
				} else if(commandType == "every.player" | commandType == "first.player") {
					player.performCommand((String) l);
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if((!(player.hasPlayedBefore())) && player.hasPermission("joincommand.first")) {
			player.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + "has joined for the first time!");
			
			String listType = "first.player";
			doCommands(player, listType);
			
			listType = "first.console";
			doCommands(player, listType);
		} else if(player.hasPlayedBefore() && player.hasPermission("joincommand.every")) {
			String listType = "every.player";
			doCommands(player, listType);
			
			listType = "every.console";
			doCommands(player, listType);
			player.getServer().broadcastMessage("test");
			plugin.getLogger().info("test");
		}
	}

}
