package globaz.osiris.db.irrecouvrable;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.APIVPPoste;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CARecouvrementOperationsSections;
import globaz.osiris.db.comptes.CARecouvrementOperationsSectionsManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
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
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Permet de traiter et ventiler les diff�rentes sections lors du traitement de recouvrement. Permet �galement de
 * calculer les recouvrements CI pour les postes de type cotisation personnelles
 * 
 * @author sch
 * 
 */
public class CARecouvrementVentilateur {

    private Integer anneeBaseManuelle;
    private List<Integer> anneePresenteList;
    private CACompteAnnexe compteAnnexe;
    private CICompteIndividuel compteIndividuelAffilie;
    private String idCompteAnnexe = "";
    private List<String> idSectionsToTreatList;
    private Map<CAKeyCumulMontantsOperationsSections, CACumulMontantsNotesDeCreditSection> notesDeCreditDesCotisationsDesSectionsMap;

    private BigDecimal montantARecouvrir;
    private BigDecimal montantRestantARecouvrir;
    private CARecouvrementBaseAmortissementContainer recouvrementBaseAmortissementContainer;
    private CARecouvrementCiContainer recouvrementCiContainer;

    private CARecouvrementPosteContainer recouvrementPosteContainer;
    private boolean rubriqueCotPers = false;
    private BSession session;
    private TITiersViewBean tiers;
    private BigDecimal montantNoteCredit;
    private List<String> messageErreurList;

    public List<String> getMessageErreurList() {
        return messageErreurList;
    }

    /**
     * Constructeur
     * 
     * @param session
     * @param idSectionToTreatList
     * @param idCompteAnnexe
     * @param recouvrementBaseAmortissement
     * @param montantARecouvrir
     * @param anneeBaseManuelle
     */
    public CARecouvrementVentilateur(BSession session, List<String> idSectionToTreatList, String idCompteAnnexe,
            CARecouvrementBaseAmortissementContainer recouvrementBaseAmortissement, BigDecimal montantARecouvrir,
            Integer anneeBaseManuelle) {
        recouvrementPosteContainer = new CARecouvrementPosteContainer();
        this.session = session;
        idSectionsToTreatList = idSectionToTreatList;
        anneePresenteList = new ArrayList<Integer>();
        recouvrementCiContainer = new CARecouvrementCiContainer();
        this.idCompteAnnexe = idCompteAnnexe;
        recouvrementBaseAmortissementContainer = recouvrementBaseAmortissement;
        this.montantARecouvrir = montantARecouvrir;
        this.anneeBaseManuelle = anneeBaseManuelle;
        notesDeCreditDesCotisationsDesSectionsMap = new HashMap<CAKeyCumulMontantsOperationsSections, CACumulMontantsNotesDeCreditSection>();
        messageErreurList = new ArrayList<String>();
    }

    /**
     * Cette m�thode permet de calculer le cumul de l'�tat des CI pour une ann�e donn�e
     * 
     * @param nss
     * @param annee
     * @return BigDecimal cumul de l'�tat CI pour une ann�e
     * @throws Exception
     */
    private BigDecimal calculerCumulsEtatCIForAnnee(String nss, Integer annee) throws Exception {
        String paramNSS = NSUtil.unFormatAVS(nss);
        Integer paramAnnee = annee;
        BTransaction transaction = null;
        BigDecimal cumulEtatCI = new BigDecimal(0);

        try {
            transaction = new BTransaction(CAIrrecouvrableUtils.createSessionPavo(session));
            transaction.openTransaction();

            BPreparedStatement cumulEtatForAnneeStatement = new BPreparedStatement(transaction);
            cumulEtatForAnneeStatement.prepareStatement(getSqlCumulEtatCIForAnnee());
            cumulEtatForAnneeStatement.clearParameters();
            cumulEtatForAnneeStatement.setString(1, paramNSS);
            cumulEtatForAnneeStatement.setInt(2, paramAnnee);
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

    /**
     * Cette m�thode permet de calculer l'�tat, elle retourne une Map qui contient le cumul de l'�tat CI pour une ann�e
     * 
     * @return Map <Integer ann�e, BigDecimal cumulEtatCI>
     * @throws Exception
     */
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

            // si aucun ci trouv� on met le ci de base
            if (nssLiesList.size() == 0) {
                if (!JadeStringUtil.isBlank(compteIndividuelAffilie.getNumeroAvs())) {
                    nssLiesList.add(compteIndividuelAffilie.getNumeroAvs());
                }
            }

            // parcours des ann�es
            for (Integer annee : anneePresenteList) {
                // parcours des nss qui ont un CI
                for (String nssLie : nssLiesList) {
                    BigDecimal montantCi = calculerCumulsEtatCIForAnnee(nssLie, annee);
                    if (montantCi.signum() != 0) {
                        // Si ann�e est d�j� contenue dans la map, on �crase la valeur
                        montantEtatCiMap.put(annee, montantCi);
                    }
                }
            }
        }

        return montantEtatCiMap;
    }

