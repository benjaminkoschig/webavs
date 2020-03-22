package globaz.al.process.decision;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.globaz.al.utils.ALFomationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionBuilderService;
import ch.globaz.al.business.services.models.dossier.DossierComplexModelService;
import ch.globaz.al.business.services.models.droit.DroitComplexModelService;
import globaz.al.process.ALAbsrtactProcess;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeFileUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Process permettant l'impression d'un lot de décision dont les ID sont contenu dans un fichier txt (Ref : InfoRom 580)
 *
 * @author jts
 */
public abstract class ALDecisionsMasseAbstractProcess extends ALAbsrtactProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ALDecisionsMasseAbstractProcess.class);

    // 61470001 Enfant
    private static final String DROIT_ENF = "61470001";
    // 61470002 Formation
    private static final String DROIT_FORM = "61470002";
    // 61470003 Enfant en incapacité
    private static final String DROIT_ENFINC = "61470003";
    // 61470004 Formation anticipée
    private static final String DROIT_FORMANT = "61470004";
    private static final long serialVersionUID = 1L;
    JadePrintDocumentContainer containerDecisions = new JadePrintDocumentContainer();
    ProtocoleLogger dataProtocoleRecapitulatif = new ProtocoleLogger();
    private String dateDebutValidite = "";
    private String dateImpression = "";
    private String email = "";
    private String fileName = "";
    private String originalFileName = "";
    private String etatFilter = "";
    private boolean gestionTexteLibre = false;
    private boolean insertionGED = true;
    protected DecisionBuilderService serviceDecision;
    protected DossierComplexModelService serviceDossier;
    private String texteLibre = "";
    private boolean gestionCopie = false;
    private String triImpression = "";
    private Collection<String> inStatut;
    private Collection<String> inNumeroAffilie;
    private Collection<String> inActivites;
    private Collection<String> inTarif;
    private Collection<String> inTypeDroit;

    private String dateValiditeGREAT = "";
    private String dateValiditeLESS = "";
    private String dateFinValiditeGREAT = "";
    private String dateFinValiditeLESS = "";

    private List<String> idDossiersList;

    /**
     * Ajout le message <code>message</code> au logger (protocole) lié au process
     *
     * @param dossier
     *            dossier pour lequel le message doit être ajouté
     * @param message
     *            le message à ajouter
     */
    protected void addProtocoleMessage(DossierComplexModel dossier, JadeBusinessMessage message) {

        String idDossier = dossier.getId();
        String allocataire = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                .getDesignation1() + " "
                + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2();

        try {
            switch (message.getLevel()) {
                case JadeBusinessMessageLevels.ERROR:
                    dataProtocoleRecapitulatif.getErrorsLogger(idDossier, allocataire).addMessage(message);
                    break;

                case JadeBusinessMessageLevels.WARN:
                    dataProtocoleRecapitulatif.getWarningsLogger(idDossier, allocataire).addMessage(message);
                    break;

                case JadeBusinessMessageLevels.INFO:
                    dataProtocoleRecapitulatif.getInfosLogger(idDossier, allocataire).addMessage(message);
                    break;

                default:
                    dataProtocoleRecapitulatif.getErrorsLogger(idDossier, allocataire).addMessage(message);
            }

        } catch (JadeApplicationException e) {
            // Si on arrive ici c'est que vraiement tout va mal. Peut se produire si l'id du dossier n'est pas
            // défini (note : c'est précisément l'id de dossier qui nous a permis d'arriver dans cette méthode
            dataProtocoleRecapitulatif.addFatalError(e);
        }
    }

    /**
     * Charge la liste des dossiers dont les identifiants sont contenus dans le fichier passé au process.
     *
     * @return Les dossiers chargés
     * @throws IOException
     *             Exception levée en cas d'erreur de lecture du fichier
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private JadeAbstractModel[] getListDossiers()
            throws IOException, JadeApplicationException, JadePersistenceException {

        List<String> idDossiersList = loadFile();

        // chargement des dossiers
        DossierComplexSearchModel searchDossier = new DossierComplexSearchModel();
        searchDossier.setInIdDossier(idDossiersList);
        searchDossier.setWhereKey(DossierComplexSearchModel.SEARCH_LIST_DOSSIER);

        return getSearchResults(searchDossier);
    }

    /**
     * Charge la liste des dossiers en statut radié avec les critères donnés.
     *
     * @return Les dossiers chargés
     * @throws IOException
     *             Exception levée en cas d'erreur de lecture du fichier
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private JadeAbstractModel[] getListDossiersRadie()
            throws IOException, JadeApplicationException, JadePersistenceException {

        // chargement des dossiers
        DossierComplexSearchModel searchDossier = new DossierComplexSearchModel();
        searchDossier.setInNumeroAffilie(inNumeroAffilie);
        searchDossier.setInActivites(inActivites);
        searchDossier.setInStatut(inStatut);
        searchDossier.setForFinValiditeGreater(dateFinValiditeGREAT);
        searchDossier.setForFinValiditeSmaller(dateFinValiditeLESS);
        searchDossier.setWhereKey(DossierComplexSearchModel.SEARCH_LIST_DOSSIER_RADIE);

        return getSearchResults(searchDossier);
    }

    /**
     * Charge la liste des dossiers en statut actif avec les critères donnés.
     *
     * @return Les dossiers chargés
     * @throws IOException
     *             Exception levée en cas d'erreur de lecture du fichier
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private JadeAbstractModel[] getListDossiersActif()
            throws IOException, JadeApplicationException, JadePersistenceException {

        // chargement des dossiers
        DossierComplexSearchModel searchDossier = new DossierComplexSearchModel();
        searchDossier.setInNumeroAffilie(inNumeroAffilie);
        searchDossier.setInActivites(inActivites);
        searchDossier.setInStatut(inStatut);
        searchDossier.setForFinValiditeGreater(dateFinValiditeGREAT);
        searchDossier.setForFinValiditeSmaller(dateFinValiditeLESS);
        searchDossier.setForValiditeGreater(dateValiditeGREAT);
        searchDossier.setForValiditeSmaller(dateValiditeLESS);
        searchDossier.setWhereKey(DossierComplexSearchModel.SEARCH_LIST_DOSSIER_ACTIF);

        return getSearchResults(searchDossier);
    }

    private String getImpressOrderKey() {
        // attention, une seconde opération OrderBy est faite en post traitement pour trier Direct et Indirect
        if (!triImpression.equals("")) {
            return triImpression;
        }
        // return default order
        return DossierComplexSearchModel.ORDER_AFFILIE_ALLOC;

    }

    // factorisation du code
    protected JadeAbstractModel[] getSearchResults(DossierComplexSearchModel searchDossier)
            throws JadeApplicationException, JadePersistenceException {
        searchDossier.setOrderKey(getImpressOrderKey());
        searchDossier.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchDossier = serviceDossier.search(searchDossier);
        return searchDossier.getSearchResults();
    }

    /**
     * applique tous les filtre et ordonnancement a faire hors du modèle
     *
     * @return
     * @throws IOException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    protected List<DossierComplexModel> getDossierOrderedFiltered()
            throws IOException, JadeApplicationException, JadePersistenceException {
        JadeAbstractModel[] arrayDossiers;
        List<DossierComplexModel> listDossiers;
        if (!fileName.equals("")) {
            arrayDossiers = getListDossiers();
            listDossiers = sortAsInputFile(arrayDossiers);
        } else {
            if (getEtatFilter().equals(DossierComplexSearchModel.ETATACTIF)) {
                arrayDossiers = getListDossiersActif();
            } else {
                arrayDossiers = getListDossiersRadie();
            }
            listDossiers = parseAndPostSQLFilter(arrayDossiers);
        }

        // Make an additional orderBy (not implementable in Model)
        if (triImpression.equals(DossierComplexSearchModel.ORDER_DIRECT_AFFILIE)
                || triImpression.equals(DossierComplexSearchModel.ORDER_DIRECT_ALLOC)) {
            // Tri en poussant tous les indirect en fin de liste mais en parcourant et gardant l'ordre alphabétique sur
            // allocataire ou affilié remonté de la DB
            int stop = listDossiers.size();
            LOG.debug("process order Direct-Indirect pushing Indirect end of list");
            for (int i = 0; i < stop; i++) {
                if (getPaiementMode(listDossiers.get(i)).equalsIgnoreCase(ALCSDossier.PAIEMENT_INDIRECT)) {
                    DossierComplexModel temp = listDossiers.get(i);
                    LOG.debug("{} is Indirect, pushing end respecting other order by Alloc or Affilie in",
                            temp.getId());
                    listDossiers.remove(i);
                    listDossiers.add(temp);
                    i--;
                    stop--;
                }
            }
        }
        return listDossiers;
    }

    /**
     * Méthode retournant le mode de paiement du dossier en fonction du tiers bénéficiaire défini
     *
     * @return
     *         <ul>
     *         <li><code>ALCSDossier.PAIEMENT_DIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_INDIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_TIERS</code></li>
     *         </ul>
     *
     */
    public String getPaiementMode(DossierComplexModel dossierComplexModel) {

        String idTiersBeneficiaire = dossierComplexModel.getDossierModel().getIdTiersBeneficiaire();
        String idTiersAllocataire = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire();

        if (idTiersBeneficiaire.equals(idTiersAllocataire)) {
            return ALCSDossier.PAIEMENT_DIRECT;
        } else if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaire)) {
            return ALCSDossier.PAIEMENT_INDIRECT;
        } else {
            return ALCSDossier.PAIEMENT_TIERS;
        }

    }

    /**
     * Charge les droits du dossier <code>idDossier</code>
     *
     * @param idDossier
     *            Id du dossier pour lequel les droit doivent être chargés
     *
     * @return Modèle de recherche contenant les droits du dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private DroitComplexSearchModel loadDroits(String idDossier)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new ALCalculException("CalculModeAbstract#loadDroits : idDossier is null or empty");
        }

        DroitComplexModelService droitsService = ALServiceLocator.getDroitComplexModelService();
        DroitComplexSearchModel droits = new DroitComplexSearchModel();
        droits.setForIdDossier(idDossier);
        droits.setOrderKey("calculDroits");
        droitsService.search(droits);

        return droits;
    }

    /**
     * tri post DB sur des critères non configurable dans le SearchModel
     *
     * @param arrayDossiers de la DB
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws ParseException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private List<DossierComplexModel> parseAndPostSQLFilter(JadeAbstractModel[] arrayDossiers)
            throws JadeApplicationException, JadePersistenceException {
        List<DossierComplexModel> dossiersFiltre = new ArrayList<DossierComplexModel>();
        getProgressHelper().setMax(getProgressHelper().getMax() + arrayDossiers.length);
        int progressInt = 0;
        for (JadeAbstractModel dossier : arrayDossiers) {
            if (!JadeNumericUtil.isEmptyOrZero(((DossierComplexModel) dossier).getId())) {
                boolean allFilterPass = true;
                if (!getInTypeDroit().isEmpty() || !getInTarif().isEmpty()) {

                    // Type de Droit
                    if (!getInTypeDroit().isEmpty()) {
                        boolean droitMatch = false;
                        DroitComplexSearchModel droits = loadDroits(dossier.getId());
                        if (droits == null) {
                            throw new ALCalculException("CalculModeAbstract#loopDroits : droits is null");
                        }

                        for (int i = 0; i < droits.getSize(); i++) {
                            DroitComplexModel droit = (DroitComplexModel) droits.getSearchResults()[i];
                            if (getInTypeDroit().contains(DROIT_ENF) || getInTypeDroit().contains(DROIT_ENFINC)) {
                                // Check Enfant
                                droitMatch = droit.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_ENF);
                                if (getInTypeDroit().contains(DROIT_ENFINC) && !getInTypeDroit().contains(DROIT_ENF)
                                        && droitMatch) {
                                    // Check Incapacité
                                    droitMatch = !droit.getEnfantComplexModel().getEnfantModel().getCapableExercer();
                                }
                            }
                            if ((getInTypeDroit().contains(DROIT_FORM) || getInTypeDroit().contains(DROIT_FORMANT))
                                    && !droitMatch) {
                                // Check formation
                                droitMatch = droit.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_FORM);
                                if (getInTypeDroit().contains(DROIT_FORMANT) && !getInTypeDroit().contains(DROIT_FORM)
                                        && droitMatch) {
                                    // Check -16ans
                                    // la DDN est strictement postérieure au début de formation, âge de formation en arrière début
                                    // de mois
                                    try {
                                        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                                        Date moinsAgeFormation;

                                        moinsAgeFormation = formatter.parse(droit.getDroitModel().getDebutDroit());
                                        // retire l'âge de formation
                                        moinsAgeFormation.setYear(moinsAgeFormation.getYear() - ALFomationUtils.getAgeFormation(droit.getDroitModel().getDebutDroit()));
                                        // début de mois
                                        moinsAgeFormation.setDate(01);

                                        droitMatch = formatter.parse(droit.getEnfantComplexModel()
                                                .getPersonneEtendueComplexModel().getPersonne().getDateNaissance())
                                                .after(moinsAgeFormation);
                                    } catch (ParseException e) {
                                        throw new ALDecisionException("noSQLFilter filter can't parse date ", e);
                                    }
                                }
                            }
                        }
                        allFilterPass = droitMatch;
                    }
                    // Tarif
                    if (!getInTarif().isEmpty() && allFilterPass) {
                        boolean tarifMatch = false;
                        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                        Date usedDate = new Date();
                        try {
                            String finValidite = ((DossierComplexModelRoot) dossier).getDossierModel().getFinValidite();
                            if (finValidite.isEmpty()) {
                                usedDate = formatter.parse(
                                        ((DossierComplexModelRoot) dossier).getDossierModel().getDebutValidite());
                            } else {
                                usedDate = formatter.parse(finValidite);
                            }
                        } catch (ParseException e) {
                            throw new ALDecisionException("noSQLFilter filter can't parse date ", e);
                        }
                        try {
                            for (CalculBusinessModel tarif : ALServiceLocator.getCalculBusinessService().getCalcul(
                                    (DossierComplexModelRoot) dossier, JadeDateUtil.getGlobazFormattedDate(usedDate))) {
                                if (getInTarif().contains(tarif.getTarif())) {
                                    tarifMatch = true;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.WARN,
                                    ALDecisionsMasseProcess.class.getName(),
                                    "al.protocoles.impressionDecisionsMasse.warning.dossierNoProcessed");
                            addProtocoleMessage((DossierComplexModel) dossier, message);
                        }
                        allFilterPass = tarifMatch;
                    }
                }
                if (allFilterPass) {
                    dossiersFiltre.add((DossierComplexModel) dossier);
                    getProgressHelper().setMax(getProgressHelper().getMax() + 1);
                }
                getProgressHelper().setCurrent(progressInt++);
            }
        }
        return dossiersFiltre;
    }

    /**
     * dans le cas d'un import d'une liste de dossiers depuis un fichier texte, remettre après l'execution SQL IN les
     * dossier dans le
     * même ordre que le dans le fichier.
     *
     * @param arrayDossiers
     * @return liste des Dossier dans le même ordre que le fichier
     * @throws IOException a la lecture du fichier
     */
    private List<DossierComplexModel> sortAsInputFile(JadeAbstractModel[] arrayDossiers) throws IOException {

        List<String> idDossiersList = loadFile();
        List<DossierComplexModel> dossiersSortedList = new ArrayList<DossierComplexModel>();

        for (String idDossier : idDossiersList) {

            for (JadeAbstractModel dossier : arrayDossiers) {
                DossierComplexModel dossierComplex = (DossierComplexModel) dossier;

                if (idDossier.equals(dossierComplex.getId())) {
                    dossiersSortedList.add((DossierComplexModel) dossier);
                    continue;
                }
            }
        }

        return dossiersSortedList;
    }

    /**
     * Lit le contenu du fichier passé au process
     *
     * @return Liste de numéro de dossiers contenus dans le fichier
     * @throws IOException
     * @throws JadeClassCastException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws JadeServiceActivatorException
     * @throws JadeServiceLocatorException
     */
    private List<String> loadFile() throws IOException {
        // chargement de la liste des id de dossiers

        // éviter le chargement double
        if (idDossiersList != null && !idDossiersList.isEmpty()) {
            return idDossiersList;
        }

        String fileName;

        try {
            fileName = JadeFsFacade.readFile(getFileName());
        } catch (Exception e) {
            throw new IOException("Loadfile via JadeFSFacade failed", e);
        }
        String fileContent = JadeFileUtil.loadTextFile(fileName);
        String[] idDossiersArray = fileContent.split(JadeFileUtil.getLineSeparator());
        idDossiersList = Arrays.asList(idDossiersArray);
        return idDossiersList;
    }

    /**
     *
     * @return
     */
    protected HashMap<String, String> getParam() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ALConstProtocoles.INFO_PROCESSUS, "al.protocoles.impressionDecisionsMasse.info.processus.val");
        params.put(ALConstProtocoles.INFO_UTILISATEUR, getSession().getUserName());
        params.put(ALConstProtocoles.INFO_USER_EMAIL, getSession().getUserEMail());
        params.put(ALConstProtocoles.INFO_EMAIL, getEmail());
        params.put(ALConstProtocoles.INFO_DATE_DEBUT_VALIDITE, getDateDebutValidite());
        params.put(ALConstProtocoles.INFO_TEXTE_LIBRE, getTexteLibre().isEmpty() ? "0" : getTexteLibre());
        params.put(ALConstProtocoles.INFO_GESTION_TEXTE_LIBRE,
                getGestionTexteLibre() ? "al.protocoles.commun.info.oui" : "al.protocoles.commun.info.non");
        params.put(ALConstProtocoles.INFO_INSERTION_GED,
                getInsertionGED() ? "al.protocoles.commun.info.oui" : "al.protocoles.commun.info.non");
        params.put(ALConstProtocoles.INFO_DATE_IMPRESSION, getDateImpression());
        params.put(ALConstProtocoles.INFO_TRI_IMPRESSION, getTriImpressionHR());
        params.put(ALConstProtocoles.INFO_GESTION_COPIE,
                getGestionCopie() ? "al.protocoles.commun.info.oui" : "al.protocoles.commun.info.non");
        params.put(ALConstProtocoles.CRITERE_LISTEDOSSIER, getOriginalFileName());
        params.put(ALConstProtocoles.CRITERE_AFFILIE,
                (getInNumeroAffilie() != null) ? Arrays.toString(getInNumeroAffilie().toArray()) : "0");
        params.put(ALConstProtocoles.CRITERE_ACTIVITE,
                (getInActivites() != null) ? Arrays.toString(getInActivitesHR().toArray()) : "0");
        params.put(ALConstProtocoles.CRITERE_STATUT,
                (getInStatut() != null) ? Arrays.toString(getInStatutHR().toArray()) : "0");
        params.put(ALConstProtocoles.CRITERE_TARIF,
                (getInTarif() != null) ? Arrays.toString(getInTarifHR().toArray()) : "0");
        params.put(ALConstProtocoles.CRITERE_DROIT,
                (getInTypeDroit() != null) ? Arrays.toString(getInTypeDroitHR().toArray()) : "0");
        params.put(ALConstProtocoles.CRITERE_ETAT,
                getEtatFilter().equals(DossierComplexSearchModel.ETATACTIF)
                        ? "al.protocoles.commun.critere.etat.actif.label"
                        : getEtatFilter().equals(DossierComplexSearchModel.ETATRADIE)
                                ? "al.protocoles.commun.critere.etat.radie.label" : "0");
        params.put(ALConstProtocoles.CRITERE_VALID_FIN_GREAT,
                getDateFinValiditeGREAT().isEmpty() ? "0" : getDateFinValiditeGREAT());
        params.put(ALConstProtocoles.CRITERE_VALID_FIN_LESS,
                getDateFinValiditeLESS().isEmpty() ? "0" : getDateFinValiditeLESS());
        params.put(ALConstProtocoles.CRITERE_VALID_GREAT,
                getDateValiditeGREAT().isEmpty() ? "0" : getDateValiditeGREAT());
        params.put(ALConstProtocoles.CRITERE_VALID_LESS, getDateValiditeLESS().isEmpty() ? "0" : getDateValiditeLESS());

        return params;
    }

    /**
     * HumanReading triImpression.
     *
     * @return ALLabel reference used in frontend
     */
    private String getTriImpressionHR() {
        if (getTriImpression().equalsIgnoreCase(DossierComplexSearchModel.ORDER_AFFILIE_ALLOC)
                || triImpression.equals("")) {
            return getSession().getLabel("AL0034_FILTRE_TRI_ORDER_AFFILIE_ALLOC");
        } else if (getTriImpression().equalsIgnoreCase(DossierComplexSearchModel.ORDER_ALLOC)) {
            return getSession().getLabel("AL0034_FILTRE_TRI_ORDER_ALLOC");
        } else if (getTriImpression().equalsIgnoreCase(DossierComplexSearchModel.ORDER_DIRECT_AFFILIE)) {
            return getSession().getLabel("AL0034_FILTRE_TRI_ORDER_DIRECT_AFFILIE");
        } else if (getTriImpression().equalsIgnoreCase(DossierComplexSearchModel.ORDER_DIRECT_ALLOC)) {
            return getSession().getLabel("AL0034_FILTRE_TRI_ORDER_DIRECT_ALLOC");
        }
        return getTriImpression();
    }

    /**
     * HumanReading
     */
    private Collection<String> getInStatutHR() {
        return translateCodeSysCollection("ALDOSSTATU", inStatut);
    }

    /**
     * HumanReading
     */
    private Collection<String> getInActivitesHR() {
        return translateCodeSysCollection("ALDOSACTAL", inActivites);
    }

    /**
     * HumanReading
     */
    private Collection<String> getInTarifHR() {
        return translateCodeSysCollection("ALTARCAT", inTarif);
    }

    /**
     * HumanReading
     */
    private Collection<String> getInTypeDroitHR() {
        return translateCodeSysCollection("ALDOSTYDRO", inTypeDroit);
    }

    /**
     * Human Readable list
     *
     * @return the list human readable (codesys)
     */
    private Collection<String> translateCodeSysCollection(String idGroupe, Collection<String> inListe) {
        HashMap<String, String> humanReadingMap = new HashMap<String, String>();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setForActif(true);
        cm.setForIdGroupe(idGroupe);
        cm.setForIdLangue(JadeThread.currentLanguage().toUpperCase().substring(0, 1));
        try {
            cm.find();
            for (int i = 0; i < cm.size(); i++) {
                FWParametersCode code = (FWParametersCode) cm.getEntity(i);
                humanReadingMap.put(code.getIdCode(), JadeCodesSystemsUtil.getCode(code.getIdCode()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collection<String> hrList = new HashSet<String>();
        for (String i : inListe) {
            hrList.add(humanReadingMap.get(i));
        }
        return hrList;
    }

    public String getDateDebutValidite() {
        return dateDebutValidite;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getEmail() {
        return email;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public boolean getGestionTexteLibre() {
        return gestionTexteLibre;
    }

    public String getTexteLibre() {
        return texteLibre;
    }

    public boolean getInsertionGED() {
        return insertionGED;
    }

    public boolean getGestionCopie() {
        return gestionCopie;
    }

    public String getTriImpression() {
        return triImpression;
    }

    public String getEtatFilter() {
        return etatFilter;
    }

    public Collection<String> getInNumeroAffilie() {
        return inNumeroAffilie;
    }

    public Collection<String> getInStatut() {
        return inStatut;
    }

    public Collection<String> getInTarif() {
        return inTarif;
    }

    public Collection<String> getInActivites() {
        return inActivites;
    }

    public Collection<String> getInTypeDroit() {
        return inTypeDroit;
    }

    public String getDateValiditeGREAT() {
        return dateValiditeGREAT;
    }

    public String getDateValiditeLESS() {
        return dateValiditeLESS;
    }

    public String getDateFinValiditeGREAT() {
        return dateFinValiditeGREAT;
    }

    public String getDateFinValiditeLESS() {
        return dateFinValiditeLESS;
    }

    // setter
    public void setDateDebutValidite(String dateDebutValidite) {
        this.dateDebutValidite = dateDebutValidite;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setGestionTexteLibre(boolean gestionTexteLibre) {
        this.gestionTexteLibre = gestionTexteLibre;
    }

    public void setInsertionGED(boolean insertionGED) {
        this.insertionGED = insertionGED;
    }

    public void setTexteLibre(String texteLibre) {
        this.texteLibre = texteLibre;
    }

    public void setGestionCopie(boolean gestionCopie) {
        this.gestionCopie = gestionCopie;
    }

    public void setTriImpression(String triImpression) {
        this.triImpression = triImpression;
    }

    public void setInStatut(Collection<String> inStatut) {
        this.inStatut = inStatut;
    }

    public void setInNumeroAffilie(Collection<String> inAffilie) {
        inNumeroAffilie = inAffilie;
    }

    public void setInActivites(Collection<String> inActivites) {
        this.inActivites = inActivites;
    }

    public void setInTarif(Collection<String> inTarif) {
        this.inTarif = inTarif;
    }

    public void setTypeDroit(Collection<String> inTypeDroit) {
        this.inTypeDroit = inTypeDroit;
    }

    public void setEtatFilter(String etatFilter) {
        this.etatFilter = etatFilter;
    }

    public void setDateValiditeGREAT(String dateValiditeGREAT) {
        this.dateValiditeGREAT = dateValiditeGREAT;
    }

    public void setDateValiditeLESS(String dateValiditeLESS) {
        this.dateValiditeLESS = dateValiditeLESS;
    }

    public void setDateFinValiditeGREAT(String dateFinValiditeGREAT) {
        this.dateFinValiditeGREAT = dateFinValiditeGREAT;
    }

    public void setDateFinValiditeLESS(String dateFinValiditeLESS) {
        this.dateFinValiditeLESS = dateFinValiditeLESS;
    }

    public void setInTypeDroit(Collection<String> inTypeDroit) {
        this.inTypeDroit = inTypeDroit;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    @Override
    public String jobQueueName() {
        return globaz.jade.job.common.JadeJobQueueNames.SYSTEM_BATCH_JOB_QUEUE;
    }

}
