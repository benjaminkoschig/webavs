package globaz.osiris.db.irrecouvrable;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreManager;
import globaz.osiris.exceptions.CABusinessException;
import globaz.osiris.utils.CAUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.phenix.toolbox.CPDataDecision;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Permet de traiter et ventiler les différentes sections lors de la mise à charge des irrécouvrables. Permet également
 * de calculer les amortissements CI pour les postes de type cotisation personnelles
 * 
 * @author bjo
 * 
 */
public class CAVentilateur {
    private CAAmortissementCiContainer amortissementCiContainer;
    private List<Integer> anneePresenteList;
    private CACompteAnnexe compteAnnexe;
    private CICompteIndividuel compteIndividuelAffilie;
    private List<String> idSectionsToTreatList;
    private CAMontantAVentilerContainer montantAVentilerContainer;

    private BigDecimal montantAVentilerTotalAvantVentilation;
    private CAPosteContainer posteContainer;
    private boolean rubriqueCotPers = false;
    private BSession session;
    private TITiersViewBean tiers;

    /**
     * @param session
     * @param idSectionToTreatList
     */
    public CAVentilateur(BSession session, List<String> idSectionToTreatList) {
        posteContainer = new CAPosteContainer();
        montantAVentilerContainer = new CAMontantAVentilerContainer();
        this.session = session;
        idSectionsToTreatList = idSectionToTreatList;
        anneePresenteList = new ArrayList<Integer>();
        amortissementCiContainer = new CAAmortissementCiContainer();
    }

