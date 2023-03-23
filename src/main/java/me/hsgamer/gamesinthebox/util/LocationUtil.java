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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The utility for {@link Location}
 */
public final class LocationUtil {
    private LocationUtil() {
        // EMPTY
    }

    /**
     * Get the location from the string.
     * The format is {@code x,y,z}
     *
     * @param world the world
     * @param value the string
     * @return the location or null if the string is invalid
     */
    public static Location getLocation(World world, String value) {
        String[] split = value.split(Pattern.quote(","), 3);
        if (split.length < 3) {
            return null;
        }
        try {
            double x = Double.parseDouble(split[0].trim());
            double y = Double.parseDouble(split[1].trim());
            double z = Double.parseDouble(split[2].trim());
            return new Location(world, x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the location from the string.
     * The format is {@code world,x,y,z}
     *
     * @param value the string
     * @return the location or null if the world is not found or the string is invalid
     */
    public static Location getLocation(String value) {
        String[] split = value.split(",", 4);
        if (split.length != 4) {
            return null;
        }

        World world = Bukkit.getWorld(split[0].trim());
        if (world == null) {
            return null;
        }
        try {
            double x = Double.parseDouble(split[1].trim());
            double y = Double.parseDouble(split[2].trim());
            double z = Double.parseDouble(split[3].trim());
            return new Location(world, x, y, z);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * Serialize the location
     *
     * @param location     the location
     * @param withWorld    whether to include the world
     * @param roundNumbers whether to round the numbers
     * @return the serialized location
     */
    public static String serializeLocation(Location location, boolean withWorld, boolean roundNumbers) {
        String world = Optional.ofNullable(location.getWorld()).map(World::getName).orElse("world");
        double x = roundNumbers ? location.getBlockX() : location.getX();
        double y = roundNumbers ? location.getBlockY() : location.getY();
        double z = roundNumbers ? location.getBlockZ() : location.getZ();
        String value = String.format("%s,%s,%s", x, y, z);
        if (withWorld) {
            value = world + "," + value;
        }
        return value;
    }
}