    /**
     * Cette m�thode permet de v�rifier diff�rents points du param�trage des ventilations
     * 
     * @param parametrageTypeProcedure
     * @throws Exception
     */
    private void checkParamatrageVentilationRecouvrement(CAVPTypeDeProcedureOrdre parametrageTypeProcedure)
            throws Exception {
        CARubrique rubrique = CAIrrecouvrableUtils.retrieveRubrique(parametrageTypeProcedure.getIdRubrique(), session);
        if (JadeStringUtil.isBlankOrZero(parametrageTypeProcedure.getIdRubriqueIrrecouvrable())
                || JadeStringUtil.isBlankOrZero(parametrageTypeProcedure.getIdRubriqueRecouvrement())) {

            throw new Exception(session.getLabel("RECOUVREMENT_PARAMETRAGE_INCORRECT") + rubrique.getIdExterne());
        }
    }

    /**
     * Cette m�thode cr��e les recouvrementPostes en se basant sur le param�trage des ventilations
     * 
     * @param idCompteAnnexe
     * @param baseAmortissementSelectionnee
     * @param anneeBaseManuelle
     * @throws Exception
     */
    private void creerPostesFromParametreVentilation(String idCompteAnnexe,
            CARecouvrementBaseAmortissementContainer baseAmortissementSelectionnee, Integer anneeBaseManuelle)
            throws Exception {

        initCompteAnnexe(idCompteAnnexe);

        CAVPTypeDeProcedureOrdreManager typeDeProcedureManager = new CAVPTypeDeProcedureOrdreManager();
        typeDeProcedureManager.setSession(session);
        typeDeProcedureManager.setForTypeProcedure(APIVPPoste.CS_PROCEDURE_AUTRE_PROCEDURE);
        typeDeProcedureManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        typeDeProcedureManager.setOrderBy(CAVPTypeDeProcedureOrdreManager.ORDER_BY_ORDRE_ASC_ID_EXTERNE_RUBRIQUE_ASC);
        typeDeProcedureManager.find();

        for (int i = 0; i < typeDeProcedureManager.size(); i++) {

            CAVPTypeDeProcedureOrdre procedure = (CAVPTypeDeProcedureOrdre) typeDeProcedureManager.getEntity(i);
            checkParamatrageVentilationRecouvrement(procedure);
            // Si pas de base d'amortissement
            if (baseAmortissementSelectionnee.getRecouvrementAmortissementMap().isEmpty()) {
                recouvrementPosteContainer.addPosteContainer(anneeBaseManuelle, procedure.getIdRubrique(),
                        procedure.getIdRubriqueIrrecouvrable(), procedure.getNumeroRubriqueIrrec(),
                        procedure.getIdRubriqueRecouvrement(), procedure.getNumeroRubriqueRecouvrement(),
                        procedure.getLibelleRubrique(procedure.getIdRubriqueRecouvrement()), new BigDecimal(0),
                        new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
                        procedure.getOrdre(), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
                        CATypeDeRecouvrementPoste.getEnumFromCodeSystem(procedure.getTypeOrdre()));
            } else {
                // Si base d'amortissement
                Map<Integer, CARecouvrementBaseAmortissement> baseAmortissementMap = baseAmortissementSelectionnee
                        .getRecouvrementAmortissementMap();

                for (Map.Entry<Integer, CARecouvrementBaseAmortissement> baseAmortissementEntry : baseAmortissementMap
                        .entrySet()) {
                    Integer annee = baseAmortissementEntry.getKey();
                    BigDecimal cumulCotisationAmortie = new BigDecimal(0);
                    BigDecimal valeurInitialeCotAmortie = new BigDecimal(0);
                    BigDecimal cumulCotisationRecouvrement = new BigDecimal(0);
                    BigDecimal valeurInitialeCotRecouvrement = new BigDecimal(0);

                    // R�cup�re les infos du compteur d'amortissement
                    CACompteur compteurCumulCotisationAmortie = CAUtil.getCompteur(idCompteAnnexe, annee,
                            procedure.getIdRubriqueIrrecouvrable(), session);
                    if (compteurCumulCotisationAmortie != null) {
                        cumulCotisationAmortie = new BigDecimal(compteurCumulCotisationAmortie.getCumulCotisation());
                        valeurInitialeCotAmortie = new BigDecimal(compteurCumulCotisationAmortie.getValeurInitialeCot());
                    }
                    // r�cup�re les infos du compteur de recouvrement
                    CACompteur compteurCumulCotisationRecouvrement = CAUtil.getCompteur(idCompteAnnexe, annee,
                            procedure.getIdRubriqueRecouvrement(), session);
                    if (compteurCumulCotisationRecouvrement != null) {
                        cumulCotisationRecouvrement = new BigDecimal(
                                compteurCumulCotisationRecouvrement.getCumulCotisation());
                        valeurInitialeCotRecouvrement = new BigDecimal(
                                compteurCumulCotisationRecouvrement.getValeurInitialeCot());
                    }

                    String idRubriqueRecouvrement = procedure.getIdRubriqueRecouvrement();
                    String libelleRubrique = procedure.getLibelleRubrique(idRubriqueRecouvrement);

                    recouvrementPosteContainer.addPosteContainer(annee, procedure.getIdRubrique(),
                            procedure.getIdRubriqueIrrecouvrable(), procedure.getNumeroRubriqueIrrec(),
                            procedure.getIdRubriqueRecouvrement(), procedure.getNumeroRubriqueRecouvrement(),
                            libelleRubrique, cumulCotisationAmortie.negate(), new BigDecimal(0),
                            cumulCotisationRecouvrement, new BigDecimal(0), new BigDecimal(0), procedure.getOrdre(),
                            valeurInitialeCotAmortie.negate(), valeurInitialeCotRecouvrement, new BigDecimal(0),
                            CATypeDeRecouvrementPoste.getEnumFromCodeSystem(procedure.getTypeOrdre()));
                }
            }
        }
    }

