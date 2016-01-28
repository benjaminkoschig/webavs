package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.enumerations.RafamImportProtocolFields;

/**
 * Service permettant la génération d'un protocole lors de l'importation du fichier d'annonces RAFam
 * 
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamImportProtocoleService extends JadeApplicationService {

    /**
     * Crée le protocole et retourne le nom du fichier
     * 
     * @return le nom du fichier
     * 
     * @throws JadeApplicationException
     */
    public String createProtocole(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole)
            throws JadeApplicationException;
}
