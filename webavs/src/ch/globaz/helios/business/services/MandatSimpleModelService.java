package ch.globaz.helios.business.services;

import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.helios.business.models.MandatSimpleModel;
import ch.globaz.helios.business.models.MandatSimpleModelSearch;

/**
 * @author sel
 * 
 */
public interface MandatSimpleModelService extends JadeCrudService<MandatSimpleModel, MandatSimpleModelSearch> {
    // Rien à surcharger, tout est dans JadeCrudService
}
