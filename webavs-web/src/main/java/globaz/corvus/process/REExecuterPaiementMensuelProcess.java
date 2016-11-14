/*
 * Créé le 28 août 07
 */
package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.rentesaccordees.REPaiementRentes;
import globaz.corvus.db.rentesaccordees.REPaiementRentesManager;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.itext.REListeRecapitulativePaiement;
import globaz.corvus.itext.REListeRecapitulativePaiementPC_RFM;
import globaz.corvus.itext.REListeRetenuesBlocages;
import globaz.corvus.itext.RERecapitulationPaiementAdapter;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.pmt.mensuel.RECumulPrstParRubrique;
import globaz.corvus.utils.pmt.mensuel.REGroupOperationCAUtil;
import globaz.corvus.utils.pmt.mensuel.REGroupOperationMotifUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.prestation.utils.PRStringFormatter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.prestation.business.constantes.CodeRecapitulationPcRfm;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;
import ch.globaz.prestation.business.models.recap.SimpleRecapitulationPcRfm;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;
import ch.globaz.prestation.business.services.models.recap.RecapitulationPcRfmService;

/**
 * <p>
 * Process effectuant les grand paiement des rentes.
 * </p>
 * <dl>
 * <dt>Pré requis</dt>
 * <dd>Impression de la liste de vérification. Cette liste va crééer le lot dans l'état ouvert pour le mois du paiement.
 * </dd>
 * <dt>Etape 1</dt>
 * <dd>Contrôle que le lot pour le mois de paiement existe et maj de son état à ERREUR. Tant que le traitement n'est pas
 * terminé, le lot reste dans l'état 'EN ERREUR' Contrôle que la période comptable existe en compta générale</dd>
 * <dt>Etape 2</dt>
 * <dd>Mis à jours des rentes accordées échues (maj du flag isBloquee) et maj du flag isAttenteMajBlocage à '1' Mis à
 * jour des retenues (maj du flag is retenue) maj du flag isAttenteMajRetenue à '1' Impression de la liste des retenues</dd>
 * <dt>Etape 3</dt>
 * <dd>Execution du paiement Traitement séparé des RA dites standards, des RA avec flag isAttenteMaj... Les RA avec flag
 * isAttenteMaj... seront traitées dans un journal séparé (journal des retenues/blocages). Pour chaque RA avec
 * retenues/blocage, maj du flag isAttenteMaj... à '0' après traitement.</dd>
 * <dt>Etape 4</dt>
 * <dd>Contrôle final</dd>
 * <dt>Etape 5</dt>
 * <dd>Mise à jour du lot dans l'état VALIDE ou PARTIEL si des erreurs 'mineurs' ont été détectées</dd>
 * </dl>
 * <p>
 * Toutes les RA n'ayant pas pu être traitées, sont marquées avec la flag isErreur = '1'. Un process spécifique permet
 * de re-traiter ces RA erronnées uniquement.
 * </p>
 * <p>
 * <b>Reprise sur erreur MAJEURE</b> <br/>
 * Cette étape est identifiée par l'état du lot à 'ERREUR'. Ce cas arrive lorsque le traitement du grand paiement n'a
 * pas pu s'exécuter entièrerement. C'est à dire, l'étape no 5 n'a pas pu s'effectuer.
 * </p>
 * <dl>
 * <dt>Etape 1</dt>
 * <dd>Annuler toutes les écritures comptables du journal contenant les RA dites standards, ainsi que le l'OG
 * correspondant. !!! Attention : Ne pas supprimer le journal des retenues/blocages, car les écritures déjà
 * comptabilisées ne seront pas regénérées.</dd>
 * <dt>Etape 2</dt>
 * <dd>Remettre toutes les RA avec le flag isErreur à '0'</dd>
 * <dt>Etape 3</dt>
 * <dd>Relancer le paiement. Le lot se trouvant dans l'état 'EN_ERREUR', les maj des retenues/blocages ne vont pas
 * s'effectuer. Dans cette étapes, toutes les RA dites 'standard' seront à nouveau traitées, et seul les RA avec
 * retenues/blocage ayant le flag 'isAttenteMaj...' à '1' seront retraitées. Celle à '0' ne seront pas recomptabilisée,
 * d'ou l'importance de ne pas supprimer le jounal des retenues/blocage en CA.</dd>
 * <dt>Etape 4</dt>
 * <dd>Supprimer la récapitulation du paiement pour les PC/RFM avec le script suivant :
 * <code>DELETE FROM SCHEMA.PCRFMREC WHERE MOIS = <i>[AAAAMM]</i></code></dd>
 * <dt>Point de non retour</dt>
 * <dd>Exception !!! Si le point de non retour n'a PAS été atteint (indiqué dans le mail si atteint), you're a lucky
 * guy!!! Aucune écriture comptable n'a été générérées. Dans ce cas, remettre le lot dans l'état ouvert, contrôler que
 * les jrn en CA sont bien vide et relancer le tout.</dd>
 * </dl>
 * 
 * @author SCR
 */
