package ch.globaz.vulpecula.domain.models.servicemilitaire;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class SMsParType implements Map<GenreSM, ServicesMilitaires> {
    Map<GenreSM, ServicesMilitaires> smsParType;

    public SMsParType(Map<GenreSM, ServicesMilitaires> smsParType) {
        this.smsParType = smsParType;
    }

    public double getSommeNbJours() {
        double nbJours = 0;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            nbJours = nbJours + servicesMilitaires.getSommeNbjours();
        }
        return nbJours;
    }

    public Montant getSommeCouvertureAPG() {
        Montant somme = Montant.ZERO;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            somme = somme.add(servicesMilitaires.getSommeCouvertureAPG());
        }
        return somme;
    }

    public Montant getSommeVersementAPG() {
        Montant somme = Montant.ZERO;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            somme = somme.add(servicesMilitaires.getSommeVersementAPG());
        }
        return somme;
    }

    public Montant getSommeBruts() {
        Montant somme = Montant.ZERO;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            somme = somme.add(servicesMilitaires.getSommeBruts());
        }
        return somme;
    }

    public Montant getSommeMontantAVS_AC() {
        Montant somme = Montant.ZERO;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            somme = somme.add(servicesMilitaires.getSommeMontantAVS_AC());
        }
        return somme;
    }

    public Montant getSommeMontantAF() {
        Montant somme = Montant.ZERO;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            somme = somme.add(servicesMilitaires.getSommeMontantAF());
        }
        return somme;
    }

    public Montant getSommeTotalVerse() {
        Montant somme = Montant.ZERO;
        for (ServicesMilitaires servicesMilitaires : smsParType.values()) {
            somme = somme.add(servicesMilitaires.getSommeTotalVerse());
        }
        return somme;
    }

    @Override
    public int size() {
        return smsParType.size();
    }

    @Override
    public boolean isEmpty() {
        return smsParType.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return smsParType.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return smsParType.containsValue(value);
    }

    @Override
    public ServicesMilitaires get(Object key) {
        return smsParType.get(key);
    }

    @Override
    public ServicesMilitaires put(GenreSM key, ServicesMilitaires value) {
        return smsParType.put(key, value);
    }

    @Override
    public ServicesMilitaires remove(Object key) {
        return smsParType.remove(key);
    }

    @Override
    public void putAll(Map<? extends GenreSM, ? extends ServicesMilitaires> m) {
        smsParType.putAll(m);
    }

    @Override
    public void clear() {
        smsParType.clear();
    }

    @Override
    public Set<GenreSM> keySet() {
        return smsParType.keySet();
    }

    @Override
    public Collection<ServicesMilitaires> values() {
        return smsParType.values();
    }

    @Override
    public Set<java.util.Map.Entry<GenreSM, ServicesMilitaires>> entrySet() {
        return smsParType.entrySet();
    }

}
