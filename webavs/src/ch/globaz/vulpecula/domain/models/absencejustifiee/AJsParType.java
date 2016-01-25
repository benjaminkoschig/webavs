package ch.globaz.vulpecula.domain.models.absencejustifiee;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class AJsParType implements Map<TypeAbsenceJustifiee, AbsencesJustifiees> {
    private final Map<TypeAbsenceJustifiee, AbsencesJustifiees> ajsParType;

    public AJsParType(Map<TypeAbsenceJustifiee, AbsencesJustifiees> ajsParType) {
        this.ajsParType = ajsParType;
    }

    @Override
    public int size() {
        return ajsParType.size();
    }

    @Override
    public boolean isEmpty() {
        return ajsParType.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return ajsParType.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return ajsParType.containsValue(value);
    }

    @Override
    public AbsencesJustifiees get(Object key) {
        return ajsParType.get(key);
    }

    @Override
    public AbsencesJustifiees put(TypeAbsenceJustifiee key, AbsencesJustifiees value) {
        return ajsParType.put(key, value);
    }

    @Override
    public AbsencesJustifiees remove(Object key) {
        return ajsParType.remove(key);
    }

    @Override
    public void putAll(Map<? extends TypeAbsenceJustifiee, ? extends AbsencesJustifiees> m) {
        ajsParType.putAll(m);
    }

    @Override
    public void clear() {
        ajsParType.clear();
    }

    @Override
    public Set<TypeAbsenceJustifiee> keySet() {
        return ajsParType.keySet();
    }

    @Override
    public Collection<AbsencesJustifiees> values() {
        return ajsParType.values();
    }

    @Override
    public Set<java.util.Map.Entry<TypeAbsenceJustifiee, AbsencesJustifiees>> entrySet() {
        return ajsParType.entrySet();
    }

    public int sommeNombreJours() {
        int sommeJours = 0;
        for (AbsencesJustifiees aj : values()) {
            sommeJours += aj.sommeNombreJours();
        }
        return sommeJours;
    }

    public Montant sommeSalairesHoraires() {
        Montant montant = Montant.ZERO;
        for (AbsencesJustifiees aj : values()) {
            montant = montant.add(aj.sommeSalairesHoraires());
        }
        return montant;
    }

    public Montant sommeMontantsBrutsOuvrier() {
        Montant montant = Montant.ZERO;
        for (AbsencesJustifiees aj : values()) {
            montant = montant.add(aj.sommeMontantsBrutsOuvrier());
        }
        return montant;
    }

    public Montant sommeMontantsBrutsEmploye() {
        Montant montant = Montant.ZERO;
        for (AbsencesJustifiees aj : values()) {
            montant = montant.add(aj.sommeMontantsBrutsEmploye());
        }
        return montant;
    }

    public Montant sommePartsPatronales() {
        Montant montant = Montant.ZERO;
        for (AbsencesJustifiees aj : values()) {
            montant = montant.add(aj.sommePartsPatronales());
        }
        return montant;
    }

    public Montant sommeMontantVerses() {
        Montant montant = Montant.ZERO;
        for (AbsencesJustifiees aj : values()) {
            montant = montant.add(aj.sommeMontantsVerses());
        }
        return montant;
    }
}
