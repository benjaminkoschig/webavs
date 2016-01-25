package ch.globaz.orion.business.services.dan;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.orion.business.constantes.PreRemplissageStatus;
import ch.globaz.orion.business.exceptions.OrionDanException;
import ch.globaz.orion.business.models.dan.Dan;

/**
 * @author sco
 * @since 12 avr. 2011
 */
public interface DanService extends JadeApplicationService {

    public PreRemplissageStatus preRemplirDan(Dan dan, Boolean override) throws OrionDanException;

    public String retrieveIdInstitutionForNumAffilie(String numAffilie, String annee, String typeInstitution)
            throws OrionDanException;
}
