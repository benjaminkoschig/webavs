package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de génération du protocole RAFam
 * 
 * @author jts
 * 
 */
public interface AnnoncesRafamProtocoleService extends JadeApplicationService {

    /**
     * Crée le protocole et retourne le nom du fichier
     * 
     * @return le nom du fichier
     * @throws JadeApplicationException
     */
    public String createProtocole() throws JadeApplicationException;
}
