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

import java.util.Locale;

public final class EntityUtil {
    private EntityUtil() {
        // EMPTY
    }

    public static void despawnSafe(Entity entity) {
        if (entity != null && entity.isValid()) {
            try {
                entity.remove();
            } catch (Exception ignored) {
                // IGNORED
            }
        }
    }

    public static EntityType tryGetLivingEntityType(String entityType, EntityType defaultValue) {
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

    public static Entity getTntSource(Entity entity) {
        Entity source;
        do {
            source = entity instanceof TNTPrimed ? ((TNTPrimed) entity).getSource() : null;
            entity = source;
        } while (source instanceof TNTPrimed);
        return source;
    }
}
