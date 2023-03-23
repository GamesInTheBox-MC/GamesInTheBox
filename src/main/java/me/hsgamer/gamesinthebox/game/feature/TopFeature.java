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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * The {@link Feature} that handles the top / leaderboard
 */
public class TopFeature implements Feature {
    private final AtomicReference<List<Pair<UUID, String>>> top = new AtomicReference<>(Collections.emptyList());
    private final AtomicReference<List<UUID>> topUUIDs = new AtomicReference<>(Collections.emptyList());

    /**
     * Get the current top
     *
     * @return the current top
     */
    public List<Pair<UUID, String>> getTop() {
        return top.get();
    }

    /**
     * Set the top
     *
     * @param top the top
     */
    public void setTop(List<Pair<UUID, String>> top) {
        this.top.lazySet(top);
        this.topUUIDs.lazySet(top.stream().map(Pair::getKey).collect(Collectors.toList()));
    }

    /**
     * Get the index of the UUID in the top
     *
     * @param uuid the UUID
     * @return the index or -1 if not found
     */
    public int getTopIndex(UUID uuid) {
        return topUUIDs.get().indexOf(uuid);
    }

    /**
     * Get the pair of the UUID and the value in the top
     *
     * @param index the index
     * @return the pair or empty if out of range
     */
    public Optional<Pair<UUID, String>> getTop(int index) {
        List<Pair<UUID, String>> getTopSnapshot = getTop();
        if (index < 0 || index >= getTopSnapshot.size()) {
            return Optional.empty();
        } else {
            return Optional.of(getTopSnapshot.get(index));
        }
    }
}
