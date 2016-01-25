package ch.globaz.pegasus.navigation.droit;

import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;

public class PCDroitLink extends PegasusNavigatorLink {

    private Droit droit = null;

    public PCDroitLink(Droit droit) {
        this.droit = droit;
    }

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        parameters.put(IPCLinkParameters.ID_DROIT, droit.getSimpleVersionDroit().getIdDroit());
        parameters.put(IPCLinkParameters.NUM_VERSION_DROIT, droit.getSimpleVersionDroit().getNoVersion());
        parameters.put(IPCLinkParameters.ID_VERSION_DROIT, droit.getSimpleVersionDroit().getIdVersionDroit());
        parameters.put(IPCLinkParameters.SELECTED_ID, droit.getSimpleVersionDroit().getIdVersionDroit());

        ids.put(IPCLinkParameters.ID_DROIT, droit.getSimpleVersionDroit().getIdDroit());
        ids.put(IPCLinkParameters.NUM_VERSION_DROIT, droit.getSimpleVersionDroit().getNoVersion());
        ids.put(IPCLinkParameters.ID_VERSION_DROIT, droit.getSimpleVersionDroit().getIdVersionDroit());
        ids.put(IPCLinkParameters.SELECTED_ID, droit.getSimpleVersionDroit().getIdVersionDroit());
    }

    @Override
    public String getLibelle() {
        return droit.getSimpleVersionDroit().getDateAnnonce()
                + ": "
                + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                        droit.getSimpleVersionDroit().getCsEtatDroit()) + " : N° "
                + droit.getSimpleVersionDroit().getNoVersion();
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.droit.droit.afficher";
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        List<NavigatorInterface> list = new ArrayList<NavigatorInterface>();
        list.add(new PCDonneefinanciereLink());
        return list;
    }
}
