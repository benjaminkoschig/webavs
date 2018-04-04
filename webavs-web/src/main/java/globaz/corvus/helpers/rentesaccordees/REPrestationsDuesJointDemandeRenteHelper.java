/*
 * Créé le 12 juil. 07
 */

package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.parametrages.ReParametrageAPI;
import globaz.corvus.db.parametrages.ReParametrageAPIManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.module.calcul.api.REMontantReferenceAPI;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRStringUtils;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HPE
 * 
 */

public class REPrestationsDuesJointDemandeRenteHelper extends PRAbstractHelper {

    private static final String CDT_DATEDEBUT = "{DateDebutRente}";

    private static final String CDT_DATEFIN = "{DateFinRente}";
    private static final String CDT_PMTM = "{DatePmt}";
    List montantsAPI = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        REPrestationsDuesJointDemandeRenteViewBean vb = (REPrestationsDuesJointDemandeRenteViewBean) viewBean;

        // Test si la date de début paiement est plus petite que la date de fin
        // de paiement
        if (!JadeStringUtil.isEmpty(vb.getDateFinPaiement())) {

            JACalendarGregorian cal = new JACalendarGregorian();

            int resultat = cal.compare(vb.getDateDebutPaiement(), vb.getDateFinPaiement());

            if (JACalendar.COMPARE_SECONDLOWER == resultat) {
                throw new Exception(((BSession) session).getLabel("MSG_ERROR_DATEFIN_PLUSPETITE_DATEDEBUT"));
            }
        }

        RERenteAccordeeManager rdmanager = new RERenteAccordeeManager();
        rdmanager.setSession((BSession) session);
        rdmanager.setForIdRenteAccordee(vb.getIdRenteAccordee());
        rdmanager.find();

        if (!rdmanager.isEmpty()) {

            RERenteAccordee rd = (RERenteAccordee) rdmanager.getFirstEntity();

            JACalendarGregorian cal = new JACalendarGregorian();

            int resultat;
            // Tests si le date de début est plus petite que la date de début de
            // la rente accordée
            if (!JadeStringUtil.isEmpty(vb.getDateDebutPaiement())) {
                resultat = cal.compare(vb.getDateDebutPaiement(), rd.getDateDebutDroit());
                if (JACalendar.COMPARE_FIRSTLOWER == resultat) {
                    throw new Exception(PRStringUtils.replaceString(
                            ((BSession) session).getLabel("MSG_ERROR_DATEDEBUT_PLUSPETITE_DATEDEBUTRENTE"),
                            CDT_DATEDEBUT, rd.getDateDebutDroit()));
                }
            }
            // La date de fin de paiement peut être vide à condidtion que celle
            // de la rente accordée le soit aussi.
            if (JadeStringUtil.isEmpty(vb.getDateFinPaiement())) {
                if (!JadeStringUtil.isEmpty(rd.getDateFinDroit())) {
                    throw new Exception(PRStringUtils.replaceString(
                            ((BSession) session).getLabel("MSG_ERROR_DATEFIN_OBLIGATOIRE"), CDT_DATEFIN,
                            rd.getDateFinDroit()));
                }

            } else {
                // Si la date de fin de paiement est renseignée et que la date
                // de fin de la rente accordée aussi, la date de fin de paiement
                // ne peut être plus grande que la date de fin de droit de la
                // rente accordée
                if (!JadeStringUtil.isEmpty(rd.getDateFinDroit())) {
                    resultat = cal.compare(vb.getDateFinPaiement(), rd.getDateFinDroit());
                    if (JACalendar.COMPARE_FIRSTUPPER == resultat) {
                        throw new Exception(PRStringUtils.replaceString(
                                ((BSession) session).getLabel("MSG_ERROR_DATEFIN_PAIEMENT"), CDT_DATEFIN,
                                rd.getDateFinDroit()));
                    }
                } else {
                    resultat = cal.compare(rd.getDateDebutDroit(), REPmtMensuel.getDateDernierPmt((BSession) session));
                    if (JACalendar.COMPARE_FIRSTLOWER == resultat || JACalendar.COMPARE_EQUALS == resultat) {
                        resultat = cal.compare(vb.getDateFinPaiement(),
                                REPmtMensuel.getDateDernierPmt((BSession) session));
                        if (JACalendar.COMPARE_FIRSTUPPER == resultat) {
                            throw new Exception(PRStringUtils.replaceString(
                                    ((BSession) session).getLabel("MSG_ERROR_DATEFIN_PLUSGRANDE_PMTM"), CDT_PMTM,
                                    REPmtMensuel.getDateDernierPmt((BSession) session)));
                        }
                    }
                }
            }
        }

