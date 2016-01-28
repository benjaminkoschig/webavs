package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BSession;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelExtournerViewBean;
import globaz.pavo.db.compte.CICompteIndividuelImprimerViewBean;

/**
 * Controlleur g�rant les fonctions sp�ciales des rassemblements/ouvetures Date de cr�ation : (16.10.2002 08:36:43)
 * 
 * @author: dgi
 */
public class CICompteIndividuelHelper extends FWHelper {
    /**
     * Constructeur.
     * 
     * @param action
     *            java.lang.String
     */
    public CICompteIndividuelHelper() {
        super();
    }

    /**
     * Ex�cution des m�thodes sp�cifiques au rassemblement/ouverture des CI. Date de cr�ation : (03.05.2002 16:23:10)
     * 
     * @return le viewBean en question
     * @param viewBean
     *            bean associ� � l'action en court
     * @param action
     *            action en court
     * @param session
     *            session de l'utilisateur
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        try {
            if ("imprimer".equals(action.getActionPart())) {
                // Pour afficher l'�cran des inscriptions au journal
                if (!((CICompteIndividuelImprimerViewBean) viewBean).hasExecRight()) {
                    viewBean.setMessage(((BSession) session).getLabel("MSG_CI_NO_AUTH"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

            } else if ("extourner".equals(action.getActionPart())) {
                CICompteIndividuel ciACopier = (CICompteIndividuel) viewBean;
                ciACopier.setSession((BSession) viewBean.getISession());
                ciACopier.setId(((CICompteIndividuelExtournerViewBean) viewBean).getCompteIndividuelId());

                try {
                    ciACopier.retrieve();
                    ciACopier.extournerCi(
                            ((CICompteIndividuelExtournerViewBean) viewBean).getCompteIndividuelIdDestination(),
                            ((CICompteIndividuelExtournerViewBean) viewBean).getAnneeDebut(),
                            ((CICompteIndividuelExtournerViewBean) viewBean).getAnneeFin());
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        /*
         * if ("extourner".equals(action.getActionPart())) { CICompteIndividuel ciACopier = new CICompteIndividuel();
         * ciACopier.setSession((BSession) viewBean.getISession());
         * ciACopier.setCompteIndividuelId(((CICompteIndividuelExtournerViewBean ) viewBean).getCompteIndividuelId());
         * return (CICompteIndividuelViewBean)viewBean; try { ciACopier.retrieve(); } catch (Exception e) { }
         * //ciACopier.extournerCi(((CICompteIndividuelExtournerViewBean) viewBean).getCompteIndividuelIdSource()); }
         */
        return viewBean;

    }
}
