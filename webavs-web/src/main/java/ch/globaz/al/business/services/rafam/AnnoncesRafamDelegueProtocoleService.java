package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface AnnoncesRafamDelegueProtocoleService extends JadeApplicationService {
    /**
     * Cr?e le protocole et retourne le nom du fichier
     * 
     * @return le nom du fichier
     * @throws JadeApplicationException
     */
    public String createProtocole(String idEmployeur) throws JadeApplicationException;
}
