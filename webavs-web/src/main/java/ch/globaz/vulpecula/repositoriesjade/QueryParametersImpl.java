package ch.globaz.vulpecula.repositoriesjade;

import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.domain.repositories.QueryParameters;

/**
 * Container de paramètres pour les requêtes SQL dynamiques avec pagination.
 * Ce container est une map possédant des méthodes pour communiquer avec le framework Jade générant des requêtes SQL.
 * Un "get" ne produira jamais de null mais retournera une chaîne vide.
 * La méthode valuesWithCaps convertira tous les paramètres en majuscules.
 */
public class QueryParametersImpl implements QueryParameters {
    private final Map<String, String> map;

    public QueryParametersImpl(String... parameters) {
        this(new HashMap<String, String>(), parameters);
    }

    public QueryParametersImpl(Map<String, String> map, String... parameters) {
        this.map = map;
        for (String parameter : parameters) {
            if (!this.map.containsKey(parameter)) {
                this.map.put(parameter, QueryParametersRegistry.EMPTY);
            }
        }
    }

    @Override
    public Map<String, String> valuesWithCaps() {
        Map<String, String> returnMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            returnMap.put(entry.getKey(), JadeStringUtil.toUpperCase(entry.getValue()));
        }
        return returnMap;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public String get(Object key) {
        if (JadeStringUtil.isEmpty(map.get(key))) {
            return QueryParametersRegistry.EMPTY;
        }
        return map.get(key);
    }

    @Override
    public String put(String key, String value) {
        return map.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<String> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }
}
