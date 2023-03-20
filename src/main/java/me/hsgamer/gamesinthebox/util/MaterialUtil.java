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

import com.cryptomorin.xseries.XMaterial;
import com.lewdev.probabilitylib.ProbabilityCollection;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class MaterialUtil {
    private MaterialUtil() {
        // EMPTY
    }

    public static Map<XMaterial, Number> parseMaterialNumberMap(Map<String, Object> values) {
        Map<XMaterial, Number> map = new EnumMap<>(XMaterial.class);
        values.forEach((k, v) -> {
            Optional<XMaterial> optionalXMaterial = XMaterial.matchXMaterial(k);
            if (!optionalXMaterial.isPresent()) return;
            double value;
            try {
                value = Double.parseDouble(Objects.toString(v));
            } catch (Exception e) {
                return;
            }
            map.put(optionalXMaterial.get(), value);
        });
        return map;
    }

    public static ProbabilityCollection<XMaterial> parseMaterialProbability(Map<String, Object> values) {
        ProbabilityCollection<XMaterial> collection = new ProbabilityCollection<>();
        parseMaterialNumberMap(values).forEach((xMaterial, number) -> collection.add(xMaterial, number.intValue()));
        return collection;
    }
}
