package ch.globaz.vulpecula.documents.listesinternes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class RecapitulatifParConvention implements Map<Convention, Map<CaisseKey, PeriodeRecapitulatifs>> {
    private TaxationOfficeContainer taxationOfficeContainer;
    private Map<Convention, Map<CaisseKey, PeriodeRecapitulatifs>> map;
    private Annee annee;

    public RecapitulatifParConvention(Map<Convention, Map<CaisseKey, PeriodeRecapitulatifs>> map,
            TaxationOfficeContainer taxationOfficeContainer, Annee annee) {
        this.map = map;
        this.taxationOfficeContainer = taxationOfficeContainer;
        this.annee = annee;
    }

    public List<Montant> getMontantsTotaux(Convention convention) {
        List<Montant> montants = new ArrayList<Montant>();
        Map<CaisseKey, PeriodeRecapitulatifs> recapsParCaisse = map.get(convention);
        for (Date date : getDates()) {
            Montant montant = Montant.ZERO;
            for (Map.Entry<CaisseKey, PeriodeRecapitulatifs> entry : recapsParCaisse.entrySet()) {
                Montant montantActuel = getMontant(convention, entry.getKey(), date);
                montant = montant.add(montantActuel);
            }
            montants.add(montant);
        }
        return montants;
    }

    public Montant getMontantTotal(Convention convention) {
        Montant montantTotal = Montant.ZERO;
        for (Montant montant : getMontantsTotaux(convention)) {
            montantTotal = montantTotal.add(montant);
        }
        return montantTotal;
    }

    public Montant getMontantTotal(Convention convention, CaisseKey caisse) {
        Montant montant = Montant.ZERO;
        PeriodeRecapitulatifs periodes = map.get(convention).get(caisse);
        for (Date date : periodes.getDates()) {
            montant = montant.add(getMontant(convention, caisse, date));
        }
        return montant;
    }

    public Montant getMasseTotal(Convention convention, CaisseKey caisse) {
        Montant masse = Montant.ZERO;
        PeriodeRecapitulatifs periodes = map.get(convention).get(caisse);
        for (Date date : periodes.getDates()) {
            masse = masse.add(getMasse(convention, caisse, date));
        }
        return masse;
    }

    public List<Montant> getMassesTotales(Convention convention) {
        List<Montant> montants = new ArrayList<Montant>();
        Map<CaisseKey, PeriodeRecapitulatifs> recapsParCaisse = map.get(convention);
        for (Date date : getDates()) {
            Montant montant = Montant.ZERO;
            for (Map.Entry<CaisseKey, PeriodeRecapitulatifs> entry : recapsParCaisse.entrySet()) {
                Montant montantActuel = getMontant(convention, entry.getKey(), date);
                montant = montant.add(montantActuel);
            }
            montants.add(montant);
        }
        return montants;
    }

    public Montant getMasseTotale(Convention convention) {
        Montant masseTotale = Montant.ZERO;
        for (Montant masse : getMassesTotales(convention)) {
            masseTotale = masseTotale.add(masse);
        }
        return masseTotale;
    }

    public Montant getMontant(Convention convention, CaisseKey caisse, Date date) {
        PeriodeRecapitulatifs periode = map.get(convention).get(caisse);
        Montant montant = periode.getMontant(date);
        Montant montantTO = taxationOfficeContainer.getMontant(convention.getCode(), caisse.getIdExterne(), date);
        return montant.substract(montantTO);
    }

    public Montant getMasse(Convention convention, CaisseKey caisse, Date date) {
        PeriodeRecapitulatifs periode = map.get(convention).get(caisse);
        Montant masse = periode.getMasse(date);
        Montant masseTO = taxationOfficeContainer.getMasse(convention.getCode(), caisse.getIdExterne(), date);
        return masse.substract(masseTO);
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
    public Map<CaisseKey, PeriodeRecapitulatifs> get(Object key) {
        return map.get(key);
    }

    @Override
    public Map<CaisseKey, PeriodeRecapitulatifs> put(Convention key, Map<CaisseKey, PeriodeRecapitulatifs> value) {
        return map.put(key, value);
    }

    @Override
    public Map<CaisseKey, PeriodeRecapitulatifs> remove(Object key) {
        return map.remove(map);
    }

    @Override
    public void putAll(Map<? extends Convention, ? extends Map<CaisseKey, PeriodeRecapitulatifs>> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Convention> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Map<CaisseKey, PeriodeRecapitulatifs>> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<Convention, Map<CaisseKey, PeriodeRecapitulatifs>>> entrySet() {
        return map.entrySet();
    }
}
