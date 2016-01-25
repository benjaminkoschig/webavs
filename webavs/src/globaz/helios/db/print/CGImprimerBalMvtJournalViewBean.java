package globaz.helios.db.print;

import globaz.helios.itext.list.CGProcessImpressionBalanceMvtJournal;

/**
 * Insérez la description du type ici. Date de création : (10.01.2003 10:23:23)
 * 
 * @author: Administrator
 */
public class CGImprimerBalMvtJournalViewBean extends CGProcessImpressionBalanceMvtJournal implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CGExerciceComptableImprimerPlanComptableViewBean.
     * 
     * @param session
     * @param filenameRoot
     * @param companyName
     * @param documentTitle
     * @param manager
     */

    public CGImprimerBalMvtJournalViewBean() throws Exception {
        super(new globaz.globall.db.BSession(globaz.helios.application.CGApplication.DEFAULT_APPLICATION_HELIOS),
                globaz.helios.application.CGApplication.APPLICATION_HELIOS_REP, "BalMvtJournal");
    }
}
