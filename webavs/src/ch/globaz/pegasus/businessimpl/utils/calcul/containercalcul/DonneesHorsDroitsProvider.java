package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetierSearch;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * Container global pour le calculateur contenant les variables pouvant être chargée une fois pour le calcul et le
 * traitement de masse.
 * 
 * @author sce
 * 
 */
public class DonneesHorsDroitsProvider {

    public static DonneesHorsDroitsProvider getInstance() throws CalculException {
        return new DonneesHorsDroitsProvider();
    }

    private List<MonnaieEtrangere> listeMonnaiesEtrangeres = null;

    private List<VariableMetier> listeVariablesMetiers = null;

    private VariableMetier tauxOFAS = null;

    private DonneesHorsDroitsProvider() {
        listeMonnaiesEtrangeres = new ArrayList<MonnaieEtrangere>();
        listeVariablesMetiers = new ArrayList<VariableMetier>();
        tauxOFAS = new VariableMetier();

        // this.init();

    }

    private void fillMonnaiesEtrangeresProvider() throws MonnaieEtrangereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        MonnaieEtrangereSearch monnaieEtrangereSearch = new MonnaieEtrangereSearch();
        monnaieEtrangereSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        monnaieEtrangereSearch = PegasusServiceLocator.getMonnaieEtrangereService().search(monnaieEtrangereSearch);
        // Ajout dans le container global
        listeMonnaiesEtrangeres = new MonnaieEtrangereBuilder().loadFor(monnaieEtrangereSearch);// (new

    }

    private void fillVariablesMetierProvider() throws CalculException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        // récupère données variables metier
        CalculVariableMetierSearch searchVarMet = new CalculVariableMetierSearch();
        searchVarMet.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchVarMet = PegasusImplServiceLocator.getCalculVariableMetierService().search(searchVarMet);
        // Ajout dans le container global
        VariableMetierBuilder varMetProv = new VariableMetierBuilder();

        listeVariablesMetiers = varMetProv.loadFor(searchVarMet);
        tauxOFAS = varMetProv.getTauxOFAS();
    }

    public List<MonnaieEtrangere> getListeMonnaiesEtrangeres() {
        return listeMonnaiesEtrangeres;
    }

    public List<VariableMetier> getListeVariablesMetiers() {
        return listeVariablesMetiers;
    }

    public VariableMetier getTauxOFAS() {
        return tauxOFAS;
    }

    public void init() throws CalculException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            MonnaieEtrangereException {
        fillMonnaiesEtrangeresProvider();
        fillVariablesMetierProvider();
    }

    public void setTauxOFAS(VariableMetier tauxOFAS) {
        this.tauxOFAS = tauxOFAS;
    }

}
