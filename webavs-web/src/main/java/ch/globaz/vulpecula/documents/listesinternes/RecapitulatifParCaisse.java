package ch.globaz.vulpecula.documents.listesinternes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.documents.listesinternes.CaisseKey.Type;
import ch.globaz.vulpecula.documents.listesinternes.ListesInternesProcess.Caisse;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class RecapitulatifParCaisse implements Map<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> {
    private TaxationOfficeContainer taxationOfficeContainer;
    private Map<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> map;
    private Annee annee;

    public RecapitulatifParCaisse(Map<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> map,
            TaxationOfficeContainer taxationOfficeContainer, Annee annee) {
        this.map = map;
        this.taxationOfficeContainer = taxationOfficeContainer;
        this.annee = annee;
    }

    public List<Montant> getMontantsTotaux(String type, CaisseKey key) {
        List<Montant> montants = new ArrayList<Montant>();
        Map<Convention, PeriodeRecapitulatifs> recapsParCaisse = map.get(type).get(
                new Caisse(key.getIdExterne(), key.getLibelle()));
        for (Date date : getDates()) {
            Montant montant = Montant.ZERO;
            for (Map.Entry<Convention, PeriodeRecapitulatifs> entry : recapsParCaisse.entrySet()) {
                Montant montantActuel = getMontant(type, entry.getKey(), key, date);
                montant = montant.add(montantActuel);
            }
            montants.add(montant);
        }
        return montants;
    }

    public Montant getMontantTotal(String type, CaisseKey key) {
        Montant montantTotal = Montant.ZERO;
        for (Montant montant : getMontantsTotaux(type, key)) {
            montantTotal = montantTotal.add(montant);
        }
        return montantTotal;
    }

    public Montant getMontantTotal(String type, Convention convention, CaisseKey caisse) {
        Montant montant = Montant.ZERO;
        PeriodeRecapitulatifs periodes = map.get(type).get(new Caisse(caisse.getIdExterne(), caisse.getLibelle()))
                .get(convention);
        for (Date date : periodes.getDates()) {
            montant = montant.add(getMontant(type, convention, caisse, date));
        }
        return montant;
    }

    public List<Montant> getMassesTotales(String type, CaisseKey key) {
        List<Montant> montants = new ArrayList<Montant>();
        Map<Convention, PeriodeRecapitulatifs> recapsParCaisse = map.get(type).get(
                new Caisse(key.getIdExterne(), key.getLibelle()));
        for (Date date : getDates()) {
            Montant montant = Montant.ZERO;
            for (Map.Entry<Convention, PeriodeRecapitulatifs> entry : recapsParCaisse.entrySet()) {
                Montant montantActuel = getMontant(type, entry.getKey(), key, date);
                montant = montant.add(montantActuel);
            }
            montants.add(montant);
        }
        return montants;
    }

    public Montant getMasseTotale(String type, CaisseKey key) {
        Montant masseTotale = Montant.ZERO;
        for (Montant masse : getMassesTotales(type, key)) {
            masseTotale = masseTotale.add(masse);
        }
        return masseTotale;
    }

    public Montant getMontant(String type, Convention convention, CaisseKey key, Date date) {
        Montant montantTO = Montant.ZERO;
        Caisse caisse = new Caisse(key.getIdExterne(), key.getLibelle());
        if (map.containsKey(type)) {
            Map<Caisse, Map<Convention, PeriodeRecapitulatifs>> s = map.get(type);

            if (s.containsKey(caisse)) {
                PeriodeRecapitulatifs periode = map.get(type).get(caisse).get(convention);
                Montant montant = key.getType() == Type.COT ? periode.getMontant(date) : periode.getMasse(date);
                if (key.getType() == Type.COT) {
                    montantTO = taxationOfficeContainer.getMontant(convention.getCode(), caisse.getIdExterne(), date);
                } else {
                    montantTO = taxationOfficeContainer.getMasse(convention.getCode(), caisse.getIdExterne(), date);
                }

                return montant.substract(montantTO);
            }
        }

        return Montant.ZERO;
    }

    private List<Date> getDates() {
        return new AnneeComptable(annee).getMois();
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
    public Map<Caisse, Map<Convention, PeriodeRecapitulatifs>> get(Object key) {
        return map.get(key);
    }

    @Override
    public Map<Caisse, Map<Convention, PeriodeRecapitulatifs>> put(String key,
            Map<Caisse, Map<Convention, PeriodeRecapitulatifs>> value) {
        return map.put(key, value);
    }

    @Override
    public Map<Caisse, Map<Convention, PeriodeRecapitulatifs>> remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> m) {
        map.putAll(m);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>> values() {
        return map.values();
    }

    @Override
    public Set<Map.Entry<String, Map<Caisse, Map<Convention, PeriodeRecapitulatifs>>>> entrySet() {
        return map.entrySet();
    }

}
