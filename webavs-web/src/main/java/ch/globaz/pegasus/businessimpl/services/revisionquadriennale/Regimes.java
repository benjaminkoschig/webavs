package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresListBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.regime.Regime;

public class Regimes extends DonneesFinancieresListBase<Regime> {

    private Map<String, Regime> mapRegimesFamille;

    public Regimes() {
        mapRegimesFamille = new HashMap<String, Regime>();
    }

    public void setMapRegimesFamille(Map<String, Regime> mapRegimesFamille) {
        this.mapRegimesFamille = mapRegimesFamille;
    }

    public Boolean containsKey(String key) {
        return mapRegimesFamille.containsKey(key);
    }

    public Regime get(String idTiers) {
        if (mapRegimesFamille.containsKey(idTiers)) {
            return mapRegimesFamille.get(idTiers);
        } else {
            return new Regime();
        }
    }

    public Regime put(String idTiers, Regime regime) {
        return mapRegimesFamille.put(idTiers, regime);
    }

}