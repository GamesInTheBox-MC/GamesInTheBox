package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RewardFeature implements Feature {
    private final Map<Integer, List<String>> topCommands;
    private final List<String> defaultCommands;

    public RewardFeature(Map<Integer, List<String>> topCommands, List<String> defaultCommands) {
        this.topCommands = topCommands;
        this.defaultCommands = defaultCommands;
    }

    public RewardFeature(Map<String, Object> value) {
        this(new HashMap<>(), new ArrayList<>());
        value.forEach((k, v) -> {
            if (k.equalsIgnoreCase("default") || k.equalsIgnoreCase("all") || k.equalsIgnoreCase("?")) {
                defaultCommands.addAll(CollectionUtils.createStringListFromObject(v, true));
            } else {
                int i;
                try {
                    i = Integer.parseInt(k);
                } catch (Exception e) {
                    return;
                }
                topCommands.put(i, CollectionUtils.createStringListFromObject(v, true));
            }
        });
    }

    public void reward(int topPosition, UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        String name = offlinePlayer.getName();
        if (name == null) return;
        List<String> commands = new ArrayList<>(topCommands.getOrDefault(topPosition, defaultCommands));
        commands.replaceAll(s -> s.replace("{name}", name).replace("{top}", Integer.toString(topPosition)));
        Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(getClass()), () -> commands.forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c)));
    }

    public void reward(Map<Integer, List<UUID>> topMap) {
        topMap.forEach((topPosition, uuidList) -> {
            for (UUID uuid : uuidList) {
                reward(topPosition, uuid);
            }
        });
    }

    public void reward(List<UUID> topList) {
        for (int i = 0; i < topList.size(); i++) {
            int top = i + 1;
            reward(top, topList.get(i));
        }
    }
}
