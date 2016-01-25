package ch.globaz.libra.businessimpl.services;

import globaz.libra.interfaces.LIDossiersInterfaces;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.services.DossierService;

public class DossierServiceImpl implements DossierService {
    LIDossiersInterfaces dossier = new LIDossiersInterfaces();

    @Override
    public void clotureDossier(String idExterne) throws LibraException {
        try {
            dossier.clotureDossier(LibraUtil.getCurrentTransaction(), idExterne);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public void createDossier(String idTiers, String csDomaine, String idExterne) throws LibraException {
        try {
            dossier.createDossier(LibraUtil.getCurrentTransaction(), idTiers, csDomaine, idExterne);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

    @Override
    public void reactivationDossier(String idExterne) throws LibraException {
        try {
            dossier.reactivationDossier(LibraUtil.getCurrentTransaction(), idExterne);
        } catch (Exception e) {
            throw new LibraException("Unable to instanciate LIService!", e);
        }
    }

}
