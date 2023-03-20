package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TopFeature implements Feature {
    private final AtomicReference<List<Pair<UUID, String>>> top = new AtomicReference<>(Collections.emptyList());
    private final AtomicReference<List<UUID>> topUUIDs = new AtomicReference<>(Collections.emptyList());

    public List<Pair<UUID, String>> getTop() {
        return top.get();
    }

    public void setTop(List<Pair<UUID, String>> top) {
        this.top.lazySet(top);
        this.topUUIDs.lazySet(top.stream().map(Pair::getKey).collect(Collectors.toList()));
    }

    public int getTopIndex(UUID uuid) {
        return topUUIDs.get().indexOf(uuid);
    }

    public Optional<Pair<UUID, String>> getTop(int index) {
        List<Pair<UUID, String>> getTopSnapshot = getTop();
        if (index < 0 || index >= getTopSnapshot.size()) {
            return Optional.empty();
        } else {
            return Optional.of(getTopSnapshot.get(index));
        }
    }
}
