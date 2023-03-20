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
