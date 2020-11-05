package globaz.cygnus.services.adaptationJournaliere;

import ch.globaz.pegasus.business.constantes.EPCProperties;
import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.adaptationsJournalieres.IRFAdaptationJournaliere;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiers;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdJointDossier;
import globaz.cygnus.db.qds.RFQdJointDossierManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenu;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.services.RFRetrieveInfoDroitPCService;
import globaz.cygnus.services.RFRetrieveInfoDroitPCServiceData;
import globaz.cygnus.services.RFRetrievePotQdPrincipaleInfoDroitPcService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

public class RFAdaptationJournaliereOctroiHandler extends RFAdaptationJournaliereAbstractHandler {

    public RFAdaptationJournaliereOctroiHandler(RFAdaptationJournaliereContext context, BSession session,
            BTransaction transaction, List<String[]> logsList, String gestionnaire, boolean isAdaptationAnnuelle) {
        super(context, session, transaction, logsList, gestionnaire, isAdaptationAnnuelle);
    }

    private boolean ajouterPeriodePcAccordeeNouvelleQd() throws Exception {

        if (validerAjouterGrandeQd()) {

            // RFQd
            RFQd qd = new RFQd();
            qd.setSession(getSession());
            qd.setLimiteAnnuelle(getContext().getLimiteAnnuelleGrandeQd());
            qd.setIsPlafonnee(true);
            qd.setDateCreation(JACalendar.todayJJsMMsAAAA());
            qd.setAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(getContext().getInfoDroitPcServiceData()
                    .getDateDebutPcaccordee()));
            qd.setCsGenreQd(IRFQd.CS_GRANDE_QD);

            if (!isAdaptationAnnuelle) {
                qd.setCsEtat(IRFQd.CS_ETAT_QD_FERME);
                qd.setCsSource(IRFQd.CS_SOURCE_QD_SYSTEME);
            } else {
                qd.setCsEtat(IRFQd.CS_ETAT_QD_OUVERT);
                qd.setCsSource(IRFQd.CS_SOURCE_QD_ADAPTATION);
            }

            qd.setIdGestionnaire(getGestionnaire());

            qd.add(getTransaction());

            // RFQdPrincipale
            RFQdPrincipale qdPrincipale = new RFQdPrincipale();
            qdPrincipale.setSession(getSession());

            qdPrincipale.setCsGenrePCAccordee(getContext().getInfoDroitPcServiceData().getGenrePc());
            qdPrincipale.setCsTypePCAccordee(getContext().getInfoDroitPcServiceData().getTypePc());
            qdPrincipale.setCsTypeBeneficiaire(getContext().getInfoDroitPcServiceData().getTypeBeneficiaire());
            qdPrincipale.setDateDebutPCAccordee(getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee());
            qdPrincipale.setDateFinPCAccordee(getContext().getInfoDroitPcServiceData().getDateFinPcaccordee());
            qdPrincipale.setExcedentPCAccordee(getContext().getInfoDroitPcServiceData().getSoldeExcedent());
            qdPrincipale.setIdQdPrincipale(qd.getIdQd());
            qdPrincipale.setIdPotDroitPc(getContext().getIdPotGrandeQd());
            qdPrincipale.setIsRI(getContext().getInfoDroitPcServiceData().isRi());

            qdPrincipale.setRemboursementRequerant(getContext().getTypeRemboursementRequerant());
            qdPrincipale.setRemboursementConjoint(getContext().getTypeRemboursementConjoint());

            qdPrincipale.setCsDegreApi(getContext().getInfoDroitPcServiceData().getCsDegreApi());

            qdPrincipale.add(getTransaction());

            // Création de la période de validité

            // calcule le nouvel id unique de famille de modification
            int famModifCompteur = 0;
            RFPeriodeValiditeQdPrincipaleManager mgr = new RFPeriodeValiditeQdPrincipaleManager();
            mgr.setSession(getSession());
            mgr.setForIdFamilleMax(true);
            mgr.changeManagerSize(0);
            mgr.find(transaction);

            if (!mgr.isEmpty()) {
                RFPeriodeValiditeQdPrincipale pv = (RFPeriodeValiditeQdPrincipale) mgr.getFirstEntity();
                if (null != pv) {
                    famModifCompteur = JadeStringUtil.parseInt(pv.getIdFamilleModification(), 0) + 1;
                } else {
                    famModifCompteur = 1;
                }
            } else {
                famModifCompteur = 1;
            }

            RFPeriodeValiditeQdPrincipale rfPeriodeValiditeQdPrincipale = new RFPeriodeValiditeQdPrincipale();
            rfPeriodeValiditeQdPrincipale.setSession(getSession());
            rfPeriodeValiditeQdPrincipale.setIdQd(qd.getIdQd());
            rfPeriodeValiditeQdPrincipale.setDateDebut(getContext().getInfoDroitPcServiceData()
                    .getDateDebutPcaccordee());
            rfPeriodeValiditeQdPrincipale.setDateFin(getContext().getInfoDroitPcServiceData().getDateFinPcaccordee());
            rfPeriodeValiditeQdPrincipale.setTypeModification(IRFQd.CS_AJOUT);
            rfPeriodeValiditeQdPrincipale.setDateModification(JACalendar.todayJJsMMsAAAA());
            rfPeriodeValiditeQdPrincipale.setIdPeriodeValModifiePar("");
            rfPeriodeValiditeQdPrincipale.setConcerne(getSession().getLabel(
                    "PROCESS_ADAPTATION_JOURNALIERE_AJOUT_GRANDE_QD_CONCERNE"));
            rfPeriodeValiditeQdPrincipale.setIdGestionnaire(getGestionnaire());
            rfPeriodeValiditeQdPrincipale.setIdFamilleModification(Integer.toString(famModifCompteur));

            rfPeriodeValiditeQdPrincipale.add(getTransaction());

            // Création du solde excédent de revenu
            if (!JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getSoldeExcedent())) {

                // calcule le nouvel id unique de famille de modification
                famModifCompteur = 0;

                RFQdSoldeExcedentDeRevenuManager rfQdSolExcDeRevMgr = new RFQdSoldeExcedentDeRevenuManager();
                rfQdSolExcDeRevMgr.setSession(getSession());
                rfQdSolExcDeRevMgr.setForIdFamilleMax(true);
                rfQdSolExcDeRevMgr.changeManagerSize(0);
                rfQdSolExcDeRevMgr.find(transaction);

                if (!rfQdSolExcDeRevMgr.isEmpty()) {
                    RFQdSoldeExcedentDeRevenu sc = (RFQdSoldeExcedentDeRevenu) rfQdSolExcDeRevMgr.getFirstEntity();
                    if (null != sc) {
                        famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                    } else {
                        famModifCompteur = 1;
                    }
                } else {
                    famModifCompteur = 1;
                }

                RFQdSoldeExcedentDeRevenu rfQdSolExcDeRev = new RFQdSoldeExcedentDeRevenu();
                rfQdSolExcDeRev.setSession(getSession());
                rfQdSolExcDeRev.setIdFamilleModification(Integer.toString(famModifCompteur));
                rfQdSolExcDeRev.setConcerne(getSession().getLabel(
                        "PROCESS_ADAPTATION_JOURNALIERE_AJOUT_GRANDE_QD_CONCERNE"));
                rfQdSolExcDeRev.setIdQd(qd.getIdQd());
                rfQdSolExcDeRev.setTypeModification(IRFQd.CS_AJOUT);
                rfQdSolExcDeRev.setVisaGestionnaire(getGestionnaire());
                rfQdSolExcDeRev.setMontantSoldeExcedent(getContext().getInfoDroitPcServiceData().getSoldeExcedent());
                rfQdSolExcDeRev.setDateModification(JACalendar.todayJJsMMsAAAA());
                rfQdSolExcDeRev.setIdSoldeExcedentModifie("");

                rfQdSolExcDeRev.add(transaction);

            }

