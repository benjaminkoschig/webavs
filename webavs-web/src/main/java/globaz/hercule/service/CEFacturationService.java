package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.facturation.CEEnteteFacture;
import globaz.hercule.service.facturation.CEEnteteFactureManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Service d'appel au module facturation
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEFacturationService {

    public static final String TOTAL_FACTURE = "TOTAL_FACTURE";
    public static final String DATE_FACTURE = "DATE_FACTURE";

    /**
     * Méthode permettant la récupération des données de facturation d'un controle employeur.
     * Le résultat est une map qui est de la forme suivante :
     * 
     * [String:String]
     * [TOTAL_FACTURE:total de la facture]
     * [DATE_FACTURE:Date de la facture]
     * 
     * Il faut utiliser les constantes suivantes pour récupérer les données de la map
     * CEFacturationService.TOTAL_FACTURE
     * CEFacturationService.DATE_FACTURE
     * 
     * @param session
     * @param transaction
     * @param idControle
     * @return
     * @throws HerculeException
     */
    public static Map<String, String> getInfosFactureForControle(BSession session, BTransaction transaction,
            String idControle) throws HerculeException {

        if (idControle == null) {
            throw new NullPointerException("Unabled to retrieve infos de facture. idControle is null");
        }
        if (session == null) {
            throw new NullPointerException("Unabled to retrieve infos de facture. Session is null");
        }

        // Recherche des masses
        CEEnteteFactureManager manager = new CEEnteteFactureManager();
        manager.setIdControle(idControle);
        manager.setSession(session);

        try {
            manager.find(transaction);

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve infos de facture for idcontrole "
                    + idControle, e);
        }

        Map<String, String> donnees = new HashMap<String, String>();

        if (manager.size() > 0) {
            CEEnteteFacture enteteFacture = (CEEnteteFacture) manager.getFirstEntity();
            donnees.put(DATE_FACTURE, enteteFacture.getDateFacturation());
            donnees.put(TOTAL_FACTURE, enteteFacture.getTotalFacture());

        }
        return donnees;
    }

    /**
     * Constructeur de CEFacturationService
     */
    protected CEFacturationService() {
        throw new UnsupportedOperationException(); // prevents calls from subclass
    }
}
