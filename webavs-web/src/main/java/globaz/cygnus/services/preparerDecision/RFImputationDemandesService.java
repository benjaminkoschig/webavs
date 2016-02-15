/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.services.RFHasDemandePCEnPremiereInstructionService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Vérifie si les demandes sont accéptées, partiellement accéptées ou refusées. Impute les montants accéptés sur les Qd.
 * 
 * @author JJE
 */
public class RFImputationDemandesService {

    private Set<RFDecisionData> decisions = null;
    private Map<String, RFImputationDemandesData> demandesAimputerMap = null;
    // idMotif système,[idMotif bd, hasMontant]
    private Map<String, String[]> idsMotifDeRefusSystemeStrMap = null;
    private boolean isAdaptationAnnuelle = false;
    private FWMemoryLog memoryLog = null;
    private final String prefixIdQdVirtuelle = "QDV";
    private Map<String, RFImputationQdsData> qdsAimputerMap = null;
    private BSession session = null;
    private BITransaction transaction = null;

    public RFImputationDemandesService(BITransaction transaction, BSession session,
            Map<String, RFImputationDemandesData> demandesAImputer, Set<RFDecisionData> decisions,
            Map<String, RFImputationQdsData> cQdsAImputerMap, FWMemoryLog memoryLog, boolean isAdaptationAnnuelle) {
        super();
        this.transaction = transaction;
        this.session = session;
        demandesAimputerMap = demandesAImputer;
        this.decisions = decisions;
        qdsAimputerMap = cQdsAImputerMap;
        this.memoryLog = memoryLog;
        this.isAdaptationAnnuelle = isAdaptationAnnuelle;

    }

    private RFImputationDemandesData ajouterMotifRefus(RFImputationDemandesData demandeCourante,
            Set<String[]> motifsDeRefusVerificationCourante) throws Exception {

        if (motifsDeRefusVerificationCourante.size() > 0) {

            BigDecimal futurMontantAccepte = new BigDecimal(demandeCourante.getMontantAPayerInitial());

            for (String[] motRefCourant : motifsDeRefusVerificationCourante) {
                if (!motRefCourant[0].equals(IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU)
                        && !motRefCourant[0].equals(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)) {
                    futurMontantAccepte = futurMontantAccepte.add(new BigDecimal(motRefCourant[1]).negate());
                }
                if (null != idsMotifDeRefusSystemeStrMap.get(motRefCourant[0])) {
                    demandeCourante.getMotifsDeRefus().add(
                            new String[] { idsMotifDeRefusSystemeStrMap.get(motRefCourant[0])[0], motRefCourant[1] });
                }
            }

            if (futurMontantAccepte.floatValue() < new BigDecimal(demandeCourante.getMontantAccepte()).floatValue()) {
                demandeCourante.setMontantAccepte(futurMontantAccepte.toString());
            }

        }

        return demandeCourante;
    }

    /**
     * Calcul du montant rétroactif d'un paiement courant
     * 
     * 
     * @param dateDebutTraitement
     * @param dateDernierPaiement_mm_yyyy
     * @param montantMensuel
     * @return montantMensuelBigDec
     * @throws Exception
     */
    private BigDecimal calculMontantRetroactif(String dateDebutTraitement, String dateDernierPaiement_mm_yyyy,
            String montantMensuel) throws Exception {

        // nombre de mois rétroactif - attention la date de dernier paiement est à prendre en compte
        int nbMoisRetroactif = JadeDateUtil
                .getNbMonthsBetween(dateDebutTraitement, "01." + dateDernierPaiement_mm_yyyy) + 1;

        // montant mensuel : montant accepté / nombre de mois
        BigDecimal montantMensuelBigDec = new BigDecimal(montantMensuel);

        return montantMensuelBigDec.multiply(new BigDecimal(nbMoisRetroactif));

    }

