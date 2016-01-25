package ch.globaz.pegasus.navigation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public abstract class PegasusNavigatorLink extends NavigatorLink {

    public String getIdDossier() throws JadeApplicationException, JadePersistenceException {
        String idDossier = ids.get(IPCLinkParameters.ID_DOSSIER);
        if (idDossier == null) {
            Demande demande = PegasusServiceLocator.getDemandeService().read(ids.get(IPCLinkParameters.ID_DEMANDE));
            idDossier = demande.getSimpleDemande().getIdDossier();
        }
        return idDossier;
    }

}
