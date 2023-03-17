package me.hsgamer.gamesinthebox.planner;

import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

public class Planner extends SimpleBukkitArena {
    public Planner(String name, PlannerManager arenaManager) {
        super(name, arenaManager);
    }
}
