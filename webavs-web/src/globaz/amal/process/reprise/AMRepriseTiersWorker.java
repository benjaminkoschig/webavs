/**
 * 
 */
package globaz.amal.process.reprise;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIHistoriqueContribuableManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import ch.globaz.amal.business.models.reprise.ContribuableReprise;
import ch.globaz.amal.business.models.reprise.ContribuableRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleContribuableReprise;
import ch.globaz.amal.business.models.reprise.SimpleFamilleReprise;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdresseSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Worker pour la reprise tiers nouvelle persistence
 * 
 * @author DHI
 * 
 */
public class AMRepriseTiersWorker implements Runnable {

    private String baseThreadName = "";
    private SimpleFamilleReprise currentFamille;
    private AMRepriseTiersProcess father;
    private transient BSession session;
    private BTransaction transaction;
    private int workerNumber;

    /**
     * Default constructor
     * 
     * @param workerNumber
     *            no du travailleur (indicatif et utilisé pour le context)
     * @param father
     *            papa lanceur
     * @param famille
     *            élément à traiter
     */
    public AMRepriseTiersWorker(int workerNumber, AMRepriseTiersProcess father, SimpleFamilleReprise famille) {
        this.workerNumber = workerNumber;
        this.father = father;
        currentFamille = famille;
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Initialisation du context d'exécution : UUID initialisé avec le worker number
     * 
     * @return Context d'exécution du thread
     * @throws Exception
     *             Erreur lors du setting du thread context
     */
    private JadeContext initContext() throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        ctxtImpl.setUUID("" + workerNumber);
        return ctxtImpl;
    }

    /**
     * Exécution du traitement de reprise pour un contribuable
     * 
     * @param famille
     * @param contribuable
     * @param gestionTiers
     */
    public void repriseContribuable(SimpleFamilleReprise famille, ContribuableReprise contribuable,
            AMGestionTiers gestionTiers) {

        // ---------------------------------------------------------------------------
        // PREPARATION DE LA RECHERCHE DANS TIERS
        // ---------------------------------------------------------------------------
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());

        // Get nom - prenom, datenaissance, noAVS
        String nom = contribuable.getSimpleContribuableInfoReprise().getNomDeFamille().trim();
        String prenom = contribuable.getSimpleContribuableInfoReprise().getPrenom().trim();
        String dateNaissance = contribuable.getSimpleContribuableInfoReprise().getDateNaissanceActuelle();
        String noAVS = contribuable.getSimpleContribuableInfoReprise().getNnss();
        String noContribuable = contribuable.getSimpleContribuableReprise().getZoneCommuneNoContribuableFormate();
        String gender = contribuable.getSimpleContribuableInfoReprise().getCivilite();
        String titre = gender;
        if (gender.indexOf("Monsieur") == 0) {
            gender = "516001";
            titre = "502001";
        } else if (gender.indexOf("Madame") == 0) {
            gender = "516002";
            titre = "502002";
        } else {
            gender = "";
            titre = "";
        }

