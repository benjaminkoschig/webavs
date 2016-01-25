package ch.globaz.pegasus.navigation.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;

public class PCDonneefinanciereLink extends PegasusNavigatorLink {

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub
        // this.parameters.put(IPCLinkParameters.SELECTED_ID, this.ids.get(IPCLinkParameters.ID_VERSION_DROIT));
        parameters.put(IPCLinkParameters.NUM_VERSION_DROIT, ids.get(IPCLinkParameters.NUM_VERSION_DROIT));
        parameters.put(IPCLinkParameters.ID_VERSION_DROIT, ids.get(IPCLinkParameters.ID_VERSION_DROIT));
        parameters.put(IPCLinkParameters.ID_DOSSIER, ids.get(IPCLinkParameters.ID_DOSSIER));
        parameters.put("idTitreMenu", "MENU_OPTION_DROITS_RENTES_AJ_API");
        parameters.put("idTitreOnglet", "ENU_ONGLET_DROITS_RENTE_AVS_API");
        // &selectedId=221&idDemandePc=221&idDroit=221&idDossier=131&noVersion=2&idVersionDroit=20110&idTitreMenu=&idTitreOnglet=MENU_ONGLET_DROITS_RENTE_AVS_API

    }

    @Override
    public String getLibelle() {
        return "pegasus.link.donneefinancieres";
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.renteijapi.renteAvsAi.afficher";
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        // TODO Auto-generated method stub
        return null;
    }

}
