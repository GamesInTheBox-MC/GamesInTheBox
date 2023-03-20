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
package me.hsgamer.gamesinthebox.config.converter;

import me.hsgamer.hscore.config.annotation.converter.Converter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class StringMapConverter<T> implements Converter {
    protected abstract T toValue(Object value);

    protected abstract Object toRawValue(Object value);

    @Override
    public Map<String, T> convert(Object raw) {
        if (raw instanceof Map) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) raw).entrySet()) {
                map.put(Objects.toString(entry.getKey()), entry.getValue());
            }
            Map<String, T> result = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                T resultValue = toValue(entry.getValue());
                if (resultValue != null) {
                    result.put(entry.getKey(), resultValue);
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public Map<String, Object> convertToRaw(Object value) {
        if (value instanceof Map) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                map.put(Objects.toString(entry.getKey()), entry.getValue());
            }
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object rawValue = toRawValue(entry.getValue());
                if (rawValue != null) {
                    result.put(entry.getKey(), rawValue);
                }
            }
            return result;
        }
        return null;
    }
}