    /**
     * Initialisation et chargement du compte annexe sur la base d'un idCompte annexe
     * 
     * @param idCompteAnnexe
     * @throws Exception
     */
    private void initCompteAnnexe(String idCompteAnnexe) throws Exception {
        if (compteAnnexe == null) {
            compteAnnexe = CAIrrecouvrableUtils.retrieveCompteAnnexe(idCompteAnnexe, session);
        }
    }

    /**
     * Cette m�thode cr��e le recouvrementCiContainer
     * 
     * @throws Exception
     */
    private void creerRecouvrementCi() throws Exception {
        // calcul les �tats de CI
        Map<Integer, BigDecimal> montantEtatCiMap = calculerEtatCi();
        if (!montantEtatCiMap.isEmpty()) {

            // calculer les montants de recouvrement CI
            Map<Integer, CAInfoMontantRecouvrementCi> infoMontantRecouvrementCiMap = recouvrementPosteContainer
                    .calculerMontantRecouvrementCiParAnnee();

            for (Map.Entry<Integer, CAInfoMontantRecouvrementCi> montantRecouvrementCiEntry : infoMontantRecouvrementCiMap
                    .entrySet()) {
                Integer annee = montantRecouvrementCiEntry.getKey();
                CAInfoMontantRecouvrementCi infoMontantRecouvrementCi = montantRecouvrementCiEntry.getValue();
                String genreDecision = infoMontantRecouvrementCi.getGenreDecision();
                BigDecimal montantRecouvrementCi = infoMontantRecouvrementCi.getMontantRecouvrementCi();
                // Suppression des centimes
                BigInteger bMontantRecouvrementCi = montantRecouvrementCi.toBigInteger();
                montantRecouvrementCi = new BigDecimal(bMontantRecouvrementCi);
                BigDecimal montantEtatCi = montantEtatCiMap.get(annee);

                if (montantEtatCi == null) {
                    montantEtatCi = new BigDecimal(0);
                }

                // si le solde est +/- 5.- on met le montant amortissement � la m�me valeur que �tat ci
                BigDecimal soldeRecouvrement = montantEtatCi.subtract(montantRecouvrementCi);
                if (CAUtil.montantDiffPlusOuMoins5(soldeRecouvrement)) {
                    montantRecouvrementCi = new BigDecimal(montantEtatCi.intValue());
                }

                recouvrementCiContainer.addRecouvrementCi(montantRecouvrementCi, montantEtatCi, annee, genreDecision);
            }
        }
    }

