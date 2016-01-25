/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeJointMotifRefus;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeJointMotifRefusManager;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author jje
 * 
 *         Recherche les motifs de refus d'une demande
 */
public class RFRechercheMotifsRefusService {

    /**
     * @param BSession
     *            session, String idDemande
     * @return String[IdMotifRefus, String[]{Montant,hasMontant}]
     * @throws Exception
     */
    public Map<String, String[]> rechercherMotifsRefus(BSession session, String idDemande, BITransaction transaction)
            throws Exception {

        Map<String, String[]> montantsMotifsRefusMap = new HashMap<String, String[]>();

        RFAssMotifsRefusDemandeJointMotifRefusManager rfAssMotifsDeRefusJointMotifRefusMgr = new RFAssMotifsRefusDemandeJointMotifRefusManager();
        rfAssMotifsDeRefusJointMotifRefusMgr.setSession(session);
        rfAssMotifsDeRefusJointMotifRefusMgr.setForIdDemande(idDemande);
        rfAssMotifsDeRefusJointMotifRefusMgr.changeManagerSize(0);
        rfAssMotifsDeRefusJointMotifRefusMgr.find(transaction);

        Iterator<RFAssMotifsRefusDemandeJointMotifRefus> rfAssMotifsDeRefusIter = rfAssMotifsDeRefusJointMotifRefusMgr
                .iterator();

        while (rfAssMotifsDeRefusIter.hasNext()) {
            RFAssMotifsRefusDemandeJointMotifRefus rfAssMotifsRefusDemande = rfAssMotifsDeRefusIter.next();
            if (null != rfAssMotifsRefusDemande) {
                montantsMotifsRefusMap.put(rfAssMotifsRefusDemande.getIdMotifsRefus(), new String[] {
                        rfAssMotifsRefusDemande.getMntMotifsDeRefus(),
                        rfAssMotifsRefusDemande.getHasMontant().toString() });
            }
        }

        return montantsMotifsRefusMap;

    }

}
