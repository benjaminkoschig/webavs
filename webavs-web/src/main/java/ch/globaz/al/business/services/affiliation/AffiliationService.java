package ch.globaz.al.business.services.affiliation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service permettant d'�changer avec les affiliations AF dans le contexte d'un dossier AF
 * 
 * @author GMO/JTS
 * 
 */
public interface AffiliationService extends JadeApplicationService {

    /**
     * Retourne le code syst�me d'un canton AF correspondant au code syst�me d'un canton affiliations
     * 
     * @param cantonNaos
     *            code syst�me d'un canton dans naos
     * @return code syst�me AF correspondant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String convertCantonNaos2CantonAF(String cantonNaos) throws JadeApplicationException;
}
