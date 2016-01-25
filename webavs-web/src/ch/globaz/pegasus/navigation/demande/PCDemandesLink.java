package ch.globaz.pegasus.navigation.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.business.models.demande.ListDemandesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;

public class PCDemandesLink extends PegasusNavigatorLink {

    private transient ListDemandesSearch listDemandesSearch;

    public PCDemandesLink(String idDemande) {
        ids.put(IPCLinkParameters.ID_DEMANDE, idDemande);
    }

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        if (!JadeStringUtil.isBlankOrZero(ids.get(IPCLinkParameters.ID_DEMANDE))) {
            parameters.put(IPCLinkParameters.ID_DOSSIER, getIdDossier());
            listDemandesSearch = new ListDemandesSearch();
            listDemandesSearch.setForIdDossier(parameters.get(IPCLinkParameters.ID_DOSSIER));
            listDemandesSearch = PegasusServiceLocator.getDemandeService().searchDemandes(listDemandesSearch);
            listDemandesSearch.setDefinedSearchSize(10);
        }
    }

    @Override
    public String getLibelle() {
        return "pegasus.link.demandes";
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.demande.demande.chercher";
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        List<NavigatorInterface> list = new ArrayList<NavigatorInterface>();
        for (JadeAbstractModel model : listDemandesSearch.getSearchResults()) {
            ListDemandes demande = (ListDemandes) model;
            list.add(new PCDemandeLink(demande));
        }
        Collections.reverse(list);
        return list;
    }
}
