package globaz.corvus.api.arc.downloader;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.db.annonces.REAnnonceInscriptionCIManager;
import globaz.corvus.db.ci.REInscriptionCI;
import globaz.corvus.db.ci.REInscriptionCIManager;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.api.helper.IHEAnnoncesCentraleHelper;
import globaz.hermes.application.HEApplication;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ame
 * 
 */
public class REDownloaderInscriptionsCIV1 extends REAbstractDownloader {

    // Le nombre doit être assez grand pour prendre en compte toutes les demandes
    // Des incidents étaient créés lorsque ce nombre était à 100 donc le meilleur
    // moyen de résoudre ce problème sans trop d'impact est de mettre un chiffre
    // que le nombre de demandes n'atteindra pas (déjà rare d'avoir plus de 100)
    public static final Integer MAX_NB_DEMANDES = 1000;

    public static PRTiersWrapper getTiersFromNss(BSession session, BITransaction transaction, String nss)
            throws Exception {
        PRTiersWrapper tiersWrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(nss));

        if (tiersWrapper == null) {
            // via l'historique des no AVS
            TIHistoriqueAvsManager avsHistoricManager = new TIHistoriqueAvsManager();
            avsHistoricManager.setSession(session);
            avsHistoricManager.setForNumAvs(NSUtil.formatAVSUnknown(nss));
            avsHistoricManager.find(transaction);

            if (avsHistoricManager.getFirstEntity() != null) {
                return PRTiersHelper.getTiersParId(session,
                        ((TIHistoriqueAvs) avsHistoricManager.getFirstEntity()).getIdTiers());
            }
        }

