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

import me.hsgamer.gamesinthebox.util.EntityUtil;
import me.hsgamer.gamesinthebox.util.Util;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The {@link Feature} for {@link Entity}
 */
public abstract class EntityFeature implements Feature {
    private final List<Entity> entities = new ArrayList<>();

    /**
     * Create the entity at the location
     *
     * @param location the location
     * @return the entity or null if it cannot be created
     */
    @Nullable
    protected abstract Entity createEntity(Location location);

    /**
     * Spawn the entity
     *
     * @param location        the location
     * @param onSpawnConsumer the consumer when the entity is spawned
     * @return the completable future of the entity, can be completed exceptionally if the entity cannot be created
     */
    public CompletableFuture<Entity> spawn(Location location, Consumer<Entity> onSpawnConsumer) {
        CompletableFuture<Entity> completableFuture = new CompletableFuture<>();
        Util.runSync(() -> {
            Entity entity = createEntity(location);
            if (entity == null) {
                completableFuture.completeExceptionally(new NullPointerException("Entity is null"));
                return;
            }
            entities.add(entity);
            onSpawnConsumer.accept(entity);
            completableFuture.complete(entity);
        });
        return completableFuture;
    }

    /**
     * Spawn the entity
     *
     * @param location the location
     * @return the completable future of the entity, can be completed exceptionally if the entity cannot be created
     */
    public CompletableFuture<Entity> spawn(Location location) {
        return spawn(location, entity -> {
        });
    }

    /**
     * Check if the entity is in the list
     *
     * @param entity the entity
     * @return true if it is in the list
     */
    public boolean contains(Entity entity) {
        return entities.contains(entity);
    }

    /**
     * Get the list of entities
     *
     * @return the list of entities
     */
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    /**
     * Count the {@link Entity#isValid()} entities
     *
     * @return the count
     */
    public long countValid() {
        return entities.stream().filter(Entity::isValid).count();
    }

    /**
     * Get the stream of {@link Entity#isValid()} entities
     *
     * @return the stream
     */
    public Stream<Entity> streamValid() {
        return entities.stream().filter(Entity::isValid);
    }

    /**
     * Clear the entities
     *
     * @return the completable future of the task
     */
    public CompletableFuture<Void> clearEntities() {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Util.runSync(() -> {
            entities.forEach(EntityUtil::despawnSafe);
            entities.clear();
            completableFuture.complete(null);
        });
        return completableFuture;
    }

    @Override
    public void clear() {
        clearEntities().join();
    }
}
