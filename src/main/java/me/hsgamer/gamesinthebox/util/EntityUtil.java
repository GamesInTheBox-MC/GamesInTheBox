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
package me.hsgamer.gamesinthebox.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * The utility class for Entity
 */
public final class EntityUtil {
    private EntityUtil() {
        // EMPTY
    }

    /**
     * Despawn an entity safely
     *
     * @param entity the entity
     */
    public static void despawnSafe(@Nullable Entity entity) {
        if (entity != null && entity.isValid()) {
            try {
                entity.remove();
            } catch (Exception ignored) {
                // IGNORED
            }
        }
    }

    /**
     * Get the living entity type from the string
     *
     * @param entityType   the string
     * @param defaultValue the default value
     * @return the entity type
     */
    @NotNull
    public static EntityType tryGetLivingEntityType(@NotNull String entityType, @NotNull EntityType defaultValue) {
        try {
            EntityType type = EntityType.valueOf(entityType.toUpperCase(Locale.ROOT));
            Class<? extends Entity> clazz = type.getEntityClass();
            if (clazz != null && LivingEntity.class.isAssignableFrom(clazz)) {
                return type;
            }
        } catch (Exception ignored) {
            // IGNORED
        }
        return defaultValue;
    }

    /**
     * Get the root source of TNT
     *
     * @param entity the entity
     * @return the source
     */
    @Nullable
    public static Entity getTntSource(@NotNull Entity entity) {
        Entity source;
        do {
            source = entity instanceof TNTPrimed ? ((TNTPrimed) entity).getSource() : null;
            entity = source;
        } while (source instanceof TNTPrimed);
        return source;
    }
}
