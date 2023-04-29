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
package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.BoundingFeature;
import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.CurrentLocationAction;
import me.hsgamer.gamesinthebox.game.simple.action.LookingBlockLocationAction;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.block.BukkitBlockAdapter;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.hscore.minecraft.block.box.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The simple {@link BoundingFeature}.
 * It will get the bounding box from {@link GameConfigFeature} in the specified path.
 * The value of the path should be like this:
 * <pre>
 *     [path]:
 *       world: world
 *       pos1: 0,0,0
 *       pos2: 0,0,0
 * </pre>
 */
public class SimpleBoundingFeature extends BoundingFeature {
    private final SimpleGameArena arena;
    private final String path;

    /**
     * Create a new {@link SimpleBoundingFeature}
     *
     * @param arena the arena
     * @param path  the path
     */
    public SimpleBoundingFeature(@NotNull SimpleGameArena arena, @NotNull String path) {
        this.arena = arena;
        this.path = path;
    }

    /**
     * Create a new {@link SimpleBoundingFeature} that will get the bounding box from the config in the path "box"
     *
     * @param arena the arena
     */
    public SimpleBoundingFeature(@NotNull SimpleGameArena arena) {
        this(arena, "box");
    }

    /**
     * Create a new {@link SimpleBoundingFeature.Editor} to edit the bounding box
     *
     * @param lookingLocation whether the editor should take the location that the player is looking at. If {@code false}, it will take the location that the player is standing.
     * @param path            the path
     * @param editorName      the name of the editor
     * @param actionName      the name of the action
     * @return the editor
     */
    @NotNull
    public static Editor editor(boolean lookingLocation, @NotNull String path, @NotNull String editorName, @NotNull String actionName) {
        return new Editor(lookingLocation, path, editorName, actionName);
    }

    /**
     * Create a new {@link SimpleBoundingFeature.Editor} to edit the bounding box in the path "box"
     *
     * @param lookingLocation whether the editor should take the location that the player is looking at. If {@code false}, it will take the location that the player is standing.
     * @return the editor
     */
    @NotNull
    public static Editor editor(boolean lookingLocation) {
        return new Editor(lookingLocation, "box", "Bounding Box", "");
    }

    @Override
    protected @NotNull Pair<World, BlockBox> createWorldBox() {
        GameConfigFeature configFeature = arena.getFeature(GameConfigFeature.class);
        String worldName = configFeature.getString(path + ".world", "");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException(arena.getName() + " has invalid world");
        }
        Location pos1 = LocationUtil.getLocation(world, configFeature.getString(path + ".pos1", ""));
        if (pos1 == null) {
            throw new IllegalStateException(arena.getName() + " has invalid position 1");
        }
        Location pos2 = LocationUtil.getLocation(world, configFeature.getString(path + ".pos2", ""));
        if (pos2 == null) {
            throw new IllegalStateException(arena.getName() + " has invalid position 2");
        }
        return Pair.of(world, new BlockBox(BukkitBlockAdapter.adapt(pos1), BukkitBlockAdapter.adapt(pos2)));
    }

    /**
     * The editor for {@link SimpleBoundingFeature}
     */
    public static class Editor {
        private final boolean lookingLocation;
        private final String path;
        private final String editorName;
        private final String actionName;
        private Location pos1;
        private Location pos2;

        private Editor(boolean lookingLocation, String path, String editorName, String actionName) {
            this.lookingLocation = lookingLocation;
            this.path = path;
            this.editorName = editorName;
            this.actionName = actionName;
        }

        /**
         * Get the status of the editor to be added to {@link SimpleGameEditor}
         *
         * @return the status
         */
        public SimpleGameEditor.SimpleEditorStatus getStatus() {
            return new SimpleGameEditor.SimpleEditorStatus() {
                @Override
                public void sendStatus(@NotNull CommandSender sender) {
                    MessageUtils.sendMessage(sender, "&6&l" + editorName.toUpperCase(Locale.ROOT));
                    MessageUtils.sendMessage(sender, "&6Pos 1: &f" + (pos1 == null ? "Not set" : LocationUtil.serializeLocation(pos1, true, true)));
                    MessageUtils.sendMessage(sender, "&6Pos 2: &f" + (pos2 == null ? "Not set" : LocationUtil.serializeLocation(pos2, true, true)));
                    if (pos1 != null && pos2 != null && pos1.getWorld() != pos2.getWorld()) {
                        MessageUtils.sendMessage(sender, "&cThe world of pos 1 and pos 2 are different");
                    }
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    pos1 = null;
                    pos2 = null;
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    if (pos1 == null || pos2 == null) return false;
                    World world1 = pos1.getWorld();
                    World world2 = pos2.getWorld();
                    return world1 != null && world1.equals(world2);
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    World world = pos1.getWorld();
                    assert world != null;
                    map.put(path + ".world", world.getName());
                    map.put(path + ".pos1", LocationUtil.serializeLocation(pos1, false, true));
                    map.put(path + ".pos2", LocationUtil.serializeLocation(pos2, false, true));
                    return map;
                }
            };
        }

        /**
         * Get the actions of the editor to be added to {@link SimpleGameEditor}
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();
            String pos1Name = actionName.isEmpty() ? "set-pos1" : actionName + "-set-pos1";
            String pos2Name = actionName.isEmpty() ? "set-pos2" : actionName + "-set-pos2";

            if (lookingLocation) {
                map.put(pos1Name, new LookingBlockLocationAction() {
                    @Override
                    public @NotNull String getDescription() {
                        return "Set the position 1 of the " + editorName + " at the location you are looking at";
                    }

                    @Override
                    protected boolean performAction(@NotNull CommandSender sender, @NotNull Location location, String... args) {
                        pos1 = location;
                        return true;
                    }
                });
                map.put(pos2Name, new LookingBlockLocationAction() {
                    @Override
                    public @NotNull String getDescription() {
                        return "Set the position 2 of the " + editorName + " at the location you are looking at";
                    }

                    @Override
                    protected boolean performAction(@NotNull CommandSender sender, @NotNull Location location, String... args) {
                        pos2 = location;
                        return true;
                    }
                });
            } else {
                map.put(pos1Name, new CurrentLocationAction() {
                    @Override
                    public @NotNull String getDescription() {
                        return "Set the position 1 of the " + editorName + " at your current location";
                    }

                    @Override
                    protected boolean performAction(@NotNull CommandSender sender, @NotNull Location location, String... args) {
                        pos1 = location;
                        return true;
                    }
                });
                map.put(pos2Name, new CurrentLocationAction() {
                    @Override
                    public @NotNull String getDescription() {
                        return "Set the position 2 of the " + editorName + " at your current location";
                    }

                    @Override
                    protected boolean performAction(@NotNull CommandSender sender, @NotNull Location location, String... args) {
                        pos2 = location;
                        return true;
                    }
                });
            }

            return map;
        }

        /**
         * Migrate the editor from a {@link BoundingFeature}
         *
         * @param feature the feature
         */
        public void migrate(BoundingFeature feature) {
            World world = feature.getWorld();
            BlockBox box = feature.getBlockBox();
            pos1 = BukkitBlockAdapter.adapt(world, new Position(box.minX, box.minY, box.minZ));
            pos2 = BukkitBlockAdapter.adapt(world, new Position(box.maxX, box.maxY, box.maxZ));
        }
    }
}