    private BigDecimal calculerCumulsEtatCIForAnnee(String nss, Integer annee) throws Exception {
        String param1 = NSUtil.unFormatAVS(nss);
        Integer param2 = annee;
        BTransaction transaction = null;
        BigDecimal cumulEtatCI = new BigDecimal(0);

        try {
            transaction = new BTransaction(CAIrrecouvrableUtils.createSessionPavo(session));
            transaction.openTransaction();

            BPreparedStatement cumulEtatForAnneeStatement = new BPreparedStatement(transaction);
            cumulEtatForAnneeStatement.prepareStatement(getSqlCumulEtatCIForAnnee());
            cumulEtatForAnneeStatement.clearParameters();
            cumulEtatForAnneeStatement.setString(1, param1);
            cumulEtatForAnneeStatement.setInt(2, param2);
            ResultSet resultCumulEtatForAnnee = cumulEtatForAnneeStatement.executeQuery();
            while (resultCumulEtatForAnnee.next()) {
                BigDecimal result = new BigDecimal(new FWCurrency(resultCumulEtatForAnnee.getDouble(1)).toString());
                cumulEtatCI = cumulEtatCI.add(result);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

        return cumulEtatCI;
    }

    private Map<Integer, BigDecimal> calculerEtatCi() throws Exception {
        Map<Integer, BigDecimal> montantEtatCiMap = new HashMap<Integer, BigDecimal>();

        tiers = CAIrrecouvrableUtils.retrieveTiers(compteAnnexe.getIdTiers(), session);
        if (tiers.isNew()) {
            throw new Exception(session.getLabel("IRRECOUVRABLE_TIERS_NON_TROUVE") + compteAnnexe.getIdTiers());
        }

        compteIndividuelAffilie = CAIrrecouvrableUtils.findFirstCompteIndividuel(tiers.getNumAvsActuel(), session);

        if (compteIndividuelAffilie != null) {
            Object[] ciLies = compteIndividuelAffilie.getCILies();
            List<Object> ciLiesList = new ArrayList<Object>(Arrays.asList(ciLies));
            List<String> nssLiesList = new ArrayList<String>();
            for (Object infoCi : ciLiesList) {
                String[] infoCiStr = (String[]) infoCi;
                nssLiesList.add(infoCiStr[1]);
            }

            // si aucun ci trouvé on met le ci de base
            if (nssLiesList.size() == 0) {
                if (!JadeStringUtil.isBlank(compteIndividuelAffilie.getNumeroAvs())) {
                    nssLiesList.add(compteIndividuelAffilie.getNumeroAvs());
                }
            }

            // parcours des années
            for (Integer annee : anneePresenteList) {
                // parcours des nss qui ont un CI
                for (String nssLie : nssLiesList) {
                    BigDecimal montantCi = calculerCumulsEtatCIForAnnee(nssLie, annee);
                    if (montantCi.signum() != 0) {
                        // Si année est déjà contenue dans la map, on écrase la valeur
                        montantEtatCiMap.put(annee, montantCi);
                    }
                }
            }
        }

        return montantEtatCiMap;
    }

    private void checkParamatrageVentilation(CAVPTypeDeProcedureOrdre parametrageTypeProcedure,
            CARubrique rubriqueIrrecouvrable) throws Exception {
        boolean isParametrageOK = CAUtil.isParamRubriqueIrrecTypeOrdreOk(parametrageTypeProcedure, session);
        if (!isParametrageOK) {
            throw new Exception(session.getLabel("IRRECOUVRABLE_PARAMETRAGE_TYPE_SAL_EMPL_SIMPLE_ERREUR")
                    + rubriqueIrrecouvrable.getIdExterne());
        }
    }

    private void creerAmortissementCi() throws Exception {
        // calcul les états de CI
        Map<Integer, BigDecimal> montantEtatCiMap = calculerEtatCi();
        if (!montantEtatCiMap.isEmpty()) {

            // calculer les montants d'amortissement CI
            Map<Integer, CAInfoMontantAmortissementCi> infoMontantAmortissementCiMap = posteContainer
                    .calculerMontantAmortissementCiParAnnee();

            for (Map.Entry<Integer, CAInfoMontantAmortissementCi> montantAmortissementCiEntry : infoMontantAmortissementCiMap
                    .entrySet()) {
                Integer annee = montantAmortissementCiEntry.getKey();
                CAInfoMontantAmortissementCi infoMontantAmortissementCi = montantAmortissementCiEntry.getValue();
                String genreDecision = infoMontantAmortissementCi.getGenreDecision();
                BigDecimal montantAmortissementCi = infoMontantAmortissementCi.getMontantAmortissementCi();
                montantAmortissementCi = montantAmortissementCi.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                BigDecimal montantEtatCi = montantEtatCiMap.get(annee);

                if (montantEtatCi == null) {
                    montantEtatCi = new BigDecimal(0);
                }

                // si le solde est +/- 5.- on met le montant amortissement à la même valeur que état ci
                BigDecimal soldeAmortissement = montantEtatCi.subtract(montantAmortissementCi);
                if (CAUtil.montantDiffPlusOuMoins5(soldeAmortissement)) {
                    montantAmortissementCi = new BigDecimal(montantEtatCi.intValue());
                }

                amortissementCiContainer
                        .addAmortissementCi(montantAmortissementCi, montantEtatCi, annee, genreDecision);
            }
        }
    }

    /**
     * Affiche dans la console le contenu du container des amortissements CI
     * 
     */
    public void displayAmortissementCiContainer() {
        System.out.println(amortissementCiContainer.toString());
    }

    /**
     * Affiche dans la console le contenu des années présentes
     */
    public void displayAnneePresenteList() {
        System.out.println("--------------------------- AFFICHAGE DES ANNEES PRESENTES ---------------------------");
        for (Integer annee : anneePresenteList) {
            System.out.println(annee);
        }
    }

    /**
     * Affiche dans la console le contenu du container des montants à ventiler
     */
    public void displayMontantAVentilerContainer() {
        System.out.println(montantAVentilerContainer.toString());
    }

    /**
     * Affiche dans la console le contenu du container des postes
     */
    public void displayPostContainer() {
        System.out.println(posteContainer.toString());
    }

    /**
     * Executer l'amortissement des CI. 1) traite les postes de type cot.pers 2) crée les amortissement si il y a des
     * rubriques cotisations personnelles
     * 
     * @throws Exception
     */
    public void executerAmortissementCi() throws Exception {
        rubriqueCotPers = traiterPostePourAmortissementCi();
        if (hasRubriqueCotPers()) {
            creerAmortissementCi();
        }
    }

    /**
     * Permet d'exécuter la ventilation sur les postes
     * 
     * @throws Exception
     */
    public void executerVentilation() throws Exception {
        for (String idSectionToTreat : idSectionsToTreatList) {
            if (CAIrrecouvrableUtils.isIdSectionValide(idSectionToTreat)) {
                traiterSection(idSectionToTreat);
            }
        }
        traiterPosteNegatif();
        traiterMontantAVentilerNegatif();
        initAnneePresenteList();
        // displayAnneePresenteList();
        // displayMontantAVentilerContainer();

        // initialisation du montant à ventiler avant la ventilation
        montantAVentilerTotalAvantVentilation = montantAVentilerContainer.calculerMontantAVentilerTotal();
        if (montantAVentilerContainer.getMontantAVentilerForAnnee(0).signum() == -1) {
            throw new Exception(session.getLabel("IRRECOUVRABLE_MONTANT_A_VENTILER_POUR_ANNEE_0_EST_NEGATIF"));
        }
        traiterVentilation();
    }

    /**
     * Retourne une nouvelle treeMap contenant tous les amortissements pour l'affichage
     * 
     * @return
     */
    public Map<Integer, CAAmortissementCi> getAmortissementCiTries() {
        return new TreeMap<Integer, CAAmortissementCi>(amortissementCiContainer.getAmortissementCiMap());
    }

    /**
     * Retourne l'année de la section en fonction de la date de fin de période de la section
     * 
     * @param CASection
     * @return Integer Année de fin de période de la section.
     * @throws JAException
     *             si la date n'est pas une date Globaz valide
     * @throws Exception
     */
    private Integer getAnneeFinPeriodeSectionValide(CASection section) throws JAException, Exception {
        Integer ecritureAnneeCotisation = 0;
        // Déterminer l'année de cotisation en fonction de la date de fin de période de la section
        if (JadeDateUtil.isGlobazDate(section.getDateFinPeriode())) {
            ecritureAnneeCotisation = JADate.getYear(section.getDateFinPeriode()).intValue();
        } else {
            throw new Exception("Unable to retrieve dateFinPeriode for section : " + section.getIdExterne());
        }
        return ecritureAnneeCotisation;
    }

    public CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    public CICompteIndividuel getCompteIndividuelAffilie() {
        return compteIndividuelAffilie;
    }

    public BigDecimal getMontantAVentilerTotalAvantVentilation() {
        return montantAVentilerTotalAvantVentilation;
    }

    public BigDecimal[] getMontantDuAndAffecteTotal() {
        return posteContainer.calculerCumulMontantDuAndAffecte();
    }

    /**
     * Retourne une nouvelle treeMap contenant tous les postes pour l'affichage. Les postes sont triés lors de la
     * création de la treeMap
     * 
     * @return
     */
    public Map<CAKeyPosteContainer, CAPoste> getPostesTries() {
        return new TreeMap<CAKeyPosteContainer, CAPoste>(posteContainer.getPostesMap());
    }

    private String getSqlCumulEtatCIForAnnee() {
        String schema = Jade.getInstance().getDefaultJdbcSchema();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SUM(CASE WHEN KBTEXT IN(0," + CIEcriture.CS_EXTOURNE_2 + ", " + CIEcriture.CS_EXTOURNE_6
                + ", " + CIEcriture.CS_EXTOURNE_8 + ")");
        sql.append(" THEN +KBMMON ELSE -KBMMON END)");
        sql.append(" FROM " + schema + ".CIECRIP EC");
        sql.append(" INNER JOIN " + schema + ".CIINDIP CI ON (EC.KAIIND = CI.KAIIND)");
        sql.append(" WHERE KANAVS = ?");
        sql.append(" AND KBNANN = ?");
        sql.append(" AND KBTCPT IN (" + CIEcriture.CS_CI + ", " + CIEcriture.CS_CI_SUSPENS + ", "
                + CIEcriture.CS_TEMPORAIRE + ", " + CIEcriture.CS_TEMPORAIRE_SUSPENS + ")");
        sql.append(" AND (KBTGEN IN (" + CIEcriture.CS_CIGENRE_2 + ", " + CIEcriture.CS_CIGENRE_3 + ", "
                + CIEcriture.CS_CIGENRE_4 + ", " + CIEcriture.CS_CIGENRE_9 + ")");
        sql.append(" OR (KBTGEN = " + CIEcriture.CS_CIGENRE_7 + " AND KBTSPE <> " + CIEcriture.CS_NONFORMATTEUR_SALARIE
                + "))");

        return sql.toString();
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    public boolean hasRubriqueCotPers() {
        return rubriqueCotPers;
    }

    private void initAnneePresenteList() {
        List<Integer> anneesPresentesInPosteContainer = posteContainer.findAnneesContenues();
        List<Integer> anneesPresentesInMontantAVentilerContainer = montantAVentilerContainer.findAnneesContenues();

        for (Integer annee : anneesPresentesInPosteContainer) {
            anneePresenteList.add(annee);
        }

        for (Integer annee : anneesPresentesInMontantAVentilerContainer) {
            if (!anneePresenteList.contains(annee)) {
                anneePresenteList.add(annee);
            }
        }

        Collections.sort(anneePresenteList);
    }

    /**
     * supprime tous les montantsAVentiler (pour une année connue) négatifs et les placent dans les montant à ventiler
     * pour l'année 0
     */
    private void traiterMontantAVentilerNegatif() {
        // supprimer les montants négatifs et les placer dans années 0 si le montant pour l'année 0 ne devient pas
        // négatif. sinon on répartit les montants sur les années
        BigDecimal montantTotalSupprime = montantAVentilerContainer.deleteAllMontantsAVentilerNegatifs();
        BigDecimal montantAVentilerAnnee0 = montantAVentilerContainer.getMontantAVentilerForAnnee(0);
        BigDecimal nouveauMontantAVentilerAnnee0 = montantAVentilerAnnee0.add(montantTotalSupprime);

        // si le nouveau montant à ventiler pour l'année 0 est >=0 on effectue réellement le traitement
        if (nouveauMontantAVentilerAnnee0.signum() >= 0) {
            montantAVentilerContainer.addMontantInMontantAVentilerContainer(0, montantTotalSupprime);
        }
        // sinon on répartit le montantTotalSupprime sur toutes les années en partant de la plus petites
        else {
            montantAVentilerContainer.repartirMontantSurAutreMontantAVentiler(montantTotalSupprime);
        }
    }

    /**
     * supprime tous les postes négatifs et ajoute les montants aux montants à ventiler par année
     */
    private void traiterPosteNegatif() {
        // supprimer les postes dont le montant total du est négatif et les placer dans année du poste
        Map<Integer, BigDecimal> montantSupprimeParAnnee = posteContainer.deleteAllPostesNegatifs();

        for (Map.Entry<Integer, BigDecimal> entry : montantSupprimeParAnnee.entrySet()) {
            Integer annee = entry.getKey();
            BigDecimal montant = entry.getValue();
            montantAVentilerContainer.addMontantInMontantAVentilerContainer(annee, montant.negate());
        }
    }

    private boolean traiterPostePourAmortissementCi() throws Exception {
        Map<CAKeyPosteContainer, List<String>> posteCotPersMap = posteContainer.findPosteCotPers();

        if (posteCotPersMap.size() < 1) {
            return false;
        }

        // parcours des clés des postes
        for (Map.Entry<CAKeyPosteContainer, List<String>> posteCotPersEntry : posteCotPersMap.entrySet()) {
            CAKeyPosteContainer keyPosteContainer = posteCotPersEntry.getKey();
            List<String> idSectionList = posteCotPersEntry.getValue();

            // parcours des idSections
            for (String idSection : idSectionList) {
                if (!keyPosteContainer.getType().equals(CATypeLigneDePoste.SIMPLE)) {
                    posteContainer.addMessageErreurToLigneDePoste(keyPosteContainer, idSection,
                            session.getLabel("IRRECOUVRABLE_RUBRIQUE_IRREC_PAS_DE_TYPE_SIMPLE"));
                    continue;
                }

                CASection section = CAIrrecouvrableUtils.retrieveSection(idSection, session);

                // récupération des infos des décisions cot pers
                List<CPDataDecision> dataDecisionList = CPToolBox.getDataDecisionPourInscriptionIrrec(session,
                        keyPosteContainer.getAnnee().toString(), compteAnnexe.getIdTiers());

                // si aucune décision => message d'erreur au niveau de la ligne
                if (dataDecisionList.size() == 0) {
                    posteContainer.addMessageErreurToLigneDePoste(
                            keyPosteContainer,
                            idSection,
                            session.getLabel("IRRECOUVRABLE_AUCUNE_DECISION_COTPERS_TROUVEE_POUR_LA_SECTION")
                                    + section.getIdExterne());
                    continue;
                }
                // déclaration de dataDecision
                CPDataDecision dataDecision = null;

                // Si plusieurs décisions, vérification qu'elles ne se chevauchent pas, dans ce cas on pourra les
                // cumuler.
                if (dataDecisionList.size() > 1) {
                    CPDataDecision dataDecisionCumul = dataDecisionList.get(0);
                    BigDecimal cotisationAVSCumul = new BigDecimal(dataDecisionCumul.getCotisationAvs().toString());
                    BigDecimal revenuCiCumul = new BigDecimal(dataDecisionCumul.getRevenuCi().toString());
                    boolean chevauchementTrouve = false;
                    for (int i = 1; i < dataDecisionList.size(); i++) {
                        CPDataDecision dataDecisionCurrent = dataDecisionList.get(i);
                        if (!dataDecisionCumul.checkChevauchementDateDecision(session, dataDecisionCurrent)) {
                            cotisationAVSCumul = cotisationAVSCumul.add(new BigDecimal(dataDecisionCurrent
                                    .getCotisationAvs().toString()));
                            revenuCiCumul = revenuCiCumul.add(new BigDecimal(dataDecisionCurrent.getRevenuCi()
                                    .toString()));
                        } else {
                            chevauchementTrouve = true;
                            break;
                        }
                    }

                    if (!chevauchementTrouve) {
                        dataDecisionCumul.setCotisationAvs(cotisationAVSCumul);
                        dataDecisionCumul.setRevenuCi(revenuCiCumul);
                        dataDecision = dataDecisionCumul;

                    } else {
                        posteContainer.addMessageErreurToLigneDePoste(keyPosteContainer, idSection,
                                session.getLabel("IRRECOUVRABLE_PROBLEME_RECUP_DECISION_COTPERS_POUR_ANNEE")
                                        + keyPosteContainer.getAnnee());
                        continue;
                    }

                } else {
                    dataDecision = dataDecisionList.get(0);
                }

                if (!JadeDateUtil.isGlobazDate(dataDecision.getDateFacturation())) {
                    posteContainer.addMessageErreurToLigneDePoste(keyPosteContainer, idSection,
                            session.getLabel("_IRRECOUVRABLE_DATE_FACTURATION_NE_DOIT_PAS_ETRE_VIDE"));
                    break;
                }
                boolean hasMontantErreur = false;
                if (dataDecision.getRevenuCi().signum() == 0) {
                    posteContainer.addMessageErreurToLigneDePoste(keyPosteContainer, idSection,
                            session.getLabel("IRRECOUVRABLE_REVENU_CI_EST_A_ZERO"));
                    hasMontantErreur = true;
                }

                if (dataDecision.getCotisationAvs().signum() == 0) {
                    posteContainer.addMessageErreurToLigneDePoste(keyPosteContainer, idSection,
                            session.getLabel("IRRECOUVRABLE_COTISATION_AVS_EST_A_ZERO"));
                    hasMontantErreur = true;
                }

                if (!hasMontantErreur) {
                    posteContainer.updateLigneDePoste(keyPosteContainer, idSection, dataDecision.getRevenuCi(),
                            dataDecision.getCotisationAvs(), dataDecision.getGenre());
                    continue;
                }
            }
        }
        return true;
    }

    private void traiterSection(String idSection) throws Exception {
        CASection section = CAIrrecouvrableUtils.retrieveSection(idSection, session);
        if (section.isNew()) {
            throw new Exception("Unable to retrieve section for id : " + idSection);
        }
        if (compteAnnexe == null) {
            compteAnnexe = CAIrrecouvrableUtils.retrieveCompteAnnexe(section.getIdCompteAnnexe(), session);
        }
        // On récupère l'année la plus grande des écritures de la section
        Integer anneeCotisationMax;
        try {
            anneeCotisationMax = CAIrrecouvrableUtils.findAnneeCotisationEcritureMaxForSection(section, session);
        } catch (CABusinessException e) {
            // En cas de problème, on retourne l'année max à zéro
            anneeCotisationMax = 0;
        }
        // String maxAnnee = (String)maxAnneeCotisationManager.getFirstEntity();
        CAEcritureManager ecritureManager = CAIrrecouvrableUtils.findEcritureForSection(section, session);
        // traite chaque écriture et génère la ligne de poste appropriée dans le PosteContainer
        for (int i = 0; i < ecritureManager.size(); i++) {
            CAEcriture ecriture = (CAEcriture) ecritureManager.get(i);
            BigDecimal ecritureMontant = new BigDecimal(ecriture.getMontant());
            Integer ecritureAnneeCotisation = 0;
            if (!JadeStringUtil.isBlankOrZero(ecriture.getAnneeCotisation())) {
                ecritureAnneeCotisation = Integer.valueOf(ecriture.getAnneeCotisation());
            } else if (anneeCotisationMax != 0) {
                ecritureAnneeCotisation = anneeCotisationMax;
            } else {
                ecritureAnneeCotisation = getAnneeFinPeriodeSectionValide(section);
            }

            // si il s'agit d'un paiement
            if (CAIrrecouvrableUtils.isPaiement(ecriture.getCompte().getNatureRubrique())) {
                // on ajoute le montant aux montants à ventiler (année = 0)
                montantAVentilerContainer.addMontantInMontantAVentilerContainer(0, ecritureMontant.negate());
                continue;
            }

            // si il s'agit d'une compensation
            if (CAIrrecouvrableUtils.isCompensation(ecriture.getCompte().getNatureRubrique())) {
                // on recherche la section de base de la compensation

                CASection sectionDeBaseCompensation = null;
                if (!JadeStringUtil.isBlankOrZero(ecriture.getIdSectionCompensation())) {
                    sectionDeBaseCompensation = CAIrrecouvrableUtils.retrieveSection(
                            ecriture.getIdSectionCompensation(), session);
                }

                if ((sectionDeBaseCompensation == null)
                        || sectionDeBaseCompensation.isNew()
                        || APISection.ID_CATEGORIE_SECTION_PAIEMENT_AVANCE.equals(sectionDeBaseCompensation
                                .getCategorieSection())) {
                    montantAVentilerContainer.addMontantInMontantAVentilerContainer(0, ecritureMontant.negate());
                    continue;
                } else {
                    Integer anneeCompensation = JADate.getYear(sectionDeBaseCompensation.getDateFinPeriode())
                            .intValue();
                    montantAVentilerContainer.addMontantInMontantAVentilerContainer(anneeCompensation,
                            ecritureMontant.negate());
                    continue;
                }
            }

            // recherche de la rubrique dans le paramétrage (idRubrique = ecriture.getIdCompte)
            CAVPTypeDeProcedureOrdreManager parametrageTypeProcedureManager = CAIrrecouvrableUtils
                    .findParametrageForRubrique(ecriture.getIdCompte(), session);

            // si rubrique non trouvée
            if (parametrageTypeProcedureManager.size() == 0) {
                // si c'est un montant négatif on ajoute le montant aux montants à ventiler
                if (CAIrrecouvrableUtils.isMontantNegatif(ecritureMontant)) {
                    montantAVentilerContainer.addMontantInMontantAVentilerContainer(ecritureAnneeCotisation,
                            ecritureMontant.negate());
                    continue;
                } else {
                    // créer la ligne de poste de type rubrique indeterminée
                    CARubrique rubriqueIndeterminee = CAIrrecouvrableUtils.retrieveRubrique(ecriture.getIdCompte(),
                            session);
                    posteContainer.addLigneInPosteContainer(ecriture.getIdCompte(), ecritureAnneeCotisation, true,
                            section.getIdSection(), section.getIdExterne(), section.getDescription(), ecritureMontant,
                            CATypeLigneDePoste.SIMPLE, ecriture.getIdCompte(), rubriqueIndeterminee.getIdExterne(),
                            rubriqueIndeterminee.getDescription(), "11");
                    continue;
                }
            }

            else if (parametrageTypeProcedureManager.size() == 1) {
                CAVPTypeDeProcedureOrdre parametrageTypeProcedure = (CAVPTypeDeProcedureOrdre) parametrageTypeProcedureManager
                        .getFirstEntity();

                if (APIVPDetailMontant.CS_VP_MONTANT_SIMPLE.equals(parametrageTypeProcedure.getTypeOrdre())) {
                    // création d'une ligne de poste simple
                    CARubrique rubriqueIrrecouvrable = CAIrrecouvrableUtils.retrieveRubrique(
                            parametrageTypeProcedure.getIdRubriqueIrrecouvrable(), session);
                    // contrôle du paramétrage des ventilations
                    checkParamatrageVentilation(parametrageTypeProcedure, rubriqueIrrecouvrable);

                    posteContainer.addLigneInPosteContainer(ecriture.getIdCompte(), ecritureAnneeCotisation, false,
                            section.getIdSection(), section.getIdExterne(), section.getDescription(), ecritureMontant,
                            CATypeLigneDePoste.SIMPLE, rubriqueIrrecouvrable.getIdRubrique(),
                            rubriqueIrrecouvrable.getIdExterne(), rubriqueIrrecouvrable.getDescription(),
                            parametrageTypeProcedure.getOrdre());
                    continue;
                }
                throw new Exception(session.getLabel("IRRECOUVRABLE_PARAMETRAGE_SALARIE_EMPLOYEUR_PAS_CORRECT")
                        + ecriture.getCompte().getIdExterne());
            } else if (parametrageTypeProcedureManager.size() == 2) {
                CAVPTypeDeProcedureOrdre parametrageTypeProcedure1 = (CAVPTypeDeProcedureOrdre) parametrageTypeProcedureManager
                        .get(0);
                CAVPTypeDeProcedureOrdre parametrageTypeProcedure2 = (CAVPTypeDeProcedureOrdre) parametrageTypeProcedureManager
                        .get(1);
                if (!CAIrrecouvrableUtils.checkParametreSalarieEmployeur(parametrageTypeProcedure1,
                        parametrageTypeProcedure2)) {
                    throw new Exception(session.getLabel("IRRECOUVRABLE_PARAMETRAGE_SALARIE_EMPLOYEUR_PAS_CORRECT")
                            + ecriture.getCompte().getIdExterne());
                }

                // rechercher le taux
                CATauxRubriques tauxRubrique = CAIrrecouvrableUtils.findTauxRubrique(ecriture);
                if ((tauxRubrique != null) && !tauxRubrique.isNew()) {
                    // calculer la part employeur et la part salarié
                    BigDecimal tauxSalarie = new BigDecimal(tauxRubrique.getTauxSalarie());
                    BigDecimal tauxEmployeur = new BigDecimal(tauxRubrique.getTauxEmployeur());
                    BigDecimal tauxTotal = CAIrrecouvrableUtils.calculerTauxTotal(tauxSalarie, tauxEmployeur);

                    BigDecimal montantSalarie = CAIrrecouvrableUtils.calculerPartSalarie(ecritureMontant, tauxSalarie,
                            tauxTotal);
                    BigDecimal montantEmployeur = CAIrrecouvrableUtils.calculerPartEmployeur(ecritureMontant,
                            tauxEmployeur, tauxTotal, montantSalarie);

                    CARubrique rubriqueIrrecouvrableEmployeur;
                    CARubrique rubriqueIrrecouvrableSalarie;
                    String ordreParamSalarie = "";
                    String ordreParamEmployeur = "";

                    if (parametrageTypeProcedure1.getTypeOrdre().equals(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)) {
                        ordreParamEmployeur = parametrageTypeProcedure1.getOrdre();
                        ordreParamSalarie = parametrageTypeProcedure2.getOrdre();
                        rubriqueIrrecouvrableEmployeur = CAIrrecouvrableUtils.retrieveRubrique(
                                parametrageTypeProcedure1.getIdRubriqueIrrecouvrable(), session);
                        checkParamatrageVentilation(parametrageTypeProcedure1, rubriqueIrrecouvrableEmployeur);
                        rubriqueIrrecouvrableSalarie = CAIrrecouvrableUtils.retrieveRubrique(
                                parametrageTypeProcedure2.getIdRubriqueIrrecouvrable(), session);
                        checkParamatrageVentilation(parametrageTypeProcedure2, rubriqueIrrecouvrableSalarie);
                    } else {
                        ordreParamEmployeur = parametrageTypeProcedure2.getOrdre();
                        ordreParamSalarie = parametrageTypeProcedure1.getOrdre();
                        rubriqueIrrecouvrableSalarie = CAIrrecouvrableUtils.retrieveRubrique(
                                parametrageTypeProcedure1.getIdRubriqueIrrecouvrable(), session);
                        checkParamatrageVentilation(parametrageTypeProcedure1, rubriqueIrrecouvrableSalarie);
                        rubriqueIrrecouvrableEmployeur = CAIrrecouvrableUtils.retrieveRubrique(
                                parametrageTypeProcedure2.getIdRubriqueIrrecouvrable(), session);
                        checkParamatrageVentilation(parametrageTypeProcedure2, rubriqueIrrecouvrableEmployeur);
                    }

                    posteContainer.addLigneInPosteContainer(ecriture.getIdCompte(), ecritureAnneeCotisation, false,
                            section.getIdSection(), section.getIdExterne(), section.getDescription(), montantSalarie,
                            CATypeLigneDePoste.SALARIE, rubriqueIrrecouvrableSalarie.getIdRubrique(),
                            rubriqueIrrecouvrableSalarie.getIdExterne(), rubriqueIrrecouvrableSalarie.getDescription(),
                            ordreParamSalarie);
                    posteContainer.addLigneInPosteContainer(ecriture.getIdCompte(), ecritureAnneeCotisation, false,
                            section.getIdSection(), section.getIdExterne(), section.getDescription(), montantEmployeur,
                            CATypeLigneDePoste.EMPLOYEUR, rubriqueIrrecouvrableEmployeur.getIdRubrique(),
                            rubriqueIrrecouvrableEmployeur.getIdExterne(),
                            rubriqueIrrecouvrableEmployeur.getDescription(), ordreParamEmployeur);

                    continue;
                } else {
                    throw new Exception(session.getLabel("IRRECOUVRABLE_TAUX_RUBRIQUE_NON_TROUVE")
                            + ecriture.getCompte().getIdExterne());
                }
            } else {
                throw new Exception(session.getLabel("IRRECOUVRABLE_PARAMETRAGE_PAS_CORRECT")
                        + ecriture.getCompte().getIdExterne());
            }
        }
    }

    private void traiterVentilation() {
        for (Integer annee : anneePresenteList) {
            if (annee == 0) {
                continue;
            }

            BigDecimal montantAVentilerForAnnee = montantAVentilerContainer.getMontantAVentilerForAnnee(annee);
            BigDecimal cumulMontantDuForAnnee = posteContainer.calculerCumulMontantDuForAnnee(annee);
            BigDecimal montantAVentilerRestant = montantAVentilerForAnnee.subtract(cumulMontantDuForAnnee);

            // si montant à ventiler >= montant cumulé par année alors on ventile tous les postes pour l'année
            // directement
            if (montantAVentilerForAnnee.signum() != 0) {
                if (montantAVentilerForAnnee.compareTo(cumulMontantDuForAnnee) >= 0) {
                    posteContainer.ventilerForAnnee(annee);
                } else {
                    for (int ordrePriorite = 1; ordrePriorite <= 10; ordrePriorite++) {
                        BigDecimal cumulMontantDuForAnneeAndOrdrePriorite = posteContainer
                                .calculerCumulMontantDuForAnneeAndOrdrePriorite(annee, "" + ordrePriorite);
                        if (cumulMontantDuForAnneeAndOrdrePriorite.signum() == 0) {
                            continue;
                        }
                        if (montantAVentilerForAnnee.compareTo(cumulMontantDuForAnneeAndOrdrePriorite) >= 0) {
                            // Bug 9167 - Traitement irrécouvrable - la ventilation par année et par priorité ne prend
                            // pas le bon montant à ventiler
                            BigDecimal montantVentileForAnneeAndPriorite = posteContainer.ventilerForAnneeAndPriorite(
                                    annee, "" + ordrePriorite);
                            montantAVentilerForAnnee = montantAVentilerForAnnee
                                    .subtract(montantVentileForAnneeAndPriorite);
                        } else {
                            BigDecimal montantVentileProrata = posteContainer.ventilerProrataForAnneeAndPriorite(annee,
                                    "" + ordrePriorite, montantAVentilerForAnnee,
                                    cumulMontantDuForAnneeAndOrdrePriorite);

                            montantAVentilerRestant = montantAVentilerForAnnee.subtract(montantVentileProrata);
                            break;
                        }
                    }
                }

                if (montantAVentilerRestant.signum() > 0) {
                    montantAVentilerContainer.addMontantInMontantAVentilerContainer(0, montantAVentilerRestant);
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        // Traitement de l'année zéro, dernière boucle
        BigDecimal montantAVentilerForAnnee = montantAVentilerContainer.getMontantAVentilerForAnnee(0);
        if (montantAVentilerForAnnee.signum() != 0) {
            // Il reste un montant à ventiler sur tous les postes par priorité
            for (int ordrePriorite = 1; ordrePriorite <= 11; ordrePriorite++) {
                BigDecimal cumulMontantDuForOrdrePriorite = posteContainer.calculerCumulMontantDuForPriorite(""
                        + ordrePriorite);
                if (montantAVentilerForAnnee.compareTo(cumulMontantDuForOrdrePriorite) >= 0) {
                    posteContainer.ventilerForOrdrePriorite("" + ordrePriorite);
                } else {
                    posteContainer.ventilerProrataForPriorite("" + ordrePriorite, montantAVentilerForAnnee,
                            cumulMontantDuForOrdrePriorite);
                    break;
                }
                BigDecimal montantAVentilerRestant = montantAVentilerForAnnee.subtract(cumulMontantDuForOrdrePriorite);
                if (montantAVentilerRestant.signum() > 0) {
                    montantAVentilerContainer.setMontantAVentiler(0, montantAVentilerRestant);
                    montantAVentilerForAnnee = montantAVentilerContainer.getMontantAVentilerForAnnee(0);
                } else {
                    break;
                }
            }

        }
    }
}
