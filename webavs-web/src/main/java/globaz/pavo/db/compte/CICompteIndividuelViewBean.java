package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;

/**
 * Insérez la description du type ici. Date de création : (20.11.2002 13:25:30)
 * 
 * @author: Administrator
 */
public class CICompteIndividuelViewBean extends CICompteIndividuel implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CICompteIndividuelViewBean.
     */
    public CICompteIndividuelViewBean() {
        super();
    }

    /**
     * Recherche d'un id de CI. Date de création : (15.11.2002 08:39:41)
     * 
     * @return le CI du tiers ou null si inexistant.
     * @param transaction
     *            la transaction à utiliser.
     */
    public String loadCI(BISession session) throws Exception {
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setISession(session);
        ciManager.orderByAvs(false);
        ciManager.setForNumeroAvs(getNumeroAvs());
        ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciManager.find();
        if (ciManager.size() != 0) {
            return ((CICompteIndividuel) ciManager.getEntity(0)).getCompteIndividuelId();
        }
        return null;
    }
}