    /**
     * Cette m�thode charge les notes de cr�dit des op�rations de nature de rubrique "cotisation avec et sans masse" des
     * sections s�lectionn�es et les stocke dans la map
     * CACumulMontantsOperationsSectionsMap, la cl� de cette
     * map est ann�e de cotisation et idRubrique
     * 
     * @return montant cumul� des notes de cr�dits des bases d'amortissements s�lectionn�es
     * @throws Exception
     */
    private BigDecimal chargerNotesDeCreditDesCotisationsDesSectionsSelectionnees() throws Exception {
        BigDecimal montantCumulNotesCredit = new BigDecimal(0);
        CARecouvrementOperationsSectionsManager recouvrementOperationsSectionsManager = new CARecouvrementOperationsSectionsManager();
        recouvrementOperationsSectionsManager.setSession(session);
        recouvrementOperationsSectionsManager.setForIdSectionIn(idSectionsToTreatList);
        recouvrementOperationsSectionsManager.find();

        if (!recouvrementOperationsSectionsManager.isEmpty()) {
            for (int i = 0; i < recouvrementOperationsSectionsManager.getSize(); i++) {

                CARecouvrementOperationsSections recouvrementOperationsSections = (CARecouvrementOperationsSections) recouvrementOperationsSectionsManager
                        .getEntity(i);
                int anneeCotisation = 0;
                if (JadeStringUtil.isBlankOrZero(recouvrementOperationsSections.getAnnee())) {
                    try {
                        CASection section = new CASection();
                        section.setSession(session);
                        section.setIdSection(recouvrementOperationsSections.getIdSection());
                        section.retrieve();
                        if (!section.isNew()) {
                            anneeCotisation = CAIrrecouvrableUtils.findAnneeCotisationEcritureMaxForSection(section,
                                    session);
                        }
                        if (anneeCotisation == 0) {
                            anneeCotisation = getAnneeFinPeriodeSectionValide(section);
                        }
                    } catch (CABusinessException e) {
                        // En cas de probl�me, on retourne l'ann�e max � z�ro
                        anneeCotisation = 0;
                    }

                } else {
                    anneeCotisation = Integer.parseInt(recouvrementOperationsSections.getAnnee());
                }
                // Cr�er la cl�
                CAKeyCumulMontantsOperationsSections keyCumulMontantsOperationsSections = new CAKeyCumulMontantsOperationsSections(
                        anneeCotisation, recouvrementOperationsSections.getIdRubriqueRecouvrement());

                CACumulMontantsNotesDeCreditSection cumulMontantsOperationsSection = null;

                if (!notesDeCreditDesCotisationsDesSectionsMap.containsKey(keyCumulMontantsOperationsSections)) {
                    if (recouvrementOperationsSections.getAnnee().equals(anneeBaseManuelle.toString())
                            || recouvrementBaseAmortissementContainer.getRecouvrementAmortissementMap().containsKey(
                                    Integer.parseInt(recouvrementOperationsSections.getAnnee()))) {
                        if (CATypeDeRecouvrementPoste.SALARIE.equals(CATypeDeRecouvrementPoste
                                .getEnumFromCodeSystem(recouvrementOperationsSections.getTypeOrdre()))
                                || CATypeDeRecouvrementPoste.SIMPLE.equals(CATypeDeRecouvrementPoste
                                        .getEnumFromCodeSystem(recouvrementOperationsSections.getTypeOrdre()))) {
                            montantCumulNotesCredit = montantCumulNotesCredit.add(new BigDecimal(
                                    recouvrementOperationsSections.getCumulMontant()).abs());
                        }
                    }
                    BigDecimal montantCotisation = new BigDecimal(recouvrementOperationsSections.getCumulMontant())
                            .abs();
                    BigDecimal montantSalarie = montantCotisation.divide(new BigDecimal(2), 5,
                            BigDecimal.ROUND_HALF_EVEN);
                    montantSalarie = JANumberFormatter.round(montantSalarie, 0.05, 2, JANumberFormatter.NEAR);
                    BigDecimal montantEmployeur = montantCotisation.subtract(montantSalarie);

                    cumulMontantsOperationsSection = new CACumulMontantsNotesDeCreditSection(montantCotisation,
                            montantSalarie, montantEmployeur);
                    notesDeCreditDesCotisationsDesSectionsMap.put(keyCumulMontantsOperationsSections,
                            cumulMontantsOperationsSection);
                } else {
                    CACumulMontantsNotesDeCreditSection cumulMontantsNotesDeCreditSectionToUpdate = notesDeCreditDesCotisationsDesSectionsMap
                            .get(keyCumulMontantsOperationsSections);
                    if (recouvrementOperationsSections.getAnnee().equals(anneeBaseManuelle.toString())
                            || recouvrementBaseAmortissementContainer.getRecouvrementAmortissementMap().containsKey(
                                    Integer.parseInt(recouvrementOperationsSections.getAnnee()))) {

                        if (recouvrementOperationsSections.getAnnee().equals(anneeBaseManuelle.toString())
                                || recouvrementBaseAmortissementContainer.getRecouvrementAmortissementMap()
                                        .containsKey(Integer.parseInt(recouvrementOperationsSections.getAnnee()))) {
                            if (CATypeDeRecouvrementPoste.SALARIE.equals(CATypeDeRecouvrementPoste
                                    .getEnumFromCodeSystem(recouvrementOperationsSections.getTypeOrdre()))
                                    || CATypeDeRecouvrementPoste.SIMPLE.equals(CATypeDeRecouvrementPoste
                                            .getEnumFromCodeSystem(recouvrementOperationsSections.getTypeOrdre()))) {
                                montantCumulNotesCredit = montantCumulNotesCredit.add(new BigDecimal(
                                        recouvrementOperationsSections.getCumulMontant()).abs());
                            }
                        }
                    }
                    BigDecimal montantCotisation = new BigDecimal(recouvrementOperationsSections.getCumulMontant())
                            .abs();
                    BigDecimal montantSalarie = montantCotisation.divide(new BigDecimal(2), 5,
                            BigDecimal.ROUND_HALF_EVEN);
                    montantSalarie = JANumberFormatter.round(montantSalarie, 0.05, 2, JANumberFormatter.NEAR);
                    BigDecimal montantEmployeur = montantCotisation.subtract(montantSalarie);

                    montantCotisation = montantCotisation.add(cumulMontantsNotesDeCreditSectionToUpdate
                            .getMontantSimple());
                    montantSalarie = montantSalarie.add(cumulMontantsNotesDeCreditSectionToUpdate.getMontantSalarie());
                    montantEmployeur = montantEmployeur.add(cumulMontantsNotesDeCreditSectionToUpdate
                            .getMontantEmployeur());

                    cumulMontantsOperationsSection = new CACumulMontantsNotesDeCreditSection(montantCotisation,
                            montantSalarie, montantEmployeur);
                    notesDeCreditDesCotisationsDesSectionsMap.put(keyCumulMontantsOperationsSections,
                            cumulMontantsOperationsSection);
                }
            }
        }
        // TODO sch 28 ao�t 2014 : enlever les displays des infos
        System.out.println("Montant cumul Notes de cr�dits : " + montantCumulNotesCredit);
        displayNotesDeCreditMap();
        return montantCumulNotesCredit;
    }