public class REExecuterPaiementMensuelProcess extends AREPmtMensuel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean currentRaEnErreur;

    private String dateDernierPmt;
    private String datePmtEnCours;
    /**
     * ATTENTION, NE PAS LAISSER EN MODE DEBUG POUR LA PRODUCTION
     */
    private final boolean DEBUG_MODE = false;
    private boolean isErreursDetectee;
    private REListeRecapitulativePaiement listeRecapitulativeePaiement = null;
    private REListeRecapitulativePaiementPC_RFM listeRecapitulativeePaiementPCRFM = null;
    private REListeRetenuesBlocages listeRetenues = null;
    private StringBuilder messageRente;
    private long nombreDeRentes;
    private long nombreDeRentesVersees;
    private boolean succes;

    public REExecuterPaiementMensuelProcess() {
        super();
        setThreadContextInitialized(true);
    }

    public REExecuterPaiementMensuelProcess(BProcess parent) {
        super(true, parent);
    }

    public REExecuterPaiementMensuelProcess(BSession session) {
        super(true, session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected void _validate() throws Exception {

        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

        if (JadeStringUtil.isBlankOrZero(getDescription())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DECSC_OBL"));
        } else if (getDescription().length() > 40) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DESC_40_CARS"));
        }

        // ISO20022
        CAOrganeExecution organeExecution = new CAOrganeExecution();
        organeExecution.setSession(getSession());
        organeExecution.setIdOrganeExecution(getIdOrganeExecution());
        organeExecution.retrieve();
        if (organeExecution.getCSTypeTraitementOG().equals(APIOrganeExecution.OG_OPAE_DTA)) {
            // BZ 5459
            if (!JadeNumericUtil.isInteger(getNumeroOG())
                    || (((Integer.parseInt(getNumeroOG())) < 1) || (Integer.parseInt(getNumeroOG()) > 99))) {
                this._addError(getTransaction(), getSession().getLabel("ERREUR_NUMERO_OG_OBLIGATOIRE"));
            }
        }

        // On contrôle que le mois précédent ait bien été validé...
        JADate moisPrecedent = new JADate(getMoisPaiement());
        JACalendar cal = new JACalendarGregorian();
        moisPrecedent = cal.addMonths(moisPrecedent, -1);

        RELotManager mgr = new RELotManager();
        mgr.setSession(getSession());
        mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
        mgr.setForCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.setForDateEnvoiInMMxAAAA(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(moisPrecedent.toStrAMJ()));
        mgr.find(getTransaction());

        if (mgr.size() != 1) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_LOT_MENS_MOIS_PREC_PAS_VALIDE"));
        }

        // On contrôle que le mois courant n'ai pas encore été traité...
        mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
        mgr.setForCsEtat(null);
        mgr.setForCsEtatIn(IRELot.CS_ETAT_LOT_OUVERT + ", " + IRELot.CS_ETAT_LOT_ERREUR);
        mgr.setForDateEnvoiInMMxAAAA(getMoisPaiement());
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.find(getTransaction());

        if (mgr.size() > 1) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_PLS_LOTS_OUVERTS_TROUVES"));
        } else if (mgr.size() == 0) {
            this._addError(getTransaction(),
                    getSession().getLabel("ERREUR_PREAPARATION_PMTMENSUEL_PAS_ENCORE_EFFECTUEE"));
        }

        // vérifier que tous les lots de décisions soient validés.
        // bz-4187
        mgr = new RELotManager();
        mgr.setSession(getSession());
        mgr.setForCsEtatDiffentDe(IRELot.CS_ETAT_LOT_VALIDE);
        mgr.setForCsType(IRELot.CS_TYP_LOT_DECISION);
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.find(getTransaction(), 1);

        if (!mgr.isEmpty()) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_PREAPARATION_LOT_NON_VALIDE"));
        }

        // On tente d'accéder à cette propriété déjà ici pour éviter que ça casse plus loin
        // si elle n'est pas déclarée. ELLE EST OBLIGATOIRE
        try {
            CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue();
        } catch (Exception exception) {
            String message = "Une erreur est intervenue lors de l'accès à la propriété [common."
                    + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getPropertyName()
                    + "]. Cette propriété doit obligatoirement être définie (valeurs possible [true, false]) pour pouvoir exécuter le paiement principal."
                    + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getDescription();
            this._addError(getTransaction(), message);
        }
    }

    private void appendToMessageRente(String value) {
        if (DEBUG_MODE) {
            messageRente.append(value + ";");
        }
    }

    /**
     * Cette méthode permet de mettre à jour les ordres de versement du journal "rapide" en état versé
     * 
     * @param compta
     * @throws Exception
     */
    private void changeOrdreVersementInEtatVerse(APIGestionRentesExterne compta) throws Exception {
        // Mise à jour des ordres de versement du journal rapide en état versé
        compta.updateOperationOrdreVersementInEtatVerse(getSession());

    }

    private void doMiseAJourRentesEchuesProcess(BTransaction transaction) throws Exception {

        // Ce traitement ne met a jours que les rentes devenant échues ce mois.
        REMiseAJourRentesEchuesProcess miseAJourRentesEchuesProcess = new REMiseAJourRentesEchuesProcess(getSession());
        miseAJourRentesEchuesProcess.setTransaction(null);
        miseAJourRentesEchuesProcess.setMemoryLog(getMemoryLog());
        miseAJourRentesEchuesProcess.doTraitement(transaction);

        // Les rentes déjà échues (bloquées) ne sont pas impactées par le traitement ci-dessus.
        // Il faut donc les parcourir et mette à jours le flag isAttenteMajBlocage.
        REPrestationAccordeeManager mgr = new REPrestationAccordeeManager();
        mgr.setSession(getSession());
        mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
        mgr.setForIsAttenteMajBlocage(Boolean.FALSE);
        mgr.setForIsPrestationBloquee(Boolean.TRUE);
        BStatement statement = mgr.cursorOpen(transaction);
        REPrestationsAccordees pa = null;
        while ((pa = (REPrestationsAccordees) mgr.cursorReadNext(statement)) != null) {
            pa.retrieve(transaction);
            pa.setIsAttenteMajBlocage(Boolean.TRUE);
            pa.update(transaction);
        }
    }

    /**
     * Lance le process de mise à jour des retenues
     * 
     * @param transaction
     * @throws Exception
     */
    private void doMiseAJourRetenuesProcess(BTransaction transaction) throws Exception {
        REMiseAJourRetenuesProcess miseAJourRetenuesProcess = new REMiseAJourRetenuesProcess(getSession());
        miseAJourRetenuesProcess.setTransaction(null);
        miseAJourRetenuesProcess.setMemoryLog(getMemoryLog());
        miseAJourRetenuesProcess.doTraitement(transaction);
    }

    private String getMessageRenteEntete() {
        return "ZTIPRA;ZTLCPR;ZTLSCP;YNICOA;YNITAP;ZTITBE;ZTMPRE;RUBCOMPTA;INCSECTION;INCREMENT;";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void logDebug(String message) {
        if (DEBUG_MODE) {
            System.out.println(message);
        }
    }

    /**
     * Remplit la liste récapitulative des rentes PC RFM
     * 
     * @throws Exception
     */
    private void remplirRecapitulationPcRfm() throws Exception {
        RecapitulationPcRfmService recapService = PrestationCommonServiceLocator.getRecapitulationPcRfmService();
        RecapitulationPcRfm recapDuMois = recapService.findInfoRecapByDate(getMoisPaiement());

        // s'il n'y a pas encore de récap pour le mois, on la crée
        if (recapDuMois == null) {
            RERecapitulationPaiementAdapter adapter = new RERecapitulationPaiementAdapter(getSession(),
                    getMoisPaiement());
            adapter.chargerParGenrePrestation();

            Map<CodeRecapitulationPcRfm, String> montantRecap = adapter.getMapRecapPcRfm();
            for (CodeRecapitulationPcRfm code : montantRecap.keySet()) {
                SimpleRecapitulationPcRfm recapPart = new SimpleRecapitulationPcRfm();
                recapPart.setCode(code);
                recapPart.setValeur(montantRecap.get(code));
                recapPart.setMois(getMoisPaiement());
                JadePersistenceManager.add(recapPart);
            }
        }
        JadeThread.commitSession();
        JadeThread.logClear();
    }

    @Override
    protected boolean runProcess() throws Exception {

        // Ne comprend pas les rentes bloquées, ni les retenues
        nombreDeRentes = 0;
        nombreDeRentesVersees = 0;
        succes = false;
        isErreursDetectee = false;
        PrintStream sysout = null;

        // Transaction globale, utilisée pour la requête principale.
        BTransaction transaction = getTransaction();
        // Transaction interne utilisée dans la boucle pour les écritures comptables.
        BTransaction innerTransaction = null;
        APIGestionRentesExterne compta = null;

        try {

            _validate();

            dateDernierPmt = REPmtMensuel.getDateDernierPmt(getSession());
            JADate jd = new JADate("01." + dateDernierPmt);
            JACalendar cal = new JACalendarGregorian();
            jd = cal.addMonths(jd, 1);
            datePmtEnCours = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(jd.toStrAMJ());
            logDebug("Date du paiement en cours : " + datePmtEnCours);
            transaction.openTransaction();

            // La transaction pour chaque groupe d'opération
            innerTransaction = (BTransaction) getSession().newTransaction();
            innerTransaction.openTransaction();

            // On récupère le lot ouvert, préalablement préparé lors du
            // traitement de préparation
            RELotManager mgrl = new RELotManager();
            mgrl.setSession(getSession());
            mgrl.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgrl.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            mgrl.setForDateEnvoiInMMxAAAA(getMoisPaiement());
            mgrl.find(innerTransaction);

            RELot lot = null;
            if (mgrl.size() > 1) {
                throw new Exception(getSession().getLabel("ERREUR_PLS_LOTS_OUVERTS_TROUVES"));
            } else if (mgrl.size() == 0) {
                throw new Exception(getSession().getLabel("ERREUR_PREAPARATION_PMTMENSUEL_PAS_ENCORE_EFFECTUEE"));
            } else {
                lot = (RELot) mgrl.getFirstEntity();
            }
            lot.retrieve(innerTransaction);

            logDebug("Id du lot utilisé pour le paiement mensuel : " + lot.getIdLot());

            getMemoryLog().logMessage(
                    "Starting process PaiementMensuelRentes : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            if (getSession().hasErrors()) {
                succes = false;
                throw new Exception("Validation failed !!!");
            }
            innerTransaction = commitResetTransaction(innerTransaction);
            getMemoryLog().setTransaction(innerTransaction);

            // Instanciation du processus standard de compta
            BISession sessionOsiris = null;

            sessionOsiris = PRSession.connectSession(getSession(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
            compta = (APIGestionRentesExterne) sessionOsiris.getAPIFor(APIGestionRentesExterne.class);

            // Creation des journaux
            compta.createJournal(getSession(), innerTransaction, getDescription() + " ", "01." + getMoisPaiement());

            compta.createOrdreGroupe(getSession(), innerTransaction, getSession().getLabel("PMT_MENSUEL_OG") + " "
                    + getDescription() + " ", getDateEcheancePaiement(), getNumeroOG(), getIdOrganeExecution());

            boolean isRepriseSurErreurMajeur = false;
            if (IRELot.CS_ETAT_LOT_ERREUR.equals(lot.getCsEtatLot())) {
                isRepriseSurErreurMajeur = true;
            } else if (IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtatLot())) {
                // MAJ du lot dans l'état ERREUR
                lot.setDescription(getSession().getLabel("PMT_MENS_RENTES") + " " + getMoisPaiement());
                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_ERREUR);
                lot.update(innerTransaction);
                innerTransaction.commit();
            } else {
                throw new Exception("Incohérance dans les données. L'Etat du lot est incorrect");
            }

            if (!isRepriseSurErreurMajeur) {
                getMemoryLog().logMessage("Mise a jours rentes echues : " + (new JATime(JACalendar.now())).toStr(":"),
                        FWMessage.INFORMATION, "");

                // Mise à jours du flag isPrestationBloquee des RA
                doMiseAJourRentesEchuesProcess(innerTransaction);

                // Mise à jours du flag isRetenues des RA
                getMemoryLog().logMessage("Mise a jours retenues : " + (new JATime(JACalendar.now())).toStr(":"),
                        FWMessage.INFORMATION, "");
                doMiseAJourRetenuesProcess(innerTransaction);

                innerTransaction = commitResetTransaction(innerTransaction);
            } else {
                // On commit la transaction. Obligé, pour garder la visibilité
                // des journaux créés en compta.
                innerTransaction = commitResetTransaction(innerTransaction);
            }

            // On imprime une nouvelle fois la liste des retenues/blocage
            getMemoryLog().logMessage("Impression de la liste des retenues/blocages.", FWMessage.INFORMATION, "");
            startListeRetenues();
            if (!listeRetenues.isOnError()) {
                for (int i = 0; i < listeRetenues.getAttachedDocuments().size(); i++) {
                    getAttachedDocuments().add(listeRetenues.getAttachedDocuments().get(i));
                }
            }

            // On imprime une nouvelle fois la liste de recapitulation
            getMemoryLog().logMessage("Impression de la liste de récapitulation.", FWMessage.INFORMATION, "");
            startListeRecapitualation(transaction);
            if (!listeRecapitulativeePaiement.isOnError()) {
                for (int i = 0; i < listeRecapitulativeePaiement.getAttachedDocuments().size(); i++) {
                    getAttachedDocuments().add(listeRecapitulativeePaiement.getAttachedDocuments().get(i));
                }
            }

            if (listeRecapitulativeePaiementPCRFM != null) {
                if (!listeRecapitulativeePaiementPCRFM.isOnError()) {
                    for (int i = 0; i < listeRecapitulativeePaiementPCRFM.getAttachedDocuments().size(); i++) {
                        getAttachedDocuments().add(listeRecapitulativeePaiementPCRFM.getAttachedDocuments().get(i));
                    }
                }
            }

            // Contrôle qu'il n'y ai plus de RA en erreur. //
            getMemoryLog().logMessage("2a) before getSqlCount : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            nombreDeRentes = getNombreRentes(getSession(), innerTransaction, Boolean.TRUE, null, null);
            getMemoryLog().logMessage("2b) after getSqlCount : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            if (nombreDeRentes > 0) {
                throw new Exception("Il y en encore des Rentes Accordées en Erreur, impossible de lancer le pmt.");
            }

            // reservation de la plage d'increment pour les écritures en CA
            getMemoryLog().logMessage("2a) before getSqlCount : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            // sans les retenues et blocage
            nombreDeRentes = getNombreRentes(getSession(), innerTransaction, Boolean.FALSE, Boolean.FALSE,
                    Boolean.FALSE);

            nombreDeRentesVersees = nombreDeRentes;

            getMemoryLog().logMessage("2b) after getSqlCount : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            PlageIncrement plageIncrementsOperations = reserverPlageIncrementsOperationsCA(getSession(), nombreDeRentes);

            logDebug("Plage d'increments des operations réservée : " + plageIncrementsOperations);
            PlageIncrement plageIncrementsSections = reserverPlageIncrementsSectionsCA(getSession(), nombreDeRentes);

            logDebug("Plage d'increments des sections réservé : " + plageIncrementsSections);
            if ((plageIncrementsOperations == null) || getMemoryLog().hasErrors()) {
                throw new Exception("Erreur lors la reservation de la plage d'increments en CA");
            } else {
                getMemoryLog().logMessage(
                        "Plage incrément : min/max = " + plageIncrementsOperations.min + "/"
                                + plageIncrementsOperations.max, FWMessage.INFORMATION, "");
            }

            // Récupération des rentes à verser...
            REPaiementRentesManager mgr = new REPaiementRentesManager();
            mgr.setSession(getSession());
            mgr.setForDatePaiement(getMoisPaiement());
            mgr.setForIsEnErreur(Boolean.FALSE);
            BStatement statement = null;
            REPaiementRentes rente = null;
            statement = mgr.cursorOpen(transaction);

            Key previousKey = null;
            Key currentKey = null;
            long increment = plageIncrementsOperations.min;
            long incrementSection = plageIncrementsSections.min;

            // Récupération des infos CA, idCompteCourant, idRubrique
            String noSecteurRente = getNoSecteurRente(getSession());

            String idCompteCourant = getIdCompteCourant(getSession(), innerTransaction,
                    APISection.ID_TYPE_SECTION_RENTE_AVS_AI, noSecteurRente);

            String idRubriqueOV = getIdRubriquePourVersement(getSession(), innerTransaction);

            REGroupOperationCAUtil grpOP = null;
            // Utilise comme clé de tri dans les listes des opérations par
            // journaux en compta aux.
            REGroupOperationMotifUtil motifVersement = null;
            String nomCache = null;
            if (innerTransaction.hasErrors() || getSession().hasErrors()) {
                throw new Exception(innerTransaction.getErrors().toString());
            }

            getMemoryLog().logMessage("3a) before main sql request : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            boolean isOperationAdded = false;

            // Contrôle que la période comptable soit bien ouverte...
            // Il faut passer la innerTransaction dans l'objet comptaAux, pour
            // éviter des lock entre transaction, car
            // c'est la inner transaction qui est utilisé pour les écritures
            // comptable, également pour les blocage/retenues...
            initComptaExterne(innerTransaction, false);

            if (!comptaExt.isPeriodeComptableOuverte("01." + getMoisPaiement())) {
                throw new Exception("Aucune période comptable ouverte pour la période " + getMoisPaiement());
            }
            // On force la création du journal
            initComptaExterne(innerTransaction, true);

            innerTransaction = commitResetTransaction(innerTransaction);

            Map<Integer, RECumulPrstParRubrique> mapCumulPrstParGenreRentes = new HashMap<Integer, RECumulPrstParRubrique>();
            int counterTransaction = 0;

            String currentIdRA = "aaa";
            String previousIdRa = "bbb";
            getMemoryLog().logMessage("Point de non retour atteint !!!", FWMessage.INFORMATION, "");

            remplirRecapitulationPcRfm();

            int noSectionIncrement = 0;
            currentRaEnErreur = false;
            PrintStream derivationLogs = new PrintStream(new ByteArrayOutputStream());
            sysout = System.out;
            if (DEBUG_MODE) {
                System.setOut(derivationLogs);
                messageRente = new StringBuilder(getMessageRenteEntete());
            }
            while ((rente = (REPaiementRentes) mgr.cursorReadNext(statement)) != null) {

                // Ecriture du message pour la rente précédente
                if (DEBUG_MODE) {
                    if (currentRaEnErreur) {
                        messageRente.append(" -> RA mise en erreur");
                    }
                    System.setOut(sysout);
                    logDebug(messageRente.toString());
                    messageRente = new StringBuilder();
                    System.setOut(derivationLogs);
                }
                currentRaEnErreur = false;

                if (DEBUG_MODE) {
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getIdRenteAccordee(), 10, "0"));
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getCodePrestation(), 3, "0"));
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getSousTypeCodePrestation(), 3, "0"));
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getIdCompteAnnexe(), 10, "0"));
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getIdTiersAdressePmt(), 10, "0"));
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getIdTiersBeneficiaire(), 10, "0"));
                    appendToMessageRente(PRStringFormatter.indentLeft(rente.getMontant(), 8, "0"));
                }

                try {
                    // Contrôle que la ra n'est pas traitée 2 fois, ne devrait
                    // jamais arriver. cf. bz-3675
                    currentIdRA = rente.getIdRenteAccordee();

                    if (currentIdRA.equals(previousIdRa)) {
                        if (grpOP != null) {
                            grpOP.setGroupOperationEnErreur(true);
                        } else {
                            doMiseEnErreurRA(getSession(), currentIdRA,
                                    getSession().getLabel("RENTE_ACCORDEE_TRAITMT_DOUBLE"));
                        }
                        getMemoryLog().logMessage(getSession().getLabel("RENTE_ACCORDEE_TRAITMT_DOUBLE"),
                                FWMessage.ERREUR,
                                getSession().getLabel("CONTROLER_ADR_PMT_RA") + " idRA = " + currentIdRA);
                        isErreursDetectee = true;
                        continue;
                    }
                    previousIdRa = currentIdRA;

                    try {
                        currentKey = getKey(rente);
                    } catch (Exception e) {
                        // En cas d'erreur de récupération de la clé (par exemple
                        // pas de CA) on met la RA en erreur !!!
                        doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), e.toString());
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                        JadeLogger.error(this, e);
                        --nombreDeRentesVersees;
                        isErreursDetectee = true;
                        currentRaEnErreur = true;
                        continue;
                    }
                    String idTiersPrincipal = rente.getIdTiersAdressePmt();

                    if (JadeStringUtil.isBlankOrZero(idTiersPrincipal) || Long.parseLong(idTiersPrincipal) < 0) {
                        idTiersPrincipal = rente.getIdTiersBeneficiaire();
                    }
                    String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(getSession(), idTiersPrincipal);
                    // 1ère écriture...
                    if (previousKey == null) {
                        noSectionIncrement = 0;

                        // Le motif de versement est généré par rapport à la 1ère écriture
                        // trouvée pour chaque groupe d'opération, car trié par groupe
                        motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                rente.getCodePrestation(), isoLangFromIdTiers);
                        nomCache = getNomCache(rente);
                        getMemoryLog().logMessage(
                                "3b) after main sql request : " + (new JATime(JACalendar.now())).toStr(":"),
                                FWMessage.INFORMATION, "");

                        grpOP = new REGroupOperationCAUtil();
                        grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                getIdAdrPmt(rente));

                        traiterRente(rente, grpOP, increment);
                    }

                    // Les clé sont identiques -> toujours dans la même adresse
                    else if (currentKey.equals(previousKey)) {
                        motifVersement.addCodePrest(rente.getCodePrestation());
                        traiterRente(rente, grpOP, increment);
                    }

                    // Nouvelle adresse de pmt
                    else {
                        long incrEcritureTypeVersement = increment;
                        long incOV = incrEcritureTypeVersement + 1;
                        try {
                            // Si l'on est toujours dans le même compte annexe, il faut utiliser un nouveau no de
                            // section.
                            if ((currentKey.idCompteAnnexe != null)
                                    && currentKey.idCompteAnnexe.equals(previousKey.idCompteAnnexe)) {
                                noSectionIncrement++;
                            }
                            // On change de compte annexe,
                            else {
                                noSectionIncrement = 0;
                            }
                            counterTransaction++;
                            RECumulPrstParRubrique[] array = grpOP.doTraitementComptable(this, getSession(),
                                    innerTransaction, compta, motifVersement.getMotif(getSession()),
                                    incrEcritureTypeVersement, incOV, incrementSection, datePmtEnCours, nomCache,
                                    noSectionIncrement, getDateEcheancePaiement());

                            for (RECumulPrstParRubrique element : array) {
                                mapCumulPrstParGenreRentes = grpOP
                                        .cumulParRubrique(mapCumulPrstParGenreRentes, element);
                            }

                            isOperationAdded = true;
                            // On reset la transaction. Ne peut être fait qu'après
                            // le traitement complet d'un groupe.
                            if (counterTransaction >= 50) {
                                counterTransaction = 0;
                                // innerTransaction =
                                // commitResetTransaction(innerTransaction);
                            }
                        } catch (Exception e) {
                            --nombreDeRentesVersees;
                            isErreursDetectee = true;
                            JadeLogger.error(this, e);
                            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                        }

                        // On passe au groupe suivant
                        ++incrementSection;
                        ++increment;
                        ++increment;

                        // Réinitialisation du nouveau groupe
                        grpOP = new REGroupOperationCAUtil();
                        grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                getIdAdrPmt(rente));

                        // Le motif de versement est généré par rapport à la 1ère
                        // écriture trouvée pour
                        // chaque groupe d'opération, car trié par groupe
                        motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                rente.getCodePrestation(), isoLangFromIdTiers);

                        nomCache = getNomCache(rente);
                        traiterRente(rente, grpOP, increment);

                    }
                    if (DEBUG_MODE) {
                        appendToMessageRente(PRStringFormatter.indentLeft(String.valueOf(incrementSection), 6, "0"));
                        appendToMessageRente(PRStringFormatter.indentLeft(String.valueOf(increment), 6, "0"));
                    }

                    previousKey = new Key(currentKey);
                    ++increment;

                } catch (Exception e) {
                    doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), e.toString());
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                    --nombreDeRentesVersees;
                    isErreursDetectee = true;
                    continue;
                }
            }

            // currentKey sera a null, si aucune rente trouvée...
            if (currentKey != null) {

                // Génère les écritures comptables pour le dernier groupe...
                try {
                    // A savoir, on commence par créer la section 1, puis la 2, 3, etc et on finis avec la section 0
                    RECumulPrstParRubrique[] array = grpOP.doTraitementComptable(this, getSession(), innerTransaction,
                            compta, motifVersement.getMotif(getSession()), increment, ++increment, incrementSection,
                            datePmtEnCours, nomCache, 0, getDateEcheancePaiement());

                    for (RECumulPrstParRubrique element : array) {
                        mapCumulPrstParGenreRentes = grpOP.cumulParRubrique(mapCumulPrstParGenreRentes, element);
                    }

                    isOperationAdded = true;
                } catch (Exception e) {
                    --nombreDeRentesVersees;
                    isErreursDetectee = true;
                    JadeLogger.error(this, e);
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                }

                innerTransaction = commitResetTransaction(innerTransaction);
                transaction = commitResetTransaction(transaction);

                // Des écritures ont été créé en compta --> on peut
                // comptabiliser...
                if (isOperationAdded) {
                    // On comptabilise le processus de compta externe, utilisé
                    // pour le traitement des RA bloquées et retenues

                    if (comptaExt != null) {
                        getMemoryLog().logMessage(
                                "Comptabilisation du journal des retenues/blocages : "
                                        + (new JATime(JACalendar.now())).toStr(":"), FWMessage.INFORMATION, "");
                        comptaExt.comptabiliser();
                    }
                }

                if (!isErreursDetectee) {
                    lot.retrieve(innerTransaction);
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                    lot.update(innerTransaction);
                } else {
                    lot.retrieve(innerTransaction);
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_PARTIEL);
                    lot.update(innerTransaction);

                }
            }
            // Aucune rente à verser, le lot passe en validé
            else {
                lot.retrieve(innerTransaction);
                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                lot.update(innerTransaction);
            }
            if (DEBUG_MODE) {
                System.setOut(sysout);
            }
            innerTransaction = commitResetTransaction(innerTransaction);

            getMemoryLog().logMessage(
                    "Comptabilisation des journaux grand pmt/OG : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            compta.finalize(this, getSession(), innerTransaction);

            getMemoryLog().logMessage("3b) Process ending at : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("Paiement mensuel terminé.", FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("=============================================================",
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("==                      RESUME                             ==",
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("=============================================================",
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("", FWMessage.INFORMATION, "");
            getMemoryLog().logMessage("Nombre de rentes à verser (exclus prst. bloquées)= " + nombreDeRentes,
                    FWMessage.INFORMATION, "");
            getMemoryLog().logMessage(
                    "Nombre de rentes versées (exclus prst. bloquées, retenues)= " + nombreDeRentesVersees,
                    FWMessage.INFORMATION, "");
            // Bug 8400
            // Changer l'état des ordre de versement du journal rapide en état versé
            changeOrdreVersementInEtatVerse(compta);

            validationPmt(getSession(), compta, comptaExt, mapCumulPrstParGenreRentes);

            innerTransaction = commitResetTransaction(innerTransaction);

            String libelleOG = getDescription() + " (suite)";
            if (comptaExt != null) {
                getMemoryLog().logMessage(
                        "Préparation de l'OG des retenues/blocages : " + (new JATime(JACalendar.now())).toStr(":"),
                        FWMessage.INFORMATION, "");
                String numOG = "";
                if (isIso20022(getIdOrganeExecution(), getSession())) {
                    libelleOG = "ISO20022 - " + libelleOG;
                } else {
                    int n = Integer.parseInt(getNumeroOG());
                    n++;
                    if (n < 10) {
                        libelleOG = "OPAE 0" + n + " - " + libelleOG;
                    } else {
                        libelleOG = "OPAE" + n + " - " + libelleOG;
                    }
                    numOG = String.valueOf(n);
                }
                comptaExt.preparerOrdreGroupe(getIdOrganeExecution(), numOG, getDateEcheancePaiement(),
                        CAOrdreGroupe.VERSEMENT, CAOrdreGroupe.NATURE_RENTES_AVS_AI, libelleOG, getIsoCsTypeAvis(),
                        getIsoGestionnaire(), getIsoHighPriority());

            }

            succes = true;

            Set<Integer> keys = mapCumulPrstParGenreRentes.keySet();
            Iterator<Integer> iter = keys.iterator();
            while (iter.hasNext()) {
                Integer key = iter.next();
                RECumulPrstParRubrique cppr = mapCumulPrstParGenreRentes.get(key);

                String descrRubrique = "";
                try {
                    CARubrique rub = new CARubrique();
                    rub.setSession(getSession());
                    rub.setIdRubrique(cppr.getIdRubrique());
                    rub.retrieve();
                    descrRubrique = rub.getDescription();
                } catch (Exception e) {
                    descrRubrique = cppr.getIdRubrique();
                }

                getMemoryLog().logMessage(
                        "Type - Rubrique : " + cppr.getType() + " - " + descrRubrique + " \tmontant ="
                                + cppr.getMontant(), FWMessage.INFORMATION, "");
            }

            innerTransaction = commitResetTransaction(innerTransaction);
        } catch (Exception e) {

            succes = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            if (innerTransaction.hasErrors()) {
                getMemoryLog().logMessage(innerTransaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            try {
                innerTransaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            throw e;
            // return false;
        } finally {

            if (succes && !isErreursDetectee) {
                emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_SUCCES");
            } else {
                emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_ERREUR");
            }

            // Dans tous les cas on remet le flux de sortie du sysout
            if (sysout != null) {
                System.setOut(sysout);
            }
            try {
                // Workaround
                // Vidage du cache, sans quoi, l'execution de l'OG plante. Raison : inconnue !!!!
                GlobazServer.getCurrentSystem().resetCache();

                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
                docInfo.setDocumentTitle("listeRetenues");
                this.mergePDF(docInfo, "listeRetenues", true, 0, false, null);

                if (innerTransaction != null) {
                    innerTransaction.closeTransaction();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (innerTransaction != null) {
                        innerTransaction.closeTransaction();
                    }
                    compta.closeAllStatements();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void startListeRecapitualation(BTransaction transaction) throws Exception {

        // On charge l'adapter
        RERecapitulationPaiementAdapter adapter = new RERecapitulationPaiementAdapter(getSession(), getMoisPaiement());
        adapter.chargerParGenrePrestation();

        listeRecapitulativeePaiement = new REListeRecapitulativePaiement(getSession());
        listeRecapitulativeePaiement.setSession(getSession());
        listeRecapitulativeePaiement.setEMailAddress(getEMailAddress());
        listeRecapitulativeePaiement.setSendCompletionMail(true);
        listeRecapitulativeePaiement.setSendMailOnError(true);
        listeRecapitulativeePaiement.setMemoryLog(getMemoryLog());
        listeRecapitulativeePaiement.setTransaction(transaction);
        listeRecapitulativeePaiement.setForMoisAnnee(getMoisPaiement());
        listeRecapitulativeePaiement.setAdapter(adapter);
        listeRecapitulativeePaiement.executeProcess();

        // Toutes les caisses n'ont pas de PC/RFM !!!
        if (adapter.getNbTotal2General() > 0) {
            listeRecapitulativeePaiementPCRFM = new REListeRecapitulativePaiementPC_RFM();
            listeRecapitulativeePaiementPCRFM.setSession(getSession());
            listeRecapitulativeePaiementPCRFM.setEMailAddress(getEMailAddress());
            listeRecapitulativeePaiementPCRFM.setSendCompletionMail(true);
            listeRecapitulativeePaiementPCRFM.setSendMailOnError(true);
            listeRecapitulativeePaiementPCRFM.setMemoryLog(getMemoryLog());
            listeRecapitulativeePaiementPCRFM.setTransaction(transaction);
            listeRecapitulativeePaiementPCRFM.setForMoisAnnee(getMoisPaiement());
            listeRecapitulativeePaiementPCRFM.setAdapter(adapter);
            listeRecapitulativeePaiementPCRFM.executeProcess();
        }

    }

    private void startListeRetenues() throws Exception {
        listeRetenues = new REListeRetenuesBlocages(getSession());
        listeRetenues.setMois(getMoisPaiement());
        listeRetenues.setSession(getSession());
        listeRetenues.setEMailAddress(getEMailAddress());
        listeRetenues.setSendCompletionMail(true);
        listeRetenues.setSendMailOnError(true);
        listeRetenues.setMemoryLog(getMemoryLog());
        listeRetenues.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        listeRetenues.executeProcess();

    }

    private void traiterRente(REPaiementRentes rente, REGroupOperationCAUtil grpOP, long increment) throws Exception {
        try {
            APIRubrique rubriqueComptable = AREModuleComptable.getRubriqueWithInit(initSessionOsiris(),
                    rente.getCodePrestation(), rente.getSousTypeCodePrestation(),
                    AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

            if (DEBUG_MODE) {
                appendToMessageRente(PRStringFormatter.indentLeft(rubriqueComptable.getIdExterne(), 14, "0"));
            }

            checkAPIRubrique(rubriqueComptable, rente);

            grpOP.traiterEcriture(getSession(), rente,
                    rente.getNomTBE() + " " + rente.getPrenomTBE() + " " + rente.getCodePrestation(), increment,
                    rubriqueComptable == null ? null : rubriqueComptable.getIdRubrique(), dateDernierPmt,
                    datePmtEnCours, false);

        } catch (Exception e) {
            doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), e.toString());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            --nombreDeRentesVersees;
            isErreursDetectee = true;
            currentRaEnErreur = true;
            // continue;
        }

    }
}
