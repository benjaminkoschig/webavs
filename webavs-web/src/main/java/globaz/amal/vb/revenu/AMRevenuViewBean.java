/**
 *
 */
package globaz.amal.vb.revenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableOnly;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;

/**
 * @author DHI
 *
 *         Viewbean for revenu management (taxation)
 *
 */
public class AMRevenuViewBean extends BJadePersistentObjectViewBean {
    private int anneeOld = 2005;
    private Contribuable contribuable = null;
    String[] navigIds = new String[2];
    private ArrayList<String> navigList = new ArrayList<String>();
    private RevenuFullComplex revenuFullComplex = null;
    private HashMap<String, String> rubriqueMap = null;
    private HashMap<String, String> rubriqueMapOld = null;

    /**
     * Default Constructor
     *
     */
    public AMRevenuViewBean() {
        super();
        revenuFullComplex = new RevenuFullComplex();
        contribuable = new Contribuable();
        if (getRevenuFullComplex().getSimpleRevenu().getIdContribuable() != null) {
            setIdContribuable(getRevenuFullComplex().getSimpleRevenu().getIdContribuable());
        }
    }

    /**
     * Consructor with parameters
     *
     * @param revenuFullComplex
     */
    public AMRevenuViewBean(RevenuFullComplex revenuFullComplex) {
        super();
        this.revenuFullComplex = revenuFullComplex;
        contribuable = new Contribuable();
        if (getRevenuFullComplex().getSimpleRevenu().getIdContribuable() != null) {
            setIdContribuable(getRevenuFullComplex().getSimpleRevenu().getIdContribuable());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // Set the type to manuel
        revenuFullComplex.getSimpleRevenu().setTypeSource("42002901");
        revenuFullComplex = AmalServiceLocator.getRevenuService().create(revenuFullComplex);
        navigList = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        revenuFullComplex = AmalServiceLocator.getRevenuService().delete(revenuFullComplex);
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
        return revenuFullComplex.getId();
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
     * Generate Id for navigation previous and next year
     *
     */
    public void getNavigation() {
        SimpleRevenuSearch simpleRevenuSearch = new SimpleRevenuSearch();
        simpleRevenuSearch.setForIdContribuable(contribuable.getId());
        simpleRevenuSearch.setOrderKey("revenuNavigation");
        try {
            simpleRevenuSearch = AmalImplServiceLocator.getSimpleRevenuService().search(simpleRevenuSearch);

            for (int i = 0; i < simpleRevenuSearch.getSearchResults().length; i++) {
                SimpleRevenu simpleRevenu = (SimpleRevenu) simpleRevenuSearch.getSearchResults()[i];
                navigList.add(simpleRevenu.getIdRevenu());
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
        simpleParametreAnnuelSearch.setForAnneeParametre(_annee);
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
    public RevenuFullComplex getRevenuFullComplex() {
        return revenuFullComplex;
    }

    /**
     * Récupération du revenu historique actif lié
     *
     * @return
     */
    public RevenuHistoriqueComplexSearch getRevenuHistoriqueActif() {
        if (revenuFullComplex != null) {
            try {
                RevenuHistoriqueComplexSearch currentSearch = new RevenuHistoriqueComplexSearch();
                currentSearch.setForIdRevenu(revenuFullComplex.getId());
                currentSearch.setForRevenuActif(true);
                return AmalServiceLocator.getRevenuService().search(currentSearch);
            } catch (Exception ex) {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Récupération du revenu historique actif lié
     *
     * @return
     */
    public RevenuHistoriqueComplexSearch getRevenuHistoriqueNonActif() {
        if (revenuFullComplex != null) {
            try {
                RevenuHistoriqueComplexSearch currentSearch = new RevenuHistoriqueComplexSearch();
                currentSearch.setForIdRevenu(revenuFullComplex.getId());
                currentSearch.setForRevenuActif(false);
                return AmalServiceLocator.getRevenuService().search(currentSearch);
            } catch (Exception ex) {
                return null;
            }

        } else {
            return null;
        }
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
        if ((revenuFullComplex != null)) {
            return new BSpy(revenuFullComplex.getSpy());
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
        revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex(getId());
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
        revenuFullComplex.setId(newId);
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
     * Set the revenuhistoriquecomplex
     *
     * @param revenuHistoriqueComplex
     */
    public void setRevenuFullComplex(RevenuFullComplex revenuFullComplex) {
        this.revenuFullComplex = revenuFullComplex;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        revenuFullComplex = AmalServiceLocator.getRevenuService().update(revenuFullComplex);
    }

}
