package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import ch.globaz.al.liste.process.ALIndeRevenuMinNonAtteintProcess;

/**
 * View bean permettant d'appeler le process pour générer la liste des indépendants aux AF avec un revenu minimal non
 * atteint
 * 
 * @author: est
 */
public class FAPassageListerIndeRevenuMinViewBean extends ALIndeRevenuMinNonAtteintProcess implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:55:33)
     */
    public FAPassageListerIndeRevenuMinViewBean() throws java.lang.Exception {
        //
    }

}
