/**
 * 
 */
package ch.globaz.vulpecula.business.services.administration;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.vulpecula.external.models.AdministrationSearchComplexModel;

/**
 * Services gérant les administrations tel que convention, assureur maladie,
 * section, syndicat et association professionnelle.
 * 
 * Surcharge les services de Pyxis afin d'avoir un service pour les
 * administrations étendant JadeCrudService.
 * 
 * @since Web@BMS 0.01.01
 */
public interface AdministrationService extends JadeApplicationService {
    AdministrationSearchComplexModel find(AdministrationSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException;
}
