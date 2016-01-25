package globaz.apg.utils;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Une simple classe utilitaire. Devrait disparaitre....
 * 
 * @author lga
 */
public class APGUtils {

    /**
     * Dans un soucis de coh�rence, il serait plus sage d'utiliser la m�thode avec la transaction...
     * 
     * @param session
     *            La session � utiliser
     * @param idDroit
     *            L'id du droit � rechercher
     * @param genreService
     *            Le genre de service du droit � rechercher
     * @return Le droit dans le bon type; APDroitAPG ou APDroitMaternite sinon une exception sera lanc�e
     * @throws Exception
     *             Si le traitement de r�cup�ration du droit � �chou�
     */
    @Deprecated
    public static final APDroitLAPG loadDroit(BSession session, String idDroit, String genreService) throws Exception {
        return APGUtils.loadDroit(session, null, idDroit, genreService);
    }

    /**
     * Dans un soucis de coh�rence, il serait plus sage d'utiliser la m�thode avec la transaction...
     * 
     * @param session
     *            La session � utiliser
     * @param transaction
     *            La transaction en cours � utiliser
     * @param idDroit
     *            L'id du droit � rechercher
     * @param genreService
     *            Le genre de service du droit � rechercher
     * @return Le droit dans le bon type; APDroitAPG ou APDroitMaternite sinon une exception sera lanc�e
     * @throws Exception
     *             Si le traitement de r�cup�ration du droit � �chou�
     */
    public static final APDroitLAPG loadDroit(BSession session, BTransaction transaction, String idDroit,
            String genreService) throws Exception {
        APDroitLAPG droit;
        if (session == null) {
            throw new IllegalArgumentException("The provided BSession is null");
        }
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new Exception("idDroit is null or empty");
        }
        if (JadeStringUtil.isEmpty(genreService)) {
            throw new Exception("genre service inconnu");
        }
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            droit = new APDroitMaternite();
        } else {
            droit = new APDroitAPG();
        }
        droit.setSession(session);
        droit.setIdDroit(idDroit);
        if (transaction != null) {
            droit.retrieve(transaction);
        } else {
            droit.retrieve();
        }
        return droit;
    }
}
