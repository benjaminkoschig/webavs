/**
 *
 */
package globaz.amal.vb.revenuHistorique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableOnly;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;

/**
 * @author dhi
 *
 *         ViewBean for revenuhistorique management (revenu - annee historique - reve det)
 *
 */
public class AMRevenuHistoriqueViewBean extends BJadePersistentObjectViewBean {
    private int anneeOld = 2007;
    private Contribuable contribuable = null;
    private ArrayList<Revenu> listTaxations = null;
    String[] navigIds = new String[2];
    private ArrayList<String> navigList = new ArrayList<String>();
    private RevenuHistoriqueComplex revenuHistoriqueComplex = null;
    private RevenuHistoriqueSearch revenuHistoriqueSearch = null;
    private HashMap<String, String> rubriqueMap = null;

    private HashMap<String, String> rubriqueMapOld = null;

    /**
     * Default constructor
     *
     */
    public AMRevenuHistoriqueViewBean() {
        revenuHistoriqueComplex = new RevenuHistoriqueComplex();
        contribuable = new Contribuable();
        if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getIdContribuable() != null) {
            setIdContribuable(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getIdContribuable());
        }
    }

    /**
     * Constructor with parameters
     *
     * @param revenuHistoriqueComplex
     */
    public AMRevenuHistoriqueViewBean(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        super();
        this.revenuHistoriqueComplex = revenuHistoriqueComplex;
        contribuable = new Contribuable();
        if (this.revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getIdContribuable() != null) {
            setIdContribuable(
                    this.revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getIdContribuable());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().create(revenuHistoriqueComplex);
        navigList = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().delete(revenuHistoriqueComplex);
        navigList = null;
    }

    /**
     * Get the contribuable
     *
     * @return
     */
    public Contribuable getContribuable() {
        return contribuable;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return revenuHistoriqueComplex.getId();
    }

    /**
     * Gets the id for next year
     *
     * @return
     */
    public String getIdRevenuAnneeNext() {
        try {
            return navigIds[1];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets the id for previous year
     *
     * @return
     */
    public String getIdRevenuAnneePrec() {
        try {
            return navigIds[0];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return the listTaxations
     */
    public ArrayList<Revenu> getListTaxations() {
        if (listTaxations == null) {
            try {
                retrieveTaxationsList();
            } catch (Exception ex) {
                listTaxations = new ArrayList<Revenu>();
            }
        }
        return listTaxations;
    }

    /**
     * Generate Id map for taxation navigation (previous and next year)
     *
     */
    public void getNavigation() {
        SimpleRevenuHistoriqueSearch simpleRevenuHistoriqueSearch = new SimpleRevenuHistoriqueSearch();
        simpleRevenuHistoriqueSearch.setForIdContribuable(contribuable.getId());
        simpleRevenuHistoriqueSearch.setForRevenuActif(true);
        simpleRevenuHistoriqueSearch.setOrderKey("revenuNavigation");
        try {
            simpleRevenuHistoriqueSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService()
                    .search(simpleRevenuHistoriqueSearch);

            for (int i = 0; i < simpleRevenuHistoriqueSearch.getSearchResults().length; i++) {
                SimpleRevenuHistorique simpleRevenuHistorique = (SimpleRevenuHistorique) simpleRevenuHistoriqueSearch
                        .getSearchResults()[i];
                navigList.add(simpleRevenuHistorique.getIdRevenuHistorique());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage());
        }
        try {
            int index = navigList.indexOf(getId());
            int indexPrev = index - 1;
            int indexNext = index + 1;
            if (index > -1) {
                if (indexPrev >= 0) {
                    navigIds[0] = navigList.get(indexPrev);
                } else {
                    navigIds[0] = "";
                }
                if (indexNext <= navigList.size() - 1) {
                    navigIds[1] = navigList.get(indexNext);
                } else {
                    navigIds[1] = "";
                }
            } else {
                navigIds[0] = "";
                navigIds[1] = "";
            }
        } catch (Exception e) {
            navigIds[0] = "";
            navigIds[1] = "";
        }
    }

    /**
     * Retrouve un paramètre avec l'année et le code type
     *
     * @param _type
     * @param _annee
     * @return la valeur du paramètre
     */
    public String getParametreAnnuel(String _type, String _annee) {
        SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
        simpleParametreAnnuelSearch.setForCodeTypeParametre(_type);
        if (!JadeStringUtil.isBlankOrZero(_annee)) {
            simpleParametreAnnuelSearch.setForAnneeParametre(_annee);
        }
        try {
            simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService()
                    .search(simpleParametreAnnuelSearch);
        } catch (Exception e) {
            JadeLogger.error(this, "Error while searching ParametreAnnuel (codeType : " + _type + " / annee " + _annee
                    + ") : " + e.getMessage());
        }
        if (simpleParametreAnnuelSearch.getSearchResults().length > 0) {
            SimpleParametreAnnuel simpleParametreAnnuel = (SimpleParametreAnnuel) simpleParametreAnnuelSearch
                    .getSearchResults()[0];
            return simpleParametreAnnuel.getValeurParametre();
        } else {
            JadeLogger.error(this, "No parameter founded with codeType : " + _type + " and annee " + _annee + ") ");
            return "";
        }
    }

    /**
     * Find a country from a id pays
     *
     * @param idPays
     * @return
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

    /**
     * Get the linked revenu historique complex
     *
     * @return
     */
    public RevenuHistoriqueComplex getRevenuHistoriqueComplex() {
        return revenuHistoriqueComplex;
    }

    /**
     * @return the revenuHistoriqueSearch
     */
    public RevenuHistoriqueSearch getRevenuHistoriqueSearch() {
        if (revenuHistoriqueSearch == null) {
            try {
                retrieveHistoriqueList();
            } catch (Exception ex) {
                revenuHistoriqueSearch = new RevenuHistoriqueSearch();
            }
        }
        return revenuHistoriqueSearch;
    }

    /**
     * Gets the taxation rubrique
     *
     * @param propertyName
     * @param anneeHistorique
     * @return
     */
    public String getRubrique(String propertyName, String anneeHistorique) {
        if (rubriqueMap == null) {
            rubriqueMap = new HashMap<String, String>();
            rubriqueMapOld = new HashMap<String, String>();
            SimpleParametreAnnuelSearch rubriqueSearch = new SimpleParametreAnnuelSearch();
            List<String> propertyList = new ArrayList<String>();
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_ALLOCATIONFAMILLE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_DEDUCAPPRENTIETUDIANT);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOCOMM);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOPRIVE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPSUCCNONPART);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_FORTUNEIMPOSABLE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_FORTUNETAUX);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_INDEMNITEIMPOSABLE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_INTERETSPASSIFSCOMM);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_INTERETSPASSIFSPRIVE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERSONNECHARGEENFANT);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEACCESSINDEP);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEINDEP);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVINDEP);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVINDEPEPOUSE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEAGRIC);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVITEAGRIC);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVITEAGRICEPOUSE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTESEXERCICESCOMM);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTESOCIETE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBCOMM);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBPRIVE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUIMPOSABLE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUNETEMPLOI);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUNETEPOUSE);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_REVENUTAUX);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_TOTAUXREVENUSNETS);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTEREPEXCOMM);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_PERTELIQUIDATION);
            propertyList.add(IAMCodeSysteme.CS_RUBRIQUE_DEDUCTIONCOUPLESMARIES);
            rubriqueSearch.setInCodeTypeParametre(propertyList);

            try {
                rubriqueSearch = AmalServiceLocator.getParametreAnnuelService().search(rubriqueSearch);

                for (int i = 0; i < rubriqueSearch.getSearchResults().length; i++) {
                    String codeRubrique = ((SimpleParametreAnnuel) rubriqueSearch.getSearchResults()[i])
                            .getCodeTypeParametre();
                    String valRubrique = ((SimpleParametreAnnuel) rubriqueSearch.getSearchResults()[i])
                            .getValeurParametreString();
                    String annee = ((SimpleParametreAnnuel) rubriqueSearch.getSearchResults()[i]).getAnneeParametre();

                    if (Integer.parseInt(annee) > anneeOld) {
                        rubriqueMap.put(codeRubrique, valRubrique);
                    } else {
                        rubriqueMapOld.put(codeRubrique, valRubrique);
                    }
                }
            } catch (Exception e) {
                JadeLogger.error("getCodeRubrique",
                        "Error while loading CodeRubrique for " + propertyName + " / year : " + anneeHistorique);
            }
        }
        try {
            if (Integer.parseInt(anneeHistorique) > anneeOld) {
                return rubriqueMap.get(propertyName);
            } else {
                return rubriqueMapOld.get(propertyName);
            }
        } catch (Exception e) {
            JadeLogger.error("getCodeRubrique", "Error CodeRubrique for " + propertyName + " / year : "
                    + anneeHistorique + " doesn't exist in map!");
            return " ";
        }
    }

    /**
     * Gets the BSession
     *
     * @return
     */
    private BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * Gets the sexe as H-F
     *
     * @param idSexe
     * @return
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

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if ((revenuHistoriqueComplex != null)) {
            return new BSpy(revenuHistoriqueComplex.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().readHistoriqueComplex(getId());

        retrieveTaxationsList();
        retrieveHistoriqueList();
    }

    /**
     * Gets the Contribuable informations
     *
     * @throws Exception
     */
    public void retrieveContribuable() throws Exception {

        // Retrieve Everybody from the family and find the contribuable
        ContribuableSearch search = new ContribuableSearch();
        search.setForIdContribuable(contribuable.getContribuable().getId());
        search = AmalServiceLocator.getContribuableService().search(search);

        // Set first contribuable (to get after the id and the idtiers of the main contribuable)
        ContribuableOnly resultContribuableSeul = null;
        if (search.getSearchResults().length > 0) {
            resultContribuableSeul = (ContribuableOnly) search.getSearchResults()[0];
        }

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            Contribuable currentContribuable = (Contribuable) it.next();
            // Set contribuable
            if ((null != resultContribuableSeul) && resultContribuableSeul.getContribuable().getIdTier()
                    .equals(currentContribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers())) {
                contribuable = currentContribuable;
                // Setting the adresse
                try {
                    contribuable
                            .setAdresseComplexModel(AmalServiceLocator.getContribuableService().getContribuableAdresse(
                                    currentContribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers()));
                } catch (Exception e) {
                    JadeLogger.error(this, "Error Loading adresse : " + contribuable.getId() + " _ " + e.getMessage());

                }
                // Setting the histo numero
                try {
                    contribuable.setHistoNumeroContribuable(
                            AmalServiceLocator.getContribuableService().getContribuableHistoriqueNoContribuable(
                                    currentContribuable.getPersonneEtendue().getPersonneEtendue().getIdTiers()));
                } catch (Exception e) {
                    JadeLogger.error(this, "Error Loading histo numéro contribuable : " + contribuable.getId() + " _ "
                            + e.getMessage());
                }
            }
        }
    }

    /**
     * Gets the list of historique, related to the current annee historique
     *
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public void retrieveHistoriqueList()
            throws RevenuException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        revenuHistoriqueSearch = new RevenuHistoriqueSearch();
        if ((getRevenuHistoriqueComplex() != null)
                && (getRevenuHistoriqueComplex().getSimpleRevenuHistorique() != null)) {

            String anneeHistorique = getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getAnneeHistorique();
            String idContribuable = getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getIdContribuable();

            if (JadeStringUtil.isBlankOrZero(anneeHistorique) || JadeStringUtil.isBlankOrZero(idContribuable)) {
            } else {
                revenuHistoriqueSearch.setForAnneeHistorique(anneeHistorique);
                revenuHistoriqueSearch.setForIdContribuable(idContribuable);
                revenuHistoriqueSearch.setForRevenuActif(false);
                revenuHistoriqueSearch = (RevenuHistoriqueSearch) AmalServiceLocator.getRevenuService()
                        .search(revenuHistoriqueSearch);
            }

        }
    }

    /**
     * Gets the list of taxation, for the year historique - 2
     *
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     * @throws DetailFamilleException
     */
    public void retrieveTaxationsList() throws DetailFamilleException, RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if ((getRevenuHistoriqueComplex() != null)
                && (getRevenuHistoriqueComplex().getSimpleRevenuHistorique() != null)) {

            // récupération de la liste des taxations jusqu'à année - 2
            String idContribuable = getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getIdContribuable();
            String anneeHistorique = getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getAnneeHistorique();
            String idRevenu = getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getIdRevenu();
            if (JadeStringUtil.isBlankOrZero(idContribuable) || JadeStringUtil.isBlankOrZero(anneeHistorique)) {
                setListTaxations(new ArrayList<Revenu>());
            } else {
                setListTaxations(
                        AmalServiceLocator.getDetailFamilleService().getListTaxations(idContribuable, anneeHistorique));
            }
            // contrôle que dans cette liste, nous trouvions la taxation liée si nous en avons une
            if (!JadeStringUtil.isEmpty(idRevenu)) {
                boolean bFound = false;
                for (int iTaxation = 0; iTaxation < getListTaxations().size(); iTaxation++) {
                    Revenu currentTaxation = getListTaxations().get(iTaxation);
                    if (currentTaxation.getIdRevenu().equals(idRevenu)) {
                        bFound = true;
                        break;
                    }
                }
                if (!bFound) {
                    RevenuSearch revenuSearch = new RevenuSearch();
                    revenuSearch.setForIdContribuable(idContribuable);
                    revenuSearch.setForIdRevenu(idRevenu);
                    revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);
                    for (int iTaxation = 0; iTaxation < revenuSearch.getSize(); iTaxation++) {
                        Revenu revenu = (Revenu) revenuSearch.getSearchResults()[iTaxation];
                        getListTaxations().add(revenu);
                    }
                }
            }
        } else {
            setListTaxations(new ArrayList<Revenu>());
        }
    }

    /**
     * Set the contribuable
     *
     * @param contribuable
     */
    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        revenuHistoriqueComplex.setId(newId);
    }

    /**
     * Set the id contribuable
     *
     * @param contribuableId
     */
    public void setIdContribuable(String contribuableId) {
        contribuable.getContribuable().setId(contribuableId);
    }

    /**
     * @param listTaxations
     *            the listTaxations to set
     */
    public void setListTaxations(ArrayList<Revenu> listTaxations) {
        this.listTaxations = listTaxations;
    }

    /**
     * Set the revenuhistoriquecomplex
     *
     * @param revenuHistoriqueComplex
     */
    public void setRevenuHistoriqueComplex(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        this.revenuHistoriqueComplex = revenuHistoriqueComplex;
    }

    /**
     * @param revenuHistoriqueSearch
     *            the revenuHistoriqueSearch to set
     */
    public void setRevenuHistoriqueSearch(RevenuHistoriqueSearch revenuHistoriqueSearch) {
        this.revenuHistoriqueSearch = revenuHistoriqueSearch;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        revenuHistoriqueComplex = AmalServiceLocator.getRevenuService().update(revenuHistoriqueComplex);
    }

}