    /**
     * Affiche dans la console le contenu des ann�es pr�sentes
     */
    public void displayAnneePresenteList() {
        System.out.println("--------------------------- AFFICHAGE DES ANNEES PRESENTES ---------------------------");
        for (Integer annee : anneePresenteList) {
            System.out.println(annee);
        }
    }

    /**
     * Affiche dans la console le contenu du container des recouvrements CI
     * 
     */
    public void displayRecouvrementCiContainer() {
        System.out.println(recouvrementCiContainer.toString());
    }

    /**
     * Affiche dans la console le contenu du container des recouvrementPostes
     */
    public void displayRecouvrementPostContainer() {
        System.out.println(recouvrementPosteContainer.toString());
    }

    /**
     * Affiche dans la console les montants de la map des notes de cr�dit
     */
    public void displayNotesDeCreditMap() {
        for (Map.Entry<CAKeyCumulMontantsOperationsSections, CACumulMontantsNotesDeCreditSection> entry : notesDeCreditDesCotisationsDesSectionsMap
                .entrySet()) {
            CAKeyCumulMontantsOperationsSections key = entry.getKey();
            CACumulMontantsNotesDeCreditSection value = entry.getValue();
            System.out.println("Key : " + key);
            System.out.println("Montant simple    : " + value.getMontantSimple());
            System.out.println("Montant salari�   : " + value.getMontantSalarie());
            System.out.println("Montant employeur : " + value.getMontantEmployeur());
            System.out.println("------------------------------------------");
        }

    }

