package ch.globaz.pegasus.navigation.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;

public class PCDroitsLink extends PegasusNavigatorLink {
    private DroitSearch droitSearch = null;

    // private Droit droit = null;

    // public PCDroitsLink(Map<String, String> ids) {
    // this.ids = ids;
    // }

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        boolean ok = false;
        if (!JadeStringUtil.isBlankOrZero(ids.get(IPCLinkParameters.ID_DEMANDE))) {
            parameters.put(IPCLinkParameters.ID_DEMANDE, ids.get(IPCLinkParameters.ID_DEMANDE));
            ok = true;
        }
        if (!JadeStringUtil.isBlankOrZero(ids.get(IPCLinkParameters.ID_DOSSIER))) {
            parameters.put(IPCLinkParameters.ID_DOSSIER, ids.get(IPCLinkParameters.ID_DOSSIER));
            // ok = true;
        }
        if (ok) {
            droitSearch = new DroitSearch();
            droitSearch.setForIdDemandePc(ids.get(IPCLinkParameters.ID_DEMANDE));
            droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        }
    }

    @Override
    public String getLibelle() {
        return "pegasus.link.droits";
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.droit.droit.chercher";
    }

    // http://localhost:8080/webavs/pegasus?userAction=pegasus.droit.droit.afficher&selectedId=20110&idDemandePc=221&idDroit=221&noVersion=2&idVersionDroit=20110

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {

        List<NavigatorInterface> list = new ArrayList<NavigatorInterface>();
        for (JadeAbstractModel model : droitSearch.getSearchResults()) {
            Droit droit = (Droit) model;
            list.add(new PCDroitLink(droit));
        }
        // Collections.reverse(list);
        return list;
    }

}
