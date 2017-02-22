package ch.globaz.naos.ree.domain.pojo;

import java.util.Map;
import ch.globaz.common.domaine.Adresse;

public class AdresseCache {

    private Map<String, Adresse> mapAdressesDomicile;

    public AdresseCache(Map<String, Adresse> mapAdresses) {
        mapAdressesDomicile = mapAdresses;
    }

    public Adresse getAdresse(String idTiers) throws AdresseCacheNoMatchException {
        if (mapAdressesDomicile.containsKey(idTiers) && mapAdressesDomicile.get(idTiers) != null) {
            return mapAdressesDomicile.get(idTiers);
        } else {
            throw new AdresseCacheNoMatchException();
        }
    }

}
