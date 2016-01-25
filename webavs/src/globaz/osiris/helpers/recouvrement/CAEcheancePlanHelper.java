package globaz.osiris.helpers.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.utils.CASursisPaiementEcheancier;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author ado Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAEcheancePlanHelper extends FWHelper {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_find(java.lang.Object, globaz.framework.controller.FWAction,
     * globaz.globall.api.BISession)
     */
    @Override
    protected void _find(Object viewBean, FWAction action, BISession session) throws Exception {
        CAPlanRecouvrement pr = (CAPlanRecouvrement) viewBean;
        computeEcheancier(pr, (BSession) session);
    }

    /**
     * Ajoute une échéance.
     * 
     * @param session
     * @param recouvrement
     * @param acompte
     * @param echeances
     * @param date
     * @return
     * @throws Exception
     */
    private JADate addEcheances(BSession session, CAPlanRecouvrement recouvrement, float acompte, Vector echeances,
            JADate date) throws Exception {
        CAEcheancePlan echeance = new CAEcheancePlan();
        echeance.setSession(session);
        echeance.setIdPlanRecouvrement(recouvrement.getIdPlanRecouvrement());

        date = CASursisPaiementEcheancier.getNextDateExigibilite(session, date, echeances.size(), recouvrement);
        echeance.setDateExigibilite(date.toStr("."));

        echeance.setMontant("" + round(acompte, 2));

        echeances.add(echeance);

        return date;
    }

    /**
     * Ajoute une dernière échéance pour solder le plan.
     * 
     * @param session
     * @param recouvrement
     * @param cumul
     * @param echeances
     * @param date
     * @throws Exception
     */
    private void addLastEcheance(BSession session, CAPlanRecouvrement recouvrement, float cumul, Vector echeances,
            JADate date) throws Exception {
        CAEcheancePlan echeance = new CAEcheancePlan();
        echeance.setSession(session);
        echeance.setIdPlanRecouvrement(recouvrement.getIdPlanRecouvrement());

        date = CASursisPaiementEcheancier.addToCalendar(session, date, recouvrement, echeances.size() + 1);
        date = CASursisPaiementEcheancier.getNextDateExigibilite(session, date, 0, recouvrement);
        echeance.setDateExigibilite(date.toStr("."));
        echeance.setMontant("" + round(cumul, 2));

        echeances.add(echeance);
    }

    /**
     * Ajout premier accompte sur la première date ouvrable.
     * 
     * @param session
     * @param recouvrement
     * @param echeances
     * @param date
     * @return
     * @throws Exception
     */
    private JADate addPremierAcompte(BSession session, CAPlanRecouvrement recouvrement, Vector echeances, JADate date)
            throws Exception {
        if (Float.parseFloat(recouvrement.getPremierAcompte()) > 0) {
            CAEcheancePlan echeance = new CAEcheancePlan();
            echeance.setSession(session);
            echeance.setIdPlanRecouvrement(recouvrement.getIdPlanRecouvrement());
            echeance.setMontant(recouvrement.getPremierAcompte());

            date = CASursisPaiementEcheancier.getNextDateExigibilite(session, date, 0, recouvrement);
            echeance.setDateExigibilite(date.toStr("."));

            echeances.add(echeance);
        }
        return date;
    }

    /**
     * @param recouvrement
     * @param cumul
     * @param acompte
     * @param nbEcheances
     * @param derniereEcheance
     */
    private Vector compute(BSession session, CAPlanRecouvrement recouvrement, float cumul, float acompte,
            int nbEcheances) throws Exception {
        Vector echeances = new Vector();

        JADate date = new JADate(recouvrement.getDateEcheance());

        date = addPremierAcompte(session, recouvrement, echeances, date);
        date.setDay(new JADate(recouvrement.getDateEcheance()).getDay());

        while (nbEcheances > 0) {
            nbEcheances--;
            cumul = cumul - acompte;

            date = addEcheances(session, recouvrement, acompte, echeances, date);

            if (!CAPlanRecouvrement.CS_ECHEANCE_HEBDOMADAIRE.equals(recouvrement.getIdTypeEcheance())
                    && !CAPlanRecouvrement.CS_ECHEANCE_QUINZAINE.equals(recouvrement.getIdTypeEcheance())) {
                date.setDay(new JADate(recouvrement.getDateEcheance()).getDay());
            }
        }

        if (cumul > 0) {
            addLastEcheance(session, recouvrement, cumul, echeances, date);
        }

        return echeances;
    }

    /**
     * @param recouvrement
     * @param session
     */
    private void computeEcheancier(CAPlanRecouvrement recouvrement, BSession session) {
        if (recouvrement.hasEcheanceAuto()) {
            float cumul = Float.parseFloat(recouvrement.getCumulSoldeSections());

            // je vérifie qu'il n'y a pas un montant maximum
            float montantPlafond = Float.valueOf(recouvrement.getPlafond()).floatValue();
            if ((cumul > montantPlafond) && (montantPlafond > 0)) {
                cumul = montantPlafond;
            }

            // on enlève le premier accompte
            cumul = cumul - Float.parseFloat(recouvrement.getPremierAcompte());

            // ensuite, pour chaque accompte, on calcule le nombre des échéances
            float acompte = Float.parseFloat(recouvrement.getAcompte());
            Float nbEcheancesF = new Float(cumul / acompte);
            int nbEcheances = nbEcheancesF.intValue(); // nbre d'échéances

            // on fait le calcul des échéances
            try {
                recouvrement.setTempEcheances(compute(session, recouvrement, cumul, acompte, nbEcheances));
            } catch (Exception e) {
                session.addError(e.getMessage());
            }
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CAPlanRecouvrement pr = (CAPlanRecouvrement) viewBean;
        if ("calculerSave".equals(action.getActionPart())) {
            computeEcheancier(pr, (BSession) session);

            Vector vec = pr.getTempEcheances();
            for (Iterator iter = vec.iterator(); iter.hasNext();) {
                CAEcheancePlan element = (CAEcheancePlan) iter.next();
                try {
                    element.add();
                } catch (Exception e) {
                    ((BSession) session).addError(e.getMessage());
                }
            }
        }
        return pr;
    }

    /**
     * @param value
     * @param decimalPlace
     * @return
     */
    private float round(float value, int decimalPlace) {
        float power_of_ten = 1;
        while (decimalPlace-- > 0) {
            power_of_ten *= 10.0;
        }
        return Math.round(value * power_of_ten) / power_of_ten;
    }

}
