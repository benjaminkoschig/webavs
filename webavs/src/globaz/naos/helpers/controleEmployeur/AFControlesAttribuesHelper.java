/*
 * Créé le 12 févr. 07
 */
package globaz.naos.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.controleEmployeur.AFControlesAttribuesViewBean;
import globaz.naos.itext.controleEmployeur.AFListeControlesAttribuesIText;

/**
 * @author hpe
 */

public class AFControlesAttribuesHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {

            AFControlesAttribuesViewBean lcaViewBean = (AFControlesAttribuesViewBean) viewBean;

            AFListeControlesAttribuesIText liste = new AFListeControlesAttribuesIText((BSession) session);
            liste.setEMailAddress(lcaViewBean.getEMailAddress());
            liste.setSelectionGroupe(lcaViewBean.getSelectionGroupe());
            liste.setVisaReviseur(lcaViewBean.getVisaReviseur());
            liste.setGenreControle(lcaViewBean.getGenreControle());
            liste.setAnnee(lcaViewBean.getAnnee());

            BProcessLauncher.start(liste);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
