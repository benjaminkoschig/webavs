package ch.globaz.vulpecula.documents.listesinternes;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess.Caisse;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class RecapitulatifParConventionParCaisse implements Map<Convention, Map<Caisse, CaisseRecapitulatifs>> {
    private Map<Convention, Map<Caisse, CaisseRecapitulatifs>> map;

    public RecapitulatifParConventionParCaisse(Map<Convention, Map<Caisse, CaisseRecapitulatifs>> map) {
        this.map = map;
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
        return map.containsKey(value);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Map<Caisse, CaisseRecapitulatifs> get(Object key) {
        return map.get(key);
    }

    @Override
    public Map<Caisse, CaisseRecapitulatifs> put(Convention key, Map<Caisse, CaisseRecapitulatifs> value) {
        return map.put(key, value);
    }

    @Override
    public Map<Caisse, CaisseRecapitulatifs> remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends Convention, ? extends Map<Caisse, CaisseRecapitulatifs>> m) {
        map.putAll(m);
    }

    @Override
    public Set<Convention> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Map<Caisse, CaisseRecapitulatifs>> values() {
        return map.values();
    }

    @Override
    public Set<Map.Entry<Convention, Map<Caisse, CaisseRecapitulatifs>>> entrySet() {
        return map.entrySet();
    }

    public Montant getTotalContrib(Convention key) {
        Montant result = Montant.ZERO;

        for (CaisseRecapitulatifs entry : map.get(key).values()) {
            result = result.add(entry.getContributions());
        }

        return result;
    }

    public Montant getTotalMasse(Convention key) {
        Montant result = Montant.ZERO;

        for (CaisseRecapitulatifs entry : map.get(key).values()) {
            result = result.add(entry.getMasse());
        }

        return result;
    }
}