    /**
     * Executer le recouvrement des CI. 1) traite les postes de type cot.pers 2) cr�e les recouvrements si il y a des
     * rubriques cotisations personnelles
     * 
     * @throws Exception
     */
    public void executerRecouvrementCi() throws Exception {
        rubriqueCotPers = traiterPostePourRecouvrementCi();
        if (hasRubriqueCotPers()) {
            creerRecouvrementCi();
        }
    }

    /**
     * Permet d'executer la ventilation sur les postes, cette m�thode est appel�e lorsqu'on clique sur
     * "actualiser le recouvrement"
     * elle ne cr�er pas les recouvrementPostes et ne traite pas les notes de cr�dit
     * 
     * @param recouvrementPosteContainer
     * @throws Exception
     */
    public void executerVentilation(CARecouvrementPosteContainer recouvrementPosteContainer) throws Exception {
        initCompteAnnexe(idCompteAnnexe);
        this.recouvrementPosteContainer = recouvrementPosteContainer;
        initAnneePresenteList();
        traiterVentilation();
    }

    /**
     * Permet d'ex�cuter la ventilation sur les postes
     * 
     * @throws Exception
     */
    public void executerVentilation() throws Exception {
        creerPostesFromParametreVentilation(idCompteAnnexe, recouvrementBaseAmortissementContainer, anneeBaseManuelle);
        initAnneePresenteList();
        montantNoteCredit = chargerNotesDeCreditDesCotisationsDesSectionsSelectionnees();
        if (montantNoteCredit.compareTo(montantARecouvrir) > 0) {
            addMessageErreur(session.getLabel("RECOUVREMENT_NOTE_CREDIT_SUPP_MONTANT_RECOUVRIR"));
        }
        traiterNoteDeCredit();
        displayRecouvrementPostContainer();
        traiterVentilation();
    }

    /**
     * Cette m�thode permet de r�cup�rer un recouvrementPosteContainer
     * 
     * @return recouvrementPosteContainer
     */
    public CARecouvrementPosteContainer getRecouvrementPosteContainer() {
        return recouvrementPosteContainer;
    }

    /**
     * Retourne l'ann�e de la section en fonction de la date de fin de p�riode de la section
     * 
     * @param CASection
     * @return Integer Ann�e de fin de p�riode de la section.
     * @throws JAException
     *             si la date n'est pas une date Globaz valide
     * @throws Exception
     */
    private Integer getAnneeFinPeriodeSectionValide(CASection section) throws JAException, Exception {
        Integer ecritureAnneeCotisation = 0;
        // D�terminer l'ann�e de cotisation en fonction de la date de fin de p�riode de la section
        if (JadeDateUtil.isGlobazDate(section.getDateFinPeriode())) {
            ecritureAnneeCotisation = JADate.getYear(section.getDateFinPeriode()).intValue();
        } else {
            throw new Exception("Unable to retrieve dateFinPeriode for section : " + section.getIdExterne());
        }
        return ecritureAnneeCotisation;
    }

    /**
     * Retourne le compte annexe
     * 
     * @return compteAnnexe
     */
    public CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * Retourne le compte individuel affili�
     * 
     * @return compteIndividuelAffilie
     */
    public CICompteIndividuel getCompteIndividuelAffilie() {
        return compteIndividuelAffilie;
    }

    /**
     * Retourne le montant total des colonnes de l'�cran : (amortissement, d�j� recouvert, recouvrement) de
     * tous les postes.
     * [0] = cumulCotisationAmortieTotal
     * [1] = cumulRecouvrementCotisationAmortieTotal
     * [3] = cumulRecouvrement
     * 
     * @return BigDecimal[]
     */
    public BigDecimal[] getCumulMontantColonnes() {
        return recouvrementPosteContainer.calculerCumulMontantColonnes();
    }

