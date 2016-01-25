package ch.globaz.al.business.services.dossiers;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import java.util.List;
import ch.globaz.al.businessimpl.services.dossiers.RadiationAutomatiqueResult;

/**
 * Service permettant la génération d'un protocole lors de la radiation automatique de dossier (
 * {@link RadiationAutomatiqueService})
 * 
 * @author jts
 * 
 */
public interface RadiationAutomatiqueDossiersProtocoleService extends JadeApplicationService {

    /**
     * Crée le protocole et retourne le nom du fichier
     * 
     * @return le nom du fichier
     * 
     * @throws JadeApplicationException
     */
    public String createProtocole(List<RadiationAutomatiqueResult> logDossiers, HashMap<String, String> errors)
            throws JadeApplicationException;
}
