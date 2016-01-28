package ch.globaz.al.business.services.affiliation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service permettant d'échanger avec les affiliations AF dans le contexte d'un dossier AF
 * 
 * @author GMO/JTS
 * 
 */
public interface AffiliationService extends JadeApplicationService {

    /**
     * Retourne le code système d'un canton AF correspondant au code système d'un canton affiliations
     * 
     * @param cantonNaos
     *            code système d'un canton dans naos
     * @return code système AF correspondant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String convertCantonNaos2CantonAF(String cantonNaos) throws JadeApplicationException;
}
