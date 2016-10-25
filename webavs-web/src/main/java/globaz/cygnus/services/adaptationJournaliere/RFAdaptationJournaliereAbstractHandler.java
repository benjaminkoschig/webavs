package globaz.cygnus.services.adaptationJournaliere;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.adaptationsJournalieres.IRFAdaptationJournaliere;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemandeJointDossierJointTiers;
import globaz.cygnus.db.demandes.RFDemandeJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

/**
 * @author JJE
 */
public abstract class RFAdaptationJournaliereAbstractHandler {

    protected RFAdaptationJournaliereContext context = null;
    public final String DEBUT_MOIS_JJ = "01.";
    public final String FIN_ANNEE_JJMM = "31.12.";
    String gestionnaire = "";
    protected List<RFQdCandidateData> idsQdCandidatesList = new LinkedList<RFQdCandidateData>();
    protected boolean isAdaptationAnnuelle = false;

    // Liste de logs, String[idAdaptation,idTiersBeneficiaire,idDecision,libelle]
    protected List<String[]> logsList = null;
    protected BSession session = null;

    protected Map<String, List<String>> idPeriodesForIdQD = new HashMap<String, List<String>>();

    protected BTransaction transaction = null;
    protected final String TYPE_MUTATION_COMPRIS_DANS_PERIODE_PC = "COMPRIS_DANS_PERIODE";
    protected final String TYPE_MUTATION_DATE_DEBUT_PERIODE_QD_EGALE_DATE_FIN_PERIODE_PC = "DATE_DEBUT_EGALE_DATE_FIN_PC";
    protected final String TYPE_MUTATION_DATE_FIN_PERIODE_QD_EGALE_DATE_DEBUT_PERIODE_PC = "DATE_FIN_EGALE_DATE_DEBUT_PC";

    protected final String TYPE_MUTATION_PERIODE_PC_COMPRISE_DANS_PERIODE_QD = "PERIODE_PC_COMPRISE_DANS_PERIODE_QD";

    public RFAdaptationJournaliereAbstractHandler(RFAdaptationJournaliereContext context, BSession session,
            BTransaction transaction, List<String[]> logsList, String gestionnaire, boolean isAdaptationAnnuelle) {
        super();
        this.context = context;
        this.session = session;
        this.transaction = transaction;
        this.logsList = logsList;
        this.gestionnaire = gestionnaire;
        this.isAdaptationAnnuelle = isAdaptationAnnuelle;
    }

