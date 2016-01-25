package ch.globaz.al.business.services.affiliation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import java.util.Map;
import ch.globaz.al.business.constantes.enumerations.affiliation.ALEnumProtocoleRadiationAffilie;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Service permettant la g�n�ration d'un protocole lors de la radiation d'un affili� ( {@link RadiationAffilieService})
 * 
 * 
 * @author jts
 * 
 */
public interface RadiationAffilieProtocoleService extends JadeApplicationService {

    /**
     * Cr�e le protocole et retourne le nom du fichier
     * 
     * @return le nom du fichier
     * 
     * @throws JadeApplicationException
     */
    public String createProtocole(
            List<Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>> log,
            Map<String, DetailPrestationComplexSearchModel> prestations, Map<String, String> errors)
            throws JadeApplicationException;
}
