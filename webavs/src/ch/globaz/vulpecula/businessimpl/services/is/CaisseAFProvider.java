package ch.globaz.vulpecula.businessimpl.services.is;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.domain.models.common.Date;

/**
 * Cette classe permet de retrouver la caisse AF relatif à un affilié.
 * Elle doit être injecté dans une classe en tant que membre car elle fait office de cache durant les triatements.
 */
class CaisseAFProvider {
    private Map<Pair<String, String>, AssuranceInfo> mapIdAffilieCaisseAF = new HashMap<Pair<String, String>, AssuranceInfo>();

    /**
     * Retourne la caisse AF en fonction du numéro d'affilié, de son activité et d'une date.
     * 
     * @param numAffilie Numéro d'affilié
     * @param activite Activité de l'allocataire
     * @param date Date de référence
     * @return AssuranceInfo contenant la caisse AF
     */
    public AssuranceInfo get(String numAffilie, String activite, Date date) {
        Pair<String, String> pair = new Pair<String, String>(numAffilie, activite);
        if (!mapIdAffilieCaisseAF.containsKey(pair)) {
            mapIdAffilieCaisseAF.put(pair, find(numAffilie, activite, date));
        }
        return mapIdAffilieCaisseAF.get(pair);
    }

    /**
     * Recherche l'assuranceInfo (caisseAF) en fonction d'un numéro d'affilié et de son activité. La liste des activités
     * se trouve dans {@link ALCSDossier}
     * 
     * @param numAffilie Numéro d'affilié pour lequel rechercher la caisse
     * @param activite Activité de l'allocataire
     * @param date Date pour laquelle rechercher la caisse.
     * @return AssuranceInfo, contenant la caisse AF
     */
    private AssuranceInfo find(String numAffilie, String activite, Date date) {
        try {
            return ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(numAffilie, activite,
                    date.getSwissValue());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
        }
    }
}