    private void ajouterLogRegimeImpacte(RFAdaptationJournalierePeriodeQdData periodeQdCouranteData, String idTiers)
            throws Exception {

        List<String> idsDemandeRegimeImpactees = getIdDemandesRegimesList(periodeQdCouranteData.getId_qd());

        if (idsDemandeRegimeImpactees.size() > 0) {

            StringBuffer idsDemandeStrBfr = new StringBuffer();
            int inc = 0;
            for (String id : idsDemandeRegimeImpactees) {
                if (!JadeStringUtil.isEmpty(id)) {
                    inc++;
                    if (idsDemandeRegimeImpactees.size() != inc) {
                        idsDemandeStrBfr.append(id + ",");
                    } else {
                        idsDemandeStrBfr.append(id);
                    }
                }
            }

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext().getIdAdaptationJournaliere(),
                    idTiers, getContext().getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                            .getNumeroDecisionPc(), "Ids demandes de r�gime potentiellement impact�es: "
                            + idsDemandeStrBfr.toString(), getLogsList());
        }
    }

    private boolean ajoutQdsCandidatesOctroi(boolean isSuppression,
            RFAdaptationJournalierePeriodeQdData periodeQdCouranteData,
            List<RFMutationPeriodeValiditeQd> mutationPeriodeCourante, boolean isPeriodesComprisesDansPeriodePC)
            throws Exception {

        corrigerValeursVideContextEtPeriodeQdCourante(periodeQdCouranteData);

        BigDecimal periodeQdCouranteDataMntExcDeRevBigDec = new BigDecimal(
                periodeQdCouranteData.getMontantExcedentDeRevenu());
        BigDecimal ContextMntExcDeRevBigDec = new BigDecimal(getContext().getMontantExcedentDeRevenu());

        // Ajout de la Qd dans la liste des Qds candidates � l'octroi d'une nouvelle p�riode PC
        if (isQdCandidate(isSuppression, periodeQdCouranteData, periodeQdCouranteDataMntExcDeRevBigDec,
                ContextMntExcDeRevBigDec)) {

            idsQdCandidatesList.add(new RFQdCandidateData(periodeQdCouranteData.getId_periode(), periodeQdCouranteData
                    .getId_qd(), isPeriodesComprisesDansPeriodePC, mutationPeriodeCourante));

            return true;
        } else {
            return false;
        }
    }

    private void corrigerValeursVideContextEtPeriodeQdCourante(
            RFAdaptationJournalierePeriodeQdData periodeQdCouranteData) {

        if (JadeStringUtil.isBlankOrZero(periodeQdCouranteData.getMontantExcedentDeRevenu())) {
            periodeQdCouranteData.setMontantExcedentDeRevenu("0.00");
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getMontantExcedentDeRevenu())) {
            getContext().setMontantExcedentDeRevenu("0.00");
        }

        if (JadeStringUtil.isBlankOrZero(periodeQdCouranteData.getCsDegreApi())) {
            periodeQdCouranteData.setCsDegreApi("");
        }

        if ((getContext().getInfoDroitPcServiceData() != null)
                && JadeStringUtil.isBlankOrZero(getContext().getInfoDroitPcServiceData().getCsDegreApi())) {
            getContext().getInfoDroitPcServiceData().setCsDegreApi("");
        }

        if (JadeStringUtil.isBlankOrZero(periodeQdCouranteData.getTypeRemboursementConjoint())) {
            periodeQdCouranteData.setTypeRemboursementConjoint("");
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getTypeRemboursementConjoint())) {
            getContext().setTypeRemboursementConjoint("");
        }

        if (JadeStringUtil.isBlankOrZero(periodeQdCouranteData.getTypeRemboursementRequerant())) {
            periodeQdCouranteData.setTypeRemboursementRequerant("");
        }

        if (JadeStringUtil.isBlankOrZero(getContext().getTypeRemboursementRequerant())) {
            getContext().setTypeRemboursementRequerant("");
        }
    }

    private void executeMutationsQd(boolean isQdCloture, RFAdaptationJournalierePeriodeQdData periodeQdCouranteData,
            List<RFMutationPeriodeValiditeQd> mutationsPeriodesList, boolean isSuppression) throws Exception {

        String idTiers = "";
        if (isSuppression) {
            idTiers = getContext().getIdTiersBeneficiaire();
        } else {
            idTiers = getContext().getInfoDroitPcServiceData().getIdTiers();
        }

        for (RFMutationPeriodeValiditeQd mutationPeriodeCourante : mutationsPeriodesList) {

            if (!isQdCloture) {

                // Si une p�riode est comprise dans la p�riode Pc, on la supprime
                if (TYPE_MUTATION_COMPRIS_DANS_PERIODE_PC.equals(mutationPeriodeCourante.getTypeDeMutation())) {

                    modifierHistoriquePeriodeGrandeQd(mutationPeriodeCourante.getIdQd(),
                            mutationPeriodeCourante.getIdPeriode(), mutationPeriodeCourante.getIdFamille(),
                            IRFQd.CS_SUPPRESSION, mutationPeriodeCourante.getDateDebutQd1(),
                            mutationPeriodeCourante.getDateFinQd1());

                    ajouterLogRegimeImpacte(periodeQdCouranteData, idTiers);

                    // Sinon on modifie sa date de fin ou de d�but
                } else if (TYPE_MUTATION_DATE_FIN_PERIODE_QD_EGALE_DATE_DEBUT_PERIODE_PC.equals(mutationPeriodeCourante
                        .getTypeDeMutation())) {

                    modifierHistoriquePeriodeGrandeQd(mutationPeriodeCourante.getIdQd(),
                            mutationPeriodeCourante.getIdPeriode(), mutationPeriodeCourante.getIdFamille(),
                            IRFQd.CS_MODIFICATION, mutationPeriodeCourante.getDateDebutQd1(),
                            mutationPeriodeCourante.getDateFinQd1());

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                            getContext().getIdAdaptationJournaliere(), idTiers, getContext().getNssTiersBeneficiaire(),
                            getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                            "Modification de la date de fin de la p�riode n�: " + periodeQdCouranteData.getId_periode()
                                    + " de la Qd n� " + periodeQdCouranteData.getId_qd(), getLogsList());

                    ajouterLogRegimeImpacte(periodeQdCouranteData, idTiers);

                } else if (TYPE_MUTATION_DATE_DEBUT_PERIODE_QD_EGALE_DATE_FIN_PERIODE_PC.equals(mutationPeriodeCourante
                        .getTypeDeMutation())) {

                    modifierHistoriquePeriodeGrandeQd(mutationPeriodeCourante.getIdQd(),
                            mutationPeriodeCourante.getIdPeriode(), mutationPeriodeCourante.getIdFamille(),
                            IRFQd.CS_MODIFICATION, mutationPeriodeCourante.getDateDebutQd1(),
                            mutationPeriodeCourante.getDateFinQd1());

                    RFUtils.ajouterLogAdaptation(
                            FWViewBeanInterface.WARNING,
                            getContext().getIdAdaptationJournaliere(),
                            idTiers,
                            getContext().getNssTiersBeneficiaire(),
                            getContext().getIdDecisionPc(),
                            getContext().getNumeroDecisionPc(),
                            "Modification de la date de d�but de la p�riode n�: "
                                    + periodeQdCouranteData.getId_periode() + " de la Qd n� "
                                    + periodeQdCouranteData.getId_qd(), getLogsList());

                    ajouterLogRegimeImpacte(periodeQdCouranteData, idTiers);

                } else if (TYPE_MUTATION_PERIODE_PC_COMPRISE_DANS_PERIODE_QD.equals(mutationPeriodeCourante
                        .getTypeDeMutation())) {

                    // Modification de la date de fin de la p�riode actuelle
                    modifierHistoriquePeriodeGrandeQd(mutationPeriodeCourante.getIdQd(),
                            mutationPeriodeCourante.getIdPeriode(), mutationPeriodeCourante.getIdFamille(),
                            IRFQd.CS_MODIFICATION, mutationPeriodeCourante.getDateDebutQd1(),
                            mutationPeriodeCourante.getDateFinQd1());

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                            getContext().getIdAdaptationJournaliere(), idTiers, getContext().getNssTiersBeneficiaire(),
                            getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                            "Suppresion de la p�riode de validit� n� " + periodeQdCouranteData.getId_periode()
                                    + " de la Qd N� " + periodeQdCouranteData.getId_qd(), getLogsList());

                    ajouterLogRegimeImpacte(periodeQdCouranteData, idTiers);

                    if (!JadeStringUtil.isBlankOrZero(mutationPeriodeCourante.getDateDebutQd2())
                            && !JadeStringUtil.isBlankOrZero(mutationPeriodeCourante.getDateFinQd2())) {
                        // Ajout d'une nouvelle p�riode
                        modifierHistoriquePeriodeGrandeQd(mutationPeriodeCourante.getIdQd(),
                                mutationPeriodeCourante.getIdPeriode(), mutationPeriodeCourante.getIdFamille(),
                                IRFQd.CS_AJOUT, mutationPeriodeCourante.getDateDebutQd2(),
                                mutationPeriodeCourante.getDateFinQd2());

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext()
                                .getIdAdaptationJournaliere(), idTiers, getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                "Cr�ation de la p�riode de validit� n� " + periodeQdCouranteData.getId_periode()
                                        + " de la Qd N� " + periodeQdCouranteData.getId_qd(), getLogsList());

                        ajouterLogRegimeImpacte(periodeQdCouranteData, idTiers);

                    } else {
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                getContext().getIdAdaptationJournaliere(),
                                idTiers,
                                getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(),
                                getContext().getNumeroDecisionPc(),
                                "impossible d'effectuer la mutation de type p�riode Pc comprise dans p�riode Qd, les dates de la 2eme p�riode de validit� sont null"
                                        + periodeQdCouranteData.getId_qd() + " introuvable", getLogsList());
                    }

                }
            }
        }

    }

    public abstract RFAdaptationJournaliereContext executerAdaptation() throws Exception;

    public RFAdaptationJournaliereContext getContext() {
        return context;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    private List<String> getIdDemandesRegimesList(String idQdPrincipale) throws Exception {

        RFDemandeJointDossierJointTiersManager rfDemJoiDosJoiTieMgr = new RFDemandeJointDossierJointTiersManager(false);
        rfDemJoiDosJoiTieMgr.setSession(getSession());
        rfDemJoiDosJoiTieMgr.setCodeTypeDeSoinList(IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE);
        rfDemJoiDosJoiTieMgr.setForIdQdPrincipale(idQdPrincipale);
        rfDemJoiDosJoiTieMgr.changeManagerSize(0);
        rfDemJoiDosJoiTieMgr.find();

        List<String> idsDemandeList = new ArrayList<String>();

        Iterator<RFDemandeJointDossierJointTiers> rfDemJoiDosJoiTieItr = rfDemJoiDosJoiTieMgr.iterator();
        while (rfDemJoiDosJoiTieItr.hasNext()) {
            RFDemandeJointDossierJointTiers demandeCourante = rfDemJoiDosJoiTieItr.next();
            if (null != demandeCourante) {
                idsDemandeList.add(demandeCourante.getIdDemande());
            }
        }

        return idsDemandeList;

    }

    public List<String[]> getLogsList() {
        return logsList;
    }

    public BSession getSession() {
        return session;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    protected boolean isContextEnErreur() {
        return getContext().getEtat().equals(IRFAdaptationJournaliere.ETAT_ECHEC);
    }

    private boolean isNotCoupleSepare() {
        return !(getContext().getInfoDroitPcServiceData().getTypeBeneficiaire()
                .equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME) || getContext()
                .getInfoDroitPcServiceData().getTypeBeneficiaire()
                .equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE));
    }

    protected boolean isPersonnesPcAcoordeeIdentiqueQd(String idQd) throws Exception {

        // pour un couple s�par�, la Qd existante comporte de toute fa�on une seule personne, la prestation accord�e PC
        // en comportera deux, ce test n'est donc pas applicable dans ce cas
        if (isNotCoupleSepare()) {

            RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierMgr = new RFAssQdDossierJointDossierJointTiersManager();
            rfAssQdDossierMgr.setSession(session);
            rfAssQdDossierMgr.setForIdQd(idQd);
            rfAssQdDossierMgr.changeManagerSize(0);
            rfAssQdDossierMgr.find();

            if (rfAssQdDossierMgr.size() == getContext().getInfoDroitPcServiceData().getPersonneDansPlanCalculList()
                    .size()) {

                Iterator<RFAssQdDossierJointDossierJointTiers> rfAssQdDossierItr = rfAssQdDossierMgr.iterator();

                while (rfAssQdDossierItr.hasNext()) {

                    RFAssQdDossierJointDossierJointTiers rfAssQdDossier = rfAssQdDossierItr.next();

                    if (null != rfAssQdDossier) {

                        boolean membreTrouve = false;

                        for (PCAMembreFamilleVO membreFamille : getContext().getInfoDroitPcServiceData()
                                .getPersonneDansPlanCalculList()) {

                            if (membreFamille.getIdTiers().equals(rfAssQdDossier.getIdTiers())
                                    && membreFamille.getIsComprisDansCalcul().equals(
                                            rfAssQdDossier.getIsComprisDansCalcul())) {
                                membreTrouve = true;
                            }
                        }

                        if (!membreTrouve) {
                            return false;
                        }

                    } else {

                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext()
                                .getIdAdaptationJournaliere(), getContext().getInfoDroitPcServiceData().getIdTiers(),
                                getContext().getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                        .getNumeroDecisionPc(), "Impossible de retrouver la pettie Qd", getLogsList());

                        return false;
                    }
                }

                return true;

            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean isQdCandidate(boolean isSuppression, RFAdaptationJournalierePeriodeQdData periodeQdCouranteData,
            BigDecimal periodeQdCouranteDataMntExcDeRevBigDec, BigDecimal ContextMntExcDeRevBigDec) throws Exception {

        if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
            return !isSuppression
                    && periodeQdCouranteData.getGenrePc().equals(getContext().getInfoDroitPcServiceData().getGenrePc())
                    && periodeQdCouranteData.getTypePc().equals(getContext().getInfoDroitPcServiceData().getTypePc())
                    && periodeQdCouranteData.getTypeBeneficiaire().equals(
                            getContext().getInfoDroitPcServiceData().getTypeBeneficiaire())
                    && isPersonnesPcAcoordeeIdentiqueQd(periodeQdCouranteData.getId_qd())
                    && (periodeQdCouranteDataMntExcDeRevBigDec.compareTo(ContextMntExcDeRevBigDec) == 0);
        } else {
            return !isSuppression
                    && periodeQdCouranteData.getGenrePc().equals(getContext().getInfoDroitPcServiceData().getGenrePc())
                    && periodeQdCouranteData.getTypePc().equals(getContext().getInfoDroitPcServiceData().getTypePc())
                    && periodeQdCouranteData.getTypeBeneficiaire().equals(
                            getContext().getInfoDroitPcServiceData().getTypeBeneficiaire())
                    && (periodeQdCouranteData.getIsRi() == getContext().getInfoDroitPcServiceData().isRi())
                    && isPersonnesPcAcoordeeIdentiqueQd(periodeQdCouranteData.getId_qd())
                    && (periodeQdCouranteDataMntExcDeRevBigDec.compareTo(ContextMntExcDeRevBigDec) == 0)
                    && getContext().getTypeRemboursementRequerant().equals(
                            periodeQdCouranteData.getTypeRemboursementRequerant())
                    && getContext().getTypeRemboursementConjoint().equals(
                            periodeQdCouranteData.getTypeRemboursementConjoint())
                    && getContext().getInfoDroitPcServiceData().getCsDegreApi()
                            .equals(periodeQdCouranteData.getCsDegreApi());
        }
    }

    protected String majDateDebutQd(GregorianCalendar calendar, DateFormat dateFormat) throws Exception {

        Date dateDebutQdDate = dateFormat.parse(getContext().getInfoDroitPcServiceData().getDateFinPcaccordee());
        calendar.setTime(dateDebutQdDate);
        calendar.add(Calendar.DAY_OF_WEEK, +1);
        dateDebutQdDate = calendar.getTime();

        return dateFormat.format(dateDebutQdDate);
    }

    protected String majDateFinQd(GregorianCalendar calendar, DateFormat dateFormat, boolean isSuppression)
            throws Exception {
        Date dateFinQdDate = null;
        if (isSuppression) {
            dateFinQdDate = dateFormat.parse(getContext().getDateDebutDecision());
            calendar.setTime(dateFinQdDate);
        } else {
            dateFinQdDate = dateFormat.parse(getContext().getInfoDroitPcServiceData().getDateDebutPcaccordee());
            calendar.setTime(dateFinQdDate);
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }

        dateFinQdDate = calendar.getTime();

        return dateFormat.format(dateFinQdDate);
    }

    protected void majEtatQdPeriodesQd(RFAdaptationJournalierePeriodeQdData periodeQdCouranteData,
            List<RFMutationPeriodeValiditeQd> mutationsPeriodesList, boolean isSuppression) throws Exception {

        String idTiers = "";
        if (isSuppression) {
            idTiers = context.getIdTiersBeneficiaire();
        } else {
            idTiers = getContext().getInfoDroitPcServiceData().getIdTiers();
        }

        // Maj de la Qd
        RFQd rfQd = new RFQd();
        rfQd.setIdQd(periodeQdCouranteData.getId_qd());
        rfQd.setSession(session);

        rfQd.retrieve();

        if (!rfQd.isNew()) {

            if (!rfQd.getCsEtat().equals(IRFQd.CS_ETAT_QD_CLOTURE)) {

                boolean isQdCloture = false;

                // Test si toutes les p�riodes (incluses dans le droit PC) sont comprise dans la p�riode Pc
                boolean isPeriodesComprisesDansPeriodePC = true;
                for (RFMutationPeriodeValiditeQd mutationPeriodeCourante : mutationsPeriodesList) {
                    if (!TYPE_MUTATION_COMPRIS_DANS_PERIODE_PC.equals(mutationPeriodeCourante.getTypeDeMutation())) {
                        isPeriodesComprisesDansPeriodePC = false;
                    }
                }

                if (isPeriodesComprisesDansPeriodePC) {
                    // on cl�ture la Qd si toutes les p�riodes (y compris celles non incluses dans le droit Pc) sont
                    // comprises dans la p�riode de droit PC

                    RFPeriodeValiditeQdPrincipaleManager rfPerValQdPriMgr = new RFPeriodeValiditeQdPrincipaleManager();
                    rfPerValQdPriMgr.setSession(getSession());
                    rfPerValQdPriMgr.setForIdQd(periodeQdCouranteData.getId_qd());
                    rfPerValQdPriMgr.changeManagerSize(0);
                    rfPerValQdPriMgr.find();

                    Iterator<RFPeriodeValiditeQdPrincipale> rfPerValPriItr = rfPerValQdPriMgr.iterator();

                    while (rfPerValPriItr.hasNext()) {
                        RFPeriodeValiditeQdPrincipale periodeValQd = rfPerValPriItr.next();
                        if (null != periodeValQd) {
                            boolean isPeriodeTrouvee = false;
                            for (RFMutationPeriodeValiditeQd mutationPeriodeCourante : mutationsPeriodesList) {
                                if (mutationPeriodeCourante.getIdPeriode().equals(periodeValQd.getIdPeriodeValidite())) {
                                    isPeriodeTrouvee = true;
                                }
                            }

                            if (!isPeriodeTrouvee) {
                                isPeriodesComprisesDansPeriodePC = false;
                            }

                        } else {

                            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                            RFUtils.ajouterLogAdaptation(
                                    FWViewBeanInterface.ERROR,
                                    getContext().getIdAdaptationJournaliere(),
                                    idTiers,
                                    getContext().getNssTiersBeneficiaire(),
                                    getContext().getIdDecisionPc(),
                                    getContext().getNumeroDecisionPc(),
                                    "RFAdaptationJournaliereOctroiHandler.majEtatQdPeriodesQd: Impossible les p�riodes de la Qd",
                                    getLogsList());
                        }
                    }
                }

                if (isPeriodesComprisesDansPeriodePC) {

                    if (!ajoutQdsCandidatesOctroi(isSuppression, periodeQdCouranteData, mutationsPeriodesList, true)) {

                        rfQd.setCsEtat(IRFQd.CS_ETAT_QD_CLOTURE);
                        rfQd.update(getTransaction());

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext()
                                .getIdAdaptationJournaliere(), idTiers, getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(),
                                "Cl�ture de la grande Qd N�: " + periodeQdCouranteData.getId_qd(), getLogsList());

                        ajouterLogRegimeImpacte(periodeQdCouranteData, idTiers);

                    }

                    isQdCloture = true;

                } else {

                    rfQd.setCsEtat(IRFQd.CS_ETAT_QD_FERME);
                    rfQd.update(getTransaction());

                    ajoutQdsCandidatesOctroi(isSuppression, periodeQdCouranteData, null, false);
                }

                executeMutationsQd(isQdCloture, periodeQdCouranteData, mutationsPeriodesList, isSuppression);

            } else {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, getContext().getIdAdaptationJournaliere(),
                        idTiers, getContext().getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                .getNumeroDecisionPc(), "Grande Qd  N� " + periodeQdCouranteData.getId_qd()
                                + " d�j� cl�tur�", getLogsList());
            }

        } else {
            getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(), idTiers,
                    getContext().getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                            .getNumeroDecisionPc(), "Grande Qd  N� " + periodeQdCouranteData.getId_qd()
                            + " introuvable", getLogsList());
        }

    }

    protected void majGrandesQd(String dateDebutDroitPc, String dateFinDroitPc, boolean isSuppression) throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        GregorianCalendar calendar = new GregorianCalendar();

        JACalendar cal = new JACalendarGregorian();
        String idQdCourante = "";
        List<RFMutationPeriodeValiditeQd> mutationsPeriodesList = new LinkedList<RFMutationPeriodeValiditeQd>();
        boolean premiereIteration = true;
        int i = 0;

        for (RFAdaptationJournalierePeriodeQdData periodeQdCouranteData : getContext().getPeriodesQdExistantes()) {
            i++;
            if (premiereIteration) {
                idQdCourante = periodeQdCouranteData.getId_qd();
                premiereIteration = false;
            } else {
                if (!idQdCourante.equals(periodeQdCouranteData.getId_qd())) {
                    majEtatQdPeriodesQd(periodeQdCouranteData, mutationsPeriodesList, isSuppression);
                    mutationsPeriodesList = new LinkedList<RFMutationPeriodeValiditeQd>();
                    idQdCourante = periodeQdCouranteData.getId_qd();

                }
            }

            RFPeriodeValiditeQdPrincipale rfDernierePeriodeValiditeQdPrincipale = new RFPeriodeValiditeQdPrincipale();
            rfDernierePeriodeValiditeQdPrincipale.setSession(getSession());
            rfDernierePeriodeValiditeQdPrincipale.setIdPeriodeValidite(periodeQdCouranteData.getId_periode());

            rfDernierePeriodeValiditeQdPrincipale.retrieve();

            if (!rfDernierePeriodeValiditeQdPrincipale.isNew()) {

                JADate qdCouranteDateDebutPeriodeJD = new JADate(rfDernierePeriodeValiditeQdPrincipale.getDateDebut());

                if (JadeStringUtil.isBlankOrZero(rfDernierePeriodeValiditeQdPrincipale.getDateFin())) {
                    rfDernierePeriodeValiditeQdPrincipale.setDateFin(FIN_ANNEE_JJMM
                            + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(rfDernierePeriodeValiditeQdPrincipale
                                    .getDateDebut()));
                }

                JADate qdCouranteDateFinPeriodeJD = new JADate(rfDernierePeriodeValiditeQdPrincipale.getDateFin());

                JADate dateDebutPcaccordeeJD = new JADate(dateDebutDroitPc);

                if (JadeStringUtil.isBlankOrZero(dateFinDroitPc)) {
                    dateFinDroitPc = RFUtils.MAX_JADATE_VALUE;
                }

                JADate dateFinPcaccordeeJD = new JADate(dateFinDroitPc);

                // la p�riode qd est comprise dans la p�riode de la pc
                if (((cal.compare(dateDebutPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                        .compare(dateDebutPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_EQUALS))
                        && ((cal.compare(dateFinPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                                .compare(dateFinPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_EQUALS))
                        && ((cal.compare(dateDebutPcaccordeeJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                                .compare(dateDebutPcaccordeeJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_EQUALS))
                        && ((cal.compare(dateFinPcaccordeeJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER) || (cal
                                .compare(dateFinPcaccordeeJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_EQUALS))) {

                    mutationsPeriodesList.add(new RFMutationPeriodeValiditeQd(rfDernierePeriodeValiditeQdPrincipale
                            .getDateDebut(), "", rfDernierePeriodeValiditeQdPrincipale.getDateFin(), "",
                            rfDernierePeriodeValiditeQdPrincipale.getIdFamilleModification(), periodeQdCouranteData
                                    .getId_periode(), periodeQdCouranteData.getId_qd(),
                            TYPE_MUTATION_COMPRIS_DANS_PERIODE_PC));

                    // le d�but et la fin de la p�riode pc est sup�rieure au d�but de la p�riode Qd -> date de fin qd =
                    // date de d�but pc - 1 (date d�but pc si suppresion)
                } else if (((cal.compare(dateDebutPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER))
                        && ((cal.compare(dateFinPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER))) {

                    mutationsPeriodesList.add(new RFMutationPeriodeValiditeQd(rfDernierePeriodeValiditeQdPrincipale
                            .getDateDebut(), "", majDateFinQd(calendar, dateFormat, isSuppression), "",
                            rfDernierePeriodeValiditeQdPrincipale.getIdFamilleModification(), periodeQdCouranteData
                                    .getId_periode(), periodeQdCouranteData.getId_qd(),
                            TYPE_MUTATION_DATE_FIN_PERIODE_QD_EGALE_DATE_DEBUT_PERIODE_PC));

                    // le d�but de la p�riode pc est inf�rieure au d�but de la p�riode Qd et la fin de la p�riode pc
                    // est comprise dans la p�riode de la Qd -> date de d�but qd = date de fin pc + 1
                } else if (((cal.compare(dateDebutPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER) || (cal
                        .compare(dateDebutPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_EQUALS))
                        && ((cal.compare(dateFinPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER))
                        && ((cal.compare(dateFinPcaccordeeJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER))) {

                    if (!isSuppression) {
                        mutationsPeriodesList.add(new RFMutationPeriodeValiditeQd(majDateDebutQd(calendar, dateFormat),
                                "", rfDernierePeriodeValiditeQdPrincipale.getDateFin(), "",
                                rfDernierePeriodeValiditeQdPrincipale.getIdFamilleModification(), periodeQdCouranteData
                                        .getId_periode(), periodeQdCouranteData.getId_qd(),
                                TYPE_MUTATION_DATE_DEBUT_PERIODE_QD_EGALE_DATE_FIN_PERIODE_PC));
                    } else {
                        // Cas impossible si suppresion car date fin pc accord�e � l'infini (31.12.9999) et date fin Qd
                        // = fin d'ann�e au maximum
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                getContext().getIdAdaptationJournaliere(),
                                getContext().getInfoDroitPcServiceData().getIdTiers(),
                                getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(),
                                getContext().getNumeroDecisionPc(),
                                "RFAdaptationJournaliereOctroiHandler.majGrandesQd(): P�riode Pc non g�r�e si suppresion (cas 1)",
                                getLogsList());
                    }

                    // La p�riode pc est comprise dans la p�riode Qd
                } else if ((cal.compare(dateDebutPcaccordeeJD, qdCouranteDateDebutPeriodeJD) == JACalendar.COMPARE_FIRSTUPPER)
                        && (cal.compare(dateFinPcaccordeeJD, qdCouranteDateFinPeriodeJD) == JACalendar.COMPARE_FIRSTLOWER)) {

                    if (!isSuppression) {

                        mutationsPeriodesList.add(new RFMutationPeriodeValiditeQd(rfDernierePeriodeValiditeQdPrincipale
                                .getDateDebut(), majDateDebutQd(calendar, dateFormat), majDateFinQd(calendar,
                                dateFormat, isSuppression), rfDernierePeriodeValiditeQdPrincipale.getDateFin(),
                                rfDernierePeriodeValiditeQdPrincipale.getIdFamilleModification(), periodeQdCouranteData
                                        .getId_periode(), periodeQdCouranteData.getId_qd(),
                                TYPE_MUTATION_PERIODE_PC_COMPRISE_DANS_PERIODE_QD));
                    } else {
                        // Cas impossible si suppresion car date fin pc accord�e � l'infini (31.12.9999) et date fin Qd
                        // = fin d'ann�e au maximum
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                getContext().getIdAdaptationJournaliere(),
                                getContext().getInfoDroitPcServiceData().getIdTiers(),
                                getContext().getNssTiersBeneficiaire(),
                                getContext().getIdDecisionPc(),
                                getContext().getNumeroDecisionPc(),
                                "RFAdaptationJournaliereOctroiHandler.majGrandesQd(): P�riode Pc non g�r�e si suppresion (cas 2)",
                                getLogsList());
                    }

                } else {
                    getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                            getContext().getInfoDroitPcServiceData().getIdTiers(), getContext()
                                    .getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                    .getNumeroDecisionPc(),
                            "RFAdaptationJournaliereOctroiHandler.majGrandesQd(): P�riode Pc non g�r�e", getLogsList());
                }

            } else {
                getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext().getIdAdaptationJournaliere(),
                        getContext().getInfoDroitPcServiceData().getIdTiers(), getContext().getNssTiersBeneficiaire(),
                        getContext().getIdDecisionPc(), getContext().getNumeroDecisionPc(), "P�riode Grande Qd "
                                + periodeQdCouranteData.getId_periode() + " introuvable", getLogsList());
            }

            if (getContext().getPeriodesQdExistantes().size() == i) {
                majEtatQdPeriodesQd(periodeQdCouranteData, mutationsPeriodesList, isSuppression);
            }

        }
    }

    protected String modifierHistoriquePeriodeGrandeQd(String idQd, String idPeriode_qd, String idFamille,
            String typeDeModification, String dateDebut, String dateFin) throws Exception {

        RFPeriodeValiditeQdPrincipale rfNouvellePeriodeValiditeQdPrincipale = new RFPeriodeValiditeQdPrincipale();
        rfNouvellePeriodeValiditeQdPrincipale.setSession(getSession());
        rfNouvellePeriodeValiditeQdPrincipale.setIdQd(idQd);
        rfNouvellePeriodeValiditeQdPrincipale.setTypeModification(typeDeModification);
        rfNouvellePeriodeValiditeQdPrincipale.setDateModification(JACalendar.todayJJsMMsAAAA());

        if (IRFQd.CS_MODIFICATION.equals(typeDeModification)) {
            rfNouvellePeriodeValiditeQdPrincipale.setIdPeriodeValModifiePar(idPeriode_qd);
        }

        rfNouvellePeriodeValiditeQdPrincipale.setConcerne(getSession().getLabel(
                "PROCESS_ADAPTATION_JOURNALIERE_MODIFIER_GRANDE_QD_CONCERNE"));
        rfNouvellePeriodeValiditeQdPrincipale.setIdGestionnaire(getGestionnaire());

        if (!IRFQd.CS_AJOUT.equals(typeDeModification)) {
            rfNouvellePeriodeValiditeQdPrincipale.setIdFamilleModification(idFamille);
        } else {
            rfNouvellePeriodeValiditeQdPrincipale.setIdFamilleModification(retrieveProchainIdFamillePeriodeVal());
        }

        rfNouvellePeriodeValiditeQdPrincipale.setDateDebut(dateDebut);
        rfNouvellePeriodeValiditeQdPrincipale.setDateFin(dateFin);

        rfNouvellePeriodeValiditeQdPrincipale.add(getTransaction());

        if (IRFQd.CS_SUPPRESSION.equals(typeDeModification)) {
            List<String> idPeriodes = new ArrayList<String>();
            if (idPeriodesForIdQD.containsKey(idQd)) {
                idPeriodes = idPeriodesForIdQD.get(idQd);
            }

            idPeriodes.add(rfNouvellePeriodeValiditeQdPrincipale.getIdPeriodeValidite());
            idPeriodesForIdQD.put(idQd, idPeriodes);
        }

        return rfNouvellePeriodeValiditeQdPrincipale.getIdPeriodeValidite();

    }

    protected void rechercherPeriodesGrandesQd(Set<String> idsTiersMembreFamille, boolean isSuppression)
            throws Exception {

        if (!isContextEnErreur()) {

            // Recherche des grandes Qd pour la p�riode de l'adaptation
            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
            rfQdJointPerValJointDosJointTieJointDemMgr.setSession(getSession());
            rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
            // rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(rfAdaJouCourante.getIdTiersBeneficiaire());

            rfQdJointPerValJointDosJointTieJointDemMgr.setForIdsTiers(idsTiersMembreFamille);
            rfQdJointPerValJointDosJointTieJointDemMgr.setForTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            rfQdJointPerValJointDosJointTieJointDemMgr.setComprisDansCalcul(true);

            if (!isSuppression) {
                rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                        .convertDate_JJxMMxAAAA_to_AAAA(context.getInfoDroitPcServiceData().getDateDebutPcaccordee()));
                rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(context
                        .getInfoDroitPcServiceData().getDateDebutPcaccordee());
                rfQdJointPerValJointDosJointTieJointDemMgr
                        .setForDateFinBetweenPeriode(JadeStringUtil.isBlankOrZero(context.getInfoDroitPcServiceData()
                                .getDateFinPcaccordee()) ? RFUtils.MAX_JADATE_VALUE : context
                                .getInfoDroitPcServiceData().getDateFinPcaccordee());
            } else {
                rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQdPlusGrandOuEgale(PRDateFormater
                        .convertDate_JJxMMxAAAA_to_AAAA(context.getDateDebutDecision()));
                rfQdJointPerValJointDosJointTieJointDemMgr
                        .setForDateDebutBetweenPeriode(context.getDateDebutDecision());
                rfQdJointPerValJointDosJointTieJointDemMgr.setForDateFinBetweenPeriode(RFUtils.MAX_JADATE_VALUE);
            }

            rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
            rfQdJointPerValJointDosJointTieJointDemMgr.find();

            Iterator<RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande> rfQdJointPerValJointDosJointTieJointDemItr = rfQdJointPerValJointDosJointTieJointDemMgr
                    .iterator();

            while (rfQdJointPerValJointDosJointTieJointDemItr.hasNext()) {

                RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfQdJoiPerValJoiTieJoiDem = rfQdJointPerValJointDosJointTieJointDemItr
                        .next();

                if (null != rfQdJoiPerValJoiTieJoiDem) {

                    RFAdaptationJournalierePeriodeQdData rfAdaJouQdDat = new RFAdaptationJournalierePeriodeQdData(
                            rfQdJoiPerValJoiTieJoiDem.getCsGenrePCAccordee(),
                            rfQdJoiPerValJoiTieJoiDem.getIdQdPrincipale(), rfQdJoiPerValJoiTieJoiDem.getIdPeriode(),
                            rfQdJoiPerValJoiTieJoiDem.getCsTypeBeneficiaire(),
                            rfQdJoiPerValJoiTieJoiDem.getCsTypePCAccordee(), rfQdJoiPerValJoiTieJoiDem.getIsRI(),
                            RFUtils.getSoldeExcedentDeRevenu(rfQdJoiPerValJoiTieJoiDem.getIdQd(), session),
                            rfQdJoiPerValJoiTieJoiDem.getRemboursementRequerant(),
                            rfQdJoiPerValJoiTieJoiDem.getRemboursementConjoint(),
                            rfQdJoiPerValJoiTieJoiDem.getCsDegreApi());

                    context.getPeriodesQdExistantes().add(rfAdaJouQdDat);
                    context.setHasQdsActuelle(true);

                } else {
                    if (context.getTypeDeDecisionPc().equals(IPCDecision.CS_TYPE_SUPPRESSION_SC)) {
                        getContext().setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, getContext()
                                .getIdAdaptationJournaliere(), getContext().getIdTiersBeneficiaire(), getContext()
                                .getNssTiersBeneficiaire(), getContext().getIdDecisionPc(), getContext()
                                .getNumeroDecisionPc(),
                                "Impossible de retrouver la Qd concernant la p�riode de la d�cision PC", getLogsList());
                    }
                }

            }
        }

    }

    protected String retrieveProchainIdFamillePeriodeVal() throws Exception {

        int famModifCompteur = 0;
        RFPeriodeValiditeQdPrincipaleManager mgr = new RFPeriodeValiditeQdPrincipaleManager();
        mgr.setSession(session);
        mgr.setForIdFamilleMax(true);
        mgr.changeManagerSize(0);
        mgr.find(transaction);

        if (!mgr.isEmpty()) {
            RFPeriodeValiditeQdPrincipale sc = (RFPeriodeValiditeQdPrincipale) mgr.getFirstEntity();
            if (null != sc) {
                famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
            } else {
                famModifCompteur = 1;
            }
        } else {
            famModifCompteur = 1;
        }

        return Integer.toString(famModifCompteur);
    }

    public void setContext(RFAdaptationJournaliereContext context) {
        this.context = context;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    public Map<String, List<String>> getIdPeriodesForIdQD() {
        return idPeriodesForIdQD;
    }
}
