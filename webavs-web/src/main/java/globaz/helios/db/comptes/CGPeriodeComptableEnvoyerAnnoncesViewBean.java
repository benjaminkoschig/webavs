package globaz.helios.db.comptes;

import globaz.helios.process.CGPeriodeComptableEnvoiAnnonces;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.01.2003 10:23:23)
 * 
 * @author: Administrator
 */
public class CGPeriodeComptableEnvoyerAnnoncesViewBean extends CGPeriodeComptableEnvoiAnnonces implements
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

    public CGPeriodeComptableEnvoyerAnnoncesViewBean() throws Exception {
        super(new globaz.globall.db.BSession(globaz.helios.application.CGApplication.DEFAULT_APPLICATION_HELIOS));
    }
}
