/**
 *
 */
package ch.globaz.amal.business.envois;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.subsideannee.SimpleSubsideAnneeChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.envoi.business.utils.EnvoiData;
import ch.globaz.envoi.business.utils.EnvoiDataFormatter;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jsp.util.GlobazJSPBeanUtil;

/**
 * Classe par défaut de génération des données nécessaires à envoi
 *
 * @author dhi
 *
 */
public class AMEnvoiData extends EnvoiData {
    private ArrayList<String> allCaisses = new ArrayList<String>();
    private ArrayList<String> allSubsides = new ArrayList<String>();

    // S180621_007 : Adaptation subsides normalement à partir de 01.2019
    private boolean isSubsidePCFamille = false;
    private boolean hasSupplementPC = false;

    /**
     * @param dataInput
     * @param idFormule
     */
    public AMEnvoiData(HashMap<Object, Object> dataInput, String idFormule) {
        super(dataInput, idFormule);
    }

    private String formatTitreFamille(String FirstLineInput) {
        // Selon demande PAC 02.10.2012
        return FirstLineInput;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.envoi.business.utils.EnvoiData#loadData()
     */
    @Override
    protected HashMap<Object, Object> loadData() throws Exception {
        // -------------------------------------------------------------------
        // 1) Classe mère fait le travail pour les cas standard
        // -------------------------------------------------------------------
        HashMap<Object, Object> mapSignetValues = super.loadData();

        // -------------------------------------------------------------------
        // 2) Cas spéciaux - Adresse - Listes - check si document en contient
        // -------------------------------------------------------------------
        try {
            // S180621_007 : Adaptation subsides normalement à partir de 01.2019
            String moisAnnee = "01." + mapSignetValues.get("RELATIV1");
            isSubsidePCFamille = SimpleSubsideAnneeChecker.checkIsSubsidePCFKind(moisAnnee);

            // Recherche des signets pour le document en cours
            SignetListModelSearch signetListModelSearch = new SignetListModelSearch();
            signetListModelSearch.setForIdFormule(getIdFormule());
            signetListModelSearch.setDefinedSearchSize(0);
            signetListModelSearch = ENServiceLocator.getSignetListModelService().search(signetListModelSearch);

            // Boucle sur les signets à renseigner
            for (int iSignet = 0; iSignet < signetListModelSearch.getSize(); iSignet++) {
                // Nom de la classe du signet
                SignetListModel currentSignet = (SignetListModel) signetListModelSearch.getSearchResults()[iSignet];
                String signetClassName = currentSignet.getSimpleSignetModel().getModel();
                String signetMethodName = currentSignet.getSimpleSignetModel().getMethode();
                String signetName = currentSignet.getSimpleSignetModel().getSignet();
                String signetCode = currentSignet.getSimpleSignetModel().getCode();
                // -------------------------------------------------------------------
                // 3) Cas spéciaux - adresses, paramètres annuels, listes...
                // -------------------------------------------------------------------
                if (AdresseTiersDetail.class.getName().equals(signetClassName)) {
                    // -------------------------------------------------------------------
                    // 3a) Cas spécial adresse souhaitée
                    // -------------------------------------------------------------------
                    mapSignetValues = loadDataAdresse(mapSignetValues, signetMethodName, signetName);
                } else if (SimpleParametreAnnuel.class.getName().equals(signetClassName)) {
                    // -------------------------------------------------------------------
                    // 3b) Cas spécial paramètres annuels souhaités
                    // -------------------------------------------------------------------
                    mapSignetValues = loadDataParametresAnnuels(mapSignetValues, signetCode, signetName);
                } else if (ArrayList.class.getName().equals(signetClassName)) {
                    mapSignetValues = loadDataTableau(mapSignetValues, signetMethodName, signetName, signetCode);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

        return mapSignetValues;
    }

    /**
     * Renseignement de la hashmap signet - value pour le cas de l'adresse Renseignement des signets ADR01
     * (signetLigne0) à ADR05
     *
     * @param mapSignetValuesInput
     *
     * @return return l'hash map complétée format ADR01 - Ac Tacim format ADR02 - Et Medine format ADR03 - Rue des
     *         Prejures 23 format ADR04 - VILLAGE
     */
    private HashMap<Object, Object> loadDataAdresse(HashMap<Object, Object> mapSignetValuesInput,
            String signetMethodName, String signetLigne0) {
        // Variables utiles
        Contribuable currentContribuable = null;
        AdresseTiersDetail adresseDetail = null;

        // Depuis les objects chargés, on récupère le contribuable
        currentContribuable = (Contribuable) getLoadedObject(Contribuable.class.getName());

        if (currentContribuable != null) {
            // On récupère l'adresse du contribuable et affectation si document famille
            try {
                adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                        currentContribuable.getPersonneEtendue().getTiers().getIdTiers(), true,
                        JadeDateUtil.getGlobazFormattedDate(new Date()), AMGestionTiers.CS_DOMAINE_AMAL,
                        AMGestionTiers.CS_TYPE_COURRIER, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // fill the map avec les lignes de l'adresse
        if ((adresseDetail.getAdresseFormate() != null) || (adresseDetail.getFields() != null)) {
            try {
                Object adresseFormatee = GlobazJSPBeanUtil.getProperty(signetMethodName, adresseDetail);
                String[] adresseLines = adresseFormatee.toString().split("\\n");
                for (int iLines = 1; iLines <= adresseLines.length; iLines++) {
                    if (iLines == 1) {
                        mapSignetValuesInput.put(signetLigne0 + iLines, formatTitreFamille(adresseLines[iLines - 1]));
                    } else {
                        mapSignetValuesInput.put(signetLigne0 + iLines, adresseLines[iLines - 1]);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return mapSignetValuesInput;
    }

    /**
     * Chargement des paramètres annuels
     *
     * @param mapSignetValues
     * @param signetCode
     * @return
     */
    private HashMap<Object, Object> loadDataParametresAnnuels(HashMap<Object, Object> mapSignetValuesInput,
            String signetCode, String signetName) {
        // Recherche de l'année historique en cours
        SimpleDetailFamille currentDetail = (SimpleDetailFamille) getLoadedObject(SimpleDetailFamille.class.getName());

        // Récupération du paramètre annuel pour l'année spécifié
        // et dans signetMethodName, paramètre du code système
        if (currentDetail != null) {
            String anneeHistorique = currentDetail.getAnneeHistorique();
            String codeSystemeParametre = signetCode;
            SimpleParametreAnnuelSearch searchParam = new SimpleParametreAnnuelSearch();
            searchParam.setForAnneeParametre(anneeHistorique);
            searchParam.setForCodeTypeParametre(codeSystemeParametre);
            try {
                searchParam = AmalServiceLocator.getParametreAnnuelService().search(searchParam);
                if (searchParam.getSize() == 1) {
                    SimpleParametreAnnuel param = (SimpleParametreAnnuel) searchParam.getSearchResults()[0];
                    if (param.getCodeTypeParametre().equals(IAMParametresAnnuels.CS_REVENU_MAX_SUBSIDE)
                            || param.getCodeTypeParametre().equals(IAMParametresAnnuels.CS_REVENU_MIN_SUBSIDE)) {
                        mapSignetValuesInput.put(signetName,
                                EnvoiDataFormatter.getFormattedValueCurrency(param.getValeurParametre()));
                    } else {
                        mapSignetValuesInput.put(signetName, param.getValeurParametre());
                    }
                }
            } catch (Exception ex) {
                getErrorBuffer().append("Error : unable to retrieve param ! CS : " + codeSystemeParametre + " / Year : "
                        + anneeHistorique);
            }
        }

        return mapSignetValuesInput;
    }

    /**
     * Renseignement de la hash map pour un tableau dans le document
     *
     * @param mapSignetValues
     *            map à compléter
     * @param signetMethodName
     *            nom de la méthode depuis les paramètres
     * @param signetName
     *            nom du signet
     * @return map complétée avec les infos suivantes : NOMPRENOM - <Josiane, Paul, Marc>
     * @throws Exception
     *
     */
    private HashMap<Object, Object> loadDataTableau(HashMap<Object, Object> mapSignetValues, String signetMethodName,
            String signetName, String signetCode) throws Exception {

        // ----------------------------------------------------------
        // 0) Préparation des variables utiles
        // ----------------------------------------------------------
        ArrayList<String> valuesToExport = new ArrayList<String>();
        ArrayList<Object> allNeededObjects = new ArrayList<Object>();

        // ----------------------------------------------------------
        // 1) Depuis les objects chargés, on récupère l'envoi courant
        // et recherche des envois d'un même groupe
        // ----------------------------------------------------------
        SimpleControleurEnvoiStatus currentEnvoi = (SimpleControleurEnvoiStatus) getLoadedObject(
                SimpleControleurEnvoiStatus.class.getName());

        SimpleControleurEnvoiStatusSearch currentEnvoiSearch = new SimpleControleurEnvoiStatusSearch();
        currentEnvoiSearch.setForIdJob(currentEnvoi.getIdJob());
        if (currentEnvoi.getNoGroupe().equals("0")) {
            currentEnvoiSearch.setForIdStatus(currentEnvoi.getIdStatus());
        } else {
            currentEnvoiSearch.setForNoGroupe(currentEnvoi.getNoGroupe());
        }
        try {
            currentEnvoiSearch = AmalServiceLocator.getControleurEnvoiService().search(currentEnvoiSearch);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        // ----------------------------------------------------------
        // 2) Chargement des beans en fonction des envois
        // ----------------------------------------------------------
        for (int iEnvoi = 0; iEnvoi < currentEnvoiSearch.getSize(); iEnvoi++) {
            SimpleControleurEnvoiStatus envoi = (SimpleControleurEnvoiStatus) currentEnvoiSearch
                    .getSearchResults()[iEnvoi];
            ComplexControleurEnvoiDetailSearch detailSearch = new ComplexControleurEnvoiDetailSearch();
            detailSearch.setForIdStatus(envoi.getIdStatus());
            detailSearch.setForIdJob(envoi.getIdJob());
            try {
                detailSearch = AmalServiceLocator.getControleurEnvoiService().search(detailSearch);
            } catch (Exception ex) {
                JadeThread.logWarn(this.getClass().getName(), ex.getMessage());
                ex.printStackTrace();
                continue;
            }
            // Chargement des beans
            for (int iDetail = 0; iDetail < detailSearch.getSize(); iDetail++) {
                ComplexControleurEnvoiDetail detail = (ComplexControleurEnvoiDetail) detailSearch
                        .getSearchResults()[iDetail];
                try {
                    SimpleDetailFamille detailFamille = AmalServiceLocator.getDetailFamilleService()
                            .read(detail.getIdDetailFamille());
                    if (!hasSupplementPC && !JadeStringUtil.isBlankOrZero(detailFamille.getSupplExtra())) {
                        hasSupplementPC = true;
                    }
                    allNeededObjects.add(detailFamille);
                    FamilleContribuable famille = AmalServiceLocator.getFamilleContribuableService()
                            .read(detailFamille.getIdFamille());
                    if (famille.getSimpleFamille().getNomPrenom() != null) {
                        allNeededObjects.add(famille.getSimpleFamille());
                    }

                } catch (Exception ex) {
                    JadeThread.logWarn(this.getClass().getName(), ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

        // ----------------------------------------------------------
        // Travail sur signetMethodName et signetclassName
        // ----------------------------------------------------------
        try {
            String signetClassName = signetMethodName.substring(0, signetMethodName.lastIndexOf("."));
            signetMethodName = signetMethodName.substring(signetMethodName.lastIndexOf(".") + 1);

            // ----------------------------------------------------------
            // Réflexion minime
            // Travail avec les objects déjà résolus par le prepareData
            // ----------------------------------------------------------
            for (int iObject = 0; iObject < allNeededObjects.size(); iObject++) {
                // Object courant et comparaison
                Object currentObject = allNeededObjects.get(iObject);
                if (signetClassName.equals(currentObject.getClass().getName())) {
                    try {
                        Object currentPropertyValue = GlobazJSPBeanUtil.getProperty(signetMethodName, currentObject);
                        // ----------------------------------------------------------
                        // cas très spécial finDroit, pour l'impression uniquement ...
                        // ----------------------------------------------------------
                        if (signetMethodName.equals("finDroit")) {
                            if (JadeStringUtil.isBlankOrZero(currentPropertyValue.toString())) {
                                SimpleDetailFamille currentDetail = (SimpleDetailFamille) getLoadedObject(
                                        SimpleDetailFamille.class.getName());
                                if (currentDetail != null) {
                                    currentPropertyValue = ("12." + currentDetail.getAnneeHistorique());
                                }
                            }
                        }
                        // Update values insides some extra arraylist to adapt the name of document
                        if (signetMethodName.equals("montantContributionAvecSupplExtra")) {
                            allSubsides.add(currentPropertyValue.toString());
                        }
                        if (signetMethodName.equals("nomCaisseMaladieFromAdministration")) {
                            if (!allCaisses.contains(currentPropertyValue.toString())) {
                                allCaisses.add(currentPropertyValue.toString());
                            }
                            // pour l'impression
                            if (!JadeStringUtil.isBlankOrZero(currentPropertyValue.toString())) {
                                if (currentPropertyValue.toString().length() > 13) {
                                    currentPropertyValue = currentPropertyValue.toString().substring(0, 12) + ".";
                                }
                            }
                        }

                        if (signetMethodName.equals("nomPrenom")) {
                            // pour l'impression
                            if (!JadeStringUtil.isBlankOrZero(currentPropertyValue.toString())) {
                                if (currentPropertyValue.toString().length() > 25) {
                                    currentPropertyValue = currentPropertyValue.toString().substring(0, 24) + ".";
                                }
                            }
                        }

                        // ----------------------------------------------------------
                        // récupération de la valeur formattée et mise le tableau de valeur
                        // ----------------------------------------------------------
                        String valeurFormate = EnvoiDataFormatter.getFormattedValue(signetCode,
                                currentPropertyValue.toString());
                        valuesToExport.add(valeurFormate);
                    } catch (Exception ex) {
                        String objectName = signetClassName.substring(signetClassName.lastIndexOf('.') + 1);
                        String errorMsg = objectName + "." + signetMethodName + " not found";
                        logErrorMsg(errorMsg);
                    }
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        // ----------------------------------------------------------
        // Mise à jour du signet avec ses valeurs (ArrayList)
        // ----------------------------------------------------------
        mapSignetValues.put(signetName, valuesToExport);

        return mapSignetValues;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.envoi.business.utils.EnvoiData#loadDocumentDataTopaz(java.util.HashMap)
     */
    @Override
    protected DocumentData loadDocumentDataTopaz(HashMap<Object, Object> mapData) {
        DocumentData toReturn = super.loadDocumentDataTopaz(mapData);
        // ---------------------------------------------------------------------------------
        // Travail sur le nombre de caisse et de subside pour déterminer le texte des documents
        // ---------------------------------------------------------------------------------
        int nbreCaisses = allCaisses.size();
        int nbreSubsides = allSubsides.size();
        if ((nbreCaisses != 0) && (nbreSubsides != 0)) {
            if (isSubsidePCFamille) {
                if (nbreCaisses > 1) {
                    if (getIdProcess().contains("DECMST8") || !hasSupplementPC) {
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs1"));
                    } else {
                        toReturn.add(
                                getTableauToKeep("pcftablexdroitsxassureurs1", toReturn.getDatabag().get("RELATIV1")));
                    }
                    toReturn.add(getTableauToKeep("pcftablexdroitsxassureurs2", toReturn.getDatabag().get("RELATIV1")));
                    toReturn.add(getTableauToKeep("pcftablexdroitsxassureurs3", toReturn.getDatabag().get("RELATIV1")));
                    toReturn.add(getTableauToDelete("pcftableundroitunassureur1"));
                    toReturn.add(getTableauToDelete("pcftableundroitunassureur2"));
                    toReturn.add(getTableauToDelete("pcftableundroitunassureur3"));
                    toReturn.add(getTableauToDelete("pcftablexdroitsunassureur1"));
                    toReturn.add(getTableauToDelete("pcftablexdroitsunassureur2"));
                    toReturn.add(getTableauToDelete("pcftablexdroitsunassureur3"));
                } else {
                    if (nbreSubsides > 1) {
                        if (getIdProcess().contains("DECMST8") || !hasSupplementPC) {
                            toReturn.add(getTableauToDelete("pcftablexdroitsunassureur1"));
                        } else {
                            toReturn.add(getTableauToKeep("pcftablexdroitsunassureur1",
                                    toReturn.getDatabag().get("RELATIV1")));
                        }
                        toReturn.add(
                                getTableauToKeep("pcftablexdroitsunassureur2", toReturn.getDatabag().get("RELATIV1")));
                        toReturn.add(
                                getTableauToKeep("pcftablexdroitsunassureur3", toReturn.getDatabag().get("RELATIV1")));
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs1"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs2"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs3"));
                        toReturn.add(getTableauToDelete("pcftableundroitunassureur1"));
                        toReturn.add(getTableauToDelete("pcftableundroitunassureur2"));
                        toReturn.add(getTableauToDelete("pcftableundroitunassureur3"));
                    } else {
                        if (getIdProcess().contains("DECMST8") || !hasSupplementPC) {
                            toReturn.add(getTableauToDelete("pcftableundroitunassureur1"));
                        } else {
                            toReturn.add(getTableauToKeep("pcftableundroitunassureur1",
                                    toReturn.getDatabag().get("RELATIV1")));
                        }
                        toReturn.add(
                                getTableauToKeep("pcftableundroitunassureur2", toReturn.getDatabag().get("RELATIV1")));
                        toReturn.add(
                                getTableauToKeep("pcftableundroitunassureur3", toReturn.getDatabag().get("RELATIV1")));
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs1"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs2"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsxassureurs3"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsunassureur1"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsunassureur2"));
                        toReturn.add(getTableauToDelete("pcftablexdroitsunassureur3"));
                    }
                }
            } else {
                if (nbreCaisses > 1) {
                    toReturn.add(getTableauToKeep("tablexdroitsxassureurs", toReturn.getDatabag().get("RELATIV1")));
                    toReturn.add(getTableauToDelete("tableundroitunassureur"));
                    toReturn.add(getTableauToDelete("tablexdroitsunassureur"));
                } else {
                    if (nbreSubsides > 1) {
                        toReturn.add(getTableauToKeep("tablexdroitsunassureur", toReturn.getDatabag().get("RELATIV1")));
                        toReturn.add(getTableauToDelete("tablexdroitsxassureurs"));
                        toReturn.add(getTableauToDelete("tableundroitunassureur"));
                    } else {
                        toReturn.add(getTableauToKeep("tableundroitunassureur", toReturn.getDatabag().get("RELATIV1")));
                        toReturn.add(getTableauToDelete("tablexdroitsxassureurs"));
                        toReturn.add(getTableauToDelete("tablexdroitsunassureur"));
                    }
                }
            }
        }
        // ---------------------------------------------------------------------------------
        // Job process et signatures
        // ---------------------------------------------------------------------------------
        SimpleControleurJob currentJob = (SimpleControleurJob) getLoadedObject(SimpleControleurJob.class.getName());
        if (currentJob.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())) {
            String userReprise = "";
            SimpleParametreApplicationSearch paramSearch = new SimpleParametreApplicationSearch();
            paramSearch.setForCsTypeParametre(IAMCodeSysteme.AMParametreApplication.USER_REPRISE.getValue());
            try {
                paramSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(paramSearch);
                if (paramSearch.getSize() == 1) {
                    SimpleParametreApplication paramApplication = new SimpleParametreApplication();
                    paramApplication = (SimpleParametreApplication) paramSearch.getSearchResults()[0];
                    userReprise = paramApplication.getValeurParametre();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (JadeStringUtil.isBlankOrZero(userReprise)) {
                userReprise = "jumyb";
            }
            boolean userRepriseExist = false;
            try {
                userRepriseExist = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getUserService()
                        .exists(userReprise);
                if (userRepriseExist == true) {
                    JadeUser[] repriseUsers = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                            .getUserService().findUsersForIdUserLike(userReprise);
                    if (repriseUsers.length > 0) {
                        JadeUser currentRepriseUser = repriseUsers[0];
                        toReturn.addData("REF", currentRepriseUser.getVisa());
                        toReturn.addData("FULLREF",
                                currentRepriseUser.getFirstname() + " " + currentRepriseUser.getLastname());
                        toReturn.addData("TEL", currentRepriseUser.getPhone());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Valeur sur les signets déjà renseignés et signatures
            // toReturn.addData(key, value)
            toReturn.addData("signatures", "mbo");
        } else {
            // Travail sur l'utilisateur pour récupérer la bonne signature
            BSession currentSession = (BSession) getLoadedObject(BSession.class.getName());
            if (currentSession.getUserEMail().toUpperCase().indexOf("PIERRETTE") >= 0) {
                toReturn.addData("signatures", "pbo");
            } else {
                toReturn.addData("signatures", "mbo");
            }
        }
        // ---------------------------------------------------------------------------------
        // Année moins 1 et moins 2
        // ---------------------------------------------------------------------------------
        try {
            String relativ1 = toReturn.getDatabag().get("RELATIV1");
            int iRelativ1 = Integer.parseInt(relativ1);
            toReturn.addData("ANNEEMOINSUN", "" + (iRelativ1 - 1));
            toReturn.addData("ANNEEMOINSDEUX", "" + (iRelativ1 - 2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // ---------------------------------------------------------------------------------
        // Date de fin selon date de début
        // ---------------------------------------------------------------------------------
        try {
            // Depuis les objects chargés, on récupère le current détail famille
            SimpleDetailFamille currentSubside = (SimpleDetailFamille) getLoadedObject(
                    SimpleDetailFamille.class.getName());
            String dateDebut = currentSubside.getDebutDroit();
            String datePrevious = JadeDateUtil.addDays("01." + dateDebut, -1);
            toReturn.addData("DATEFINASSISTE", EnvoiDataFormatter.getFormattedValueDateJJMMMMYYYY(datePrevious));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            // Particularités DECMST10,11,12 et 13
            if (getIdProcess().contains("DECMST10") || getIdProcess().contains("DECMST11")
                    || getIdProcess().contains("DECMST12") || getIdProcess().contains("DECMST13")) {
                SimpleRevenuDeterminant simpleRevenuDeterminant = (SimpleRevenuDeterminant) getLoadedObject(
                        SimpleRevenuDeterminant.class.getName());
                RevenuHistoriqueComplex revenuSearch = AmalServiceLocator.getRevenuService()
                        .readHistoriqueComplex(simpleRevenuDeterminant.getIdRevenuHistorique());

                toReturn.addData("ANNEETAXREC",
                        revenuSearch.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation());
                toReturn.addData("DATAXREC",
                        revenuSearch.getRevenuFullComplex().getSimpleRevenu().getDateAvisTaxation());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logWarn("AMEnvoiData.loadDocumentDataTopaz()", e.getMessage());
        }

        // BEGIN-- S180621_007 modification dans les documents:
        // ATSUBS1, ATSUBS2, ATSUBS3
        // DECMST1, DECMST2, DECMST5, DECMST8,
        // DECMISA, DECMASB, DECMPCE
        if (isSubsidePCFamille) {
            String complement = "_2019";
            checkAndSetIdProcessEndText("ATSUBS1", complement);
            checkAndSetIdProcessEndText("ATSUBS2", complement);
            checkAndSetIdProcessEndText("ATSUBS3", complement);
            checkAndSetIdProcessEndText("DECMST1", complement);
            checkAndSetIdProcessEndText("DECMST2", complement);
            checkAndSetIdProcessEndText("DECMST5", complement);
            checkAndSetIdProcessEndText("DECMST8", complement);
            checkAndSetIdProcessEndText("DECMISA", complement);
            checkAndSetIdProcessEndText("DECMASB", complement);
            checkAndSetIdProcessEndText("DECMPCE", complement);
        }
        // END-- S180621_007 modification dans les documents:

        // ---------------------------------------------------------------------------------
        // Tableau enfants ATENF1, ATENF8
        // ---------------------------------------------------------------------------------
        if (getIdProcess().contains("ATENF")) {
            boolean bFoundAndIsEmpty = false;
            Iterator<Collection> iteratorCollection = toReturn.getCollectionsList().iterator();// .getCollections().iterator();
            while (iteratorCollection.hasNext()) {
                Collection currentCollection = iteratorCollection.next();
                String currentCollectionName = currentCollection.getName();
                if (!JadeStringUtil.isEmpty(currentCollectionName) && currentCollectionName.equals("tableau0")) {
                    if (currentCollection.canRemoveArrayFromDocument()) {
                        bFoundAndIsEmpty = true;
                        break;
                    }
                }
            }
            if (bFoundAndIsEmpty) {
                Collection tableauToKeep = new Collection("tableauenfantvide");
                DataList ligneTableau = new DataList("ligne");
                ligneTableau.addData("CONTENT", "");
                tableauToKeep.add(ligneTableau);
                toReturn.add(tableauToKeep);
            } else {
                Collection tableauToDelete1 = new Collection("tableauenfantvide");
                toReturn.add(tableauToDelete1);
            }
        }
        // ---------------------------------------------------------------------------------
        // Particularité DECMPC7
        // Ajout d'une particularité : DES 2015 (S140922_008) (22.09.2014)
        // Ajout d'une particularité : DES 2016 (S150923_008) (08.10.2015)
        // Ajout d'une particularité : DES 2018 (S171020_001) (27.10.2017)

        // Récapitulatif :
        // 2011 et avant + 2015 ==> DECMPC7_2011
        // 2012 à 2014 ==> DECMPC7 (default)
        // 2016 à 2017 ==> DECMPC7_2016
        // 2018 et après ==> DECMPC7_2018
        // ---------------------------------------------------------------------------------
        if (getIdProcess().contains("DECMPC7")) {
            // Depuis les objects chargés, on récupère le current détail famille
            SimpleDetailFamille currentSubside = (SimpleDetailFamille) getLoadedObject(
                    SimpleDetailFamille.class.getName());
            int iAnneeHistorique = 0;
            String anneHistorique = currentSubside.getAnneeHistorique();
            try {
                iAnneeHistorique = Integer.parseInt(anneHistorique);
                if (iAnneeHistorique < 2012 || iAnneeHistorique == 2015) {
                    setIdProcess("DECMPC7_2011");
                } else if (iAnneeHistorique >= 2016 && iAnneeHistorique < 2018) {
                    setIdProcess("DECMPC7_2016");
                } else if (iAnneeHistorique >= 2018) {
                    setIdProcess("DECMPC7_2018");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // ---------------------------------------------------------------------------------
        // Particularité DECMPCM
        // Ajout d'une particularité : DES 2015 (S140922_008) (22.09.2014)
        // Ajout d'une particularité : DES 2016 (S150923_008) (08.10.2015)
        // Ajout d'une particularité : DES 2018 (S171020_001) (27.10.2017)

        // Récapitulatif :
        // 2011 et avant + 2015 ==> DECMPCM_2011
        // 2012 à 2014 ==> DECMPCM (default)
        // 2016 à 2017 ==> DECMPCM_2016
        // 2018 et après ==> DECMPCM_2018
        // ---------------------------------------------------------------------------------
        if (getIdProcess().contains("DECMPCM")) {
            // Depuis les objects chargés, on récupère le current détail famille
            SimpleDetailFamille currentSubside = (SimpleDetailFamille) getLoadedObject(
                    SimpleDetailFamille.class.getName());
            int iAnneeHistorique = 0;
            String anneHistorique = currentSubside.getAnneeHistorique();
            try {
                iAnneeHistorique = Integer.parseInt(anneHistorique);
                if (iAnneeHistorique < 2012 || iAnneeHistorique == 2015) {
                    setIdProcess("DECMPCM_2011");
                } else if (iAnneeHistorique >= 2016 && iAnneeHistorique < 2018) {
                    setIdProcess("DECMPCM_2016");
                } else if (iAnneeHistorique >= 2018) {
                    setIdProcess("DECMPCM_2018");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ---------------------------------------------------------------------------------
        // Particularité DECMPCM DES 2015 pour les assistés (S140922_008)

        // 08.10.2015 : Ajout d'une particularité dès 2016 (S150923_008)
        // Récapitulatif :
        // 2014 et avant : DECMAS7 (default)
        // 2015 ==> DECMAS7_2015
        // 2016 et après ==> DECMAS7_2016
        // ---------------------------------------------------------------------------------
        if (getIdProcess().contains("DECMAS7")) {
            // Depuis les objects chargés, on récupère le current détail famille
            SimpleDetailFamille currentSubside = (SimpleDetailFamille) getLoadedObject(
                    SimpleDetailFamille.class.getName());
            int iAnneeHistorique = 0;
            String anneHistorique = currentSubside.getAnneeHistorique();
            try {
                iAnneeHistorique = Integer.parseInt(anneHistorique);
                if (iAnneeHistorique == 2015) {
                    setIdProcess("DECMAS7_2015");
                } else if (iAnneeHistorique >= 2016) {
                    setIdProcess("DECMAS7_2016");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ---------------------------------------------------------------------------------
        // Particularité DECMPCM DES 2015 pour les assistés (S140922_008)

        // 08.10.2015 : Ajout d'une particularité dès 2016 (S150923_008)
        // Récapitulatif :
        // 2014 et avant : DECMASM (default)
        // 2015 ==> DECMASM_2015
        // 2016 et après ==> DECMASM_2016
        // ---------------------------------------------------------------------------------
        if (getIdProcess().contains("DECMASM")) {
            // Depuis les objects chargés, on récupère le current détail famille
            SimpleDetailFamille currentSubside = (SimpleDetailFamille) getLoadedObject(
                    SimpleDetailFamille.class.getName());
            int iAnneeHistorique = 0;
            String anneHistorique = currentSubside.getAnneeHistorique();
            try {
                iAnneeHistorique = Integer.parseInt(anneHistorique);
                if (iAnneeHistorique == 2015) {
                    setIdProcess("DECMASM_2015");
                } else if (iAnneeHistorique >= 2016) {
                    setIdProcess("DECMASM_2016");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return toReturn;
    }

    private void checkAndSetIdProcessEndText(String idProcess, String complementProcess) {
        if (getIdProcess().contains(idProcess)) {
            String newId = idProcess + complementProcess;
            setIdProcess(newId);
        }
    }

    private Collection getTableauToDelete(String tableName) {
        return new Collection(tableName);
    }

    private Collection getTableauToKeep(String tableName, String relativeData) {
        Collection tableauToKeep = new Collection(tableName);
        DataList ligneTableau = new DataList("maligne");
        ligneTableau.addData("CONTENT", "");
        ligneTableau.addData("RELATIV1", relativeData);
        tableauToKeep.add(ligneTableau);
        return tableauToKeep;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.envoi.business.utils.EnvoiData#prepareData(java.util.HashMap)
     */
    @Override
    protected ArrayList<Object> prepareData(HashMap<Object, Object> inputMap) {
        ArrayList<Object> objectArray = new ArrayList<Object>();
        // Parcours des éléments et instanciation
        Set<Object> classNames = inputMap.keySet();
        Iterator it = classNames.iterator();
        while (it.hasNext()) {
            String currentClassName = (String) it.next();
            String currentId = (String) inputMap.get(currentClassName);

            Object objectToAdd = null;
            if (Contribuable.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveContribuable(currentId);
            } else if (SimpleDetailFamille.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveSimpleDetailFamille(currentId);
            } else if (SimpleFamille.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveSimpleFamille(currentId);
            } else if (BSession.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveSession(currentId);
            } else if (RevenuHistoriqueComplex.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveRevenuHistorique(currentId);
            } else if (SimpleControleurEnvoiStatus.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveControleurEnvoiStatus(currentId);
            } else if (SimpleControleurJob.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveControleurJob(currentId);
            } else if (SimpleSubsideAnnee.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveSimpleSubsideAnnee(currentId);
            } else if (SimpleDocument.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveHistoriqueEnvoi(currentId);
            } else if (SimpleRevenuDeterminant.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveRevenuDeterminant(currentId);
            }
            if (objectToAdd != null) {
                objectArray.add(objectToAdd);
            }
        }
        return objectArray;
    }

    /**
     * Retrieve du contribuable en fonction de son id
     *
     * @param idContribuable
     * @return
     */
    protected Object retrieveContribuable(String idContribuable) {

        Contribuable currentContribuable = null;
        try {
            currentContribuable = AmalServiceLocator.getContribuableService().read(idContribuable);
            if (currentContribuable.getContribuable().getIdTier() == null) {
                currentContribuable = null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve Contribuable id '" + idContribuable + "'");
            // ex.printStackTrace();
        }
        return currentContribuable;

    }

    /**
     * Retrieve du controleur envoi detail status en fonction de son id
     *
     * @param currentId
     *
     * @return
     */
    protected Object retrieveControleurEnvoiStatus(String currentId) {

        SimpleControleurEnvoiStatus currentEnvoi = null;
        try {
            currentEnvoi = AmalServiceLocator.getControleurEnvoiService().readSimpleEnvoiStatus(currentId);
            if (currentEnvoi.getNoGroupe() != null) {
                return currentEnvoi;
            } else {
                return null;
            }

        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve controlleurEnvoiStatus id '" + currentId + "'");
            ex.printStackTrace();
        }
        return currentEnvoi;

    }

    /**
     *
     * @param currentId
     * @return
     */
    protected Object retrieveControleurJob(String currentId) {
        SimpleControleurJob currentJob = null;
        try {
            currentJob = AmalServiceLocator.getControleurEnvoiService().readSimpleControleurJob(currentId);
            if (currentJob.getDateJob() != null) {
                return currentJob;
            } else {
                return null;
            }

        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve controlleurJob id '" + currentId + "'");
            ex.printStackTrace();
        }
        return currentJob;
    }

    /**
     * Retrieve du revenuDeterminant en fonction de son id
     *
     * @param idSimpleFamille
     * @return
     */
    protected Object retrieveHistoriqueEnvoi(String idEnvoiDocument) {
        SimpleDocument documentEnvoi = null;
        try {
            documentEnvoi = AmalImplServiceLocator.getSimpleDocumentService().read(idEnvoiDocument);
            if (documentEnvoi.getDateEnvoi() != null) {
                return documentEnvoi;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve SimpleDocument id '" + idEnvoiDocument + "'");
            ex.printStackTrace();
        }
        return documentEnvoi;
    }

    /**
     * Retrieve du revenuDeterminant en fonction de son id
     *
     * @param idSimpleFamille
     * @return
     */
    protected Object retrieveRevenuDeterminant(String idRevenuDeterminant) {
        SimpleRevenuDeterminant revenuDeterminant = null;
        try {
            revenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().read(idRevenuDeterminant);
            if (!revenuDeterminant.isNew()) {
                return revenuDeterminant;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve SimpleRevenuDeterminant id '" + idRevenuDeterminant + "'");
            ex.printStackTrace();
        }
        return revenuDeterminant;
    }

    /**
     * Retrieve du simplefamille en fonction de son id
     *
     * @param idSimpleFamille
     * @return
     */
    protected Object retrieveRevenuHistorique(String idRevenuHistorique) {
        RevenuHistoriqueComplex revenuHistorique = null;
        try {
            revenuHistorique = AmalServiceLocator.getRevenuService().readHistoriqueComplex(idRevenuHistorique);
            if (revenuHistorique.getSimpleRevenuHistorique().getAnneeHistorique() != null) {
                return revenuHistorique;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve RevenuHistorique id '" + idRevenuHistorique + "'");
            ex.printStackTrace();
        }
        return revenuHistorique;
    }

    /**
     * Retrieve de la session
     *
     * @param currentUserId
     * @return
     */
    protected Object retrieveSession(String currentUserId) {
        BSession currentSession = null;
        try {
            currentSession = BSessionUtil.getSessionFromThreadContext();
            if (currentSession.getUserId().equals(currentUserId)) {
                return currentSession;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve Session id '" + currentUserId + "'");
            ex.printStackTrace();
        }
        return currentSession;
    }

    /**
     * Retrieve du simpledetailfamille (subside) en fonction de son id
     *
     * @param idSimpleDetailFamille
     * @return
     */
    protected Object retrieveSimpleDetailFamille(String idSimpleDetailFamille) {
        SimpleDetailFamille detailFamille = null;
        try {
            detailFamille = AmalServiceLocator.getDetailFamilleService().read(idSimpleDetailFamille);
            if (detailFamille.getAnneeHistorique() == null) {
                detailFamille = null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve SimpleDetailFamille id '" + idSimpleDetailFamille + "'");
            ex.printStackTrace();
        }
        return detailFamille;
    }

    /**
     * Retrieve du simplefamille en fonction de son id
     *
     * @param idSimpleFamille
     * @return
     */
    protected Object retrieveSimpleFamille(String idSimpleFamille) {
        SimpleFamille famille = null;
        try {
            FamilleContribuable currentFamille = AmalServiceLocator.getFamilleContribuableService()
                    .read(idSimpleFamille);
            if (currentFamille.getSimpleFamille().getNomPrenom() == null) {
                return null;
            } else {
                famille = currentFamille.getSimpleFamille();
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve SimpleFamille id '" + idSimpleFamille + "'");
            ex.printStackTrace();
        }
        return famille;
    }

    /**
     *
     * @param currentId
     * @return
     */
    protected Object retrieveSimpleSubsideAnnee(String currentId) {
        SimpleSubsideAnnee simpleSubsideAnnee = null;
        try {
            simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().read(currentId);
            if (simpleSubsideAnnee.getIdSubsideAnnee() != null) {
                return simpleSubsideAnnee;
            } else {
                return null;
            }

        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve subsideAnnee id '" + currentId + "'");
            ex.printStackTrace();
        }
        return simpleSubsideAnnee;
    }

}
