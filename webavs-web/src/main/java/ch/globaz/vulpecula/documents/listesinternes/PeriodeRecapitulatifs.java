package ch.globaz.vulpecula.documents.listesinternes;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class PeriodeRecapitulatifs {
    private Map<Date, RecapitulatifDTO> map = new TreeMap<Date, RecapitulatifDTO>();

    Montant getMontant(Date date) {
        RecapitulatifDTO recap = map.get(date);
        if (recap == null) {
            return Montant.ZERO;
        } else {
            return new Montant(recap.getMontant());
        }
    }

    Montant getMasse(Date date) {
        RecapitulatifDTO recap = map.get(date);
        if (recap == null) {
            return Montant.ZERO;
        } else {
            return new Montant(recap.getMasse());
        }
    }

    public Collection<Date> getDates() {
        return map.keySet();
    }

    public void put(Date key, RecapitulatifDTO recapForCaisse) {
        map.put(key, recapForCaisse);
    }
}
