package globaz.cygnus.services.adaptationJournaliere;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.api.adaptationsJournalieres.IRFAdaptationJournaliere;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.adaptationsJournalieres.RFRfmAccordeeJointDecisionJointQd;
import globaz.cygnus.db.adaptationsJournalieres.RFRfmAccordeeJointDecisionJointQdManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.qds.RFQdAugmentation;
import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RFAdaptationJournaliereSuppressionHandler extends RFAdaptationJournaliereAbstractHandler {

    public RFAdaptationJournaliereSuppressionHandler(RFAdaptationJournaliereContext context, BSession session,
            BTransaction transaction, List<String[]> logsList, String gestionnaire, boolean isAdaptationAnnuelle) {
        super(context, session, transaction, logsList, gestionnaire, isAdaptationAnnuelle);
    }

    private void diminuerPrestationsAccordee() throws Exception {

        // Diminution des paiements mensuels
        for (String[] idPrestAccCourante : getContext().getIdsPrestationAccordeeEnCours()) {

            RFPrestationAccordee rfPrestationAcc = new RFPrestationAccordee();
            rfPrestationAcc.setSession(getSession());
            rfPrestationAcc.setIdRFMAccordee(idPrestAccCourante[0]);

            rfPrestationAcc.retrieve();

            if (!rfPrestationAcc.isNew()) {

                REPrestationsAccordees rePrestationAccordee = new REPrestationsAccordees();
                rePrestationAccordee.setSession(getSession());
                rePrestationAccordee.setIdPrestationAccordee(idPrestAccCourante[0]);

                rePrestationAccordee.retrieve();

                if (!rePrestationAccordee.isNew()) {

                    rePrestationAccordee.setDateFinDroit(getContext().getDateDebutDecision());
                    rePrestationAccordee.update(getTransaction());

                    // rfPrestationAcc.setDateFin(this.getContext().getDateFin());
                    // rfPrestationAcc.update(this.getTransaction());

                    // Réattribution de la somme non payée, mais déjà imputée sur la Qd
                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession(getSession());

                    rfDemande.setIdDemande(idPrestAccCourante[1]);

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {

                        // Calcul du montant à restituer
                        BigDecimal montantMensuelBigDec = new BigDecimal(rfDemande.getMontantMensuel().replace("'", ""));

                        // Nombre de mois entre la date de fin de droit PC et la date de fin de la prestation
                        // accordée
                        String dateFinDeTraitement = "";
                        if (JadeStringUtil.isBlankOrZero(rfDemande.getDateFinTraitement())) {
                            dateFinDeTraitement = FIN_ANNEE_JJMM
                                    + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(rfDemande.getDateDebutTraitement());
                        } else {
                            dateFinDeTraitement = rfDemande.getDateDebutTraitement();
                        }

                        // TODO: a voir peut-être date de dernier paiement ??
                        int nbJoursDateFinDroitDateFinPrestation = JadeDateUtil.getNbMonthsBetween(getContext()
                                .getDateFinDecision(), dateFinDeTraitement);

                        BigDecimal montantARestituerBigDec = montantMensuelBigDec.multiply(new BigDecimal(
                                nbJoursDateFinDroitDateFinPrestation));

                        // calcule le nouvel id unique de famille de modification
                        int famModifCompteur;

                        RFQdAugmentationManager mgr = new RFQdAugmentationManager();
                        mgr.setSession(getSession());
                        mgr.setForIdFamilleMax(true);
                        mgr.changeManagerSize(0);
                        mgr.find();

                        if (!mgr.isEmpty()) {
                            RFQdAugmentation rfAugmentation = (RFQdAugmentation) mgr.getFirstEntity();
                            if (null != rfAugmentation) {
                                famModifCompteur = JadeStringUtil
                                        .parseInt(rfAugmentation.getIdFamilleModification(), 0) + 1;
                            } else {
                                famModifCompteur = 1;
                            }
                        } else {
                            famModifCompteur = 1;
                        }

                        RFQdAugmentation rfQdAug = new RFQdAugmentation();
                        rfQdAug.setSession(getSession());
                        rfQdAug.setVisaGestionnaire(getGestionnaire());
                        rfQdAug.setRemarque("");
                        rfQdAug.setConcerne(getSession().getLabel(
                                "PROCESS_ADAPTATION_JOURNALIERE_AJOUT_DIMUNUTION_PAIEMENT_MENSUEL"));
                        rfQdAug.setMontantAugmentationQd(montantARestituerBigDec.toString());

                        rfQdAug.setIdFamilleModification(Integer.toString(famModifCompteur));
                        rfQdAug.setIdQd(idPrestAccCourante[2]);
                        rfQdAug.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                        rfQdAug.setTypeModification(IRFQd.CS_AJOUT);
                        rfQdAug.setIdAugmentationQdModifiePar("");

                        rfQdAug.add(getTransaction());

                    } else {
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                getContext().getIdAdaptationJournaliere(),
                                getContext().getIdTiersBeneficiaire(),
                                getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(),
                                getContext().getNumeroDecisionPc(),
                                "RFAdaptationJournaliereSuppressionHandler.diminuerPrestationsAccordee() : Impossible de retrouver la demande du paiement mensuel "
                                        + idPrestAccCourante[0], getLogsList());
                    }

                } else {
                    getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                    RFUtils.ajouterLogAdaptation(
                            FWViewBeanInterface.ERROR,
                            getContext().getIdAdaptationJournaliere(),
                            getContext().getIdTiersBeneficiaire(),
                            getContext().getNssTiersBeneficiaire(),
                            getContext().getIdDecisionPc(),
                            getContext().getNumeroDecisionPc(),
                            "RFAdaptationJournaliereSuppressionHandler.diminuerPrestationsAccordee() : Impossible de retrouver la prestation accordée (rente) numéro "
                                    + idPrestAccCourante[0], getLogsList());
                }

            } else {
                getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                RFUtils.ajouterLogAdaptation(
                        FWViewBeanInterface.ERROR,
                        getContext().getIdAdaptationJournaliere(),
                        getContext().getIdTiersBeneficiaire(),
                        getContext().getNssTiersBeneficiaire(),
                        getContext().getIdDecisionPc(),
                        getContext().getNumeroDecisionPc(),
                        "RFAdaptationJournaliereSuppressionHandler.diminuerPrestationsAccordee() : Impossible de retrouver la prestation accordée (RFM) numéro "
                                + idPrestAccCourante[0], getLogsList());
            }

        }

    }

    @Override
    public RFAdaptationJournaliereContext executerAdaptation() throws Exception {

        // Une décision de suppression intervient seulement si le droit Pc s'arrête définitivement (changement de
        // canton, décès) ou si un changemement
        // dans la famille intervient, dans ce dernier cas une ou plusieurs décisions d'octrois suivent la décision de
        // suppresssion -> donc la décision de suppression doit entrainer la fermeture de toutes les Qds qui suivent la
        // date de fin

        if (!JadeStringUtil.isBlankOrZero(context.getDateDebutDecision())) {

            // Màj de la date de début (doit concerner le mois entier)
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            GregorianCalendar calendar = new GregorianCalendar();

            Date dateDebutDate = dateFormat.parse(getContext().getDateDebutDecision());
            calendar.setTime(dateDebutDate);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            dateDebutDate = calendar.getTime();
            context.setDateDebutDecision(dateFormat.format(dateDebutDate));

            Set<String> idsTiersMembreFamille = new HashSet<String>();
            idsTiersMembreFamille.add(getContext().getIdTiersBeneficiaire());

            rechercherPeriodesGrandesQd(idsTiersMembreFamille, true);

            majGrandesQd(getContext().getDateDebutDecision(), RFUtils.MAX_JADATE_VALUE, true);

            // this.rechercherPrestationsAccordees();
            // this.diminuerPrestationsAccordee();

        } else {

            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getIdTiersBeneficiaire(), getContext().getNssTiersBeneficiaire(), getContext()
                            .getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "Date de début de suppression vide", getLogsList());
        }

        return getContext();
    }

    private void rechercherPrestationsAccordees() throws Exception {

        if (!isContextEnErreur()) {

            // Recherche des prestations accordées en cours liées aux Qds
            for (RFAdaptationJournalierePeriodeQdData periodeCourante : context.getPeriodesQdExistantes()) {

                RFRfmAccordeeJointDecisionJointQdManager rfRfmAccJoiDecJoiQdMgr = new RFRfmAccordeeJointDecisionJointQdManager();
                rfRfmAccJoiDecJoiQdMgr.setSession(getSession());
                rfRfmAccJoiDecJoiQdMgr.setForIdQdPrincipale(periodeCourante.getId_qd());
                rfRfmAccJoiDecJoiQdMgr.changeManagerSize(0);
                rfRfmAccJoiDecJoiQdMgr.find();

                Iterator<RFRfmAccordeeJointDecisionJointQd> rfRfmAccJoiDecJoiQdItr = rfRfmAccJoiDecJoiQdMgr.iterator();

                while (rfRfmAccJoiDecJoiQdItr.hasNext()) {

                    RFRfmAccordeeJointDecisionJointQd rfRfmAccJoiDecJoiQd = rfRfmAccJoiDecJoiQdItr.next();

                    if (null != rfRfmAccJoiDecJoiQd) {

                        context.getIdsPrestationAccordeeEnCours().add(
                                new String[] { rfRfmAccJoiDecJoiQd.getIdRFMAccordee(),
                                        rfRfmAccJoiDecJoiQd.getIdDemande(),
                                        context.getPeriodesQdExistantes().get(0).getId_qd() });

                    } else {
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                getContext().getIdAdaptationJournaliere(),
                                getContext().getIdTiersBeneficiaire(),
                                getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(),
                                getContext().getNumeroDecisionPc(),
                                "RFAdaptationJournaliereSuppressionHandler.rechercherPrestationsAccordees() : Impossible de retrouver la prestation accordée liée à la Qd "
                                        + periodeCourante.getId_qd(), getLogsList());
                    }

                }
            }
        }

    }

}
