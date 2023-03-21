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

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.GameVariableFeature;
import me.hsgamer.gamesinthebox.game.feature.HologramFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.unihologram.common.api.Hologram;
import me.hsgamer.unihologram.common.api.HologramLine;
import me.hsgamer.unihologram.common.line.TextHologramLine;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DescriptiveHologramFeature implements Feature {
    private final SimpleGameArena arena;
    private final Function<String, List<String>> defaultLinesFunction;
    private final List<HologramUpdater> hologramUpdaters = new ArrayList<>();

    public DescriptiveHologramFeature(SimpleGameArena arena, Function<String, List<String>> defaultLinesFunction) {
        this.arena = arena;
        this.defaultLinesFunction = defaultLinesFunction;
    }

    @Override
    public void postInit() {
        GameConfigFeature gameConfigFeature = arena.getFeature(GameConfigFeature.class);
        Object hologramSection = gameConfigFeature.get("hologram");
        List<Map<String, Object>> hologramList = new ArrayList<>();
        if (hologramSection instanceof List) {
            for (Object hologramObject : (List<?>) hologramSection) {
                MapUtils.castOptionalStringObjectMap(hologramObject).ifPresent(hologramList::add);
            }
        } else {
            MapUtils.castOptionalStringObjectMap(hologramSection).ifPresent(hologramList::add);
        }

        for (Map<String, Object> hologramMap : hologramList) {
            Location location = LocationUtil.getLocation(Objects.toString(hologramMap.get("location")));
            List<String> lines = CollectionUtils.createStringListFromObject(hologramMap.get("lines"));
            if (location == null || lines.isEmpty()) {
                continue;
            }

            List<String> finalLines = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("default:")) {
                    finalLines.addAll(defaultLinesFunction.apply(line.substring(8)));
                } else {
                    finalLines.add(line);
                }
            }
            Hologram<Location> hologram = arena.getFeature(HologramFeature.class).createHologram(location);
            hologramUpdaters.add(new HologramUpdater(hologram, finalLines));
        }
    }

    @Override
    public void clear() {
        clearHologram();
        hologramUpdaters.clear();
    }

    public void initHologram() {
        hologramUpdaters.forEach(hologramUpdater -> HologramFeature.reInit(hologramUpdater.hologram));
    }

    public void updateHologram() {
        hologramUpdaters.forEach(HologramUpdater::update);
    }

    public void clearHologram() {
        hologramUpdaters.forEach(hologramUpdater -> HologramFeature.clearIfInitialized(hologramUpdater.hologram));
    }

    private class HologramUpdater {
        private final Hologram<Location> hologram;
        private final List<String> lines;

        private HologramUpdater(Hologram<Location> hologram, List<String> lines) {
            this.hologram = hologram;
            this.lines = lines;
        }

        public void update() {
            if (!hologram.isInitialized()) {
                return;
            }
            List<HologramLine> replacedLines = lines.stream()
                    .map(line -> arena.getFeature(GameVariableFeature.class).replace(line))
                    .map(ColorUtils::colorize)
                    .map(TextHologramLine::new)
                    .collect(Collectors.toList());
            hologram.setLines(replacedLines);
        }
    }
}
