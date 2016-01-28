package globaz.helios.db.comptes;

import globaz.globall.db.BSession;
import globaz.helios.itext.list.CGProcessImpressionJournalEcritures;

/**
 * Insérez la description du type ici. Date de création : (19.12.2002 17:35:25)
 * 
 * @author: Administrator
 */
public class CGJournalImprimerEcrituresViewBean extends CGProcessImpressionJournalEcritures implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGJournalImprimerEcrituresViewBean() throws Exception {
        super(new BSession(globaz.helios.application.CGApplication.DEFAULT_APPLICATION_HELIOS));
    }
}
