/*
 * Créé le 2 mars 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.helpers.process;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.ij.itext.IJListeAttestationsNonRecus;
import globaz.ij.vb.process.IJListeFormulairesNonRecusViewBean;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRDateFormater;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * @author hpe
 * 
 */
public class IJListeFormulairesNonRecusHelper extends PRAbstractHelper {

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
        IJListeFormulairesNonRecusViewBean lfViewBean = (IJListeFormulairesNonRecusViewBean) viewBean;

        String annee = lfViewBean.getForAnnee();
        String mois = lfViewBean.getForMois();
        BSession bSession = (BSession) session;

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MONTH, Integer.parseInt(mois) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(annee));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        String dateFin = getDateFormatted(cal, bSession);

        cal.set(Calendar.DAY_OF_MONTH, 1);

        String dateDebut = getDateFormatted(cal, bSession);

        String mName = JACalendar.getMonthName(cal.get(Calendar.MONTH) + 1, bSession.getIdLangueISO());
        String aName = String.valueOf(cal.get(Calendar.YEAR));

        IJListeAttestationsNonRecus process = new IJListeAttestationsNonRecus(bSession, mName, aName);

        process.setDateDebutBaseIndemn(dateDebut);
        process.setDateFinBaseIndemn(dateFin);
        process.setDateDebutPrononce(dateDebut);
        process.setDateFinPrononce(dateFin);
        process.setDateDocument(lfViewBean.getDate());

        process.setEMailAddress(lfViewBean.getEMailAddress());
        process.setSendCompletionMail(true);

        process.setMois(mName);
        process.setAnnee(aName);

        process.setOrderBy(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM + ","
                + IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM);

        process.start();

    }

    /**
     * 
     * @param cal
     * @param bSession
     * @return
     */
    private String getDateFormatted(Calendar cal, BSession bSession) {
        DateFormat df = PRDateFormater.getDateFormatInstance(bSession, "dd.MM.yyyy");
        return df.format(cal.getTime());

    }
}
