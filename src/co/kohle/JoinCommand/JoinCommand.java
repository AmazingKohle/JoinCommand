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

package co.kohle.JoinCommand;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import co.kohle.JoinCommand.events.Join;
import co.kohle.JoinCommand.metrics.Metrics;

public class JoinCommand extends JavaPlugin implements Listener {
	
	String[] defaultCommands = {"version", "plugins"};
	
	public void onEnable() {
		getLogger().info("Enabled!");
		loadConfiguration();
		getServer().getPluginManager().registerEvents(new Join(this), this);
		
		if(this.getConfig().getInt("players.unique.count") == 0) {
			File f = new File(this.getConfig().getString("players.unique.world"));
            int count = 0;
            for (File file : f.listFiles()) {
			    if (file.isFile()) {
			            count++;
			    }
			    
			this.getConfig().set("players.unique.count", count);
			this.saveConfig();
			}
		}
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		    getLogger().info("Reporting to Metrics...");
		} catch (IOException e) {
		    getLogger().log(Level.WARNING, "Could not submit metrics!");
		}
	}
	
	public void onDisable() {
		getLogger().info("Disabled!");
	}
	
	public void loadConfiguration() {
		final FileConfiguration config = this.getConfig();
		config.addDefault("every.player", Arrays.asList(defaultCommands));
		config.addDefault("every.console", Arrays.asList(defaultCommands));
		config.addDefault("first.player", Arrays.asList(defaultCommands));
		config.addDefault("first.console", Arrays.asList(defaultCommands));
		config.addDefault("messages.first.enabled", true);
		config.addDefault("messages.first.message", "%player% has joined for the first time!");
		config.addDefault("messages.every.enabled", false);
		config.addDefault("messages.every.message", "Welcome back, %player%!");
		config.addDefault("messages.unique.enabled", true);
		config.addDefault("messages.unique.message", "%count% unique players have joined!");
		config.addDefault("players.unique.enabled", true);
		config.addDefault("players.unique.count", 0);
		config.addDefault("players.unique.world", "C:/Server/world/players");
		config.options().copyDefaults(true);
		this.saveConfig();
	}
	
}