            // Création du lien entre le dossier et la Qd de base
            List<PCAMembreFamilleVO> personnesDansPlanCalcul = getContext().getInfoDroitPcServiceData().getPersonneDansPlanCalculList();
            String dateReforme = EPCProperties.DATE_REFORME_PC.getValue();
            String dateDebutPCA = getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee();
            boolean hasEnfantRentier =  personnesDansPlanCalcul.size() > 1 && personnesDansPlanCalcul.stream().anyMatch(each -> each.getIsRentier() && Objects.equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT,each.getCsRoleFamillePC())) &&  !JadeDateUtil.isDateBefore(dateDebutPCA, dateReforme);
            if (RFUtils.typeBeneficiairePlusieursPersonnesComprisesDansCalcul.contains(getContext().getInfoDroitPcServiceData().getTypeBeneficiaire()) || hasEnfantRentier) {

                RFRetrieveInfoDroitPCServiceData infoDroitPcServiceData = getContext().getInfoDroitPcServiceData();

                if (infoDroitPcServiceData.getPersonneDansPlanCalculList().size() > 0) {

                    Vector<String[]> personnesDansPlanCalculVec = new Vector<String[]>();

                    for (PCAMembreFamilleVO membreCourant : getContext().getInfoDroitPcServiceData()
                            .getPersonneDansPlanCalculList()) {
                        personnesDansPlanCalculVec.add(RFUtils.getMembreFamilleTabString(membreCourant.getIdTiers(),
                                membreCourant.getCsRoleFamillePC(), membreCourant.getNss(), membreCourant.getNom(),
                                membreCourant.getPrenom(), membreCourant.getDateNaissance(), membreCourant.getCsSexe(),
                                membreCourant.getCsNationalite(), membreCourant.getIsComprisDansCalcul(), membreCourant.getIsRentier()));
                    }

                    RFUtils.ajouterAssociationDossierQdMembreFamille(personnesDansPlanCalculVec, getGestionnaire(),
                            getSession(), getTransaction(), qd.getIdQd(), "", infoDroitPcServiceData.getDateDebutPcaccordee());

                    // MàJ de l'état des autres Qds
                    if (isAdaptationAnnuelle) {
                        Set<String> isTiersQdSet = new HashSet<String>();
                        for (String[] personneDansPlanCal : personnesDansPlanCalculVec) {
                            isTiersQdSet.add(personneDansPlanCal[0]);
                        }
                        // Mise à jour des autres Qds de même tiers dans l'état ouvert
                        majEtatQdsOuvertAdaptationAnnuelle(qd.getAnneeQd(), isTiersQdSet);
                    }

                } else {
                    getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);
                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                            getContext().getInfoDroitPcServiceData().getIdTiers(), getContext()
                                    .getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                    .getNumeroDecisionPc(), "Membres compris dans le calcul introuvable", getLogsList());

                    return false;
                }

                // Un seul dossier
            } else {
                RFAssQdDossier rfAssQdDossier = new RFAssQdDossier();
                rfAssQdDossier.setSession(getSession());

                RFPrDemandeJointDossier rfPrDemJoiDos = RFUtils.getDossierJointPrDemande(getContext()
                        .getInfoDroitPcServiceData().getIdTiers(), getSession());
                String idDossier = "";
                if (null == rfPrDemJoiDos) {
                    idDossier = RFUtils.ajouterDossier(getContext().getInfoDroitPcServiceData().getIdTiers(),
                            getGestionnaire(), getSession(), getTransaction());
                } else {
                    idDossier = rfPrDemJoiDos.getIdDossier();
                }

                rfAssQdDossier.setIdQd(qd.getIdQd());
                rfAssQdDossier.setIdDossier(idDossier);
                rfAssQdDossier.setTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                rfAssQdDossier.setIsComprisDansCalcul(Boolean.TRUE);

                rfAssQdDossier.add(transaction);

                if (isAdaptationAnnuelle) {
                    Set<String> isTiersQdSet = new HashSet<String>();
                    isTiersQdSet.add(getContext().getInfoDroitPcServiceData().getIdTiers());
                    // Mise à jour des autres Qds de même tiers dans l'état ouvert
                    majEtatQdsOuvertAdaptationAnnuelle(qd.getAnneeQd(), isTiersQdSet);
                }

            }

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "Création de la Qd n° " + qd.getIdQd() + " et de sa période de validité (n°"
                            + rfPeriodeValiditeQdPrincipale.getIdPeriodeValidite() + ")", getLogsList());

            return true;
        } else {
            return false;
        }

    }

    @Override
    public RFAdaptationJournaliereContext executerAdaptation() throws Exception {

        if (!JadeStringUtil.isBlankOrZero(context.getDateDebutDecision())) {

            rechercherInfoDroitPc();

            Set<String> idsTiersMembreFamille = new HashSet<String>();

            if (null != context.getInfoDroitPcServiceData()) {

                for (PCAMembreFamilleVO idMembreFamille : context.getInfoDroitPcServiceData()
                        .getPersonneDansPlanCalculList()) {
                    if (idMembreFamille.getIsComprisDansCalcul().booleanValue()) {
                        idsTiersMembreFamille.add(idMembreFamille.getIdTiers());
                    }
                }

                // MàJ de la période de la PCAccordée
                if (!JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getDateFinPcaccordee())) {
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    GregorianCalendar calendar = new GregorianCalendar();

                    Date dateFinPcaccordeDate = dateFormat.parse("01."
                            + getContext().getInfoDroitPcServiceData().getDateFinPcaccordee());
                    calendar.setTime(dateFinPcaccordeDate);

                    calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.DAY_OF_WEEK, -1);
                    dateFinPcaccordeDate = calendar.getTime();
                    getContext().getInfoDroitPcServiceData().setDateFinPcaccordee(
                            dateFormat.format(dateFinPcaccordeDate));
                } else {
                    getContext().getInfoDroitPcServiceData().setDateFinPcaccordee(
                            FIN_ANNEE_JJMM
                                    + PRDateFormater.convertDate_MMxAAAA_to_AAAA(getContext()
                                            .getInfoDroitPcServiceData().getDateDebutPcaccordee()));
                }

                getContext().getInfoDroitPcServiceData().setDateDebutPcaccordee(
                        DEBUT_MOIS_JJ + getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee());

                if (getContext().getInfoDroitPcServiceData().getTypeBeneficiaire()
                        .equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)
                        || getContext().getInfoDroitPcServiceData().getTypeBeneficiaire()
                                .equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME)) {
                    Set<String> idsTiersSet = new HashSet<String>();
                    idsTiersSet.add(getContext().getInfoDroitPcServiceData().getIdTiers());
                    rechercherPeriodesGrandesQd(idsTiersSet, false);
                } else {
                    rechercherPeriodesGrandesQd(idsTiersMembreFamille, false);
                }

                // this.majPetitesQd();

                majGrandesQd(getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee(), getContext()
                        .getInfoDroitPcServiceData().getDateFinPcaccordee(), false);

                octroyerDroitRfm();

            }

        } else {

            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getIdTiersBeneficiaire(), getContext().getNssTiersBeneficiaire(), getContext()
                            .getIdDecisionPc(), getContext().getNumeroDecisionPc(), "Date de début d'octroi vide",
                    getLogsList());

        }

        return getContext();
    }

    private void majEtatQdsOuvertAdaptationAnnuelle(String notAnneeQd, Set<String> idsTiers) throws Exception {

        RFQdJointDossierManager rfQdJointDossierMgr = new RFQdJointDossierManager();

        rfQdJointDossierMgr.setForNotAnneeQd(notAnneeQd);
        rfQdJointDossierMgr.setForCsEtatQd(IRFQd.CS_ETAT_QD_OUVERT);
        rfQdJointDossierMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        rfQdJointDossierMgr.setForIdsTiers(idsTiers);
        rfQdJointDossierMgr.find();

        Iterator<RFQdJointDossier> rfQdJointDossierItr = rfQdJointDossierMgr.iterator();

        Set<String> idsQdToUpdateSet = new HashSet<String>();
        while (rfQdJointDossierItr.hasNext()) {
            RFQdJointDossier rFQdJointDossier = rfQdJointDossierItr.next();
            idsQdToUpdateSet.add(rFQdJointDossier.getIdQd());
        }

        for (String idQdToUpdate : idsQdToUpdateSet) {
            RFQd rfQd = new RFQd();
            rfQd.setSession(getSession());
            rfQd.setIdQd(idQdToUpdate);

            rfQd.retrieve();

            if (!rfQd.isNew()) {
                rfQd.setCsEtat(IRFQd.CS_ETAT_QD_FERME);
                rfQd.update(getTransaction());
            } else {
                throw new Exception(
                        "RFAdaptationJournaliereOctroiHandler.majEtatQdsOuvertAdaptationAnnuelle():Impossible de changer l'état de la Qd N° "
                                + rfQd.getIdQd());
            }
        }

    }

    private void majPetiteQd(String idQd, String dateDebut, String dateFin, String csEtat) throws Exception {

        RFQdAssure rfQdAss = new RFQdAssure();
        rfQdAss.setSession(session);
        rfQdAss.setIdQdAssure(idQd);

        rfQdAss.retrieve();

        if (!rfQdAss.isNew()) {

            rfQdAss.setDateDebut(dateDebut);
            rfQdAss.setDateFin(dateFin);

            RFQd rfQd = new RFQd();
            rfQd.setSession(session);
            rfQd.setIdQd(idQd);

            rfQd.retrieve();

            if (!rfQd.isNew()) {

                rfQd.setCsEtat(csEtat);
                rfQd.update(transaction);

                rfQdAss.update(transaction);

            } else {
                getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                        getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                        getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                        "RFAdaptationJournaliereOctroiHandler.majPetiteQd(): Impossible de retrouver la pettie Qd",
                        getLogsList());
            }

        } else {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.majPetiteQd(): Impossible de retrouver la pettie Qd",
                    getLogsList());
        }

    }

    private void majPetitesQd() throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        GregorianCalendar calendar = new GregorianCalendar();

        JACalendar cal = new JACalendarGregorian();

        Set<String> idsTiersMembreFamille = new HashSet<String>();

        for (PCAMembreFamilleVO idMembreFamille : context.getInfoDroitPcServiceData().getPersonneDansPlanCalculList()) {
            idsTiersMembreFamille.add(idMembreFamille.getIdTiers());
        }

        RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = RFUtils
                .getRFQdAssureJointDossierJointTiersManager(session, idsTiersMembreFamille, null, "", "", "",
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(context.getInfoDroitPcServiceData()
                                .getDateDebutPcaccordee()), context.getInfoDroitPcServiceData()
                                .getDateDebutPcaccordee(), JadeStringUtil.isBlankOrZero(context
                                .getInfoDroitPcServiceData().getDateFinPcaccordee()) ? RFUtils.MAX_DATE_VALUE : context
                                .getInfoDroitPcServiceData().getDateFinPcaccordee(), "", true, "",
                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        if (rfQdAssJointDosJointTieMgr.size() > 0) {

            JADate droitPCDateDebutJD = new JADate(getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee());
            JADate droitPCDateFinJD = new JADate(JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData()
                    .getDateFinPcaccordee()) ? FIN_ANNEE_JJMM
                    + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(getContext().getInfoDroitPcServiceData()
                            .getDateFinPcaccordee())/* RFUtils.MAX_JADATE_VALUE */: getContext()
                    .getInfoDroitPcServiceData().getDateFinPcaccordee());

            Iterator<RFQdAssureJointDossierJointTiers> rfQdAssJointDosJointTieItr = rfQdAssJointDosJointTieMgr
                    .iterator();

            // On ferme les petites Qds qui concernent plusieurs personnes uniquement si celles-ci sont différentes
            // de la PC Accordées (enfants non compris)
            while (rfQdAssJointDosJointTieItr.hasNext()) {

                RFQdAssureJointDossierJointTiers rfQdAssJoiDosJoiTie = rfQdAssJointDosJointTieItr.next();

                if (null != rfQdAssJoiDosJoiTie) {

                    if (RFUtils.isSousTypeDeSoinCsConcernePlusieursPersonnes(rfQdAssJoiDosJoiTie.getIdSousTypeDeSoin())
                            && !isPersonnesPcAcoordeeIdentiqueQd(rfQdAssJoiDosJoiTie.getIdQdAssure())) {

                        JADate qdCouranteDateDebutPeriodeJD = new JADate(rfQdAssJoiDosJoiTie.getDateDebut());
                        JADate qdCouranteDateFinPeriodeJD = new JADate(rfQdAssJoiDosJoiTie.getDateFin());

                        // la qd est comprise dans la période de la pc
                        if (((cal.compare(droitPCDateDebutJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                                .compare(droitPCDateDebutJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_EQUALS))
                                && ((cal.compare(droitPCDateFinJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                                        .compare(droitPCDateFinJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_EQUALS))
                                && ((cal.compare(droitPCDateDebutJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                                        .compare(droitPCDateDebutJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_EQUALS))
                                && ((cal.compare(droitPCDateFinJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                                        .compare(droitPCDateFinJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_EQUALS))) {

                            majPetiteQd(rfQdAssJoiDosJoiTie.getIdQdAssure(), rfQdAssJoiDosJoiTie.getDateDebut(),
                                    rfQdAssJoiDosJoiTie.getDateFin(), IRFQd.CS_ETAT_QD_CLOTURE);

                            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext()
                                    .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData()
                                    .getIdTiers(), getContext().getNssTiersBeneficiaire(), getContext()
                                    .getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                    "Clôture de la petite Qd n° " + rfQdAssJoiDosJoiTie.getIdQdAssure(), getLogsList());

                            // le début et la fin de la période pc est supérieure au début de la Qd -> date de fin
                            // qd = date de début pc -1
                        } else if (((cal.compare(droitPCDateDebutJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER))
                                && ((cal.compare(droitPCDateFinJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER))) {

                            majPetiteQd(rfQdAssJoiDosJoiTie.getIdQd(), rfQdAssJoiDosJoiTie.getDateDebut(),
                                    majDateFinQd(calendar, dateFormat, false), IRFQd.CS_ETAT_QD_FERME);

                            RFUtils.ajouterLogAdaptation(
                                    FWViewBeanInterface.WARNING,
                                    getContext().getIdAdaptationJournaliere(),
                                    getContext().getInfoDroitPcServiceData().getIdTiers(),
                                    getContext().getNssTiersBeneficiaire(),
                                    getContext().getIdDecisionPc(),
                                    getContext().getNumeroDecisionPc(),
                                    "Mise à jour de la de la date de fin de la petite Qd n° "
                                            + rfQdAssJoiDosJoiTie.getIdQdAssure(), getLogsList());

                            // la début de la période pc est inférieure au début de la période Qd et la fin de la
                            // période pc
                            // est comprise dans la période de la Qd -> date de début qd = date de fin pc + 1
                        } else if (((cal.compare(droitPCDateDebutJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                                .compare(droitPCDateDebutJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_EQUALS))
                                && ((cal.compare(droitPCDateFinJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER))
                                && ((cal.compare(droitPCDateFinJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER))) {

                            majPetiteQd(rfQdAssJoiDosJoiTie.getIdQd(), majDateDebutQd(calendar, dateFormat),
                                    rfQdAssJoiDosJoiTie.getDateFin(), IRFQd.CS_ETAT_QD_FERME);

                            RFUtils.ajouterLogAdaptation(
                                    FWViewBeanInterface.WARNING,
                                    getContext().getIdAdaptationJournaliere(),
                                    getContext().getInfoDroitPcServiceData().getIdTiers(),
                                    getContext().getNssTiersBeneficiaire(),
                                    getContext().getIdDecisionPc(),
                                    getContext().getNumeroDecisionPc(),
                                    "Mise à jour de la date de début de la petite Qd n° "
                                            + rfQdAssJoiDosJoiTie.getIdQdAssure(), getLogsList());

                            // La période pc est comprise dans la période Qd
                        } else if ((cal.compare(droitPCDateDebutJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER)
                                && (cal.compare(droitPCDateFinJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER)) {

                            majPetiteQd(rfQdAssJoiDosJoiTie.getIdQd(), rfQdAssJoiDosJoiTie.getDateDebut(),
                                    majDateFinQd(calendar, dateFormat, false), IRFQd.CS_ETAT_QD_FERME);

                            RFUtils.ajouterLogAdaptation(
                                    FWViewBeanInterface.WARNING,
                                    getContext().getIdAdaptationJournaliere(),
                                    getContext().getInfoDroitPcServiceData().getIdTiers(),
                                    getContext().getNssTiersBeneficiaire(),
                                    getContext().getIdDecisionPc(),
                                    getContext().getNumeroDecisionPc(),
                                    "Mise à jour de la de la date de fin de la petite Qd n° "
                                            + rfQdAssJoiDosJoiTie.getIdQdAssure(), getLogsList());

                            // C'est le batch "préparer décision" qui va s'occuper de créer la seconde petite Qd

                        } else {
                            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext()
                                    .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData()
                                    .getIdTiers(), getContext().getNssTiersBeneficiaire(), getContext()
                                    .getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                    "RFAdaptationJournaliereOctroiHandler.majPetitesQd(): Période Pc non gérée",
                                    getLogsList());
                        }

                    }

                } else {
                    getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                    RFUtils.ajouterLogAdaptation(
                            FWViewBeanInterface.ERROR,
                            getContext().getIdAdaptationJournaliere(),
                            getContext().getInfoDroitPcServiceData().getIdTiers(),
                            getContext().getNssTiersBeneficiaire(),
                            getContext().getIdDecisionPc(),
                            getContext().getNumeroDecisionPc(),
                            "RFAdaptationJournaliereOctroiHandler.majPetitesQd(): Impossible de retrouver la pettie Qd",
                            getLogsList());
                }

            }
        }

    }

    protected void octroyerDroitRfm() throws Exception {

        if (!isContextEnErreur()) {

            // Soit on ajoute une nouvelle période à une qd existante ou on créé une nouvelle Qd
            if (idsQdCandidatesList.size() > 0) {

                boolean premiereIteration = true;
                for (RFQdCandidateData qdCourante : idsQdCandidatesList) {

                    // Si la Qd traitée à toutes ses périodes dans la période de droit PC, on la clôture sauf si on lui
                    // octroie la nouvelle période
                    if (qdCourante.isPeriodesComprisesDansPeriodePc()) {
                        //
                        if (!premiereIteration) {

                            RFQd rfQd = new RFQd();
                            rfQd.setIdQd(qdCourante.getIdQdPrincipale());
                            rfQd.setSession(session);

                            rfQd.retrieve();

                            if (!rfQd.isNew()) {

                                rfQd.setCsEtat(IRFQd.CS_ETAT_QD_CLOTURE);
                                rfQd.update(getTransaction());

                                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext()
                                        .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData()
                                        .getIdTiers(), getContext().getNssTiersBeneficiaire(), getContext()
                                        .getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                        "Clôture de la grande Qd N°: " + qdCourante.getIdQdPrincipale(), getLogsList());

                            } else {
                                getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext()
                                        .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData()
                                        .getIdTiers(), getContext().getNssTiersBeneficiaire(), getContext()
                                        .getIdDecisionPc(), getContext().getNumeroDecisionPc(), "Grande Qd  N° "
                                        + qdCourante.getIdQdPrincipale() + " introuvable", getLogsList());
                            }
                        } else {
                            premiereIteration = false;

                            // màj des périodes de la qd sélectionnée pour l'ajout de la nouvelle période si la Qd
                            // traitée à toutes ses périodes dans la période de droit PC
                            for (RFMutationPeriodeValiditeQd mutationPeriodeCourante : qdCourante.getMutations()) {

                                if (mutationPeriodeCourante.getTypeDeMutation().equals(
                                        TYPE_MUTATION_COMPRIS_DANS_PERIODE_PC)) {

                                    modifierHistoriquePeriodeGrandeQd(mutationPeriodeCourante.getIdQd(),
                                            mutationPeriodeCourante.getIdPeriode(),
                                            mutationPeriodeCourante.getIdFamille(), IRFQd.CS_SUPPRESSION,
                                            mutationPeriodeCourante.getDateDebutQd1(),
                                            mutationPeriodeCourante.getDateFinQd1());

                                } else {
                                    getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);
                                    RFUtils.ajouterLogAdaptation(
                                            FWViewBeanInterface.ERROR,
                                            getContext().getIdAdaptationJournaliere(),
                                            getContext().getInfoDroitPcServiceData().getIdTiers(),
                                            getContext().getNssTiersBeneficiaire(),
                                            getContext().getIdDecisionPc(),
                                            getContext().getNumeroDecisionPc(),
                                            "Type de mutation inconsistente (grande Qd  N° "
                                                    + qdCourante.getIdQdPrincipale() + "période N° "
                                                    + qdCourante.getIdPeriode() + " )", getLogsList());
                                }
                            }

                            // Octroi de la période de droit Pc
                            modifierHistoriquePeriodeGrandeQd(qdCourante.getIdQdPrincipale(),
                                    qdCourante.getIdPeriode(), "", IRFQd.CS_AJOUT, getContext()
                                            .getInfoDroitPcServiceData().getDateDebutPcaccordee(), getContext()
                                            .getInfoDroitPcServiceData().getDateFinPcaccordee());

                            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext()
                                    .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData()
                                    .getIdTiers(), getContext().getNssTiersBeneficiaire(), getContext()
                                    .getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                    "Ajout d'une période de validité à la Qd n° " + qdCourante.getIdQdPrincipale(),
                                    getLogsList());

                        }
                        // Si la Qd traitée n'a pas toutes ses périodes Qd dans la période de droit PC, on ajoute le
                        // droit à la Qd candidate séléctionnée
                    } else {

                        modifierHistoriquePeriodeGrandeQd(qdCourante.getIdQdPrincipale(), qdCourante.getIdPeriode(),
                                "", IRFQd.CS_AJOUT, getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee(),
                                getContext().getInfoDroitPcServiceData().getDateFinPcaccordee());

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext()
                                .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData().getIdTiers(),
                                getContext().getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                        .getNumeroDecisionPc(), "Ajout d'une période de validité à la Qd n° "
                                        + qdCourante.getIdQdPrincipale(), getLogsList());
                    }
                }

            } else {
                ajouterPeriodePcAccordeeNouvelleQd();
            }
        }
    }

    private void rechercherInfoDroitPc() throws Exception {

        if (!isContextEnErreur()) {

            // Si c'est un octroi, octroi partiel ou refus AC on recherche le nouveau pot de la nouvelle Qd
            if (context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_OCTROI_AC)
                    || context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_PARTIEL_AC)
                    || context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_REFUS_AC) || isAdaptationAnnuelle) {

                // Recherche des PCAccordées liées à la décision courante IRFAdaptationJournaliere.
                RFRetrieveInfoDroitPCService rfRetInfDroPcSer = new RFRetrieveInfoDroitPCService();
                RFRetrieveInfoDroitPCServiceData[] resultat = rfRetInfDroPcSer.retrieve(getLogsList(),
                        context.getIdAdaptationJournaliere(), null, getSession(), context.getIdTiersBeneficiaire(),
                        context.getDateDebutDecision(), transaction, context.getTypeDeDecisionPc(),
                        isAdaptationAnnuelle);

                boolean isIdTiersTraite = false;

                if (resultat != null) {

                    for (RFRetrieveInfoDroitPCServiceData resCourant : resultat) {
                        if (resCourant != null) {
                            // Une décision PC concerne toujours un tiers (couple séparé -> 2 décisions)
                            if (resCourant.getIdTiers().equals(context.getIdTiersBeneficiaire())) {
                                context.setInfoDroitPcServiceData(resCourant);
                                context.setTypeRemboursementRequerant(resCourant.getTypeRemboursement());
                                context.setMontantExcedentDeRevenu(resCourant.getSoldeExcedent());
                                context.getInfoDroitPcServiceData().setCsDegreApi(resCourant.getCsDegreApi());
                                isIdTiersTraite = true;
                            } else {
                                context.setTypeRemboursementConjoint(resCourant.getTypeRemboursement());
                            }
                        }
                    }

                    if (!isIdTiersTraite) {
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext()
                                .getIdAdaptationJournaliere(), "", "", getContext().getIdDecisionPc(), getContext()
                                .getNumeroDecisionPc(),
                                "Les informations concernant le droit PC sont introuvable pour la décision PC",
                                getLogsList());

                    } else {

                        // Recherche du pot de la nouvelle Qd (String[idPot, limiteAnnuelle, isLimiteAnnuelleOk, isRi])
                        String[] pot = RFRetrievePotQdPrincipaleInfoDroitPcService.retrieveLimiteAnnuelle(null, context
                                .getInfoDroitPcServiceData().getDateDebutPcaccordee(), context
                                .getInfoDroitPcServiceData().getTypeBeneficiaire(), context.getInfoDroitPcServiceData()
                                .getGenrePc(), getSession(), getTransaction());

                        if (pot[2].equals(Boolean.FALSE.toString())) {
                            context.setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext()
                                    .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData()
                                    .getIdTiers(), getContext().getNssTiersBeneficiaire(), getContext()
                                    .getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                    "Pot grande Qd introuvable", getLogsList());

                        } else {
                            context.setIdPotGrandeQd(pot[0]);
                            context.setLimiteAnnuelleGrandeQd(pot[1]);
                        }
                    }

                } else {

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                            getContext().getIdAdaptationJournaliere(), getContext().getIdTiersBeneficiaire(),
                            getContext().getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                    .getNumeroDecisionPc(), "Décision non prise en compte (copie)", getLogsList());
                }

            }
        }

    }

    private boolean validerAjouterGrandeQd() {

        boolean resultat = true;

        if (JadeStringUtil.isBlankOrZero(getContext().getLimiteAnnuelleGrandeQd())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(
                    FWViewBeanInterface.ERROR,
                    getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(),
                    getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(),
                    getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): Limite annuelle grande Qd obligatoire",
                    getLogsList());

            resultat = false;
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getIdPotGrandeQd())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): Id pot grande Qd obligatoire",
                    getLogsList());

            resultat = false;
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getGenrePc())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): Genre Pc obligatoire",
                    getLogsList());

            resultat = false;
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getTypePc())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): Type Pc obligatoire", getLogsList());

            resultat = false;
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getTypeBeneficiaire())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): Type bénéficiaire obligatoire",
                    getLogsList());

            resultat = false;
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(
                    FWViewBeanInterface.ERROR,
                    getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(),
                    getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(),
                    getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): date de début Pc accordée obligatoire",
                    getLogsList());

            resultat = false;
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getIdTiers())) {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                    getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                    getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                    "RFAdaptationJournaliereOctroiHandler.validerAjouterGrandeQd(): Id tiers bénéficiaire obligatoire",
                    getLogsList());

            resultat = false;
        }

        return resultat;

    }

}