        return tiersWrapper;
    }

    private final BTransaction _transaction;

    private final FWMemoryLog log = new FWMemoryLog();

    public REDownloaderInscriptionsCIV1(BTransaction transaction, BSession session) {
        super();
        _transaction = transaction;
        setSession(session);
    }

    /**
     * Analyse les CI additionnels reçu en fonction de chaque tiers
     * 
     * @param transaction
     * @throws Exception
     */
    private Map<String, List<String>> analyseDesCIAdditionnelsRecu(
            Map<String, List<REInscriptionCI>> inscriptionCIAjoutees) throws Exception {

        Map<String, List<String>> idsRassemblementCIAddTraites = new HashMap<String, List<String>>();

        for (String idTiers : inscriptionCIAjoutees.keySet()) {
            analyseDesCIAdditionnelsRecuPourUnTiers(idTiers, inscriptionCIAjoutees.get(idTiers),
                    idsRassemblementCIAddTraites);
        }

        return idsRassemblementCIAddTraites;
    }

    /**
     * Analyse les CI additionnels reçu en fonction de l'id tiers
     * 
     * @param transaction
     * @param annoncesManager
     * @param idTiers
     * @throws Exception
     */
    private void analyseDesCIAdditionnelsRecuPourUnTiers(String idTiers,
            List<REInscriptionCI> listeCIAdditionnelPourLeTiers, Map<String, List<String>> idsRassemblementCIAddTraites)
            throws Exception {

        /* On vas récupérer les annonces de CI en attente de CI additionnels pour ce tiers */
        REAnnonceInscriptionCIManager annoncesManager = new REAnnonceInscriptionCIManager();
        annoncesManager.setSession(getBSession());
        annoncesManager.setOnlyCIEnAttenteDeCIAdd(true);
        annoncesManager.setForIdTiers(idTiers);
        annoncesManager.find(_transaction, BManager.SIZE_NOLIMIT);

        /* Pour chaque CI en attente de CI add on vas voir si les CI additionnels qu'on à reçu correspondent */
        for (Object o : annoncesManager) {
            REAnnonceInscriptionCI annonceInscriptionCIEnAttenteCIAdd = (REAnnonceInscriptionCI) o;

            for (REInscriptionCI inscriptionCIAdditionnel : listeCIAdditionnelPourLeTiers) {
                traiterCIAdditionnelPourLeTiers(annonceInscriptionCIEnAttenteCIAdd, inscriptionCIAdditionnel,
                        idsRassemblementCIAddTraites);
            }
        }
    }

    /**
     * <p>
     * <h2>Recuperation des annonces HERMES</h2>
     * </p>
     * <p>
     * Structure des envois de Hermes: <br/>
     * <br/>
     * -- Code app. 11 <-- la ref. ARC est ici <br/>
     * ------ Code app. 38<br/>
     * ------ Code app. 38<br/>
     * ------ Code app. 38<br/>
     * -- Code app. 39<br/>
     * ------ Code app. 38<br/>
     * ------ Code app. 38<br/>
     * -- Code app. 39<br/>
     * ------ Code app. 38<br/>
     * ------ Code app. 38<br/>
     * -- Code app. 39 <-- CI-ADD<br/>
     * -- Code app. 11 <-- la ref. ARC est ici<br/>
     * ------ Code app. 38<br/>
     * -- Code app. 39
     * </p>
     * <p>
     * Pour simplifier le traitement, la structure est parcourue de bas en haut.
     * </p>
     * <p>
     * Traitement:
     * <ul>
     * <li>CI
     * <ul>
     * <li>si un RCI correspondant existe (on trouve un RCI avec la ref. ARC)
     * <ul>
     * <li>mise a jour du RCI</li>
     * <li>ajout inscription</li>
     * <li>ajout annonce</li>
     * </ul>
     * </li>
     * <li>si inexistant (normalement HERMES doit filtrer ces cas) et si on trouve une correspondance dans les tiers au
     * NSS
     * <ul>
     * <li>de toute façon, creation du RCI car on a une annonce 11</li>
     * <li>creation d'un RCI (avec motif et date de clôture de l'annonce 39)</li>
     * <li>creation d'un CI si nécessaire</li>
     * <li>ajout inscription</li>
     * <li>ajout annonce</li>
     * </ul>
     * </li>
     * <li>si on ne trouve rien dans les tiers pour ce NSS
     * <ul>
     * <li>on ne fait rein</li>
     * </ul>
     * </li>
     * </ul>
     * <li>CI-ADD
     * <ul>
     * <li>si on trouve une correspondance dans les tiers au NSS et si un RCI correspondant existe (mêmes motif et date
     * de clôture)
     * <ul>
     * <li>Creation d'un RCI enfant (un nouveau pour chaque CI-ADD ( pour chaque annonce 39))</li>
     * <li>ajout inscription</li>
     * <li>ajout annonce</li>
     * </ul>
     * </li>
     * <li>si il n'y a pas de RCI
     * <ul>
     * <li>creation d'un RCI (avec motif et date de clôture de l'annonce 39)</li>
     * <li>création d'un CI si nécessaire</li>
     * <li>creation d'un RCI enfant</li>
     * <li>ajout inscription</li>
     * <li>ajout annonce</li>
     * </ul>
     * </li>
     * <li>si on ne trouve rien dans les tiers pour ce NSS
     * <ul>
     * <li>on ne fait rien (ces cas seront récupérés avec un autre process)</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     * <p>
     * <h2>Récapitulatif:</h2> Tout d'abord, on trie toutes les annonces pour les regrouper<br/>
     * Ensuite, on traite cas par cas !
     * </p>
     * <br/>
     * <p>
     * <b> Enregistrement de donnee</b><br/>
     * Code application 11 - Enregistrement 01<br/>
     * <table>
     * <thead>
     * <tr>
     * <th>Champ</th>
     * <th>Position</th>
     * <th>Contenu</th>
     * <th>Observation</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td>1</td>
     * <td>1 - 2</td>
     * <td>Code application: 11</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>3- 4</td>
     * <td>Code enregistrement: 01</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>5- 7</td>
     * <td>Numéro de la Caisse</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>4</td>
     * <td>8- 10</td>
     * <td>Numéro de l'Agence</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>5</td>
     * <td>11- 30</td>
     * <td>Référence interne de la Caisse</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>6</td>
     * <td>31- 36</td>
     * <td>Numéro de l'annonce</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>7</td>
     * <td>37- 47</td>
     * <td>Numéro d'assuré</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>8</td>
     * <td>48- 87</td>
     * <td>Etat nominatif</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>9</td>
     * <td>88</td>
     * <td>Sexe</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>10</td>
     * <td>89- 96</td>
     * <td>Date de naissance = JJMMAAAA</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>11</td>
     * <td>97- 99</td>
     * <td>Etat d'origine</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>12</td>
     * <td>100-101</td>
     * <td>Motif de l'annonce</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>13</td>
     * <td>102-112</td>
     * <td>Numéro d'assuré 1 utilise jusque'ici</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>14</td>
     * <td>113-120</td>
     * <td>Réserve: a blanc</td>
     * <td></td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * <br/>
     * <p>
     * <b> Enregistrement de donnee</b><br/>
     * Code application 11 - Enregistrement 04<br/>
     * </p>
     * <p>
     * <table>
     * <thead>
     * <tr>
     * <th>Champ</th>
     * <th>Position</th>
     * <th>Contenu</th>
     * <th>Observation</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <tr>
     * <td>1</td>
     * <td>1- 2</td>
     * <td>Code application: 11</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>3- 4</td>
     * <td>Code enregistrement: 04</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td colspan="3">...</td>
     * </tr>
     * <tr>
     * <td>6</td>
     * <td>57- 60</td>
     * <td>Date de clôture MMAA</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td colspan="3">...</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * <br/>
     * <p>
     * <b>Enregistrement de donnee</b><br/>
     * Code application 25 - Enregistrement 01<br/>
     * </p>
     * <p>
     * <table>
     * <thead>
     * <tr>
     * <th>Champ</th>
     * <th>Position</th>
     * <th>Contenu</th>
     * <th>Observation</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td>1</td>
     * <td>1- 2</td>
     * <td>Code application: 25</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>3- 4</td>
     * <td>Code enregistrement: 01</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>5- 7</td>
     * <td>Numéro de la Caisse commettante</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>4</td>
     * <td>8- 10</td>
     * <td>Numéro de l'Agence commettante</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>5</td>
     * <td>11- 21</td>
     * <td>Numéro d'assuré</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>6</td>
     * <td>22- 32</td>
     * <td>Numéro d'assuré avant le 1.7.1972</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>7</td>
     * <td>33- 72</td>
     * <td>Etat nominatif</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>8</td>
     * <td>73- 92</td>
     * <td>Référence interne de la Caisse</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>9</td>
     * <td>93-103</td>
     * <td>Numéro de l'assuré ayant droit ou du partenaire en cas d'ordre de splitting</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>10</td>
     * <td>104-105</td>
     * <td>Motif de l'annonce</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>11</td>
     * <td>106-109</td>
     * <td>Date de clôture: MMAA</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>12</td>
     * <td>110</td>
     * <td>Réserve: a blanc</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>13</td>
     * <td>111-116</td>
     * <td>Date de l'ordre: JJMMAA</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>14</td>
     * <td>117-120</td>
     * <td>Réserve: a blanc</td>
     * <td></td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * <br/>
     * <p>
     * <b>Enregistrement de donnee</b><br/>
     * Code application 25 - Enregistrement 02-99<br/>
     * </p>
     * <p>
     * <table>
     * <thead>
     * <tr>
     * <th>Champ</th>
     * <th>Position</th>
     * <th>Contenu</th>
     * <th>Observation</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td>1</td>
     * <td>1- 2</td>
     * <td>Code application: 25</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>3- 4</td>
     * <td>Code enregistrement: 02-99</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>5- 15</td>
     * <td>Numéro d'assuré</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>4</td>
     * <td>16- 17</td>
     * <td>Code traitement du registre central des assurés</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>5</td>
     * <td>18- 21</td>
     * <td>Date: MMAA ou des zéro si enregistré avant le 1.7.1972</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>6</td>
     * <td>22- 24</td>
     * <td>Numéro de la caisse</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>7</td>
     * <td>25- 27</td>
     * <td>Numéro de l'agence</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td colspan="3">... répétition des champs 4 à 7</td>
     * </tr>
     * <tr>
     * <td>36</td>
     * <td>112-120</td>
     * <td>Réserve: a blanc</td>
     * <td></td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * <br/>
     */
    private Map<String, List<REInscriptionCI>> doTraitement(IHEOutputAnnonce[] annonces) throws REDownloaderException {
        try {

            // Extraction et tri des groups d'annonces Hermes
            RETraitementExportAnnonces annoncesExport = new RETraitementExportAnnonces(_transaction, getBSession(), log);

            List<REArrayListPourAnnonce> annonceArrayList = annoncesExport.execute(annonces);

            RETraitementImportAnnoncesV1 annoncesImport = new RETraitementImportAnnoncesV1(_transaction, getBSession(),
                    log);

            return annoncesImport.execute(annonceArrayList);

        } catch (Exception e) {
            throw new REDownloaderException(
                    "Exception occurs in : " + this.getClass().getName() + " : " + e.toString(), e);
        }
    }

    public void download() throws REDownloaderException {

        BISession remoteSession = null;
        BITransaction remoteTransaction = null;
        IHEOutputAnnonce[] annonces = null;
        boolean abort = false;

        // on effectue le traitement par lot de MAX_NB_DEMANDES
        // le traitement se fait au moins une fois, jusqu'a ce que l'on reçoive
        // un tableau vide
        do {

            try {
                assertBeforeExecute();

                this.logMessage(getLabel("INFO_DEMANDE_LOT"));

                remoteSession = PRSession.connectSession(getSession(), HEApplication.DEFAULT_APPLICATION_HERMES);
                IHEAnnoncesCentraleHelper aCHelper = new IHEAnnoncesCentraleHelper();

                // Une nouvelle transaction HERMES pour chaque lot
                remoteTransaction = ((BSession) remoteSession).newTransaction();
                if (!remoteTransaction.isOpened()) {
                    remoteTransaction.openTransaction();
                }

                // Récupération des annonces HERMES
                aCHelper.setISession(remoteSession);
                annonces = null;
                annonces = aCHelper.getAnnoncesTerminees((BTransaction) remoteTransaction,
                        REDownloaderInscriptionsCIV1.MAX_NB_DEMANDES);

                // traitement et insertion des annonces HERMES dans CORVUS
                if (!remoteTransaction.hasErrors()) {

                    if ((annonces != null) && (annonces.length > 0)) {
                        this.logMessage(FWMessageFormat.format(getLabel("INFO_LOT_CONTIENT_N_ENREGISTREMENTS"),
                                Integer.toString(annonces.length)));

                        Map<String, List<REInscriptionCI>> inscriptionCIAjoutees = doTraitement(annonces);

                        this.logMessage(getLabel("INFO_FIN_TRAITEMENT_LOT"));

                        // Analyse des CI additionnels reçu afin de trouver des correspondance avec les CI en attente de
                        // CI additionnels
                        Map<String, List<String>> idsRassemblementCIAddTraites = analyseDesCIAdditionnelsRecu(inscriptionCIAjoutees);

                        // Si tous les CI additionnels d'un rassemblement ont été traité on ajoute une date de
                        // traitement
                        traitementDesRassemblementsCIAdditionnels(idsRassemblementCIAddTraites);

                    } else {
                        this.logMessage(getLabel("INFO_LOT_VIDE"));
                    }
                }

                // validation du lot
                // A la fin des traitements :
                // --> on transmet à HERMES que ces annonces ont été traitées
                // HERMES met une date de traitement pour ces annonces
                if (!remoteTransaction.hasErrors() && !_transaction.hasErrors()) {
                    remoteTransaction.commit();
                    _transaction.commit();

                    // Monitoring du BZ9102 - AME
                    RETiersDuplicationDansReciReporter.report(getBSession(), _transaction, log);

                } else {
                    abort = true;
                    remoteTransaction.rollback();
                    _transaction.rollback();
                    log.logStringBuffer(_transaction.getErrors(), this.getClass().getName());
                    log.logStringBuffer(remoteTransaction.getErrors(), this.getClass().getName());
                }

            } catch (Exception e) {
                abort = true;
                System.out.println(e.toString());
                logErreur(e.toString());
                try {
                    _transaction.rollback();
                    if (remoteTransaction != null) {
                        remoteTransaction.rollback();
                    }
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

        } while ((annonces != null) && (annonces.length > 0) && !abort);
    }

    private BSession getBSession() {
        return (BSession) getSession();
    }

    private String getLabel(String id) {
        return getBSession().getLabel(id);
    }

    public FWMemoryLog getLog() {
        return log;
    }

    /**
     * Compare 2 annonces d'inscriptions CI pour savoir si elle correspondent. Dans le sens ou l'une serait l'annonce
     * d'un CI en attente de CI additionnel et la seconde l'annonce d'un CI additionnel
     * 
     * @return true si les différentes valeurs ne sont pas vides et correspondent
     * @throws IllegalArgumentException
     *             Dans le cas ou l'une des annonces serait null
     */
    private boolean isAnnonceInscriptionCIIdentique(REAnnonceInscriptionCI annonceInscriptionCI,
            REAnnonceInscriptionCI annonceInscriptionCI2) throws IllegalArgumentException {
        if ((annonceInscriptionCI == null)) {
            String message = "La première annonce d'inscription CI (REAnnonceInscriptionCI) est null, impossibble d'effectuer une comparaison";
            throw new IllegalArgumentException(message);
        }
        if ((annonceInscriptionCI2 == null)) {
            String message = "La deuxième annonce d'inscription CI (REAnnonceInscriptionCI) est null, impossibble d'effectuer une comparaison";
            throw new IllegalArgumentException(message);
        }
        /* 1 - NSS : on ne test pas car il n'existe pas dans les annonces d'inscription CI */

        /* 2 - noCaisse */
        if (JadeStringUtil.isBlank(annonceInscriptionCI.getNumeroCaisse())
                || JadeStringUtil.isBlank(annonceInscriptionCI2.getNumeroCaisse())
                || !annonceInscriptionCI.getNumeroCaisse().equals(annonceInscriptionCI2.getNumeroCaisse())) {
            return false;
        }

        /* 3 - noAgence */
        if (JadeStringUtil.isBlank(annonceInscriptionCI.getNumeroAgence())
                || JadeStringUtil.isBlank(annonceInscriptionCI2.getNumeroAgence())
                || !annonceInscriptionCI.getNumeroAgence().equals(annonceInscriptionCI2.getNumeroAgence())) {
            return false;
        }

        /* 4 - moisDebut */
        if (JadeStringUtil.isBlankOrZero(annonceInscriptionCI.getMoisDebutCotisations())
                || JadeStringUtil.isBlankOrZero(annonceInscriptionCI2.getMoisDebutCotisations())
                || !annonceInscriptionCI.getMoisDebutCotisations().equals(
                        annonceInscriptionCI2.getMoisDebutCotisations())) {
            return false;
        }

        /* 5 - moisFin */
        if (JadeStringUtil.isBlankOrZero(annonceInscriptionCI.getMoisFinCotisations())
                || JadeStringUtil.isBlankOrZero(annonceInscriptionCI2.getMoisFinCotisations())
                || !annonceInscriptionCI.getMoisFinCotisations().equals(annonceInscriptionCI2.getMoisFinCotisations())) {
            return false;
        }

        /* 6 - année */
        if (JadeStringUtil.isBlankOrZero(annonceInscriptionCI.getAnneeCotisations())
                || JadeStringUtil.isBlankOrZero(annonceInscriptionCI2.getAnneeCotisations())
                || !annonceInscriptionCI.getAnneeCotisations().equals(annonceInscriptionCI2.getAnneeCotisations())) {
            return false;
        }

        /* 7 - codeExtourne */
        if (JadeStringUtil.isEmpty(annonceInscriptionCI.getCodeExtourne())) {
            annonceInscriptionCI.setCodeExtourne("0");
        }
        if (JadeStringUtil.isEmpty(annonceInscriptionCI2.getCodeExtourne())) {
            annonceInscriptionCI2.setCodeExtourne("0");
        }
        if (!annonceInscriptionCI.getCodeExtourne().equals(annonceInscriptionCI2.getCodeExtourne())) {
            return false;
        }

        /* 8 - genreCotisation */
        if (JadeStringUtil.isBlankOrZero(annonceInscriptionCI.getGenreCotisation())
                || JadeStringUtil.isBlankOrZero(annonceInscriptionCI2.getGenreCotisation())
                || !annonceInscriptionCI.getGenreCotisation().equals(annonceInscriptionCI2.getGenreCotisation())) {
            return false;
        }

        /* 9 - codeSplitting = CODE_PARTICULIER. Correspond au champ : CODE_SPLITTING dans ACOR */
        if (JadeStringUtil.isEmpty(annonceInscriptionCI.getCodeParticulier())) {
            annonceInscriptionCI.setCodeParticulier("0");
        }
        if (JadeStringUtil.isEmpty(annonceInscriptionCI2.getCodeParticulier())) {
            annonceInscriptionCI2.setCodeParticulier("0");
        }

        if (!annonceInscriptionCI.getCodeParticulier().equals(annonceInscriptionCI2.getCodeParticulier())) {
            return false;
        }

        /* 10 - revenu */
        if (JadeStringUtil.isEmpty(annonceInscriptionCI.getRevenu())) {
            annonceInscriptionCI.setRevenu("0");
        }
        if (JadeStringUtil.isEmpty(annonceInscriptionCI2.getRevenu())) {
            annonceInscriptionCI2.setRevenu("0");
        }

        if (!annonceInscriptionCI.getRevenu().equals(annonceInscriptionCI2.getRevenu())) {
            return false;
        }

        return true;
    }

    private void logErreur(String text) {
        this.logMessage(text, FWMessage.ERREUR);
    }

    private void logMessage(String text) {
        this.logMessage(text, FWMessage.INFORMATION);
    }

    private void logMessage(String text, String level) {
        log.logMessage(text, level, this.getClass().getName());
    }

    /**
     * Ajoute une date de traitement au rassemblement de CI additionnels si toutes les inscriptions de ce rassemblement
     * ont étés traitées
     * 
     * @param transaction
     * @throws Exception
     */
    private void traitementDesRassemblementsCIAdditionnels(Map<String, List<String>> idsRassemblementCIAddTraites) {
        /* Pour chaque rassemblement de CI Add traité dans ce processus */
        for (String idRCI : idsRassemblementCIAddTraites.keySet()) {
            try {
                /* On recherche toute les inscriptions de CI additionnel qui font partie de ce rassemblement */
                REInscriptionCIManager manager = new REInscriptionCIManager();
                manager.setSession(getBSession());
                manager.setForIdRCI(idRCI);
                manager.find(_transaction, BManager.SIZE_NOLIMIT);

                /* On récupère la liste des inscription CIAdd qui ont été traité pour ce rassemblement */
                List<String> idsInscriptionCIAddTraites = idsRassemblementCIAddTraites.get(idRCI);
                boolean hasInscriptionCIAddNonTraite = false;

                /* On regarde si toutes les inscription CIAdd de ce rassemblement ont été traités */
                for (Object o : manager) {
                    REInscriptionCI inscriptionCI = (REInscriptionCI) o;
                    if (!idsInscriptionCIAddTraites.contains(inscriptionCI.getIdInscription())) {
                        hasInscriptionCIAddNonTraite = true;
                    }
                }

                /* S'il n'y a plus d'inscrition de CIAdd non traité, on ajoute une date de traitement au rassemblement */
                if (!hasInscriptionCIAddNonTraite) {
                    RERassemblementCI rassemblementCIAdditionnels = new RERassemblementCI();
                    rassemblementCIAdditionnels.setIdRCI(idRCI);
                    rassemblementCIAdditionnels.setSession(getBSession());
                    rassemblementCIAdditionnels.retrieve(_transaction);
                    if (rassemblementCIAdditionnels.isNew()) {
                        throw new Exception("Impossible de récupérer le rassemblement de CI additionnel avec l'id ["
                                + idRCI + "] pour lui ajouter une date de clôture");
                    }
                    // TODO s'assurer que le format de la date est correct AAAAMMJJ
                    String dateTraitement = JadeDateUtil.getGlobazFormattedDate(new Date());
                    rassemblementCIAdditionnels.setDateTraitement(dateTraitement);
                    rassemblementCIAdditionnels.update(_transaction);
                }
            } catch (Exception exception) {
                StringBuilder message = new StringBuilder();
                message.append("Une erreur s'est produite lors de l'analyse d'un rassemblement de CI additionnel. ");
                message.append("Message d'erreur parent : ");
                message.append(exception.getMessage());
                JadeThread.logError(this.getClass().getName(), message.toString());
            }
        }
    }

    /**
     * Compare une annonce d'inscription CI en attente de CI additionnel avec un CI additionnel reçu pour le tiers
     * 
     * @param transaction
     * @param annonceInscriptionCIEnAttenteCIAdd
     * @param inscriptionCIAdditionnel
     * @throws JadeNoBusinessLogSessionError
     */
    private void traiterCIAdditionnelPourLeTiers(REAnnonceInscriptionCI annonceInscriptionCIEnAttenteCIAdd,
            REInscriptionCI inscriptionCIAdditionnel, Map<String, List<String>> idsRassemblementCIAddTraites) {
        REAnnonceInscriptionCI annonceInscriptionCIAdditionnel = null;
        try {
            annonceInscriptionCIAdditionnel = new REAnnonceInscriptionCI();
            annonceInscriptionCIAdditionnel.setSession(getBSession());
            annonceInscriptionCIAdditionnel.setIdAnnonce(inscriptionCIAdditionnel.getIdArc());
            annonceInscriptionCIAdditionnel.retrieve(_transaction);

            if (annonceInscriptionCIAdditionnel.isNew()) {
                throw new Exception("Impossible de récupérer l'entité REAnnonceInscriptionCI avec l'id ["
                        + inscriptionCIAdditionnel.getIdArc() + "]"); // TODO
            }

            // CI add match le CI en attente de CI Add ?
            if (isAnnonceInscriptionCIIdentique(annonceInscriptionCIEnAttenteCIAdd, annonceInscriptionCIAdditionnel)) {
                annonceInscriptionCIEnAttenteCIAdd
                        .setAttenteCIAdditionnelCS(REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_TRAITE);
                annonceInscriptionCIEnAttenteCIAdd.update(_transaction);

                // Le CI additionnel a été traité, on ajoute son id dans la map
                if (!idsRassemblementCIAddTraites.containsKey(inscriptionCIAdditionnel.getIdRCI())) {
                    idsRassemblementCIAddTraites.put(inscriptionCIAdditionnel.getIdRCI(), new ArrayList<String>());
                }

                idsRassemblementCIAddTraites.get(inscriptionCIAdditionnel.getIdRCI()).add(
                        inscriptionCIAdditionnel.getIdInscription());
            }
        }

        // On catch l'exception à ce niveau afin de continuer le traitement des autres CI additionnels
        catch (Exception exception) {
            StringBuilder message = new StringBuilder();
            message.append("Une exception s'est produite lors de l'analyse de l'annonce REANICI avec l'id : [");
            message.append(annonceInscriptionCIEnAttenteCIAdd.getIdAnnonce());
            message.append("] en attente de CI additionnel et l'annonce REANICI avec l'id : [");
            message.append(annonceInscriptionCIAdditionnel.getIdAnnonce());
            message.append("] qui est un CI additionnel. Message d'erreur parent : ");
            message.append(exception.getMessage());
            JadeThread.logError(this.getClass().getName(), message.toString());
        }
    }
}