        if (!vb.getIsWarningAffiche().equals("false")) {

            // Faire le test et si msg a afficher, break de la méthode.
            if (plausiMontantAnneeFin1(vb, (BSession) session)) {
                vb.setIsWarningAffiche("true");
                vb.setMessage(((BSession) session).getLabel("MSG_ERROR_PLAUSI"));
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (plausiMontantAnneeFin2(vb, (BSession) session)) {
                vb.setIsWarningAffiche("true");
                vb.setMessage(((BSession) session).getLabel("MSG_ERROR_PLAUSI"));
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (!vb.hasErrors()) {
                // creation de la prestation due
                REPrestationDue pd = new REPrestationDue();
                pd.setSession((BSession) session);
                pd.setIdRenteAccordee(vb.getIdRenteAccordee());
                pd.setIdInteretMoratoire(vb.getIdInteretMoratoire());
                pd.setDateDebutPaiement(vb.getDateDebutPaiement());
                pd.setDateFinPaiement(vb.getDateFinPaiement());
                pd.setRam(vb.getRam());
                pd.setMontant(vb.getMontant());
                pd.setCsEtat(vb.getCsEtat());
                pd.setCsType(vb.getCsType());
                pd.setMontantReductionAnticipation(vb.getMontantReductionAnticipation());
                pd.setMontantSupplementAjournement(vb.getMontantSupplementAjournement());

                pd.add();
            }

        } else {
            // creation de la prestation due
            REPrestationDue pd = new REPrestationDue();
            pd.setSession((BSession) session);
            pd.setIdRenteAccordee(vb.getIdRenteAccordee());
            pd.setIdInteretMoratoire(vb.getIdInteretMoratoire());
            pd.setDateDebutPaiement(vb.getDateDebutPaiement());
            pd.setDateFinPaiement(vb.getDateFinPaiement());
            pd.setRam(vb.getRam());
            pd.setMontant(vb.getMontant());
            pd.setCsEtat(vb.getCsEtat());
            pd.setCsType(vb.getCsType());
            pd.setMontantReductionAnticipation(vb.getMontantReductionAnticipation());
            pd.setMontantSupplementAjournement(vb.getMontantSupplementAjournement());

            pd.add();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = ((BSession) session).newTransaction();

        try {
            transaction.openTransaction();

            REPrestationsDuesJointDemandeRenteViewBean vb = (REPrestationsDuesJointDemandeRenteViewBean) viewBean;

            REPrestationDue pd = new REPrestationDue();
            pd.setSession((BSession) session);
            pd.setIdPrestationDue(vb.getIdPrestationDue());
            pd.retrieve(transaction);

            REDeleteCascadeDemandeAPrestationsDues.supprimerPrestationDueCascade_noCommit((BSession) session,
                    transaction, pd, IREValidationLevel.VALIDATION_LEVEL_NONE);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        REPrestationsDuesJointDemandeRenteViewBean vb = (REPrestationsDuesJointDemandeRenteViewBean) viewBean;

        // Test si la date de début paiement est plus petite que la date de fin
        // de paiement
        if (!JadeStringUtil.isEmpty(vb.getDateFinPaiement())) {

            JACalendarGregorian cal = new JACalendarGregorian();

            int resultat = cal.compare(vb.getDateDebutPaiement(), vb.getDateFinPaiement());

            if (JACalendar.COMPARE_SECONDLOWER == resultat) {
                throw new Exception(((BSession) session).getLabel("MSG_ERROR_DATEFIN_PLUSPETITE_DATEDEBUT"));
            }
        }

        // validate : La date de fin de la dernière période (prestation due)
        // AI/API doit être vide, sauf si elle
        // est inférieure ou égale à la date du dernier pmt

        if (vb.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)
                || vb.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

            if (!JadeStringUtil.isBlankOrZero(vb.getDateFinPaiement())) {

                // dateFin = mm.aaaa
                // dateDernierPmt =

                JACalendar cal = new JACalendarGregorian();

                if (cal.compare(new JADate(vb.getDateFinPaiement()),
                        new JADate(REPmtMensuel.getDateDernierPmt((BSession) session))) == JACalendar.COMPARE_SECONDLOWER) {

                    throw new Exception(((BSession) session).getLabel("MSG_ERROR_DATE"));

                }

            }

        }

        RERenteAccordeeManager rdmanager = new RERenteAccordeeManager();
        rdmanager.setSession((BSession) session);
        rdmanager.setForIdRenteAccordee(vb.getIdRenteAccordee());
        rdmanager.find();

        if (!rdmanager.isEmpty()) {

            RERenteAccordee rd = (RERenteAccordee) rdmanager.getFirstEntity();

            JACalendarGregorian cal = new JACalendarGregorian();

            int resultat;
            // Tests si le date de début est plus petite que la date de début de
            // la rente accordée
            if (!JadeStringUtil.isEmpty(vb.getDateDebutPaiement())) {
                resultat = cal.compare(vb.getDateDebutPaiement(), rd.getDateDebutDroit());
                if (JACalendar.COMPARE_FIRSTLOWER == resultat) {
                    throw new Exception(PRStringUtils.replaceString(
                            ((BSession) session).getLabel("MSG_ERROR_DATEDEBUT_PLUSPETITE_DATEDEBUTRENTE"),
                            CDT_DATEDEBUT, rd.getDateDebutDroit()));
                }
            }
            // La date de fin de paiement peut être vide à condidtion que celle
            // de la rente accordée le soit aussi.
            if (JadeStringUtil.isEmpty(vb.getDateFinPaiement())) {
                if (!JadeStringUtil.isEmpty(rd.getDateFinDroit())) {
                    throw new Exception(PRStringUtils.replaceString(
                            ((BSession) session).getLabel("MSG_ERROR_DATEFIN_OBLIGATOIRE"), CDT_DATEFIN,
                            rd.getDateFinDroit()));
                }

            } else {
                // Si la date de fin de paiement est renseignée et que la date
                // de fin de la rente accordée aussi, la date de fin de paiement
                // ne peut être plus grande que la date de fin de droit de la
                // rente accordée
                if (!JadeStringUtil.isEmpty(rd.getDateFinDroit())) {
                    resultat = cal.compare(vb.getDateFinPaiement(), rd.getDateFinDroit());
                    if (JACalendar.COMPARE_FIRSTUPPER == resultat) {
                        throw new Exception(PRStringUtils.replaceString(
                                ((BSession) session).getLabel("MSG_ERROR_DATEFIN_PAIEMENT"), CDT_DATEFIN,
                                rd.getDateFinDroit()));
                    }
                } else {
                    resultat = cal.compare(rd.getDateDebutDroit(), REPmtMensuel.getDateDernierPmt((BSession) session));
                    if (JACalendar.COMPARE_FIRSTLOWER == resultat || JACalendar.COMPARE_EQUALS == resultat) {
                        resultat = cal.compare(vb.getDateFinPaiement(),
                                REPmtMensuel.getDateDernierPmt((BSession) session));
                        if (JACalendar.COMPARE_FIRSTUPPER == resultat) {
                            throw new Exception(PRStringUtils.replaceString(
                                    ((BSession) session).getLabel("MSG_ERROR_DATEFIN_PLUSGRANDE_PMTM"), CDT_PMTM,
                                    REPmtMensuel.getDateDernierPmt((BSession) session)));
                        }
                    }
                }
            }
        }

        if (!vb.getIsWarningAffiche().equals("false")) {

            // Faire le test et si msg a afficher, break de la méthode.
            if (plausiMontantAnneeFin1(vb, (BSession) session)) {
                vb.setIsWarningAffiche("true");
                vb.setMessage(((BSession) session).getLabel("MSG_ERROR_PLAUSI"));
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (plausiMontantAnneeFin2(vb, (BSession) session)) {
                vb.setIsWarningAffiche("true");
                vb.setMessage(((BSession) session).getLabel("MSG_ERROR_PLAUSI"));
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (!vb.hasErrors()) {
                // on retrouve la prestation due
                REPrestationDue pd = new REPrestationDue();
                pd.setSession((BSession) session);
                pd.setIdPrestationDue(vb.getIdPrestationDue());
                pd.retrieve();

                pd.setIdRenteAccordee(vb.getIdRenteAccordee());
                pd.setIdInteretMoratoire(vb.getIdInteretMoratoire());
                pd.setDateDebutPaiement(vb.getDateDebutPaiement());
                pd.setDateFinPaiement(vb.getDateFinPaiement());
                pd.setRam(vb.getRam());
                pd.setMontant(vb.getMontant());
                pd.setCsEtat(vb.getCsEtat());
                pd.setCsType(vb.getCsType());
                pd.setMontantReductionAnticipation(vb.getMontantReductionAnticipation());
                pd.setMontantSupplementAjournement(vb.getMontantSupplementAjournement());

                pd.update();
            }

        } else {
            // on retrouve la prestation due
            REPrestationDue pd = new REPrestationDue();
            pd.setSession((BSession) session);
            pd.setIdPrestationDue(vb.getIdPrestationDue());
            pd.retrieve();

            pd.setIdRenteAccordee(vb.getIdRenteAccordee());
            pd.setIdInteretMoratoire(vb.getIdInteretMoratoire());
            pd.setDateDebutPaiement(vb.getDateDebutPaiement());
            pd.setDateFinPaiement(vb.getDateFinPaiement());
            pd.setRam(vb.getRam());
            pd.setMontant(vb.getMontant());
            pd.setCsEtat(vb.getCsEtat());
            pd.setCsType(vb.getCsType());
            pd.setMontantReductionAnticipation(vb.getMontantReductionAnticipation());
            pd.setMontantSupplementAjournement(vb.getMontantSupplementAjournement());

            pd.update();
        }

    }

    private REMontantReferenceAPI getMontantAPI(JADate date, BSession session) throws Exception {
        int year = date.getYear();

        for (Iterator iter = montantsAPI.iterator(); iter.hasNext();) {
            REMontantReferenceAPI element = (REMontantReferenceAPI) iter.next();
            if (year == element.getAnnee()) {
                return element;
            }
        }
        throw new Exception(session.getLabel("ERREUR_AUNCUN_MNT_API_CORR_DATE") + date);
    }

    private int loadMontantsAPI(BSession session) throws Exception {

        int result = -1;
        if (montantsAPI != null) {
            return result;
        } else {

            montantsAPI = new LinkedList();

            ReParametrageAPIManager mgr = new ReParametrageAPIManager();

            mgr.setSession(session);

            mgr.find(BManager.SIZE_NOLIMIT);

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {

                ReParametrageAPI element = (ReParametrageAPI) iter.next();

                int yyyyDebut = (new JADate(element.getDateDebut())).getYear();
                int yyyyFin = (new JADate(element.getDateFin())).getYear();

                if (result < yyyyFin) {
                    result = yyyyFin;
                }

                if (yyyyDebut == yyyyFin) {
                    REMontantReferenceAPI mntAPI = new REMontantReferenceAPI(yyyyDebut, new BigDecimal(
                            element.getMontantApiMin()), new BigDecimal(element.getMontantApiMax()));
                    montantsAPI.add(mntAPI);
                } else {
                    for (int i = yyyyDebut; i <= yyyyFin; i++) {
                        REMontantReferenceAPI mntAPI = new REMontantReferenceAPI(i, new BigDecimal(
                                element.getMontantApiMin()), new BigDecimal(element.getMontantApiMax()));
                        montantsAPI.add(mntAPI);
                    }

                }
            }
            return result;
        }
    }

    /**
     * 
     * @param
     * @return
     * @throws Exception
     */
    private boolean plausiMontantAnneeFin1(REPrestationsDuesJointDemandeRenteViewBean vb, BSession session)
            throws Exception {

        // si type = prst. mensuelle
        if (vb.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {

            // Retrieve du montant max de l'année de la prestation due
            JADate date;

            // si pas de date de fin, prendre l'année en cours
            if (JadeStringUtil.isBlankOrZero(vb.getDateFinPaiement())) {
                date = new JADate(REPmtMensuel.getDateDernierPmt(session));
            } else {
                date = new JADate(vb.getDateFinPaiement());
            }

            loadMontantsAPI(session);

            REMontantReferenceAPI montant = getMontantAPI(date, session);

            FWCurrency montantMax = new FWCurrency(montant.getRenteMaximale().doubleValue());
            FWCurrency montantPD = new FWCurrency(vb.getMontant());

            montantMax = new FWCurrency(montantMax.doubleValue() * 1.3);

            // si montantPD > "montant max de année de fin" *1.3
            if (montantPD.doubleValue() > montantMax.doubleValue()) {
                return true;
            }

        }

        return false;
    }

    /**
     * 
     * @param
     * @return
     * @throws Exception
     */
    private boolean plausiMontantAnneeFin2(REPrestationsDuesJointDemandeRenteViewBean vb, BSession session)
            throws Exception {

        // if demande de rente de type vieillesse
        if (vb.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

            // si code ajournement dans demande de rente
            REDemandeRenteVieillesse demande = new REDemandeRenteVieillesse();
            demande.setSession(session);
            demande.setIdDemandeRente(vb.getNoDemandeRente());
            demande.retrieve();

            if (!demande.isNew()) {

                if (demande.getIsAjournementRequerant().booleanValue()) {

                    // Retrieve du montant max de l'année de la prestation due
                    JADate date;

                    // si pas de date de fin, prendre l'année en cours
                    if (JadeStringUtil.isBlankOrZero(vb.getDateFinPaiement())) {
                        date = new JADate(REPmtMensuel.getDateDernierPmt(session));
                    } else {
                        date = new JADate(vb.getDateFinPaiement());
                    }

                    loadMontantsAPI(session);

                    REMontantReferenceAPI montant = getMontantAPI(date, session);

                    FWCurrency montantMax = new FWCurrency(montant.getRenteMaximale().doubleValue());
                    FWCurrency montantPD = new FWCurrency(vb.getMontant());

                    montantMax = new FWCurrency(montantMax.doubleValue() * 1.5);

                    // si montantPD > "montant max de année de fin" *1.5
                    if (montantPD.doubleValue() > montantMax.doubleValue()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
