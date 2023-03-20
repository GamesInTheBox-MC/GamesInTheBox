/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PointFeature implements Feature {
    private final Map<UUID, Integer> points = new IdentityHashMap<>();
    private final AtomicReference<List<Pair<UUID, Integer>>> topSnapshot = new AtomicReference<>(Collections.emptyList());
    private final PointConsumer pointConsumer;

    public PointFeature(PointConsumer pointConsumer) {
        this.pointConsumer = pointConsumer;
    }

    public PointFeature() {
        this((uuid, point, totalPoint) -> {
        });
    }

    public void takeTopSnapshot() {
        List<Pair<UUID, Integer>> updatedTopSnapshot = getTop();
        topSnapshot.lazySet(updatedTopSnapshot);
    }

    public List<Pair<UUID, Integer>> getTopSnapshot() {
        return topSnapshot.get();
    }

    public List<Pair<UUID, String>> getTopSnapshotAsStringPair() {
        return getTopSnapshot()
                .stream()
                .map(point -> Pair.of(point.getKey(), Integer.toString(point.getValue())))
                .collect(Collectors.toList());
    }

    public void applyPoint(UUID uuid, int point) {
        if (point > 0) {
            points.merge(uuid, point, Integer::sum);
            pointConsumer.accept(uuid, point, getPoint(uuid));
        } else if (point < 0) {
            int currentPoint = getPoint(uuid);
            if (currentPoint > 0) {
                points.put(uuid, Math.max(0, currentPoint + point));
                pointConsumer.accept(uuid, Math.max(point, -currentPoint), getPoint(uuid));
            }
        } else {
            pointConsumer.accept(uuid, 0, getPoint(uuid));
        }
    }

    public int getPoint(UUID uuid) {
        return points.getOrDefault(uuid, 0);
    }

    public List<Pair<UUID, Integer>> getTop() {
        List<Pair<UUID, Integer>> list;
        if (points.isEmpty()) {
            list = Collections.emptyList();
        } else {
            list = new ArrayList<>();
            points.forEach((uuid, point) -> {
                if (point > 0) {
                    list.add(Pair.of(uuid, point));
                }
            });
            list.sort(Comparator.<Pair<UUID, Integer>>comparingInt(Pair::getValue).reversed());
        }
        return list;
    }

    public List<UUID> getTopUUID() {
        return getTop().stream().map(Pair::getKey).collect(Collectors.toList());
    }

    public void resetPointIfNotOnline() {
        points.replaceAll((uuid, point) -> Bukkit.getPlayer(uuid) == null ? 0 : point);
    }

    public void clearPoints() {
        points.clear();
    }

    public interface PointConsumer {
        void accept(UUID uuid, int point, int totalPoint);
    }
}
