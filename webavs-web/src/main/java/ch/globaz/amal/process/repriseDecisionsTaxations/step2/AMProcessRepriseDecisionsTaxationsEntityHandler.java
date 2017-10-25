package ch.globaz.amal.process.repriseDecisionsTaxations.step2;

import globaz.amal.process.reprise.AMRepriseTiersProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIHistoriqueContribuableManager;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.util.TINSSFormater;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMCodeTraitementDossierFamille;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.amal.businessimpl.utils.SessionProvider;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.process.repriseDecisionsTaxations.AMProcessRepriseDecisionsTaxationsEnum;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService;
import ch.globaz.pyxis.business.service.PersonneEtendueService.motifsModification;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author cbu
 * 
 */
public class AMProcessRepriseDecisionsTaxationsEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {
    public static int _adresseUpToDate = 0;
    public static int _courrierNonDomicileNon = 0;
    public static int _courrierNonDomicileNonCPPA = 0;
    public static int _courrierNonDomicileOui = 0;
    public static int _courrierNonDomicileOuiCPPA = 0;
    public static int _courrierOuiDomicileNon = 0;
    public static int _courrierOuiDomicileOui = 0;
    public static long FOUND = 0;
    public static long FOUND_HISTO = 0;
    public static boolean isRepriseAdresses = false;
    public static long NOT_FOUND = 0;
    private ch.globaz.amal.xmlns.am_0001._1.Adresse currentContribuableAdresse = null;
    private ch.globaz.amal.xmlns.am_0001._1.Personne currentContribuableConjoint = null;
    private ch.globaz.amal.xmlns.am_0001._1.Personne currentContribuablePrincipal = null;
    private ch.globaz.amal.xmlns.am_0001._1.Contribuables currentContribuables = null;
    private JadeProcessEntity currentEntity = null;
    private String currentNoContribuable = null;
    private String currentNoContribuablePrecedent = null;
    private String dateLimiteToUpdateAdresse = null;
    private String dateProcess = null;
    private boolean dossierHistorique = false;
    private String idJob = null;
    private SortedMap<String, ch.globaz.amal.xmlns.am_0002._1.Taxation> mapCurrentPersonneChargeTaxation = null;
    private int nbEnfants = 0;
    private int nbEnfantsSuspens = 0;
    private Map<String, String> persChargDecedes = new HashMap<String, String>();
    private Map<Enum<?>, String> properties = null;
    private transient BSession session;
    private PersonneEtendueComplexModel tiersPrincipalGlobal = null;
    private BTransaction transaction;
    private Unmarshaller unmarshaller = null;
    private String yearSubside = null;

    public AMProcessRepriseDecisionsTaxationsEntityHandler(ContainerParametres container, Unmarshaller _unmarshaller,
            String _idJob) {
        unmarshaller = _unmarshaller;
        idJob = _idJob;
    }

    /**
     * Ajoute une erreur dans le Thread
     * 
     * @param e
     * @throws JadeNoBusinessLogSessionError
     */
    private void addError(Exception e) throws JadeNoBusinessLogSessionError {
        this.addError(e, null);
    }

    /**
     * Ajoute une erreur dans le Thread
     * 
     * @param e
     * @param param
     * @throws JadeNoBusinessLogSessionError
     */
    private void addError(Exception e, String[] param) throws JadeNoBusinessLogSessionError {

        JadeThread.logError("ERROR", (e.getMessage() != null) ? e.getMessage() : e.toString() + ";", param);
        if (e.getCause() != null) {
            String cause = "<br />" + e.getCause().toString();
            JadeThread.logError("CAUSE", cause + ";");
        }

    }