    /**
     * Retourne une nouvelle treeMap contenant tous les amortissements pour l'affichage
     * 
     * @return
     */
    public Map<Integer, CARecouvrementCi> getRecouvrementCiTries() {
        return new TreeMap<Integer, CARecouvrementCi>(recouvrementCiContainer.getRecouvrementCiMap());
    }

    /**
     * Retourne une nouvelle treeMap contenant tous les postes pour l'affichage. Les postes sont tri�s lors de la
     * cr�ation de la treeMap
     * 
     * @return
     */
    public Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> getRecouvrementPostesTries() {
        Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> treeMap = new TreeMap<CARecouvrementKeyPosteContainer, CARecouvrementPoste>(
                recouvrementPosteContainer.getRecouvrementPostesMap());

        return treeMap;
    }

    /**
     * Cette m�thode pr�pare la requ�te sql pour rechercher le cumul de l'Etat CI pour une ann�e
     * 
     * @return String sql
     */
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

    /**
     * Retourne le tiers
     * 
     * @return tiers
     */
    public TITiersViewBean getTiers() {
        return tiers;
    }

    /**
     * Cette m�thode indique si des rubriques cot. pers. ont �t� trouv�es
     * 
     * @return
     */
    public boolean hasRubriqueCotPers() {
        return rubriqueCotPers;
    }

    /**
     * Initialise la liste les ann�es pr�sentes dans recouvrementPosteContainer
     */
    private void initAnneePresenteList() {
        List<Integer> anneesPresentesInPosteContainer = recouvrementPosteContainer.findAnneesContenues();

        for (Integer annee : anneesPresentesInPosteContainer) {
            anneePresenteList.add(annee);
        }

        Collections.sort(anneePresenteList);
    }

    /**
     * Cette m�thode permet de traiter les postes pour le recouvrement CI en fonction des d�cisions
     * Un message est ajout� dans recouvrementPosteContainer si aucune d�cision trouv�e, on contr�le �galement qu'il n'y
     * ait pas de chevauchement
     * des d�cisions
     * 
     * @return boolean
     * @throws Exception
     */
    private boolean traiterPostePourRecouvrementCi() throws Exception {
        Map<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteCotPersMap = recouvrementPosteContainer
                .findRecouvrementPosteCotPers();

        if (recouvrementPosteCotPersMap.size() < 1) {
            return false;
        }

        // parcours des cl�s des postes
        for (Map.Entry<CARecouvrementKeyPosteContainer, CARecouvrementPoste> recouvrementPosteCotPersEntry : recouvrementPosteCotPersMap
                .entrySet()) {
            CARecouvrementKeyPosteContainer keyRecouvrementPosteContainer = recouvrementPosteCotPersEntry.getKey();
            CARecouvrementPoste recouvrementPoste = recouvrementPosteCotPersEntry.getValue();

            // r�cup�ration des infos des d�cisions cot pers
            List<CPDataDecision> dataDecisionList = CPToolBox.getDataDecisionPourInscriptionIrrec(session,
                    keyRecouvrementPosteContainer.getAnnee().toString(), compteAnnexe.getIdTiers());

            // si aucune d�cision => message d'erreur au niveau de la ligne
            if (dataDecisionList.size() == 0) {
                recouvrementPosteContainer.addMessageErreurToPoste(keyRecouvrementPosteContainer,
                        session.getLabel("RECOUVREMENT_AUCUNE_DECISION_COTPERS_TROUVEE_POUR_LE_POSTE")
                                + recouvrementPoste.getIdRubriqueRecouvrement());
                continue;
            }

            // d�claration de dataDecision
            CPDataDecision dataDecision = null;

            // Si plusieurs d�cisions, v�rification qu'elles ne se chevauchent pas, dans ce cas on pourra les
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
                        revenuCiCumul = revenuCiCumul.add(new BigDecimal(dataDecisionCurrent.getRevenuCi().toString()));
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
                    recouvrementPosteContainer.addMessageErreurToPoste(keyRecouvrementPosteContainer,
                            session.getLabel("IRRECOUVRABLE_PROBLEME_RECUP_DECISION_COTPERS_POUR_ANNEE")
                                    + keyRecouvrementPosteContainer.getAnnee());
                    continue;
                }

            } else {
                dataDecision = dataDecisionList.get(0);
            }

