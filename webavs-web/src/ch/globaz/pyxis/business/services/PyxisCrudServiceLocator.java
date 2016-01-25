package ch.globaz.pyxis.business.services;

import ch.globaz.common.business.services.CrudService;
import ch.globaz.pyxis.businessimpl.services.PersonneAvsCrudServiceJadeImpl;
import ch.globaz.pyxis.businessimpl.services.PersonneCrudServiceJadeImpl;
import ch.globaz.pyxis.businessimpl.services.TiersCrudServiceJadeImpl;

/**
 * Catalogue des services CRUD pour le module Pyxis
 * 
 * @see CrudService
 */
public class PyxisCrudServiceLocator {

    /**
     * @return le service de persistance pour une personne ayant un numéro AVS
     */
    public static PersonneAvsCrudService getPersonneAvsCrudService() {
        return new PersonneAvsCrudServiceJadeImpl();
    }

    /**
     * @return le service de persistance pour une personne physique
     */
    public static PersonneCrudService getPersonneCrudService() {
        return new PersonneCrudServiceJadeImpl();
    }

    /**
     * @return le service de persistance pour un tiers
     */
    public static TiersCrudService getTiersCrudService() {
        return new TiersCrudServiceJadeImpl();
    }
}
