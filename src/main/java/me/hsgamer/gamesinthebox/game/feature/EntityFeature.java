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
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.scheduler.Task;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * The {@link Feature} for {@link Entity}
 */
public abstract class EntityFeature implements Feature {
    private final Set<Entity> entities = ConcurrentHashMap.newKeySet();
    private final Queue<Entity> entityQueue = new ConcurrentLinkedQueue<>();
    private final AtomicReference<Task> currentEntityTaskRef = new AtomicReference<>(null);
    private final AtomicReference<List<Predicate<Entity>>> entityClearCheckRef = new AtomicReference<>(Collections.emptyList());
    private final AtomicBoolean clearAllEntities = new AtomicBoolean(false);

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
        if (!location.getChunk().isLoaded()) {
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<Entity> completableFuture = new CompletableFuture<>();
        Scheduler.providingPlugin(EntityFeature.class).sync().runLocationTask(location, () -> {
            Entity entity;
            try {
                entity = createEntity(location);
            } catch (Throwable throwable) {
                JavaPlugin.getProvidingPlugin(EntityFeature.class).getLogger().log(Level.WARNING, "There is an error when creating the entity", throwable);
                completableFuture.completeExceptionally(throwable);
                return;
            }
            if (entity == null) {
                completableFuture.completeExceptionally(new NullPointerException("Entity is null"));
                return;
            }
            entities.add(entity);
            entityQueue.add(entity);
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
    public Collection<Entity> getEntities() {
        return Collections.unmodifiableSet(entities);
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
     * Count the {@link Entity#isValid()} entities
     *
     * @return the count
     */
    public long countValid() {
        return streamValid().count();
    }

    /**
     * Add a predicate to clear the entity if it returns true
     *
     * @param predicate the predicate
     */
    public void addEntityClearCheck(Predicate<Entity> predicate) {
        List<Predicate<Entity>> list = new ArrayList<>(entityClearCheckRef.get());
        list.add(predicate);
        entityClearCheckRef.set(list);
    }

    /**
     * Clear all the predicates to clear the entity
     */
    public void clearAllEntityClearChecks() {
        entityClearCheckRef.set(Collections.emptyList());
    }

    /**
     * Start the task to clear the entities
     */
    public void startClearEntities() {
        Task currentTask = currentEntityTaskRef.get();

        if (currentEntityTaskRef.get() != null && !currentTask.isCancelled()) {
            return;
        }

        Task task = Scheduler.providingPlugin(EntityFeature.class).async().runTaskTimer(() -> {
            Entity entity = entityQueue.poll();
            if (entity == null) {
                return;
            }

            if (!entity.isValid()) {
                entities.remove(entity);
                return;
            }

            boolean toRemove;
            if (clearAllEntities.get()) {
                toRemove = true;
            } else {
                toRemove = false;
                List<Predicate<Entity>> list = entityClearCheckRef.get();
                if (list != null && !list.isEmpty()) {
                    for (Predicate<Entity> predicate : list) {
                        if (predicate.test(entity)) {
                            toRemove = true;
                            break;
                        }
                    }
                }
            }

            if (toRemove) {
                entities.remove(entity);
                Scheduler.providingPlugin(EntityFeature.class).sync().runEntityTask(entity, () -> EntityUtil.despawnSafe(entity), () -> {
                });
            } else {
                entityQueue.add(entity);
            }
        }, 0L, 0L);

        currentEntityTaskRef.set(task);
    }

    /**
     * Stop the task to clear the entities
     */
    public void stopClearEntities() {
        Task currentTask = currentEntityTaskRef.getAndSet(null);
        if (currentTask != null && !currentTask.isCancelled()) {
            currentTask.cancel();
        }
    }

    /**
     * Set the value to clear all the entities when the task is running
     *
     * @param clearAllEntities true to clear all the entities
     */
    public void setClearAllEntities(boolean clearAllEntities) {
        this.clearAllEntities.set(clearAllEntities);
    }

    /**
     * Check if all the entities are cleared
     *
     * @return true if all the entities are cleared
     */
    public boolean isAllEntityCleared() {
        return entityQueue.isEmpty();
    }

    /**
     * Clear all the entities
     */
    public void clearAllEntities() {
        entities.forEach(EntityUtil::despawnSafe);
        entities.clear();
        entityQueue.clear();
    }

    @Override
    public void clear() {
        stopClearEntities();
        clearAllEntityClearChecks();
        clearAllEntities();
        clearAllEntities.set(false);
    }
}
