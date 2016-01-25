package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.enumerations.RafamImportProtocolFields;

/**
 * Service permettant la g�n�ration d'un protocole lors de l'importation du fichier d'annonces RAFam
 * 
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamImportProtocoleService extends JadeApplicationService {

    /**
     * Cr�e le protocole et retourne le nom du fichier
     * 
     * @return le nom du fichier
     * 
     * @throws JadeApplicationException
     */
    public String createProtocole(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole)
            throws JadeApplicationException;
}
