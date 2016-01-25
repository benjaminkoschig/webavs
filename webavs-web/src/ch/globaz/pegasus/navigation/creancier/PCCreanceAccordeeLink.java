package ch.globaz.pegasus.navigation.creancier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;
import ch.globaz.pegasus.navigation.demande.PCDemandesLink;

public class PCCreanceAccordeeLink extends PegasusNavigatorLink {

    public PCCreanceAccordeeLink(String idDemande) {
        ids.put(IPCLinkParameters.ID_DEMANDE, idDemande);

    }

    public PCCreanceAccordeeLink(String idDemande, String idDecision) {
        ids.put(IPCLinkParameters.ID_DEMANDE, idDemande);
        ids.put(IPCLinkParameters.ID_DECISION, idDecision);
    }

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {

        if (ids.containsKey(IPCLinkParameters.ID_DECISION)) {
            parameters.put(IPCLinkParameters.ID_DECISION, ids.get(IPCLinkParameters.ID_DECISION));
        }

        parameters.put(IPCLinkParameters.ID_DEMANDE, ids.get(IPCLinkParameters.ID_DEMANDE));
        parameters.put(IPCLinkParameters.ID_DEMANDE_PC, ids.get(IPCLinkParameters.ID_DEMANDE));
    }

    @Override
    public String getLibelle() {
        return "pegasus?userAction=pegasus.link.creanceAccordee";
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        List<NavigatorInterface> list = new ArrayList<NavigatorInterface>();

        PCCreancierLink link = null;

        if (ids.containsKey(IPCLinkParameters.ID_DECISION)) {
            link = new PCCreancierLink(IPCLinkParameters.ID_DEMANDE, IPCLinkParameters.ID_DECISION, true);
        } else {
            link = new PCCreancierLink(ids.get(IPCLinkParameters.ID_DEMANDE), true);
        }
        list.add(link);
        list.add(new PCDemandesLink(ids.get(IPCLinkParameters.ID_DEMANDE)));
        return list;
    }

}
