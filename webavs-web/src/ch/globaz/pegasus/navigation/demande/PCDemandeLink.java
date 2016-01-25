package ch.globaz.pegasus.navigation.demande;

import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.navigation.IPCLinkParameters;
import ch.globaz.pegasus.navigation.NavigatorInterface;
import ch.globaz.pegasus.navigation.PegasusNavigatorLink;

public class PCDemandeLink extends PegasusNavigatorLink {

    private ListDemandes demandes;

    public PCDemandeLink(ListDemandes demande) {
        demandes = demande;
    }

    @Override
    public void computParmetresLink() throws JadeApplicationException, JadePersistenceException {
        parameters.put(IPCLinkParameters.ID_DOSSIER, demandes.getDossier().getId());
        parameters.put(IPCLinkParameters.SELECTED_ID, demandes.getSimpleDemande().getIdDemande());
    }

    public String getKey() {
        return null;
    }

    @Override
    public String getLibelle() {
        return demandes.getSimpleDemande().getDateDebut()
                + " - "
                + demandes.getSimpleDemande().getDateFin()
                + " : "
                + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                        demandes.getSimpleDemande().getCsEtatDemande());
    }

    @Override
    public String getLink() {
        return "pegasus?userAction=pegasus.demande.demandeDetail.afficher";
    }

    @Override
    public List<NavigatorInterface> getNavigatorLinks() {
        // TODO Auto-generated method stub
        return null;
    }
}
