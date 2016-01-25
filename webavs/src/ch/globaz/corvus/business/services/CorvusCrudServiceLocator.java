package ch.globaz.corvus.business.services;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.services.models.decisions.SoldePourRestitutionCrudService;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.CompensationInterDecisionCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.OrdresVersementCrudService;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeCrudService;
import ch.globaz.corvus.businessimpl.services.models.decisions.SoldePourRestitutionCrudServiceJadeImpl;
import ch.globaz.corvus.businessimpl.services.models.demande.DemandeRenteCrudServiceJadeImpl;
import ch.globaz.corvus.businessimpl.services.models.ordresversement.CompensationInterDecisionCrudServiceJadeImpl;
import ch.globaz.corvus.businessimpl.services.models.ordresversement.OrdresVersementCrudServiceJadeImpl;
import ch.globaz.corvus.businessimpl.services.models.rentesaccordees.RenteAccordeeCrudServiceJadeImpl;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.pyxis.business.services.PyxisCrudServiceLocator;

/**
 * Catalogue des services permettant de persister les objets de domaines
 */
public final class CorvusCrudServiceLocator {

    public static final CompensationInterDecisionCrudService getCompensationInterDecisionCrudService() {
        return new CompensationInterDecisionCrudServiceJadeImpl(
                CorvusCrudServiceLocator.getOrdresVersementCrudService(),
                PyxisCrudServiceLocator.getPersonneAvsCrudService());
    }

    public static final DemandeRenteCrudService getDemandeRenteCrudService() {
        try {
            return new DemandeRenteCrudServiceJadeImpl(JadeBusinessServiceLocator.getCodeSystemeService());
        } catch (JadeApplicationServiceNotAvailableException ex) {
            throw new RETechnicalException(ex);
        }
    }

    public static final OrdresVersementCrudService getOrdresVersementCrudService() {
        return new OrdresVersementCrudServiceJadeImpl();
    }

    public static final RenteAccordeeCrudService getRenteAccordeeCrudService() {
        return new RenteAccordeeCrudServiceJadeImpl();
    }

    public static final SoldePourRestitutionCrudService getSoldePourRestitutionCrudService() {
        return new SoldePourRestitutionCrudServiceJadeImpl(CorvusCrudServiceLocator.getOrdresVersementCrudService());
    }
}
