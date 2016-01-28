/*
 * Créé le 10 oct. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.helpers.tauxImposition;

import globaz.cepheus.vb.tauxImposition.DOTauxImpositionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOTauxImpositionHelper extends PRAbstractHelper {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_add(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._add(viewBean, action, session);

        // si la periode precedente n'est pas close, on set sa date de fin a
        // dateDebut-1
        // de la nouvelle periode
        closPeriodePrecedente(viewBean, session);

    }

    /**
     * Si la periode precedente n'est pas close, on set sa date de fin a dateDebut-1 de la nouvelle periode
     * 
     * @param viewBean
     * @param session
     */
    private void closPeriodePrecedente(FWViewBeanInterface viewBean, BISession session) {

        DOTauxImpositionViewBean tiViewBean = (DOTauxImpositionViewBean) viewBean;

        PRTauxImpositionManager periodes = new PRTauxImpositionManager();
        periodes.setSession((BSession) session);
        periodes.setForCsCanton(tiViewBean.getCsCanton());
        periodes.setForTypeImpot(tiViewBean.getTypeImpotSource());
        periodes.setForIdTauxImpositionDifferentFrom(tiViewBean.getIdTauxImposition());
        periodes.setForPeriodeNotClose(true);

        try {
            periodes.find();

            if (periodes.size() == 1) {
                PRTauxImposition periode = (PRTauxImposition) periodes.get(0);

                JACalendar jaCal = new JACalendarGregorian();
                JADate dateFin = new JADate(tiViewBean.getDateDebut());
                dateFin = jaCal.addDays(dateFin, -1);

                periode.setDateFin(dateFin.toStr("."));
                periode.update();
            }
        } catch (Exception e) {
            tiViewBean.setMessage(e.toString());
            tiViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
