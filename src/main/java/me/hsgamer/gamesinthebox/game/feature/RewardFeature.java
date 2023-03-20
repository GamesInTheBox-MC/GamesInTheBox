package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Supplier;

public class RewardFeature implements Feature {
    private final Supplier<Pair<Map<Integer, List<String>>, List<String>>> topCommandsSupplier;
    private Map<Integer, List<String>> topCommands = Collections.emptyMap();
    private List<String> defaultCommands = Collections.emptyList();

    public RewardFeature(Supplier<Pair<Map<Integer, List<String>>, List<String>>> topCommandsSupplier) {
        this.topCommandsSupplier = topCommandsSupplier;
    }

    public RewardFeature(Map<Integer, List<String>> topCommands, List<String> defaultCommands) {
        this(() -> Pair.of(topCommands, defaultCommands));
    }

    @Override
    public void postInit() {
        Pair<Map<Integer, List<String>>, List<String>> pair = topCommandsSupplier.get();
        topCommands = pair.getKey();
        defaultCommands = pair.getValue();
    }

    @Override
    public void clear() {
        topCommands = Collections.emptyMap();
        defaultCommands = Collections.emptyList();
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
