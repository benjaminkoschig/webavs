package ch.globaz.common.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.exceptions.CommonTechnicalException;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

public class ConvertValueEnum<V, E extends Enum<E>> {
    private BiMap<V, E> mapping;

    public ConvertValueEnum(Map<V, E> mapping) {
        // HashBiMap
        this.mapping = ImmutableBiMap.copyOf(mapping);
    }

    public ConvertValueEnum() {
        this.mapping = HashBiMap.create();
    }

    public E put(V key, E value) {
        return mapping.put(key, value);
    }

    public E convert(V v) {
        E val = this.mapping.get(v);
        if (val != null) {
            return val;
        }
        throw new CommonTechnicalException("Unable to convert this value: (" + v + ")");
    }

    public V convert(E t) {
        V val = this.mapping.inverse().get(t);
        if (val != null) {
            return val;
        }
        throw new CommonTechnicalException("Unable to convert this enum: " + t.toString());
    }

    public Map<Enum<?>, V> toMapEnum() {
        Map<Enum<?>, V> map = new HashMap<Enum<?>, V>();
        for (Entry<V, E> entry : mapping.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        return map;
    }
}
