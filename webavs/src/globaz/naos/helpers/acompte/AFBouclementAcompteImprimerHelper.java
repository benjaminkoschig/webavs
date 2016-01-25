/*
 * Cr�� le 9 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.acompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.acompte.AFBouclementAcompteImprimerViewBean;
import globaz.naos.process.AFImpressionAcompteProcess;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Helper permettant de passer les donnees depuis le {@link globaz.naos.process.AFAnnonceSalairesViewBean le viewBean
 * l�ger} vers {@link globaz.naos.process.AFAnnonceSalaires le process}
 * </p>
 * 
 * @author vre
 */
public class AFBouclementAcompteImprimerHelper extends FWHelper {

    // Type de documents : 1 = Bouclement d'acompte
    // 2 = Pr�vision d'acompte
    // 3 = Decision d'acompte
    // 4 = Relev� � blanc

    public static final String typeDocument = "1";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe AFAnnonceSalairesHelper.
     */
    public AFBouclementAcompteImprimerHelper() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * copie les donnees du viewBean vers le process.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFBouclementAcompteImprimerViewBean asViewBean = (AFBouclementAcompteImprimerViewBean) viewBean;
        AFImpressionAcompteProcess process = new AFImpressionAcompteProcess();

        process.setAffiliationId(asViewBean.getAffiliationId());
        process.setDateEnvoi(asViewBean.getDateEnvoi());
        process.setDateRetour(asViewBean.getDateRetour());
        // process.setPeriode(asViewBean.getPeriode());
        process.setDateDebut(asViewBean.getDateDebut());
        process.setDateFin(asViewBean.getDateFin());
        process.setPlanAffiliationId(asViewBean.getPlanAffiliationId());
        process.setEMailAddress(asViewBean.getEmail());
        process.setFromIdExterneRole(asViewBean.getFromIdExterneRole());
        process.setTillIdExterneRole(asViewBean.getTillIdExterneRole());
        process.setTypeDocument(AFBouclementAcompteImprimerHelper.typeDocument);
        process.setISession(session);

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