        // Set personne etendue
        PersonneEtendueComplexModel personneEtendue = new PersonneEtendueComplexModel();
        if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
            personneEtendue.getPersonne().setDateNaissance(dateNaissance);
        }
        if (!JadeStringUtil.isBlankOrZero(noAVS)) {
            personneEtendue.getPersonneEtendue().setNumAvsActuel(noAVS);
        }
        if (!JadeStringUtil.isBlankOrZero(noContribuable)) {
            personneEtendue.getPersonneEtendue().setNumContribuableActuel(noContribuable);
        }
        if (!JadeStringUtil.isBlankOrZero(gender)) {
            personneEtendue.getPersonne().setSexe(gender);
        }
        if (!JadeStringUtil.isBlankOrZero(titre)) {
            personneEtendue.getTiers().setTitreTiers(titre);
        }
        personneEtendue.getTiers().setDesignation1(nom);
        personneEtendue.getTiers().setDesignation2(prenom);
        personneEtendue.getTiers().setPersonnePhysique(true);

        // ---------------------------------------------------------------------------
        // Recherche dans TIERS
        // ---------------------------------------------------------------------------

        // Log
        String traceLog = "CONTRIBUABLE";
        try {
            traceLog += ";" + contribuable.getId();
            traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNumeroContribuableActuelFormate();
            traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNnss();
            traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getDateNaissanceActuelle();
            traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNomDeFamille();
            traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getPrenom();
            traceLog += ";";
            // Search tiers
            PersonneEtendueSearchComplexModel tiersSearch = gestionTiers.findTiers(personneEtendue);
            if ((tiersSearch != null) && (tiersSearch.getSize() == 1)) {
                personneEtendue = (PersonneEtendueComplexModel) tiersSearch.getSearchResults()[0];
                Boolean personneEtendueNeedUpdate = false;
                // Tiers trouvé, mettons-le à jour
                // NSS
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumAvsActuel())) {
                    if (!JadeStringUtil.isBlankOrZero(noAVS)) {
                        personneEtendue.getPersonneEtendue().setNumAvsActuel(noAVS);
                        personneEtendue.setDateModifAvs(dateToday);
                        personneEtendue.setMotifModifAvs(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // No contribuable - toujours à écraser selon PAC 11.05.2012
                // if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumContribuableActuel())) {
                // }
                if (!JadeStringUtil.isBlankOrZero(noContribuable)) {
                    if (!personneEtendue.getPersonneEtendue().getNumContribuableActuel().equals(noContribuable)) {
                        personneEtendue.getPersonneEtendue().setNumContribuableActuel(noContribuable);
                        personneEtendue.setDateModifContribuable(dateToday);
                        personneEtendue
                                .setMotifModifContribuable(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Date de naissance
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
                    if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
                        personneEtendue.getPersonne().setDateNaissance(dateNaissance);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Sexe
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getSexe())) {
                    if (!JadeStringUtil.isBlankOrZero(gender)) {
                        personneEtendue.getPersonne().setSexe(gender);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Titre
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getTitreTiers())) {
                    if (!JadeStringUtil.isBlankOrZero(titre)) {
                        personneEtendue.getTiers().setTitreTiers(titre);
                        personneEtendue.setDateModifTitre(dateToday);
                        personneEtendue.setMotifModifTitre(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }

                if (personneEtendueNeedUpdate) {
                    try {
                        if (!father.getIsSimulation()) {
                            personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                            personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(
                                    personneEtendue);
                        }
                        // Update des fichiers de status
                        father.incrementCountTiersUpdated();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS, traceLog);
                    } catch (Exception ex) {
                        // update des fichiers de status, ne doit pas provoquer une erreur
                        // donc ajout d'un warning au niveau de la reprise
                        // Update des fichiers de status
                        traceLog += "WARNING : " + AMRepriseTiersProcess.stack2string(ex);
                        father.incrementCountTiersUpdated();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS, traceLog);
                    } finally {
                        JadeThread.logClear();
                        if (transaction.hasErrors()) {
                            JadeLogger.error(this, "Error transaction update tiers : " + transaction.getErrors());
                            transaction.clearErrorBuffer();
                        }
                        if (transaction.hasWarnings()) {
                            JadeLogger.warn(this, "Warning transaction update tiers : " + transaction.getWarnings());
                            transaction.clearWarningBuffer();
                        }
                        transaction.commit();
                    }

                } else {
                    // Update des fichiers de status
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS, traceLog);
                    father.incrementCountTiersUpdated();
                }
            } else if ((tiersSearch != null) && (tiersSearch.getSize() > 1)) {
                // multiple tiers avec ces paramètres de recherches
                traceLog += "Multiple tiers trouvé selon nss, no contribuable, date naissance;";
                father.incrementCountTiersError();
                father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
                personneEtendue = null;
            } else if (tiersSearch == null) {
                // pas trouvé, à créer
                try {
                    if (!father.getIsSimulation()) {
                        personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                        personneEtendue.getTiers().setLangue(IConstantes.CS_TIERS_LANGUE_FRANCAIS);
                        personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().create(personneEtendue);
                        if (transaction.hasErrors()) {
                            JadeLogger.error(this, "Error transaction new tiers : " + transaction.getErrors());
                            transaction.clearErrorBuffer();
                        }
                        if (transaction.hasWarnings()) {
                            JadeLogger.warn(this, "Warning transaction new tiers : " + transaction.getWarnings());
                            transaction.clearWarningBuffer();
                        }
                        JadeThread.logClear();
                        transaction.commit();
                    } else {
                        personneEtendue = null;
                    }
                    father.incrementCountTiersNew();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_TIERS, traceLog);
                } catch (Exception ex) {
                    personneEtendue = null;
                    traceLog += AMRepriseTiersProcess.stack2string(ex);
                    father.incrementCountTiersError();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
                    transaction.clearErrorBuffer();
                    transaction.clearWarningBuffer();
                    JadeThread.logClear();
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Exception searching tiers for contribuable - famille id : " + famille.getId()
                    + " - " + ex.toString());
            JadeLogger.error(this, "- No Contribuable : " + noContribuable);
            JadeLogger.error(this, "- NAVS : " + noAVS);
            JadeLogger.error(this, "- Nom : " + nom);
            JadeLogger.error(this, "- Prenom : " + prenom);
            JadeLogger.error(this, "- Date de naissance : " + dateNaissance);
            personneEtendue = null;
            traceLog += AMRepriseTiersProcess.stack2string(ex);
            father.incrementCountTiersError();
            father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
            transaction.clearErrorBuffer();
            transaction.clearWarningBuffer();
            JadeThread.logClear();
        }

        // ---------------------------------------------------------------------------
        // Mise à jour des tables de reprises >> commit dans updateInnerMF
        // ---------------------------------------------------------------------------
        if ((personneEtendue != null) && (personneEtendue.getTiers() != null)
                && !JadeStringUtil.isEmpty(personneEtendue.getTiers().getIdTiers())) {
            if (father.getIsSimulation() == false) {
                try {
                    // Update RP_AMAL_MACONTRI (id_tiers = id_famille)
                    SimpleContribuableReprise contribuableReprise = contribuable.getSimpleContribuableReprise();
                    contribuableReprise.setIdTiers(famille.getIdFamille());
                    contribuableReprise = father.updateSimpleContribuableReprise(contribuableReprise);
                    contribuable.setSimpleContribuableReprise(contribuableReprise);
                    // Update RP_AMAL_MAFAMILL (id_tiers = id_famille)
                    famille.setIdTiers(famille.getIdFamille());
                    famille = father.updateSimpleFamilleReprise(famille);
                    // Update RP_INNER_MF (id_famille - id_tiers)
                    father.updateInnerMF(famille.getIdTiers(), personneEtendue.getTiers().getIdTiers());
                } catch (Exception ex) {
                    traceLog += AMRepriseTiersProcess.stack2string(ex);
                    father.incrementCountTiersError();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
                    transaction.clearErrorBuffer();
                    transaction.clearWarningBuffer();
                    JadeThread.logClear();
                }
            }
            // Update Adresses dans Tiers
            synchronized (father) {
                repriseContribuableAdresseNew(famille, contribuable, gestionTiers, personneEtendue);
            }
            if (transaction.hasErrors()) {
                JadeLogger.error(this, "Error transaction reprise adresse : " + transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            if (transaction.hasWarnings()) {
                JadeLogger.warn(this, "Warning transaction reprise adresse : " + transaction.getWarnings());
                transaction.clearWarningBuffer();
            }
            JadeThread.logClear();
            // Update historique contribuable dans Tiers
            repriseContribuableHistorique(famille, contribuable, gestionTiers, personneEtendue);
            if (transaction.hasErrors()) {
                JadeLogger.error(this, "Error transaction reprise historique : " + transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            if (transaction.hasWarnings()) {
                JadeLogger.warn(this, "Warning transaction reprise historique : " + transaction.getWarnings());
                transaction.clearWarningBuffer();
            }
            JadeThread.logClear();
            // Update rôle
            repriseRole(personneEtendue.getTiers().getIdTiers());
            if (transaction.hasErrors()) {
                JadeLogger.error(this, "Error transaction reprise rôle : " + transaction.getErrors());
                transaction.clearErrorBuffer();
            }
            if (transaction.hasWarnings()) {
                JadeLogger.warn(this, "Warning transaction reprise rôle : " + transaction.getWarnings());
                transaction.clearWarningBuffer();
            }
            JadeThread.logClear();
        } else {
            if (father.getIsSimulation() == false) {
                try {
                    // Update RP_AMAL_MAFAMILL
                    famille.setIdTiers("0");
                    famille = father.updateSimpleFamilleReprise(famille);
                    // Update RP_AMAL_MACONTRI
                    SimpleContribuableReprise contribuableReprise = contribuable.getSimpleContribuableReprise();
                    contribuableReprise.setIdTiers("0");
                    contribuableReprise = father.updateSimpleContribuableReprise(contribuableReprise);
                    contribuable.setSimpleContribuableReprise(contribuableReprise);
                    // Update RP_INNER_MF (PAS BESOIN !, ON A PAS TROUVE ID TIERS CORRECT)
                    // this.father.updateInnerMF(famille.getIdTiers(), "0");
                } catch (Exception ex) {
                    traceLog += AMRepriseTiersProcess.stack2string(ex);
                    father.incrementCountTiersError();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
                    transaction.clearErrorBuffer();
                    transaction.clearWarningBuffer();
                    JadeThread.logClear();
                }
            }
        }

    }

    /**
     * Traitement pour l'enregistrement de l'adresse du contribuable
     * 
     * @param famille
     * @param contribuable
     * @param gestionTiers
     * @param personneEtendue
     */
    private void repriseContribuableAdresse(SimpleFamilleReprise famille, ContribuableReprise contribuable,
            AMGestionTiers gestionTiers, PersonneEtendueComplexModel personneEtendue) {

        // Infos Adresse sous :
        // TODO : TRANSFORMER EN CODE SYSTEM CIVILITE POUR TITRE ADRESSE
        String civilite = contribuable.getSimpleContribuableInfoReprise().getCivilite();
        String nom = personneEtendue.getTiers().getDesignation1();
        String prenom = personneEtendue.getTiers().getDesignation2();
        String nomPrenom = nom + " " + prenom;
        String currentLine1 = nomPrenom;
        String currentLine2 = contribuable.getSimpleContribuableInfoReprise().getLigneAdresse1();
        String currentLine3 = contribuable.getSimpleContribuableInfoReprise().getLigneAdresse2();
        String currentLine4 = contribuable.getSimpleContribuableInfoReprise().getLigneAdresse3();
        String rue = contribuable.getSimpleContribuableInfoReprise().getNomDeRue();
        String noRue = contribuable.getSimpleContribuableInfoReprise().getNumeroDeRue();
        String casePostale = contribuable.getSimpleContribuableInfoReprise().getCasePostale();
        String npa = contribuable.getSimpleContribuableInfoReprise().getNumeroPostal();
        npa += "00";
        String localiteCommune = contribuable.getSimpleContribuableInfoReprise().getCommune();
        String dateDebut = "01.01.2011";

        // décaler les lignes si vides
        if (currentLine2.length() == 0) {
            currentLine2 = currentLine3;
            currentLine3 = "";
        }
        if (currentLine3.length() == 0) {
            currentLine3 = currentLine4;
            currentLine4 = "";
        }
        // Log
        String traceLog = "";
        traceLog += ";" + contribuable.getId();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNumeroContribuableActuelFormate();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNnss();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getDateNaissanceActuelle();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNomDeFamille();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getPrenom();
        traceLog += ";" + currentLine1;
        traceLog += ";" + currentLine2;
        traceLog += ";" + currentLine3;
        traceLog += ";" + currentLine4;
        traceLog += ";" + rue;
        traceLog += ";" + noRue;
        traceLog += ";CP " + casePostale;
        traceLog += ";" + npa;
        traceLog += ";" + localiteCommune;
        traceLog += ";";

        // Préparation des manager pyxis
        TIAvoirAdresse adresse = null;
        TILocaliteManager mgr = new TILocaliteManager();
        mgr.setSession(getSession());
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.setInclureInactif(Boolean.TRUE);
        mgr.setForNumPostal(npa);
        try {
            mgr.find(1);
            if (mgr.size() > 0) {

                TILocalite localite = (TILocalite) mgr.getFirstEntity();

                // Recherche des adresses LAMAL
                TIAvoirAdresseManager adrMgr = new TIAvoirAdresseManager();
                adrMgr.setSession(getSession());
                adrMgr.setForIdTiers(personneEtendue.getTiers().getIdTiers());
                adrMgr.setForIdApplication(AMGestionTiers.CS_DOMAINE_AMAL);
                adrMgr.setForTypeAdresse(AMGestionTiers.CS_TYPE_COURRIER);
                try {
                    adrMgr.find();

                    // Delete every AMAL Adresse >> temporaire ??????
                    // A affiner...
                    // ------------------------------------------------
                    for (int iAdresse = 0; iAdresse < adrMgr.size(); iAdresse++) {
                        TIAvoirAdresse adresseToDelete = (TIAvoirAdresse) adrMgr.get(iAdresse);
                        if (!father.getIsSimulation()) {
                            try {
                                try {
                                    adresseToDelete.delete(transaction);
                                    transaction.commit();
                                } catch (Exception ex) {
                                    traceLog += AMRepriseTiersProcess.stack2string(ex);
                                    JadeLogger.error(this, "Error deleting adresse :" + traceLog);
                                    // ex.printStackTrace();
                                } finally {
                                    JadeThread.logClear();
                                    transaction.clearErrorBuffer();
                                    transaction.clearWarningBuffer();
                                }
                            } catch (Exception ex) {
                                traceLog += AMRepriseTiersProcess.stack2string(ex);
                                JadeLogger.error(this, "Error deleting AMAL adresse" + traceLog);
                                // ex.printStackTrace();
                            }
                        }
                    }
                    // Find again pour création ensuite
                    adrMgr.find();
                    // -------------------------------------------------
                } catch (Exception exGlobal) {
                    exGlobal.printStackTrace();
                }

                if (adrMgr.size() > 0) {
                    adresse = (TIAvoirAdresse) adrMgr.getFirstEntity();
                    traceLog = "ADRESSE_UPDATED;" + traceLog;
                } else {
                    TIAdresse emptyAdresse = new TIAdresse();
                    emptyAdresse.setSession(getSession());
                    emptyAdresse.setIdLocalite(localite.getIdLocalite());
                    if (!JadeStringUtil.isBlank(casePostale)) {
                        emptyAdresse.setCasePostale(casePostale);
                    }
                    emptyAdresse.setRue(rue);
                    emptyAdresse.setNumeroRue(noRue);
                    if (!JadeStringUtil.isBlank(currentLine1)) {
                        emptyAdresse.setLigneAdresse1(currentLine1);
                    }
                    if (!JadeStringUtil.isBlank(currentLine2)) {
                        emptyAdresse.setLigneAdresse2(currentLine2);
                    }
                    if (!JadeStringUtil.isBlank(currentLine3)) {
                        emptyAdresse.setLigneAdresse3(currentLine3);
                    }
                    if (!JadeStringUtil.isBlank(currentLine4)) {
                        emptyAdresse.setLigneAdresse4(currentLine4);
                    }
                    if (!father.getIsSimulation()) {
                        try {
                            emptyAdresse.save(transaction);
                            transaction.commit();
                        } catch (Exception ex) {
                            JadeLogger.error(this,
                                    "Error saving adresse" + traceLog + " - " + AMRepriseTiersProcess.stack2string(ex));
                            // ex.printStackTrace();
                        } finally {
                            transaction.clearErrorBuffer();
                            transaction.clearWarningBuffer();
                            JadeThread.logClear();
                        }
                        emptyAdresse.retrieve(transaction);
                    }
                    adresse = new TIAvoirAdresse();
                    adresse.setIdAdresse(emptyAdresse.getIdAdresse());

                    traceLog = "ADRESSE_NEW;" + traceLog;
                }
                adresse.setSession(getSession());
                adresse.setIdLocalite(localite.getIdLocalite());
                adresse.setWantUpdatePaiement(false);
                adresse.setIdTiers(personneEtendue.getTiers().getIdTiers());
                adresse.setIdApplication(AMGestionTiers.CS_DOMAINE_AMAL);
                adresse.setTypeAdresse(AMGestionTiers.CS_TYPE_COURRIER);
                adresse.setDateDebutRelation(dateDebut);
                if (!JadeStringUtil.isBlank(casePostale)) {
                    adresse.setCasePostale(casePostale);
                }
                adresse.setRue(rue);
                adresse.setNumeroRue(noRue);
                if (!JadeStringUtil.isBlank(currentLine1)) {
                    adresse.setLigneAdresse1(currentLine1);
                }
                if (!JadeStringUtil.isBlank(currentLine2)) {
                    adresse.setLigneAdresse2(currentLine2);
                }
                if (!JadeStringUtil.isBlank(currentLine3)) {
                    adresse.setLigneAdresse3(currentLine3);
                }
                if (!JadeStringUtil.isBlank(currentLine4)) {
                    adresse.setLigneAdresse4(currentLine4);
                }
                if (!father.getIsSimulation()) {
                    adresse.save(transaction);
                    if (transaction.isRollbackOnly()) {
                        traceLog += " - isRollbackOnly : " + transaction.isRollbackOnly();
                        traceLog += " - errorMessages : " + transaction.getErrors();
                        traceLog += " - warningMessages : " + transaction.getWarnings();
                    }
                    transaction.commit();
                }
                if (traceLog.indexOf("_NEW") >= 0) {
                    father.incrementCountAdresseNew();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES, traceLog);
                } else {
                    father.incrementCountAdresseUpdated();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_ADRESSES, traceLog);
                }
            } else {
                traceLog = "ADRESSE_ERROR_NPA_NOT_FOUND;" + traceLog;
                father.incrementCountAdresseError();
                father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
            }
        } catch (Exception e) {
            traceLog = "ADRESSE_ERROR;" + traceLog;
            traceLog += AMRepriseTiersProcess.stack2string(e);
            father.incrementCountAdresseError();
            father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
            e.printStackTrace();
            transaction.clearErrorBuffer();
            transaction.clearWarningBuffer();
            JadeThread.logClear();
        }
        father.incrementCountAdresseTotal();
    }

    /**
     * Nouvelle méthode de création des adresses
     * 
     * @param famille
     * @param contribuable
     * @param gestionTiers
     * @param personneEtendue
     */
    private void repriseContribuableAdresseNew(SimpleFamilleReprise famille, ContribuableReprise contribuable,
            AMGestionTiers gestionTiers, PersonneEtendueComplexModel personneEtendue) {

        // --------------------------------------------------------------------------------
        // VALEUR DE TRAVAIL STRING
        // --------------------------------------------------------------------------------
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateToday = sdf.format(cal.getTime());
        String dateAvantReprise = "15.11.2011";
        String dateApresReprise = "16.11.2011";
        String nom = personneEtendue.getTiers().getDesignation1();
        String prenom = personneEtendue.getTiers().getDesignation2();
        String nomPrenom = nom + " " + prenom;
        String currentLine1 = nomPrenom;
        String currentLine2 = contribuable.getSimpleContribuableInfoReprise().getLigneAdresse1();
        String currentLine3 = contribuable.getSimpleContribuableInfoReprise().getLigneAdresse2();
        String currentLine4 = contribuable.getSimpleContribuableInfoReprise().getLigneAdresse3();
        if (currentLine2.toUpperCase().startsWith("ET ")) {
            currentLine2 = currentLine3;
            currentLine3 = currentLine4;
            currentLine4 = "";
        }
        String rue = contribuable.getSimpleContribuableInfoReprise().getNomDeRue();
        String noRue = contribuable.getSimpleContribuableInfoReprise().getNumeroDeRue();
        String casePostale = contribuable.getSimpleContribuableInfoReprise().getCasePostale();
        String npa = contribuable.getSimpleContribuableInfoReprise().getNumeroPostal();
        npa += "00";
        String localiteCommune = contribuable.getSimpleContribuableInfoReprise().getCommune();
        // --------------------------------------------------------------------------------
        // VALEUR DE TRAVAIL BOOLEAN
        // --------------------------------------------------------------------------------
        boolean bHasPA = false;
        if (currentLine2.toUpperCase().contains("P/A")) {
            bHasPA = true;
        }
        if (currentLine3.toUpperCase().contains("P/A")) {
            bHasPA = true;
        }
        if (currentLine4.toUpperCase().contains("P/A")) {
            bHasPA = true;
        }
        boolean bHasCP = false;
        if (!JadeStringUtil.isEmpty(casePostale)) {
            bHasCP = true;
        }

        // --------------------------------------------------------------------------------
        // LOG
        // --------------------------------------------------------------------------------
        String traceLog = "";
        traceLog += ";" + contribuable.getId();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNumeroContribuableActuelFormate();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNnss();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getDateNaissanceActuelle();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getNomDeFamille();
        traceLog += ";" + contribuable.getSimpleContribuableInfoReprise().getPrenom();
        traceLog += ";" + currentLine1;
        traceLog += ";" + currentLine2;
        traceLog += ";" + currentLine3;
        traceLog += ";" + currentLine4;
        traceLog += ";" + rue;
        traceLog += ";" + noRue;
        traceLog += ";CP " + casePostale;
        traceLog += ";" + npa;
        traceLog += ";" + localiteCommune;
        traceLog += ";";
        // --------------------------------------------------------------------------------

        try {
            // --------------------------------------------------------------------------------
            // RECHERCHE D'UNE ADRESSE EXISTANTE STANDARD
            // --------------------------------------------------------------------------------
            AdresseTiersDetail currentAdresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    personneEtendue.getTiers().getIdTiers(), true, dateToday, AMGestionTiers.CS_DOMAINE_DEFAUT,
                    AMGestionTiers.CS_TYPE_COURRIER, null);
            // --------------------------------------------------------------------------------
            // SI PAS D'ADRESSE TROUVEE
            // TRAVAIL SUR L'ADRESSE AMAL
            // --------------------------------------------------------------------------------
            if ((currentAdresse == null) || JadeStringUtil.isEmpty(currentAdresse.getAdresseFormate())) {
                if (bHasPA) {
                    // --------------------------------------------------------------------------------
                    // SI ADRESSE AMAL CONTIENT P/A >> UNIQUEMENT ADRESSE COURRIER
                    // --------------------------------------------------------------------------------
                    AdresseComplexModel newAdresse = new AdresseComplexModel();
                    if (!father.getIsSimulation()) {
                        newAdresse.getAvoirAdresse().setDateDebutRelation(dateToday);
                        newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                        newAdresse.getLocalite().setNumPostal(npa);
                        newAdresse.getAdresse().setRue(rue);
                        newAdresse.getAdresse().setNumeroRue(noRue);
                        newAdresse.getAdresse().setTitreAdresse(personneEtendue.getTiers().getTitreTiers());
                        newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                        newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                        newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                        newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                        newAdresse.getAdresse().setCasePostale(casePostale);
                        newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(newAdresse,
                                AMGestionTiers.CS_DOMAINE_DEFAUT, AMGestionTiers.CS_TYPE_COURRIER, false);
                        transaction.commit();
                    }
                    if (newAdresse != null) {
                        traceLog = "ADRESSE_NEW_COURRIER_ONLY;" + traceLog;
                        father.incrementCountAdresseNew();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES, traceLog);
                    } else {
                        if (transaction.hasErrors()) {
                            traceLog = "ADRESSE_ERROR_COURRIER_ONLY;" + transaction.getErrors() + ";" + traceLog;
                            transaction.clearErrorBuffer();
                        } else {
                            traceLog = "ADRESSE_ERROR_COURRIER_ONLY;" + traceLog;
                        }
                        father.incrementCountAdresseError();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                    }
                } else if (bHasCP) {
                    // --------------------------------------------------------------------------------
                    // SI ADRESSE AMAL CONTIENT CASE POSTALE >> ADRESSE COURRIER ET ADRESSE DOMICILE
                    // --------------------------------------------------------------------------------
                    // --------------------------------------------------------------------------------
                    // 1) ADRESSE DOMICILE
                    // --------------------------------------------------------------------------------
                    AdresseComplexModel newAdresse = new AdresseComplexModel();
                    if (!father.getIsSimulation()) {
                        newAdresse.getAvoirAdresse().setDateDebutRelation(dateToday);
                        newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                        newAdresse.getLocalite().setNumPostal(npa);
                        newAdresse.getAdresse().setRue(rue);
                        newAdresse.getAdresse().setNumeroRue(noRue);
                        newAdresse.getAdresse().setTitreAdresse(personneEtendue.getTiers().getTitreTiers());
                        newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                        newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                        newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                        newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                        newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(newAdresse,
                                AMGestionTiers.CS_DOMAINE_DEFAUT, AMGestionTiers.CS_TYPE_DOMICILE, false);
                        transaction.commit();
                    }
                    if (newAdresse != null) {
                        traceLog = "ADRESSE_NEW_DOMICILE_BOTH;" + traceLog;
                        father.incrementCountAdresseNew();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES, traceLog);
                    } else {
                        if (transaction.hasErrors()) {
                            traceLog = "ADRESSE_ERROR_DOMICILE_BOTH;" + transaction.getErrors() + ";" + traceLog;
                            transaction.clearErrorBuffer();
                        } else {
                            traceLog = "ADRESSE_ERROR_DOMICILE_BOTH;" + traceLog;
                        }
                        father.incrementCountAdresseError();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                    }
                    // --------------------------------------------------------------------------------
                    // 2) ADRESSE COURRIER
                    // --------------------------------------------------------------------------------
                    newAdresse = new AdresseComplexModel();
                    if (!father.getIsSimulation()) {
                        newAdresse.getAvoirAdresse().setDateDebutRelation(dateToday);
                        newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                        newAdresse.getLocalite().setNumPostal(npa);
                        newAdresse.getAdresse().setRue(rue);
                        newAdresse.getAdresse().setNumeroRue(noRue);
                        newAdresse.getAdresse().setTitreAdresse(personneEtendue.getTiers().getTitreTiers());
                        newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                        newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                        newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                        newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                        newAdresse.getAdresse().setCasePostale(casePostale);
                        newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(newAdresse,
                                AMGestionTiers.CS_DOMAINE_DEFAUT, AMGestionTiers.CS_TYPE_COURRIER, false);
                        transaction.commit();
                    }
                    if (newAdresse != null) {
                        traceLog = "ADRESSE_NEW_COURRIER_BOTH;" + traceLog;
                        father.incrementCountAdresseNew();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES, traceLog);
                    } else {
                        if (transaction.hasErrors()) {
                            traceLog = "ADRESSE_ERROR_COURRIER_BOTH;" + transaction.getErrors() + ";" + traceLog;
                            transaction.clearErrorBuffer();
                        } else {
                            traceLog = "ADRESSE_ERROR_COURRIER_BOTH;" + traceLog;
                        }
                        father.incrementCountAdresseError();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                    }
                } else {
                    // --------------------------------------------------------------------------------
                    // AUTREMENT, UNIQUEMENT ADRESSE DOMICILE
                    // --------------------------------------------------------------------------------
                    AdresseComplexModel newAdresse = new AdresseComplexModel();
                    if (!father.getIsSimulation()) {
                        newAdresse.getAvoirAdresse().setDateDebutRelation(dateToday);
                        newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                        newAdresse.getLocalite().setNumPostal(npa);
                        newAdresse.getAdresse().setRue(rue);
                        newAdresse.getAdresse().setNumeroRue(noRue);
                        newAdresse.getAdresse().setTitreAdresse(personneEtendue.getTiers().getTitreTiers());
                        newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                        newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                        newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                        newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                        newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(newAdresse,
                                AMGestionTiers.CS_DOMAINE_DEFAUT, AMGestionTiers.CS_TYPE_DOMICILE, false);
                        transaction.commit();
                    }
                    if (newAdresse != null) {
                        traceLog = "ADRESSE_NEW_DOMICILE_ONLY;" + traceLog;
                        father.incrementCountAdresseNew();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES, traceLog);
                    } else {
                        if (transaction.hasErrors()) {
                            traceLog = "ADRESSE_ERROR_DOMICILE_ONLY;" + transaction.getErrors() + ";" + traceLog;
                            transaction.clearErrorBuffer();
                        } else {
                            traceLog = "ADRESSE_ERROR_DOMICILE_ONLY;" + traceLog;
                        }
                        father.incrementCountAdresseError();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                    }
                }
            } else {
                // --------------------------------------------------------------------------------
                // CONTROLE DE L'ADRESSE RETOURNEE
                // TYPE DOMICILE >> TRAVAIL
                // TYPE COURRIER >> ON LAISSE TEL QUEL
                // --------------------------------------------------------------------------------
                AdresseComplexModel adresseToUpdate = null;
                String idAdresse = currentAdresse.getFields().get(AdresseTiersDetail.ADRESSE_ID_ADRESSE);
                AdresseSearchComplexModel adresseSearch = new AdresseSearchComplexModel();
                adresseSearch.setForIdAdresseInterneUnique(idAdresse);
                adresseSearch.setForIdTiers(personneEtendue.getTiers().getIdTiers());
                adresseSearch = TIBusinessServiceLocator.getAdresseService().findAdresse(adresseSearch);
                for (int iAdresse = 0; iAdresse < adresseSearch.getSize(); iAdresse++) {
                    AdresseComplexModel currentAdresseComplex = (AdresseComplexModel) adresseSearch.getSearchResults()[iAdresse];
                    if (JadeStringUtil.isEmpty(currentAdresseComplex.getAvoirAdresse().getDateFinRelation())) {
                        adresseToUpdate = currentAdresseComplex;
                        break;
                    }
                }
                if (adresseToUpdate != null) {
                    // --------------------------------------------------------------------------------
                    // ADRESSE TROUVEE, TRAVAIL SUR L'ADRESSE
                    // RETOURNEE DEPUIS LE DOMAINE STANDARD
                    // --------------------------------------------------------------------------------
                    if (adresseToUpdate.getAvoirAdresse().getTypeAdresse().equals(AMGestionTiers.CS_TYPE_COURRIER)) {
                        // --------------------------------------------------------------------------------
                        // PAS D'UPDATE, IL S'AGIT D'UNE ADRESSE COURRIER
                        // --------------------------------------------------------------------------------
                        traceLog = "ADRESSE_UPDATED_NO_NEED_TO_UPDATE_IS_COURRIER;" + traceLog;
                        father.incrementCountAdresseUpdated();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_ADRESSES, traceLog);
                    } else if (adresseToUpdate.getAvoirAdresse().getTypeAdresse()
                            .equals(AMGestionTiers.CS_TYPE_DOMICILE)) {
                        // contrôler si la date de début est renseignée !!!
                        if (JadeStringUtil.isEmpty(adresseToUpdate.getAvoirAdresse().getDateDebutRelation())
                                || JadeDateUtil.isDateAfter(dateAvantReprise, adresseToUpdate.getAvoirAdresse()
                                        .getDateDebutRelation())) {
                            // --------------------------------------------------------------------------------
                            // UPDATE SI LA DATE DE DEBUT DE VALIDITE EST AVANT LE 15.11.2011
                            // --------------------------------------------------------------------------------
                            // --------------------------------------------------------------------------------
                            // 1) UPDATE DE L'ADRESSE DOMICILE SI PAS PA AVEC LA DATE FIN
                            // --------------------------------------------------------------------------------
                            boolean bCanCreateNewDomicile = false;
                            if (!bHasPA) {
                                TIAvoirAdresseManager adrMgr = new TIAvoirAdresseManager();
                                adrMgr.setSession(getSession());
                                adrMgr.setForIdTiers(personneEtendue.getTiers().getIdTiers());
                                adrMgr.setForIdAdresse(adresseToUpdate.getAdresse().getIdAdresse());
                                adrMgr.setForIdApplication(adresseToUpdate.getAvoirAdresse().getIdApplication());
                                adrMgr.setForTypeAdresse(adresseToUpdate.getAvoirAdresse().getTypeAdresse());
                                adrMgr.setForIdExterne(adresseToUpdate.getAvoirAdresse().getIdExterne());
                                // --------------------------------------------------------------------------------
                                // DATE DE FIN ENTITE EXISTANTE
                                // --------------------------------------------------------------------------------
                                try {
                                    adrMgr.find();
                                    if (adrMgr.getSize() >= 1) {
                                        List<TIAvoirAdresse> listeAdresses = new ArrayList<TIAvoirAdresse>();
                                        for (int iAdresse = 0; iAdresse < adrMgr.getSize(); iAdresse++) {
                                            TIAvoirAdresse adresseAvoirToUpdate = (TIAvoirAdresse) adrMgr.get(iAdresse);
                                            if (JadeStringUtil.isEmpty(adresseAvoirToUpdate.getDateFinRelation())) {
                                                listeAdresses.add(adresseAvoirToUpdate);
                                            }
                                        }
                                        if (listeAdresses.size() == 1) {
                                            if (!father.getIsSimulation()) {
                                                TIAvoirAdresse adresseAvoirToUpdate = listeAdresses.get(0);
                                                adresseAvoirToUpdate.setDateFinRelation(dateAvantReprise);
                                                adresseAvoirToUpdate.save(transaction);
                                                if (transaction.isRollbackOnly()) {
                                                    traceLog = " - Adresse BIZARRE errorMessages : "
                                                            + transaction.getErrors() + traceLog;
                                                    traceLog = traceLog.replaceAll("(\r\n|\r|\n|\n\r)", " ");
                                                }
                                                transaction.commit();
                                                traceLog = "ADRESSE_UPDATED_DATE_FIN_DOMICILE_OK;" + traceLog;
                                                father.incrementCountAdresseUpdated();
                                                father.writeStatusFile(
                                                        AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_ADRESSES,
                                                        traceLog);
                                            }
                                            // Si la fermeture de l'ancienne adresse s'est bien déroulée
                                            // on peut recréer la nouvelle
                                            bCanCreateNewDomicile = true;
                                        } else {
                                            JadeLogger.info(null, " PROBLEM : FIND " + listeAdresses.size()
                                                    + " ADRESSE(S) TO UPDATE WITH DATE " + dateAvantReprise + traceLog);
                                        }
                                    } else {
                                        JadeLogger.info(null, " PROBLEM : FIND " + adrMgr.getSize()
                                                + " ADRESSE(S) TO UPDATE WITH DATE " + dateAvantReprise + traceLog);
                                    }
                                } catch (Exception exAvoir) {
                                    // if (this.transaction.isRollbackOnly()) {
                                    // try {
                                    // this.transaction.rollback();
                                    // } catch (Exception e) {
                                    // // TODO Auto-generated catch block
                                    // e.printStackTrace();
                                    // }
                                    // }
                                    traceLog = "ADRESSE_ERROR_UPDATED_DATE_FIN_DOMICILE;" + traceLog;
                                    traceLog += AMRepriseTiersProcess.stack2string(exAvoir);
                                    father.incrementCountAdresseError();
                                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES,
                                            traceLog);
                                } finally {
                                    JadeThread.logClear();
                                    transaction.clearErrorBuffer();
                                    transaction.clearWarningBuffer();
                                }
                                // --------------------------------------------------------------------------------
                                // CREATION NOUVELLE ADRESSE DE DOMICILE
                                // --------------------------------------------------------------------------------
                                if (bCanCreateNewDomicile) {
                                    AdresseComplexModel newAdresse = new AdresseComplexModel();
                                    if (!father.getIsSimulation()) {
                                        newAdresse.getAvoirAdresse().setDateDebutRelation(dateApresReprise);
                                        newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                                        newAdresse.getLocalite().setNumPostal(npa);
                                        newAdresse.getAdresse().setRue(rue);
                                        newAdresse.getAdresse().setNumeroRue(noRue);
                                        newAdresse.getAdresse().setTitreAdresse(
                                                personneEtendue.getTiers().getTitreTiers());
                                        newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                                        newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                                        newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                                        newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                                        newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(
                                                newAdresse, AMGestionTiers.CS_DOMAINE_DEFAUT,
                                                AMGestionTiers.CS_TYPE_DOMICILE, false);
                                        transaction.commit();
                                    }
                                    if (newAdresse != null) {
                                        traceLog = "ADRESSE_NEW_DOMICILE_UPDATED;" + traceLog;
                                        father.incrementCountAdresseNew();
                                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES,
                                                traceLog);
                                    } else {
                                        if (transaction.hasErrors()) {
                                            traceLog = "ADRESSE_ERROR_DOMICILE_UPDATED;" + transaction.getErrors()
                                                    + ";" + traceLog;
                                            transaction.clearErrorBuffer();
                                        } else {
                                            traceLog = "ADRESSE_ERROR_DOMICILE_UPDATED;" + traceLog;
                                        }
                                        father.incrementCountAdresseError();
                                        father.writeStatusFile(
                                                AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                                    }
                                }
                            }

                            if (bHasPA || bHasCP) {
                                // --------------------------------------------------------------------------------
                                // 2) CREATION DE L'ADRESSE COURRIER si PA OU CP
                                // --------------------------------------------------------------------------------
                                AdresseComplexModel newAdresse = new AdresseComplexModel();
                                if (!father.getIsSimulation()) {
                                    newAdresse.getAvoirAdresse().setDateDebutRelation(dateToday);
                                    newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                                    newAdresse.getLocalite().setNumPostal(npa);
                                    newAdresse.getAdresse().setRue(rue);
                                    newAdresse.getAdresse().setNumeroRue(noRue);
                                    newAdresse.getAdresse().setTitreAdresse(personneEtendue.getTiers().getTitreTiers());
                                    newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                                    newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                                    newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                                    newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                                    newAdresse.getAdresse().setCasePostale(casePostale);
                                    newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(newAdresse,
                                            AMGestionTiers.CS_DOMAINE_DEFAUT, AMGestionTiers.CS_TYPE_COURRIER, false);
                                    transaction.commit();
                                }
                                if (newAdresse != null) {
                                    traceLog = "ADRESSE_NEW_COURRIER_DOMICILE_EXISTING;" + traceLog;
                                    father.incrementCountAdresseNew();
                                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES,
                                            traceLog);
                                } else {
                                    if (transaction.hasErrors()) {
                                        traceLog = "ADRESSE_ERROR_COURRIER_DOMICILE_EXISTING;"
                                                + transaction.getErrors() + ";" + traceLog;
                                        transaction.clearErrorBuffer();
                                    } else {
                                        traceLog = "ADRESSE_ERROR_COURRIER_DOMICILE_EXISTING;" + traceLog;
                                    }
                                    father.incrementCountAdresseError();
                                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES,
                                            traceLog);
                                }
                            }
                        } else {
                            // --------------------------------------------------------------------------------
                            // UPDATE SI LA DATE DE DEBUT DE VALIDITE EST APRES LE 15.11.2011
                            // --------------------------------------------------------------------------------
                            // --------------------------------------------------------------------------------
                            // 1) CREATION DE L'ADRESSE COURRIER si cp ou pa
                            // --------------------------------------------------------------------------------
                            if (bHasPA || bHasCP) {
                                AdresseComplexModel newAdresse = new AdresseComplexModel();
                                if (!father.getIsSimulation()) {
                                    newAdresse.getAvoirAdresse().setDateDebutRelation(dateToday);
                                    newAdresse.getTiers().setId(personneEtendue.getTiers().getId());
                                    newAdresse.getLocalite().setNumPostal(npa);
                                    newAdresse.getAdresse().setRue(rue);
                                    newAdresse.getAdresse().setNumeroRue(noRue);
                                    newAdresse.getAdresse().setTitreAdresse(personneEtendue.getTiers().getTitreTiers());
                                    newAdresse.getAdresse().setLigneAdresse1(currentLine1);
                                    newAdresse.getAdresse().setLigneAdresse2(currentLine2);
                                    newAdresse.getAdresse().setLigneAdresse3(currentLine3);
                                    newAdresse.getAdresse().setLigneAdresse4(currentLine4);
                                    newAdresse.getAdresse().setCasePostale(casePostale);
                                    newAdresse = TIBusinessServiceLocator.getAdresseService().addAdresse(newAdresse,
                                            AMGestionTiers.CS_DOMAINE_DEFAUT, AMGestionTiers.CS_TYPE_COURRIER, false);
                                    transaction.commit();
                                }
                                if (newAdresse != null) {
                                    traceLog = "ADRESSE_NEW_COURRIER_DOMICILE_EXISTING;" + traceLog;
                                    father.incrementCountAdresseNew();
                                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES,
                                            traceLog);
                                } else {
                                    if (transaction.hasErrors()) {
                                        traceLog = "ADRESSE_ERROR_COURRIER_DOMICILE_EXISTING;"
                                                + transaction.getErrors() + ";" + traceLog;
                                        transaction.clearErrorBuffer();
                                    } else {
                                        traceLog = "ADRESSE_ERROR_COURRIER_DOMICILE_EXISTING;" + traceLog;
                                    }
                                    father.incrementCountAdresseError();
                                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES,
                                            traceLog);
                                }
                            }
                        }

                    } else {
                        // --------------------------------------------------------------------------------
                        // BIZARRE, NI COURRIER, NI DOMICILE
                        // --------------------------------------------------------------------------------
                        traceLog = "ADRESSE_ERROR_DOMICILE_COURRIER_NOT_FOUND;" + traceLog;
                        father.incrementCountAdresseError();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                    }
                } else {
                    // --------------------------------------------------------------------------------
                    // BIZARRE, CASCADE NOUS RETOURNE UNE ADRESSE, MAIS AUCUNE N'EST ACTIVE
                    // --------------------------------------------------------------------------------
                    traceLog = "ADRESSE_ERROR_DOMICILE_COURRIER_NOT_ACTIVE;" + traceLog;
                    father.incrementCountAdresseError();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
                }
            }
        } catch (Exception ex) {
            if (transaction.hasErrors()) {
                traceLog = "ADRESSE_ERROR;" + transaction.getErrors() + ";" + traceLog;
            } else {
                traceLog = "ADRESSE_ERROR;" + traceLog;
            }
            // if (this.transaction.isRollbackOnly()) {
            // try {
            // this.transaction.rollback();
            // } catch (Exception e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // }
            transaction.clearErrorBuffer();
            transaction.clearWarningBuffer();
            traceLog += AMRepriseTiersProcess.stack2string(ex);
            father.incrementCountAdresseError();
            father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES, traceLog);
        }
    }

    /**
     * Traitement pour l'enregistrement de l'historique des no de contribuables
     * 
     * @param famille
     * @param contribuable
     * @param gestionTiers
     * @param personneEtendue
     */
    private void repriseContribuableHistorique(SimpleFamilleReprise famille, ContribuableReprise contribuable,
            AMGestionTiers gestionTiers, PersonneEtendueComplexModel personneEtendue) {

        ArrayList<String> allNumbers = new ArrayList<String>();

        // Info no contribuable sous :
        String noContribuable1 = contribuable.getSimpleContribuableInfoReprise().getNumeroContribuableActuelFormate();
        String noContribuable2 = contribuable.getSimpleContribuableInfoReprise()
                .getNumeroContribuableNouveauFormatNouvelleValeur();
        String noContribuable3 = contribuable.getSimpleContribuableInfoReprise()
                .getNumeroContribuableAncienFormatNouvelleValeur();
        String noContribuable4 = contribuable.getSimpleContribuableInfoReprise()
                .getNumeroContribuableNouveauFormatAncienneValeur();
        String noContribuable5 = contribuable.getSimpleContribuableInfoReprise()
                .getNumeroContribuableAncienFormatAncienneValeur();

        if (!personneEtendue.getPersonneEtendue().getNumContribuableActuel().equals(noContribuable1)) {
            if (!JadeStringUtil.isEmpty(noContribuable1) && !allNumbers.contains(noContribuable1)) {
                allNumbers.add(noContribuable1);
            }
        }
        if (!personneEtendue.getPersonneEtendue().getNumContribuableActuel().equals(noContribuable2)) {
            if (!JadeStringUtil.isEmpty(noContribuable2) && !allNumbers.contains(noContribuable2)) {
                allNumbers.add(noContribuable2);
            }
        }
        if (!personneEtendue.getPersonneEtendue().getNumContribuableActuel().equals(noContribuable3)) {
            if (!JadeStringUtil.isEmpty(noContribuable3) && !allNumbers.contains(noContribuable3)) {
                allNumbers.add(noContribuable3);
            }
        }
        if (!personneEtendue.getPersonneEtendue().getNumContribuableActuel().equals(noContribuable4)) {
            if (!JadeStringUtil.isEmpty(noContribuable4) && !allNumbers.contains(noContribuable4)) {
                allNumbers.add(noContribuable4);
            }
        }
        if (!personneEtendue.getPersonneEtendue().getNumContribuableActuel().equals(noContribuable5)) {
            if (!JadeStringUtil.isEmpty(noContribuable5) && !allNumbers.contains(noContribuable5)) {
                allNumbers.add(noContribuable5);
            }
        }

        for (int iNumber = 0; iNumber < allNumbers.size(); iNumber++) {
            String currentNumber = allNumbers.get(iNumber);
            if ((currentNumber == null) || currentNumber.equals("")) {
                continue;
            }
            // Search dans l'historique si nous avons déjà ce numéro de contribuable
            TIHistoriqueContribuableManager histComMng = new TIHistoriqueContribuableManager();
            histComMng.setSession(getSession());
            histComMng.setForIdTiers(personneEtendue.getTiers().getIdTiers());
            try {
                histComMng.find();
                Boolean bFound = false;
                for (int i = 0; i < histComMng.size(); i++) {
                    TIHistoriqueContribuable entity = ((TIHistoriqueContribuable) histComMng.getEntity(i));
                    if (entity.getNumContribuable().equals("")) {
                        if (!father.getIsSimulation()) {
                            try {
                                entity.delete(transaction);
                                transaction.commit();
                            } catch (Exception ex) {
                                JadeLogger.error(this,
                                        "Error deleting no contribuable " + AMRepriseTiersProcess.stack2string(ex));
                                // ex.printStackTrace();
                                JadeThread.logClear();
                                transaction.clearErrorBuffer();
                                transaction.clearWarningBuffer();
                            }
                        }
                    }
                    if (entity.getNumContribuable().equals(currentNumber.trim())) {
                        bFound = true;
                        break;
                    }
                }
                // Si non, ajout dans l'historique
                if (!bFound) {
                    TIHistoriqueContribuable currentHisto = new TIHistoriqueContribuable();
                    currentHisto.setSession(session);
                    currentHisto.setEntreeVigueur("15.11.2011");
                    currentHisto.setFinVigueur("16.11.2011");
                    currentHisto.setIdTiers(personneEtendue.getTiers().getIdTiers());
                    currentHisto.setMotifHistorique(TIHistoriqueContribuable.CS_CREATION);
                    currentHisto.setNumContribuable(currentNumber.trim());
                    JadeLogger.info(this, "Saving histo no contribuable (" + currentNumber.trim() + ", "
                            + personneEtendue.getTiers().getDesignation1() + ","
                            + personneEtendue.getTiers().getDesignation2() + ")");
                    if (!father.getIsSimulation()) {
                        try {
                            currentHisto.save();
                            transaction.commit();
                            // synchronized (this.father) {
                            // try {
                            // currentHisto.save(this.transaction);
                            // this.transaction.commit();
                            // } catch (Exception ex) {
                            // JadeThread.logClear(); // TODO : HANDLE ERRORS
                            // this.transaction.clearErrorBuffer();
                            // }
                            // }
                        } catch (Exception ex) {
                            JadeLogger.error(this, "Error saving histo no contribuable (" + currentNumber.trim() + ", "
                                    + personneEtendue.getTiers().getDesignation1() + ","
                                    + personneEtendue.getTiers().getDesignation2() + ") : " + ex.toString());
                            ex.printStackTrace();
                            JadeThread.logClear();
                            transaction.clearErrorBuffer();
                            transaction.clearWarningBuffer();
                        }
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, "Error searching histo no contribuable (" + currentNumber.trim() + ", "
                        + personneEtendue.getTiers().getDesignation1() + ","
                        + personneEtendue.getTiers().getDesignation2() + ") : " + e.toString());
                e.printStackTrace();
            }

        }

    }

    /**
     * Traitement de la reprise d'un membre de famille
     * 
     * @param famille
     * @param gestionTiers
     */
    public void repriseMembreFamille(SimpleFamilleReprise famille, AMGestionTiers gestionTiers) {

        // ---------------------------------------------------------------------------
        // PREPARATION DE LA RECHERCHE DANS TIERS
        // ---------------------------------------------------------------------------
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());
        // Get nom - prenom, datenaissance, noAVS
        String nomPrenom = famille.getNomPrenom().trim();
        String prenom = nomPrenom.substring(famille.getNomPrenom().lastIndexOf(" ")).trim();
        String nom = nomPrenom.substring(0, famille.getNomPrenom().lastIndexOf(" ")).trim();
        String dateNaissance = famille.getDateNaissance();
        String gender = famille.getSexe();
        String titre = gender;
        if (gender.indexOf("Monsieur") == 0) {
            gender = "516001";
            titre = "502001";
        } else if (gender.indexOf("Madame") == 0) {
            gender = "516002";
            titre = "502002";
        } else {
            gender = "";
            titre = "";
        }

        // Attention no brut à formatter !
        String noAVS = famille.getNoAVS();

        // Set personne etendue
        PersonneEtendueComplexModel personneEtendue = new PersonneEtendueComplexModel();
        if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
            personneEtendue.getPersonne().setDateNaissance(dateNaissance);
        }
        if (!JadeStringUtil.isBlankOrZero(noAVS)) {
            String noAVSFormate = "";
            if (noAVS.length() == 13) {
                // NSS : xxx.xxxx.xxxx.xx
                noAVSFormate = noAVS.substring(0, 3);
                noAVSFormate += ".";
                noAVSFormate += noAVS.substring(3, 7);
                noAVSFormate += ".";
                noAVSFormate += noAVS.substring(7, 11);
                noAVSFormate += ".";
                noAVSFormate += noAVS.substring(11);
            } else if (noAVS.length() == 11) {
                // NOAVS : xxx.xx.xxx.xxx
                noAVSFormate = noAVS.substring(0, 3);
                noAVSFormate += ".";
                noAVSFormate += noAVS.substring(3, 5);
                noAVSFormate += ".";
                noAVSFormate += noAVS.substring(5, 8);
                noAVSFormate += ".";
                noAVSFormate += noAVS.substring(8);
            }
            noAVS = noAVSFormate;
            if (!JadeStringUtil.isBlankOrZero(noAVS)) {
                personneEtendue.getPersonneEtendue().setNumAvsActuel(noAVS);
            }
        }
        personneEtendue.getTiers().setDesignation1(nom);
        personneEtendue.getTiers().setDesignation2(prenom);
        personneEtendue.getTiers().setPersonnePhysique(true);

        // ---------------------------------------------------------------------------
        // Recherche dans TIERS
        // ---------------------------------------------------------------------------

        // Log
        String traceLog = "FAMILLE";
        try {
            // Log
            traceLog += ";" + famille.getId();
            traceLog += ";" + famille.getIdContribuable();
            traceLog += ";" + noAVS;
            traceLog += ";" + famille.getDateNaissance();
            traceLog += ";" + nom;
            traceLog += ";" + prenom;
            traceLog += ";";
            // Search tiers
            PersonneEtendueSearchComplexModel tiersSearch = gestionTiers.findTiers(personneEtendue);
            if ((tiersSearch != null) && (tiersSearch.getSize() == 1)) {
                personneEtendue = (PersonneEtendueComplexModel) tiersSearch.getSearchResults()[0];
                Boolean personneEtendueNeedUpdate = false;
                // Tiers trouvé, mettons-le à jour
                // NSS
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumAvsActuel())) {
                    if (!JadeStringUtil.isBlankOrZero(noAVS)) {
                        personneEtendue.getPersonneEtendue().setNumAvsActuel(noAVS);
                        personneEtendue.setDateModifAvs(dateToday);
                        personneEtendue.setMotifModifAvs(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Date de naissance
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
                    if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
                        personneEtendue.getPersonne().setDateNaissance(dateNaissance);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Sexe
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getSexe())) {
                    if (!JadeStringUtil.isBlankOrZero(gender)) {
                        personneEtendue.getPersonne().setSexe(gender);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Titre
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getTitreTiers())) {
                    if (!JadeStringUtil.isBlankOrZero(titre)) {
                        personneEtendue.getTiers().setTitreTiers(titre);
                        personneEtendue.setDateModifTitre(dateToday);
                        personneEtendue.setMotifModifTitre(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }

                if (personneEtendueNeedUpdate) {
                    try {
                        if (!father.getIsSimulation()) {
                            JadeThread.logClear();
                            transaction.clearErrorBuffer();
                            transaction.clearWarningBuffer();
                            personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                            personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(
                                    personneEtendue);
                        }
                        // Update des fichiers de status
                        father.incrementCountTiersUpdated();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS, traceLog);
                    } catch (Exception ex) {
                        // Update des fichiers de status AVEC UN WARNING
                        traceLog += "WARNING : " + AMRepriseTiersProcess.stack2string(ex);
                        father.incrementCountTiersUpdated();
                        father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS, traceLog);

                        // traceLog += AMRepriseTiersProcess.stack2string(ex);
                        // personneEtendue = null;
                        // this.father.incrementCountTiersError();
                        // this.father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS,
                        // traceLog);
                    } finally {
                        JadeThread.logClear();
                        transaction.clearErrorBuffer();
                        transaction.clearWarningBuffer();
                        transaction.commit();
                    }

                } else {
                    // Update des fichiers de status
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS, traceLog);
                    father.incrementCountTiersUpdated();
                }
            } else if ((tiersSearch != null) && (tiersSearch.getSize() > 1)) {
                // multiple tiers avec ces paramètres de recherches
                traceLog += "Multiple tiers trouvé selon nss, no contribuable, date naissance;";
                personneEtendue = null;
                father.incrementCountTiersError();
                father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
            } else if (tiersSearch == null) {
                // IL S'AGIT D'UN MEMBRE FAMILLE
                // SI PAS TROUVE, NOUS NE LE CREONS PAS >> personneEtendue=null
                personneEtendue = null;
                father.incrementCountTiersNew();
                father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_TIERS, traceLog);
            }
        } catch (Exception ex) {
            JadeLogger.error(this,
                    "Exception searching tiers for famille id : " + famille.getId() + " - " + ex.toString());
            personneEtendue = null;
            traceLog += AMRepriseTiersProcess.stack2string(ex);
            father.incrementCountTiersError();
            father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
        }
        // ---------------------------------------------------------------------------
        // Mise à jour des tables de reprises >> commit dans updateInnerMF
        // ---------------------------------------------------------------------------
        if ((personneEtendue != null) && (personneEtendue.getTiers() != null)
                && !JadeStringUtil.isEmpty(personneEtendue.getTiers().getIdTiers())) {

            if (father.getIsSimulation() == false) {
                try {
                    // Update RP_AMAL_MAFAMILL
                    famille.setIdTiers(famille.getIdFamille());
                    famille = father.updateSimpleFamilleReprise(famille);
                    // famille = AmalServiceLocator.getContribuableRepriseService().updateSimpleFamille(famille);
                    // Update RP_INNER_MF
                    father.updateInnerMF(famille.getIdTiers(), personneEtendue.getTiers().getIdTiers());
                } catch (Exception ex) {
                    traceLog += AMRepriseTiersProcess.stack2string(ex);
                    father.incrementCountTiersError();
                    father.writeStatusFile(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS, traceLog);
                    JadeThread.logClear();
                    transaction.clearErrorBuffer();
                    transaction.clearWarningBuffer();
                }
                JadeThread.logClear();
                transaction.clearErrorBuffer();
                transaction.clearWarningBuffer();
                repriseRole(personneEtendue.getTiers().getIdTiers());
            }
        }

    }

    /**
     * Inscription du rôle tiers si inexistant
     * 
     * @param idTiers
     */
    private void repriseRole(String idTiers) {

        // Update rôle
        if (father.getIsSimulation() == false) {
            boolean hasRole = false;
            try {
                hasRole = TIBusinessServiceLocator.getRoleService().hasRole(idTiers, AMGestionTiers.CS_ROLE_AMAL);
            } catch (Exception ex) {
                ex.printStackTrace();
                JadeThread.logClear();
                transaction.clearErrorBuffer();
                transaction.clearWarningBuffer();
            }
            if (!hasRole) {
                try {
                    TIBusinessServiceLocator.getRoleService().beginRole(idTiers, AMGestionTiers.CS_ROLE_AMAL);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            try {
                if (JadeStringUtil.isEmpty(baseThreadName)) {
                    baseThreadName = Thread.currentThread().getName();
                    if (baseThreadName.indexOf(">>") >= 0) {
                        baseThreadName = baseThreadName.split(">>")[0];
                        baseThreadName = baseThreadName.trim();
                    }
                }
                String threadName = baseThreadName + " >> " + currentFamille.getNomPrenom();
                Thread.currentThread().setName(threadName);
            } catch (Exception exN) {

            }

            // Défini l'utilisation du thread context
            JadeThreadActivator.startUsingJdbcContext(this, initContext());
            JadeThread.storeTemporaryObject("bsession", getSession());

            // Défini la transaction (PYXIS)
            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            // Do the job
            if (!currentFamille.getIsContribuable()) {
                // Membre famille NON CONTRIBUABLE
                // ---------------------------------------------------------------------
                repriseMembreFamille(currentFamille, father.getGestionTiers());
                father.incrementCountTiersTotal();
            } else if (!currentFamille.getIdTiers().equals("0")) {
                // Membre famille CONTRIBUABLE PRINCIPAL et à reprendre car idTiers != 0
                // ---------------------------------------------------------------------
                ContribuableRepriseSearch contribuableSearch = new ContribuableRepriseSearch();
                contribuableSearch.setForIdContribuable(currentFamille.getIdContribuable());
                contribuableSearch = AmalServiceLocator.getContribuableRepriseService().search(contribuableSearch);
                if (contribuableSearch.getSize() == 1) {
                    ContribuableReprise contribuable = (ContribuableReprise) contribuableSearch.getSearchResults()[0];
                    repriseContribuable(currentFamille, contribuable, father.getGestionTiers());
                    father.incrementCountTiersTotal();
                } else {
                    JadeLogger.error(this,
                            "Error searching contribuable search for id : " + currentFamille.getIdContribuable());
                }
            }
        } catch (Exception ex) {
            String csError = "Exception in AMRepriseTiersWorker, worker : " + workerNumber;
            csError += ", idContribuable : " + currentFamille.getIdContribuable();
            csError += ", idFamille : " + currentFamille.getIdFamille();
            csError += ", NomPrenom : " + currentFamille.getNomPrenom();
            JadeLogger.error(this, csError);
            JadeLogger.error(this, "Exception : " + ex.toString());
        } finally {
            if ((transaction != null) && transaction.isOpened()) {
                if (!father.getIsSimulation()) {
                    try {
                        transaction.clearErrorBuffer();
                        transaction.clearWarningBuffer();

                        transaction.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            transaction.clearErrorBuffer();
                            transaction.clearWarningBuffer();
                            transaction.closeTransaction();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        transaction.clearErrorBuffer();
                        transaction.clearWarningBuffer();
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // Libère le context - Si nécessaire, commit ou rollback la
            // transaction
            JadeThreadActivator.stopUsingContext(this);
            JadeThread.logClear();
        }

    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

}
