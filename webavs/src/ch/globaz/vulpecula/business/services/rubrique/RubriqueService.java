/**
 * 
 */
package ch.globaz.vulpecula.business.services.rubrique;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.vulpecula.business.models.comptabilite.RubriqueSearchSimpleModel;

/**
 * Services gérant les administrations tel que convention, assureur maladie,
 * section, syndicat et association professionnelle.
 * 
 * Surcharge les services de Pyxis afin d'avoir un service pour les
 * administrations étendant JadeCrudService.
 * 
 * @since Web@BMS 0.01.01
 */
public interface RubriqueService extends JadeApplicationService {
    RubriqueSearchSimpleModel find(RubriqueSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException;
}