    /**
     * Recherche des personnes comprises dans le calcul des grandes Qds de l'année de la demande
     * 
     * @param demandeCouranteData
     * @return ArrayList<String>
     * @throws Exception
     */
    private ArrayList<String> getIdTiersComprisDansCalculGrandeQdAnneeDemande(
            RFImputationDemandesData demandeCouranteData) throws Exception {

        ArrayList<String> idTiersComprisDansCalculGrandeQdList = new ArrayList<String>();

        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(session);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(demandeCouranteData.getIdTiers());

        if (JadeStringUtil.isBlankOrZero(demandeCouranteData.getDateDebutTraitement())) {

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(demandeCouranteData.getDateFacture()));
        } else {

            if (demandeCouranteData.getDateDebutTraitement().length() == 7) {
                demandeCouranteData.setDateDebutTraitement("01." + demandeCouranteData.getDateDebutTraitement());
            }

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(demandeCouranteData.getDateDebutTraitement()));
        }
        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
        rfQdJointPerValJointDosJointTieJointDemMgr.find(transaction);

        Iterator<RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande> rfQdJointPerValJointDosJointTieJointDemItr = rfQdJointPerValJointDosJointTieJointDemMgr
                .iterator();
        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() > 0) {
            while (rfQdJointPerValJointDosJointTieJointDemItr.hasNext()) {
                RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfQdJointPerValJointDosJointTieJointDem = rfQdJointPerValJointDosJointTieJointDemItr
                        .next();
                if (rfQdJointPerValJointDosJointTieJointDem != null) {
                    idTiersComprisDansCalculGrandeQdList.add(rfQdJointPerValJointDosJointTieJointDem.getIdTiers());
                }
            }
        }

        return idTiersComprisDansCalculGrandeQdList;

        /*
         * RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = RFUtils
         * .getMembresFamilleGrandeQd(this.session, demandeCouranteData.getIdQdPrincipale()); if
         * (rfAssQdDossierJointDossierJointTiersMgr.size() > 0) { Iterator<RFAssQdDossierJointDossierJointTiers>
         * rfAssQdDossierJointDossierJointTiersIter = rfAssQdDossierJointDossierJointTiersMgr .iterator(); while
         * (rfAssQdDossierJointDossierJointTiersIter.hasNext()) { RFAssQdDossierJointDossierJointTiers
         * rfAssQdDossierJointDossierJointTiers = rfAssQdDossierJointDossierJointTiersIter .next(); if
         * (rfAssQdDossierJointDossierJointTiers != null) { if
         * (rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul()) {
         * idTiersComprisDansCalculGrandeQdList.add(rfAssQdDossierJointDossierJointTiers.getIdTiers()); } } else { throw
         * new Exception( "RFImputationDemandesService impossible de remonter les membres de la Qd principale"); } } }
         * else { throw new
         * Exception("RFImputationDemandesService impossible de remonter les membres de la Qd principale"); }
         */
    }

    /**
     * Calcul le montant mensuel du premier et des paiements suivants d'une décision mensuel
     * 
     * 
     * @param demandeCourante
     * @return BigDecimal[montant premier paiement, montant paiments suivants]
     * @throws JAException
     */
    private String getMontantMensuelArrondi5cts(RFImputationDemandesData demandeCourante) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getMontantAccepte())) {

            // Calcul du nouveau montant mensuel
            int nbMois = 0;

            // si l'année de la date de fin de traitement est la même que la date de début de traitement
            if ((JadeDateUtil.getNbYearsBetween(demandeCourante.getDateDebutTraitement(),
                    demandeCourante.getDateFinTraitement(), JadeDateUtil.YEAR_COMPARISON) <= 0)
                    && !JadeStringUtil.isBlankOrZero(demandeCourante.getDateFinTraitement())) {
                nbMois = JadeDateUtil.getNbMonthsBetween(demandeCourante.getDateDebutTraitement(),
                        demandeCourante.getDateFinTraitement()) + 1;
            } else {
                nbMois = JadeDateUtil.getNbMonthsBetween(demandeCourante.getDateDebutTraitement(), "31.12."
                        + JADate.getYear(demandeCourante.getDateDebutTraitement()).toString());
            }

            BigDecimal montantAccepteBigDec = new BigDecimal(demandeCourante.getMontantAccepte());

            BigDecimal montantMensuelBigDec = montantAccepteBigDec.divide(new BigDecimal(nbMois), 2,
                    RoundingMode.HALF_UP);

            return new BigDecimal(Math.ceil(montantMensuelBigDec.doubleValue() * 20) / 20).setScale(2,
                    RoundingMode.HALF_UP).toString();

        } else {
            return "0.00";
        }
    }

    private RFImputationQdsData getRFImputationQdsData(String idQd, boolean isQdVirtuelle) {

        if (!qdsAimputerMap.containsKey(idQd)) {
            RFImputationQdsData rfImpQdDat = new RFImputationQdsData();
            rfImpQdDat.setIdQd(idQd);
            if (isQdVirtuelle) {
                rfImpQdDat.setQdVirtuelle(true);
            }

            qdsAimputerMap.put(idQd, rfImpQdDat);
        }

        return qdsAimputerMap.get(idQd);

    }

    /**
     * 
     * Lance l'imputation des demandes sur les Qds
     * 
     * @param transaction
     * @param session
     * @param demandesAImputer
     * @param decisions
     * @param cQdsAImputerMap
     * @throws Exception
     */
    public void imputerDemandes() throws Exception {
        // Initialisation des map contenant les demandes et Qds à vérifier
        idsMotifDeRefusSystemeStrMap = RFUtils.getIdsMotifDeRefusSysteme(session, (BTransaction) transaction);

        String idMotifRefusDepassementQdBd = idsMotifDeRefusSystemeStrMap
                .get(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0];

        if (null != decisions) {
            for (RFDecisionData decisionCourante : decisions) {
                System.out.println("************ decisionCourante: " + decisionCourante.getNumeroDecision());
                for (String keyDemande : decisionCourante.getIdDemandes()) {
                    System.out.println("dateDemande: " + demandesAimputerMap.get(keyDemande).getDateDemande());

                    verifierDemande(demandesAimputerMap.get(keyDemande), decisionCourante);
                }
            }

            miseAJourDecisionApresVerification(idMotifRefusDepassementQdBd);

        } else {
            throw new Exception("RFImputationDemandesService.imputerDemandes() : Aucune décision créée");
        }
    }

    private void isForcerPaiementAccepte(RFImputationDemandesData demandeCourante) throws Exception {

        if (demandeCourante.isForcerPaiement()
        /* && !demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR) */) {

            if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())
                    && qdsAimputerMap.containsKey(demandeCourante.getIdQdPrincipale())) {

                RFImputationQdsData qdDemandeCourante = qdsAimputerMap.get(demandeCourante.getIdQdPrincipale());

                if (null != qdDemandeCourante) {

                    if (!qdDemandeCourante.isRI()) {
                        demandeCourante.setForcerPaiement(false);

                        memoryLog.logMessage(session.getLabel("PROCESS_PREPARER_DECISIONS_FORCE_PAIEMENT_REFUSE") + " "
                                + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") ",
                                FWMessage.INFORMATION, "RFImputationDemandesService.isForcerPaiementAccepte()");
                    }

                } else {
                    throw new Exception(
                            "RFImputationDemandesService.isForcerPaiementAccepte() : Impossible de retrouver la Qd");
                }
            }

        }
    }

    /******************************************************
     * /*Debug /
     ******************************************************/
    private void logQdsDemandes(long demTimeInMillisFin, long demTimeInMillisDeb,
            RFImputationDemandesData demandeCourante) {
        long demTimeInMillisFinale = demTimeInMillisFin - demTimeInMillisDeb;
        System.out.print("\tidDem:" + demandeCourante.getIdDemande());
        System.out.print("\tstat:" + demandeCourante.getStatutDemande());
        System.out.print("\taPIni:" + demandeCourante.getMontantAPayerInitial());
        System.out.print("\tacce:" + demandeCourante.getMontantAccepte());
        if (null != qdsAimputerMap.get(demandeCourante.getIdQdPrincipale())) {

            System.out.print("\tQPid:" + qdsAimputerMap.get(demandeCourante.getIdQdPrincipale()).getIdQd());
            System.out.print("\tQPres:" + qdsAimputerMap.get(demandeCourante.getIdQdPrincipale()).getMntResiduel()
                    + "(SExc:" + qdsAimputerMap.get(demandeCourante.getIdQdPrincipale()).getSoldeExcedent() + ")");
            // System.out.print("\tQPsExc:" + qdsAimputerMap.get(demandeCourante.idQdPrincipale).soldeExcedent);
            System.out.print("\tQPplaf:" + qdsAimputerMap.get(demandeCourante.getIdQdPrincipale()).isPlafonnee());

        } else {
            System.out.print("\tQPid: null");
            System.out.print("\tQPres: null");
            System.out.print("\tQPsExc: null");
            System.out.print("\tQPplaf: null");
        }
        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdAssure())) {
            if (null != qdsAimputerMap.get(demandeCourante.getIdQdAssure())) {
                System.out.print("\tQAid:" + qdsAimputerMap.get(demandeCourante.getIdQdAssure()).getIdQd());
                System.out.print("\tQAres:" + qdsAimputerMap.get(demandeCourante.getIdQdAssure()).getMntResiduel());
                System.out.print("\tQPplaf:" + qdsAimputerMap.get(demandeCourante.getIdQdAssure()).isPlafonnee());
            }
        } else {
            if (null != qdsAimputerMap.get(demandeCourante.getIdQdVirtuelle())) {
                System.out.print("\tQVid:" + qdsAimputerMap.get(demandeCourante.getIdQdVirtuelle()).getIdQd());
                System.out.print("\tQVres:" + qdsAimputerMap.get(demandeCourante.getIdQdVirtuelle()).getMntResiduel());
                System.out.print("\tQPplaf: null");
            } else {
                System.out.print("\tQAid: null");
                System.out.print("\tQAres: null");
                System.out.print("\tQPplaf: null+");
            }
        }
        System.out.print("\t tps: ");
        System.out.println(demTimeInMillisFinale);
    }

    /**
     * 
     * MàJ du motif de refus dépassement de la grande QD, selon le montant admis
     * 
     * @param demandeCourante
     * @param idMotifRefusDepassementGrandeQd
     */
    private void majMotifDeRefusGrandeQdSelonMontantAdmis(RFImputationDemandesData demandeCourante,
            String idMotifRefusDepassementGrandeQd) {

        BigDecimal montantMotifDepassementQdBigDec = new BigDecimal("0");
        BigDecimal montantAccepteDemandeBigDec = new BigDecimal(demandeCourante.getMontantAccepte());
        BigDecimal montantAccepteInitialBigDec = new BigDecimal(demandeCourante.getMontantAPayerInitial());
        String[] motifDepassementQd = null;

        for (String[] motifCourant : demandeCourante.getMotifsDeRefus()) {
            if (idMotifRefusDepassementGrandeQd.equals(motifCourant[0])) {
                montantMotifDepassementQdBigDec = new BigDecimal(motifCourant[1].replace("'", ""));
                motifDepassementQd = motifCourant;
            }
        }

        if (null != motifDepassementQd) {
            if (montantMotifDepassementQdBigDec.compareTo(new BigDecimal("0")) > 0) {
                // Si le Montant accepté est plus petit que le montant dépassement de Qd, on supprime le motif de refus
                // dépassement de Qd
                if (montantAccepteDemandeBigDec.compareTo(montantAccepteInitialBigDec
                        .add(montantMotifDepassementQdBigDec.negate())) <= 0) {
                    demandeCourante.getMotifsDeRefus().remove(motifDepassementQd);
                }
                // Sinon on met à jour le montant accepté, et on laisse le motif de refus tel quel
                else {
                    BigDecimal nouveauMontantAccepteBigDec = new BigDecimal(demandeCourante.getMontantAPayerInitial()
                            .replace("'", "")).add(montantMotifDepassementQdBigDec.negate());

                    demandeCourante.setMontantAccepte(nouveauMontantAccepteBigDec.toString());
                }
            }
        }
    }

    /**
     * Màj des motifs de refus système et ajout de l'excedent de revenu à la décision. Le motif de refus
     * "Solde excédent de recette" ne doit pas être modifié (car il est comptabilisé sur une rubrique différente) ainsi
     * que le motif de refus "dépassement grande Qd" (car si forcer paiement, il est comptabilisé sur une rubrique
     * différente)
     * 
     * 
     * @param demandeCourante
     * @param idMotifRefusSoldeExcedentBd
     * @param idMotifRefusDepassementGrandeQd
     * @param decisionCourante
     */
    private void majMotifsDeRefus(RFImputationDemandesData demandeCourante, String idMotifRefusSoldeExcedentBd,
            String idMotifRefusDepassementGrandeQd, RFDecisionData decisionCourante) {

        BigDecimal sommeTotalMotifRefus = new BigDecimal(demandeCourante.getMontantAPayerInitial()).add(new BigDecimal(
                demandeCourante.getMontantAccepte()).negate());

        for (String[] motif : demandeCourante.getMotifsDeRefus()) {

            if (idMotifRefusSoldeExcedentBd.equals(motif[0])) {

                BigDecimal montantMotifRefusSoldeExcedentBigDec = new BigDecimal(motif[1]);

                decisionCourante.setExcedentDeRevenus(new BigDecimal(decisionCourante.getExcedentDeRevenus()).add(
                        montantMotifRefusSoldeExcedentBigDec).toString());

                sommeTotalMotifRefus = sommeTotalMotifRefus.add(montantMotifRefusSoldeExcedentBigDec.negate());
            }

            if (idMotifRefusDepassementGrandeQd.equals(motif[0])) {

                BigDecimal montantMotifRefusDepassementQdBigDec = new BigDecimal(motif[1]);

                sommeTotalMotifRefus = sommeTotalMotifRefus.add(montantMotifRefusDepassementQdBigDec.negate());
            }
        }

        for (String[] motif : demandeCourante.getMotifsDeRefus()) {
            if (!(idMotifRefusSoldeExcedentBd.equals(motif[0]) || idMotifRefusDepassementGrandeQd.equals(motif[0]))) {
                BigDecimal montantMotifBigDec = new BigDecimal(motif[1]);

                if (montantMotifBigDec.floatValue() < sommeTotalMotifRefus.floatValue()) {
                    if (sommeTotalMotifRefus.floatValue() != 0) {
                        sommeTotalMotifRefus = sommeTotalMotifRefus.add(montantMotifBigDec.negate());
                    } else {
                        motif[1] = "0.00";
                    }
                } else {
                    if (montantMotifBigDec.floatValue() > sommeTotalMotifRefus.floatValue()) {
                        motif[1] = sommeTotalMotifRefus.toString();
                    }

                    sommeTotalMotifRefus = new BigDecimal("0.00");
                }
            }

            // System.out.println("id motif: " + motif[0] + "\t" + "montant: " + motif[1]);
        }

    }

    /**
     * 
     * MàJ du solde excedent de revenu selon le montant admis
     * 
     * @param qdPrincipaleData
     * @param demandeCourante
     * @param idMotifRefusSoldeExcedentBd
     * @throws Exception
     */
    private void majSoldeExcedent(RFImputationQdsData qdPrincipaleData, RFImputationDemandesData demandeCourante,
            String idMotifRefusSoldeExcedentBd) throws Exception {

        BigDecimal montantAImputerSurQdAssure = new BigDecimal(0);

        qdPrincipaleData.setHasSoldeExcedentModifie(true);

        BigDecimal montantAccepteDemandeBigDec = new BigDecimal(demandeCourante.getMontantAccepte());

        // Recherche du montant du solde excédent
        String[] motifExcedent = null;
        for (String[] motif : demandeCourante.getMotifsDeRefus()) {
            if (idMotifRefusSoldeExcedentBd.equals(motif[0])) {
                motifExcedent = motif;
            }
        }

        if ((motifExcedent != null) && !JadeStringUtil.isBlankOrZero(motifExcedent[1])) {

            BigDecimal montantSoldeExcedentBigDec = new BigDecimal(motifExcedent[1]);
            montantAImputerSurQdAssure = new BigDecimal(demandeCourante.getMontantAccepte());

            // Màj Qd principale
            if (montantSoldeExcedentBigDec.compareTo(montantAccepteDemandeBigDec) >= 0) {

                qdPrincipaleData.setSoldeExcedent(qdPrincipaleData.getSoldeExcedent().add(
                        montantAccepteDemandeBigDec.negate()));

                // Màj du motif de refus excedent de revenu
                if (JadeStringUtil.isBlankOrZero(demandeCourante.getMontantAccepte())
                        || demandeCourante.getMontantAccepte().equals("0.0")
                        || demandeCourante.getMontantAccepte().equals("0.00")) {
                    demandeCourante.getMotifsDeRefus().remove(motifExcedent);
                } else {
                    motifExcedent[1] = demandeCourante.getMontantAccepte();
                }
                demandeCourante.setStatutDemande(IRFDemande.REFUSE);
                demandeCourante.setMontantAccepte("0.00");

            } else {

                BigDecimal montantAccepteMoinsSoldeExcedentBigDec = montantAccepteDemandeBigDec
                        .add(montantSoldeExcedentBigDec.negate());

                if (qdPrincipaleData.isPlafonnee()) {
                    qdPrincipaleData.setMntResiduel(qdPrincipaleData.getMntResiduel()
                            .add(qdPrincipaleData.getMontantCorrectionBigDec())
                            .add(montantAccepteMoinsSoldeExcedentBigDec.negate()));
                }

                qdPrincipaleData.setChargeRfm(qdPrincipaleData.getChargeRfm().add(
                        montantAccepteMoinsSoldeExcedentBigDec));

                qdPrincipaleData.setSoldeExcedent(qdPrincipaleData.getSoldeExcedent().add(
                        montantSoldeExcedentBigDec.negate()));

                demandeCourante.setMontantAccepte(montantAccepteMoinsSoldeExcedentBigDec.toString());

            }
            // Màj Qd assuré
            if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdAssure())
                    || !JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdVirtuelle())) {
                RFImputationQdsData qdAssureData = qdsAimputerMap.get(!JadeStringUtil.isBlankOrZero(demandeCourante
                        .getIdQdAssure()) ? demandeCourante.getIdQdAssure() : demandeCourante.getIdQdVirtuelle());
                if (null != qdAssureData) {
                    if (qdAssureData.isPlafonnee()) {
                        qdAssureData.setMntResiduel(qdAssureData.getMntResiduel()
                                .add(montantAImputerSurQdAssure.negate())
                                .add(qdAssureData.getMontantCorrectionBigDec()));
                    }

                    qdAssureData.setChargeRfm(qdAssureData.getChargeRfm().add(montantAImputerSurQdAssure));

                    if (qdAssureData.getMntResiduel().floatValue() < 0) {
                        throw new Exception(
                                "RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() : Montant residuel < 0 (qd assuré avec solde exc)");
                        // qdAssureData.mntResiduel = new BigDecimal("0");
                    }
                } else {
                    throw new Exception(
                            "RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() :impossible de retrouver la Qd assuré");
                }
            }

            if (qdPrincipaleData.getSoldeExcedent().floatValue() < 0) {
                // qdPrincipaleData.soldeExcedent = new BigDecimal("0");
                throw new Exception("RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() : Solde excédent < 0");
            }

        } else {
            throw new Exception(
                    "RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() : impossible de retrouver le motif de refus du solde excedent");
        }

    }

    private RFImputationDemandesData majStatutDemande(String nouveauStatut, RFImputationDemandesData demandeCourante) {

        if (!demandeCourante.getStatutDemande().equals(IRFDemande.REFUSE)) {
            if (demandeCourante.getStatutDemande().equals(IRFDemande.PARTIELLEMENT_ACCEPTE)) {
                if (nouveauStatut.equals(IRFDemande.REFUSE)) {
                    demandeCourante.setStatutDemande(nouveauStatut);
                }
            } else {
                demandeCourante.setStatutDemande(nouveauStatut);
            }
        }
        // Pas de mise à jour si le montant de la demande en cours est refusé
        return demandeCourante;
    }

    private void majStatutDemandeRFCalculMontantAPayerData(RFCalculMontantAPayerData rfCalMonAPayDat,
            RFImputationDemandesData demandeCourante, boolean isQdPrincipale, boolean isQdAssure,
            RFImputationQdsData impQdDat) throws Exception {

        if (null != rfCalMonAPayDat) {

            if (rfCalMonAPayDat.isHasSoldeExcedent() && isQdPrincipale) {
                demandeCourante.setHasSoldeExcedent(true);
            }

            // REFUS
            if ((rfCalMonAPayDat.getIdStrMotifDeRefus().size() > 0)
                    && JadeStringUtil.isBlankOrZero(rfCalMonAPayDat.getMontantAccepte())) {

                demandeCourante = ajouterMotifRefus(demandeCourante, rfCalMonAPayDat.getIdStrMotifDeRefus());

                demandeCourante.setStatutDemande(IRFDemande.REFUSE);

            } else {

                // PARTIELLEMENT ACCEPTE
                if ((rfCalMonAPayDat.getIdStrMotifDeRefus().size() > 0)
                        && !JadeStringUtil.isBlankOrZero(rfCalMonAPayDat.getMontantAccepte())) {

                    demandeCourante = ajouterMotifRefus(demandeCourante, rfCalMonAPayDat.getIdStrMotifDeRefus());

                    demandeCourante = majStatutDemande(IRFDemande.PARTIELLEMENT_ACCEPTE, demandeCourante);

                    // ACCEPTE
                } else if ((rfCalMonAPayDat.getIdStrMotifDeRefus().size() == 0)
                        && !JadeStringUtil.isBlankOrZero(rfCalMonAPayDat.getMontantAccepte())) {

                    demandeCourante = majStatutDemande(IRFDemande.ACCEPTE, demandeCourante);
                }
            }

            if (isQdAssure) {
                if (impQdDat.isQdVirtuelle()) {
                    demandeCourante.setIdQdVirtuelle(impQdDat.getIdQd());
                } else {
                    demandeCourante.setIdQdAssure(impQdDat.getIdQd());
                }
            }

        } else {
            throw new Exception(
                    "RFImputationDemandesService.majStatutDemandeRFCalculMontantAPayerData() : Montant à payer égale à zéro");
        }

    }

    private RFImputationDemandesData majStatutDemandeTypesDeSoinDroitPC(Set<String[]> rfVerTypDeSoiSer,
            RFImputationDemandesData demandeCourante) throws Exception {

        if ((null != rfVerTypDeSoiSer) && (rfVerTypDeSoiSer.size() > 0)) {

            // Traitement des motifs de refus
            for (String[] rfVerTypDeSoi : rfVerTypDeSoiSer) {

                if (null != rfVerTypDeSoi) {

                    String idMotifDeRefus = rfVerTypDeSoi[0];
                    String montantMotifRefus = rfVerTypDeSoi[1];
                    String montantAccepte = rfVerTypDeSoi[2];

                    Set<String[]> motifsDeRefusTypesDeSoinDroitPC = new HashSet<String[]>();
                    motifsDeRefusTypesDeSoinDroitPC.add(new String[] { idMotifDeRefus, montantMotifRefus });

                    if (!JadeStringUtil.isBlankOrZero(idMotifDeRefus)) {
                        demandeCourante = ajouterMotifRefus(demandeCourante, motifsDeRefusTypesDeSoinDroitPC);

                        if (new BigDecimal(montantAccepte).floatValue() == 0) {

                            demandeCourante = majStatutDemande(IRFDemande.REFUSE, demandeCourante);
                        } else {

                            demandeCourante = majStatutDemande(IRFDemande.PARTIELLEMENT_ACCEPTE, demandeCourante);
                        }

                    }
                }
            }
            return demandeCourante;

        } else {
            return demandeCourante;
        }
    }

    /**
     * Màj des montants résiduels des Qds impactées par la demande et des montants des motifs de refus
     * 
     * Condition obligatoire -> SI SOLDE EXCEDENT > 0 ALORS MNT RES > SOLDE EXCEDENT
     * 
     * CAS 1) mnt demande : 1300.-
     * 
     * Validation G. Qd: mnt res = 25000.- Solde excedent = 200.-
     * 
     * -> mnt refuse solde excedent = 200.-
     * 
     * validation P. Qd: mnt res = 1000.-
     * 
     * -> mnt accepte = 1000.- -> mnt refuse = 300.-
     * 
     * résultat final : mnt accepte=1000.- mnt motif refus solde excedent = 200.- ->Mnt a payer=800.-
     * 
     * imputé sur G. qd -> 800.- imputé sur P. qd -> 1000.-
     * 
     * CAS 2) mnt demande : 1300.-
     * 
     * Validation G. Qd: mnt res = 25000.- Solde excedent = 200.-
     * 
     * -> mnt refuse solde excedent = 200.-
     * 
     * validation P. Qd: mnt res = 1200.-
     * 
     * -> mnt accepte = 1200.- -> mnt refuse = 100.-
     * 
     * résultat final : mnt accepte 1200.- mnt motif refus solde excedent = 200.-
     * 
     * imputé sur G. qd -> 1000 imputé sur P. qd -> 1200.-
     * 
     * CAS 3)SANS P. Qd ou autres sous-compteurs:
     * 
     * mnt demande : 1300.-
     * 
     * Validation G. Qd: mnt res = 25000.- Solde excedent = 200.-
     * 
     * -> mnt accepte = 1100.- -> mnt refuse solde excedent = 200.-
     * 
     * résultat final : mnt accepte 1100.- mnt motif refus solde excedent = 200.-
     * 
     * imputé sur G. qd -> 1100.- imputé sur P. qd -> 1300.-
     * 
     * CAS 4) mnt demande : 1300.-
     * 
     * Validation G. Qd: mnt res = 25000.- Solde excedent = 800.-
     * 
     * -> mnt accepte = 500.- mnt refuse solde excedent = 800.-
     * 
     * validation P. Qd: mnt res = 500.-
     * 
     * -> mnt accepte = 500.- -> mnt refuse = 800.-
     * 
     * résultat final : mnt accepte 500.- mnt motif refus solde excedent = 500.-
     * 
     * imputé sur G. qd -> 0.- imputé sur P. qd -> 500.-
     * 
     */
    private void majStatutFinalQdMotifsDeRefus(RFImputationDemandesData demandeCourante, RFDecisionData decisionCourante)
            throws Exception {

        String idMotifRefusSoldeExcedentBd = idsMotifDeRefusSystemeStrMap
                .get(IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU)[0];

        String idMotifRefusDepassementGrandeQd = idsMotifDeRefusSystemeStrMap
                .get(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0];

        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {

            RFImputationQdsData qdPrincipaleData = qdsAimputerMap.get(demandeCourante.getIdQdPrincipale());

            if (null != qdPrincipaleData) {

                // Si le sous type de soin ne concerne pas la grande Qd on enlève les motifs de refus
                // "Dépassement de qd" et "Solde excédent de revenu"
                if (RFUtils.isSousTypeDeSoinNonImputeSurGrandeQd(demandeCourante.getCodeTypeDeSoin(),
                        demandeCourante.getCodeSousTypeDeSoin())) {

                    for (String[] motifCourant : demandeCourante.getMotifsDeRefus()) {
                        if (motifCourant[0].equals(idMotifRefusSoldeExcedentBd)
                                || motifCourant[0].equals(idMotifRefusSoldeExcedentBd)) {
                            demandeCourante.getMotifsDeRefus().remove(motifCourant);
                        }
                    }

                    demandeCourante.setHasSoldeExcedent(false);
                }

                majMotifDeRefusGrandeQdSelonMontantAdmis(demandeCourante, idMotifRefusDepassementGrandeQd);

                if (demandeCourante.isHasSoldeExcedent()) {

                    majSoldeExcedent(qdPrincipaleData, demandeCourante, idMotifRefusSoldeExcedentBd);

                } else {

                    // Si le sous type de la demande ne s'impute pas sur la grande Qd, on ne la modifie pas
                    if (!RFUtils.isSousTypeDeSoinNonImputeSurGrandeQd(demandeCourante.getCodeTypeDeSoin(),
                            demandeCourante.getCodeSousTypeDeSoin())) {

                        if (qdPrincipaleData.isPlafonnee()) {
                            qdPrincipaleData.setMntResiduel(qdPrincipaleData.getMntResiduel()
                                    .add(qdPrincipaleData.getMontantCorrectionBigDec())
                                    .add(new BigDecimal(demandeCourante.getMontantAccepte()).negate()));
                        }
                        qdPrincipaleData.setChargeRfm(qdPrincipaleData.getChargeRfm().add(
                                new BigDecimal(demandeCourante.getMontantAccepte())));

                    }

                    if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdAssure())
                            || !JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdVirtuelle())) {
                        RFImputationQdsData qdAssureData = qdsAimputerMap.get(!JadeStringUtil
                                .isBlankOrZero(demandeCourante.getIdQdAssure()) ? demandeCourante.getIdQdAssure()
                                : demandeCourante.getIdQdVirtuelle());

                        if (null != qdAssureData) {
                            if (qdAssureData.isPlafonnee()) {
                                qdAssureData.setMntResiduel(qdAssureData.getMntResiduel()
                                        .add(qdAssureData.getMontantCorrectionBigDec())
                                        .add(new BigDecimal(demandeCourante.getMontantAccepte()).negate()));
                            }

                            qdAssureData.setChargeRfm(qdAssureData.getChargeRfm().add(
                                    new BigDecimal(demandeCourante.getMontantAccepte())));

                            // Euh ca devrait jamais arriver ca ...
                            if (qdAssureData.getMntResiduel().floatValue() < 0) {
                                throw new Exception(
                                        "RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() : Montant residuel < 0 (qd assuré sans solde exc)");
                            }

                        } else {
                            throw new Exception(
                                    "RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() : impossible de retrouver la Qd assuré");
                        }
                    }
                }

                if (qdPrincipaleData.getMntResiduel().floatValue() < 0) {
                    throw new Exception("Montant residuel < 0 (qd principale)");
                }

            } else {
                throw new Exception(
                        "RFImputationDemandesService.majStatutFinalQdMotifsDeRefus() : impossible de retrouver la Qd principale");
            }

        }

        // MàJ des motifs de refus autres que le "Solde excédent de recette" et "dépassement grande Qd"
        majMotifsDeRefus(demandeCourante, idMotifRefusSoldeExcedentBd, idMotifRefusDepassementGrandeQd,
                decisionCourante);

    }

    private RFImputationDemandesData majStatutQdConvention(RFCalculMontantAPayerData rfCalMonAPayDat,
            boolean isQdPrincipale, boolean isQdAssure, RFImputationDemandesData demandeCouranteData) throws Exception {

        if (null != rfCalMonAPayDat) {

            if (JadeStringUtil.isBlankOrZero(rfCalMonAPayDat.getIdConvention())) {

                if (rfCalMonAPayDat.isConventionNonTrouvee()) {
                    majStatutDemandeRFCalculMontantAPayerData(rfCalMonAPayDat, demandeCouranteData, isQdPrincipale,
                            isQdAssure, null);
                } else {

                    RFImputationQdsData impQdDat = null;

                    if (!JadeStringUtil.isBlankOrZero(rfCalMonAPayDat.getIdQd())) {
                        impQdDat = getRFImputationQdsData(rfCalMonAPayDat.getIdQd(), false);
                    }// Qd virtuelle
                    else {

                        String anneeQdPrincipaleStr = qdsAimputerMap.get(demandeCouranteData.getIdQdPrincipale())
                                .getAnneeQd();
                        if (JadeStringUtil.isBlankOrZero(anneeQdPrincipaleStr)) {
                            anneeQdPrincipaleStr = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCouranteData
                                    .getDateDemande());
                        }

                        if (!RFUtils.isSousTypeDeSoinCsConcernePlusieursPersonnes(demandeCouranteData
                                .getCsSousTypeDeSoin())) {

                            impQdDat = getRFImputationQdsData(
                                    prefixIdQdVirtuelle + demandeCouranteData.getIdTiers()
                                            + demandeCouranteData.getCodeTypeDeSoin()
                                            + demandeCouranteData.getCodeSousTypeDeSoin() + anneeQdPrincipaleStr, true);

                        } else {
                            // Si la petite Qd concerne plusieurs personnes, on recherche une petite Qd pour toutes les
                            // personnes comprises dans le calcul de toutes les Qds de l'année
                            ArrayList<String> idTiersComprisDansCalculGrandeQdList = getIdTiersComprisDansCalculGrandeQdAnneeDemande(demandeCouranteData);
                            for (String idTiers : idTiersComprisDansCalculGrandeQdList) {
                                String idQdKey = prefixIdQdVirtuelle + idTiers
                                        + demandeCouranteData.getCodeTypeDeSoin()
                                        + demandeCouranteData.getCodeSousTypeDeSoin() + anneeQdPrincipaleStr;

                                if (qdsAimputerMap.containsKey(idQdKey)) {
                                    impQdDat = getRFImputationQdsData(idQdKey, true);
                                    break;
                                }
                            }

                            if (impQdDat == null) {
                                impQdDat = getRFImputationQdsData(
                                        prefixIdQdVirtuelle + demandeCouranteData.getIdTiers()
                                                + demandeCouranteData.getCodeTypeDeSoin()
                                                + demandeCouranteData.getCodeSousTypeDeSoin() + anneeQdPrincipaleStr,
                                        true);
                            }
                        }
                    }

                    // Si la Qd n'est pas présente dans le tableau qdsAimputerMap,on l'initialise et on utilise le
                    // résultat de RFVerificationQd...Service
                    if (impQdDat.isNew()) {

                        impQdDat.setNew(false);
                        if (isQdAssure && !impQdDat.isQdVirtuelle()) {
                            impQdDat.setQdAssure(true);
                        } else if (isQdAssure && impQdDat.isQdVirtuelle()) {
                            impQdDat.setMntPlafondPot(rfCalMonAPayDat.getPlafondPotAssure());
                            impQdDat.setIdPotQdAssure(rfCalMonAPayDat.getIdPotAssure());

                            impQdDat.setAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCouranteData
                                    .getDateDemande()));
                            impQdDat.setCsCodeTypeDeSoin(demandeCouranteData.getCsSousTypeDeSoin());

                            impQdDat.setIdTiers(demandeCouranteData.getIdTiers());
                            impQdDat.setIdDossier(demandeCouranteData.getIdDossier());
                            impQdDat.setCodeTypeDeSoin(demandeCouranteData.getCodeTypeDeSoin());
                            impQdDat.setCodeSousTypeDeSoin(demandeCouranteData.getCodeSousTypeDeSoin());
                            impQdDat.setIdQdPrincipale(demandeCouranteData.getIdQdPrincipale());

                            impQdDat.setDateDebutPetiteQd(rfCalMonAPayDat.getDateDebutPetiteQd());
                            impQdDat.setDateFinPetiteQd(rfCalMonAPayDat.getDateDeFinPetiteQd());

                        } else if (isQdPrincipale) {
                            impQdDat.setQdPrincipale(true);
                            impQdDat.setSoldeExcedent(new BigDecimal(rfCalMonAPayDat.getSoldeExcedentInitial()));
                            impQdDat.setCsTypeBeneficiaire(rfCalMonAPayDat.getCsTypeBeneficiaire());
                            impQdDat.setCsTypePcAccordee(rfCalMonAPayDat.getCsTypePcAccordee());
                            impQdDat.setCsGenrePcAccordee(rfCalMonAPayDat.getCsGenrePcAccordee());
                            impQdDat.setCsDegreApi(rfCalMonAPayDat.getCsDegreApi());
                            impQdDat.setRI(rfCalMonAPayDat.isRi());
                            impQdDat.setLAPRAMS(rfCalMonAPayDat.isLaprams());
                            impQdDat.setRemboursementConjoint(rfCalMonAPayDat.getRemboursementConjoint());
                            impQdDat.setRemboursementRequerant(rfCalMonAPayDat.getRemboursementRequerant());
                            impQdDat.setAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCouranteData
                                    .getDateDemande()));
                        }

                        impQdDat.setMntResiduel(new BigDecimal(rfCalMonAPayDat.getMontantResiduelInitial()));
                        impQdDat.setPlafonnee(rfCalMonAPayDat.isPlafonnee());
                        impQdDat.setMontantAutresQdBigDec(rfCalMonAPayDat.getMontantAutresQdBigDec());

                    }
                    // Si la Qd est présente dans le tableau qdsAimputerMap, on doit imputer en fonction de celle-ci
                    else {
                        if (isQdAssure) {
                            RFVerificationQdAssureService rfVerificationQdAssureService = new RFVerificationQdAssureService();
                            rfCalMonAPayDat = rfVerificationQdAssureService.calculMontantAPayer(impQdDat
                                    .getMntResiduel().toString(), impQdDat.getMontantAutresQdBigDec(),
                                    demandeCouranteData.getMontantAPayerInitial(), demandeCouranteData
                                            .getCodeTypeDeSoin(), impQdDat.getIdQd(), impQdDat.getIdPotQdAssure(),
                                    impQdDat.getMntPlafondPot(), "", "", impQdDat.getMontantCorrectionBigDec());
                        }
                    }

                    majStatutDemandeRFCalculMontantAPayerData(rfCalMonAPayDat, demandeCouranteData, isQdPrincipale,
                            isQdAssure, impQdDat);
                }

            } else {
                majStatutDemandeRFCalculMontantAPayerData(rfCalMonAPayDat, demandeCouranteData, isQdPrincipale,
                        isQdAssure, null);
            }

        }// Si null-> pas de Qd principale -> REFUSE
        else {
            if (isQdPrincipale) {
                throw new Exception("RFImputationDemandesService.majStatutQdConvention() : pas de QD principale");
            }
        }

        return demandeCouranteData;

    }

    private void miseAJourDecisionApresVerification(String idMotifRefusDepassementQdBd) throws Exception {
        // Màj des montants à payer, partie future et partie courante de la décision
        for (RFDecisionData decisionCourante : decisions) {

            if (null != decisionCourante) {

                BigDecimal montantAPayer = new BigDecimal("0");
                BigDecimal montantDepassementQd = new BigDecimal("0");
                BigDecimal montantARembourserDsas = new BigDecimal("0");
                BigDecimal montantRestitution = new BigDecimal("0");
                boolean onlyDemandesType14 = true;

                for (String idDemande : decisionCourante.getIdDemandes()) {
                    RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

                    if (null != demandeCourante) {
                        // Recherche du type de paiemnt (futur, courant, rétro) de la décision
                        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getTypeDePaiment())) {
                            decisionCourante.setTypeDePaiment(demandeCourante.getTypeDePaiment());
                        }

                        // permet de définir si on a que des demandes de type 14 pour la décision courante (afin de
                        // définir par la suite si on imprime ou non le bordereau d'accompagnement)
                        if (!IRFCodeTypesDeSoins.TYPE_14_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL.equals(demandeCourante
                                .getCodeTypeDeSoin())) {
                            onlyDemandesType14 = false;
                        }

                        // Recherche du montant total à payer de la décision
                        montantAPayer = montantAPayer.add(new BigDecimal(demandeCourante.getMontantAccepte()));
                        BigDecimal montantMotifDepassementQd = new BigDecimal("0");

                        // Recherche du dépassement de Qd total de la décision
                        for (String motifCourant[] : demandeCourante.getMotifsDeRefus()) {
                            if (idMotifRefusDepassementQdBd.equals(motifCourant[0])) {
                                montantMotifDepassementQd = new BigDecimal(motifCourant[1].replace("'", ""));
                                montantDepassementQd = montantDepassementQd.add(montantMotifDepassementQd);
                            }
                        }

                        // Recherche du montant total restitué pour la décision courante
                        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent())
                                || !JadeStringUtil.isBlankOrZero(demandeCourante
                                        .getRestitutionMontantDepassementQdParent())) {

                            montantRestitution = montantRestitution.add(new BigDecimal(JadeStringUtil
                                    .isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent()) ? "0"
                                    : demandeCourante.getRestitutionMontantAPayeParent().replace("'", ""))
                                    .add(new BigDecimal(JadeStringUtil.isBlankOrZero(demandeCourante
                                            .getRestitutionMontantDepassementQdParent()) ? "0" : demandeCourante
                                            .getRestitutionMontantDepassementQdParent().replace("'", ""))));
                        }

                        // Recherche du montant total avancé par le DSAS
                        if (demandeCourante.isForcerPaiement()) {
                            decisionCourante.setHasDemandeForcerPaiement(true);
                            montantARembourserDsas = montantARembourserDsas.add(montantMotifDepassementQd);
                        }

                        // Màj des montants de la partie rétro et futur d'une décision de type courant
                        if (decisionCourante.getTypeDePaiment().equals(IRFTypePaiement.PAIEMENT_COURANT)) {

                            decisionCourante.setDateDebutRetro(demandeCourante.getDateDebutTraitement());
                            decisionCourante.setDateFinRetro(demandeCourante.getDateDernierPaiement_mm_yyyy());

                            String montantMensuel = getMontantMensuelArrondi5cts(demandeCourante);

                            decisionCourante.setMontantMensuel(montantMensuel);
                            demandeCourante.setMontantMensuel(montantMensuel);

                            decisionCourante.setMontantCourantPartieRetro(calculMontantRetroactif(
                                    demandeCourante.getDateDebutTraitement(),
                                    demandeCourante.getDateDernierPaiement_mm_yyyy(),
                                    demandeCourante.getMontantMensuel()).toString());

                        } else if (decisionCourante.getTypeDePaiment().equals(IRFTypePaiement.PAIEMENT_FUTURE)) {

                            String montantMensuel = getMontantMensuelArrondi5cts(demandeCourante);

                            decisionCourante.setMontantMensuel(montantMensuel);
                            demandeCourante.setMontantMensuel(montantMensuel);

                        } else if (decisionCourante.getTypeDePaiment().equals(IRFTypePaiement.PAIEMENT_RETROACTIF)
                                && (demandeCourante.getCsSousTypeDeSoin().equals(
                                        IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE) || demandeCourante
                                        .getCsSousTypeDeSoin().equals(
                                                IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE))) {

                            String montantMensuel = getMontantMensuelArrondi5cts(demandeCourante);

                            decisionCourante.setMontantMensuel(montantMensuel);
                            demandeCourante.setMontantMensuel(montantMensuel);

                            decisionCourante.setMontantCourantPartieRetro(demandeCourante.getMontantAccepte());

                        }

                    } else {
                        throw new Exception(
                                "RFImputationDemandesService.imputerDemandes() : Impossible de retrouver la demande");
                    }
                }

                // si la décision ne contient que des demandes de type 14, on ne veut pas le bordereau d'accompagnement.
                // Par défaut il y a un bordereau
                if (onlyDemandesType14) {
                    decisionCourante.setBordereauAccompagnement(Boolean.FALSE);
                }

                decisionCourante.setMontantTotalAPayer(montantAPayer.toString());

                // Si la décision possède des demandes corrigées on ajoute le montant à réstituer à la décision
                if (montantRestitution.compareTo(new BigDecimal("0")) != 0) {
                    decisionCourante.setMontantRestitution(montantRestitution.toString());
                } else {
                    decisionCourante.setMontantRestitution("");
                }

                // Si la décision possède un dépasssment de Qd on l'ajoute à la décision
                if (montantDepassementQd.compareTo(new BigDecimal(0)) == 1) {
                    decisionCourante.setDepassementQd(montantDepassementQd.toString());
                } else {
                    decisionCourante.setDepassementQd("");
                }

                // Si la décision possède un montant à rembourser par le DSAS on l'ajoute à la décision
                if (montantARembourserDsas.compareTo(new BigDecimal(0)) == 1) {
                    decisionCourante.setMontantARembourserDsas(montantARembourserDsas.toString());
                } else {
                    decisionCourante.setMontantARembourserDsas("");
                }

            } else {
                throw new Exception(
                        "RFImputationDemandesService.imputerDemandes() : Impossible de retrouver la décision");
            }
        }

    }

    private void verifierDemande(RFImputationDemandesData demandeCourante, RFDecisionData decisionCourante)
            throws Exception {

        if (null != demandeCourante) {
            /********************* DEBUG *************************************************/
            long demTimeInMillisDeb = System.currentTimeMillis();
            /***************************************************************************/
            // Si la demande concerne une FRQP pp, on vérifie uniquement qu'il existe une grande Qd
            if (!(demandeCourante.isPP() && demandeCourante.getCodeTypeDeSoin().equals(
                    RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR))) {

                if (!isAdaptationAnnuelle) {
                    // Vérification des motifs de refus utilisateurs. Permet de déterminier le statut initial de la
                    // demande
                    RFVerificationMotifsRefusUtilisateurService rfVerificationMotifsRefusUtilisateurService = new RFVerificationMotifsRefusUtilisateurService();
                    RFRechercheMotifsRefusService rfRechercheMotifsRefusService = new RFRechercheMotifsRefusService();
                    String statutVerMotRefUtiSer = rfVerificationMotifsRefusUtilisateurService
                            .verifierMotifsDeRefusUtilisateur(
                                    rfRechercheMotifsRefusService.rechercherMotifsRefus(session,
                                            demandeCourante.getIdDemande(), transaction).entrySet(),
                                    demandeCourante.getStatutDemande());

                    demandeCourante.setStatutDemande(statutVerMotRefUtiSer);

                    // Vérification si les délais sont respectés
                    RFVerificationDelaisDemandeService rfVerificationDelaisDemandeService = new RFVerificationDelaisDemandeService();
                    Set<String[]> idsMotifRefusDelais = rfVerificationDelaisDemandeService.verifierDelais_Deces_15Mois(
                            new RFVerificationDelaisDemandeData(demandeCourante.getDateFacture(), demandeCourante
                                    .getDateReception(), demandeCourante.getIdTiers(), demandeCourante
                                    .getMontantAccepte(), demandeCourante.getDateDeces()), session, demandeCourante
                                    .getIsRetro());

                    // Ajout du motif de refus
                    if ((null != idsMotifRefusDelais) && (idsMotifRefusDelais.size() > 0)) {

                        demandeCourante = ajouterMotifRefus(demandeCourante, idsMotifRefusDelais);

                        demandeCourante.setStatutDemande(IRFDemande.REFUSE);
                        demandeCourante.setMontantAccepte("0.00");
                    }
                }

                // Vérification si la limite annuelle de la QdPrincipale est dépassée
                // Si la demande n'est pas liée à une Qd principale on ajoute directement le motif de refus
                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {

                    // Si la Qd à déjà été initialisée, on calcul directement le montant a payer en fonction de celle-ci
                    if (!qdsAimputerMap.containsKey(demandeCourante.getIdQdPrincipale())) {
                        RFVerificationQdPrincipaleService rfVerificationQdPrincipaleService = new RFVerificationQdPrincipaleService();
                        RFCalculMontantAPayerData idsMotifRefusQdPrincipal = rfVerificationQdPrincipaleService
                                .montantQdPrincipale(
                                        new RFVerificationQdPrincipaleData(demandeCourante.getDateDebutTraitement(),
                                                demandeCourante.getDateFacture(), demandeCourante.getIdTiers(),
                                                demandeCourante.getMontantAPayerInitial(), demandeCourante
                                                        .getCodeTypeDeSoin(), demandeCourante.getCodeSousTypeDeSoin()),
                                        session, true, transaction, qdsAimputerMap);

                        demandeCourante = majStatutQdConvention(idsMotifRefusQdPrincipal, true, false, demandeCourante);
                    } else {

                        RFImputationQdsData impQdDat = qdsAimputerMap.get(demandeCourante.getIdQdPrincipale());

                        RFVerificationQdPrincipaleService rfVerificationQdPrincipaleService = new RFVerificationQdPrincipaleService();
                        RFCalculMontantAPayerData rfCalMonAPayDat = rfVerificationQdPrincipaleService
                                .calculMontantAPayer(impQdDat.getMntResiduel().toString(), demandeCourante
                                        .getMontantAPayerInitial(), impQdDat.getSoldeExcedent().toString(), impQdDat
                                        .getIdQd(), impQdDat.isPlafonnee(), impQdDat.getCsTypeBeneficiaire(), impQdDat
                                        .getCsTypePcAccordee(), impQdDat.getCsGenrePcAccordee(), impQdDat
                                        .getMontantAutresQdBigDec(), impQdDat.getMontantCorrectionBigDec(), impQdDat
                                        .isRI(), impQdDat.isLAPRAMS(), demandeCourante.getCodeTypeDeSoin(),
                                        demandeCourante.getCodeSousTypeDeSoin(), impQdDat.getRemboursementConjoint(),
                                        impQdDat.getRemboursementRequerant(), impQdDat.getCsDegreApi());

                        majStatutDemandeRFCalculMontantAPayerData(rfCalMonAPayDat, demandeCourante, true, false,
                                impQdDat);
                    }

                    if (qdsAimputerMap.containsKey(demandeCourante.getIdQdPrincipale())) {

                        // Vérification si la limite annuelle de la QdAssure est dépassée
                        RFVerificationQdAssureService rfVerificationQdAssureService = new RFVerificationQdAssureService();
                        RFCalculMontantAPayerData idsMotifRefusQdAssure = rfVerificationQdAssureService
                                .montantQdAssure(
                                        new RFVerificationQdAssureData(demandeCourante.getDateDebutTraitement(),
                                                demandeCourante.getDateFacture(), demandeCourante.getIdTiers(),
                                                demandeCourante.getMontantAPayerInitial(), demandeCourante
                                                        .getIdQdPrincipale(), demandeCourante.getCodeSousTypeDeSoin(),
                                                demandeCourante.getCodeTypeDeSoin(), demandeCourante.isEnfant()),
                                        session,
                                        true,
                                        transaction,
                                        qdsAimputerMap.get(demandeCourante.getIdQdPrincipale()).getCsTypeBeneficiaire(),
                                        qdsAimputerMap.get(demandeCourante.getIdQdPrincipale()).getCsGenrePcAccordee());

                        // Ajout du motif de refus, si null pas de QdAssure ou de limite type de soin
                        demandeCourante = majStatutQdConvention(idsMotifRefusQdAssure, false, true, demandeCourante);
                    } else {
                        throw new Exception(
                                "RFImputationDemandesService.verifierDemande() : impossible de retrouver la Qd principale");
                    }

                } else {

                    Set<String[]> motifsDeRefus = new HashSet<String[]>();
                    if (demandeCourante.isEnfantExclu()) {
                        motifsDeRefus.add(new String[] { IRFMotifsRefus.ID_ENFANT_EXCLUS_PC,
                                demandeCourante.getMontantAccepte() });
                    } else {
                        RFHasDemandePCEnPremiereInstructionService rfHasDemPcEnPreInsSer = new RFHasDemandePCEnPremiereInstructionService(
                                (BTransaction) transaction);

                        if (!rfHasDemPcEnPreInsSer.hasDemandePCEnPremiereInstruction(demandeCourante.getIdTiers())) {
                            motifsDeRefus.add(new String[] { IRFMotifsRefus.ID_PAS_DROIT_A_LA_PC,
                                    demandeCourante.getMontantAccepte() });
                        } else {
                            motifsDeRefus.add(new String[] { IRFMotifsRefus.ID_PAS_DE_DOCUMENTS_POUR_CALCULER_LA_PC,
                                    demandeCourante.getMontantAccepte() });
                        }
                    }

                    demandeCourante = ajouterMotifRefus(demandeCourante, motifsDeRefus);

                    demandeCourante.setStatutDemande(IRFDemande.REFUSE);
                    demandeCourante.setMontantAccepte("0.00");
                }

                // Vérification si "forcé paiement" accepté (Si pas FRQP -> doit être au RI)
                isForcerPaiementAccepte(demandeCourante);

                // Vérification si le montant de la convention est dépassé
                RFVerificationConventionService rfVerificationConventionService = new RFVerificationConventionService(
                        new RFVerificationConventionData(demandeCourante.getIdTiers(),
                                demandeCourante.getCodeSousTypeDeSoin(), demandeCourante.getCodeTypeDeSoin(),
                                demandeCourante.getIdFournisseur(), demandeCourante.getDateDebutTraitement(),
                                demandeCourante.getDateFacture(), demandeCourante.getCsEtat(),
                                demandeCourante.getMontantAPayerInitial(), demandeCourante.getIdQdPrincipale(),
                                demandeCourante.getIdDemande(), qdsAimputerMap.containsKey(demandeCourante
                                        .getIdQdPrincipale()) ? qdsAimputerMap.get(demandeCourante.getIdQdPrincipale())
                                        .getCsTypeBeneficiaire() : "", qdsAimputerMap.containsKey(demandeCourante
                                        .getIdQdPrincipale()) ? qdsAimputerMap.get(demandeCourante.getIdQdPrincipale())
                                        .getCsTypePcAccordee() : "", qdsAimputerMap.containsKey(demandeCourante
                                        .getIdQdPrincipale()) ? qdsAimputerMap.get(demandeCourante.getIdQdPrincipale())
                                        .getCsGenrePcAccordee() : "", qdsAimputerMap.containsKey(demandeCourante
                                        .getIdQdPrincipale()) ? qdsAimputerMap.get(demandeCourante.getIdQdPrincipale())
                                        .getCsDegreApi() : ""), session, true, transaction);

                RFCalculMontantAPayerData idsMotifRefusConventions = rfVerificationConventionService.convention();

                demandeCourante = majStatutQdConvention(idsMotifRefusConventions, false, false, demandeCourante);

                // Vérification des attestations
                RFVerificationAttestationsService rfVerAttSer = new RFVerificationAttestationsService(session);

                VerificationAttestation verificationAttestation = rfVerAttSer.hasAttestationSelonSousTypeDeSoin(
                        demandeCourante.getCodeTypeDeSoin(),
                        demandeCourante.getCodeSousTypeDeSoin(),
                        JadeStringUtil.isBlankOrZero(demandeCourante.getDateDebutTraitement()) ? demandeCourante
                                .getDateFacture() : demandeCourante.getDateDebutTraitement(), demandeCourante
                                .getIdDossier(), demandeCourante.getCsSousTypeDeSoin());

                if (verificationAttestation.isNiveauRefus() && !verificationAttestation.hasAttestation()) {
                    Set<String[]> motifRefusAttestationSet = new HashSet<String[]>();
                    motifRefusAttestationSet.add(new String[] { IRFMotifsRefus.ID_ATTESTATION_NON_TROUVEE,
                            demandeCourante.getMontantAPayerInitial() });

                    demandeCourante = ajouterMotifRefus(demandeCourante, motifRefusAttestationSet);
                    demandeCourante = majStatutDemande(IRFDemande.REFUSE, demandeCourante);
                }

                // Vérification spécifique à certain sous-type de soin
                RFVerificationTypesDeSoinsService rfVerificationTypesDeSoinsService = new RFVerificationTypesDeSoinsService(
                        new RFVerificationTypesDeSoinsData(demandeCourante.getCodeTypeDeSoin(),
                                demandeCourante.getCodeSousTypeDeSoin(), demandeCourante.isConventionne(),
                                demandeCourante.getMontantAPayerInitial(), demandeCourante.getMontantFacture44(),
                                demandeCourante.getMontantVerseOAI(), qdsAimputerMap.containsKey(demandeCourante
                                        .getIdQdPrincipale()) ? qdsAimputerMap.get(demandeCourante.getIdQdPrincipale())
                                        .getCsTypePcAccordee() : "",

                                qdsAimputerMap.containsKey(demandeCourante.getIdQdPrincipale()) ? qdsAimputerMap.get(
                                        demandeCourante.getIdQdPrincipale()).getCsTypeBeneficiaire() : "",
                                qdsAimputerMap.containsKey(demandeCourante.getIdQdPrincipale()) ? qdsAimputerMap.get(
                                        demandeCourante.getIdQdPrincipale()).getCsGenrePcAccordee() : ""), session

                );
                Set<String[]> rfVerTypDeSoiSer = rfVerificationTypesDeSoinsService.verifierTypesDeSoins();

                demandeCourante = majStatutDemandeTypesDeSoinDroitPC(rfVerTypDeSoiSer, demandeCourante);

                // Imputation de la demande sur les bonnes Qds
                majStatutFinalQdMotifsDeRefus(demandeCourante, decisionCourante);

            } else {
                if (JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {
                    demandeCourante.setStatutDemande(IRFDemande.REFUSE);
                    demandeCourante.getMotifsDeRefus().add(
                            new String[] { idsMotifDeRefusSystemeStrMap.get(IRFMotifsRefus.ID_PAS_DROIT_A_LA_PC)[0],
                                    demandeCourante.getMontantAPayerInitial().replace("'", "") });
                } else {
                    demandeCourante.setStatutDemande(IRFDemande.ACCEPTE);
                }
            }

            /********************* DEBUG *************************************************/
            long demTimeInMillisFin = System.currentTimeMillis();
            logQdsDemandes(demTimeInMillisFin, demTimeInMillisDeb, demandeCourante);
            /***************************************************************************/
        }
    }
}
