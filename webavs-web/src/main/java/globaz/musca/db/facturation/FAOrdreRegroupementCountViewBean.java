package globaz.musca.db.facturation;

import globaz.globall.db.BSession;
import java.util.HashMap;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class FAOrdreRegroupementCountViewBean extends FAOrdreRegroupement {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FAOrdreRegroupementCountViewBean() {
        super();
    }

    public Integer getNbLignes(FAEnteteFacture entFacture, BSession session) throws Exception {

        FAAfactImpressionManager manager = new FAAfactImpressionManager();
        manager.setSession(session);
        manager.setForIdEnteteFacture(entFacture.getIdEntete());
        manager.find(0);
        int count = 0;
        HashMap<String, String> ordre = new HashMap<String, String>();
        FAAfact afact = new FAAfact();
        for (int i = 0; i < manager.getSize(); i++) {
            afact = (FAAfact) manager.getEntity(i);
            if (!afact.getLibelleOrdre(entFacture.getISOLangueTiers()).equals("")) {
                if (!ordre.containsKey(afact.getOrdreRegroupement()) && !afact.isAQuittancer().booleanValue()) {
                    ordre.put(afact.getOrdreRegroupement(), afact.getLibelleOrdre(entFacture.getISOLangueTiers()));
                    count++;
                }
            } else if (!afact.isAQuittancer().booleanValue()) {
                count++;
            }
        }
        return new Integer(count);
    }

}
