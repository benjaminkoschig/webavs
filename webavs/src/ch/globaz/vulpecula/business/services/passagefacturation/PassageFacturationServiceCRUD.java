package ch.globaz.vulpecula.business.services.passagefacturation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.musca.business.models.PlanFacturationPassageSearchComplexModel;

public interface PassageFacturationServiceCRUD extends JadeApplicationService {
    /**
     * Recherche un passage de facturation de type Taxation d'office
     * 
     * @throws JadePersistenceException
     */
    PlanFacturationPassageSearchComplexModel searchPassageFacturationTO(
            PlanFacturationPassageSearchComplexModel passageSearchModel) throws JadePersistenceException;

    /**
     * Recherche des lots de facturation relatifs aux passages de facturation AJ, CP, SM
     */
    PlanFacturationPassageSearchComplexModel searchPassageFacturationPrestations(
            PlanFacturationPassageSearchComplexModel passageSearchModel) throws JadePersistenceException;
}
