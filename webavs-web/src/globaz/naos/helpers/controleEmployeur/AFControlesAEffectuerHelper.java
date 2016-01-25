/*
 * Créé le 14 févr. 07
 */
package globaz.naos.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.controleEmployeur.AFControlesAEffectuerViewBean;
import globaz.naos.itext.controleEmployeur.AFListeControlesAEffectuerIText;
import globaz.naos.process.AFListeControlesAEffectuerProcess;

/**
 * @author hpe
 */

public class AFControlesAEffectuerHelper extends FWHelper {

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

            AFControlesAEffectuerViewBean lcaViewBean = (AFControlesAEffectuerViewBean) viewBean;
            if (lcaViewBean.getListeExcel().booleanValue()) {
                AFListeControlesAEffectuerProcess process = new AFListeControlesAEffectuerProcess();

                // Setter autres propriétés ?
                process.setSession((BSession) session);
                process.setGenreControle(lcaViewBean.getGenreControle());
                // process.setIsGenerationControleEmployeur(lcaViewBean.getIsGenerationControleEmployeur());
                // process.setIsTenirComptePerioAffilie(lcaViewBean.getIsTenirComptePerioAffilie());
                // process.setIsTenirComptePerioCaisse(lcaViewBean.getIsTenirComptePerioCaisse());
                process.setAnnee(lcaViewBean.getAnnee());
                // process.setAnnee1(lcaViewBean.getAnnee1());
                // process.setAnnee2(lcaViewBean.getAnnee2());
                // process.setAnnee3(lcaViewBean.getAnnee3());
                // process.setAnnee4(lcaViewBean.getAnnee4());
                // process.setAnnee5(lcaViewBean.getAnnee5());
                process.setMasseSalA(lcaViewBean.getMasseSalA());
                process.setMasseSalDe(lcaViewBean.getMasseSalDe());
                process.setIsAvecReviseur(lcaViewBean.getIsAvecReviseur());
                process.setListeExcel(lcaViewBean.getListeExcel());
                process.setListePdf(lcaViewBean.getListePdf());
                process.setEMailAddress(lcaViewBean.getEMailAddress());
                process.setTypeAdresse(lcaViewBean.getTypeAdresse());
                BProcessLauncher.start(process);
                // process.executeProcess();
            } else {
                AFListeControlesAEffectuerIText listePdf = new AFListeControlesAEffectuerIText((BSession) session);
                listePdf.setEMailAddress(lcaViewBean.getEMailAddress());
                listePdf.setGenreControle(lcaViewBean.getGenreControle());
                listePdf.setAnnee(lcaViewBean.getAnnee());
                listePdf.setIsAvecReviseur(lcaViewBean.getIsAvecReviseur());
                listePdf.setMasseSalDe(lcaViewBean.getMasseSalDe());
                listePdf.setMasseSalA(lcaViewBean.getMasseSalA());

                BProcessLauncher.start(listePdf);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
