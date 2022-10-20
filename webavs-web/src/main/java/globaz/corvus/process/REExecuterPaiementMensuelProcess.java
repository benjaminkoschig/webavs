/*
 * Cr�� le 28 ao�t 07
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
import globaz.corvus.module.compta.REModuleComptableFactory;
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
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
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
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilation;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilationSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.constantes.CAProperties;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
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
 * <dt>Pr� requis</dt>
 * <dd>Impression de la liste de v�rification. Cette liste va cr��er le lot dans l'�tat ouvert pour le mois du paiement.
 * </dd>
 * <dt>Etape 1</dt>
 * <dd>Contr�le que le lot pour le mois de paiement existe et maj de son �tat � ERREUR. Tant que le traitement n'est pas
 * termin�, le lot reste dans l'�tat 'EN ERREUR' Contr�le que la p�riode comptable existe en compta g�n�rale</dd>
 * <dt>Etape 2</dt>
 * <dd>Mis � jours des rentes accord�es �chues (maj du flag isBloquee) et maj du flag isAttenteMajBlocage � '1' Mis �
 * jour des retenues (maj du flag is retenue) maj du flag isAttenteMajRetenue � '1' Impression de la liste des retenues</dd>
 * <dt>Etape 3</dt>
 * <dd>Execution du paiement Traitement s�par� des RA dites standards, des RA avec flag isAttenteMaj... Les RA avec flag
 * isAttenteMaj... seront trait�es dans un journal s�par� (journal des retenues/blocages). Pour chaque RA avec
 * retenues/blocage, maj du flag isAttenteMaj... � '0' apr�s traitement.</dd>
 * <dt>Etape 4</dt>
 * <dd>Contr�le final</dd>
 * <dt>Etape 5</dt>
 * <dd>Mise � jour du lot dans l'�tat VALIDE ou PARTIEL si des erreurs 'mineurs' ont �t� d�tect�es</dd>
 * </dl>
 * <p>
 * Toutes les RA n'ayant pas pu �tre trait�es, sont marqu�es avec la flag isErreur = '1'. Un process sp�cifique permet
 * de re-traiter ces RA erronn�es uniquement.
 * </p>
 * <p>
 * <b>Reprise sur erreur MAJEURE</b> <br/>
 * Cette �tape est identifi�e par l'�tat du lot � 'ERREUR'. Ce cas arrive lorsque le traitement du grand paiement n'a
 * pas pu s'ex�cuter enti�rerement. C'est � dire, l'�tape no 5 n'a pas pu s'effectuer.
 * </p>
 * <dl>
 * <dt>Etape 1</dt>
 * <dd>Annuler toutes les �critures comptables du journal contenant les RA dites standards, ainsi que le l'OG
 * correspondant. !!! Attention : Ne pas supprimer le journal des retenues/blocages, car les �critures d�j�
 * comptabilis�es ne seront pas reg�n�r�es.</dd>
 * <dt>Etape 2</dt>
 * <dd>Remettre toutes les RA avec le flag isErreur � '0'</dd>
 * <dt>Etape 3</dt>
 * <dd>Relancer le paiement. Le lot se trouvant dans l'�tat 'EN_ERREUR', les maj des retenues/blocages ne vont pas
 * s'effectuer. Dans cette �tapes, toutes les RA dites 'standard' seront � nouveau trait�es, et seul les RA avec
 * retenues/blocage ayant le flag 'isAttenteMaj...' � '1' seront retrait�es. Celle � '0' ne seront pas recomptabilis�e,
 * d'ou l'importance de ne pas supprimer le jounal des retenues/blocage en CA.</dd>
 * <dt>Etape 4</dt>
 * <dd>Supprimer la r�capitulation du paiement pour les PC/RFM avec le script suivant :
 * <code>DELETE FROM SCHEMA.PCRFMREC WHERE MOIS = <i>[AAAAMM]</i></code></dd>
 * <dt>Point de non retour</dt>
 * <dd>Exception !!! Si le point de non retour n'a PAS �t� atteint (indiqu� dans le mail si atteint), you're a lucky
 * guy!!! Aucune �criture comptable n'a �t� g�n�r�r�es. Dans ce cas, remettre le lot dans l'�tat ouvert, contr�ler que
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

            String message = "D�marrage du process " + new SimpleDateFormat(HH_MM_SS).format(new Date());
            mailLogs.add(message);
            logger.info(message);

            // Transaction globale, utilis�e pour la requ�te principale.
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
            message = "Id du lot utilis� pour le paiement mensuel : " + lot.getIdLot();
            mailLogs.add(message);
            logger.info(message);

            boolean isRepriseSurErreurMajeur = isReprisePaiementSuiteAUneErreur(transaction, lot);
            transaction = commitResetTransaction(transaction);

            if (isRepriseSurErreurMajeur) {
                message = "Ex�cution du paiement suite � des erreurs : " + lot.getIdLot();
                mailLogs.add(message);
                logger.info(message);
            } else {
                message = "Ex�cution du paiement standard : " + lot.getIdLot();
                mailLogs.add(message);
                logger.info(message);

                // Mise � jours du flag isPrestationBloquee des RA
                message = "Ex�cution de la mise � jour des rentes �chues";
                mailLogs.add(message);
                logger.info(message);
                doMiseAJourRentesEchuesProcess(transaction);
                transaction = commitResetTransaction(transaction);

                // Mise � jours du flag isRetenues des RA
                message = "Ex�cution de la mise � jour des retenues";
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
            message = "Impression de la liste de r�capitulation";
            mailLogs.add(message);
            logger.info(message);
            startListeRecapitualation(transaction);
            transaction = commitResetTransaction(transaction);

            // Impression de la liste de r�capitulation des PC / RFM
            message = "Impression de la liste de r�capitulation des PC / RFM";
            mailLogs.add(message);
            logger.info(message);
            remplirRecapitulationPcRfm();
            transaction = commitResetTransaction(transaction);

            // Contr�le qu'il n'y ai plus de RA en erreur.
            message = "Contr�le qu'il n'y ai plus de RA en erreur";
            mailLogs.add(message);
            logger.info(message);
            controlePlusDeRenteEnErreur(transaction);
            transaction = commitResetTransaction(transaction);

            // R�cup�ration des rentes � verser...
            REPaiementRentesManager mgr = new REPaiementRentesManager();
            mgr.setSession(getSession());
            mgr.setForDatePaiement(getMoisPaiement());
            mgr.setForIsEnErreur(Boolean.FALSE);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            nombreDeRentesAVersees = mgr.getContainer().size();

            if (nombreDeRentesAVersees > 0) {
                message = nombreDeRentesAVersees + " rentes � verser";
                mailLogs.add(message);
                logger.info(message);

                // Contr�le qu'il n'y ai plus de RA en erreur.
                message = "Contr�le de la taille et du nombre des OG";
                mailLogs.add(message);
                logger.info(message);
                controleMaxOG(nombreDeRentesAVersees);

                // Contr�le des rentes avant de boucler pour le paiement
                message = "Contr�le des rentes avant l'initialisation de la compta ";
                mailLogs.add(message);
                logger.info(message);
                controleDesRentes(mgr.getContainer());

                // ---------------------------------------------------------------
                // Initialisation de la comptabilit�
                // ---------------------------------------------------------------
                message = "Initialisation de la comptabilit�";
                mailLogs.add(message);
                logger.info(message);

                // reservation de la plage d'increment pour les �critures en CA
                // FIXME --> joyeux bordel la r�servation de ces incr�ments de merde
                PlageIncrement plageIncrementsOperations = reserverPlageIncrementsOperationsCA(getSession(),
                        transaction, nombreDeRentesAVersees);
                PlageIncrement plageIncrementsSections = reserverPlageIncrementsSectionsCA(getSession(),
                        nombreDeRentesAVersees);
                transaction = commitResetTransaction(transaction);

                message = "Plage d'incr�ment r�serv�e pour les op�rations : min/max = " + plageIncrementsOperations.min
                        + "/" + plageIncrementsOperations.max;
                mailLogs.add(message);
                logger.info(message);

                message = "Plage d'incr�ment r�serv�e pour les sections : min/max = " + plageIncrementsSections.min
                        + "/" + plageIncrementsSections.max;
                mailLogs.add(message);
                logger.info(message);

                // R�cup�ration du num�ro de secteur des rentes
                String noSecteurRente = getNoSecteurRente(getSession());
                message = "Num�ro de secteur utilis� pour les rentes [" + noSecteurRente + "] ";
                mailLogs.add(message);
                logger.info(message);

                // R�cup�ration du compte courant
                String idCompteCourant = getIdCompteCourant(getSession(), transaction,
                        APISection.ID_TYPE_SECTION_RENTE_AVS_AI, noSecteurRente);
                message = "Id du compte courant [" + idCompteCourant + "] ";
                mailLogs.add(message);
                logger.info(message);

                // R�cup�ration de la rubrique pour les OVs
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

                String libelleOG = getDescription();
                if (isIso20022(getIdOrganeExecution(), getSession())) {
                    libelleOG = "ISO20022 - " + libelleOG + " ";
                } else {
                    libelleOG = getSession().getLabel("PMT_MENSUEL_OG") + " " + libelleOG + " ";
                }

                compta.createOrdreGroupe(getSession(), transaction, libelleOG, getDateEcheancePaiement(),
                        getNumeroOG(), getIdOrganeExecution());

                initComptaExterne(transaction, false);

                if (!comptaExt.isPeriodeComptableOuverte("01." + getMoisPaiement())) {
                    throw new Exception("Aucune p�riode comptable ouverte pour la p�riode " + getMoisPaiement());
                }

                // On force la cr�ation du journal

                initComptaExterne(transaction, true);

                transaction = commitResetTransaction(transaction);
                message = "Comptabilit� initialis� avec succ�s";
                mailLogs.add(message);
                logger.info(message);
                // -------------------------------------------------------------------------//
                mailLogs.add("");
                message = "D�marrage de la boucle de paiement des rentes individuelles";
                mailLogs.add("----------------------------------------------");
                mailLogs.add(message);
                logger.info(message);

                // Pr�paration des champs pour la boucle du paiement

                REPaiementRentes rente = null;
                REGroupOperationCAUtil grpOP = null;
                REGroupOperationMotifUtil motifVersement = null;
                String nomCache = null;
                Key previousKey = null;
                Key currentKey = null;

                // Initialisation des �l�ments comptable
                int noSectionIncrement = 0;
                long increment = plageIncrementsOperations.min;
                long incrementSections = plageIncrementsSections.min;
                /*
                 * D�but de la boucle de traitement des rentes
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
                        // 1�re �criture...
                        if (previousKey == null) {
                            noSectionIncrement = 0;

                            // Le motif de versement est g�n�r� par rapport � la 1�re �criture
                            // trouv�e pour chaque groupe d'op�ration, car tri� par groupe
                            motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                    rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                    rente.getCodePrestation(), isoLangFromIdTiers);
                            nomCache = getNomCache(rente);

                            grpOP = new REGroupOperationCAUtil();
                            grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                    rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                    getIdAdrPmt(rente), rente.getIdReferenceQR());

                        }

                        // Les cl� sont identiques -> toujours dans la m�me adresse
                        else if (currentKey.equals(previousKey)) {
                            motifVersement.addCodePrest(rente.getCodePrestation());
                        }

                        // Nouvelle adresse de pmt
                        else {
                            try {
                                // Si l'on est toujours dans le m�me compte annexe, il faut utiliser un nouveau no de
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
                                message = "Une exception est survenue lors du traitement comptable du groupe d'op�rtation suivant ["
                                        + grpOP.toString() + "]. Message d'erreur : " + e.toString();
                                logger.error(message, e);
                                mailLogs.add(message);
                            }

                            // On passe au groupe suivant
                            ++incrementSections;
                            ++increment;
                            ++increment;

                            // R�initialisation du nouveau groupe
                            grpOP = new REGroupOperationCAUtil();
                            grpOP.initOperation(getSession(), rente.getIdCompteAnnexe(), idCompteCourant,
                                    rente.getIdTiersBeneficiaire(), idRubriqueOV, rente.getIdTiersAdressePmt(),
                                    getIdAdrPmt(rente), rente.getIdReferenceQR());

                            // Le motif de versement est g�n�r� par rapport � la 1�re
                            // �criture trouv�e pour
                            // chaque groupe d'op�ration, car tri� par groupe
                            motifVersement = new REGroupOperationMotifUtil(rente.getNssTBE(), getMoisPaiement(),
                                    rente.getNomTBE(), rente.getPrenomTBE(), rente.getReferencePmt(),
                                    rente.getCodePrestation(), isoLangFromIdTiers);

                            nomCache = getNomCache(rente);

                        }
                        ResultatTraitementRente resultat = traiterRente(rente, grpOP, increment, dateDernierPmt,
                                datePmtEnCours);
                        increment = resultat.getIncrement();

                        String label = "La rente avec l'id [" + rente.getIdRenteAccordee() + "] ";
                        if (resultat.getResult()) {
                            renteTraiterAvecSucces.add(resultat);
                            logger.info(label + "a �t� trait�e avec succ�s");
                        } else {
                            renteTraiterAvecErreurs.add(resultat);
                            logger.warn(label + "a �t� trait�e avec erreur");
                        }
                    } catch (Exception e) {
                        message = "Une exception s'est produite lors du traitement de la rente avec l'id ["
                                + rente.getIdRenteAccordee() + "]. Erreur :" + e.toString();
                        logger.error(message, e);
                        renteTraiterAvecErreurs.add(new ResultatTraitementRente(rente.getIdRenteAccordee(), false,
                                message, increment));

                        doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), message);
                    }

                    previousKey = new Key(currentKey);
                    ++increment;
                }
                // FIN DE LA BOUCLE

                /*
                 * Traitement du dernier groupe d'op�ration
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
                    message = "Aucune rente en erreur, passage du lot [" + lot.getIdLot() + "] en �tat [VALIDE]";
                    logger.info(message);
                    mailLogs.add(message);

                    emailObject = getSession().getLabel("EMAIL_OBJECT_PMT_MENS_RENTES_SUCCES");
                    lot.retrieve(transaction);
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                    lot.update(transaction);
                } else {
                    message = "Aucune rente en erreur, passage du lot [" + lot.getIdLot() + "] en �tat [PARTIEL]";
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
                    mailLogs.add("Le nombre de transactions est sup�rieur � la limite pour l'ISO20022, plusieurs ordres group�s ont �t� cr��s. ["
                            + Arrays.toString(listOg.toArray()) + "]");
                }

                // Changer l'�tat des ordre de versement du journal rapide en �tat vers�
                changeOrdreVersementInEtatVerse(compta);
                transaction = commitResetTransaction(transaction);

                // Comptabilisation des journal des retenues /blocagges
                comptabiliserJournalRetenuesBlocages();
                commitResetTransaction(transaction);

                // Pr�paration de l'OG des retenues/blocages
                preparerOGRetenuesBlocages();
                commitResetTransaction(transaction);

                // Workaround : Vidage du cache, sans quoi, l'execution de l'OG plante. Raison : inconnue !!!!
                GlobazServer.getCurrentSystem().resetCache();

                // Contr�le des montants vers�s � l'aide du cumul par rubriques
                mailLogs.add("");
                mailLogs.add("Contr�le des cumuls par rubriques");
                mailLogs.add("----------------------------------------------");
                controleCumulParRubrique(mapCumulPrstParGenreRentes);
                mailLogs.add("----------------------------------------------");
                mailLogs.add("");

                validationPmt(getSession(), compta, mapCumulPrstParGenreRentes);

                commitResetTransaction(transaction);
            }
            // Aucune rente � verser, le lot passe en valid�
            else {
                message = "Aucune rente � verser";
                mailLogs.add(message);
                logger.info(message);

                lot.retrieve(transaction);
                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
                lot.update(transaction);
            }

        } catch (Exception globalException) {
            String message = "Une exception s'est produite lors de l'ex�cution du paiement des rentes : "
                    + globalException.toString();
            mailLogs.add(message);
            logger.error(message, globalException);
            setEmailObject(message);
        }

        // Cr�ation du contenu du mail avec les logs utilisateurs DOIT ETRE AVANT LE MERGE QUI SUIT SINON PAS D'INFO !!
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

    private void controleMaxOG(int nombreDeRentesAVersees) {
        try {
            int maxOVforThisOG = Integer.parseInt(CAProperties.ISO_SEPA_MAX_OVPAROG.getValue());
            int numberOG = Integer.parseInt(CAProperties.ISO_SEPA_MAX_MULTIOG.getValue());
            Long totMax = (long) (numberOG * maxOVforThisOG);
            if (nombreDeRentesAVersees > totMax) {
                throw new RETechnicalException(
                        "Impossible de traiter les rentes. Le nombre de rente d�passe le nombre d'OG max [" + numberOG
                                + "] de chacun " + maxOVforThisOG + " OV d�fini dans les propri�t�s");
            }
        } catch (NumberFormatException e) {
            throw new RETechnicalException(
                    "Impossible de d�terminer le nombre maximum d'OV selon les propri�t�s de l'ISO");
        } catch (PropertiesException e) {
            throw new RETechnicalException("Impossible de trouver les propri�t�s de l'ISO");
        }

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
        message = "Pr�paration de l'OG des retenues/blocages";
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
            logger.info("Le groupe d'op�ration [" + grpOP.toString() + "] � �t� trait� avec succ�s");

            for (RECumulPrstParRubrique element : array) {
                grpOP.cumulParRubrique(mapCumulPrstParGenreRentes, element);
            }
            result = true;
        } catch (Exception e) {
            String message = "Une exception est survenue lors du traitement comptable du groupe d'op�rtation suivant ["
                    + grpOP.toString() + "]. Message d'erreur : " + e.toString();
            logger.error(message, e);
            mailLogs.add(message);
            /*
             * Au vu de la mis�re du code en amont, on assure le coup en allant �galement mettre �les RA en erreurs
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
     * Surcharge de cette m�thode pour pouvoir g�n�r� le contenu du mail nous m�me
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
        String message = "Nombre de rentes � verser : " + nombreDeRentesAVersees;
        logger.info(message);
        userLogs.add(message);

        message = "Nombre de rentes trait�es avec succ�s : " + renteTraiterAvecSuccees.size();
        logger.info(message);
        userLogs.add(message);

        message = "Nombre de rentes trait�es avec erreurs : " + renteTraiterAvecErreurs.size();
        logger.info(message);
        userLogs.add(message);

        int difference = nombreDeRentesAVersees - (renteTraiterAvecSuccees.size() + renteTraiterAvecErreurs.size());
        if (aCommencerDeTraiterLesRentes && difference != 0) {
            message = "Une diff�rence de ["
                    + difference
                    + "] rentes est d�tect�e entre le nombre de rentes � traiter et les rentes trait�es avec succ�s/erreurs!";
            logger.warn(message);
            userLogs.add(message);
        }
        if (!aCommencerDeTraiterLesRentes) {
            message = "Le traitement des rentes � payer n'a pas commenc�.";
            logger.info(message);
            userLogs.add(message);
        }

        if (result && renteTraiterAvecErreurs.isEmpty()) {
            message = "Aucune rente en erreur suite � l'ex�cution du paiement";
            logger.info(message);
            userLogs.add(message);
        } else {
            message = "Des rentes sont en erreurs suite � l'ex�cution du paiement : ";
            logger.warn(message);
            userLogs.add(message);
            for (ResultatTraitementRente resultat : renteTraiterAvecErreurs) {
                message = "Probl�me(s) rencontr�s lors du traitement de la rente avec l'id ["
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
         * Histoire de ramener �galement les logs perdu dans le MemoryLog du process :'(
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
     * Contr�le des rentes :</br>
     * - pas 2x le m�me id rentes accord�es</br>
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

            // Contr�le que la rente ne soit pas pr�sente � double
            String id = rente.getIdRenteAccordee();
            if (idRentesAccordees.contains(id)) {
                errors.add("La rente avec l'id [" + id + "] est pr�sente deux fois pour le paiement");
            }
            idRentesAccordees.add(id);

            // Contr�le que la rente poss�de bien un compte annexe
            if (JadeStringUtil.isBlankOrZero(rente.getIdCompteAnnexe())) {
                errors.add("Pas de compte annexe  pour la RA avec l'id [" + id + "] ");
            }
            // Contr�le que la rente poss�de bien une adresse de paiement
            if (JadeStringUtil.isBlankOrZero(rente.getIdTiersAdressePmt())) {
                errors.add("Pas d'adresse de paiement pour la RA avec l'id [" + id + "] ");
            }
        }
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Des erreurs ont �t� d�tect�es lors du contr�le des rentes : \n");
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
                    "Il y en encore des Rentes Accord�es en Erreur, impossible de lancer le pmt.");
        }
    }

    private boolean isReprisePaiementSuiteAUneErreur(BTransaction transaction, RELot lot) throws Exception {
        lot.retrieve(transaction);
        boolean isRepriseSurErreurMajeur = false;
        if (IRELot.CS_ETAT_LOT_ERREUR.equals(lot.getCsEtatLot())) {
            isRepriseSurErreurMajeur = true;
        } else if (IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtatLot())) {
            // MAJ du lot dans l'�tat ERREUR
            lot.setDescription(getSession().getLabel("PMT_MENS_RENTES") + " " + getMoisPaiement());
            lot.setCsEtatLot(IRELot.CS_ETAT_LOT_ERREUR);
            lot.update(transaction);
        } else {
            throw new Exception("Incoh�rance dans les donn�es. L'Etat du lot est incorrect");
        }
        return isRepriseSurErreurMajeur;
    }

    /**
     * R�cup�re le lot ouvert pr�alablement pr�par� lors du traitement de pr�paration pour le paiement principal
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
            throw new RETechnicalException("Impossible de r�cup�rer le lot avec l'id [" + lot.getIdLot()
                    + "] pour �x�cuter le paiement principal");
        }
        return lot;

    }

    /**
     * Calcul la date du paiement en cours. Lance une Runtime en cas de probl�me
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
     * R�cup�re la date du dernier paiement. Lance une Runtime en cas de probl�me
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
        long inc = increment;

        try {
            APIRubrique rubriqueComptable = AREModuleComptable.getRubriqueWithInit(initSessionOsiris(),
                    rente.getCodePrestation(), rente.getSousTypeCodePrestation(),
                    AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

            if (rubriqueComptable == null) {
                String message = "Aucune rubrique comptable trouv�e pour la rente accord�e avec l'id : ["
                        + rente.getIdRenteAccordee() + "], codePrestation : [" + rente.getCodePrestation()
                        + "], sousTypeGenrePrestation : [" + rente.getSousTypeCodePrestation() + "]";
                errors.add(message);
                doMiseEnErreurRA(getSession(), rente.getIdRenteAccordee(), message);

            } else {
                // test des montants ventil�s uniquement sur les PC_AI et PC_AVS pour limiter l'acc�s de la recherche �
                // la base
                // � supprimer si montant ventil� sur d'autres rubriques
                if (rubriqueComptable.equals(REModuleComptableFactory.getInstance().PC_AI)
                        || rubriqueComptable.equals(REModuleComptableFactory.getInstance().PC_AVS)) {

                    // mise � jour de l'incr�ment par rapport au nombre de montant ventil�
                    inc = hasVentilation(rente, grpOP, increment, dateDernierPmt, datePmtEnCours, rubriqueComptable);
                }
                String libelle = rente.getNomTBE() + " " + rente.getPrenomTBE() + " " + rente.getCodePrestation();
                grpOP.traiterEcriture(getSession(), rente, libelle, inc, rubriqueComptable.getIdRubrique(),
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
        return new ResultatTraitementRente(rente.getIdRenteAccordee(), result, errors, inc);

    }

    /**
     * 
     * Ajoute une ecriture pour chaque montant ventil�
     * 
     * @param rente
     * @param grpOP
     * @param inc
     * @param dateDernierPmt
     * @param datePmtEnCours
     * @param rubriqueComptable
     * @return
     * @throws Exception
     */
    private long hasVentilation(REPaiementRentes rente, REGroupOperationCAUtil grpOP, long inc, String dateDernierPmt,
            String datePmtEnCours, APIRubrique rubriqueComptable) throws Exception {
        long increment = inc;
        JadeAbstractModel[] ventilations = getVentilations(rente.getIdRenteAccordee());
        if (ventilations.length != 0) {
            Montant montant = Montant.valueOf(rente.getMontant());
            for (JadeAbstractModel model : ventilations) {
                SimpleVentilation ventilation = (SimpleVentilation) model;

                REPaiementRentes renteVentile = (REPaiementRentes) BeanUtilsBean.getInstance().cloneBean(rente);

                Montant montantVentilation = Montant.valueOf(ventilation.getMontantVentile());

                // A modifier si d'autres type de ventilation que les parts cantonales
                APIRubrique rubriqueComptable2;
                if (rubriqueComptable.equals(REModuleComptableFactory.getInstance().PC_AI)) {
                    rubriqueComptable2 = REModuleComptableFactory.getInstance().PC_AI_PART_CANTONALE;
                } else {
                    rubriqueComptable2 = REModuleComptableFactory.getInstance().PC_AVS_PART_CANTONALE;
                }

                renteVentile.setMontant(ventilation.getMontantVentile());
                String libelle = renteVentile.getNomTBE() + " " + renteVentile.getPrenomTBE() + " "
                        + renteVentile.getCodePrestation();
                grpOP.traiterEcriture(getSession(), renteVentile, libelle, increment,
                        rubriqueComptable2.getIdRubrique(), dateDernierPmt, datePmtEnCours, false);
                increment++;
                montant = montant.substract(montantVentilation);
            }
            // Mets � jour le montant restant
            rente.setMontant(montant.getValue());
        }
        return increment;
    }

    private JadeAbstractModel[] getVentilations(String idRa) throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleVentilationSearch ventilationSearch = new SimpleVentilationSearch();
        ventilationSearch.setForIdPrestationAccordee(idRa);
        ventilationSearch = CorvusServiceLocator.getSimpleVentilationService().search(ventilationSearch);
        return ventilationSearch.getSearchResults();
    }

    /**
     * Retourne le d�tails d'une rente pour les logs
     * 
     * @param rente
     * @return le d�tails d'une rente pour les logs
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
     * Cette m�thode permet de mettre � jour les ordres de versement du journal "rapide" en �tat vers�
     * 
     * @param compta
     * @throws Exception
     */
    private void changeOrdreVersementInEtatVerse(APIGestionRentesExterne compta) throws Exception {
        compta.updateOperationOrdreVersementInEtatVerse(getSession());
    }

    private void doMiseAJourRentesEchuesProcess(BTransaction transaction) throws Exception {
        // Ce traitement ne met a jours que les rentes devenant �chues ce mois.
        REMiseAJourRentesEchuesProcess miseAJourRentesEchuesProcess = new REMiseAJourRentesEchuesProcess(getSession());
        miseAJourRentesEchuesProcess.setTransaction(null);
        miseAJourRentesEchuesProcess.setMemoryLog(getMemoryLog());
        miseAJourRentesEchuesProcess.doTraitement(transaction);

        // Les rentes d�j� �chues (bloqu�es) ne sont pas impact�es par le traitement ci-dessus.
        // Il faut donc les parcourir et mette � jours le flag isAttenteMajBlocage.
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
     * Lance le process de mise � jour des retenues
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
     * Remplit la liste r�capitulative des rentes PC RFM
     * 
     * @throws Exception
     */
    private void remplirRecapitulationPcRfm() throws Exception {
        RecapitulationPcRfmService recapService = PrestationCommonServiceLocator.getRecapitulationPcRfmService();
        RecapitulationPcRfm recapDuMois = recapService.findInfoRecapByDate(getMoisPaiement());

        // s'il n'y a pas encore de r�cap pour le mois, on la cr�e
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

        logger.info("Validation du processus avant d�marrage");

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

        // On contr�le que le mois pr�c�dent ait bien �t� valid�...
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

        // On contr�le que le mois courant n'ai pas encore �t� trait�...
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

        // v�rifier que tous les lots de d�cisions soient valid�s.
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

        // On tente d'acc�der � cette propri�t� d�j� ici pour �viter que �a casse plus loin
        // si elle n'est pas d�clar�e. ELLE EST OBLIGATOIRE
        try {
            CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue();
        } catch (Exception exception) {
            String message = "Une erreur est intervenue lors de l'acc�s � la propri�t� [common."
                    + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getPropertyName()
                    + "]. Cette propri�t� doit obligatoirement �tre d�finie (valeurs possible [true, false]) pour pouvoir ex�cuter le paiement principal."
                    + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getDescription();
            this._addError(getTransaction(), message);
        }
    }
}
