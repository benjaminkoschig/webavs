package ch.globaz.pyxis.business.services;

import ch.globaz.common.business.services.CrudService;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Service de persistance pour une personne avec un numéro AVS
 */
public interface PersonneAvsCrudService extends CrudService<PersonneAVS> {

    public PersonneAVS readByNss(String nss);
}
