package ch.globaz.pegasus.navigation.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;

public class PCDemandeRcLink extends PegasusNavigatorLink {

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isBlankOrZero(ids.get(IPCLinkParameters.ID_DOSSIER))) {
            ids.put(IPCLinkParameters.ID_DOSSIER, getIdDossier());
        }
        parameters.put(IPCLinkParameters.ID_DOSSIER, ids.get(IPCLinkParameters.ID_DOSSIER));
    }

    @Override
    public String getLibelle() {
        return "pegasus.link.demandes.rc";
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.demande.demande.chercher";
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        return null;
    }

}
