package globaz.pegasus.vb.decision;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.models.decision.ListDecisionsSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Classe de gestion des listBeans MonnaieEtrangere 6.2010
 * 
 * @author SCE
 * 
 */
public class PCDecisionListViewBean extends BJadePersistentObjectListViewBean {

    // instance du modele de recherche
    private ListDecisionsSearch listDecisionsSearch = null;

    /**
     * Constructeur
     */
    public PCDecisionListViewBean() {
        super();
        listDecisionsSearch = new ListDecisionsSearch();
    }

    /**
     * Méthode de recherche avec paramètre de recherche
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if(testHasOneParameter(listDecisionsSearch)) {
            // si for demande pas vide --> recherche avec demande
            if (!JadeStringUtil.isEmpty(listDecisionsSearch.getForDemande())) {
                listDecisionsSearch.setWhereKey("forDemandeSearch");
            }
            listDecisionsSearch = PegasusServiceLocator.getDecisionService().searchDecisions(listDecisionsSearch);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < listDecisionsSearch.getSize() ? new PCDecisionViewBean(
                (ListDecisions) listDecisionsSearch.getSearchResults()[idx]) : new PCDecisionViewBean();
    }

    public String getCurrentUserFullName() {
        return getSession().getUserFullName();
    }

    /**
     * Retourne l'instance du modele de recherche
     * 
     * @return the monnaieEtrangereSearch
     */
    public ListDecisionsSearch getListDecisionsSearch() {
        return listDecisionsSearch;
    }

    /**
     * Retourne une chaine de caractere séparé par des virgules contenant la liste des décision liés pour le meme lot
     * 
     * @return
     */
    public String getLotDecisions() {

        StringBuilder returnString = new StringBuilder("");
        String separator = ",";

        for (JadeAbstractModel decision : listDecisionsSearch.getSearchResults()) {
            returnString.append(
                    ((ListDecisions) decision).getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader())
                    .append(separator);
            // returnString += ((ListDecisions) decision).getDecisionHeader().getSimpleDecisionHeader()
            // .getIdDecisionHeader();
            // returnString += separator;
        }
        // Suppression derniere virgule
        // returnString = returnString.substring(0, returnString.length() - 1);
        return returnString.substring(0, returnString.length() - 1);
    }

    /**
     * retourne le jadeAbstractModel
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return listDecisionsSearch;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * @param variableMetierSearch
     *            the variableMetierSearch to set
     */
    public void setDecisionSearch(ListDecisionsSearch listDecisionsSearch) {
        this.listDecisionsSearch = listDecisionsSearch;
    }
    
    private boolean testHasOneParameter(ListDecisionsSearch listDecisionsSearch) {
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getLikeNss())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getLikeNom())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getLikePrenom())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDateNaissance())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForCsEtat())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForCsSexe())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForCsTypeDecision())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDansDernierLot())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDateDebutDroit())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDateDecision())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDateValidationGreaterOrEqual())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDateValidationLessOrEqual())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDemande())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDepuisDebutDroit())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDepuisValidation())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDossier())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForDroit())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForIdDecision())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForIdTiers())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForNoDecision())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForPcAccorde())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForPreparePar())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForValidePar())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForVersionDroitApc())) {
            return true;
        }
        if(!JadeStringUtil.isEmpty(listDecisionsSearch.getForVersionDroitSup())) {
            return true;
        }
        
        return false;
    }
}