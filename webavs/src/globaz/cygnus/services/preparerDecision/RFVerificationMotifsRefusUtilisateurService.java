/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.demandes.IRFDemande;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author jje
 * 
 *         Retourne le statut de la demande en fonction des motifs de refus
 * 
 */
public class RFVerificationMotifsRefusUtilisateurService {

    /**
     * 
     * @param dates
     * @param session
     * @return String statut
     * @throws Exception
     */
    public String verifierMotifsDeRefusUtilisateur(Set<Map.Entry<String, String[]>> data, String statutInitial)
            throws Exception {

        String statut = "";
        if (!JadeStringUtil.isBlankOrZero(statutInitial)) {
            statut = statutInitial;
        } else {
            statut = IRFDemande.ACCEPTE;
        }

        Iterator<Map.Entry<String, String[]>> iter = data.iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String[]> montantMotifsRefusKeys = iter.next();

            if (JadeStringUtil.isBlank(montantMotifsRefusKeys.getValue()[1])
                    || montantMotifsRefusKeys.getValue()[1].equals(Boolean.TRUE.toString())) {

                if (!JadeStringUtil.isEmpty(montantMotifsRefusKeys.getValue()[0])) {
                    if (!statut.equals(IRFDemande.REFUSE)) {
                        statut = IRFDemande.PARTIELLEMENT_ACCEPTE;
                    }
                } else {
                    statut = IRFDemande.REFUSE;
                }

            } else {
                statut = IRFDemande.REFUSE;
            }
        }

        return statut;
    }

}
