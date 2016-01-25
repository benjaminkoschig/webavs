package ch.globaz.vulpecula.businessimpl.services.prestations;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.musca.business.constantes.FACSPassage;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.prestations.Prestation;
import ch.globaz.vulpecula.external.models.musca.Passage;

/**
 * Classe permettant de d�terminer le status d'une prestation.
 * Elle v�rifie notamment qu'une prestation est modifiable
 * Une prestation modifiable est une prestation soit : <li>A l'�tat saisi <li>Qui poss�de un passage de facturation
 * annul�
 * 
 */
public class PrestationStatus {
    private Map<String, Passage> passages = new HashMap<String, Passage>();

    public boolean isModifiable(Prestation p) {
        if (p.isModifiable()) {
            return true;
        }
        String idPassage = p.getIdPassageFacturation();
        if (!passages.containsKey(idPassage)) {
            passages.put(idPassage, VulpeculaServiceLocator.getPassageService().findById(idPassage));
        }
        return FACSPassage.STATUS_ANNULE.equals(passages.get(idPassage).getStatus());
    }
}
