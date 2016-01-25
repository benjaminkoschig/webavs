package ch.globaz.orion.business.services.inscription;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.orion.business.exceptions.OrionInscriptionException;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;

public interface InscriptionService extends JadeApplicationService {
    public InscriptionEbusiness createCompteAffilie(String idInscription, String statut, String remarque)
            throws OrionInscriptionException;

    public InscriptionEbusiness[] listInscriptionNouvelle() throws OrionInscriptionException;
}
