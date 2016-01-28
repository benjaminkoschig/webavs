package ch.globaz.pegasus.navigation.creancier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;
import ch.globaz.pegasus.navigation.demande.PCDemandeRcLink;
import ch.globaz.pegasus.navigation.droit.PCDroitsLink;

public class PCCreancierLink extends PegasusNavigatorLink {
    private boolean displayOnlyDetail = false;

    public PCCreancierLink(String idDemande) {
        ids.put(IPCLinkParameters.ID_DEMANDE, idDemande);
    }

    public PCCreancierLink(String idDemande, boolean displayOnlyDetail) {
        ids.put(IPCLinkParameters.ID_DEMANDE, idDemande);

        this.displayOnlyDetail = displayOnlyDetail;
    }

    public PCCreancierLink(String idDemande, String idDecision, boolean displayOnlyDetail) {
        ids.put(IPCLinkParameters.ID_DEMANDE, idDemande);
        ids.put(IPCLinkParameters.ID_DECISION, idDemande);
        this.displayOnlyDetail = displayOnlyDetail;
    }

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        // this.parameters.put(IPCLinkParameters.ID_DEMANDE, this.ids.get(IPCLinkParameters.ID_DEMANDE));
        parameters.put(IPCLinkParameters.ID_DEMANDE_PC, ids.get(IPCLinkParameters.ID_DEMANDE));

        if (ids.containsKey(IPCLinkParameters.ID_DECISION)) {
            parameters.put(IPCLinkParameters.ID_DECISION, ids.get(IPCLinkParameters.ID_DECISION));
        }

    }

    @Override
    public String getLibelle() {
        return "pegasus.link.creancier";
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.creancier.creancier.afficher";
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        List<NavigatorInterface> list = new ArrayList<NavigatorInterface>();

        if (!displayOnlyDetail) {
            list.add(new PCDemandeRcLink());
            // list.add(new PCDemandesLink(this.ids.get(IPCLinkParameters.ID_DEMANDE)));
            // list.add(new PcDemandesLink(this.ids.get(IPCLinkParameters.ID_DEMANDE)));
            list.add(new PCDroitsLink());
        }
        ;
        return list;
    }
}
