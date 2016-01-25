/**
 * 
 */
package ch.globaz.vulpecula.business.services.administration;

import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;

/**
 * Services gérant les administrations tel que convention, assureur maladie,
 * section, syndicat et association professionnelle.
 * 
 * Surcharge les services de Pyxis afin d'avoir un service pour les
 * administrations étendant JadeCrudService.
 * 
 * @since Web@BMS 0.01.01
 */
public interface AdministrationServiceCRUD extends
        JadeCrudService<AdministrationComplexModel, AdministrationSearchComplexModel> {

}
