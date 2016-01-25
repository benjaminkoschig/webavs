package globaz.ij.helpers.process;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.ij.process.IJGenererFormulairesProcess;
import globaz.ij.vb.process.IJGenererFormulairesViewBean;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRDateFormater;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJGenererFormulairesHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        super._retrieve(viewBean, action, session);
        if (JadeGedFacade.isInstalled()) {
            List l = JadeGedFacade.getDocumentNamesList();
            for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                String s = (String) iterator.next();
                if (s != null && s.startsWith(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ)) {
                    ((IJGenererFormulairesViewBean) viewBean).setDisplaySendToGed("1");
                    break;
                } else {
                    ((IJGenererFormulairesViewBean) viewBean).setDisplaySendToGed("0");
                }
            }
        }
    }

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
        IJGenererFormulairesViewBean gfViewBean = (IJGenererFormulairesViewBean) viewBean;

        // la date de fin
        String annee = gfViewBean.getForAnneeFin();
        String mois = gfViewBean.getForMoisFin();
        BSession bSession = (BSession) session;
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MONTH, Integer.parseInt(mois) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(annee));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        String dateFin = getDateFormatted(cal, bSession);

        // la date de debut
        annee = gfViewBean.getForAnneeDebut();
        mois = gfViewBean.getForMoisDebut();

        cal.set(Calendar.MONTH, Integer.parseInt(mois) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(annee));
        cal.set(Calendar.DAY_OF_MONTH, 1);

        String dateDebut = getDateFormatted(cal, bSession);

        IJGenererFormulairesProcess process = new IJGenererFormulairesProcess(bSession);
        process.setDateDebut(dateDebut);
        process.setDateFin(dateFin);
        process.setGenererFormulaires(gfViewBean.getGenererFormulaires());
        process.setImprimerFormulaires(gfViewBean.getImprimerFormulaires());
        process.setDateSurDocument(gfViewBean.getDate());
        process.setEMailAddress(gfViewBean.getEMailAddress());
        process.setSendCompletionMail(true);
        process.setMois(JACalendar.getMonthName(cal.get(Calendar.MONTH) + 1, bSession.getIdLangue()));
        process.setAnnee(String.valueOf(cal.get(Calendar.YEAR)));
        process.setAnneeDebut(gfViewBean.getForAnneeDebut());
        process.setAnneeFin(gfViewBean.getForAnneeFin());
        process.setMoisDebut(gfViewBean.getForMoisDebut());
        process.setMoisFin(gfViewBean.getForMoisFin());
        process.setDateRetour(gfViewBean.getDateRetour());
        process.setImpressionFomulairesForEtat(gfViewBean.getImpressionFomulairesForEtat());
        process.setForIdPrononce(gfViewBean.getForIdPrononce());
        process.setIsSendToGed(gfViewBean.getIsSendToGed());

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
