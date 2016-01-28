package globaz.corvus.api.arc.downloader;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.process.REGenererListesABProcess;
import globaz.corvus.vb.annonces.REAnnoncesAbstractLevel1AListViewBean;
import globaz.corvus.vb.annonces.REAnnoncesAbstractLevel1AViewBean;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.api.helper.IHEAnnoncesCentraleHelper;
import globaz.hermes.application.HEApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRSession;

public class REDownloaderAnnonces50 extends REAbstractDownloader {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // inalement pas de limite
    // public static final Integer MAX_NB_DEMANDES = new Integer(100);
    private REAnnonces50Container container = null;
    private FWMemoryLog log = new FWMemoryLog();
    private BProcess parentProcess = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Traitement des annonces 50 (confirmation de la Centrale aux caisses)
     * 
     * ------------------------------------------------------------------------ ------
     * 
     * Traitement: ===========
     * 
     * - pour toutes les annonces - Retrouver l'annonce correspondant a la confirmation de la centrale - mise a jour du
     * code de traitement - preparation pour l'impression des Listes A et B
     * 
     * - si aucune annonce n'est retrouvee - preparation pour l'impression des Listes A et B
     * 
     * - impression des Listes A et B
     * 
     */
    public void doTraitement(BITransaction transaction, IHEOutputAnnonce[] annonces) throws REDownloaderException {
        try {

            for (int i = 0; i < annonces.length; i++) {
                IHEOutputAnnonce annonce = annonces[i];

                REAnnonce50VO annonceVO = lireAnnonce50(transaction, annonce);

                // //////////////////////////////////////////////////////////////////////////
                // RECHERCHE ET MISE A JOUR DE L'ANNONCE
                // //////////////////////////////////////////////////////////////////////////

                // Premiere recherche avec:
                // - nss ayant droit
                // - genre de prestation
                // - mois de rapport
                // - date de fin
                REAnnoncesAbstractLevel1AListViewBean annoncesManager = new REAnnoncesAbstractLevel1AListViewBean();
                annoncesManager.setSession((BSession) getSession());
                annoncesManager.setForNssAyantDroit(NSUtil.formatAVSUnknown(annonceVO.getNssAyantDroit()));
                annoncesManager.setForGenrePrestation(annonceVO.getGenrePrestations());
                annoncesManager.setForFinDroit(annonceVO.getFinDroit());
                annoncesManager.find(transaction);

                if (annoncesManager.size() == 0) {
                    // finalement on ne dit rien dans ce cas la
                    // throw new
                    // REDownloaderException("Aucune annonce ne correspond à l'annonce 50: no annonce="
                    // + annonceVO.getNumeroAnnonce() +
                    // " nss ayant droit: " + annonceVO.getNssAyantDroit());
                } else if (annoncesManager.size() > 1) {
                    //
                    // throw new
                    // REDownloaderException("Plusieurs annonces correspondent à l'annonce 50: no annonce="
                    // + annonceVO.getNumeroAnnonce() +
                    // " nss ayant droit: " + annonceVO.getNssAyantDroit());
                } else {

                    REAnnoncesAbstractLevel1AViewBean annonceEnvoyee = (REAnnoncesAbstractLevel1AViewBean) annoncesManager
                            .getFirstEntity();

                    REAnnonceRenteManager arManager = new REAnnonceRenteManager();
                    arManager.setSession((BSession) getSession());
                    arManager.setForIdAnnonceHeader(annonceEnvoyee.getIdAnnonce());
                    arManager.find(transaction);

                    if (arManager.size() == 0) {
                        // finalement on ne dit rien dans ce cas la
                        // throw new
                        // REDownloaderException("Pas d'annonce de rente avec l'id: "
                        // + annonceEnvoyee.getIdAnnonce());
                    } else {
                        REAnnonceRente ar = (REAnnonceRente) arManager.getFirstEntity();

                        // misa a jour du code de traitement
                        if (REAnnonce50VO.CODE_TRAITEMENT_TRAITE.equals(annonceVO.getCodeTraitement())) {
                            ar.setCsTraitement(IREAnnonces.CS_CODE_TRAITE);
                        } else if (REAnnonce50VO.CODE_TRAITEMENT_EN_COURS.equals(annonceVO.getCodeTraitement())) {
                            // pas de retour en arriere dans la logique des
                            // etats de traitement
                            if (!REAnnonce50VO.CODE_TRAITEMENT_TRAITE.equals(ar.getCsTraitement())) {
                                ar.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                            }
                        } else {
                            // pas de retour en arriere dans la logique des
                            // etats de traitement
                            if (!REAnnonce50VO.CODE_TRAITEMENT_TRAITE.equals(ar.getCsTraitement())) {
                                // REAnnonce50VO.CODE_TRAITEMENT_NON_TRAITE
                                ar.setCsTraitement(IREAnnonces.CS_CODE_NON_TRAITE);
                            }
                        }

                        ar.update(transaction);
                    }
                }

                // //////////////////////////////////////////////////////////////////////////
                // PREPARATION DE L'IMPRESSION DES LISTES A ET B
                // //////////////////////////////////////////////////////////////////////////
                container.insertAnnonce(annonceVO);
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

        container = new REAnnonces50Container(getSession());

        try {
            assertBeforeExecute();

            remoteSession = PRSession.connectSession(getSession(), HEApplication.DEFAULT_APPLICATION_HERMES);
            IHEAnnoncesCentraleHelper aCHelper = new IHEAnnoncesCentraleHelper();

            IHEOutputAnnonce[] annonces = null;

            // Une nouvelle transaction HERMES pour chaque lot
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            if (!remoteTransaction.isOpened()) {
                remoteTransaction.openTransaction();
            }

            // Recuperation des annonces HERMES
            aCHelper.setISession(remoteSession);

            // do {
            // HERMES nous donne les annonces 50
            annonces = null;
            annonces = aCHelper.getAnnoncesRentes((BTransaction) remoteTransaction);

            // traitement et insertion des annonces HERMES dans CORVUS
            if (remoteTransaction.hasErrors()) {
                throw new Exception(remoteSession.getErrors().toString());
            }
            doTraitement(transaction, annonces);

            // } while (annonces != null && annonces.length >0);

            // //////////////////////////////////////////////////////////////////////////
            // IMPRESSION DES LISTES A ET B
            // //////////////////////////////////////////////////////////////////////////
            REGenererListesABProcess impressionProcess = new REGenererListesABProcess();
            impressionProcess.setISession(getSession());
            impressionProcess.setTransaction(transaction);
            impressionProcess.setControleTransaction(true);
            impressionProcess.setContainer(container);
            if (parentProcess != null) {
                impressionProcess.setParent(parentProcess);
            }

            impressionProcess.executeProcess();

            // validation du lot
            // A la fin des traitements :
            // --> on transmet à HERMES que ces annonces ont été traitées
            // HERMES met une date de traitement pour ces annonces
            if (!remoteTransaction.hasErrors() && !transaction.hasErrors()) {
                remoteTransaction.commit();
                transaction.commit();
                remoteTransaction.closeTransaction();
            } else {
                log.logStringBuffer(transaction.getErrors(), getClass().getName());
                log.logStringBuffer(remoteTransaction.getErrors(), getClass().getName());
            }

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

    public FWMemoryLog getLog() {
        return log;
    }

    public BProcess getParentProcess() {
        return parentProcess;
    }

    /**
     * Lecture de l'annonce 50
     * 
     * -----+----------+---------------------------------------+------------ champ| position | Contenu | Observation
     * -----+----------+---------------------------------------+------------ 1 | 1- 2 | Code application: 50 | 2 | 3- 4
     * | Code enregistrement: 01 | 3 | 5- 7 | Numéro de la Caisse |1 4 | 8- 10 | Numéro de l'agence |1 5 | 11- 16 |
     * Numéro de l'annonce |1, 3 6 | 17- 36 | Référence interne de la Caisse |3 7 | 37- 47 | Numéro d'assuré de l'ayant
     * droit à | | | la prestation (11 chiffres) | 8 | 48- 58 | Premier numéro d'assuré |2 | | complémentaire | 9 | 59-
     * 69 | Second numéro de l'assuré |2 | | comlpémentaire | 10 | 70- 80 | Nouveau numéro d'assuré de l'ayant | | |
     * droit à la prestation (11 chiffres) | 11 | 81 | Etat civi | 12 | 82 | Réfugié | 13 | 83- 85 | Canton/Etat de
     * domicile |1 14 | 86- 87 | Genre de prestations |1 15 | 88- 91 | Début du droit: MMAA | 16 | 92- 96 | Mensualité
     * de la prestation en francs |1 17 | 97-101 | Mensualité de la rente ordinaire |1, 4 | | remplacée en francs | 15 |
     * 102-105 | Fin du droit: MMAA | 16 | 106-109 | Mois de rapport: MMAA | 20 | 110-111 | Code de mutation | 21 | 112
     * | Code de traitement: | | | 0 = Traité | | | 1 = En cours de traitement; | | | sera réglé par le code 0 ou par |
     * | | une nouvelle annonce de la Caisse | | | 2 = Non traité, voir avis d'erreur; | | | la correction doit être
     * exécutée | | | par la caisse | 22 | 113 | Genre d'annonce: | | | 0 = type d'annonce invalide | | | 1 = prestation
     * portée en augmentation | | | 2 = prestations portée en diminution | | | 3 = cas exclusif de paiement | | |
     * retroactif | | | 4 = annonce de mofification | 23 | 114-120 | Réserve. à blanc |
     * -----+----------+---------------------------------------+------------
     * 
     * Les champs inutilisés sont à blanc. 1 = Cadré à droite, les positions inoccupées sont complétées par des zéros. 2
     * = Cadré à gauche, les positions inoccupées sont à blanc. 3 = Répétition des indications annoncées par la Caisse.
     * 4 = Dans les cas calculés selon le nouveau droit: à blanc.
     * -------------------------------------------------------------------
     * 
     * @param annonce
     * @return id de l'annonce créée
     * @throws Exception
     */
    private REAnnonce50VO lireAnnonce50(BITransaction transaction, IHEOutputAnnonce annonce) throws Exception {

        REAnnonce50VO vo = new REAnnonce50VO();

        // //////////////////////////////////////////////////////////////////////////
        // RECUPERATION DES VALEUR DE L'ANNONCE RETOURNEE PAR HERMES
        // //////////////////////////////////////////////////////////////////////////

        // 1 | Code application: 50
        // 2 | Code enregistrement: 01

        // 3 | Numéro de la Caisse
        vo.setNumeroCaisse(JadeStringUtil.substring(annonce.getChampEnregistrement(), 4, 3));

        // 4 | Numéro de l'agence
        vo.setNumeroAgence(JadeStringUtil.substring(annonce.getChampEnregistrement(), 7, 3));

        // 5 | Numéro de l'annonce
        vo.setNumeroAnnonce(JadeStringUtil.substring(annonce.getChampEnregistrement(), 10, 6));

        // 6 | Référence interne de la Caisse
        vo.setRefInterne(JadeStringUtil.substring(annonce.getChampEnregistrement(), 16, 20));

        // 7 | Numéro d'assuré de l'ayant droit à
        // | la prestation (11 chiffres)
        String nssAyantDroit = JadeStringUtil.substring(annonce.getChampEnregistrement(), 36, 11);
        if (nssAyantDroit.startsWith("-")) {
            nssAyantDroit = JadeStringUtil.change(nssAyantDroit, "-", "756");
        }
        vo.setNssAyantDroit(nssAyantDroit);

        // 8 | Premier numéro d'assuré
        // | complémentaire
        String nssAssureCompl1 = JadeStringUtil.substring(annonce.getChampEnregistrement(), 47, 11);
        if (nssAssureCompl1.startsWith("-")) {
            nssAssureCompl1 = JadeStringUtil.change(nssAssureCompl1, "-", "756");
        }
        vo.setNssAssureCompl1(nssAssureCompl1);

        // 9 | Second numéro de l'assuré
        // | comlpémentaire
        String nssAssureCompl2 = JadeStringUtil.substring(annonce.getChampEnregistrement(), 58, 11);
        if (nssAssureCompl2.startsWith("-")) {
            nssAssureCompl2 = JadeStringUtil.change(nssAssureCompl2, "-", "756");
        }
        vo.setNssAssureCompl2(nssAssureCompl2);

        // 10 | Nouveau numéro d'assuré de l'ayant
        // | droit à la prestation (11 chiffres)
        String nouveauNssAyantDroit = JadeStringUtil.substring(annonce.getChampEnregistrement(), 69, 11);
        if (nouveauNssAyantDroit.startsWith("-")) {
            nouveauNssAyantDroit = JadeStringUtil.change(nouveauNssAyantDroit, "-", "756");
        }
        vo.setNouveauNssAyantDroit(nouveauNssAyantDroit);

        // 11 | Etat civi
        vo.setEtatCivil(JadeStringUtil.substring(annonce.getChampEnregistrement(), 80, 1));

        // 12 | Réfugié
        vo.setRefugie(JadeStringUtil.substring(annonce.getChampEnregistrement(), 81, 1));

        // 13 | Canton/Etat de domicile
        vo.setCantonEtatDomicil(JadeStringUtil.substring(annonce.getChampEnregistrement(), 82, 3));

        // 14 | Genre de prestations
        vo.setGenrePrestations(JadeStringUtil.substring(annonce.getChampEnregistrement(), 85, 2));

        // 15 | Début du droit: MMAA
        vo.setDebutDroit(JadeStringUtil.substring(annonce.getChampEnregistrement(), 87, 4));

        // 16 | Mensualité de la prestation en francs
        vo.setMensualitePrestation(JadeStringUtil.substring(annonce.getChampEnregistrement(), 91, 5));

        // 17 | Mensualité de la rente ordinaire
        // | remplacée en francs
        vo.setMensualiteRORemplacee(JadeStringUtil.substring(annonce.getChampEnregistrement(), 96, 5));

        // 15 | Fin du droit: MMAA
        vo.setFinDroit(JadeStringUtil.substring(annonce.getChampEnregistrement(), 101, 4));

        // 16 | Mois de rapport: MMAA
        vo.setMoisRapport(JadeStringUtil.substring(annonce.getChampEnregistrement(), 105, 4));

        // 20 | Code de mutation
        vo.setCodeMutation(JadeStringUtil.substring(annonce.getChampEnregistrement(), 109, 2));

        // 21 | Code de traitement:
        vo.setCodeTraitement(JadeStringUtil.substring(annonce.getChampEnregistrement(), 111, 1));

        // 22 | Genre d'annonce:
        vo.setGenreAnnonce(JadeStringUtil.substring(annonce.getChampEnregistrement(), 112, 1));

        return vo;
    }

    public void setLog(FWMemoryLog log) {
        this.log = log;
    }

    public void setParentProcess(BProcess parentProcess) {
        this.parentProcess = parentProcess;
    }

}
