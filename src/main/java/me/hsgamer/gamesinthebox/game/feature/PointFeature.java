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
import java.util.stream.Collectors;

/**
 * The {@link Feature} that handles points of the players
 */
public class PointFeature implements Feature {
    private final Map<UUID, Integer> points = new IdentityHashMap<>();
    private final PointConsumer pointConsumer;

    /**
     * Create a new instance
     *
     * @param pointConsumer the consumer when the point of a player is changed
     */
    public PointFeature(PointConsumer pointConsumer) {
        this.pointConsumer = pointConsumer;
    }

    /**
     * Create a new instance
     */
    public PointFeature() {
        this((uuid, point, totalPoint) -> {
        });
    }

    /**
     * Apply the point to the player.
     * If the point is positive, it will be added to the player's point.
     * If the point is negative, it will be subtracted from the player's point.
     * If the point is zero, it will do nothing.
     *
     * @param uuid  the uuid of the player
     * @param point the point to apply
     */
    public void applyPoint(UUID uuid, int point) {
        if (point > 0) {
            points.merge(uuid, point, Integer::sum);
            pointConsumer.onChanged(uuid, point, getPoint(uuid));
        } else if (point < 0) {
            int currentPoint = getPoint(uuid);
            if (currentPoint > 0) {
                points.put(uuid, Math.max(0, currentPoint + point));
                pointConsumer.onChanged(uuid, Math.max(point, -currentPoint), getPoint(uuid));
            }
        } else {
            pointConsumer.onChanged(uuid, 0, getPoint(uuid));
        }
    }

    /**
     * Get the point of the player
     *
     * @param uuid the uuid of the player
     * @return the point of the player
     */
    public int getPoint(UUID uuid) {
        return points.getOrDefault(uuid, 0);
    }

    /**
     * Get the top, sorted by the point
     *
     * @return the top
     */
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

    /**
     * Get the uuid part of the top
     *
     * @return the uuid part of the top
     */
    public List<UUID> getTopUUID() {
        return getTop().stream().map(Pair::getKey).collect(Collectors.toList());
    }

    /**
     * Get the top as string pair
     *
     * @return the top as string pair
     */
    public List<Pair<UUID, String>> getTopAsStringPair() {
        return getTop()
                .stream()
                .map(point -> Pair.of(point.getKey(), Integer.toString(point.getValue())))
                .collect(Collectors.toList());
    }

    /**
     * Reset the point of the player if he/she is offline
     */
    public void resetPointIfNotOnline() {
        points.replaceAll((uuid, point) -> Bukkit.getPlayer(uuid) == null ? 0 : point);
    }

    /**
     * Clear all points
     */
    public void clearPoints() {
        points.clear();
    }

    /**
     * The consumer when the point of a player is changed
     */
    public interface PointConsumer {
        /**
         * Called when the point of a player is changed
         *
         * @param uuid       the uuid of the player
         * @param point      the point
         * @param totalPoint the total point
         */
        void onChanged(UUID uuid, int point, int totalPoint);
    }
}
