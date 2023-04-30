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
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The {@link Feature} for {@link Entity}
 */
public abstract class EntityFeature implements Feature {
    private final Set<Entity> entities = ConcurrentHashMap.newKeySet();
    private final Queue<Entity> entityQueue = new ArrayDeque<>();
    private final Queue<SpawnRequest> spawnRequestQueue = new ConcurrentLinkedQueue<>();
    private final AtomicReference<Task> currentEntityTaskRef = new AtomicReference<>(null);
    private final AtomicReference<List<Predicate<Entity>>> entityClearCheckRef = new AtomicReference<>(Collections.emptyList());
    private final AtomicBoolean clearAllEntities = new AtomicBoolean(false);
    private final AtomicInteger spawnRequestPerTick = new AtomicInteger(1);

    /**
     * Create the entity at the location
     *
     * @param location the location
     * @return the entity or null if it cannot be created
     */
    @Nullable
    protected abstract Entity createEntity(Location location);

    /**
     * Request to spawn the entity
     *
     * @param location        the location
     * @param onSpawnConsumer the consumer when the entity is spawned
     * @return the request
     */
    public SpawnRequest spawn(Location location, Consumer<Entity> onSpawnConsumer) {
        if (location.getWorld() == null) {
            return new SpawnRequest(CompletableFuture.completedFuture(null), onSpawnConsumer, location);
        }
        CompletableFuture<Entity> completableFuture = new CompletableFuture<>();
        SpawnRequest spawnRequest = new SpawnRequest(completableFuture, onSpawnConsumer, location);
        if (!isTaskRunning()) {
            completableFuture.completeExceptionally(new IllegalStateException("The task is not running"));
        } else {
            spawnRequestQueue.add(spawnRequest);
        }
        return spawnRequest;
    }

    /**
     * Request to spawn the entity
     *
     * @param location the location
     * @return the request
     */
    public SpawnRequest spawn(Location location) {
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
     * Count the spawn requests
     *
     * @return the count
     */
    public long countSpawnRequests() {
        return spawnRequestQueue.size();
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
     * Check if the task is running
     *
     * @return true if it is running
     */
    public boolean isTaskRunning() {
        Task currentTask = currentEntityTaskRef.get();
        return currentTask != null && !currentTask.isCancelled();
    }

    /**
     * Start the task
     */
    public void startTask() {
        if (isTaskRunning()) {
            return;
        }

        Task task = Scheduler.providingPlugin(EntityFeature.class).async().runTaskTimer(() -> {
            if (clearAllEntities.get()) {
                while (true) {
                    SpawnRequest spawnRequest = spawnRequestQueue.poll();
                    if (spawnRequest == null) {
                        break;
                    }
                    spawnRequest.completableFuture.completeExceptionally(new IllegalStateException("The task is cleared"));
                }
            } else {
                int spawnAmount = Math.max(spawnRequestPerTick.get(), 1);
                for (int i = 0; i < spawnAmount; i++) {
                    SpawnRequest spawnRequest = spawnRequestQueue.poll();
                    if (spawnRequest == null) {
                        break;
                    }
                    Scheduler.providingPlugin(EntityFeature.class).sync().runLocationTask(spawnRequest.location, () -> {
                        Entity entity = createEntity(spawnRequest.location);
                        if (entity == null) {
                            spawnRequest.completableFuture.completeExceptionally(new IllegalStateException("Cannot create the entity"));
                        } else {
                            spawnRequest.onSpawnConsumer.accept(entity);
                            entities.add(entity);
                            entityQueue.add(entity);
                            spawnRequest.completableFuture.complete(entity);
                        }
                    });
                }
            }

            Entity entity = entityQueue.poll();
            if (entity == null || !entity.isValid()) {
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
    public void stopTask() {
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
     * Set how many entities to spawn per tick
     *
     * @param spawnRequestPerTick the amount
     */
    public void setSpawnRequestPerTick(int spawnRequestPerTick) {
        this.spawnRequestPerTick.set(spawnRequestPerTick);
    }

    /**
     * Check if all the entities are cleared
     *
     * @return true if all the entities are cleared
     */
    public boolean isAllEntityCleared() {
        return entityQueue.isEmpty() && spawnRequestQueue.isEmpty();
    }

    /**
     * Clear all the entities
     */
    public void clearAllEntities() {
        entities.forEach(EntityUtil::despawnSafe);
        entities.clear();
        entityQueue.clear();
        spawnRequestQueue.forEach(spawnRequest -> spawnRequest.completableFuture.completeExceptionally(new IllegalStateException("The task is cleared")));
        spawnRequestQueue.clear();
    }

    @Override
    public void clear() {
        stopTask();
        clearAllEntityClearChecks();
        clearAllEntities();
        clearAllEntities.set(false);
    }

    /**
     * The spawn request for the {@link Entity}
     */
    public static class SpawnRequest {
        /**
         * The completable future to complete the {@link Entity}
         */
        public final CompletableFuture<Entity> completableFuture;
        /**
         * The consumer to accept the {@link Entity} when it is spawned
         */
        public final Consumer<Entity> onSpawnConsumer;
        /**
         * The location to spawn the {@link Entity}
         */
        public final Location location;

        private SpawnRequest(CompletableFuture<Entity> completableFuture, Consumer<Entity> onSpawnConsumer, Location location) {
            this.completableFuture = completableFuture;
            this.onSpawnConsumer = onSpawnConsumer;
            this.location = location;
        }
    }
}
