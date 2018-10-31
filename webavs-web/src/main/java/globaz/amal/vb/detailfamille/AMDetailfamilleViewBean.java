/**
 *
 */
package globaz.amal.vb.detailfamille;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.envois.AMEnvoiData;
import ch.globaz.amal.business.envois.AMEnvoiDataFactory;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.parametreModel.ParametreModelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistorique;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.subsideannee.SimpleSubsideAnneeChecker;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.FormuleListException;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAVector;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.model.document.Document;
import globaz.op.wordml.model.document.WordmlDocument;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;

/**
 * @author DHI
 *
 */
public class AMDetailfamilleViewBean extends BJadePersistentObjectViewBean {

    // store subsides calculs
    private CalculsSubsidesContainer calculs = null;
    // store contribuable information
    private Contribuable contribuable = null;
    // store current subside (detail famille) information
    private SimpleDetailFamille detailFamille = null;
    // store DetailFamilleSearch results
    private SimpleDetailFamilleSearch detailFamilleSearch = null;
    // store current membre famille information
    private FamilleContribuable familleContribuable = null;
    // store FormuleListSearch results
    private FormuleListSearch formuleListSearch = null;
    // store le nom de l'assurance
    private String nomAssurance = null;
    // store parameter model search results
    private ParametreModelComplexSearch parametreModelComplexSearch = null;
    private Boolean savedCodeActifDetailFamille = null;
    // store SimpleDocumentSearch results
    private SimpleDocumentSearch simpleDocumentSearch = null;

