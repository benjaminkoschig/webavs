/*
 * Créé le 6 août 07
 */
package globaz.corvus.helpers.interetsmoratoires;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.creances.IRECreancier;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoireManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean;
import globaz.corvus.vb.interetsmoratoires.REPreparationInteretMoratoireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;

/**
 * @author BSC
 * 
 */
public class RECalculInteretMoratoireHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Preparation pour le calcul des interets moratoires. Pour chaque rente accordee: - calcul de la dette envers des
     * tiers - calcul du montant retroactif
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface calculerInteretMoratoire(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        REPreparationInteretMoratoireViewBean pimViewBean = (REPreparationInteretMoratoireViewBean) viewBean;
        RECalculInteretMoratoireViewBean cimViewBean = new RECalculInteretMoratoireViewBean();
        cimViewBean.setSession(pimViewBean.getSession());
        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            // on supprime les interets moratoires pour cette demande de rente
            // les calculs d'interets moratoires pour cette demande de rente et
            // le champs
            // idCalculInteretMaratoire des rentes accordees de la demande de
            // rente sont gerer
            // dans les classes parentes
            REInteretMoratoireManager imManager = new REInteretMoratoireManager();
            imManager.setSession(session);
            imManager.setForIdDemandeRente(pimViewBean.getIdDemandeRente());
            imManager.find();
            for (int i = 0; i < imManager.size(); i++) {
                REInteretMoratoire im = (REInteretMoratoire) imManager.getEntity(i);
                im.retrieve(transaction);
                im.delete(transaction);
            }

            // /////////////////////////////////////////////////////////////////////////////////////////////
            // Preparation pour le calcul des interets moratoires
            // /////////////////////////////////////////////////////////////////////////////////////////////
            RERenteAccJoinTblTiersJoinDemRenteManager rajdrManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
            rajdrManager.setSession(session);
            rajdrManager.setForNoDemandeRente(pimViewBean.getIdDemandeRente());
            rajdrManager.setFromDateDebutDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(pimViewBean
                    .getDateDecision()));

            // date prochaine facturation+1

            rajdrManager.find(transaction);

            // pour toutes les rentes accordees
            for (int i = 0; i < rajdrManager.size(); i++) {
                RERenteAccJoinTblTiersJoinDemandeRente rajdr = (RERenteAccJoinTblTiersJoinDemandeRente) rajdrManager
                        .getEntity(i);

                RECalculInteretMoratoire cim = new RECalculInteretMoratoire();
                cim.setSession(session);
                cim.setDateDebut(rajdr.getDateDebutDroit());
                cim.setDateFin(rajdr.getDateFinDroit());
                cim.setIdTiers(rajdr.getIdTiersBeneficiaire());

                // On cherche le montant retroactif pour cette rente accordee en
                // additionnant
                // les differentes periodes $P de la rente accordee.
                REPrestationsDuesManager pdManager = new REPrestationsDuesManager();
                pdManager.setSession(session);
                pdManager.setForIdRenteAccordes(rajdr.getIdPrestationAccordee());
                pdManager.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                pdManager.find(transaction);
                FWCurrency totalRetro = new FWCurrency("0.0");
                for (int p = 0; p < pdManager.size(); p++) {
                    REPrestationDue pd = (REPrestationDue) pdManager.getEntity(p);

                    JADate dateDebut = new JADate(pd.getDateDebutPaiement());

                    // on met la date de fin au dernier jour du moi
                    JADate dateFin = new JADate(pd.getDateFinPaiement());
                    JACalendar cal = new JACalendarGregorian();
                    dateFin = cal.addMonths(dateFin, 1);
                    dateFin = cal.addDays(dateFin, -1);

                    // si la date de fin est plus grande que la date de decision
                    // on modifie la date de fin au mois qui precede celui de la
                    // decision
                    JADate dateDecision = new JADate(pimViewBean.getDateDecision());
                    if (JadeStringUtil.isEmpty(pd.getDateFinPaiement())
                            || BSessionUtil.compareDateFirstGreaterOrEqual(session, dateFin.toStr("."),
                                    dateDecision.toStr("."))) {
                        dateFin = dateDecision;
                        if (dateDecision.getMonth() == 1) {
                            dateFin.setYear(dateFin.getYear() - 1);
                            dateFin.setMonth(12);
                        } else {
                            dateFin.setMonth(dateFin.getMonth() - 1);
                        }

                        // on met a jours la date de fin
                        cim.setDateFin(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateFin.toStr(".")));
                    }

                    int nbMois = getNombreDeMoisEntre(dateDebut, dateFin);
                    if (nbMois < 0) {
                        nbMois = 0;
                    }

                    BigDecimal retro = new BigDecimal(pd.getMontant());
                    retro = retro.multiply(new BigDecimal(nbMois));
                    totalRetro.add(retro.doubleValue());
                }
                cim.setMontantRetro(totalRetro.toString());

                // on cherche les dettes envers des tiers
                RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
                caManager.setSession(session);
                caManager.setForIdRenteAccordee(rajdr.getIdPrestationAccordee());
                caManager.setForCsTypeCreancierNotEqual(IRECreancier.CS_IMPOT_SOURCE);
                caManager.find(transaction);
                FWCurrency totalDette = new FWCurrency("0.0");
                for (int j = 0; j < caManager.size(); j++) {
                    RECreanceAccordee ca = (RECreanceAccordee) caManager.getEntity(j);
                    totalDette.add(ca.getMontant());
                }

                // on recherche les rentes versées à tort
                RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();
                renteVerseeATortManager.setSession(session);
                renteVerseeATortManager.setForIdRenteNouveauDroit(Long.parseLong(rajdr.getIdPrestationAccordee()));
                renteVerseeATortManager.find();
                for (RERenteVerseeATort uneRenteVerseeATort : renteVerseeATortManager.getContainerAsList()) {
                    totalDette.add(uneRenteVerseeATort.getMontant().toString());
                }

                if (totalDette.compareTo(totalRetro) > 0) {
                    cim.setMontantDette(totalRetro.toString());
                } else {
                    cim.setMontantDette(totalDette.toString());
                }
                cim.add(transaction);

                // mise a jours de la rente accodee
                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(session);
                ra.setIdPrestationAccordee(rajdr.getIdPrestationAccordee());
                ra.retrieve(transaction);
                ra.setIdCalculInteretMoratoire(cim.getIdCalculInteretMoratoire());
                ra.update(transaction);
            }

            // on set le viewBean pour le RC
            cimViewBean.setIdDemandeRente(pimViewBean.getIdDemandeRente());
            cimViewBean.setIdTiersDemandeRente(pimViewBean.getIdTierRequerant());
            cimViewBean.setDateDebutDroit(pimViewBean.getDateDebutDroit());
            cimViewBean.setDateDecision(pimViewBean.getDateDecision());
            cimViewBean.setDateDepotDemande(pimViewBean.getDateDepotDemande());
            cimViewBean.setDecisionDepuis(pimViewBean.getDecisionDepuis());

            if (transaction.hasErrors()) {
                transaction.rollback();
                cimViewBean.setMessage(transaction.getErrors().toString());
                cimViewBean.setMsgType(FWViewBeanInterface.ERROR);
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            cimViewBean.setMessage(transaction.getErrors().toString());
            cimViewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

        return cimViewBean;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * @param date1
     * @param date2
     * @return le nombre de mois en 2 dates. Condition : date1 antérieure à date2
     */
    private int getNombreDeMoisEntre(JADate date1, JADate date2) {
        int diffAnnee = date2.getYear() - date1.getYear();

        if (diffAnnee == 0) {
            return (date2.getMonth() - date1.getMonth()) + 1;
        } else if (diffAnnee == 1) {
            return ((12 - date1.getMonth()) + 1) + date2.getMonth();
        } else {
            return ((diffAnnee - 1) * 12) + (((12 - date1.getMonth()) + 1) + date2.getMonth());
        }
    }

}
