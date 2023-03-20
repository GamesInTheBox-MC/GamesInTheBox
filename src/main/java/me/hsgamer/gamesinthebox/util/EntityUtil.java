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
