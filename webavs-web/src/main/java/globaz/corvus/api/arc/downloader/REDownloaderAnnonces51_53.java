package globaz.corvus.api.arc.downloader;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.annonces.REFicheAugmentation;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.api.helper.IHEAnnoncesCentraleHelper;
import globaz.hermes.application.HEApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author HPE
 * 
 */
public class REDownloaderAnnonces51_53 extends REAbstractDownloader {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    Map<String, ArrayList<Object>> annoncesCrees = new HashMap<String, ArrayList<Object>>();
    private String idLot = "";

    private FWMemoryLog log = new FWMemoryLog();

    Map<String, IHEOutputAnnonce> mapTraites = new HashMap<String, IHEOutputAnnonce>();
    private int nbAnnoncesImportees = 0;

    private int nbAnnoncesTotal = 0;
    private BProcess parentProcess = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Traitement des annonces 51 et 53 (Annonces 9�me et 10�me r�vision pour l'adaptation des rentes
     * 
     * ----------------------------------------------------------------------
     * 
     * Traitement: ===========
     * 
     * - pour toutes les annonces - Importation des annonces - Stockage des annonces dans les tables REANN51 et REANN53
     * 
     */
    public void doTraitement(BITransaction transaction, IHEOutputAnnonce[] annonces) throws REDownloaderException {
        try {

            for (int i = 0; i < annonces.length; i++) {
                IHEOutputAnnonce annonce = annonces[i];

                /*
                 * ATTENTION : ===========
                 * 
                 * Toutes les annnonces sont cens�es arriver dans l'ordre :
                 * 
                 * 51 / 01 51 / 02 51 / 03
                 * 
                 * 51 / 01 51 / 02 51 / 03
                 * 
                 * 53 / 01 53 / 02 53 / 03
                 * 
                 * ==> Il faut les traiter dans cet ordre pour les ins�rer correctemnt !!!
                 */

                String codeApplication = JadeStringUtil.substring(annonce.getChampEnregistrement(), 0, 2);

                if (!mapTraites.containsKey(annonce.getIdAnnonce())) {
                    if (codeApplication.equals("51")) {
                        int nbCourant = lireAnnonce51(transaction, annonces, i);
                        i = nbCourant;
                    } else if (codeApplication.equals("53")) {
                        int nbCourant = lireAnnonce53(transaction, annonces, i);
                        i = nbCourant;
                    }
                }

                nbAnnoncesImportees++;

            }

        } catch (Exception e) {
            throw new REDownloaderException("Exception occurs in : " + this.getClass().getName(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.corvus.api.external.arc.IREUploader#upload(globaz.globall.api. BITransaction,
     * globaz.corvus.api.external.arc.rassemblement.IArcVO[])
     */
    public void download(BTransaction transaction) throws REDownloaderException {

        BISession remoteSession = null;
        BITransaction remoteTransaction = null;

        try {

            remoteSession = PRSession.connectSession(getSession(), HEApplication.DEFAULT_APPLICATION_HERMES);
            IHEAnnoncesCentraleHelper aCHelper = new IHEAnnoncesCentraleHelper();

            if (JadeStringUtil.isBlankOrZero(getIdLot())) {
                throw new Exception(((BSession) remoteSession).getLabel("PROCESS_IMP_51_53_LOT_VIDE"));
            }

            assertBeforeExecute();

            IHEOutputAnnonce[] annonces = null;

            // Une nouvelle transaction HERMES pour chaque lot
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            if (!remoteTransaction.isOpened()) {
                remoteTransaction.openTransaction();
            }

            // Recuperation des annonces HERMES
            aCHelper.setISession(remoteSession);

            // HERMES nous donne les annonces 51 et 53
            annonces = null;

            // Pour des raisons de s�curit� (crash m�moire), vaut mieux
            // peut-�tre ne pas tout charger en m�me temps

            int nbAnnoncesSimultanees = 100000;

            do {
                annonces = aCHelper.getAnnoncesAdaptationRentes((BTransaction) remoteTransaction, getIdLot(),
                        nbAnnoncesSimultanees);

                nbAnnoncesTotal += annonces.length * 3;

                // traitement et insertion des annonces HERMES dans CORVUS
                if (remoteTransaction.hasErrors()) {
                    throw new Exception(remoteSession.getErrors().toString());
                }

                doTraitement(transaction, annonces);

                // validation du lot
                // A la fin des traitements :
                // --> on transmet � HERMES que ces annonces ont �t� trait�es
                // HERMES met une date de traitement pour ces annonces
                if (!remoteTransaction.hasErrors() && !transaction.hasErrors()) {
                    remoteTransaction.commit();
                } else {
                    log.logStringBuffer(transaction.getErrors(), getClass().getName());
                    log.logStringBuffer(remoteTransaction.getErrors(), getClass().getName());
                }

            } while ((annonces != null) && (annonces.length > 0));

        } catch (Exception e) {

            try {
                transaction.rollback();
                remoteTransaction.rollback();
            } catch (Exception e2) {
                // on forward l'Exception source
            }

            throw new REDownloaderException("Exception occurs in : " + this.getClass().getName(), e);
        } finally {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception e) {
                    throw new REDownloaderException("error closing transaction", e);
                }
            }
        }
    }

    public Map<String, ArrayList<Object>> getAnnoncesCrees() {
        return annoncesCrees;
    }

    private String getChampEnregistrement(IHEOutputAnnonce annonce) {
        return JadeStringUtil.fillWithSpaces(annonce.getChampEnregistrement(), 120);
    }

    public String getIdLot() {
        return idLot;
    }

    public FWMemoryLog getLog() {
        return log;
    }

    public int getNbAnnoncesImportees() {
        return nbAnnoncesImportees;
    }

    public int getNbAnnoncesTotal() {
        return nbAnnoncesTotal;
    }

    public BProcess getParentProcess() {
        return parentProcess;
    }

    /**
     * Lecture de l'annonce 51
     * 
     * => Enregistrement 01, 02 et 03
     * 
     * Pour le d�tail des champs, voir le document de la centrale
     * 
     */
    private int lireAnnonce51(BITransaction transaction, IHEOutputAnnonce[] annonces, int i) throws Exception {

        REAnnonce51 ann51 = new REAnnonce51();

        // //////////////////////////////////////////////////////////////////////////
        // RECUPERATION DES VALEUR DE L'ANNONCE RETOURNEE PAR HERMES
        // //////////////////////////////////////////////////////////////////////////

        // Lecture et ajout de l'enregistrement 01
        IHEOutputAnnonce annonce = annonces[i];

        // 1 | Code application: 51
        ann51.setCodeApplication("51");

        // 2 | Code enregistrement: 01
        ann51.setCodeEnregistrement01("01");

        // 3 | Num�ro de la Caisse
        ann51.setNumeroCaisse(JadeStringUtil.substring(getChampEnregistrement(annonce), 4, 3));

        // 4 | Num�ro de l'agence
        ann51.setNumeroAgence(JadeStringUtil.substring(getChampEnregistrement(annonce), 7, 3));

        // 5 | Num�ro de l'annonce
        ann51.setNumeroAnnonce(JadeStringUtil.substring(getChampEnregistrement(annonce), 10, 6));

        // 6 | R�f�rence interne de la Caisse
        ann51.setReferenceCaisseInterne(JadeStringUtil.substring(getChampEnregistrement(annonce), 16, 20));

        // 7 | Num�ro d'assur� de l'ayant droit �
        // | la prestation (11 chiffres)
        String nssAyantDroit = JadeStringUtil.substring(getChampEnregistrement(annonce), 36, 11);
        if (nssAyantDroit.startsWith("-")) {
            nssAyantDroit = JadeStringUtil.change(nssAyantDroit, "-", "756");
        }
        ann51.setNoAssAyantDroit(nssAyantDroit);

        // 8 | Premier num�ro d'assur�
        // | compl�mentaire
        String nssAssureCompl1 = JadeStringUtil.substring(getChampEnregistrement(annonce), 47, 11);
        if (nssAssureCompl1.startsWith("-")) {
            nssAssureCompl1 = JadeStringUtil.change(nssAssureCompl1, "-", "756");
        }
        ann51.setPremierNoAssComplementaire(nssAssureCompl1);

        // 9 | Second num�ro de l'assur�
        // | comlp�mentaire
        String nssAssureCompl2 = JadeStringUtil.substring(getChampEnregistrement(annonce), 58, 11);
        if (nssAssureCompl2.startsWith("-")) {
            nssAssureCompl2 = JadeStringUtil.change(nssAssureCompl2, "-", "756");
        }
        ann51.setSecondNoAssComplementaire(nssAssureCompl2);

        // 10 | R�serve: � blanc

        // 11 | Etat civi
        ann51.setEtatCivil(JadeStringUtil.substring(getChampEnregistrement(annonce), 80, 1));

        // 12 | R�fugi�
        ann51.setIsRefugie(JadeStringUtil.substring(getChampEnregistrement(annonce), 81, 1));

        // 13 | Canton/Etat de domicile
        ann51.setCantonEtatDomicile(JadeStringUtil.substring(getChampEnregistrement(annonce), 82, 3));

        // 14 | Genre de prestations
        ann51.setGenrePrestation(JadeStringUtil.substring(getChampEnregistrement(annonce), 85, 2));

        // 15 | D�but du droit: MMAA
        ann51.setDebutDroit(JadeStringUtil.substring(getChampEnregistrement(annonce), 87, 4));

        // 16 | Mensualit� de la prestation en francs
        ann51.setMensualitePrestationsFrancs(JadeStringUtil.substring(getChampEnregistrement(annonce), 91, 5));

        // 17 | Mensualit� de la rente ordinaire
        // | remplac�e en francs
        ann51.setMontantAncRenteRemplacee(JadeStringUtil.substring(getChampEnregistrement(annonce), 96, 5));

        // 18 | Fin du droit: MMAA
        ann51.setFinDroit(JadeStringUtil.substring(getChampEnregistrement(annonce), 101, 4));

        // 19 | Mois de rapport: MMAA
        ann51.setMoisRapport(JadeStringUtil.substring(getChampEnregistrement(annonce), 105, 4));

        // 20 | Code de mutation
        ann51.setCodeMutation(JadeStringUtil.substring(getChampEnregistrement(annonce), 109, 2));

        // 21 | R�serve: � blanc

        ann51.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann51.add(transaction);
        mapTraites.put(annonce.getIdAnnonce(), annonce);
        i++;

        // Lecture et ajout de l'enregistrement 02
        annonce = annonces[i];

        // contr�le si bien la bonne en cours
        String codeApplication = JadeStringUtil.substring(getChampEnregistrement(annonce), 0, 2);
        if (!codeApplication.equals("51")) {
            throw new Exception("Erreur dans la lecture des annonces, code application incorrect.");
        }

        String codeEnregistrement = JadeStringUtil.substring(getChampEnregistrement(annonce), 2, 2);
        if (!codeEnregistrement.equals("02")) {
            throw new Exception("Erreur dans la lecture des annonces, code enregistrement incorrect.");
        }

        REAnnonce51 ann51_02 = new REAnnonce51();
        // 1 | Code application: 51
        ann51_02.setCodeApplication("51");

        // 2 | Code enregistrement: 02
        ann51_02.setCodeEnregistrement01("02");

        // 3 | Revenu annuel moyen d�terminant en francs
        ann51_02.setRamDeterminant(JadeStringUtil.substring(getChampEnregistrement(annonce), 4, 8));

        // 4 | Dur�e de cotisation pour d�terminer le revenu annuel moyen : AAMM
        ann51_02.setDureeCotPourDetRAM(JadeStringUtil.substring(getChampEnregistrement(annonce), 12, 4));

        // 5 | Ann�e de niveau
        ann51_02.setAnneeNiveau(JadeStringUtil.substring(getChampEnregistrement(annonce), 16, 2));

        // 6 | Revenus pris en compte
        ann51_02.setRevenuPrisEnCompte(JadeStringUtil.substring(getChampEnregistrement(annonce), 18, 1));

        // 7 | Echelle de rente
        ann51_02.setEchelleRente(JadeStringUtil.substring(getChampEnregistrement(annonce), 19, 2));

        // 8 | Dur�e de cotisations prise en compte pour le choix de l'�chelle
        // de rentes avant 1973 : AAMM
        ann51_02.setDureeCoEchelleRenteAv73(JadeStringUtil.substring(getChampEnregistrement(annonce), 22, 4));

        // 9 | Dur�e de cotisations prise en compte pour le choix de l'�chelle
        // de rentes apr�s 1973 : AAMM
        ann51_02.setDureeCoEchelleRenteDes73(JadeStringUtil.substring(getChampEnregistrement(annonce), 26, 4));

        // 10 | Prise en compte des dur�es de cotisations manquantes en mois
        // pour les ann�es 1948-72
        ann51_02.setDureeCotManquante48_72(JadeStringUtil.substring(getChampEnregistrement(annonce), 30, 2));

        // 11 | Ann�es de cotisations de la classe d'�ge
        ann51_02.setAnneeCotClasseAge(JadeStringUtil.substring(getChampEnregistrement(annonce), 32, 2));

        // 12 | Dur�e de l'ajournement : AMM
        ann51_02.setDureeAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 34, 3));

        // 13 | Suppl�ment d'ajournement en francs
        ann51_02.setSupplementAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 37, 5));

        // 14 | Date de r�vocation de l'ajournement : MMAA
        ann51_02.setDateRevocationAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 42, 4));

        // 15 | Limites de revenu
        ann51_02.setIsLimiteRevenu(JadeStringUtil.substring(getChampEnregistrement(annonce), 46, 1));

        // 16 | Minimum garanti
        ann51_02.setIsMinimumGaranti(JadeStringUtil.substring(getChampEnregistrement(annonce), 47, 1));

        // 17 | Office AI comp�tent - ayant droit
        ann51_02.setOfficeAICompetent(JadeStringUtil.substring(getChampEnregistrement(annonce), 48, 2));

        // 18 | Office AI comp�tent - �pouse
        ann51_02.setOfficeAiCompEpouse(JadeStringUtil.substring(getChampEnregistrement(annonce), 50, 2));

        // 19 | Degr� invalidit� ayant droit
        ann51_02.setDegreInvalidite(JadeStringUtil.substring(getChampEnregistrement(annonce), 52, 3));

        // 20 | Degr� invalidit� �pouse
        ann51_02.setDegreInvaliditeEpouse(JadeStringUtil.substring(getChampEnregistrement(annonce), 55, 3));

        // 21 | Code l'infirmit� - ayant droit
        ann51_02.setCodeInfirmite(JadeStringUtil.substring(getChampEnregistrement(annonce), 58, 5));

        // 22 | Code de l'informit� - �pouse
        ann51_02.setCodeInfirmiteEpouse(JadeStringUtil.substring(getChampEnregistrement(annonce), 63, 5));

        // 23 | Survenance de l'�v�nement assur� - ayant droit
        ann51_02.setSurvenanceEvenAssure(JadeStringUtil.substring(getChampEnregistrement(annonce), 68, 4));

        // 24 | Survenance de l'�v�nement assur� - �pouse
        ann51_02.setSurvenanceEvtAssureEpouse(JadeStringUtil.substring(getChampEnregistrement(annonce), 72, 4));

        // 25 | Age au d�but de l'invalidit� - ayant droit
        ann51_02.setAgeDebutInvalidite(JadeStringUtil.substring(getChampEnregistrement(annonce), 76, 1));

        // 26 | Age au d�but de l'invalidit� - �pouse
        ann51_02.setAgeDebutInvaliditeEpouse(JadeStringUtil.substring(getChampEnregistrement(annonce), 77, 1));

        // 27 | Genre du droit � l'API
        ann51_02.setGenreDroitAPI(JadeStringUtil.substring(getChampEnregistrement(annonce), 78, 1));

        // 28 | R�duction
        ann51_02.setReduction(JadeStringUtil.substring(getChampEnregistrement(annonce), 79, 2));

        // 29 | Cas sp�cial code 1
        ann51_02.setCasSpecial1(JadeStringUtil.substring(getChampEnregistrement(annonce), 81, 2));

        // 30 | Cas sp�cial code 2
        ann51_02.setCasSpecial2(JadeStringUtil.substring(getChampEnregistrement(annonce), 83, 2));

        // 31 | Cas sp�cial code 3
        ann51_02.setCasSpecial3(JadeStringUtil.substring(getChampEnregistrement(annonce), 85, 2));

        // 32 | Cas sp�cial code 4
        ann51_02.setCasSpecial4(JadeStringUtil.substring(getChampEnregistrement(annonce), 87, 2));

        // 33 | Cas sp�cial code 5
        ann51_02.setCasSpecial5(JadeStringUtil.substring(getChampEnregistrement(annonce), 89, 2));

        // 34 | Prise en compte des dur�es de cotisations manquantes en mois
        // pour les ann�es 73-78
        ann51_02.setDureeCotManquante73_78(JadeStringUtil.substring(getChampEnregistrement(annonce), 91, 2));

        // 35 | Revenu annuel moyen sans BTE
        ann51_02.setRevenuAnnuelMoyenSansBTE(JadeStringUtil.substring(getChampEnregistrement(annonce), 93, 8));

        // 36 | Bonifications pour t�ches �ducatives moyennes prises en compte
        // en frans
        ann51_02.setBteMoyennePrisEnCompte(JadeStringUtil.substring(getChampEnregistrement(annonce), 101, 6));

        // 37 | Nombre d'ann�es BTE
        ann51_02.setNombreAnneeBTE(JadeStringUtil.substring(getChampEnregistrement(annonce), 107, 2));

        // 38 | R�serve: � blanc

        ann51_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann51_02.add(transaction);
        mapTraites.put(annonce.getIdAnnonce(), annonce);
        i++;

        // mise � jour de l'idLien de l'annonce 01
        ann51.retrieve();
        ann51.setIdLienAnnonce(ann51_02.getIdAnnonce());
        ann51.update(transaction);

        // Lecture et ajout de l'enregistrement 03
        annonce = annonces[i];

        REAnnonce51 ann51_03 = new REAnnonce51();

        // contr�le si bien la bonne en cours
        codeApplication = JadeStringUtil.substring(getChampEnregistrement(annonce), 0, 2);
        if (!codeApplication.equals("51")) {
            throw new Exception("Erreur dans la lecture des annonces, code application incorrect.");
        }

        codeEnregistrement = JadeStringUtil.substring(getChampEnregistrement(annonce), 2, 2);
        if (!codeEnregistrement.equals("03")) {
            throw new Exception("Erreur dans la lecture des annonces, code enregistrement incorrect.");
        }

        // 1 | Code application
        ann51_03.setCodeApplication("51");

        // 2 | Code enregistrement
        ann51_03.setCodeEnregistrement01("03");

        // 3 | Nom de famille - nom de jeune fille, pr�nom
        // Non stock�

        // 4 | Nationalit�
        // Non stock�

        // 5 | Fraction de la rente
        ann51_03.setFractionRente(JadeStringUtil.substring(getChampEnregistrement(annonce), 47, 1));

        // 6 | Ancien revenu annuel d�terminant moyen en francs
        ann51_03.setAncienRAM(JadeStringUtil.substring(getChampEnregistrement(annonce), 49, 8));

        // 7 | Ancienne RO remplac��
        ann51_03.setMontantAncRenteRemplacee(JadeStringUtil.substring(getChampEnregistrement(annonce), 56, 5));

        // 8 | Ancien montant mensuel
        ann51_03.setAncienMontantMensuel(JadeStringUtil.substring(getChampEnregistrement(annonce), 61, 5));

        // 9 | Ancien code cas sp�cial 1
        ann51_03.setAncienCodeCasSpecial1(JadeStringUtil.substring(getChampEnregistrement(annonce), 66, 2));

        // 10 | Ancien code cas sp�cial 2
        ann51_03.setAncienCodeCasSpecial2(JadeStringUtil.substring(getChampEnregistrement(annonce), 68, 2));

        // 11 | Ancien code cas sp�cial 3
        ann51_03.setAncienCodeCasSpecial3(JadeStringUtil.substring(getChampEnregistrement(annonce), 70, 2));

        // 12 | Ancien code cas sp�cial 4
        ann51_03.setAncienCodeCasSpecial4(JadeStringUtil.substring(getChampEnregistrement(annonce), 72, 2));

        // 13 | Ancien code cas sp�cial 5
        ann51_03.setAncienCodeCasSpecial5(JadeStringUtil.substring(getChampEnregistrement(annonce), 74, 2));

        // 14 | Observations de la centrale
        ann51_03.setObservationCentrale(JadeStringUtil.substring(getChampEnregistrement(annonce), 76, 10));

        // 15 | Ancien revenu annuel moyen sans BTE
        ann51_03.setAncienRAMSansBTE(JadeStringUtil.substring(getChampEnregistrement(annonce), 86, 8));

        // 16 | Anciennes BTE prises en comtpe
        ann51_03.setAncienneBTEMoyennePrisCompte(JadeStringUtil.substring(getChampEnregistrement(annonce), 94, 6));

        // 17 | Ancien suppl�ment d'ajournement en francs
        ann51_03.setAncienSupplementAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 100, 5));

        // 18 | R�serve: � blanc

        ann51_03.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann51_03.add(transaction);
        mapTraites.put(annonce.getIdAnnonce(), annonce);

        // mise � jour de l'idLien de l'annonce 01
        ann51_02.retrieve();
        ann51_02.setIdLienAnnonce(ann51_03.getIdAnnonce());
        ann51_02.update(transaction);

        ArrayList<Object> listAnn01_02_03 = new ArrayList<Object>();
        listAnn01_02_03.add(ann51);
        listAnn01_02_03.add(ann51_02);
        listAnn01_02_03.add(ann51_03);

        REFicheAugmentation ficheAugmentation = new REFicheAugmentation();
        ficheAugmentation.setSession((BSession) getSession());
        ficheAugmentation.setIdAnnonceHeader(ann51.getIdAnnonce());

        JADate date = new JADate(REPmtMensuel.getDateProchainPmt((BSession) getSession()));
        ficheAugmentation.setDateAugmentation(date.toStrAMJ());
        ficheAugmentation.add(transaction);

        annoncesCrees.put(ann51.getIdAnnonce(), listAnn01_02_03);

        return i;
    }

    /**
     * Lecture de l'annonce 53
     * 
     * => Enregistrement 01, 02 et 03
     * 
     * Pour le d�tail des champs, voir le document de la centrale
     * 
     */
    private int lireAnnonce53(BITransaction transaction, IHEOutputAnnonce[] annonces, int i) throws Exception {

        REAnnonce53 ann53 = new REAnnonce53();

        // //////////////////////////////////////////////////////////////////////////
        // RECUPERATION DES VALEUR DE L'ANNONCE RETOURNEE PAR HERMES
        // //////////////////////////////////////////////////////////////////////////

        // Lecture et ajout de l'enregistrement 01
        IHEOutputAnnonce annonce = annonces[i];

        // 1 | Code application: 53
        ann53.setCodeApplication("53");

        // 2 | Code enregistrement: 01
        ann53.setCodeEnregistrement01("01");

        // 3 | Num�ro de la Caisse
        ann53.setNumeroCaisse(JadeStringUtil.substring(getChampEnregistrement(annonce), 4, 3));

        // 4 | Num�ro de l'agence
        ann53.setNumeroAgence(JadeStringUtil.substring(getChampEnregistrement(annonce), 7, 3));

        // 5 | Num�ro de l'annonce
        ann53.setNumeroAnnonce(JadeStringUtil.substring(getChampEnregistrement(annonce), 10, 6));

        // 6 | R�f�rence interne de la Caisse
        ann53.setReferenceCaisseInterne(JadeStringUtil.substring(getChampEnregistrement(annonce), 16, 20));

        // 7 | Num�ro d'assur� de l'ayant droit �
        // | la prestation (11 chiffres)
        String nssAyantDroit = JadeStringUtil.substring(getChampEnregistrement(annonce), 36, 11);
        if (nssAyantDroit.startsWith("-")) {
            nssAyantDroit = JadeStringUtil.change(nssAyantDroit, "-", "756");
        }
        ann53.setNoAssAyantDroit(nssAyantDroit);

        // 8 | Premier num�ro d'assur�
        // | compl�mentaire
        String nssAssureCompl1 = JadeStringUtil.substring(getChampEnregistrement(annonce), 47, 11);
        if (nssAssureCompl1.startsWith("-")) {
            nssAssureCompl1 = JadeStringUtil.change(nssAssureCompl1, "-", "756");
        }
        ann53.setPremierNoAssComplementaire(nssAssureCompl1);

        // 9 | Second num�ro de l'assur�
        // | comlp�mentaire
        String nssAssureCompl2 = JadeStringUtil.substring(getChampEnregistrement(annonce), 58, 11);
        if (nssAssureCompl2.startsWith("-")) {
            nssAssureCompl2 = JadeStringUtil.change(nssAssureCompl2, "-", "756");
        }
        ann53.setSecondNoAssComplementaire(nssAssureCompl2);

        // 10 | R�serve: � blanc

        // 11 | Etat civi
        ann53.setEtatCivil(JadeStringUtil.substring(getChampEnregistrement(annonce), 80, 1));

        // 12 | R�fugi�
        ann53.setIsRefugie(JadeStringUtil.substring(getChampEnregistrement(annonce), 81, 1));

        // 13 | Canton/Etat de domicile
        ann53.setCantonEtatDomicile(JadeStringUtil.substring(getChampEnregistrement(annonce), 82, 3));

        // 14 | Genre de prestations
        ann53.setGenrePrestation(JadeStringUtil.substring(getChampEnregistrement(annonce), 85, 2));

        // 15 | D�but du droit: MMAA
        ann53.setDebutDroit(JadeStringUtil.substring(getChampEnregistrement(annonce), 87, 4));

        // 16 | Mensualit� de la prestation en francs
        ann53.setMensualitePrestationsFrancs(JadeStringUtil.substring(getChampEnregistrement(annonce), 91, 5));

        // 17 | Fin du droit: MMAA
        ann53.setFinDroit(JadeStringUtil.substring(getChampEnregistrement(annonce), 96, 4));

        // 18 | Mois de rapport: MMAA
        ann53.setMoisRapport(JadeStringUtil.substring(getChampEnregistrement(annonce), 100, 4));

        // 19 | Code de mutation
        ann53.setCodeMutation(JadeStringUtil.substring(getChampEnregistrement(annonce), 104, 2));

        // 20 | R�serve: � blanc

        ann53.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53.add(transaction);
        mapTraites.put(annonce.getIdAnnonce(), annonce);
        i++;

        // Lecture et ajout de l'enregistrement 02
        annonce = annonces[i];

        // contr�le si bien la bonne en cours
        String codeApplication = JadeStringUtil.substring(getChampEnregistrement(annonce), 0, 2);
        if (!codeApplication.equals("53")) {
            throw new Exception("Erreur dans la lecture des annonces, code application incorrect.");
        }

        String codeEnregistrement = JadeStringUtil.substring(getChampEnregistrement(annonce), 2, 2);
        if (!codeEnregistrement.equals("02")) {
            throw new Exception("Erreur dans la lecture des annonces, code enregistrement incorrect.");
        }

        REAnnonce53 ann53_02 = new REAnnonce53();
        // 1 | Code application: 53
        ann53_02.setCodeApplication("53");

        // 2 | Code enregistrement: 02
        ann53_02.setCodeEnregistrement01("02");

        // 3 | Echelle de rente
        ann53_02.setRamDeterminant(JadeStringUtil.substring(getChampEnregistrement(annonce), 4, 2));

        // 4 | Dur�e de cotisations prise en compte pour le choix de l'�chelle
        // de rentes avant 1973 : AAMM
        ann53_02.setDureeCoEchelleRenteAv73(JadeStringUtil.substring(getChampEnregistrement(annonce), 6, 4));

        // 5 | Dur�e de cotisations prise en compte pour le choix de l'�chelle
        // de rentes apr�s 1973 : AAMM
        ann53_02.setDureeCoEchelleRenteDes73(JadeStringUtil.substring(getChampEnregistrement(annonce), 10, 4));

        // 6 | Prise en compte des dur�es de cotisations manquantes en mois pour
        // les ann�es 1948-72
        ann53_02.setDureeCotManquante48_72(JadeStringUtil.substring(getChampEnregistrement(annonce), 14, 2));

        // 7 | Prise en compte des dur�es de cotisations manquantes en mois pour
        // les ann�es 73-78
        ann53_02.setDureeCotManquante73_78(JadeStringUtil.substring(getChampEnregistrement(annonce), 16, 2));

        // 8 | Ann�es de cotisations de la classe d'�ge
        ann53_02.setAnneeCotClasseAge(JadeStringUtil.substring(getChampEnregistrement(annonce), 18, 2));

        // 9 | Revenu annuel moyen en francs
        ann53_02.setRamDeterminant(JadeStringUtil.substring(getChampEnregistrement(annonce), 20, 8));

        // 10 | Code de revenus splitt�s
        ann53_02.setCodeRevenuSplitte(JadeStringUtil.substring(getChampEnregistrement(annonce), 28, 1));

        // 11 | Dur�e de cotisations pour d�terminer le RAM
        ann53_02.setDureeCotPourDetRAM(JadeStringUtil.substring(getChampEnregistrement(annonce), 29, 4));

        // 12 | Ann�e de niveau
        ann53_02.setAnneeNiveau(JadeStringUtil.substring(getChampEnregistrement(annonce), 33, 2));

        // 13 | Nombre d'ann�es BTE
        ann53_02.setNombreAnneeBTE(JadeStringUtil.substring(getChampEnregistrement(annonce), 35, 4));

        // 14 | Nombre d'ann�es BTA
        ann53_02.setNbreAnneeBTA(JadeStringUtil.substring(getChampEnregistrement(annonce), 39, 4));

        // 15 | Nombre d'ann�es avec des bonifications transitoires
        ann53_02.setNbreAnneeBonifTrans(JadeStringUtil.substring(getChampEnregistrement(annonce), 43, 2));

        // 16 | Office AI comp�tent
        ann53_02.setOfficeAICompetent(JadeStringUtil.substring(getChampEnregistrement(annonce), 45, 3));

        // 17 | Degr� invalidit�
        ann53_02.setDegreInvalidite(JadeStringUtil.substring(getChampEnregistrement(annonce), 48, 3));

        // 18 | Code l'infirmit�
        ann53_02.setCodeInfirmite(JadeStringUtil.substring(getChampEnregistrement(annonce), 51, 5));

        // 19 | Survenance de l'�v�nement assur�
        ann53_02.setSurvenanceEvenAssure(JadeStringUtil.substring(getChampEnregistrement(annonce), 56, 4));

        // 20 | Age au d�but de l'invalidit�
        ann53_02.setAgeDebutInvalidite(JadeStringUtil.substring(getChampEnregistrement(annonce), 60, 1));

        // 21 | Genre du droit � l'API
        ann53_02.setGenreDroitAPI(JadeStringUtil.substring(getChampEnregistrement(annonce), 61, 1));

        // 22 | R�duction
        ann53_02.setReduction(JadeStringUtil.substring(getChampEnregistrement(annonce), 62, 2));

        // 23 | Cas sp�cial code 1
        ann53_02.setCasSpecial1(JadeStringUtil.substring(getChampEnregistrement(annonce), 64, 2));

        // 24 | Cas sp�cial code 2
        ann53_02.setCasSpecial2(JadeStringUtil.substring(getChampEnregistrement(annonce), 66, 2));

        // 25 | Cas sp�cial code 3
        ann53_02.setCasSpecial3(JadeStringUtil.substring(getChampEnregistrement(annonce), 68, 2));

        // 26 | Cas sp�cial code 4
        ann53_02.setCasSpecial4(JadeStringUtil.substring(getChampEnregistrement(annonce), 70, 2));

        // 27 | Cas sp�cial code 5
        ann53_02.setCasSpecial5(JadeStringUtil.substring(getChampEnregistrement(annonce), 72, 2));

        // 28 | Nombre d'ann�es d'anticipation
        ann53_02.setNbreAnneeAnticipation(JadeStringUtil.substring(getChampEnregistrement(annonce), 74, 1));

        // 29 | r�duction anticipation
        ann53_02.setReductionAnticipation(JadeStringUtil.substring(getChampEnregistrement(annonce), 75, 5));

        // 30 | Date d�but anticipation
        ann53_02.setDateDebutAnticipation(JadeStringUtil.substring(getChampEnregistrement(annonce), 80, 4));

        // 31 | Dur�e ajournement
        ann53_02.setDureeAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 84, 3));

        // 32 | Suppl�ment d'ajournement en francs
        ann53_02.setSupplementAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 87, 5));

        // 33 | Date r�vocation ajournement
        ann53_02.setDateRevocationAjournement(JadeStringUtil.substring(getChampEnregistrement(annonce), 92, 4));

        // 34 | code survivant invalide
        ann53_02.setIsSurvivant(JadeStringUtil.substring(getChampEnregistrement(annonce), 96, 1));

        // 35 | R�serve: � blanc

        ann53_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53_02.add(transaction);
        mapTraites.put(annonce.getIdAnnonce(), annonce);
        i++;

        // mise � jour de l'idLien de l'annonce 01
        ann53.retrieve();
        ann53.setIdLienAnnonce(ann53_02.getIdAnnonce());
        ann53.update(transaction);

        // Lecture et ajout de l'enregistrement 03
        annonce = annonces[i];

        REAnnonce53 ann53_03 = new REAnnonce53();

        // contr�le si bien la bonne en cours
        codeApplication = JadeStringUtil.substring(getChampEnregistrement(annonce), 0, 2);
        if (!codeApplication.equals("53")) {
            throw new Exception("Erreur dans la lecture des annonces, code application incorrect.");
        }

        codeEnregistrement = JadeStringUtil.substring(getChampEnregistrement(annonce), 2, 2);
        if (!codeEnregistrement.equals("03")) {
            throw new Exception("Erreur dans la lecture des annonces, code enregistrement incorrect.");
        }

        // 1 | Code application
        ann53_03.setCodeApplication("53");

        // 2 | Code enregistrement
        ann53_03.setCodeEnregistrement01("03");

        // 3 | Nom de famille - nom de jeune fille, pr�nom
        // Non stock�

        // 4 | Nationalit�
        // Non stock�

        // 5 | Fraction de la rente
        ann53_03.setFractionRente(JadeStringUtil.substring(getChampEnregistrement(annonce), 47, 1));

        // 6 | Ancien revenu annuel d�terminant moyen en francs
        ann53_03.setAncienRAM(JadeStringUtil.substring(getChampEnregistrement(annonce), 48, 8));

        // 7 | Ancien suppl�ment d'ajournement
        ann53_03.setAncienSupplementAjourn(JadeStringUtil.substring(getChampEnregistrement(annonce), 56, 5));

        // 8 | Ancienne r�duction
        ann53_03.setAncienRedAnticipation(JadeStringUtil.substring(getChampEnregistrement(annonce), 61, 5));

        // 9 | Ancien montant mensuel
        ann53_03.setAncienMontantMensuel(JadeStringUtil.substring(getChampEnregistrement(annonce), 66, 5));

        // 10 | Ancien code cas sp�cial 1
        ann53_03.setAncienCodeCasSpecial1(JadeStringUtil.substring(getChampEnregistrement(annonce), 71, 2));

        // 11 | Ancien code cas sp�cial 2
        ann53_03.setAncienCodeCasSpecial2(JadeStringUtil.substring(getChampEnregistrement(annonce), 73, 2));

        // 12 | Ancien code cas sp�cial 3
        ann53_03.setAncienCodeCasSpecial3(JadeStringUtil.substring(getChampEnregistrement(annonce), 75, 2));

        // 13 | Ancien code cas sp�cial 4
        ann53_03.setAncienCodeCasSpecial4(JadeStringUtil.substring(getChampEnregistrement(annonce), 77, 2));

        // 14 | Ancien code cas sp�cial 5
        ann53_03.setAncienCodeCasSpecial5(JadeStringUtil.substring(getChampEnregistrement(annonce), 79, 2));

        // 15 | Observations de la centrale
        ann53_03.setObservationCentrale(JadeStringUtil.substring(getChampEnregistrement(annonce), 81, 10));

        // 16 | R�serve: � blanc

        ann53_03.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53_03.add(transaction);
        mapTraites.put(annonce.getIdAnnonce(), annonce);

        // mise � jour de l'idLien de l'annonce 01
        ann53_02.retrieve();
        ann53_02.setIdLienAnnonce(ann53_03.getIdAnnonce());
        ann53_02.update(transaction);

        ArrayList<Object> listAnn01_02_03 = new ArrayList<Object>();
        listAnn01_02_03.add(ann53);
        listAnn01_02_03.add(ann53_02);
        listAnn01_02_03.add(ann53_03);

        REFicheAugmentation ficheAugmentation = new REFicheAugmentation();
        ficheAugmentation.setSession((BSession) getSession());
        ficheAugmentation.setIdAnnonceHeader(ann53.getIdAnnonce());

        JADate date = new JADate(REPmtMensuel.getDateProchainPmt((BSession) getSession()));
        ficheAugmentation.setDateAugmentation(date.toStrAMJ());
        ficheAugmentation.add(transaction);

        annoncesCrees.put(ann53.getIdAnnonce(), listAnn01_02_03);

        return i;
    }

    public void setAnnoncesCrees(Map<String, ArrayList<Object>> annoncesCrees) {
        this.annoncesCrees = annoncesCrees;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setLog(FWMemoryLog log) {
        this.log = log;
    }

    public void setNbAnnoncesImportees(int nbAnnoncesImportees) {
        this.nbAnnoncesImportees = nbAnnoncesImportees;
    }

    public void setNbAnnoncesTotal(int nbAnnoncesTotal) {
        this.nbAnnoncesTotal = nbAnnoncesTotal;
    }

    public void setParentProcess(BProcess parentProcess) {
        this.parentProcess = parentProcess;
    }

}