    /**
     * Change le no de contribuable si on a un champ "ndcprecedent"
     * 
     * @param contribuable
     * @throws Exception
     */
    private void changementNoContribuable(SimpleContribuable contribuable) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(currentNoContribuablePrecedent)
                && !contribuable.getNoContribuable().equals(currentNoContribuable) && !contribuable.isNew()) {
            try {
                String noContribuableActuel = currentNoContribuable;
                contribuable.setNoContribuable(noContribuableActuel);
                contribuable.setZoneCommuneNoContrFormate(formatNoContribuable(noContribuableActuel));
                contribuable.setZoneCommuneContribuable(noContribuableActuel);
                contribuable = AmalImplServiceLocator.getSimpleContribuableService().update(contribuable);
                JadeThread.logInfo("INFO", "Changement no contribuable'" + currentNoContribuablePrecedent
                        + "' remplacé par :" + currentNoContribuable + ";");
            } catch (Exception e) {
                throw new Exception("Error while changing noContribuable ==>"
                        + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + " / "
                        + e.getMessage());
            }

        }

    }

    /**
     * Vérifie que le tiers que l'on met à jour est bien le bon
     * 
     * @param tiersPrincipal
     * @return
     */
    private boolean checkCorrespondanceWithXmlFile(PersonneEtendueComplexModel tiersPrincipal) {
        String tiersNNSS = JadeStringUtil.removeChar(tiersPrincipal.getPersonneEtendue().getNumAvsActuel(), '.');
        String tiersNomPrenom = tiersPrincipal.getTiers().getDesignationUpper1() + " "
                + tiersPrincipal.getTiers().getDesignationUpper2();
        tiersNomPrenom = JadeStringUtil.convertSpecialChars(tiersNomPrenom);
        String tiersDateNaissance = tiersPrincipal.getPersonne().getDateNaissance();
        String xmlNNSS = JadeStringUtil.removeChar(currentContribuablePrincipal.getNavs13().toString(), '.');
        String xmlNomPrenom = currentContribuablePrincipal.getNom().toUpperCase() + " "
                + currentContribuablePrincipal.getPrenom().toUpperCase();
        xmlNomPrenom = JadeStringUtil.convertSpecialChars(xmlNomPrenom);
        String xmlDateNaissance = currentContribuablePrincipal.getDateNaiss();

        if (!tiersNNSS.equals(xmlNNSS)) {
            // if (!tiersNomPrenom.equals(xmlNomPrenom)) {
            if (!tiersNomPrenom.contains(xmlNomPrenom) && !xmlNomPrenom.contains(tiersNomPrenom)) {
                if (!tiersDateNaissance.equals(xmlDateNaissance)) {
                    JadeThread.logError("ERREUR", "Le tiers du contribuable principal (" + tiersNomPrenom
                            + ") ne peut pas être relié au contribuable du fichier XML (" + xmlNomPrenom + ")");
                    return false;
                }
            }
        }

        return true;

    }

    /**
     * Vérifie si le tiers existe déjà et le retourne si c'est le cas.
     * 
     * @param contri
     * @return
     */
    private String checkTiersADouble(SimpleContribuable contri) {
        try {
            SimpleContribuable c = AmalImplServiceLocator.getSimpleContribuableService().read(
                    contri.getIdContribuable());

            PersonneEtendueSearchComplexModel pSearchNoContribuable = new PersonneEtendueSearchComplexModel();
            pSearchNoContribuable.setForNoContribuable(formatNoContribuable(currentNoContribuable));
            pSearchNoContribuable.setForDateNaissance(currentContribuablePrincipal.getDateNaiss());
            pSearchNoContribuable.setFor_isInactif("2");
            pSearchNoContribuable = TIBusinessServiceLocator.getPersonneEtendueService().find(pSearchNoContribuable);

            if (pSearchNoContribuable.getSize() == 1) {

                PersonneEtendueSearchComplexModel pSearchNoAVS = new PersonneEtendueSearchComplexModel();
                pSearchNoAVS.setForNumeroAvsActuel(getCurrentValidFormattedNSS(currentContribuablePrincipal.getNavs13()
                        .toString()));

                if (JadeStringUtil.isBlankOrZero(pSearchNoAVS.getForNumeroAvsActuel())) {
                    return null;
                }

                pSearchNoAVS.setFor_isInactif("2");
                pSearchNoAVS = TIBusinessServiceLocator.getPersonneEtendueService().find(pSearchNoAVS);

                if (pSearchNoAVS.getSize() == 1) {

                    PersonneEtendueComplexModel pNoContribuable = (PersonneEtendueComplexModel) pSearchNoContribuable
                            .getSearchResults()[0];
                    PersonneEtendueComplexModel pNoAVS = (PersonneEtendueComplexModel) pSearchNoAVS.getSearchResults()[0];

                    String idTiersCorrect = pNoAVS.getTiers().getIdTiers();
                    String idTiersCurrent = pNoContribuable.getTiers().getIdTiers();

                    if (idTiersCorrect.equals(idTiersCurrent)) {
                        return null;
                    }

                    SimpleFamilleSearch sfSearch = new SimpleFamilleSearch();
                    sfSearch.setForIdContribuable(c.getIdContribuable());
                    sfSearch.setIsContribuable(true);
                    // sfSearch.setForFinDefinitive("0");
                    sfSearch = AmalImplServiceLocator.getSimpleFamilleService().search(sfSearch);

                    if (sfSearch.getSize() > 0) {

                        if (sfSearch.getSize() > 1) {
                            boolean contribPrincFoud = false;
                            for (JadeAbstractModel model : sfSearch.getSearchResults()) {
                                SimpleFamille sf = (SimpleFamille) model;

                                if (JadeStringUtil.isBlankOrZero(sf.getFinDefinitive())) {
                                    if (contribPrincFoud) {
                                        throw new Exception("Le contribuable " + c.getNoContribuable()
                                                + " possède plusieurs contribuables principals !");
                                    }
                                    contribPrincFoud = true;
                                }
                            }
                        }

                        SimpleFamille sf = (SimpleFamille) sfSearch.getSearchResults()[0];
                        // On force une relecture pour syncroniser le membre famille avec le tiers
                        sf = AmalImplServiceLocator.getSimpleFamilleService().read(sf.getIdFamille());
                        sf.setIdTier(idTiersCorrect);
                        AmalImplServiceLocator.getSimpleFamilleService().update(sf);
                        c.setIdTier(idTiersCorrect);
                        AmalImplServiceLocator.getSimpleContribuableService().update(c);
                        JadeThread.logInfo("INFO", "idTiers modifiés ! (" + idTiersCurrent + " ==> " + idTiersCorrect
                                + ")");

                        ArrayList<TIRole> roles = TIBusinessServiceLocator.getRoleService().whichRoles(
                                pNoContribuable.getTiers().getIdTiers());
                        if (roles.size() == 1) {
                            TIRole role = roles.get(0);
                            if (AMGestionTiers.CS_ROLE_AMAL.equals(role.getRole())) {
                                pNoContribuable.getTiers().set_inactif("1");
                                TIBusinessServiceLocator.getPersonneEtendueService().update(pNoContribuable);
                                JadeThread.logInfo("INFO", "Ancien tiers relié désactivé (Id : "
                                        + pNoContribuable.getTiers().getIdTiers() + ")");
                            } else {
                                JadeThread.logInfo("INFO", "1 rôle mais différent de AMAL sur le tiers "
                                        + pNoContribuable.getTiers().getDesignation1() + " "
                                        + pNoContribuable.getTiers().getDesignation2() + " - id: "
                                        + pNoContribuable.getTiers().getIdTiers());
                            }
                        } else if (roles.size() > 1) {
                            JadeThread.logInfo("INFO",
                                    "Plusieurs tiers trouvés mais rôles autre que AMAL présents. Tiers : "
                                            + pNoContribuable.getTiers().getDesignation1() + " "
                                            + pNoContribuable.getTiers().getDesignation2() + " - id: "
                                            + pNoContribuable.getTiers().getIdTiers());
                        } else {
                            JadeThread.logInfo("INFO", "Aucun rôles trouvé. Tiers : "
                                    + pNoContribuable.getTiers().getDesignation1() + " "
                                    + pNoContribuable.getTiers().getDesignation2() + " - id: "
                                    + pNoContribuable.getTiers().getIdTiers());
                        }

                        return idTiersCorrect;
                    } else {
                        JadeThread.logWarn("WARN", "Le contribuable " + c.getNoContribuable()
                                + " ne possède pas de contribuable principal !");
                    }
                } else if (pSearchNoAVS.getSize() > 1) {
                    JadeThread.logInfo("INFO",
                            "Plusieurs tiers trouvés avec le NNSS : " + pSearchNoAVS.getForNumeroAvsActuel());
                }

            } else if (pSearchNoContribuable.getSize() > 1) {
                JadeThread.logInfo(
                        "INFO",
                        "Plusieurs tiers trouvés avec le No contribuable : "
                                + pSearchNoContribuable.getForNoContribuable());
            }
        } catch (Exception e) {
            if (!(e.getCause() == null)) {
                if (!(e.getCause().getCause() == null)) {
                    JadeThread.logError("ERROR",
                            "Erreur recherche tiers relié ==> " + e.getMessage() + " / " + e.getCause() + " / "
                                    + e.getCause().getCause());
                } else {
                    JadeThread.logError("ERROR",
                            "Erreur recherche tiers relié ==> " + e.getMessage() + " / " + e.getCause());
                }
            } else {
                JadeThread.logError("ERROR", "Erreur recherche tiers relié ==> " + e.getMessage());
            }

        }
        return null;
    }

    /**
     * Recherche du tiers / création si inexistant
     * 
     * @param typePersonne
     * @param noContribuable
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @param dateDeces
     * @param nnss
     * @param gender
     * @param etatCivil
     * @param titre
     * @return
     */
    private PersonneEtendueComplexModel createTiers(ch.globaz.amal.xmlns.am_0001._1.TypePersonne typePersonne,
            String noContribuable, String nom, String prenom, String dateNaissance, String dateDeces, String nnss,
            String gender, String etatCivil, String titre) {
        AMGestionTiers gestionTiers = new AMGestionTiers();
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());
        // -----------------------------------------------------------------------------------------
        // Set personne etendue
        // -----------------------------------------------------------------------------------------
        PersonneEtendueComplexModel personneEtendue = new PersonneEtendueComplexModel();
        if (!JadeStringUtil.isBlankOrZero(nnss)) {
            personneEtendue.getPersonneEtendue().setNumAvsActuel(nnss);
        }
        if (!JadeStringUtil.isBlankOrZero(dateNaissance) && isValideDate(dateNaissance)) {
            personneEtendue.getPersonne().setDateNaissance(dateNaissance);
        }
        personneEtendue.getTiers().setDesignation1(nom);
        personneEtendue.getTiers().setDesignation2(prenom);
        personneEtendue.getTiers().setPersonnePhysique(true);

        // ---------------------------------------------------------------------------
        // Recherche dans TIERS
        // ---------------------------------------------------------------------------
        // Log
        try {
            // Search tiers
            ArrayList<String> personneEtendueModifications = new ArrayList<String>();
            PersonneEtendueSearchComplexModel tiersSearch = gestionTiers.findTiers(personneEtendue);

            if ((tiersSearch != null) && (tiersSearch.getSize() == 1)) {
                PersonneEtendueComplexModel personneEtendue2 = new PersonneEtendueComplexModel();
                personneEtendue = (PersonneEtendueComplexModel) tiersSearch.getSearchResults()[0];
                String dNaiss = personneEtendue.getPersonne().getDateNaissance();

                String yearDateNaissance = "";
                try {
                    yearDateNaissance = String.valueOf(new JADate(dateNaissance).getYear());
                } catch (Exception e) {
                    yearDateNaissance = "";
                }

                String yearDNaiss = "";
                try {
                    yearDNaiss = String.valueOf(new JADate(dNaiss).getYear());
                } catch (Exception e) {
                    yearDNaiss = "";
                }

                // Log des tiers non concordants
                if (!dateNaissance.equals(dNaiss) && !JadeStringUtil.isBlankOrZero(dNaiss)) {
                    JadeThread.logInfo("INFO", "Date de naissance différente ! " + nom + " " + prenom
                            + " : Date naissance PYXIS :" + dNaiss + " / Fichier : " + dateNaissance);
                }

                if (!yearDateNaissance.equals(yearDNaiss) && !JadeStringUtil.isBlankOrZero(yearDNaiss)) {
                    personneEtendue2.getPersonneEtendue().setNumAvsActuel(null);
                    if (!JadeStringUtil.isBlankOrZero(dateNaissance) && isValideDate(dateNaissance)) {
                        personneEtendue2.getPersonne().setDateNaissance(dateNaissance);
                    }
                    personneEtendue2.getTiers().setDesignation1(nom);
                    personneEtendue2.getTiers().setDesignation2(prenom);
                    personneEtendue2.getTiers().setPersonnePhysique(true);
                    tiersSearch = gestionTiers.findTiers(personneEtendue2);
                    JadeThread.logError("ERREUR", "Tiers non concordants ! " + nom + " " + prenom
                            + " : Date naissance PYXIS :" + dNaiss + " / Fichier : " + dateNaissance);
                    personneEtendue = personneEtendue2;
                }
            }

            if ((tiersSearch != null) && (tiersSearch.getSize() == 1)) {
                personneEtendue = (PersonneEtendueComplexModel) tiersSearch.getSearchResults()[0];
                Boolean personneEtendueNeedUpdate = false;
                // -----------------------------------------------------------------------------------------
                // Tiers trouvé, mettons-le à jour
                // -----------------------------------------------------------------------------------------
                // No Contribuable
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumContribuableActuel())) {
                    if (!JadeStringUtil.isBlankOrZero(noContribuable)) {
                        personneEtendue.getPersonneEtendue().setNumContribuableActuel(
                                formatNoContribuable(noContribuable));
                        personneEtendue.setDateModifContribuable(dateToday);
                        personneEtendue
                                .setMotifModifContribuable(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                        personneEtendueModifications.add("No contribuable");
                    }
                } else {
                    String noContribuablePrecedentFormate = formatNoContribuable(currentNoContribuablePrecedent);
                    if (noContribuablePrecedentFormate.equals(personneEtendue.getPersonneEtendue()
                            .getNumContribuableActuel())) {
                        if (!JadeStringUtil.isBlankOrZero(noContribuable)) {
                            personneEtendue.getPersonneEtendue().setNumContribuableActuel(
                                    formatNoContribuable(noContribuable));
                            personneEtendue.setDateModifContribuable(dateToday);
                            personneEtendue
                                    .setMotifModifContribuable(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                            personneEtendueNeedUpdate = true;

                            historisationNoContribuable(personneEtendue);
                            personneEtendueModifications.add("No contribuable avec historisation");
                        }
                    }
                }
                // NSS
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumAvsActuel())) {
                    if (!JadeStringUtil.isBlankOrZero(nnss)) {
                        personneEtendue.getPersonneEtendue().setNumAvsActuel(nnss);
                        personneEtendue.setDateModifAvs(dateToday);
                        personneEtendue.setMotifModifAvs(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                        personneEtendueModifications.add("NNSS");
                    }
                }
                // Date de naissance
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
                    if (!JadeStringUtil.isBlankOrZero(dateNaissance) && isValideDate(dateNaissance)) {
                        personneEtendue.getPersonne().setDateNaissance(dateNaissance);
                        personneEtendueNeedUpdate = true;
                        personneEtendueModifications.add("Date naissance");
                    }
                }
                // Date de décès
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateDeces())) {
                    if (!JadeStringUtil.isBlankOrZero(dateDeces) && isValideDate(dateDeces)) {
                        personneEtendue.getPersonne().setDateDeces(dateDeces);
                        personneEtendueNeedUpdate = true;
                        personneEtendueModifications.add("Date décès");
                    }
                }
                // Sexe
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getSexe())) {
                    if (!JadeStringUtil.isBlankOrZero(gender)) {
                        personneEtendue.getPersonne().setSexe(gender);
                        personneEtendueNeedUpdate = true;
                        personneEtendueModifications.add("Sexe");
                    }
                }
                // Etat civil
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getEtatCivil())) {
                    if (!JadeStringUtil.isBlankOrZero(etatCivil)) {
                        personneEtendue.getPersonne().setEtatCivil(etatCivil);
                        personneEtendueNeedUpdate = true;
                        personneEtendueModifications.add("Etat civil");
                    }
                }
                // Titre
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getTitreTiers())) {
                    if (!JadeStringUtil.isBlankOrZero(titre)) {
                        personneEtendue.getTiers().setTitreTiers(titre);
                        boolean doNotUpdate = false;
                        if ((IConstantes.CS_TIERS_TITRE_MONSIEUR.equals(personneEtendue.getTiers().getTitreTiers()) && IConstantes.CS_PERSONNE_SEXE_FEMME
                                .equals(personneEtendue.getPersonne().getSexe()))) {
                            doNotUpdate = true;
                        }

                        if ((IConstantes.CS_TIERS_TITRE_MADAME.equals(personneEtendue.getTiers().getTitreTiers()) && IConstantes.CS_PERSONNE_SEXE_HOMME
                                .equals(personneEtendue.getPersonne().getSexe()))) {
                            doNotUpdate = true;
                        }

                        if (!doNotUpdate) {

                            personneEtendue.setDateModifTitre(dateToday);
                            personneEtendue
                                    .setMotifModifTitre(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                            personneEtendueNeedUpdate = true;
                            personneEtendueModifications.add("Titre");
                        } else {
                            personneEtendue.getTiers().setTitreTiers("0");
                            JadeThread.logInfo("INFO", "Le titre du tiers ne correspond pas au sexe défini pour "
                                    + personneEtendue.getTiers().getDesignation1() + " "
                                    + personneEtendue.getTiers().getDesignation2() + "!");
                        }
                    }
                }

                // Type tiers
                // Update type tiers
                if (!IConstantes.CS_TIERS_TYPE_TIERS.equals(personneEtendue.getTiers().getTypeTiers())) {
                    personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                    personneEtendueNeedUpdate = true;
                    personneEtendueModifications.add("Type tiers");
                }

                if (personneEtendueNeedUpdate) {
                    try {
                        personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                        personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
                        JadeThread.logInfo("INFO", "Tiers existant mis à jour !;");
                        JadeThread.logInfo("INFO",
                                "[T] " + personneEtendue.getTiers().getDesignation1() + " "
                                        + personneEtendue.getTiers().getDesignation2() + " : "
                                        + personneEtendueModifications.toString() + ";");

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        personneEtendue = null;
                    }
                }
            } else if ((tiersSearch != null) && (tiersSearch.getSize() > 1)) {
                personneEtendue = null;
                JadeThread.logError("ERREUR", "Plusieurs tiers ont été trouvé pour le contribuable ==> "
                        + getPersonneObjectInfos(typePersonne) + ";");
            } else if (tiersSearch == null) {
                // -----------------------------------------------------------------------------------------
                // Création du tiers car pas trouvé
                // -----------------------------------------------------------------------------------------
                try {
                    personneEtendue.getPersonneEtendue().setNumAvsActuel(nnss);
                    if (!JadeStringUtil.isBlankOrZero(dateNaissance) && isValideDate(dateNaissance)) {
                        personneEtendue.getPersonne().setDateNaissance(dateNaissance);
                    }
                    if (!JadeStringUtil.isBlankOrZero(dateDeces) && isValideDate(dateDeces)) {
                        personneEtendue.getPersonne().setDateDeces(dateDeces);
                    }
                    personneEtendue.getTiers().setDesignation1(nom);
                    personneEtendue.getTiers().setDesignation2(prenom);
                    personneEtendue.getTiers().setDesignation3("");
                    personneEtendue.getTiers().setDesignation4("");
                    personneEtendue.getTiers().setDesignationUpper1(nom.toUpperCase());
                    personneEtendue.getTiers().setDesignationUpper2(prenom.toUpperCase());
                    personneEtendue.getTiers().setTitreTiers(titre);
                    personneEtendue.getTiers().setPersonnePhysique(true);
                    personneEtendue.getPersonne().setSexe(gender);
                    personneEtendue.getPersonne().setEtatCivil(etatCivil);
                    personneEtendue.getPersonneEtendue().setNumContribuableActuel(formatNoContribuable(noContribuable));
                    personneEtendue.getTiers().setLangue(IConstantes.CS_TIERS_LANGUE_FRANCAIS);
                    personneEtendue.getTiers().setIdPays("100");
                    personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                    personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().create(personneEtendue);
                    JadeThread.logInfo("INFO", "Le tiers " + personneEtendue.getTiers().getIdTiers() + " : "
                            + personneEtendue.getTiers().getDesignation1() + " "
                            + personneEtendue.getTiers().getDesignation2() + " a été crée;");
                } catch (Exception ex) {
                    JadeThread.logError("ERROR",
                            "Erreur création tiers " + nom + " " + prenom + " ==> " + ex.getMessage());
                    ex.printStackTrace();
                    personneEtendue = null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            personneEtendue = null;
        }

        // -----------------------------------------------------------------------------------------
        // Update rôle
        // -----------------------------------------------------------------------------------------
        if (!(personneEtendue == null)) {
            try {
                if (!TIBusinessServiceLocator.getRoleService().hasRole(personneEtendue.getTiers().getIdTiers(),
                        AMGestionTiers.CS_ROLE_AMAL)) {
                    TIBusinessServiceLocator.getRoleService().beginRole(personneEtendue.getTiers().getIdTiers(),
                            AMGestionTiers.CS_ROLE_AMAL);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                personneEtendue = null;
            }
        }
        return personneEtendue;
    }

    /**
     * Retourne le tiers pour la personne à charge
     * 
     * @param personneACharge
     * @return
     */
    private PersonneEtendueComplexModel creationContribuablePersonneACharge(
            ch.globaz.amal.xmlns.am_0002._1.Personne personneACharge) {
        String nom = personneACharge.getNom();
        String prenom = personneACharge.getPrenom();
        String dateNaissance = personneACharge.getDateNaiss();
        String dateDeces = personneACharge.getDateDeces();
        String noAVS = getCurrentValidFormattedNSS(personneACharge.getNavs13().toString());
        if (JadeStringUtil.isEmpty(noAVS)) {
            noAVS = "";
        }
        String gender = getSexeCS(personneACharge.getSexe().toString());
        String titre = "";
        if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(gender)) {
            titre = "502001";
        } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(gender)) {
            titre = "502002";
        }

        if (!JadeStringUtil.isBlankOrZero(nom) && !JadeStringUtil.isBlankOrZero(prenom)
                && !JadeStringUtil.isBlankOrZero(dateNaissance)) {
            return createTiers(null, null, nom, prenom, dateNaissance, dateDeces, noAVS, gender, null, titre);
        } else {
            return null;
        }
    }

    /**
     * Retourne le tiers pour le conjoint
     * 
     * @return
     */
    private PersonneEtendueComplexModel creationTiersConjoint() {
        // -----------------------------------------------------------------------------------------
        // Get nom - prenom, datenaissance, noAVS depuis le fichier XML
        // -----------------------------------------------------------------------------------------
        String nom = currentContribuableConjoint.getNom();
        String prenom = currentContribuableConjoint.getPrenom();
        String dateNaissance = currentContribuableConjoint.getDateNaiss();
        String dateDeces = currentContribuableConjoint.getDateDeces();
        String noAVS = getCurrentValidFormattedNSS(currentContribuableConjoint.getNavs13().toString());
        String noContribuableConjoint = currentContribuableConjoint.getNdcConj();
        if (JadeStringUtil.isEmpty(noAVS)) {
            noAVS = "";
        }
        String gender = getSexeCS(currentContribuableConjoint.getSexe().toString());
        String titre = "";
        if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(gender)) {
            titre = "502001";
        } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(gender)) {
            titre = "502002";
        }
        String etatCivil = getEtatCivilCS(currentContribuablePrincipal.getEcPers().toString());
        return createTiers(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT, noContribuableConjoint, nom, prenom,
                dateNaissance, dateDeces, noAVS, gender, etatCivil, titre);
    }

    /**
     * Retourne le tiers pour le contribuable principal
     * 
     * @return
     */
    private PersonneEtendueComplexModel creationTiersContribuablePrincipal() {
        // Get nom - prenom, datenaissance, noAVS
        String noContribuable = currentNoContribuable;
        String nom = currentContribuablePrincipal.getNom();
        String prenom = currentContribuablePrincipal.getPrenom();
        String dateNaissance = currentContribuablePrincipal.getDateNaiss();
        String dateDeces = currentContribuablePrincipal.getDateDeces();
        String noAVS = getCurrentValidFormattedNSS(currentContribuablePrincipal.getNavs13().toString());
        if (JadeStringUtil.isEmpty(noAVS)) {
            noAVS = "";
        }
        String gender = getSexeCS(currentContribuablePrincipal.getSexe().toString());
        String titre = "";
        if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(gender)) {
            titre = "502001";
        } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(gender)) {
            titre = "502002";
        }

        String etatCivil = currentContribuablePrincipal.getEcPers().toString();
        etatCivil = getEtatCivilCS(etatCivil);

        return createTiers(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL, noContribuable, nom, prenom,
                dateNaissance, dateDeces, noAVS, gender, etatCivil, titre);
    }

    private void disableMembre(FamilleContribuable membre, String date, boolean dateFinAnnee,
            AMCodeTraitementDossierFamille codeTraitement) {
        try {
            String dateFin = "";
            if (dateFinAnnee) {
                JADate dDeces = new JADate(date);
                dateFin = "12." + dDeces.getYear();
            } else {
                JADate dDeces = new JADate(date);
                dateFin = JadeStringUtil.fillWithZeroes(String.valueOf(dDeces.getMonth()), 2) + "." + dDeces.getYear();
            }

            if (!membre.isNew() && JadeStringUtil.isBlankOrZero(membre.getSimpleFamille().getCodeTraitementDossier())) {
                membre.getSimpleFamille().setFinDefinitive(dateFin);
                membre.getSimpleFamille().setCodeTraitementDossier(codeTraitement.getValue());

                if (codeTraitement.equals(AMCodeTraitementDossierFamille.DECES)) {
                    // Mise à jour de la date de décès dans Pyxis si pas encore de date
                    if (!(membre.getPersonneEtendue() == null) && !(membre.getPersonneEtendue().isNew())) {
                        if (!isValideDate(membre.getPersonneEtendue().getPersonne().getDateDeces())) {
                            membre.getPersonneEtendue().getPersonne().setDateDeces(date);
                            PersonneEtendueComplexModel personneEtendue = TIBusinessServiceLocator
                                    .getPersonneEtendueService().update(membre.getPersonneEtendue());
                            JadeThread.logInfo("INFO", membre.getSimpleFamille().getNomPrenom() + " : PYXIS "
                                    + codeTraitement.toString());
                        }
                    }
                }

                membre = AmalServiceLocator.getFamilleContribuableService().update(membre);
                JadeThread
                        .logInfo("INFO", membre.getSimpleFamille().getNomPrenom() + " : " + codeTraitement.toString());
            }

        } catch (Exception e) {
            JadeThread.logError("ERROR", membre.getSimpleFamille().getNomPrenom()
                    + " : Erreur mise à jour code traitement membre famille ! ==> " + e.getMessage());
        }
    }

    /**
     * Méthode qui recherche un membre famille par différents critères :
     * <ul>
     * <li>idTiers</li>
     * <li>noAVS</li>
     * <li>nom et prénom</li>
     * </ul>
     * 
     * Si idTiers non trouvé, alors on cherche par noAVS, puis par nom,prénom
     * 
     * @param searchPersonneCharge
     * @param tiersPersonneCharge
     * @return
     * @throws FamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private SimpleFamilleSearch findMembreFamilleByCriters(SimpleFamilleSearch searchPersonneCharge,
            PersonneEtendueComplexModel tiersPersonneCharge) throws FamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // -----------------------------------------------------------------------------------------
        // On commence par rechercher par idTiers
        // -----------------------------------------------------------------------------------------
        searchPersonneCharge.setForIdTiers(tiersPersonneCharge.getTiers().getIdTiers());
        searchPersonneCharge = AmalServiceLocator.getFamilleContribuableService().search(searchPersonneCharge);

        // -----------------------------------------------------------------------------------------
        // Si rien trouvé avec idTiers, on essaie avec le no AVS...
        // -----------------------------------------------------------------------------------------
        if (searchPersonneCharge.getSize() == 0) {
            searchPersonneCharge.setForIdTiers(null);
            if (!JadeStringUtil.isBlankOrZero(tiersPersonneCharge.getPersonneEtendue().getNumAvsActuel())) {
                String noAVSWithoutPoint = JadeStringUtil.change(tiersPersonneCharge.getPersonneEtendue()
                        .getNumAvsActuel(), ".", "");
                searchPersonneCharge.setLikeNoAVS(noAVSWithoutPoint);
                searchPersonneCharge = AmalServiceLocator.getFamilleContribuableService().search(searchPersonneCharge);
            }
        }

        // -----------------------------------------------------------------------------------------
        // Si rien trouvé avec le no AVS, on tente avec le nom et prénom...
        // -----------------------------------------------------------------------------------------
        if (searchPersonneCharge.getSize() == 0) {
            searchPersonneCharge.setLikeNoAVS(null);
            searchPersonneCharge.setLikeNomPrenom(tiersPersonneCharge.getTiers().getDesignationUpper1() + " "
                    + tiersPersonneCharge.getTiers().getDesignationUpper2());
            searchPersonneCharge = AmalServiceLocator.getFamilleContribuableService().search(searchPersonneCharge);
        }

        // -----------------------------------------------------------------------------------------
        // Tests sur une partie du prénom
        // -----------------------------------------------------------------------------------------
        if (searchPersonneCharge.getSize() == 0) {
            if (!JadeStringUtil.isBlank(tiersPersonneCharge.getTiers().getDesignationUpper2())) {
                searchPersonneCharge.setLikeNomPrenom(null);
                searchPersonneCharge.setLikeNomPrenom(tiersPersonneCharge.getTiers().getDesignationUpper2());
                searchPersonneCharge = AmalServiceLocator.getFamilleContribuableService().search(searchPersonneCharge);
            }
        }

        // -----------------------------------------------------------------------------------------
        // en désespoire de cause, on tente encore la date de naissance...
        // -----------------------------------------------------------------------------------------
        if (searchPersonneCharge.getSize() == 0) {
            searchPersonneCharge.setLikeNomPrenom(null);
            searchPersonneCharge.setForDateNaissance(tiersPersonneCharge.getPersonne().getDateNaissance());
            searchPersonneCharge = AmalServiceLocator.getFamilleContribuableService().search(searchPersonneCharge);
            if (searchPersonneCharge.getSize() > 1) {
                // Jumeaux, triplés,... on test encore le prénom
                if (!JadeStringUtil.isBlank(tiersPersonneCharge.getTiers().getDesignationUpper2())) {
                    searchPersonneCharge.setLikeNomPrenom(tiersPersonneCharge.getTiers().getDesignationUpper2());
                    searchPersonneCharge = AmalServiceLocator.getFamilleContribuableService().search(
                            searchPersonneCharge);
                }
            }
        }
        return searchPersonneCharge;
    }

    /**
     * format le numéro au format : xxx.xxx.xxx.xx
     */
    private String formatNoContribuable(String val) {
        if (val == null) {
            return "";
        }

        String str = "";
        for (int i = 0; i < val.length(); i++) {
            str += val.charAt(i);

            switch (i) {
                case 2:
                case 5:
                case 8:
                    str += ".";
                    break;
            }
        }

        return str;
    }

    /**
     * Recherche du contribuable dans les dossiers actifs
     * 
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private ContribuableRCListe getContribuable(String searchedNoContribuable, String searchedNSS,
            String searchedFamilyName, String searchedGivenName, String searchedDateOfBirth)
            throws JadeApplicationServiceNotAvailableException {

        ContribuableRCListe currentContribuableRCListe = null;
        ContribuableRCListeSearch currentContribuableRCListeSearch = new ContribuableRCListeSearch();

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoContribuable = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByNSS(
                    currentContribuableRCListeSearch, searchedNSS);
            if (currentContribuableRCListeSearch.getSize() == 1) {
                bSearchByNoContribuable = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoContribuable = false;
                bSearchByName = false;
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByDateOfBirth(
                        currentContribuableRCListeSearch, searchedDateOfBirth);
                if (currentContribuableRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableRCListeSearch);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }

        // Recherche par N° contribuable, puis date de naissance
        if (bSearchByNoContribuable && !JadeStringUtil.isBlankOrZero(searchedNoContribuable)) {
            JadeLogger.info(null, "RECHERCHE PAR NO CONTRIBUABLE : " + searchedNoContribuable);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByNoContribuable(
                    currentContribuableRCListeSearch, searchedNoContribuable);
            if (currentContribuableRCListeSearch.getSize() == 1) {
                // bSearchByNoNSS = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                // bSearchByNoNSS = false;
                bSearchByName = false;
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByDateOfBirth(
                        currentContribuableRCListeSearch, searchedDateOfBirth);
                if (currentContribuableRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableRCListeSearch);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByDateOfBirth(
                    currentContribuableRCListeSearch, searchedDateOfBirth);
            if (currentContribuableRCListeSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM : " + searchedFamilyName);
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByFamilyName(
                        currentContribuableRCListeSearch, searchedFamilyName);
                if (currentContribuableRCListeSearch.getSize() >= 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR PRENOM : " + searchedGivenName);
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierByGivenName(currentContribuableRCListeSearch, searchedGivenName);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    } else if (currentContribuableRCListeSearch.getSize() > 1) {
                        JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                        currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                                .getDossierLastSubside(currentContribuableRCListeSearch);
                        if (currentContribuableRCListeSearch.getSize() == 1) {
                            bFound = true;
                        }
                    }
                }
            }
        }

        if (bFound) {
            currentContribuableRCListe = (ContribuableRCListe) currentContribuableRCListeSearch.getSearchResults()[0];
            String idContribuable = currentContribuableRCListe.getIdContribuable();
            JadeLogger.info(null, "FOUND : " + idContribuable);
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            try {
                return currentContribuableRCListe;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            JadeLogger.info(null, "DOSSIER ACTIF NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }
    }

    /**
     * Recherche du contribuable dans les dossiers historiques
     * 
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private SimpleContribuableInfos getContribuableHistorique(String searchedNoContribuable, String searchedNSS,
            String searchedFamilyName, String searchedGivenName, String searchedDateOfBirth)
            throws JadeApplicationServiceNotAvailableException {

        ContribuableHistoriqueRCListe currentContribuableHistoriqueRCListe = null;
        ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoContribuable = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche par No contribuable, puis date de naissance
        if (bSearchByNoContribuable && !JadeStringUtil.isBlankOrZero(searchedNoContribuable)) {
            JadeLogger.info(null, "RECHERCHE PAR NO CONTRIBUABLE : " + searchedNoContribuable);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByNoContribuable(currentContribuableHistoriqueRCListeSearch, searchedNoContribuable);
            if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                bSearchByNoContribuable = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoContribuable = false;
                bSearchByName = false;
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
                if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByNSS(
                    currentContribuableHistoriqueRCListeSearch, searchedNSS);
            if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByName = false;
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
                if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
            if (currentContribuableHistoriqueRCListeSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM : " + searchedFamilyName);
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByFamilyName(currentContribuableHistoriqueRCListeSearch, searchedFamilyName);
                if (currentContribuableHistoriqueRCListeSearch.getSize() >= 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR PRENOM : " + searchedGivenName);
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierByGivenName(currentContribuableHistoriqueRCListeSearch, searchedGivenName);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                        JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                        currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                                .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                        if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                            bFound = true;
                        }
                    }
                }
            }
        }

        if (bFound) {
            currentContribuableHistoriqueRCListe = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                    .getSearchResults()[0];
            String idContribuable = currentContribuableHistoriqueRCListe.getIdContribuableInfo();
            JadeLogger.info(null, "FOUND HISTORIQUE : " + idContribuable);
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            try {
                return AmalServiceLocator.getContribuableService().readInfos(idContribuable);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            JadeLogger.info(null, "DOSSIER HISTORIQUE NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }

    }

    /**
     * Récupération du nss formatté
     * 
     * @return
     */
    private String getCurrentValidFormattedNSS(String val) {
        String currentNSS = "";
        // ------------------------------------------------------------------------------------
        // Récupération du noAVS
        // ------------------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(val)) {
            try {
                String nssFormatted = TINSSFormater.format(val, TINSSFormater.findType(val));
                currentNSS = nssFormatted;
            } catch (Exception e) {
                currentNSS = "";
            }
        } else {
            currentNSS = "";
        }
        return currentNSS;
    }

    /**
     * Retourne le CS de l'état civil selon le code du fichier XML
     * 
     * @param etatCivil
     * @return
     */
    private String getEtatCivilCS(String etatCivil) {
        if (etatCivil.equals("1")) {
            etatCivil = "515001";
        } else if (etatCivil.equals("2")) {
            etatCivil = "515002";
        } else if (etatCivil.equals("3")) {
            etatCivil = "515004";
        } else if (etatCivil.equals("4")) {
            etatCivil = "515003";
        } else if (etatCivil.equals("5")) {
            etatCivil = "515005";
        } else {
            etatCivil = "0";
        }
        return etatCivil;
    }

    /**
     * Retourne un code PERE pour un homme, MERE pour une femme
     * 
     * @param sexe
     * @return
     */
    private String getPereMereSelonSexe(String sexe) {
        if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(sexe)) {
            return IAMCodeSysteme.CS_TYPE_PERE;
        } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(sexe)) {
            return IAMCodeSysteme.CS_TYPE_MERE;
        } else {
            return "";
        }
    }

    /**
     * Retourne des infos concernant la personne à charge
     * 
     * @param personneACharge
     * @return
     */
    private String getPersonneChargeObjectInfos(ch.globaz.amal.xmlns.am_0002._1.Personne personneACharge) {
        String info = "Contribuable : " + formatNoContribuable(currentNoContribuable) + "/" + personneACharge.getNom()
                + "/" + personneACharge.getPrenom() + "/" + personneACharge.getNip() + "/"
                + personneACharge.getNavs13().toString();

        return info;
    }

    /**
     * Donne des infos concernant l'objet &lt;Personne&gt; en cours
     * 
     * @return
     */
    private String getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne typePersonne) {
        // 0 Contribuable
        // 1 Conjoint
        // 2 Personne à charge

        int type = 0;

        if (typePersonne == null) {
            type = 2;
        } else if (typePersonne.equals(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT)) {
            type = 1;
        } else if (typePersonne.equals(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL)) {
            type = 0;
        }

        String info = "";
        switch (type) {
            case 0:
                info += formatNoContribuable(currentNoContribuable) + "/" + currentContribuablePrincipal.getNom() + "/"
                        + currentContribuablePrincipal.getPrenom() + "/" + currentContribuablePrincipal.getNip() + "/"
                        + currentContribuablePrincipal.getNavs13().toString();
                break;
            case 1:
                info += formatNoContribuable(currentNoContribuable) + "/" + currentContribuableConjoint.getNom() + "/"
                        + currentContribuableConjoint.getPrenom() + "/" + currentContribuableConjoint.getNip() + "/"
                        + currentContribuableConjoint.getNavs13().toString();
                break;
            case 2:
                info += formatNoContribuable(currentNoContribuable) + "/" + currentContribuablePrincipal.getNom() + "/"
                        + currentContribuablePrincipal.getPrenom() + "/" + currentContribuablePrincipal.getNip();
                break;
            default:
                info += formatNoContribuable(currentNoContribuable) + "/" + currentContribuablePrincipal.getNom() + "/"
                        + currentContribuablePrincipal.getPrenom() + "/" + currentContribuablePrincipal.getNip();
                break;
        }

        return info;

    }

    /**
     * Transforme un code provenant du fichier XML en CS pour la politesse
     * 
     * @param politesse
     * @return
     */
    private String getPolitesseCS(String politesse) {
        if (JadeStringUtil.isEmpty(politesse)) {
            return "";
        }
        String politesseUpper = JadeStringUtil.toUpperCase(politesse);

        if (politesseUpper.equals("MONSIEUR")) {
            return "502001";
        } else if (politesseUpper.equals("MADAME")) {
            return "502002";
        } else if (politesseUpper.equals("MESSIEURS")) {
            return "19150009";
        } else if (politesseUpper.equals("MESDAMES")) {
            return "19150008";
        } else if (politesseUpper.equals("HOIRIE")) {
            return "502004";
        } else if (politesseUpper.equals("MADAME ET MONSIEUR") || politesseUpper.equals("MONSIEUR ET MADAME")) {
            // Modification du 15.10.2012 : Si l'on a le titre "Madame et Monsieur" (ou inversement), on set "Monsieur"
            // ou "Madame" selon le sexe du contribuable principal
            if ("2".equals(currentContribuablePrincipal.getSexe())) {
                // Femme --> return "Madame"
                return "502002";
            } else {
                // Sinon return "Monsieur"
                return "502001";
            }
        } else if (politesseUpper.equals("MAITRE")) {
            return "19150016";
        } else if (politesseUpper.equals("BUREAU")) {
            return "19150017";
        } else if (politesseUpper.equals("FIDUCIAIRE")) {
            return "19150019";
        } else if (politesseUpper.equals("FONDATION")) {
            return "19150025";
        } else if (politesseUpper.equals("MADEMOISELLE")) {
            return "19150007";
        } else {
            return "";
        }
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        if (session == null) {
            session = SessionProvider.findSession();
        }
        return session;
    }

    /**
     * Transforme un code provenant du fichier XML en CS pour le sexe
     * 
     * @param sexe
     * @return
     */
    private String getSexeCS(String sexe) {
        if (sexe.equals("1")) {
            return IConstantes.CS_PERSONNE_SEXE_HOMME;
        } else if (sexe.equals("2")) {
            return IConstantes.CS_PERSONNE_SEXE_FEMME;
        } else {
            return "0";
        }
    }

    /**
     * Vérifie si le thread contient des erreurs dans la session ou dans la transaction
     * 
     * @return True si erreur(s) dans la session ou dans la transaction
     */
    private boolean hasErrors() {
        if (getSession().hasErrors()) {
            JadeThread.logError("ERROR", getSession().getErrors().toString());
            return true;
        } else if (getSession().getCurrentThreadTransaction().hasErrors()) {
            JadeThread.logError("ERROR", getSession().getCurrentThreadTransaction().getErrors().toString());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Traitement pour l'enregistrement de l'historique des no de contribuables
     * 
     * @param famille
     * @param contribuable
     * @param gestionTiers
     * @param personneEtendue
     * @return true si tout s'est bien passée
     */
    private boolean historisationNoContribuable(PersonneEtendueComplexModel personneEtendue) {
        boolean processOk = true;
        ArrayList<String> allNumbers = new ArrayList<String>();

        // Info no contribuable sous :
        allNumbers.add(formatNoContribuable(currentNoContribuable));
        allNumbers.add(formatNoContribuable(currentNoContribuablePrecedent));

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
                        try {
                            entity.delete(transaction);
                            session.getCurrentThreadTransaction().getConnection().commit();
                        } catch (Exception ex) {
                            processOk = false;
                            JadeLogger.error(this,
                                    "Error deleting no contribuable " + AMRepriseTiersProcess.stack2string(ex));
                            JadeThread.logClear();
                            session.getCurrentThreadTransaction().clearErrorBuffer();
                            session.getCurrentThreadTransaction().clearWarningBuffer();
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
                    currentHisto.setEntreeVigueur("11.11.2011");
                    currentHisto.setFinVigueur("11.12.2011");
                    currentHisto.setIdTiers(personneEtendue.getTiers().getIdTiers());
                    currentHisto.setMotifHistorique(TIHistoriqueContribuable.CS_CREATION);
                    currentHisto.setNumContribuable(currentNumber.trim());
                    JadeLogger.info(this, "Saving histo no contribuable (" + currentNumber.trim() + ", "
                            + personneEtendue.getTiers().getDesignation1() + ","
                            + personneEtendue.getTiers().getDesignation2() + ")");
                    try {
                        currentHisto.save();
                        session.getCurrentThreadTransaction().getConnection().commit();
                    } catch (Exception ex) {
                        processOk = false;
                        JadeLogger.error(this, "Error saving histo no contribuable (" + currentNumber.trim() + ", "
                                + personneEtendue.getTiers().getDesignation1() + ","
                                + personneEtendue.getTiers().getDesignation2() + ") : " + ex.toString());
                        ex.printStackTrace();
                        JadeThread.logClear();
                        session.getCurrentThreadTransaction().clearErrorBuffer();
                        session.getCurrentThreadTransaction().clearWarningBuffer();
                    }
                }
            } catch (Exception e) {
                processOk = false;
                JadeLogger.error(this, "Error searching histo no contribuable (" + currentNumber.trim() + ", "
                        + personneEtendue.getTiers().getDesignation1() + ","
                        + personneEtendue.getTiers().getDesignation2() + ") : " + e.toString());
                e.printStackTrace();
            }

        }

        return processOk;

    }

    /**
     * Contrôle si l'adresse actuelle et celle du fichier xml sont identiques
     * 
     * @param rueXml
     * @param rueNpaXml
     * @param currentAdresse
     * @return
     * @throws JadeNoBusinessLogSessionError
     */
    @SuppressWarnings("unchecked")
    private boolean isSameAdresse(String rueXml, String rueNpaXml, AdresseTiersDetail currentAdresse)
            throws JadeNoBusinessLogSessionError {
        String idAdresse = "";
        try {
            idAdresse = currentAdresse.getFields().get(AdresseTiersDetail.ADRESSE_ID_ADRESSE);
            TIAdresse adresse = new TIAdresse();
            adresse.setSession(getSession());
            adresse.setId(idAdresse);
            adresse.retrieve();

            if (!adresse.isNew()) {
                TILocalite loca = new TILocalite();
                loca.setSession(getSession());
                loca.setId(adresse.getIdLocalite());
                loca.retrieve();
                String locaNpa = "";
                if (!loca.isNew()) {
                    locaNpa = loca.getNumPostal();
                }

                String pyxisRue = adresse.getRue();
                String pyxisNoRue = adresse.getNumeroRue();

                if (locaNpa.equals(rueNpaXml)) {
                    if ((rueXml == null) || JadeStringUtil.isNull(rueXml)) {
                        return false;
                    }

                    List<String> rueList = JadeStringUtil.tokenize(rueXml, ",");
                    if (rueList.size() <= 1) {
                        // Certaines adresses sont séparés par des "." au lieu de virgules...
                        rueList = JadeStringUtil.tokenize(rueXml, ".");
                    }

                    String xmlRueNom = "";
                    String xmlRueNumero = "";
                    // Si list == 1, on n'a pas de numéro de rue
                    if (rueList.size() == 1) {
                        xmlRueNom = rueList.get(0);
                        xmlRueNumero = "";
                    } else {
                        xmlRueNumero = rueList.get(0);
                        xmlRueNom = rueList.get(1);
                    }

                    xmlRueNom = JadeStringUtil.stripBlanks(xmlRueNom);
                    xmlRueNom = regularizeRue(xmlRueNom).toUpperCase();
                    xmlRueNom = JadeStringUtil.change(xmlRueNom, "-", " ");
                    xmlRueNom = JadeStringUtil.convertSpecialChars(xmlRueNom);

                    pyxisRue = JadeStringUtil.stripBlanks(pyxisRue);
                    pyxisRue = regularizeRue(pyxisRue).toUpperCase();
                    pyxisRue = JadeStringUtil.change(pyxisRue, "-", " ");
                    pyxisRue = JadeStringUtil.convertSpecialChars(pyxisRue);

                    if (xmlRueNumero.equalsIgnoreCase(pyxisNoRue)) {
                        if (xmlRueNom.equalsIgnoreCase(pyxisRue)) {
                            // 2 noms de rues sont pareils
                            return true;
                        } else if (JadeStringUtil.contains(xmlRueNom, pyxisRue)
                                || JadeStringUtil.contains(pyxisRue, xmlRueNom)) {
                            // Si la rue du fichier xml est contenu dans la rue Pyxis ou inversement
                            return true;
                        }
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Vérifie si une date est bien formée et valide
     * 
     * @param date
     * @return true si date valide, sinon false
     */
    private boolean isValideDate(String date) {
        boolean isValid = true;
        try {
            if (!JadeDateUtil.isGlobazDate(date)) {
                return false;
            }

            JADate d = new JADate(date);

            if ((d.getDay() < 1) || (d.getDay() > 31)) {
                isValid = false;
            }

            if ((d.getMonth() < 1) || (d.getMonth() > 12)) {
                isValid = false;
            }

            if ((d.getYear() < 1900) || (d.getYear() > 2999)) {
                isValid = false;
            }
        } catch (Exception e) {
            JadeThread.logError("ERREUR", "Date invalide '" + date + "' : " + e.getMessage());
            return false;
        }

        if (!isValid) {
            JadeThread.logError("ERREUR", "Date invalide '" + date + "'");
        }

        return isValid;
    }

    /**
     * Vérifie que le membre ne possède pas de subside (autre que ATENF1 et ATENF8)
     * 
     * @param sfEnfantVide
     * @return true si l'enfant a déjà eu une attribution
     */
    private boolean membreHasSubside(SimpleFamille sfEnfantVide) {
        try {
            FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
            familleContribuableSearch.setForIdContribuable(sfEnfantVide.getIdContribuable());
            familleContribuableSearch.setForIdFamille(sfEnfantVide.getIdFamille());
            familleContribuableSearch.setForAnneeHistorique(yearSubside);
            familleContribuableSearch.setForFinDroit("0");
            familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                    familleContribuableSearch);

            if (familleContribuableSearch.getSize() > 0) {
                FamilleContribuable familleContribuable = (FamilleContribuable) familleContribuableSearch
                        .getSearchResults()[0];

                String model = familleContribuable.getSimpleDetailFamille().getNoModeles();

                if (model.equals(IAMCodeSysteme.AMDocumentModeles.ATENF1.getValue())
                        || model.equals(IAMCodeSysteme.AMDocumentModeles.ATENF8.getValue())) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            JadeThread.logWarn("WARN", "Erreur lors de la recherche d'un subside de l'année : " + yearSubside
                    + " pour le membre :" + sfEnfantVide.getNomPrenom());
            return true;
        }
        return false;
    }

    /**
     * Recherche du contribuable
     * 
     * @param _tiersPrincipal
     * 
     * @return
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws ContribuableException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private String rechercheContribuable() throws JadeNoBusinessLogSessionError, JadePersistenceException,
            ContribuableException, JadeApplicationServiceNotAvailableException {
        String searchedNSS = getCurrentValidFormattedNSS(currentContribuablePrincipal.getNavs13().toString());
        String searchedNoContribuable = formatNoContribuable(currentNoContribuable);
        String searchedNoContribuablePrecedent = formatNoContribuable(currentNoContribuablePrecedent);
        String searchedFamilyName = currentContribuablePrincipal.getNom();
        String searchedGivenName = currentContribuablePrincipal.getPrenom();
        String searchedDateOfBirth = currentContribuablePrincipal.getDateNaiss();

        ContribuableRCListe currentContribuable = null;
        SimpleContribuableInfos currentContribuableHistorique = null;
        String idContribuableDossier = null;
        dossierHistorique = false;
        // ---------------------------------------------------------------------------------------------
        // 1 - RECHERCHE DOSSIER ACTIF - CONTRIBUABLES STANDARDS
        // ---------------------------------------------------------------------------------------------
        try {
            currentContribuable = getContribuable(searchedNoContribuable, searchedNSS, searchedFamilyName,
                    searchedGivenName, searchedDateOfBirth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------------
        // 1 - RECHERCHE DOSSIER ACTIF - CONTRIBUABLES STANDARDS AVEC NO CONTRIBUABLE PRECEDENT (SI DISPO)
        // ---------------------------------------------------------------------------------------------
        try {
            if ((currentContribuable == null) && !JadeStringUtil.isBlankOrZero(searchedNoContribuablePrecedent)) {
                currentContribuable = getContribuable(searchedNoContribuablePrecedent, searchedNSS, searchedFamilyName,
                        searchedGivenName, searchedDateOfBirth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------------
        // 2 - RECHERCHE DOSSIER HISTORIQUE - CONTRIBUABLES STANDARDS
        // ---------------------------------------------------------------------------------------------
        try {
            if (currentContribuable == null) {
                currentContribuableHistorique = getContribuableHistorique(searchedNoContribuable, searchedNSS,
                        searchedFamilyName, searchedGivenName, searchedDateOfBirth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------------
        // 4 - CONCLUSION DOSSIER TROUVE OU NON
        // ---------------------------------------------------------------------------------------------
        if ((currentContribuable == null) && (currentContribuableHistorique == null)) {
            JadeThread.logInfo("INFO", "DOSSIER NON TROUVE ==>"
                    + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + ";");
        } else {
            if (currentContribuable != null) {
                idContribuableDossier = currentContribuable.getId();
                JadeThread.logInfo("INFO", "DOSSIER TROUVE : " + currentContribuable.getId() + " ==> "
                        + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + ";");
            } else if (currentContribuableHistorique != null) {
                dossierHistorique = true;
                idContribuableDossier = currentContribuableHistorique.getId();
                JadeThread.logInfo("INFO", "DOSSIER HISTORIQUE TROUVE : " + currentContribuableHistorique.getId()
                        + " ==> " + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL)
                        + ";");
            }
        }

        if (dossierHistorique) {
            try {
                tiersPrincipalGlobal = creationTiersContribuablePrincipal();

                if (tiersPrincipalGlobal != null) {
                    SimpleContribuable simpleContribuable = new SimpleContribuable();
                    simpleContribuable.setIdContribuable(idContribuableDossier);
                    simpleContribuable.setNoContribuable(currentNoContribuable);
                    simpleContribuable.setNumeroInternePersonnel(currentContribuablePrincipal.getNip().toString());
                    simpleContribuable.setIdTier(tiersPrincipalGlobal.getTiers().getIdTiers());
                    simpleContribuable.setIsContribuableActif(true);
                    simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().create(
                            simpleContribuable);
                    JadeThread.logInfo("INFO", "Contribuable crée à partir de l'historique !;");

                    SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
                    simpleFamilleSearch.setForIdContribuable(idContribuableDossier);
                    simpleFamilleSearch.setIsContribuable(true);
                    simpleFamilleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(simpleFamilleSearch);

                    if (simpleFamilleSearch.getSize() > 0) {
                        SimpleFamille sfChef = (SimpleFamille) simpleFamilleSearch.getSearchResults()[0];
                        FamilleContribuable chef = AmalServiceLocator.getFamilleContribuableService().read(
                                sfChef.getId());
                        chef.setPersonneEtendue(tiersPrincipalGlobal);
                        updateMissingsInfos(tiersPrincipalGlobal, chef, currentContribuablePrincipal);
                    }
                    AmalImplServiceLocator.getContribuableService().deleteInfo(currentContribuableHistorique);
                    JadeThread.logInfo("INFO", "Contribuable supprimé de l'historique !;");
                    idContribuableDossier = simpleContribuable.getId();
                } else {
                    throw new ContribuableException("Tiers non crée. Abandon du processus !");
                }
            } catch (Exception e) {
                throw new ContribuableException(e.getMessage());
            }
        }

        return idContribuableDossier;
    }

    /**
     * Transformation des "Rte". en "Route", des "Ch." en "Chemin", "Pl." en "Place" et "Imp." en "Impasse" pour les
     * comparaisons sur les noms des rues.
     * 
     * @param val
     * @return
     */
    private String regularizeRue(String val) {
        val = val.toUpperCase();
        if ((JadeStringUtil.indexOf(val, "RTE ") != -1) && (JadeStringUtil.indexOf(val, "RTE ") < 2)) {
            val = JadeStringUtil.change(val, "RTE ", "ROUTE ");
            return val;
        } else if ((JadeStringUtil.indexOf(val, "CH. ") != -1) && (JadeStringUtil.indexOf(val, "CH. ") < 2)) {
            val = JadeStringUtil.change(val, "CH. ", "CHEMIN ");
            return val;
        } else if ((JadeStringUtil.indexOf(val, "PL. ") != -1) && (JadeStringUtil.indexOf(val, "PL. ") < 2)) {
            val = JadeStringUtil.change(val, "PL. ", "PLACE ");
            return val;
        } else if ((JadeStringUtil.indexOf(val, "IMP. ") != -1) && (JadeStringUtil.indexOf(val, "IMP. ") < 2)) {
            val = JadeStringUtil.change(val, "IMP. ", "IMPASSE ");
            return val;
        }
        return val;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        try {
            String idContribuableDossier = rechercheContribuable();
            Contribuable newDossier = new Contribuable();

            // ---------------------------------------------------------------------------------------------
            // 5 - DOSSIER NON TROUVE, CREATION SI CRITERES COMPLETS
            // ---------------------------------------------------------------------------------------------
            if (JadeStringUtil.isBlankOrZero(idContribuableDossier)) {

                if (AMProcessRepriseDecisionsTaxationsEntityHandler.isRepriseAdresses) {
                    JadeThread.logWarn("Warning",
                            "Dossier inexistant ! Pas de création de dossier en mode reprise adresse.");
                    return;
                }

                // a) création tiers
                tiersPrincipalGlobal = creationTiersContribuablePrincipal();
                if (tiersPrincipalGlobal == null) {
                    JadeThread.logError("ERREUR", "DOSSIER NON TROUVE ET ERREUR CREATION TIERS " + " ==> "
                            + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + ";");
                    return;
                }

                // -----------------------------------------------------------------------------------------
                // b) création dossier
                // -----------------------------------------------------------------------------------------
                newDossier.setPersonneEtendueComplexModel(tiersPrincipalGlobal);
                newDossier.getContribuable().setNoContribuable(currentNoContribuable);
                newDossier.getContribuable().setZoneCommuneContribuable(currentNoContribuable);
                newDossier.getContribuable().setZoneCommuneNoContrFormate(formatNoContribuable(currentNoContribuable));
                newDossier.getContribuable()
                        .setNumeroInternePersonnel(currentContribuablePrincipal.getNip().toString());
                newDossier.getContribuable().setIsContribuableActif(true);
                newDossier.getFamille().setNoPersonne(currentContribuablePrincipal.getNip().toString());

                try {
                    newDossier = AmalServiceLocator.getContribuableService().create(newDossier);
                    JadeThread.logInfo("INFO", "Dossier contribuable crée (No contribuable :" + currentNoContribuable
                            + ";");
                } catch (Exception ex) {
                    JadeThread.logError("ERREUR", "DOSSIER NON TROUVE ET ERREUR CREATION DOSSIER " + " ==> "
                            + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + ";");
                    return;
                }

                idContribuableDossier = newDossier.getId();
            } else {
                // -----------------------------------------------------------------------------------------
                // On cherche le tiers spécifié dans le contribuable
                // -----------------------------------------------------------------------------------------
                SimpleContribuable c = AmalImplServiceLocator.getSimpleContribuableService()
                        .read(idContribuableDossier);

                String idTiers = c.getIdTier();

                if (!JadeStringUtil.isBlankOrZero(currentContribuablePrincipal.getNavs13().toString())) {
                    String newIdTiers = checkTiersADouble(c);

                    if (newIdTiers != null) {
                        idTiers = newIdTiers;
                    }
                }

                tiersPrincipalGlobal = TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);

                if (!AMProcessRepriseDecisionsTaxationsEntityHandler.isRepriseAdresses) {
                    if (!checkCorrespondanceWithXmlFile(tiersPrincipalGlobal)) {
                        return;
                    }
                    // -----------------------------------------------------------------------------------------
                    // Recherche du membre famille
                    // -----------------------------------------------------------------------------------------
                    SimpleFamilleSearch membreContriSearch = new SimpleFamilleSearch();
                    membreContriSearch.setForIdTiers(tiersPrincipalGlobal.getTiers().getIdTiers());
                    membreContriSearch.setIsContribuable(true);
                    membreContriSearch = AmalImplServiceLocator.getSimpleFamilleService().search(membreContriSearch);

                    // -----------------------------------------------------------------------------------------
                    // Si on trouve qqch...
                    // -----------------------------------------------------------------------------------------
                    if (membreContriSearch.getSize() > 0) {
                        SimpleFamille membreContri = (SimpleFamille) membreContriSearch.getSearchResults()[0];
                        FamilleContribuable fcMembreContri = AmalServiceLocator.getFamilleContribuableService().read(
                                membreContri.getId());
                        // fcMembreContri.setSimpleFamille(membreContri);
                        updateMissingsInfos(tiersPrincipalGlobal, fcMembreContri, currentContribuablePrincipal);
                        c = AmalImplServiceLocator.getSimpleContribuableService().read(idContribuableDossier);
                    }

                    changementNoContribuable(c);
                }
            }
            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) || hasErrors()) {
                return;
            }

            if (!AMProcessRepriseDecisionsTaxationsEntityHandler.isRepriseAdresses) {
                // -----------------------------------------------------------------------------------------
                // c) création conjoint
                // -----------------------------------------------------------------------------------------
                traitementConjoint(idContribuableDossier);
                if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) || hasErrors()) {
                    return;
                }

                // -----------------------------------------------------------------------------------------
                // d) création des enfants
                // -----------------------------------------------------------------------------------------
                traitementPersonneCharge(idContribuableDossier);
                if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) || hasErrors()) {
                    return;
                }

                // Traitement des codes de fin (décès) des membres familles
                traitementCodesFin(idContribuableDossier);
            }
            // -----------------------------------------------------------------------------------------
            // e) Création de l'adresse
            // -----------------------------------------------------------------------------------------
            traitementAdresse(tiersPrincipalGlobal, idContribuableDossier);
            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) || hasErrors()) {
                return;
            }

            // -----------------------------------------------------------------------------------------
            // f) enregistrement id dossier dans tables upload
            // -----------------------------------------------------------------------------------------
            saveInfosDossier(idContribuableDossier, newDossier);

        } catch (Exception e) {
            this.addError(e);
        } finally {
            currentContribuableAdresse = null;
            currentContribuableConjoint = null;
            currentContribuablePrincipal = null;
            currentEntity = null;
            mapCurrentPersonneChargeTaxation = null;
            JadeLogger.info(null, "-------------------------------");
            JadeLogger.info(null, "[Total Memory: " + Runtime.getRuntime().totalMemory() + "]");
            JadeLogger.info(null, "[Free Memory: " + Runtime.getRuntime().freeMemory() + "]");
            JadeLogger.info(null, "-------------------------------");
        }
    }

    /**
     * Enregistrement de informations utiles aux étapes suivantes
     * 
     * @param idContribuableDossier
     * @param newDossier
     */
    private void saveInfosDossier(String idContribuableDossier, Contribuable newDossier) {
        if (newDossier != null) {
            SimpleUploadFichierReprise fileUploaded = null;
            try {
                fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(currentEntity.getIdRef());
                if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                    String typeDossier = "A";
                    if (dossierHistorique) {
                        typeDossier = "H";
                    }
                    fileUploaded.setCustomValue(idContribuableDossier + ";" + nbEnfants + ";" + nbEnfantsSuspens + ";"
                            + typeDossier);
                    fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().update(fileUploaded);
                    JadeThread.logInfo("INFO", "Informations de reprise mis à jour !;");
                }
            } catch (Exception e) {
                currentContribuables = null;
            }
        }
    }

    /**
     * Récupère un membre famille enfant "vide" (sans nom, prénom, tiers rattaché et sans attribution), ceci afin de
     * pouvoir le remplacer par un enfant que l'on a dans le fichier xml
     * 
     * @param idContribuable
     * @return
     */
    private SimpleFamille searchEnfantVide(String idContribuable) {
        try {
            SimpleFamilleSearch sfEnfantsVideSearch = new SimpleFamilleSearch();
            sfEnfantsVideSearch.setForIdContribuable(idContribuable);
            sfEnfantsVideSearch.setForPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
            sfEnfantsVideSearch = AmalImplServiceLocator.getSimpleFamilleService().search(sfEnfantsVideSearch);

            for (JadeAbstractModel model : sfEnfantsVideSearch.getSearchResults()) {
                SimpleFamille sfEnfantVide = (SimpleFamille) model;
                String nomPrenom = sfEnfantVide.getNomPrenom();
                String dateNaissance = sfEnfantVide.getDateNaissance();
                String nnss = sfEnfantVide.getNnssContribuable();
                String idTiers = sfEnfantVide.getIdTier();

                if (JadeStringUtil.isBlankOrZero(idTiers) && JadeStringUtil.isEmpty(nomPrenom)
                        && JadeStringUtil.isBlankOrZero(dateNaissance) && JadeStringUtil.isBlankOrZero(nnss)) {
                    // On vérifie tout de même qu'il n'ai pas deja eu d'attribution...
                    if (!membreHasSubside(sfEnfantVide)) {
                        return sfEnfantVide;
                    } else {
                        return null;
                    }
                }

            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        currentEntity = entity;
        String idUpload = entity.getIdRef();
        SimpleUploadFichierReprise fileUploaded = null;
        SimpleUploadFichierRepriseSearch fileUploadedPersCharge = null;
        try {
            // -----------------------------------------------------------------------------------------
            // Lecture de la ligne qui contient le fichier
            // -----------------------------------------------------------------------------------------
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(idUpload);
            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                StringBuffer ios = new StringBuffer(new StringBuffer(fileUploaded.getXmlLignes()));
                currentContribuables = (ch.globaz.amal.xmlns.am_0001._1.Contribuables) unmarshaller
                        .unmarshal(new StreamSource(new StringReader(ios.toString())));
                for (ch.globaz.amal.xmlns.am_0001._1.Contribuable c : currentContribuables.getContribuable()) {
                    currentNoContribuable = c.getNdc();
                    currentNoContribuablePrecedent = c.getNdcprecedent();
                    currentContribuableAdresse = c.getAdresse();
                    for (ch.globaz.amal.xmlns.am_0001._1.Personne p : c.getPersonne()) {
                        if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL.equals(p.getType())) {
                            currentContribuablePrincipal = p;
                        } else if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT.equals(p.getType())) {
                            currentContribuableConjoint = p;
                        }
                    }
                    fileUploadedPersCharge = new SimpleUploadFichierRepriseSearch();
                    fileUploadedPersCharge.setForNoContribuable(currentNoContribuable);
                    fileUploadedPersCharge.setLikeTypeReprise("PCHARG");
                    fileUploadedPersCharge.setForIdJob(idJob);
                    fileUploadedPersCharge = AmalServiceLocator.getSimpleUploadFichierService().search(
                            fileUploadedPersCharge);
                    if (fileUploadedPersCharge.getSize() == 1) {
                        fileUploaded = (SimpleUploadFichierReprise) fileUploadedPersCharge.getSearchResults()[0];
                        ios = new StringBuffer(new StringBuffer(fileUploaded.getXmlLignes()));
                        mapCurrentPersonneChargeTaxation = new TreeMap<String, ch.globaz.amal.xmlns.am_0002._1.Taxation>();
                        ch.globaz.amal.xmlns.am_0002._1.Contribuables currentPersonneCharge = (ch.globaz.amal.xmlns.am_0002._1.Contribuables) unmarshaller
                                .unmarshal(new StreamSource(new StringReader(ios.toString())));
                        for (ch.globaz.amal.xmlns.am_0002._1.Taxation personneCharge : currentPersonneCharge
                                .getContribuable().get(0).getTaxations().getTaxation()) {
                            mapCurrentPersonneChargeTaxation
                                    .put(personneCharge.getPeriode().toString(), personneCharge);
                        }
                    } else if (fileUploadedPersCharge.getSize() > 1) {
                        JadeThread
                                .logError(
                                        "ERREUR",
                                        "MULTIPLE CONTRIBUABLE PERSONNE CHARGE;PLUSIEURS PERSONNES A CHARGES TROUVEES AVEC LE MEME NO CONTRIBUABLE ! ==> "
                                                + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL)
                                                + ";");
                    }
                }
            } else {
                JadeThread.logError("ERROR", "Error entity #" + idUpload);
            }
            currentContribuables = null;
        } catch (Exception e) {
            currentContribuables = null;
        }
    }

    @Override
    public void setProperties(Map<Enum<?>, String> hashMap) {
        properties = hashMap;

        try {
            JADate currentDate = new JADate(JACalendar.todayJJsMMsAAAA());
            int yearCurrent = currentDate.getYear();
            int monthCurrent = currentDate.getMonth();
            dateProcess = JadeStringUtil.fillWithZeroes(String.valueOf(monthCurrent), 2) + "." + yearCurrent;
        } catch (JAException e) {
            JadeThread.logError("ERROR", "Error setProperties ! ==> " + e.getMessage());
        }

        yearSubside = properties.get(AMProcessRepriseDecisionsTaxationsEnum.YEAR_SUBSIDE);
        try {
            if (properties.containsKey(AMProcessRepriseDecisionsTaxationsEnum.DATE_LIMITE_ADRESSE)) {
                dateLimiteToUpdateAdresse = properties.get(AMProcessRepriseDecisionsTaxationsEnum.DATE_LIMITE_ADRESSE);
            } else {
                dateLimiteToUpdateAdresse = dateProcess;
            }
        } catch (Exception e) {
            dateLimiteToUpdateAdresse = dateProcess;
        }
        if (properties.containsKey(AMProcessRepriseDecisionsTaxationsEnum.IS_REPRISE_ADRESSE)) {
            AMProcessRepriseDecisionsTaxationsEntityHandler.isRepriseAdresses = true;
        }
    }

    /**
     * Recherche de l'adresse. Création si non trouvé
     * 
     * @param tiersPrincipal
     * @param idContribuableDossier
     */
    private void traitementAdresse(PersonneEtendueComplexModel tiersPrincipal, String idContribuableDossier) {
        try {
            String xmlRue = currentContribuableAdresse.getRue();
            String xmlRueSuite = currentContribuableAdresse.getRueSuite();
            String xmlNpa = currentContribuableAdresse.getNpa();

            AdresseTiersDetail currentAdresseEmpty = new AdresseTiersDetail();
            AdresseTiersDetail currentAdresseStandardCourrier = new AdresseTiersDetail();
            AdresseTiersDetail currentAdresseStandardDomicile = new AdresseTiersDetail();
            // -----------------------------------------------------------------------------------------
            // Recherche de l'adresse courrier domaine AMAL
            // -----------------------------------------------------------------------------------------
            String dateToday = "";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            dateToday = sdf.format(cal.getTime());

            boolean hasAdresseStandardCourrier = false;
            boolean hasAdresseStandardDomicile = false;
            boolean adresseHasRue = false;

            currentAdresseStandardDomicile = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    tiersPrincipal.getTiers().getIdTiers(), false, dateToday, AMGestionTiers.CS_DOMAINE_DEFAUT,
                    AMGestionTiers.CS_TYPE_DOMICILE, null);
            if (currentAdresseStandardDomicile.getFields() != null) {
                hasAdresseStandardDomicile = true;
            }

            currentAdresseStandardCourrier = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    tiersPrincipal.getTiers().getIdTiers(), false, dateToday, AMGestionTiers.CS_DOMAINE_DEFAUT,
                    AMGestionTiers.CS_TYPE_COURRIER, null);
            if (currentAdresseStandardCourrier.getFields() != null) {
                hasAdresseStandardCourrier = true;
            }

            if (JadeStringUtil.isEmpty(xmlRue)) {
                xmlRue = "";
            }

            if (JadeStringUtil.isEmpty(xmlRueSuite)) {
                xmlRueSuite = "";
            }

            boolean hasCP = false;
            boolean hasPA = false;
            String adrRue = "";
            String adrCP = "";
            String adrPA = "";

            if (JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRue.toLowerCase()), "case postale")
                    || JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRue.toLowerCase()), "postfach")) {
                hasCP = true;
                adrCP = xmlRue;
            } else if (JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRue.toLowerCase()), "p/a")
                    || JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRue.toLowerCase()), "c/o")) {
                hasPA = true;
                adrPA = xmlRue;
            } else if (!JadeStringUtil.isEmpty(xmlRue)) {
                adresseHasRue = true;
                adrRue = xmlRue;
            }

            if (JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRueSuite.toLowerCase()), "case postale")
                    || JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRueSuite.toLowerCase()), "postfach")) {
                hasCP = true;
                adrCP = xmlRueSuite;
            } else if (JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRueSuite.toLowerCase()), "p/a")
                    || JadeStringUtil.contains(JadeStringUtil.stripBlanks(xmlRueSuite.toLowerCase()), "c/o")) {
                hasPA = true;
                adrPA = xmlRueSuite;
            } else if (!JadeStringUtil.isEmpty(xmlRueSuite)) {
                adresseHasRue = true;
                adrRue = xmlRueSuite;
            }

            if (!hasAdresseStandardCourrier) {
                if (!hasAdresseStandardDomicile) {
                    if (hasCP) {
                        if (adresseHasRue) {
                            // Création adresse domicile AVEC rue
                            JadeThread.logInfo("INFO", "Aucune adresse trouvée. Création domicile avec rue!");
                            traitementAdresseCreation(AMGestionTiers.CS_TYPE_DOMICILE, tiersPrincipal, adrRue, "", "",
                                    xmlNpa, currentAdresseEmpty);
                            JadeThread.logInfo("INFO", "Adresse domicile crée;");
                        } else {
                            boolean entityOnErrorBefore = JadeThread
                                    .logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                                    || getSession().getCurrentThreadTransaction().hasErrors();

                            if (!entityOnErrorBefore) {
                                // Création adresse domicile SANS rue
                                JadeThread.logInfo("INFO",
                                        "Aucune adresse trouvée. Tentative de création domicile sans rue!");
                                traitementAdresseCreation(AMGestionTiers.CS_TYPE_DOMICILE, tiersPrincipal, "", "", "",
                                        xmlNpa, currentAdresseEmpty);

                                if ((JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) || getSession()
                                        .getCurrentThreadTransaction().hasErrors())) {
                                    getSession().getCurrentThreadTransaction().clearErrorBuffer();
                                    JadeBusinessMessage[] msgs = JadeThread.logMessages();
                                    JadeThread.logClear();
                                    for (JadeBusinessMessage m : msgs) {
                                        if (m.getLevel() == 1) {
                                            JadeThread.logInfo(m.getSource(), m.getMessageId());
                                        } else if (m.getLevel() == 2) {
                                            JadeThread.logWarn(m.getSource(), m.getMessageId());
                                        }
                                    }
                                    JadeThread.logInfo("INFO", "Adresse domicile non crée;");
                                } else {
                                    JadeThread.logInfo("INFO", "Adresse domicile crée;");
                                }
                            }
                        }
                        // Création adresse courrier avec Case postale
                        JadeThread.logInfo("INFO", "Aucune adresse trouvée. Création avec CP!");
                        traitementAdresseCreation(AMGestionTiers.CS_TYPE_COURRIER, tiersPrincipal, "", adrCP, adrPA,
                                xmlNpa, currentAdresseEmpty);
                        JadeThread.logInfo("INFO", "Adresse courrier crée;");
                        AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileNonCPPA++;
                    } else {
                        // Création adresse domicile
                        JadeThread.logInfo("INFO", "Aucune adresse trouvée. Création sans CP !");
                        traitementAdresseCreation(AMGestionTiers.CS_TYPE_DOMICILE, tiersPrincipal, adrRue, adrCP,
                                adrPA, xmlNpa, currentAdresseEmpty);
                        JadeThread.logInfo("INFO", "Adresse domicile crée;");
                        AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileNon++;
                    }
                } else {
                    if (hasPA || hasCP) {
                        JadeThread.logInfo("INFO", "Adresse domicile trouvé. Création courrier avec P/A ou CP !");
                        traitementAdresseCreation(AMGestionTiers.CS_TYPE_COURRIER, tiersPrincipal, adrRue, adrCP,
                                adrPA, xmlNpa, currentAdresseEmpty);
                        JadeThread.logInfo("INFO", "Adresse courrier crée;");
                        AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileOuiCPPA++;
                    } else {
                        if (isSameAdresse(adrRue, xmlNpa, currentAdresseStandardDomicile)) {
                            // Si on a deja la même adresse, on ne fait rien
                            JadeThread.logInfo("INFO", "Adresse domicile déjà à jour !");
                            AMProcessRepriseDecisionsTaxationsEntityHandler._adresseUpToDate++;
                        } else {
                            JadeThread.logInfo("INFO", "Adresse domicile trouvé. Création domicile sans CP ou P/A !");
                            traitementAdresseCreation(AMGestionTiers.CS_TYPE_DOMICILE, tiersPrincipal, adrRue, "", "",
                                    xmlNpa, currentAdresseStandardDomicile);
                            JadeThread.logInfo("INFO", "Adresse domicile mise à jour;");
                            AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileOui++;
                        }
                    }
                }
            } else {
                if (hasAdresseStandardDomicile) {
                    AMProcessRepriseDecisionsTaxationsEntityHandler._courrierOuiDomicileOui++;
                    JadeThread.logInfo("INFO", "Adresse courier et domicile existantes.");
                } else {
                    AMProcessRepriseDecisionsTaxationsEntityHandler._courrierOuiDomicileNon++;
                    JadeThread.logInfo("INFO", "Adresse courrier seule existante.");
                }
            }
        } catch (Exception e) {
            String msgErr = "";

            msgErr = e.toString();

            if (e.getCause() != null) {
                msgErr += e.getCause().getMessage();
            }
            JadeThread.logError("ERREUR", "ERREUR LORS DE LA CREATION DE L'ADRESSE ! ==>" + msgErr + ";");
        }

    }

    /**
     * Création de l'adresse
     * 
     * @param csTypeAdresse
     * @param tiersPrincipal
     * @param rueNo
     * @param casePostale
     * @param representant
     * @param rueNpa
     * @param adresseToCreate
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private void traitementAdresseCreation(String csTypeAdresse, PersonneEtendueComplexModel tiersPrincipal,
            String rueNo, String casePostale, String representant, String rueNpa, AdresseTiersDetail adresseToCreate)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        if ((adresseToCreate.getFields() != null)
                && adresseToCreate.getFields().containsKey(AdresseTiersDetail.ADRESSE_VAR_DATE_DEBUT_RELATION)) {
            String dateDebutRelation = adresseToCreate.getFields().get(
                    AdresseTiersDetail.ADRESSE_VAR_DATE_DEBUT_RELATION);

            if (!JadeStringUtil.isBlankOrZero(dateLimiteToUpdateAdresse)
                    && JadeDateUtil.isDateAfter(dateDebutRelation, dateLimiteToUpdateAdresse)) {
                JadeThread.logInfo("INFO", "Adresse existante plus récente");
                AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileOui--;
                return;
            }
        }

        // -----------------------------------------------------------------------------------------
        // Récupération des infos du fichier XML
        // -----------------------------------------------------------------------------------------
        String designation = currentContribuableAdresse.getDesignation();
        String nomPrenom = currentContribuableAdresse.getNomPrenom();

        String npa = currentContribuableAdresse.getNpa();
        String localite = currentContribuableAdresse.getLocalite();

        if (JadeStringUtil.isEmpty(nomPrenom) && JadeStringUtil.isEmpty(currentContribuableAdresse.getNpa())) {
            JadeThread.logError("ERREUR",
                    "Le nom et prénom ainsi que le NPA sont obligatoires pour créer une adresse !;");
            return;
        }

        AdresseComplexModel adresse = new AdresseComplexModel();
        adresse.getTiers().setId(tiersPrincipal.getTiers().getIdTiers());
        if (!JadeStringUtil.isBlankOrZero(designation)) {
            String politesse = getPolitesseCS(designation);
            adresse.getAdresse().setTitreAdresse(politesse);
        }

        if (!JadeStringUtil.isEmpty(nomPrenom)) {
            adresse.getAdresse().setLigneAdresse1(JadeStringUtil.firstLetterToUpperCase(nomPrenom));
        }

        // -----------------------------------------------------------------------------------------
        // Traitement en fonction de ce qu'on a dans le fichier XML (Case postale, p/a, rue, rueSuite,...)
        // -----------------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(casePostale)) {
            if (JadeStringUtil.contains(casePostale.toLowerCase(), "case postale")) {
                casePostale = JadeStringUtil.change(casePostale, "Case postale", "", true);
            }
            adresse.getAdresse().setCasePostale(casePostale);
        }

        if (!JadeStringUtil.isEmpty(representant)) {
            adresse.getAdresse().setLigneAdresse3(representant);
        }

        if (!JadeStringUtil.isEmpty(rueNo)) {
            try {
                List<String> rueNum = JadeStringUtil.tokenize(rueNo, ",");

                if (rueNum.size() == 1) {
                    rueNum = JadeStringUtil.tokenize(rueNo, ".");
                }

                if (rueNum.size() == 1) {
                    adresse.getAdresse().setRue(rueNo);
                } else if (rueNum.size() > 1) {
                    adresse.getAdresse().setNumeroRue(rueNum.get(0));
                    adresse.getAdresse().setRue(
                            JadeStringUtil.firstLetterToUpperCase(JadeStringUtil.stripBlanks(rueNum.get(1))));
                }
            } catch (Exception e) {
                JadeThread.logError("ERREUR", "ERREUR LORS DE LA CREATION DE L'ADRESSE ==> " + e.toString() + ";");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(npa)) {
            adresse.getLocalite().setNumPostal(npa);
        }

        if (!JadeStringUtil.isBlankOrZero(localite)) {
            adresse.getLocalite().setLocalite(localite);
        }

        adresse.getTiers().getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
        // -----------------------------------------------------------------------------------------
        // Création / Update de l'adresse
        // -----------------------------------------------------------------------------------------
        TIBusinessServiceLocator.getAdresseService().addAdresse(adresse, AMGestionTiers.CS_DOMAINE_DEFAUT,
                csTypeAdresse, false);
    }

    /**
     * Ajout des dates de sortie et des codes traitements dans le cas ou un membre de la famille est décédé
     * 
     * @param idContribuableDossier
     */
    private void traitementCodesFin(String idContribuableDossier) {
        try {
            String dateDecesPrincipal = currentContribuablePrincipal.getDateDeces();
            String dateDecesConjoint = "";
            if (!(currentContribuableConjoint == null)) {
                dateDecesConjoint = currentContribuableConjoint.getDateDeces();
            }

            // Si on a aucune date de décès, on ressort
            if (!isValideDate(dateDecesPrincipal) && !isValideDate(dateDecesConjoint) && (persChargDecedes.size() == 0)) {
                return;
            }

            SimpleFamilleSearch sfSearch = new SimpleFamilleSearch();
            sfSearch.setForIdContribuable(idContribuableDossier);
            sfSearch.setForFinDefinitive("0");
            sfSearch = AmalImplServiceLocator.getSimpleFamilleService().search(sfSearch);
            if (sfSearch.getSize() == 0) {
                return;
            }
            FamilleContribuable membreContrib = new FamilleContribuable();
            FamilleContribuable membreConjoint = new FamilleContribuable();
            ArrayList<FamilleContribuable> listPersCharge = new ArrayList<FamilleContribuable>();
            for (JadeAbstractModel membre_model : sfSearch.getSearchResults()) {
                SimpleFamille membre = (SimpleFamille) membre_model;

                FamilleContribuable membreFc = AmalServiceLocator.getFamilleContribuableService().read(membre.getId());

                if (IAMCodeSysteme.CS_TYPE_ENFANT.equals(membre.getPereMereEnfant())) {
                    listPersCharge.add(membreFc);
                } else {
                    if (membre.getIsContribuable()) {
                        membreContrib = membreFc;
                    } else {
                        membreConjoint = membreFc;
                    }
                }
            }

            if (isValideDate(dateDecesPrincipal) && isValideDate(dateDecesConjoint)) {
                // Les 2 sont décédés
                disableMembre(membreContrib, dateDecesPrincipal, false, AMCodeTraitementDossierFamille.DECES);
                disableMembre(membreConjoint, dateDecesConjoint, false, AMCodeTraitementDossierFamille.DECES);
            } else if (isValideDate(dateDecesPrincipal) && !isValideDate(dateDecesConjoint)) {
                // Seul le contribuable principal est décédé
                disableMembre(membreContrib, dateDecesPrincipal, false, AMCodeTraitementDossierFamille.DECES);
                disableMembre(membreConjoint, dateDecesPrincipal, true, AMCodeTraitementDossierFamille.CHGT_ETAT_CIVIL);

                // Fin de droit à la fin de l'année pour les enfants
                if (listPersCharge.size() > 0) {
                    for (FamilleContribuable pCharg : listPersCharge) {
                        disableMembre(pCharg, dateDecesPrincipal, true,
                                AMCodeTraitementDossierFamille.CHGT_ETAT_CIVIL_MERE);
                    }
                }
            } else if (!isValideDate(dateDecesPrincipal) && isValideDate(dateDecesConjoint)) {
                // Seul le conjoint est décédé
                disableMembre(membreConjoint, dateDecesConjoint, false, AMCodeTraitementDossierFamille.DECES);
            }

            // Date de décès sur personne a charge ?
            for (Iterator it = persChargDecedes.keySet().iterator(); it.hasNext();) {
                String idFamilleContribuable = (String) it.next();
                String dateDecesPersCharg = persChargDecedes.get(idFamilleContribuable).toString();
                for (FamilleContribuable pCharg : listPersCharge) {
                    if (pCharg.getSimpleFamille().getIdFamille().equals(idFamilleContribuable)) {
                        disableMembre(pCharg, dateDecesPersCharg, false, AMCodeTraitementDossierFamille.DECES);
                    }
                }
            }

        } catch (Exception e) {
            // TODO EXCEPTION !!
        }
    }

    /**
     * Traitement des informations du conjoint
     * 
     * @param idContribuableDossier
     */
    private void traitementConjoint(String idContribuableDossier) {
        try {
            if ((currentContribuableConjoint != null)) {
                boolean isConjointDecede = false;

                if (isValideDate(currentContribuableConjoint.getDateDeces())) {
                    isConjointDecede = true;
                }

                SimpleFamilleSearch sfConjointSearch = new SimpleFamilleSearch();
                sfConjointSearch.setForIdContribuable(idContribuableDossier);
                sfConjointSearch.setIsContribuable(false);
                ArrayList<String> inValues = new ArrayList<String>();
                inValues.add(IAMCodeSysteme.CS_TYPE_PERE);
                inValues.add(IAMCodeSysteme.CS_TYPE_MERE);
                sfConjointSearch.setInPereMereEnfant(inValues);
                sfConjointSearch = AmalImplServiceLocator.getSimpleFamilleService().search(sfConjointSearch);

                SimpleFamille conjoint = new SimpleFamille();
                FamilleContribuable familleContribuable = new FamilleContribuable();

                // Pas de conjoint dans Web@Amal... on le crée
                if (sfConjointSearch.getSize() == 0) {
                    if (isConjointDecede) {
                        JadeThread.logInfo("INFO", "Pas de création du conjoint si date de décès !");
                        return;
                    }
                    conjoint.setIdContribuable(idContribuableDossier);

                    if (isValideDate(currentContribuableConjoint.getDateNaiss())) {
                        conjoint.setDateNaissance(currentContribuableConjoint.getDateNaiss());
                    } else {
                        JadeThread.logError("ERROR", "Création du membre impossible. Date de naissance incorrecte : "
                                + currentContribuableConjoint.getDateNaiss());
                        return;
                    }
                    conjoint.setIsContribuable(false);
                    conjoint.setNoAVS(currentContribuableConjoint.getNavs13().toString());
                    conjoint.setNnssContribuable(currentContribuablePrincipal.getNavs13().toString());
                    conjoint.setNomPrenom(currentContribuableConjoint.getNom() + " "
                            + currentContribuableConjoint.getPrenom());
                    conjoint.setNomPrenomUpper(currentContribuableConjoint.getNom().toUpperCase() + " "
                            + currentContribuableConjoint.getPrenom().toUpperCase());
                    conjoint.setNoPersonne(currentContribuableConjoint.getNip().toString());
                    conjoint.setSexe(getSexeCS(currentContribuableConjoint.getSexe().toString()));
                    conjoint.setPereMereEnfant(getPereMereSelonSexe(conjoint.getSexe()));
                    conjoint.setCarteCulture(Boolean.FALSE);
                    conjoint = AmalImplServiceLocator.getSimpleFamilleService().create(conjoint);
                    familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(conjoint.getId());
                    JadeThread.logInfo("INFO", "Menbre famille conjoint crée !");
                } else {
                    // Conjoint trouvé...
                    int nbConj = 0;

                    for (JadeAbstractModel model : sfConjointSearch.getSearchResults()) {
                        conjoint = (SimpleFamille) model;
                        familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(conjoint.getId());

                        if (!JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getFinDefinitive())) {
                            continue;
                        }
                        nbConj++;
                        if (nbConj > 1) {
                            JadeThread.logError("ERREUR", "Plusieurs conjoints actifs trouvés !" + " ==> "
                                    + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT)
                                    + ";");
                            return;
                        }
                    }

                    if (nbConj == 0) {
                        JadeThread.logInfo("INFO", "Conjoint avec date de fin trouvé !");
                        return;
                    }
                }
                ArrayList<String> familleModifications = new ArrayList<String>();
                boolean sfNeedUpdate = false;
                PersonneEtendueComplexModel tiersConjointXML = new PersonneEtendueComplexModel();
                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getIdTier())) {
                    tiersConjointXML = creationTiersConjoint();
                    if ((tiersConjointXML == null) || tiersConjointXML.isNew()) {
                        JadeThread.logError("ERREUR", "ERREUR CREATION TIERS CONJOINT" + " ==> "
                                + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT) + ";");
                        return;
                    }
                } else {
                    tiersConjointXML = familleContribuable.getPersonneEtendue();
                }

                updateMissingsInfosConjoint(tiersConjointXML, familleContribuable.getSimpleFamille(),
                        currentContribuableConjoint);
                // On met le conjoint à jour
                familleContribuable.setPersonneEtendue(tiersConjointXML);
                // -----------------------------------------------------------------------------------------
                // Si le membre n'est pas encore rattaché à un tiers, on le fait
                // -----------------------------------------------------------------------------------------
                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getIdTier())) {
                    familleContribuable.getSimpleFamille().setIdTier(tiersConjointXML.getTiers().getIdTiers());
                    sfNeedUpdate = true;
                    familleModifications.add("idTiers");
                }
                if (!isValideDate(familleContribuable.getSimpleFamille().getDateNaissance())) {
                    if (isValideDate(tiersConjointXML.getPersonne().getDateNaissance())) {
                        familleContribuable.getSimpleFamille().setDateNaissance(
                                tiersConjointXML.getPersonne().getDateNaissance());
                    } else {
                        if (isValideDate(currentContribuableConjoint.getDateNaiss())) {
                            familleContribuable.getSimpleFamille().setDateNaissance(
                                    currentContribuableConjoint.getDateNaiss());
                        } else {
                            JadeThread.logError("ERROR",
                                    "Création du membre impossible. Date de naissance incorrecte : "
                                            + currentContribuableConjoint.getDateNaiss());
                        }
                    }
                    sfNeedUpdate = true;
                    familleModifications.add("Date de naissance");
                }

                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getNoAVS())) {
                    if (!JadeStringUtil.isBlankOrZero(tiersConjointXML.getPersonneEtendue().getNumAvsActuel())) {
                        familleContribuable.getSimpleFamille()
                                .setNoAVS(
                                        JadeStringUtil.removeChar(tiersConjointXML.getPersonneEtendue()
                                                .getNumAvsActuel(), '.'));
                    } else {
                        familleContribuable.getSimpleFamille().setNoAVS(
                                currentContribuableConjoint.getNavs13().toString());
                    }
                    sfNeedUpdate = true;
                    familleModifications.add("No AVS");
                }

                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getNomPrenom())) {
                    familleContribuable.getSimpleFamille().setNomPrenom(
                            tiersConjointXML.getTiers().getDesignation1() + " "
                                    + tiersConjointXML.getTiers().getDesignation2());
                    familleContribuable.getSimpleFamille().setNomPrenomUpper(
                            tiersConjointXML.getTiers().getDesignation1().toUpperCase() + " "
                                    + tiersConjointXML.getTiers().getDesignation2().toUpperCase());
                    sfNeedUpdate = true;
                    familleModifications.add("Nom et prénom");
                }

                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getNoPersonne())) {
                    familleContribuable.getSimpleFamille().setNoPersonne(
                            currentContribuableConjoint.getNip().toString());
                    sfNeedUpdate = true;
                    familleModifications.add("NIP");
                }

                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getSexe())) {
                    if (!JadeStringUtil.isBlankOrZero(tiersConjointXML.getPersonne().getSexe())) {
                        familleContribuable.getSimpleFamille().setSexe(tiersConjointXML.getPersonne().getSexe());
                    } else {
                        familleContribuable.getSimpleFamille().setSexe(
                                getSexeCS(currentContribuableConjoint.getSexe().toString()));
                    }
                    sfNeedUpdate = true;
                    familleModifications.add("Sexe");
                }

                if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getPereMereEnfant())) {
                    familleContribuable.getSimpleFamille().setPereMereEnfant(
                            getPereMereSelonSexe(familleContribuable.getSimpleFamille().getSexe()));
                    sfNeedUpdate = true;
                    familleModifications.add("Code PereMereEnfant");
                }

                if (sfNeedUpdate) {
                    familleContribuable = AmalServiceLocator.getFamilleContribuableService()
                            .update(familleContribuable);
                    JadeThread.logInfo("INFO", "[F] " + familleContribuable.getSimpleFamille().getNomPrenom() + " : "
                            + familleModifications.toString() + ";");
                }
            }
        } catch (Exception e) {
            if (!(e.getCause() == null)) {
                JadeThread.logError("ERREUR", "ERREUR TRAITEMENT CONJOINT  : " + e.getCause() + " ==> "
                        + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT) + ";");
            } else {
                JadeThread.logError("ERREUR", "ERREUR TRAITEMENT CONJOINT " + " ==> "
                        + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT) + ";");
            }
        }
    }

    /**
     * Méthode de traitement des personnes à charge
     * 
     * @param idContribuableDossier
     * @throws JadeNoBusinessLogSessionError
     */
    private void traitementPersonneCharge(String idContribuableDossier) throws JadeNoBusinessLogSessionError {
        try {
            PersonneEtendueComplexModel tiersPersonneCharge = null;
            nbEnfants = 0;
            nbEnfantsSuspens = 0;
            persChargDecedes = new HashMap<String, String>();
            if (mapCurrentPersonneChargeTaxation != null) {
                ch.globaz.amal.xmlns.am_0002._1.Taxation taxation = mapCurrentPersonneChargeTaxation
                        .get(mapCurrentPersonneChargeTaxation.lastKey());
                for (ch.globaz.amal.xmlns.am_0002._1.Personne personneACharge : taxation.getPersonne()) {
                    // -----------------------------------------------------------------------------------------
                    // Récupération des taux de déduction
                    // -----------------------------------------------------------------------------------------
                    String tauxDeducIc = personneACharge.getTauxDeductIc();
                    String tauxDeducIfd = personneACharge.getTauxDeductIfd();

                    // -----------------------------------------------------------------------------------------
                    // Si l'enfant n'est plus à charge on ne le reprend pas
                    // -----------------------------------------------------------------------------------------
                    if (JadeStringUtil.isBlankOrZero(tauxDeducIc) && JadeStringUtil.isBlankOrZero(tauxDeducIfd)) {
                        JadeThread.logInfo("INFO", personneACharge.getNom() + " " + personneACharge.getPrenom()
                                + " ==> Enfant non pris en charge (Taux déductions Ic et Ifd = 0)");
                        continue;
                    }

                    // -----------------------------------------------------------------------------------------
                    // On vérifie également que la personne ne soit pas née après la période fiscale
                    // -----------------------------------------------------------------------------------------
                    int yearNaissance = new JADate(personneACharge.getDateNaiss()).getYear();
                    int periode = taxation.getPeriode().intValue();
                    boolean bornAfterFiscalPeriode = yearNaissance > periode;

                    // -----------------------------------------------------------------------------------------
                    // Comptage des enfants / enfants en suspens
                    // -----------------------------------------------------------------------------------------
                    if ("0".equals(tauxDeducIc)) {
                        // On ne compte pas l'enfant
                    } else if ("50".equals(tauxDeducIc)) {
                        nbEnfantsSuspens++;
                    } else {
                        if (!bornAfterFiscalPeriode) {
                            nbEnfants++;
                        }
                    }
                    // -----------------------------------------------------------------------------------------
                    // On vérifie que la personne à charge n'existe pas déjà
                    // -----------------------------------------------------------------------------------------
                    tiersPersonneCharge = creationContribuablePersonneACharge(personneACharge);
                    if (tiersPersonneCharge == null) {
                        return;
                    }
                    SimpleFamilleSearch searchPersonneCharge = new SimpleFamilleSearch();
                    searchPersonneCharge.setForPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
                    searchPersonneCharge.setForIdContribuable(idContribuableDossier);
                    // -----------------------------------------------------------------------------------------
                    // Recherche du membre famille
                    // -----------------------------------------------------------------------------------------
                    searchPersonneCharge = findMembreFamilleByCriters(searchPersonneCharge, tiersPersonneCharge);

                    // -----------------------------------------------------------------------------------------
                    // Si on ne trouve rien, on crée le membre
                    // -----------------------------------------------------------------------------------------
                    if ((searchPersonneCharge.getSize() == 0)) {
                        // On recherche un enfant "vide" (Sans idTiers, NSS, nom/prénom ou date de naissance)
                        SimpleFamille enfantVide = searchEnfantVide(idContribuableDossier);

                        // Si pas d'enfant vide, on crée normalement
                        if (enfantVide == null) {
                            FamilleContribuable familleContribuable = new FamilleContribuable();
                            familleContribuable.getSimpleFamille().setIdContribuable(idContribuableDossier);
                            familleContribuable.setPersonneEtendue(tiersPersonneCharge);
                            familleContribuable.getSimpleFamille().setNoPersonne(personneACharge.getNip().toString());
                            familleContribuable.getSimpleFamille().setPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
                            familleContribuable.getSimpleFamille().setTauxDeductIc(tauxDeducIc);
                            familleContribuable.getSimpleFamille().setTauxDeductIfd(tauxDeducIfd);

                            try {
                                familleContribuable = AmalServiceLocator.getFamilleContribuableService().create(
                                        familleContribuable);
                                JadeThread.logInfo("INFO", familleContribuable.getSimpleFamille().getNomPrenom()
                                        + " crée;");

                                // Si l'enfant est décédé, on log pour plus tard afin de mettre des codes Fin
                                if (isValideDate(personneACharge.getDateDeces())) {
                                    persChargDecedes.put(familleContribuable.getSimpleFamille().getIdFamille(),
                                            personneACharge.getDateDeces());
                                }
                            } catch (Exception ex) {
                                JadeThread.logError("ERREUR", "DOSSIER NON TROUVE ET ERREUR CREATION PERSONNE A CHARGE"
                                        + " ==> " + getPersonneChargeObjectInfos(personneACharge) + ";");
                                return;
                            }
                        } else {
                            // Si on trouve un enfant vide, on le met à jour
                            FamilleContribuable familleContribuable = new FamilleContribuable();
                            familleContribuable.setSimpleFamille(enfantVide);
                            familleContribuable.getSimpleFamille().setIdContribuable(idContribuableDossier);
                            familleContribuable.setPersonneEtendue(tiersPersonneCharge);
                            familleContribuable.getSimpleFamille().setNoPersonne(personneACharge.getNip().toString());
                            familleContribuable.getSimpleFamille().setPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
                            familleContribuable.getSimpleFamille().setTauxDeductIc(tauxDeducIc);
                            familleContribuable.getSimpleFamille().setTauxDeductIfd(tauxDeducIfd);

                            try {
                                familleContribuable = AmalServiceLocator.getFamilleContribuableService().update(
                                        familleContribuable);
                                JadeThread.logInfo("INFO", "Membre famille (id "
                                        + familleContribuable.getSimpleFamille().getIdFamille()
                                        + ") mis à jour par dessus membre incomplet ! ==> "
                                        + familleContribuable.getSimpleFamille().getNomPrenom() + ";");

                                // Si l'enfant est décédé, on log pour plus tard afin de mettre des codes Fin
                                if (isValideDate(personneACharge.getDateDeces())) {
                                    persChargDecedes.put(familleContribuable.getSimpleFamille().getIdFamille(),
                                            personneACharge.getDateDeces());
                                }
                            } catch (Exception ex) {
                                JadeThread.logError("ERREUR",
                                        "DOSSIER NON TROUVE ET ERREUR MISE A JOUR PERSONNE A CHARGE" + " ==> "
                                                + getPersonneChargeObjectInfos(personneACharge) + ";");
                                return;
                            }
                        }
                    } else if (searchPersonneCharge.getSize() == 1) {
                        SimpleFamille simpleFamille = (SimpleFamille) searchPersonneCharge.getSearchResults()[0];
                        FamilleContribuable familleEnfant = AmalServiceLocator.getFamilleContribuableService().read(
                                simpleFamille.getId());
                        // -----------------------------------------------------------------------------------------
                        // Recherche de l'âge du membre
                        // -----------------------------------------------------------------------------------------
                        // int age = this.checkAge(familleEnfant.getSimpleFamille(), currentYearSubside);
                        boolean valueUpdated = false;

                        if (!familleEnfant.getSimpleFamille().getTauxDeductIc().equals(tauxDeducIc)) {
                            familleEnfant.getSimpleFamille().setTauxDeductIc(tauxDeducIc);
                            valueUpdated = true;
                        }

                        if (!familleEnfant.getSimpleFamille().getTauxDeductIfd().equals(tauxDeducIfd)) {
                            familleEnfant.getSimpleFamille().setTauxDeductIfd(tauxDeducIfd);
                            valueUpdated = true;
                        }

                        // -----------------------------------------------------------------------------------------
                        // Update des infos manquantes
                        // -----------------------------------------------------------------------------------------
                        updateMissingsInfos(tiersPersonneCharge, familleEnfant, personneACharge);

                        if (valueUpdated) {
                            familleEnfant = AmalServiceLocator.getFamilleContribuableService().update(familleEnfant);
                        }

                        // Si l'enfant est décédé, on log pour plus tard afin de mettre des codes Fin
                        if (isValideDate(personneACharge.getDateDeces())) {
                            persChargDecedes.put(familleEnfant.getSimpleFamille().getIdFamille(),
                                    personneACharge.getDateDeces());
                        }
                        // }
                    } else if (searchPersonneCharge.getSize() > 1) {
                        // -----------------------------------------------------------------------------------------
                        // Plusieurs fois le même enfant ==> Erreur
                        // -----------------------------------------------------------------------------------------
                        JadeThread.logError("ERREUR", "ENFANT PRESENT PLUSIEURS FOIS ! ==>"
                                + getPersonneChargeObjectInfos(personneACharge) + ";");
                    }
                }
            }
        } catch (Exception ex) {
            JadeThread.logError("ERREUR", "DOSSIER NON TROUVE ET ERREUR CREATION ENFANT(S) " + ";"
                    + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + ";");
        }
    }

    /**
     * Update les informations manquantes dans PYXIS et / ou dans le membre de famille (SimpleFamille)
     * 
     * @param personneEtendue
     *            - Entité PYXIS
     * @param membreFamille
     *            - Entité SimpleFamille
     * @param jaxbObject
     *            - Objet contenant les informations du fichier XML
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationException
     * @throws JAException
     */
    private void updateMissingsInfos(PersonneEtendueComplexModel personneEtendue, FamilleContribuable membreFamille,
            Object jaxbObject) throws FamilleException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError, JadeApplicationException,
            JAException {
        boolean familleNeedUpdate = false;
        ArrayList<String> familleModifications = new ArrayList<String>();
        boolean personneEtendueNeedUpdate = false;
        ArrayList<String> personneEtendueModifications = new ArrayList<String>();
        String nipXML = "";
        String noContribuableXML = formatNoContribuable(currentNoContribuable);
        String noContribuablePrecedentXML = formatNoContribuable(currentNoContribuablePrecedent);
        String dateNaissanceXML = "";
        String noAvsXML = "";
        String sexeXML = "";
        String etatCivilXML = "";
        ch.globaz.amal.xmlns.am_0001._1.TypePersonne typePersonne = null;
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());
        // -----------------------------------------------------------------------------------------
        // Récupération des informations du fichier XML
        // -----------------------------------------------------------------------------------------
        if (jaxbObject instanceof ch.globaz.amal.xmlns.am_0001._1.Personne) {
            ch.globaz.amal.xmlns.am_0001._1.Personne personne = (ch.globaz.amal.xmlns.am_0001._1.Personne) jaxbObject;
            nipXML = personne.getNip().toString();
            dateNaissanceXML = personne.getDateNaiss();
            noAvsXML = personne.getNavs13().toString();
            sexeXML = personne.getSexe().toString();
            etatCivilXML = personne.getEcPers().toString();
            typePersonne = personne.getType();
        } else if (jaxbObject instanceof ch.globaz.amal.xmlns.am_0002._1.Personne) {
            ch.globaz.amal.xmlns.am_0002._1.Personne personne = (ch.globaz.amal.xmlns.am_0002._1.Personne) jaxbObject;
            nipXML = personne.getNip().toString();
            dateNaissanceXML = personne.getDateNaiss();
            noAvsXML = personne.getNavs13().toString();
            sexeXML = personne.getSexe().toString();
        } else {
            JadeThread.logError("ERREUR", "ERREUR CASTING JAXBOBJECT !! ==> "
                    + getPersonneObjectInfos(ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL) + ";");
        }
        PersonneEtendueComplexModel personneEtendueToUpdate = TIBusinessServiceLocator.getPersonneEtendueService()
                .read(personneEtendue.getId());
        // -----------------------------------------------------------------------------------------
        // Date de décès PYXIS
        // -----------------------------------------------------------------------------------------
        // boolean isSortieDossier = false;

        // -----------------------------------------------------------------------------------------
        // Date de naissance PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonne().getDateNaissance())) {
            if (!JadeStringUtil.isBlankOrZero(dateNaissanceXML) && isValideDate(dateNaissanceXML)) {
                personneEtendueToUpdate.getPersonne().setDateNaissance(dateNaissanceXML);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Date naissance");
            }
        }
        // -----------------------------------------------------------------------------------------
        // Date de naissance SimpleFamille
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(membreFamille.getSimpleFamille().getDateNaissance())) {
            if (!JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonne().getDateNaissance())) {
                if (isValideDate(personneEtendueToUpdate.getPersonne().getDateNaissance())) {
                    membreFamille.getSimpleFamille().setDateNaissance(
                            personneEtendueToUpdate.getPersonne().getDateNaissance());
                    familleNeedUpdate = true;
                    familleModifications.add("Date naissance");
                }
            } else if (!JadeStringUtil.isBlankOrZero(dateNaissanceXML)) {
                if (isValideDate(dateNaissanceXML)) {
                    membreFamille.getSimpleFamille().setDateNaissance(dateNaissanceXML);
                    familleNeedUpdate = true;
                    familleModifications.add("Date naissance");
                }
            }
        }

        // if (!isSortieDossier) {
        // -----------------------------------------------------------------------------------------
        // Si le membre n'est pas encore rattaché à un tiers, on le fait
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(membreFamille.getSimpleFamille().getIdTier())) {
            membreFamille.getSimpleFamille().setIdTier(personneEtendueToUpdate.getTiers().getIdTiers());
            familleNeedUpdate = true;
            familleModifications.add("idTiers");
        }

        // -----------------------------------------------------------------------------------------
        // Update NIP
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(membreFamille.getSimpleFamille().getNoPersonne())) {
            membreFamille.getSimpleFamille().setNoPersonne(nipXML);
            familleNeedUpdate = true;
            familleModifications.add("NIP");
        }

        // -----------------------------------------------------------------------------------------
        // N° de contribuable Pyxis
        // -----------------------------------------------------------------------------------------
        if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL.equals(typePersonne)) {
            if (!noContribuableXML.equals(personneEtendueToUpdate.getPersonneEtendue().getNumContribuableActuel())) {
                personneEtendueToUpdate.getPersonneEtendue().setNumContribuableActuel(noContribuableXML);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("no Contribuable");
                personneEtendueToUpdate.setDateModifContribuable(dateToday);
                personneEtendueToUpdate
                        .setMotifModifContribuable(PersonneEtendueService.motifsModification.MOTIF_CREATION);
            }
            // else
            if (!JadeStringUtil.isBlankOrZero(noContribuablePrecedentXML)
                    && personneEtendueToUpdate.getPersonneEtendue().getNumContribuableActuel()
                            .equals(noContribuablePrecedentXML)) {

                personneEtendueToUpdate.getPersonneEtendue().setNumContribuableActuel(noContribuableXML);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("changement no Contribuable");
                historisationNoContribuable(personneEtendueToUpdate);
                personneEtendueToUpdate.setDateModifContribuable(dateToday);
                personneEtendueToUpdate
                        .setMotifModifContribuable(PersonneEtendueService.motifsModification.MOTIF_MODIFICATION);
            }
        }
        // -----------------------------------------------------------------------------------------
        // NNSS Pyxis
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonneEtendue().getNumAvsActuel())) {
            if (!JadeStringUtil.isBlankOrZero(noAvsXML)) {
                // membreFamille.setNnssContribuable(noAvsXML);
                personneEtendueToUpdate.getPersonneEtendue().setNumAvsActuel(noAvsXML);
                personneEtendueToUpdate.setDateModifAvs(dateToday);
                personneEtendueToUpdate.setMotifModifAvs(motifsModification.MOTIF_CREATION);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("no AVS");
            }
        }
        // -----------------------------------------------------------------------------------------
        // NNSS SimpleFamille
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(membreFamille.getSimpleFamille().getNoAVS())) {
            if (!JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonneEtendue().getNumAvsActuel())) {
                membreFamille.getSimpleFamille().setNoAVS(
                        JadeStringUtil.change(personneEtendueToUpdate.getPersonneEtendue().getNumAvsActuel(), ".", ""));
                familleNeedUpdate = true;
                familleModifications.add("No AVS");
            } else if (!JadeStringUtil.isBlankOrZero(noAvsXML)) {
                membreFamille.getSimpleFamille().setNoAVS(JadeStringUtil.change(noAvsXML, ".", ""));
                familleNeedUpdate = true;
                familleModifications.add("No AVS");
            }
        }

        // -----------------------------------------------------------------------------------------
        // Sexe PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonne().getSexe())) {
            if (!JadeStringUtil.isBlankOrZero(sexeXML)) {
                if (sexeXML.equals("1")) {
                    personneEtendueToUpdate.getPersonne().setSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);
                } else if (sexeXML.equals("2")) {
                    personneEtendueToUpdate.getPersonne().setSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);
                }
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Sexe");
            }
        }
        // -----------------------------------------------------------------------------------------
        // Sexe membre famille
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(membreFamille.getSimpleFamille().getSexe())) {
            if (!JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonne().getSexe())) {
                membreFamille.getSimpleFamille().setSexe(personneEtendueToUpdate.getPersonne().getSexe());
                familleNeedUpdate = true;
                familleModifications.add("Sexe");
            } else if (!JadeStringUtil.isBlankOrZero(sexeXML)) {
                if (sexeXML.equals("1")) {
                    membreFamille.getSimpleFamille().setSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);
                } else if (currentContribuableConjoint.getSexe().toString().equals("2")) {
                    membreFamille.getSimpleFamille().setSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);
                }
                familleNeedUpdate = true;
                familleModifications.add("Sexe");
            }
        }
        // -----------------------------------------------------------------------------------------
        // Titre PYXIS
        // Correction si sexe = homme et titre Madame, ou si sexe = femme et titre = homme
        // -----------------------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getTiers().getTitreTiers())) {
            if ((IConstantes.CS_TIERS_TITRE_MONSIEUR.equals(personneEtendueToUpdate.getTiers().getTitreTiers()) && IConstantes.CS_PERSONNE_SEXE_FEMME
                    .equals(personneEtendueToUpdate.getPersonne().getSexe()))) {
                if (sexeXML.equals("1")) {
                    personneEtendueToUpdate.getTiers().setTitreTiers(IConstantes.CS_TIERS_TITRE_MONSIEUR);
                    personneEtendueNeedUpdate = true;
                    personneEtendueModifications.add("Titre");
                }
            } else if ((IConstantes.CS_TIERS_TITRE_MADAME.equals(personneEtendueToUpdate.getTiers().getTitreTiers()) && IConstantes.CS_PERSONNE_SEXE_HOMME
                    .equals(personneEtendueToUpdate.getPersonne().getSexe()))) {
                if (sexeXML.equals("2")) {
                    personneEtendueToUpdate.getTiers().setTitreTiers(IConstantes.CS_TIERS_TITRE_MADAME);
                    personneEtendueNeedUpdate = true;
                    personneEtendueModifications.add("Titre");
                }
            }

        }
        // -----------------------------------------------------------------------------------------
        // Etat Civil PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendueToUpdate.getPersonne().getEtatCivil())) {
            if (!JadeStringUtil.isBlankOrZero(etatCivilXML)) {
                String etatCivilCS = getEtatCivilCS(etatCivilXML);
                personneEtendueToUpdate.getPersonne().setEtatCivil(etatCivilCS);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Etat civil");
            }
        }
        // }
        // -----------------------------------------------------------------------------------------
        // Update SimpleFamille
        // -----------------------------------------------------------------------------------------
        if (familleNeedUpdate) {
            membreFamille.setPersonneEtendue(personneEtendueToUpdate);
            membreFamille = AmalServiceLocator.getFamilleContribuableService().update(membreFamille);
            JadeThread.logInfo("INFO", "[F] " + membreFamille.getSimpleFamille().getNomPrenom() + " : "
                    + familleModifications.toString() + ";");
        }

        // -----------------------------------------------------------------------------------------
        // Update PYXIS
        // -----------------------------------------------------------------------------------------
        if (personneEtendueNeedUpdate) {
            // Update type tiers
            if (!IConstantes.CS_TIERS_TYPE_TIERS.equals(personneEtendueToUpdate.getTiers().getTypeTiers())) {
                personneEtendueToUpdate.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
            }
            personneEtendueToUpdate = TIBusinessServiceLocator.getPersonneEtendueService().update(
                    personneEtendueToUpdate);

            JadeThread.logInfo("INFO",
                    "[T] " + personneEtendueToUpdate.getTiers().getDesignation1() + " "
                            + personneEtendueToUpdate.getTiers().getDesignation2() + " : "
                            + personneEtendueModifications.toString() + ";");
        }
    }

    /**
     * Update les informations manquantes dans PYXIS
     * 
     * @param personneEtendue
     *            - Entité PYXIS
     * @param membreFamille
     *            - Entité SimpleFamille
     * @param jaxbObject
     *            - Objet contenant les informations du fichier XML
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationException
     */
    private void updateMissingsInfosConjoint(PersonneEtendueComplexModel personneEtendue, SimpleFamille membreFamille,
            Object jaxbObject) throws FamilleException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError, JadeApplicationException {
        boolean personneEtendueNeedUpdate = false;
        ArrayList<String> personneEtendueModifications = new ArrayList<String>();
        String dateNaissanceXML = "";
        String dateDecesXML = "";
        String noAvsXML = "";
        String sexeXML = "";
        String etatCivilXML = "";
        // -----------------------------------------------------------------------------------------
        // Récupération des informations du fichier XML
        // -----------------------------------------------------------------------------------------
        ch.globaz.amal.xmlns.am_0001._1.Personne personne = (ch.globaz.amal.xmlns.am_0001._1.Personne) jaxbObject;
        try {
            dateNaissanceXML = personne.getDateNaiss();
        } catch (Exception e) {
            dateNaissanceXML = "";
        }

        try {
            dateDecesXML = personne.getDateDeces();
        } catch (Exception e) {
            dateDecesXML = "";
        }

        try {
            noAvsXML = personne.getNavs13().toString();
        } catch (Exception e) {
            noAvsXML = "";
        }

        try {
            sexeXML = personne.getSexe().toString();
        } catch (Exception e) {
            sexeXML = "";
        }

        try {
            etatCivilXML = personne.getEcPers().toString();
        } catch (Exception e) {
            etatCivilXML = "";
        }

        // -----------------------------------------------------------------------------------------
        // Date de naissance PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
            if (!JadeStringUtil.isBlankOrZero(dateNaissanceXML) && isValideDate(dateNaissanceXML)) {
                personneEtendue.getPersonne().setDateNaissance(dateNaissanceXML);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Date naissance");
            }
        }

        // -----------------------------------------------------------------------------------------
        // Date de décès PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateDeces())) {
            if (!JadeStringUtil.isBlankOrZero(dateDecesXML) && isValideDate(dateDecesXML)) {
                personneEtendue.getPersonne().setDateDeces(dateDecesXML);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Date décès");
            }
        }

        // Update type tiers
        if (!IConstantes.CS_TIERS_TYPE_TIERS.equals(personneEtendue.getTiers().getTypeTiers())) {
            personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
            personneEtendueNeedUpdate = true;
        }

        // -----------------------------------------------------------------------------------------
        // NNSS Pyxis
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumAvsActuel())) {
            if (!JadeStringUtil.isBlankOrZero(noAvsXML)) {
                membreFamille.setNnssContribuable(noAvsXML);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("No AVS");
            }
        }

        // -----------------------------------------------------------------------------------------
        // Sexe PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getSexe())) {
            if (!JadeStringUtil.isBlankOrZero(sexeXML)) {
                if (sexeXML.equals("1")) {
                    personneEtendue.getPersonne().setSexe(IConstantes.CS_PERSONNE_SEXE_HOMME);
                } else if (sexeXML.equals("2")) {
                    personneEtendue.getPersonne().setSexe(IConstantes.CS_PERSONNE_SEXE_FEMME);
                }
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Sexe");
            }
        }

        // -----------------------------------------------------------------------------------------
        // Etat Civil PYXIS
        // -----------------------------------------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getEtatCivil())) {
            if (!JadeStringUtil.isBlankOrZero(etatCivilXML)) {
                String etatCivilCS = getEtatCivilCS(etatCivilXML);
                personneEtendue.getPersonne().setEtatCivil(etatCivilCS);
                personneEtendueNeedUpdate = true;
                personneEtendueModifications.add("Etat civil");
            }
        }

        // -----------------------------------------------------------------------------------------
        // Titre PYXIS
        // Correction si sexe = homme et titre Madame, ou si sexe = femme et titre = homme
        // -----------------------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getTitreTiers())) {
            if ((IConstantes.CS_TIERS_TITRE_MONSIEUR.equals(personneEtendue.getTiers().getTitreTiers()) && IConstantes.CS_PERSONNE_SEXE_FEMME
                    .equals(personneEtendue.getPersonne().getSexe()))) {
                if (sexeXML.equals("1")) {
                    personneEtendue.getTiers().setTitreTiers(IConstantes.CS_TIERS_TITRE_MONSIEUR);
                    personneEtendueNeedUpdate = true;
                    personneEtendueModifications.add("Titre");
                }
            } else if ((IConstantes.CS_TIERS_TITRE_MADAME.equals(personneEtendue.getTiers().getTitreTiers()) && IConstantes.CS_PERSONNE_SEXE_HOMME
                    .equals(personneEtendue.getPersonne().getSexe()))) {
                if (sexeXML.equals("2")) {
                    personneEtendue.getTiers().setTitreTiers(IConstantes.CS_TIERS_TITRE_MADAME);
                    personneEtendueNeedUpdate = true;
                    personneEtendueModifications.add("Titre");
                }
            }

        }

        // -----------------------------------------------------------------------------------------
        // Update PYXIS
        // -----------------------------------------------------------------------------------------
        if (personneEtendueNeedUpdate) {
            personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
            JadeThread.logInfo("INFO", "[T] " + personneEtendue.getTiers().getDesignation1() + " "
                    + personneEtendue.getTiers().getDesignation2() + " : " + personneEtendueModifications.toString()
                    + ";");
        }
    }
}
