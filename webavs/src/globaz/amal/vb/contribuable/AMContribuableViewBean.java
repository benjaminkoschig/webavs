/**
 * 
 */
package globaz.amal.vb.contribuable;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.calcul.CalculsSubsidesContainer;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.parametreModel.ParametreModelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableOnly;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuableView;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.FormuleListException;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMContribuableViewBean extends BJadePersistentObjectViewBean {
    // Container de calculs de subside
    CalculsSubsidesContainer calculs = null;
    private Contribuable contribuable = null;
    private Contribuable contribuableAll = null;
    private SimpleContribuableInfos contribuableInfos = null;
    private ContribuableRCListe contribuableRcListe = null;
    private List<Contribuable> listeFamille = new ArrayList<Contribuable>();
    // Get all subsides informations
    private List<FamilleContribuableView> listeFamilleContribuableView = new ArrayList<FamilleContribuableView>();

    // Get all subsides informations by year
    protected Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewAnnee = new HashMap<String, List<FamilleContribuableView>>();

    // Get all subsides informations by family member
    private Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewMember = new HashMap<String, List<FamilleContribuableView>>();

    private List<SimpleFamille> listeMemberFamille = new ArrayList<SimpleFamille>();

    private SimpleFamille membreFamille = null;

    // Revenus (Données financières - taxations)
    private RevenuSearch revenusContribuable = null;
    // Revenus Historiques
    private RevenuHistoriqueSearch revenusHistoriquesContribuable = null;

    /**
	 * 
	 */
    public AMContribuableViewBean() {
        super();
        contribuable = new Contribuable();
        contribuableRcListe = new ContribuableRCListe();
        revenusContribuable = new RevenuSearch();
        revenusHistoriquesContribuable = new RevenuHistoriqueSearch();
    }

    public AMContribuableViewBean(Contribuable contribuable) {
        this();
        this.contribuable = contribuable;
    }

    public AMContribuableViewBean(ContribuableRCListe contribuable) {
        this();
        contribuableRcListe = contribuable;
        this.contribuable.setPersonneEtendueComplexModel(contribuable.getPersonneEtendue());
    }

    @Override
    public void add() throws Exception {
        contribuable = AmalServiceLocator.getContribuableService().create(contribuable);
    }

    private void copyMembresFamilles(String idDossierBase, String idDossierAFusionner) throws FamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, CloneNotSupportedException,
            DetailFamilleException {
        SimpleFamilleSearch sfBaseSearch = new SimpleFamilleSearch();
        sfBaseSearch.setForIdContribuable(idDossierBase);
        sfBaseSearch = AmalImplServiceLocator.getSimpleFamilleService().search(sfBaseSearch);

        SimpleFamilleSearch sfAFusionnerSearch = new SimpleFamilleSearch();
        sfAFusionnerSearch.setForIdContribuable(idDossierAFusionner);
        sfAFusionnerSearch = AmalImplServiceLocator.getSimpleFamilleService().search(sfAFusionnerSearch);

        for (JadeAbstractModel model : sfAFusionnerSearch.getSearchResults()) {
            SimpleFamille sfAFusionner = (SimpleFamille) model;

            // Le membre n'a pas de tiers rattaché, on n'en tient donc pas compte.
            if ((sfAFusionner.getIdTier() == null) || JadeStringUtil.isBlankOrZero(sfAFusionner.getIdTier())) {
                continue;
            }
            //
            boolean memberFound = false;
            for (JadeAbstractModel model2 : sfBaseSearch.getSearchResults()) {
                SimpleFamille sfBase = (SimpleFamille) model2;

                if (sfAFusionner.getIdTier().equals(sfBase.getIdTier())) {
                    copySubsides(idDossierBase, idDossierAFusionner, sfBase.getIdFamille(), sfAFusionner.getIdFamille());

                    memberFound = true;
                    break;
                }
            }

            if (!memberFound) {
                SimpleFamille mfCopy = new SimpleFamille();
                mfCopy = sfAFusionner.clone();
                mfCopy.setIdContribuable(idDossierBase);
                mfCopy = AmalImplServiceLocator.getSimpleFamilleService().create(mfCopy);

                copySubsides(idDossierBase, idDossierAFusionner, mfCopy.getIdFamille(), sfAFusionner.getIdFamille());
            }
        }
    }

    private void copyRevenus(String idDossierBase, String idDossierAFusionner) throws JadePersistenceException,
            RevenuException, JadeApplicationServiceNotAvailableException, CloneNotSupportedException {
        RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearchAFusionner = new RevenuHistoriqueComplexSearch();
        revenuHistoriqueComplexSearchAFusionner.setForIdContribuable(idDossierAFusionner);
        revenuHistoriqueComplexSearchAFusionner = AmalServiceLocator.getRevenuService().search(
                revenuHistoriqueComplexSearchAFusionner);

        for (JadeAbstractModel modelRevenuAFusionner : revenuHistoriqueComplexSearchAFusionner.getSearchResults()) {
            RevenuHistoriqueComplex revenuAFusionner = (RevenuHistoriqueComplex) modelRevenuAFusionner;

            RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearchBase = new RevenuHistoriqueComplexSearch();
            revenuHistoriqueComplexSearchBase.setForIdContribuable(idDossierBase);
            revenuHistoriqueComplexSearchBase.setForAnneeHistorique(revenuAFusionner.getSimpleRevenuHistorique()
                    .getAnneeHistorique());
            revenuHistoriqueComplexSearchBase = AmalServiceLocator.getRevenuService().search(
                    revenuHistoriqueComplexSearchBase);

            if (revenuHistoriqueComplexSearchBase.getSize() == 0) {
                // Le revenu n'existe pas et doit être créé
                RevenuFullComplex revenuFullComplexCreated = copyTaxations(idDossierBase,
                        revenuAFusionner.getRevenuFullComplex());

                if (IAMCodeSysteme.AMTypeSourceTaxation.MANUELLE.getValue().equals(
                        revenuAFusionner.getRevenuFullComplex().getSimpleRevenu().getTypeSource())
                        || JadeStringUtil.isBlankOrZero(revenuAFusionner.getRevenuFullComplex().getSimpleRevenu()
                                .getTypeSource())) {
                    revenuFullComplexCreated.getSimpleRevenu().setTypeSource(
                            IAMCodeSysteme.AMTypeSourceTaxation.FUSION_MANU.getValue());
                } else {
                    revenuFullComplexCreated.getSimpleRevenu().setTypeSource(
                            IAMCodeSysteme.AMTypeSourceTaxation.FUSION_AUTO.getValue());
                }

                revenuFullComplexCreated = AmalServiceLocator.getRevenuService().create(revenuFullComplexCreated);

                RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
                revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplexCreated);

                RevenuHistoriqueComplex revenuHistoriqueComplexToCreate = new RevenuHistoriqueComplex();
                revenuHistoriqueComplexToCreate.setRevenuFullComplex(revenuFullComplexCreated);

                revenuHistoriqueComplexToCreate.getSimpleRevenuHistorique().setAnneeHistorique(
                        revenuAFusionner.getSimpleRevenuHistorique().getAnneeHistorique());

                revenuHistoriqueComplexToCreate.getSimpleRevenuHistorique().setIdRevenu(
                        revenuFullComplexCreated.getSimpleRevenu().getIdRevenu());

                revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplexToCreate);
            } else {
                // TODO Juste créer la taxation si elle est différente ?
                boolean taxationExist = false;
                for (JadeAbstractModel modelRevenuBase : revenuHistoriqueComplexSearchBase.getSearchResults()) {
                    RevenuHistoriqueComplex revenuBase = (RevenuHistoriqueComplex) modelRevenuBase;

                    if (!revenuBase.getRevenuFullComplex().getSimpleRevenu().getIsSourcier()) {
                        boolean sameYear = revenuAFusionner.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation()
                                .equals(revenuBase.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation());
                        boolean sameRevenuImposable = revenuAFusionner
                                .getRevenuFullComplex()
                                .getSimpleRevenuContribuable()
                                .getRevenuImposable()
                                .equals(revenuBase.getRevenuFullComplex().getSimpleRevenuContribuable()
                                        .getRevenuImposable());
                        if (sameYear && sameRevenuImposable) {
                            taxationExist = true;
                            break;
                        }
                    }

                }

                if (!taxationExist) {
                    // Le revenu n'existe pas et doit être créé
                    RevenuFullComplex revenuFullComplexCreated = copyTaxations(idDossierBase,
                            revenuAFusionner.getRevenuFullComplex());

                    if (IAMCodeSysteme.AMTypeSourceTaxation.MANUELLE.getValue().equals(
                            revenuAFusionner.getRevenuFullComplex().getSimpleRevenu().getTypeSource())
                            || JadeStringUtil.isBlankOrZero(revenuAFusionner.getRevenuFullComplex().getSimpleRevenu()
                                    .getTypeSource())) {
                        revenuFullComplexCreated.getSimpleRevenu().setTypeSource(
                                IAMCodeSysteme.AMTypeSourceTaxation.FUSION_MANU.getValue());
                    } else {
                        revenuFullComplexCreated.getSimpleRevenu().setTypeSource(
                                IAMCodeSysteme.AMTypeSourceTaxation.FUSION_AUTO.getValue());
                    }

                    revenuFullComplexCreated = AmalServiceLocator.getRevenuService().create(revenuFullComplexCreated);
                }
            }
        }
    }

    private void copySubsides(String idDossierBase, String idDossierAFusionner, String idFamilleBase,
            String idFamilleAFusionner) throws DetailFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, CloneNotSupportedException {

        SimpleDetailFamilleSearch sdfAFusionnerSearch = new SimpleDetailFamilleSearch();
        sdfAFusionnerSearch.setForIdContribuable(idDossierAFusionner);
        sdfAFusionnerSearch.setForIdFamille(idFamilleAFusionner);
        sdfAFusionnerSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(sdfAFusionnerSearch);

        for (JadeAbstractModel model_sdf_A_Fusionner : sdfAFusionnerSearch.getSearchResults()) {
            SimpleDetailFamille sdfAFusionner = (SimpleDetailFamille) model_sdf_A_Fusionner;

            SimpleDetailFamille subsideToCreate = new SimpleDetailFamille();
            subsideToCreate = sdfAFusionner.clone();
            subsideToCreate.setIdContribuable(idDossierBase);

            SimpleDetailFamilleSearch sdfSearch = new SimpleDetailFamilleSearch();
            sdfSearch.setForAnneeHistorique(sdfAFusionner.getAnneeHistorique());
            sdfSearch.setForIdContribuable(idDossierBase);
            sdfSearch.setForIdFamille(idFamilleBase);
            if (AmalImplServiceLocator.getSimpleDetailFamilleService().count(sdfSearch) > 0) {
                subsideToCreate.setCodeActif(false);
            } else {
                subsideToCreate.setCodeActif(true);
            }
            subsideToCreate.setIdFamille(idFamilleBase);
            subsideToCreate.setIdDetailFamille(null);
            subsideToCreate = AmalImplServiceLocator.getSimpleDetailFamilleService().create(subsideToCreate);
        }
    }

    private RevenuFullComplex copyTaxations(String idDossierBase, RevenuFullComplex revenuFullComplex)
            throws CloneNotSupportedException, RevenuException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        RevenuFullComplex revenuFullComplexToCreate = new RevenuFullComplex();

        SimpleRevenu simpleRevenuCopy = revenuFullComplex.getSimpleRevenu().clone();
        simpleRevenuCopy.setIdRevenu(null);
        simpleRevenuCopy.setIdContribuable(idDossierBase);

        SimpleRevenuContribuable simpleRevenuContribuableCopy = revenuFullComplex.getSimpleRevenuContribuable().clone();
        simpleRevenuContribuableCopy.setIdRevenuContribuable(null);

        SimpleRevenuSourcier simpleRevenuSourcierCopy = revenuFullComplex.getSimpleRevenuSourcier().clone();
        simpleRevenuSourcierCopy.setIdRevenuSourcier(null);

        revenuFullComplexToCreate.setSimpleRevenu(simpleRevenuCopy);
        revenuFullComplexToCreate.setSimpleRevenuContribuable(simpleRevenuContribuableCopy);
        revenuFullComplexToCreate.setSimpleRevenuSourcier(simpleRevenuSourcierCopy);

        return revenuFullComplexToCreate;
    }

    @Override
    public void delete() throws Exception {
        contribuable = AmalServiceLocator.getContribuableService().delete(contribuable);
    }

    public void fusionDossier(String idDossierBase, String idDossierAFusionner) throws Exception {
        try {
            copyRevenus(idDossierBase, idDossierAFusionner);
            copyMembresFamilles(idDossierBase, idDossierAFusionner);

            SimpleContribuable contribuableToDisable = new SimpleContribuable();
            contribuableToDisable = AmalImplServiceLocator.getSimpleContribuableService().read(idDossierAFusionner);
            contribuableToDisable.setIsContribuableActif(false);
            contribuableToDisable.setIdContribuableFusion(idDossierBase);
            contribuableToDisable = AmalImplServiceLocator.getSimpleContribuableService().update(contribuableToDisable);

        } catch (Exception e) {
            JadeThread.logError("Error", e.getMessage());
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public void generateSubside() throws DetailFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, RevenuException, DocumentException, ControleurEnvoiException, AnnonceException,
            ControleurJobException, FamilleException, ParametreModelException, FormuleListException {
        AmalServiceLocator.getDetailFamilleService().generateSubside(getCalculs(), false);
    }

    /**
     * Récupération des annonces Sedex pour le dossier en cours
     * 
     * @return
     */
    public ComplexAnnonceSedexSearch getAnnonceSedex() {
        ComplexAnnonceSedexSearch currentSearch = new ComplexAnnonceSedexSearch();
        String idContribuable = getId();
        currentSearch.setOrderKey("orderByIdDetFamDESC_numDecisionDESC_msgSubTypeASc");
        currentSearch.setForSDXIdContribuable(idContribuable);
        try {
            currentSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(currentSearch);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentSearch;
    }

    /**
     * @return the calculs
     */
    public CalculsSubsidesContainer getCalculs() {
        return calculs;
    }

    /**
     * @return the contribuable
     */
    public Contribuable getContribuable() {
        return contribuable;
    }

    public SimpleContribuableInfos getContribuableInfos() {
        return contribuableInfos;
    }

    public ContribuableRCListe getContribuableRcListe() {
        return contribuableRcListe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return contribuable.getId();
    }

    public HashMap<String, String> getIdDetailFamilleSedex() {
        ComplexAnnonceSedexSearch sedexSearch = new ComplexAnnonceSedexSearch();
        String idContribuable = getId();
        sedexSearch.setOrderKey("orderByIdDetFamDESC_numDecisionDESC_msgSubTypeASc");
        sedexSearch.setForSDXIdContribuable(idContribuable);
        try {
            sedexSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(sedexSearch);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        HashMap<String, String> arrayIdDetailsFamilles = new HashMap<String, String>();
        for (JadeAbstractModel abstractSimpleAnnonceSedex : sedexSearch.getSearchResults()) {
            ComplexAnnonceSedex complexAnnonceSedex = (ComplexAnnonceSedex) abstractSimpleAnnonceSedex;
            String idDetailFamille = complexAnnonceSedex.getSimpleDetailFamille().getIdDetailFamille();
            String nom = complexAnnonceSedex.getSimpleFamille().getNomPrenom();
            String annee = complexAnnonceSedex.getSimpleDetailFamille().getAnneeHistorique();
            String debutPeriode = complexAnnonceSedex.getSimpleDetailFamille().getDebutDroit();
            String finPeriode = complexAnnonceSedex.getSimpleDetailFamille().getFinDroit();
            if (JadeStringUtil.isBlankOrZero(finPeriode)) {
                finPeriode = "12." + complexAnnonceSedex.getSimpleDetailFamille().getAnneeHistorique();
            }

            if (!arrayIdDetailsFamilles.containsKey(idDetailFamille)) {
                arrayIdDetailsFamilles.put(idDetailFamille, annee + " - " + nom + " (" + debutPeriode + " - "
                        + finPeriode + ")");
            }
        }

        return arrayIdDetailsFamilles;
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
        cm.setForIdLangue(getSession().getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
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
     * @return the listeContribuable
     */
    public List getListeContribuables() {
        return listeFamille;
    }

    public List<Contribuable> getListeFamille() {
        return listeFamille;
    }

    /**
     * @return the listeFamilleContribuableView
     */
    public List<FamilleContribuableView> getListeFamilleContribuableView() {
        return listeFamilleContribuableView;
    }

    /**
     * @return the listeFamilleContribuableViewAnnee
     */
    public Map<String, List<FamilleContribuableView>> getListeFamilleContribuableViewAnnee() {
        return listeFamilleContribuableViewAnnee;
    }

    /**
     * @return the listeFamilleContribuableViewMember
     */
    public Map<String, List<FamilleContribuableView>> getListeFamilleContribuableViewMember() {
        return listeFamilleContribuableViewMember;
    }

    public List<SimpleFamille> getListeMemberFamille() {
        return listeMemberFamille;
    }

    public SimpleFamille getMembreFamille() {
        return membreFamille;
    }

    public PersonneEtendueComplexModel getPersonneEtendueContribuable()
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        PersonneEtendueComplexModel contrib = TIBusinessServiceLocator.getPersonneEtendueService().read(
                getContribuable().getContribuable().getIdTier());

        return contrib;
    }

    /**
     * Gets the revenus (taxations) for the current contribuable
     * 
     * @return
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public RevenuSearch getRevenus() throws RevenuException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        return revenusContribuable;
    }

    public RevenuSearch getRevenusContribuable() {
        return revenusContribuable;
    }

    /**
     * Gets the revenus historiques (link annee subside, rev det, revenu(taxation))
     * 
     * @return
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public RevenuHistoriqueSearch getRevenusHistoriques() throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        return revenusHistoriquesContribuable;
    }

    public RevenuHistoriqueSearch getRevenusHistoriquesContribuable() {
        return revenusHistoriquesContribuable;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if ((contribuable != null) && (contribuable.getContribuable() != null)) {
            return new BSpy(contribuable.getSpy());
        } else {
            return null;
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public void loadHistoriqueFamille(String idContribuable) throws Exception {
        setId(idContribuable);
        SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
        familleSearch.setForIdContribuable(getId());
        familleSearch = AmalServiceLocator.getFamilleContribuableService().search(familleSearch);

        // Get an empty list
        listeMemberFamille = new ArrayList();

        for (Iterator it = Arrays.asList(familleSearch.getSearchResults()).iterator(); it.hasNext();) {
            membreFamille = (SimpleFamille) it.next();
            listeMemberFamille.add(membreFamille);
            if (membreFamille.getIsContribuable()) {
                setContribuable(AmalServiceLocator.getContribuableService().read(membreFamille.getIdContribuable()));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        RetrieveContribuableList();
        RetrieveFamilleContribuableViewList();
        RetrieveRevenuList();
        // Setting the adresse
        try {
            contribuable.setAdresseComplexModel(AmalServiceLocator.getContribuableService().getContribuableAdresse(
                    contribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers()));
        } catch (Exception e) {
            JadeLogger.error(this, "Error Loading adresse id cont : " + contribuable.getId() + " _ " + e.getMessage());
        }
        // Setting the histo numero
        try {
            contribuable.setHistoNumeroContribuable(AmalServiceLocator.getContribuableService()
                    .getContribuableHistoriqueNoContribuable(
                            contribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers()));
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Error Loading histo numéro contribuable : " + contribuable.getId() + " _ " + e.getMessage());
        }
        // prepare the container de calcul à la réception des calculs
        // lors de useraction generate calcul
        calculs = new CalculsSubsidesContainer();
    }

    /**
     * Retrieve the family member
     */
    protected void RetrieveContribuableList() throws Exception {
        // Retrieve Membre Famille
        ContribuableSearch search = new ContribuableSearch();
        String idContribuable = getId();

        if (JadeStringUtil.isBlankOrZero(idContribuable)) {
            try {
                String forNoContribuable = getContribuable().getContribuable().getNoContribuable();
                // ndc = JadeStringUtil.substring(ndc, 0, 3) + "." + JadeStringUtil.substring(ndc, 3, 3) + "."
                // + JadeStringUtil.substring(ndc, 6, 3) + "." + JadeStringUtil.substring(ndc, 9, 2);
                if (!JadeStringUtil.isBlankOrZero(forNoContribuable)) {
                    SimpleContribuableSearch simpleContribuableSearch = new SimpleContribuableSearch();
                    simpleContribuableSearch.setForNoContribuable(forNoContribuable);
                    simpleContribuableSearch = AmalImplServiceLocator.getSimpleContribuableService().search(
                            simpleContribuableSearch);

                    if (simpleContribuableSearch.getSize() > 0) {
                        SimpleContribuable simpleContribuable = (SimpleContribuable) simpleContribuableSearch
                                .getSearchResults()[0];
                        idContribuable = simpleContribuable.getIdContribuable();
                    }
                }
            } catch (Exception e) {
                JadeThread.logError(this.getClass().getName(),
                        "Erreur RetrieveContribuableList() --> " + e.getMessage());
            }
        }
        search.setForIdContribuable(idContribuable);
        search = AmalServiceLocator.getContribuableService().search(search);

        // Set first contribuable (to get after the id and the idtiers of the main contribuable)
        ContribuableOnly resultContribuableSeul = null;
        if (search.getSearchResults().length > 0) {
            resultContribuableSeul = (ContribuableOnly) search.getSearchResults()[0];
        }

        // Create new empty table
        listeFamille = new ArrayList<Contribuable>();
        // Gets the family member
        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            // Fill the famille
            contribuableAll = (Contribuable) it.next();
            listeFamille.add(contribuableAll);
            // Set contribuable (compare the id tiers of contribuable entity and current member)
            if ((null != resultContribuableSeul)
                    && resultContribuableSeul.getContribuable().getIdTier()
                            .equals(contribuableAll.getPersonneEtendue().getPersonneEtendue().getIdTiers())) {
                setContribuable(contribuableAll);
            }
        }
    }

    protected void RetrieveFamilleContribuableViewList() throws Exception {
        // Retrieve Detail Famille
        FamilleContribuableViewSearch famillesearch = new FamilleContribuableViewSearch();
        // Set searched parameters
        famillesearch.setForIdContribuable(contribuable.getId());
        // Set order by AnneeHistorique DESC, PereMereEnfant ASC
        famillesearch.setOrderKey("revenuHistorique");
        // Perform search
        famillesearch = AmalServiceLocator.getFamilleContribuableService().search(famillesearch);

        // Create Arrays to get empty list at start
        listeFamilleContribuableView = new ArrayList<FamilleContribuableView>();
        listeFamilleContribuableViewAnnee = new HashMap<String, List<FamilleContribuableView>>();
        listeFamilleContribuableViewMember = new HashMap<String, List<FamilleContribuableView>>();

        // return results
        for (Iterator it = Arrays.asList(famillesearch.getSearchResults()).iterator(); it.hasNext();) {
            FamilleContribuableView familleContribuableView = (FamilleContribuableView) it.next();
            listeFamilleContribuableView.add(familleContribuableView);

            // Fill the Map by year and member, if we have data by anneehistorique
            String anneeHistorique = familleContribuableView.getAnneeHistorique();
            if ((null != anneeHistorique) && (anneeHistorique.length() > 0)) {

                // Fill the Map by year
                List<FamilleContribuableView> currentList = listeFamilleContribuableViewAnnee.get(anneeHistorique);
                if (null == currentList) {
                    // create annee historique key and related arraylist
                    currentList = new ArrayList<FamilleContribuableView>();
                }
                // add and put result in the map
                currentList.add(familleContribuableView);
                listeFamilleContribuableViewAnnee.put(anneeHistorique, currentList);

                // Fill the Map by member
                // String memberId = familleContribuableView.getNumAvsActuel();
                String memberId = familleContribuableView.getFamilleId();
                // si pas de no AVS, prendre nom-prenom
                if ((null == memberId) || (memberId.length() == 0)) {
                    memberId = familleContribuableView.getNomPrenom();
                }
                List<FamilleContribuableView> currentListMember = listeFamilleContribuableViewMember.get(memberId);
                if (null == currentListMember) {
                    // create annee historique key and related arraylist
                    currentListMember = new ArrayList<FamilleContribuableView>();
                }
                // add and put result in the map
                currentListMember.add(familleContribuableView);
                listeFamilleContribuableViewMember.put(memberId, currentListMember);
            }

        }
    }

    /**
     * Retrieve the Revenu List
     */
    protected void RetrieveRevenuList() throws Exception {
        // Retrieve Revenus (Taxation) list
        revenusContribuable = new RevenuSearch();
        revenusContribuable.setForIdContribuable(getId());
        revenusContribuable = AmalServiceLocator.getRevenuService().search(revenusContribuable);
        // Retrieve Revenus Historiques list
        revenusHistoriquesContribuable = new RevenuHistoriqueSearch();
        revenusHistoriquesContribuable.setForIdContribuable(getId());
        revenusHistoriquesContribuable.setForRevenuActif(true);
        revenusHistoriquesContribuable = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService().search(
                revenusHistoriquesContribuable);
    }

    /**
     * @param calculs
     *            the calculs to set
     */
    public void setCalculs(CalculsSubsidesContainer calculs) {
        this.calculs = calculs;
    }

    /**
     * @param contribuable
     *            the contribuable to set
     */
    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public void setContribuableInfos(SimpleContribuableInfos contribuableInfos) {
        this.contribuableInfos = contribuableInfos;
    }

    public void setContribuableRcListe(ContribuableRCListe contribuableRcListe) {
        this.contribuableRcListe = contribuableRcListe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        contribuable.setId(newId);
    }

    public void setListeFamille(List<Contribuable> listeFamille) {
        this.listeFamille = listeFamille;
    }

    public void setListeFamilleContribuableView(List<FamilleContribuableView> listeFamilleContribuableView) {
        this.listeFamilleContribuableView = listeFamilleContribuableView;
    }

    /**
     * @return the listeFamilleContribuableViewAnnee
     */
    public void setListeFamilleContribuableViewAnnee(
            Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewAnnee) {
        this.listeFamilleContribuableViewAnnee = listeFamilleContribuableViewAnnee;
    }

    public void setListeFamilleContribuableViewMember(
            Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewMember) {
        this.listeFamilleContribuableViewMember = listeFamilleContribuableViewMember;
    }

    public void setListeMemberFamille(List<SimpleFamille> listeMemberFamille) {
        this.listeMemberFamille = listeMemberFamille;
    }

    public void setMembreFamille(SimpleFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setRevenusContribuable(RevenuSearch revenusContribuable) {
        this.revenusContribuable = revenusContribuable;
    }

    public void setRevenusHistoriquesContribuable(RevenuHistoriqueSearch revenusHistoriquesContribuable) {
        this.revenusHistoriquesContribuable = revenusHistoriquesContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }
}