    /**
     *
     * Default constructor
     *
     */
    public AMDetailfamilleViewBean() {
        super();
        familleContribuable = new FamilleContribuable();
        contribuable = new Contribuable();
        detailFamille = new SimpleDetailFamille();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     *
     * ADD a new subside
     */
    @Override
    public void add() throws Exception {
        // Set id
        detailFamille.setIdContribuable(getContribuable().getId());
        detailFamille.setIdFamille(getFamilleContribuable().getSimpleFamille().getId());
        // Call service
        detailFamille = AmalServiceLocator.getDetailFamilleService().create(detailFamille);
    }

    public void createInteractivDocument(String noModele) {

    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     *
     * Delete the current subside
     */
    @Override
    public void delete() throws Exception {
        detailFamille = AmalServiceLocator.getDetailFamilleService().delete(detailFamille);
    }

    /**
     *
     * @param simulation
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws RevenuException
     * @throws ControleurJobException
     * @throws AnnonceException
     * @throws ControleurEnvoiException
     * @throws DocumentException
     * @throws FamilleException
     * @throws FormuleListException
     * @throws ParametreModelException
     */
    public void generateSubside() throws DetailFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, RevenuException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException {
        AmalServiceLocator.getDetailFamilleService().generateSubside(getCalculs(), false);
    }

    public boolean isSupplementPCFamille() {
        SimpleDetailFamille detailleFam = getDetailFamille();
        boolean isSubsidePCFamille = false;

        if (detailleFam != null) {
            isSubsidePCFamille = SimpleSubsideAnneeChecker.checkIsSubsidePCFKind(detailleFam);
        }
        return isSubsidePCFamille;
    }

    /**
     * @return the calculs
     */
    public CalculsSubsidesContainer getCalculs() {
        return calculs;
    }

    /**
     * Get the current contribuable information (family responsible)
     *
     * @return
     */
    public Contribuable getContribuable() {
        return contribuable;
    }

    /**
     * Get the current detailFamille information (subside)
     *
     * @return
     */
    public SimpleDetailFamille getDetailFamille() {
        return detailFamille;
    }

    /**
     * @return the detailFamilleSearch
     */
    public SimpleDetailFamilleSearch getDetailFamilleSearch() {
        return detailFamilleSearch;
    }

    /**
     * Get the current famille contribuable information (family member)
     *
     * @return
     */
    public FamilleContribuable getFamilleContribuable() {
        return familleContribuable;
    }

    /**
     * @return the formuleListSearch
     */
    public FormuleListSearch getFormuleListSearch() {
        return formuleListSearch;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     *
     * Get the current subside Id
     */
    @Override
    public String getId() {
        return detailFamille.getId();
    }

    /**
     * Création du nom du document interactif en appelant le service correspondant
     *
     * @param csDateComplete
     * @param modeleId
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public String getInteractivDocumentFileName(String csDateComplete, String noModele)
            throws JadeApplicationServiceNotAvailableException {
        return AmalServiceLocator.getDetailFamilleService().getInteractivDocumentFileName(csDateComplete, getId(),
                noModele, noModele);
    }

    /**
     * @param id
     *            ID du code système
     *
     * @return libelle général du code système correspondant
     *
     */
    public String getLibelleCodeSysteme(String id) {

        if ((id == null) || (id.length() <= 0)) {
            return "";
        }

        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        cm.setSession(getSession());
        cm.setForCodeUtilisateur(id);
        cm.setForIdGroupe("AMMODELES");
        try {
            cm.find();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        FWParametersCode code = (FWParametersCode) cm.getEntity(0);

        if (code == null) {
            return "";
        } else {
            return code.getLibelle();
        }
    }

    /**
     * @param id
     *            ID du code système
     *
     * @return libelle général du code système correspondant
     *
     */
    public JAVector getListeDocuments() {
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        cm.setSession(getSession());
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(getSession().getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return new JAVector();
        }
        JAVector containerCS = cm.getContainer();
        return containerCS;
    }

    /**
     * Retourne le nom de l'assurance liée à ce subside
     *
     * @return
     */
    public String getNomAssurance() {
        if (nomAssurance == null) {
            if (!JadeStringUtil.isBlankOrZero(getDetailFamille().getNoCaisseMaladie())) {
                nomAssurance = this.getNomAssurance(getDetailFamille().getNoCaisseMaladie());
            } else {
                nomAssurance = "";
            }
        }
        return nomAssurance;
    }

    /**
     *
     * @param noCaisse
     *            idTiers de l'assurance
     * @return le nom de l'assurance correspondant à l'id Tiers (nocaisse)
     */
    private String getNomAssurance(String noCaisse) {
        try {
            AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService().read(noCaisse);
            return admin.getAdmin().getCodeAdministration() + " - " + admin.getTiers().getDesignation1() + ","
                    + admin.getId();
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading administration " + noCaisse + " - " + e.getMessage());
            return "";
        }
    }

    /**
     *
     * @param noCaisse
     *            annonce
     * @return le nom clair ou ""
     */
    public String getNomAssuranceAnnonce(String noCaisse) {
        if (getDetailFamille().getNoCaisseMaladie().equals(noCaisse)) {
            return this.getNomAssurance();
        } else {
            return this.getNomAssurance(noCaisse);
        }
    }

    /**
     * Retourne toutes les caisses-maladie présentes dans pyxis
     *
     * @return
     */
    public String[] getNomAssurancesAll() {

        try {
            AdministrationSearchComplexModel allCMSearch = new AdministrationSearchComplexModel();
            allCMSearch.setForGenreAdministration(AMGestionTiers.CS_TYPE_CAISSE_MALADIE);
            allCMSearch = TIBusinessServiceLocator.getAdministrationService().find(allCMSearch);
            ArrayList<String> myArray = new ArrayList<String>();
            for (int iCaisse = 0; iCaisse < allCMSearch.getSize(); iCaisse++) {
                AdministrationComplexModel caisse = (AdministrationComplexModel) allCMSearch
                        .getSearchResults()[iCaisse];
                if ((caisse.getTiers().getInactif() == true) || caisse.getTiers().get_inactif().equals("1")) {
                    continue;
                }
                String toAdd = caisse.getAdmin().getCodeAdministration() + " - " + caisse.getTiers().getDesignation1()
                        + "," + caisse.getId();
                // JadeLogger.info(this, "CM " + caisse.getTiers().getInactif() + " " + caisse.getTiers().get_inactif()
                // + " : " + toAdd);
                myArray.add(toAdd);
            }
            Collections.sort(myArray);
            String[] returnResults = new String[myArray.size()];
            myArray.toArray(returnResults);
            return returnResults;

        } catch (Exception e) {
            JadeLogger.error(this,
                    "Error loading administration " + getDetailFamille().getNoCaisseMaladie() + " - " + e.getMessage());
            return new String[0];
        }
    }

    /**
     * @return the parametreModelComplexSearch
     */
    public ParametreModelComplexSearch getParametreModelComplexSearch() {
        return parametreModelComplexSearch;
    }

    /**
     *
     * @param idPays
     * @return String pays ISO
     */
    public String getPays(String idPays) {

        if (!JadeStringUtil.isBlankOrZero(idPays)) {
            BSession session = getSession();

            TIPaysManager paysManager = new TIPaysManager();
            paysManager.setSession(session);
            paysManager.setForIdPays(idPays);
            try {
                paysManager.find();
                TIPays pays = (TIPays) paysManager.getEntity(0);
                return pays.getCodeIso();
            } catch (Exception e) {
                session.addWarning("Code pays introuvable pour l'idPays : " + idPays);
            }
        }

        return "";

    }

    public Boolean getSavedCodeActifDetailFamille() {
        return savedCodeActifDetailFamille;
    }

    /**
     * Get the linked session
     *
     * @return BSession session
     */
    private BSession getSession() {
        return (BSession) getISession();
    }

    /**
     *
     * @param idSexe
     * @return String 1 letter H/F sexe
     */
    public String getSexe(String idSexe) {
        if ("516001".equals(idSexe)) {
            return "H";
        } else if ("516002".equals(idSexe)) {
            return "F";
        } else {
            return "N/A";
        }

    }

    /**
     * @return the simpleDocumentSearch
     */
    public SimpleDocumentSearch getSimpleDocumentSearch() {
        return simpleDocumentSearch;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     *
     * get the spy
     */
    @Override
    public BSpy getSpy() {
        if (detailFamille != null) {
            return new BSpy(detailFamille.getSpy());
        } else {
            return null;
        }
    }

    /**
     * Merge du document (Wordml) appelé par la servlet action merge
     *
     * @param idFormule
     * @return
     * @throws Exception
     */
    public Object merge(String noFormule) throws Exception {
        // Récupération idFormule depuis son no
        String idFormule = AmalServiceLocator.getDetailFamilleService().getIdFormuleFromNoFormule(noFormule);

        // Préparation de la map nom-classe, id
        HashMap<Object, Object> map = prepareMapIdForFusion();

        // Préparation de l'EnvoiData
        AMEnvoiData currentEnvoiData = AMEnvoiDataFactory.getAMEnvoiData(map, idFormule);

        // Création du document
        Object returnDocument = ENServiceLocator.getDocumentMergeService().createDocument(currentEnvoiData);
        if (returnDocument instanceof WordmlDocument) {
            return returnDocument;
        } else {
            return null;
        }
    }

    /**
     * Préparation du tableau d'id nécessaire au merge du document wordml
     *
     * @param currentDetail
     *            information du document courant
     * @return map renseignée
     */
    private HashMap<Object, Object> prepareMapIdForFusion() {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        // Récupération des données fiscales - revenu historique
        String anneHistorique = getDetailFamille().getAnneeHistorique();
        String idContribuable = getContribuable().getContribuable().getId();
        // Renseigne information de revenu lié
        RevenuHistoriqueSearch revenuSearch = new RevenuHistoriqueSearch();
        revenuSearch.setForAnneeHistorique(anneHistorique);
        revenuSearch.setForRevenuActif(true);
        revenuSearch.setForIdContribuable(idContribuable);
        try {
            revenuSearch = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService().search(revenuSearch);
            if (revenuSearch.getSize() > 0) {
                RevenuHistorique revenu = (RevenuHistorique) revenuSearch.getSearchResults()[0];
                map.put(RevenuHistoriqueComplex.class.getName(), revenu.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Préparation des données à transmettre au formulaire
        // classe - id
        map.put(getContribuable().getClass().getName(), getContribuable().getId());
        map.put(getDetailFamille().getClass().getName(), getDetailFamille().getId());
        map.put(getFamilleContribuable().getSimpleFamille().getClass().getName(),
                getFamilleContribuable().getSimpleFamille().getId());
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        map.put(currentSession.getClass().getName(), currentSession.getUserId());

        return map;

    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     *
     * Read the current subside information Read the current famillecontribuable information Read the current
     * contribuable information
     */
    @Override
    public void retrieve() throws Exception {
        retrieveDetailFamilleContribuable();
        retrieveFamilleContribuable();
        retrieveContribuable();
        retrieveDetailFamilleSearch();
        retrieveFormuleList();
        // prepare the container de calcul à la réception des calculs
        // lors de useraction generate calcul
        calculs = new CalculsSubsidesContainer();

    }

    /**
     * Retrieve Contribuable informations
     *
     * @throws Exception
     */
    public void retrieveContribuable() throws Exception {
        JadeLogger.info(this, "Retrieve Contribuable informations");
        JadeLogger.info(this, "-------------------------------------------------");
        if (detailFamille.getIdContribuable() != null) {
            contribuable = AmalServiceLocator.getContribuableService().read(detailFamille.getIdContribuable());
        }
    }

    /**
     *
     * Retrieve subside (detailfamille) information
     *
     * @throws Exception
     */
    public void retrieveDetailFamilleContribuable() throws Exception {
        JadeLogger.info(this, "Retrieve Detail Famille Contribuable informations");
        JadeLogger.info(this, "-------------------------------------------------");
        detailFamille = AmalServiceLocator.getDetailFamilleService().read(getId());
    }

    /**
     * Retrieve the list of detail famille for a specific annee historique
     *
     * @throws Exception
     */
    public void retrieveDetailFamilleSearch() throws Exception {
        JadeLogger.info(this, "Retrieve Simple Detail Famille informations for current year");
        JadeLogger.info(this, "------------------------------------------------------------");
        if (detailFamille.getIdContribuable() != null) {
            detailFamilleSearch = new SimpleDetailFamilleSearch();
            detailFamilleSearch.setForAnneeHistorique(detailFamille.getAnneeHistorique());
            detailFamilleSearch.setForIdFamille(getDetailFamille().getIdFamille());
            detailFamilleSearch = AmalServiceLocator.getDetailFamilleService().search(detailFamilleSearch);
        }
    }

    /**
     *
     * Retrieve Famille Contribuable informations
     *
     * @throws Exception
     */
    public void retrieveFamilleContribuable() throws Exception {
        JadeLogger.info(this, "Retrieve Famille Contribuable informations");
        JadeLogger.info(this, "-------------------------------------------------");
        if (detailFamille.getIdFamille() != null) {
            familleContribuable = AmalServiceLocator.getFamilleContribuableService().read(detailFamille.getIdFamille());
        }
    }

    public void retrieveFormuleList() {
        JadeLogger.info(this, "Retrieve Formules List informations");
        JadeLogger.info(this, "-------------------------------------------------");
        formuleListSearch = new FormuleListSearch();
        try {
            formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
        } catch (Exception e) {
            JadeLogger.error(this, "Error getting formule list");
        }
        parametreModelComplexSearch = new ParametreModelComplexSearch();
        parametreModelComplexSearch.setWhereKey("basic");
        try {
            parametreModelComplexSearch = AmalServiceLocator.getParametreModelService()
                    .search(parametreModelComplexSearch);
        } catch (Exception e) {
            JadeLogger.error(this, "Error getting parameter model list");
        }
    }

    /**
     * @param calculs
     *            the calculs to set
     */
    public void setCalculs(CalculsSubsidesContainer calculs) {
        this.calculs = calculs;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        detailFamille.setId(newId);
    }

    /**
     * Set the contribuable Id (case : add new subside) Please invoke retrievecontribuable after
     *
     * @param newId
     */
    public void setIdContribuable(String newId) {
        detailFamille.setIdContribuable(newId);
    }

    /**
     * Set the family id (case : add new subside) Please invoke retrieveFamille after
     *
     * @param newId
     */
    public void setIdFamille(String newId) {
        detailFamille.setIdFamille(newId);
    }

    public void setSavedCodeActifDetailFamille(Boolean savedCodeActifDetailFamille) {
        this.savedCodeActifDetailFamille = savedCodeActifDetailFamille;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     *
     * Update the current subside information
     */
    @Override
    public void update() throws Exception {
        detailFamille = AmalServiceLocator.getDetailFamilleService().update(detailFamille);
    }

    /**
     * Inscription de l'étape de création de document dans le status de job
     *
     * @param csDateComplete
     * @param csModele
     * @param csJobType
     * @throws Exception
     */
    public void writeInJobTable(String csDateComplete, String csModele, String csJobType) throws Exception {
        AmalServiceLocator.getDetailFamilleService().writeInJobTable(csDateComplete, csModele, csJobType,
                getDetailFamille().getIdDetailFamille(), 0);
    }

    /**
     * Create the WordML file and put it on a shared folder
     *
     * @return the path where the file is saved
     * @throws JadeApplicationServiceNotAvailableException
     */
    public String writeInteractivDocument(String fileName, Document docToSave)
            throws JadeApplicationServiceNotAvailableException {
        String returnPath = "";
        returnPath = AmalServiceLocator.getDetailFamilleService().writeInteractivDocument(fileName, docToSave);
        return returnPath;
    }

}