            if (!JadeDateUtil.isGlobazDate(dataDecision.getDateFacturation())) {
                recouvrementPosteContainer.addMessageErreurToPoste(keyRecouvrementPosteContainer,
                        session.getLabel("_IRRECOUVRABLE_DATE_FACTURATION_NE_DOIT_PAS_ETRE_VIDE"));
                break;
            }
            boolean hasMontantErreur = false;
            if (dataDecision.getRevenuCi().signum() == 0) {
                recouvrementPosteContainer.addMessageErreurToPoste(keyRecouvrementPosteContainer,
                        session.getLabel("IRRECOUVRABLE_REVENU_CI_EST_A_ZERO"));
                hasMontantErreur = true;
            }

            if (dataDecision.getCotisationAvs().signum() == 0) {
                recouvrementPosteContainer.addMessageErreurToPoste(keyRecouvrementPosteContainer,
                        session.getLabel("IRRECOUVRABLE_COTISATION_AVS_EST_A_ZERO"));
                hasMontantErreur = true;
            }

            if (!hasMontantErreur) {
                recouvrementPosteContainer.updateRecouvrementPoste(keyRecouvrementPosteContainer,
                        dataDecision.getRevenuCi(), dataDecision.getCotisationAvs(), dataDecision.getGenre());
                continue;
            }
        }
        return true;
    }

    /**
     * Cette m�thode va traiter les ventilations par ordre de priorit� ou au prorata par ordre de priorit�
     * 
     * @throws Exception
     */
    private void traiterVentilation() throws Exception {

        BigDecimal montantARecouvrirSansNC = montantARecouvrir.subtract(montantNoteCredit);

        montantRestantARecouvrir = montantARecouvrirSansNC;
        for (int ordrePriorite = 1; ordrePriorite <= 11; ordrePriorite++) {
            BigDecimal cumulMontantDisponiblePostesPourOrdrePriorite = recouvrementPosteContainer
                    .calculerCumulMontantTotalPostesPourPriorite("" + ordrePriorite);
            if (cumulMontantDisponiblePostesPourOrdrePriorite.compareTo(new BigDecimal(0)) == 0) {
                continue;
            }

            if (montantRestantARecouvrir.compareTo(cumulMontantDisponiblePostesPourOrdrePriorite) >= 0) {
                recouvrementPosteContainer.ventilerForOrdrePriorite("" + ordrePriorite);
            } else {
                recouvrementPosteContainer.ventilerProrataForPriorite("" + ordrePriorite, montantRestantARecouvrir,
                        cumulMontantDisponiblePostesPourOrdrePriorite);
                break;
            }
            montantRestantARecouvrir = montantRestantARecouvrir.subtract(recouvrementPosteContainer
                    .getMontantAttribue());
            if (montantRestantARecouvrir.signum() <= 0) {
                break;
            }
        }
    }

    /**
     * Cette m�thode permet de traiter les notes de cr�dits en les attribuant aux diff�rents recouvrementPostes
     * 
     * @throws Exception
     */
    private void traiterNoteDeCredit() throws Exception {

        boolean hasReductionCotAndInscriptionCI = false;
        for (int ordrePriorite = 1; ordrePriorite <= 11; ordrePriorite++) {
            BigDecimal cumulMontantDisponiblePostesPourOrdrePriorite = recouvrementPosteContainer
                    .calculerCumulMontantTotalPostesPourPriorite("" + ordrePriorite);
            if (!notesDeCreditDesCotisationsDesSectionsMap.isEmpty()) {
                if (recouvrementPosteContainer.traiterNoteDeCreditForPriorite("" + ordrePriorite,
                        montantRestantARecouvrir, cumulMontantDisponiblePostesPourOrdrePriorite,
                        notesDeCreditDesCotisationsDesSectionsMap)) {
                    hasReductionCotAndInscriptionCI = true;
                }
            }
        }
        if (hasReductionCotAndInscriptionCI) {
            addMessageErreur(session.getLabel("RECOUVREMENT_REDUCTION_COT_VERIFIER_REVENU_CI"));
        }
    }

    /**
     * Ajoute un mesage d'erreur dans le recouvrementVentilateur
     * 
     * @param message
     */
    public void addMessageErreur(String message) {
        messageErreurList.add(message);
    }

    /**
     * @return the montantNoteCredit
     */
    public BigDecimal getMontantNoteCredit() {
        return montantNoteCredit;
    }

    /**
     * @param montantNoteCredit the montantNoteCredit to set
     */
    public void setMontantNoteCredit(BigDecimal montantNoteCredit) {
        this.montantNoteCredit = montantNoteCredit;
    }
}
