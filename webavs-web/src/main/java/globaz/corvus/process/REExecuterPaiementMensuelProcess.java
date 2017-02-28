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
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.itext.REListeRecapitulativePaiement;
import globaz.corvus.itext.REListeRecapitulativePaiementPC_RFM;
import globaz.corvus.itext.REListeRetenuesBlocages;
import globaz.corvus.itext.RERecapitulationPaiementAdapter;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.process.paiement.mensuel.Key;
import globaz.corvus.process.paiement.mensuel.ResultatTraitementRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.pmt.mensuel.RECumulPrstParRubrique;
import globaz.corvus.utils.pmt.mensuel.REGroupOperationCAUtil;
import globaz.corvus.utils.pmt.mensuel.REGroupOperationMotifUtil;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String HH_MM_SS = "HH:mm:ss";
    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(REExecuterPaiementMensuelProcess.class);

    List<String> mailLogs = new LinkedList<String>();

    public REExecuterPaiementMensuelProcess(BProcess parent) {
        super(true, parent);
    }

    public REExecuterPaiementMensuelProcess() {
        super();
        setThreadContextInitialized(true);
    }

    public REExecuterPaiementMensuelProcess(BSession session) {
        super(true, session);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean runProcess() throws Exception {
        boolean result = true;
        int nombreDeRentesAVersees = 0;
        boolean aCommencerDeTraiterLesRentes = false;

        Map<Integer, RECumulPrstParRubrique> mapCumulPrstParGenreRentes = new HashMap<Integer, RECumulPrstParRubrique>();
        List<ResultatTraitementRente> renteTraiterAvecSucces = new LinkedList<ResultatTraitementRente>();
        List<ResultatTraitementRente> renteTraiterAvecErreurs = new LinkedList<ResultatTraitementRente>();

        try {

            String message = "Démarrage du process " + new SimpleDateFormat(HH_MM_SS).format(new Date());
            mailLogs.add(message);
            logger.info(message);

            // Transaction globale, utilisée pour la requête principale.
            BTransaction transaction = getTransaction();
            transaction.openTransaction();

            String dateDernierPmt = recupererDateDernierPaiement();
            message = "Date du dernier paiement : " + dateDernierPmt;
            mailLogs.add(message);
            logger.info(message);

            String datePmtEnCours = calculerDatePaiementEnCours(dateDernierPmt);
            message = "Date du paiement en cours : " + datePmtEnCours;
            mailLogs.add(message);
            logger.info(message);

            RELot lot = recupererLotPaiement(transaction);
            transaction = commitResetTransaction(transaction);
            message = "Id du lot utilisé pour le paiement mensuel : " + lot.getIdLot();
            mailLogs.add(message);
            logger.info(message);

            boolean isRepriseSurErreurMajeur = isReprisePaiementSuiteAUneErreur(transaction, lot);
            transaction = commitResetTransaction(transaction);

            if (isRepriseSurErreurMajeur) {
                message = "Exécution du paiement suite à des erreurs : " + lot.getIdLot();
                mailLogs.add(message);
                logger.info(message);
            } else {
                message = "Exécution du paiement standard : " + lot.getIdLot();
                mailLogs.add(message);
                logger.info(message);

                // Mise à jours du flag isPrestationBloquee des RA
                message = "Exécution de la mise à jour des rentes échues";
                mailLogs.add(message);
                logger.info(message);
                doMiseAJourRentesEchuesProcess(transaction);
                transaction = commitResetTransaction(transaction);

                // Mise à jours du flag isRetenues des RA
                message = "Exécution de la mise à jour des retenues";
                mailLogs.add(message);
                logger.info(message);
                doMiseAJourRetenuesProcess(transaction);
                transaction = commitResetTransaction(transaction);
            }

            // Impression de la liste des retenues/blocage
            message = "Impression de la liste des retenues/blocages";
            mailLogs.add(message);
            logger.info(message);
            startListeRetenues(transaction);
            transaction = commitResetTransaction(transaction);

            // Impression de la liste de recapitulation
            message = "Impression de la liste de récapitulation";
            mailLogs.add(message);
            logger.info(message);
            startListeRecapitualation(transaction);
            transaction = commitResetTransaction(transaction);

            // Impression de la liste de récapitulation des PC / RFM
            message = "Impression de la liste de récapitulation des PC / RFM";
            mailLogs.add(message);
            logger.info(message);
            remplirRecapitulationPcRfm();
            transaction = commitResetTransaction(transaction);

            // Contrôle qu'il n'y ai plus de RA en erreur.
            message = "Contrôle qu'il n'y ai plus de RA en erreur";
            mailLogs.add(message);
            logger.info(message);
            controlePlusDeRenteEnErreur(transaction);
            transaction = commitResetTransaction(transaction);

            // Récupération des rentes à verser...
            REPaiementRentesManager mgr = new REPaiementRentesManager();
            mgr.setSession(getSession());
            mgr.setForDatePaiement(getMoisPaiement());
            mgr.setForIsEnErreur(Boolean.FALSE);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            nombreDeRentesAVersees = mgr.getContainer().size();

            if (nombreDeRentesAVersees > 0) {
                message = nombreDeRentesAVersees + " rentes à verser";
                mailLogs.add(message);
                logger.info(message);

                // Contrôle des rentes avant de boucler pour le paiement
                message = "Contrôle des rentes avant l'initialisation de la compta ";
                mailLogs.add(message);
                logger.info(message);
                controleDesRentes(mgr.getContainer());

                // ---------------------------------------------------------------
                // Initialisation de la comptabilité
                // ---------------------------------------------------------------
                message = "Initialisation de la comptabilité";
                mailLogs.add(message);
                logger.info(message);

                // reservation de la plage d'increment pour les écritures en CA
                // FIXME --> joyeux bordel la réservation de ces incréments de merde
                PlageIncrement plageIncrementsOperations = reserverPlageIncrementsOperationsCA(getSession(),
                        transaction, nombreDeRentesAVersees);
                PlageIncrement plageIncrementsSections = reserverPlageIncrementsSectionsCA(getSession(),
                        nombreDeRentesAVersees);
                transaction = commitResetTransaction(transaction);

                message = "Plage d'incrément réservée pour les opérations : min/max = " + plageIncrementsOperations.min
                        + "/" + plageIncrementsOperations.max;
                mailLogs.add(message);
                logger.info(message);

                message = "Plage d'incrément réservée pour les sections : min/max = " + plageIncrementsSections.min
                        + "/" + plageIncrementsSections.max;
                mailLogs.add(message);
                logger.info(message);

                // Récupération du numéro de secteur des rentes
                String noSecteurRente = getNoSecteurRente(getSession());
                message = "Numéro de secteur utilisé pour les rentes [" + noSecteurRente + "] ";
                mailLogs.add(message);
                logger.info(message);

                // Récupération du compte courant
                String idCompteCourant = getIdCompteCourant(getSession(), transaction,
                        APISection.ID_TYPE_SECTION_RENTE_AVS_AI, noSecteurRente);
                message = "Id du compte courant [" + idCompteCourant + "] ";
                mailLogs.add(message);
                logger.info(message);

                // Récupération de la rubrique pour les OVs
                String idRubriqueOV = getIdRubriquePourVersement(getSession(), transaction);
                message = "Id rubrique pour les OVs [" + idCompteCourant + "] ";
                mailLogs.add(message);
                logger.info(message);

                transaction = commitResetTransaction(transaction);

                // Instanciation du processus standard de compta
                BISession sessionOsiris = PRSession.connectSession(getSession(),
                        CAApplication.DEFAULT_APPLICATION_OSIRIS);
                APIGestionRentesExterne compta = (APIGestionRentesExterne) sessionOsiris
                        .getAPIFor(APIGestionRentesExterne.class);

                // Creation des journaux
                compta.createJournal(getSession(), transaction, getDescription() + " ", "01." + getMoisPaiement());

                compta.createOrdreGroupe(getSession(), transaction, getSession().getLabel("PMT_MENSUEL_OG") + " "
                        + getDescription() + " ", getDateEcheancePaiement(), getNumeroOG(), getIdOrganeExecution());

                initComptaExterne(transaction, false);

                if (!comptaExt.isPeriodeComptableOuverte("01." + getMoisPaiement())) {
                    throw new Exception("Aucune période comptable ouverte pour la période " + getMoisPaiement());
                }

                // On force la création du journal

                initComptaExterne(transaction, true);

                transaction = commitResetTransaction(transaction);
                message = "Comptabilité initialisé avec succès";
                mailLogs.add(message);
                logger.info(message);
                // -------------------------------------------------------------------------//
                mailLogs.add("");
                message = "Démarrage de la boucle de paiement des rentes individuelles";
                mailLogs.add("----------------------------------------------");
                mailLogs.add(message);
                logger.info(message);

                // Préparation des champs pour la boucle du paiement

                REPaiementRentes rente = null;
                REGroupOperationCAUtil grpOP = null;
                REGroupOperationMotifUtil motifVersement = null;
                String nomCache = null;
                Key previousKey = null;
                Key currentKey = null;

                // Initialisation des éléments comptable
                int noSectionIncrement = 0;
                long increment = plageIncrementsOperations.min;
                long incrementSections = plageIncrementsSections.min;
                /*
                 * Début de la boucle de traitement des rentes
                 */
                for (Object o : mgr.getContainer()) {
                    aCommencerDeTraiterLesRentes = true;
                    try {
                        rente = (REPaiementRentes) o;
                        logger.info("Traitement de la rente avec l'id [" + rente.getIdRenteAccordee() + "]");

                        currentKey = getKey(rente);
                        // TODO voir avec RJE pour la gestion des exception
                        String isoLangFromIdTiers = getLangueDuTiersPourIso(rente);
                        // ////////////////////////////////////////////////////////////////////
                        // 1ère écriture...
                        if (previousKey == null) {
                            noSectionIncrement = 0;

                            // Le motif de versement est généré par rapport à la 1ère écriture
                            // trouvée pour chaque groupe d'opération, car trié par groupe
                            motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                    rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                    rente.getCodePrestation(), isoLangFromIdTiers);
                            nomCache = getNomCache(rente);

                            grpOP = new REGroupOperationCAUtil();
                            grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                    rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                    getIdAdrPmt(rente));

                        }

                        // Les clé sont identiques -> toujours dans la même adresse
                        else if (currentKey.equals(previousKey)) {
                            motifVersement.addCodePrest(rente.getCodePrestation());
                        }

                        // Nouvelle adresse de pmt
                        else {
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
                                boolean success = doTraiterGroupeOperation(mapCumulPrstParGenreRentes, transaction,
                                        datePmtEnCours, compta, grpOP, motifVersement, nomCache, noSectionIncrement,
                                        increment, incrementSections);
                                if (!success) {
                                    result = false;
                                }
                                JadeThread.logClear();
                            } catch (Exception e) {
                                message = "Une exception est survenue lors du traitement comptable du groupe d'opértation suivant ["
                                        + grpOP.toString() + "]. Message d'erreur : " + e.toString();
                                logger.error(message, e);
                                mailLogs.add(message);
                            }

                            // On passe au groupe suivant
                            ++incrementSections;
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

                        }
                        ResultatTraitementRente resultat = traiterRente(rente, grpOP, increment, dateDernierPmt,
                                datePmtEnCours);

                        String label = "La rente avec l'id [" + rente.getIdRenteAccordee() + "] ";
                        if (resultat.getResult()) {
                            renteTraiterAvecSucces.add(resultat);
                            logger.info(label + "a été traitée avec succès");
                        } else {
                            renteTraiterAvecErreurs.add(resultat);
                            logger.warn(label + "a été traitée avec erreur");
                        }
                    } catch (Exception e) {
                        message = "Une exception s'est produite lors du traitement de la rente avec l'id ["
                                + rente.getIdRenteAccordee() + "]. Erreur :" + e.toString();
                        logger.error(message, e);
                        renteTraiterAvecErreurs.add(new ResultatTraitementRente(rente.getIdRenteAccordee(), false,
                                message));

                        doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), message);
                    }

                    previousKey = new Key(currentKey);
                    ++increment;
                }
                // FIN DE LA BOUCLE

                /*
                 * Traitement du dernier groupe d'opération
                 */
                doTraiterGroupeOperation(mapCumulPrstParGenreRentes, transaction, datePmtEnCours, compta, grpOP,
                        motifVersement, nomCache, noSectionIncrement, increment, incrementSections);

                mailLogs.add("----------------------------------------------");
                message = "Fin de la boucle de traitement des rentes";
                logger.info(message);
                mailLogs.add(message);
                mailLogs.add("");

                String emailObject;
                if (result && renteTraiterAvecErreurs.isEmpty()) {
                    message = "Aucune rente en erreur, passage du lot [" + lot.getIdLot() + "] en état [VALIDE]";
                    logger.info(message);
                    mailLogs.add(message);

                    emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_SUCCES");
                    lot.retrieve(transaction);
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                    lot.update(transaction);
                } else {
                    message = "Aucune rente en erreur, passage du lot [" + lot.getIdLot() + "] en état [PARTIEL]";
                    logger.info(message);
                    mailLogs.add(message);

                    emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_ERREUR");
                    lot.retrieve(transaction);
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_PARTIEL);
                    lot.update(transaction);
                }
                setEmailObject(emailObject);

                message = "Comptabilisation du lot";
                logger.info(message);
                mailLogs.add(message);

                List<String> listOg = compta.bouclerOG(this, getSession(), transaction);
                if (listOg.size() > 1) {
                    mailLogs.add("Le nombre de transactions est supérieur à la limite pour l'ISO20022, plusieurs ordres groupés ont été créés. ["
                            + Arrays.toString(listOg.toArray()) + "]");
                }

                // Changer l'état des ordre de versement du journal rapide en état versé
                changeOrdreVersementInEtatVerse(compta);
                transaction = commitResetTransaction(transaction);

                // Comptabilisation des journal des retenues /blocagges
                comptabiliserJournalRetenuesBlocages();
                commitResetTransaction(transaction);

                // Préparation de l'OG des retenues/blocages
                preparerOGRetenuesBlocages();
                commitResetTransaction(transaction);

                // Workaround : Vidage du cache, sans quoi, l'execution de l'OG plante. Raison : inconnue !!!!
                GlobazServer.getCurrentSystem().resetCache();

                // Contrôle des montants versés à l'aide du cumul par rubriques
                mailLogs.add("");
                mailLogs.add("Contrôle des cumuls par rubriques");
                mailLogs.add("----------------------------------------------");
                controleCumulParRubrique(mapCumulPrstParGenreRentes);
                mailLogs.add("----------------------------------------------");
                mailLogs.add("");

                validationPmt(getSession(), compta, mapCumulPrstParGenreRentes);

                commitResetTransaction(transaction);
            }
            // Aucune rente à verser, le lot passe en validé
            else {
                message = "Aucune rente à verser";
                mailLogs.add(message);
                logger.info(message);

                lot.retrieve(transaction);
                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                lot.update(transaction);
            }

        } catch (Exception globalException) {
            String message = "Une exception s'est produite lors de l'exécution du paiement des rentes : "
                    + globalException.toString();
            mailLogs.add(message);
            logger.error(message, globalException);
            setEmailObject(message);
        }

        // Création du contenu du mail avec les logs utilisateurs DOIT ETRE AVANT LE MERGE QUI SUIT SINON PAS D'INFO !!
        reporting(mailLogs, renteTraiterAvecErreurs, renteTraiterAvecSucces, nombreDeRentesAVersees,
                aCommencerDeTraiterLesRentes, result);

        // Traitement des listes
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        docInfo.setDocumentTitle("listeRetenues");
        this.mergePDF(docInfo, "listeRetenues", true, 0, false, null);

        return result;
    }

    private void comptabiliserJournalRetenuesBlocages() {
        String message;
        message = "Comptabilisation du journal des retenues/blocages";
        logger.info(message);
        mailLogs.add(message);
        comptaExt.comptabiliser();
    }

    private void preparerOGRetenuesBlocages() {
        String libelleOG = getDescription() + " (suite)";
        String message;
        message = "Préparation de l'OG des retenues/blocages";
        logger.info(message);
        mailLogs.add(message);

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
                CAOrdreGroupe.VERSEMENT, CAOrdreGroupe.NATURE_RENTES_AVS_AI, libelleOG, getIsoGestionnaire(),
                getIsoHighPriority());
    }

    private void controleCumulParRubrique(Map<Integer, RECumulPrstParRubrique> mapCumulPrstParGenreRentes) {
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
            mailLogs.add("Type - Rubrique : " + cppr.getType() + " - " + descrRubrique + " \tmontant ="
                    + cppr.getMontant());
        }
    }

    private boolean doTraiterGroupeOperation(Map<Integer, RECumulPrstParRubrique> mapCumulPrstParGenreRentes,
            BTransaction transaction, String datePmtEnCours, APIGestionRentesExterne compta,
            REGroupOperationCAUtil grpOP, REGroupOperationMotifUtil motifVersement, String nomCache,
            int noSectionIncrement, long increment, long incrementSections) {

        boolean result = false;
        try {
            RECumulPrstParRubrique[] array = grpOP.doTraitementComptable(this, getSession(), transaction, compta,
                    motifVersement.getMotif(getSession()), increment, (increment + 1), incrementSections,
                    datePmtEnCours, nomCache, noSectionIncrement, getDateEcheancePaiement());
            logger.info("Le groupe d'opération [" + grpOP.toString() + "] à été traité avec succès");

            for (RECumulPrstParRubrique element : array) {
                grpOP.cumulParRubrique(mapCumulPrstParGenreRentes, element);
            }
            result = true;
        } catch (Exception e) {
            String message = "Une exception est survenue lors du traitement comptable du groupe d'opértation suivant ["
                    + grpOP.toString() + "]. Message d'erreur : " + e.toString();
            logger.error(message, e);
            mailLogs.add(message);
            /*
             * Au vu de la misère du code en amont, on assure le coup en allant également mettre éles RA en erreurs
             */
            for (String idRA : grpOP.getIdRAEcritures()) {
                String m = message + ". Mise en erreur de la RA avec l'id [" + idRA + "]";
                logger.warn(message);
                mailLogs.add(message);
                doMiseEnErreurRA(getSession(), idRA, m);
            }
        }
        return result;
    }

    /**
     * Surcharge de cette méthode pour pouvoir généré le contenu du mail nous même
     */
    @Override
    public String getSubjectDetail() {
        StringBuilder sb = new StringBuilder();
        for (String log : mailLogs) {
            sb.append(log).append("\n");
        }
        return sb.toString();
    }

    private String getLangueDuTiersPourIso(REPaiementRentes rente) {
        return PRTiersHelper.getIsoLangFromIdTiers(getSession(), rente.getIdTiersAdressePmt());
    }

    private void reporting(List<String> userLogs, List<ResultatTraitementRente> renteTraiterAvecErreurs,
            List<ResultatTraitementRente> renteTraiterAvecSuccees, int nombreDeRentesAVersees,
            boolean aCommencerDeTraiterLesRentes, boolean result) {
        String message = "Nombre de rentes à verser : " + nombreDeRentesAVersees;
        logger.info(message);
        userLogs.add(message);

        message = "Nombre de rentes traitées avec succès : " + renteTraiterAvecSuccees.size();
        logger.info(message);
        userLogs.add(message);

        message = "Nombre de rentes traitées avec erreurs : " + renteTraiterAvecErreurs.size();
        logger.info(message);
        userLogs.add(message);

        int difference = nombreDeRentesAVersees - (renteTraiterAvecSuccees.size() + renteTraiterAvecErreurs.size());
        if (aCommencerDeTraiterLesRentes && difference != 0) {
            message = "Une différence de ["
                    + difference
                    + "] rentes est détectée entre le nombre de rentes à traiter et les rentes traitées avec succès/erreurs!";
            logger.warn(message);
            userLogs.add(message);
        }
        if (!aCommencerDeTraiterLesRentes) {
            message = "Le traitement des rentes à payer n'a pas commencé.";
            logger.info(message);
            userLogs.add(message);
        }

        if (result && renteTraiterAvecErreurs.isEmpty()) {
            message = "Aucune rente en erreur suite à l'exécution du paiement";
            logger.info(message);
            userLogs.add(message);
        } else {
            message = "Des rentes sont en erreurs suite à l'exécution du paiement : ";
            logger.warn(message);
            userLogs.add(message);
            for (ResultatTraitementRente resultat : renteTraiterAvecErreurs) {
                message = "Problème(s) rencontrés lors du traitement de la rente avec l'id ["
                        + resultat.getIdRenteAccordee() + "] : ";
                logger.warn(message);
                userLogs.add(message);

                for (String messageErreur : resultat.getErrors()) {
                    message = " - " + messageErreur;
                    logger.warn(message);
                    userLogs.add(message);
                }
            }
        }
        message = "Fin du process " + new SimpleDateFormat(HH_MM_SS).format(new Date());
        logger.info(message);
        userLogs.add(message);
        userLogs.add("");

        /*
         * Histoire de ramener également les logs perdu dans le MemoryLog du process :'(
         */
        userLogs.add("");
        userLogs.add("Log from memoryLog : ");
        userLogs.add(getMemoryLog().getMessagesInString());
    }

    private boolean isReferenceDePaiementDifferent(Key currentKey, Key previousKey) {
        return !currentKey.isReferenceDePaiementIdentique(previousKey);
    }

    private boolean isAdresseDePaiementDifferent(Key currentKey, Key previousKey) {
        return !currentKey.isAdresseDePaiementIdentique(previousKey);
    }

    private boolean isCompteAnnexeDifferent(Key currentKey, Key previousKey) {
        return !currentKey.isCompteAnnexeIdentique(previousKey);
    }

    private boolean isKeyDifferent(Key currentKey, Key previousKey) {
        return !currentKey.isKeyIdentique(previousKey);
    }

    /**
     * Contrôle des rentes :</br>
     * - pas 2x le même id rentes accordées</br>
     * - chaque rente doit avoir un compte annexe</br>
     * - chaque rente doit avoir une adresse de paiement</br>
     * 
     * @param rentes
     */
    private void controleDesRentes(JAVector rentes) {
        REPaiementRentes rente = null;
        List<String> errors = new ArrayList<String>();
        Set<String> idRentesAccordees = new HashSet<String>();

        for (Object o : rentes) {
            rente = (REPaiementRentes) o;

            // Contrôle que la rente ne soit pas présente à double
            String id = rente.getIdRenteAccordee();
            if (idRentesAccordees.contains(id)) {
                errors.add("La rente avec l'id [" + id + "] est présente deux fois pour le paiement");
            }
            idRentesAccordees.add(id);

            // Contrôle que la rente possède bien un compte annexe
            if (JadeStringUtil.isBlankOrZero(rente.getIdCompteAnnexe())) {
                errors.add("Pas de compte annexe  pour la RA avec l'id [" + id + "] ");
            }
            // Contrôle que la rente possède bien une adresse de paiement
            if (JadeStringUtil.isBlankOrZero(rente.getIdTiersAdressePmt())) {
                errors.add("Pas d'adresse de paiement pour la RA avec l'id [" + id + "] ");
            }
        }
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Des erreurs ont été détectées lors du contrôle des rentes : \n");
            for (String error : errors) {
                sb.append("- " + error).append("\n");
            }
            throw new RETechnicalException(sb.toString());
        }
    }

    private void controlePlusDeRenteEnErreur(BTransaction transaction) throws Exception {
        long nombreDeRentes;
        nombreDeRentes = getNombreRentes(getSession(), transaction, Boolean.TRUE, null, null);
        if (nombreDeRentes > 0) {
            throw new RETechnicalException(
                    "Il y en encore des Rentes Accordées en Erreur, impossible de lancer le pmt.");
        }
    }

    private boolean isReprisePaiementSuiteAUneErreur(BTransaction transaction, RELot lot) throws Exception {
        lot.retrieve(transaction);
        boolean isRepriseSurErreurMajeur = false;
        if (IRELot.CS_ETAT_LOT_ERREUR.equals(lot.getCsEtatLot())) {
            isRepriseSurErreurMajeur = true;
        } else if (IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtatLot())) {
            // MAJ du lot dans l'état ERREUR
            lot.setDescription(getSession().getLabel("PMT_MENS_RENTES") + " " + getMoisPaiement());
            lot.setCsEtatLot(IRELot.CS_ETAT_LOT_ERREUR);
            lot.update(transaction);
        } else {
            throw new Exception("Incohérance dans les données. L'Etat du lot est incorrect");
        }
        return isRepriseSurErreurMajeur;
    }

    /**
     * Récupère le lot ouvert préalablement préparé lors du traitement de préparation pour le paiement principal
     * 
     * @param transaction
     * @return Le lot pour le paiement
     */
    private RELot recupererLotPaiement(BTransaction transaction) {
        RELotManager mgrl = new RELotManager();
        RELot lot = null;
        try {
            mgrl.setSession(getSession());
            mgrl.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgrl.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            mgrl.setForDateEnvoiInMMxAAAA(getMoisPaiement());
            mgrl.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            String message = "";
            logger.error(message, e);
            throw new RETechnicalException(message, e);
        }

        if (mgrl.size() > 1) {
            throw new RETechnicalException(getSession().getLabel("ERREUR_PLS_LOTS_OUVERTS_TROUVES"));
        } else if (mgrl.size() == 0) {
            throw new RETechnicalException(getSession().getLabel("ERREUR_PREAPARATION_PMTMENSUEL_PAS_ENCORE_EFFECTUEE"));
        } else {
            lot = (RELot) mgrl.getFirstEntity();
        }
        // FIXME WTF here ???
        // lot.retrieve(transaction);
        if (lot.isNew()) {
            throw new RETechnicalException("Impossible de récupérer le lot avec l'id [" + lot.getIdLot()
                    + "] pour éxécuter le paiement principal");
        }
        return lot;

    }

    /**
     * Calcul la date du paiement en cours. Lance une Runtime en cas de problème
     * 
     * @param dateDernierPmt La date du dernier paiement
     * @return La date du paiement en cours
     */
    private String calculerDatePaiementEnCours(String dateDernierPmt) {
        try {
            JADate jd = new JADate("01." + dateDernierPmt);
            JACalendar cal = new JACalendarGregorian();
            jd = cal.addMonths(jd, 1);
            String datePmtEnCours = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(jd.toStrAMJ());
            return datePmtEnCours;

        } catch (JAException e) {
            String message = "Une exception est survenue lors du calcul de la date du paiement en cours : ";
            message += e.toString();
            logger.error(message, e);
            throw new RETechnicalException(message, e);
        }
    }

    /**
     * Récupère la date du dernier paiement. Lance une Runtime en cas de problème
     * 
     * @return la date du dernier paiement
     */
    private String recupererDateDernierPaiement() {
        String dateDernierPmt = REPmtMensuel.getDateDernierPmt(getSession());
        if (JadeStringUtil.isBlankOrZero(dateDernierPmt)
                || REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPmt)) {
            String message = getSession().getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
            throw new RETechnicalException(message);
        }
        return dateDernierPmt;
    }

    @SuppressWarnings("unchecked")
    private void startListeRecapitualation(BTransaction transaction) throws Exception {
        REListeRecapitulativePaiementPC_RFM listeRecapitulativeePaiementPCRFM = null;
        // On charge l'adapter
        RERecapitulationPaiementAdapter adapter = new RERecapitulationPaiementAdapter(getSession(), getMoisPaiement());
        adapter.chargerParGenrePrestation();
        REListeRecapitulativePaiement listeRecapitulativeePaiement = new REListeRecapitulativePaiement(getSession());
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
    }

    /**
     * Imprime la liste des retenues
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void startListeRetenues(BTransaction transaction) throws Exception {
        REListeRetenuesBlocages listeRetenues = new REListeRetenuesBlocages(getSession());
        listeRetenues.setMois(getMoisPaiement());
        listeRetenues.setSession(getSession());
        listeRetenues.setTransaction(transaction);
        listeRetenues.setEMailAddress(getEMailAddress());
        listeRetenues.setSendCompletionMail(true);
        listeRetenues.setSendMailOnError(true);
        listeRetenues.setMemoryLog(getMemoryLog());
        listeRetenues.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        listeRetenues.executeProcess();

        if (!listeRetenues.isOnError()) {
            for (int i = 0; i < listeRetenues.getAttachedDocuments().size(); i++) {
                getAttachedDocuments().add(listeRetenues.getAttachedDocuments().get(i));
            }
        }

    }

    /**
     * Traite le paiement de la rente. REnvoi
     * 
     * @param rente
     * @param grpOP
     * @param increment
     * @throws Exception
     */
    private ResultatTraitementRente traiterRente(REPaiementRentes rente, REGroupOperationCAUtil grpOP, long increment,
            String dateDernierPmt, String datePmtEnCours) {
        logger.info("traiterRente(...) : Traitement de la rente avec l'id [" + rente.getIdRenteAccordee() + "]");
        boolean result = false;
        List<String> errors = new ArrayList<String>();

        try {
            APIRubrique rubriqueComptable = AREModuleComptable.getRubriqueWithInit(initSessionOsiris(),
                    rente.getCodePrestation(), rente.getSousTypeCodePrestation(),
                    AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

            if (rubriqueComptable == null) {
                String message = "Aucune rubrique comptable trouvée pour la rente accordée avec l'id : ["
                        + rente.getIdRenteAccordee() + "], codePrestation : [" + rente.getCodePrestation()
                        + "], sousTypeGenrePrestation : [" + rente.getSousTypeCodePrestation() + "]";
                errors.add(message);
                doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), message);

            } else {
                String libelle = rente.getNomTBE() + " " + rente.getPrenomTBE() + " " + rente.getCodePrestation();
                grpOP.traiterEcriture(getSession(), rente, libelle, increment, rubriqueComptable.getIdRubrique(),
                        dateDernierPmt, datePmtEnCours, false);
                result = true;
            }

        } catch (Exception e) {
            String message = "Une exception s'est produite lors du traitement de la rente "
                    + getDetailRentePourLog(rente) + ". Exception : " + e.toString();
            logger.error(message, e);
            errors.add(message);
            doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), message);
        }
        return new ResultatTraitementRente(rente.getIdRenteAccordee(), result, errors);

    }

    /**
     * Retourne le détails d'une rente pour les logs
     * 
     * @param rente
     * @return le détails d'une rente pour les logs
     */
    private String getDetailRentePourLog(REPaiementRentes rente) {
        return rente.getIdRenteAccordee();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do here
    }

    /**
     * Cette méthode permet de mettre à jour les ordres de versement du journal "rapide" en état versé
     * 
     * @param compta
     * @throws Exception
     */
    private void changeOrdreVersementInEtatVerse(APIGestionRentesExterne compta) throws Exception {
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
    protected void _validate() throws Exception {

        logger.info("Validation du processus avant démarrage");

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
        mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (mgr.size() != 1) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_LOT_MENS_MOIS_PREC_PAS_VALIDE"));
        }

        // On contrôle que le mois courant n'ai pas encore été traité...
        mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
        mgr.setForCsEtat(null);
        mgr.setForCsEtatIn(IRELot.CS_ETAT_LOT_OUVERT + ", " + IRELot.CS_ETAT_LOT_ERREUR);
        mgr.setForDateEnvoiInMMxAAAA(getMoisPaiement());
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);

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
}
