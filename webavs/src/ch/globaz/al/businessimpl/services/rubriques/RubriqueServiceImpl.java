package ch.globaz.al.businessimpl.services.rubriques;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.rubriques.RubriqueService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubriques.comptables.RubriqueComptableServiceFactory;

/**
 * Implémentation du service permettant la récupération de rubriques (comptable ou autre
 * 
 * @author jts
 * 
 */
public class RubriqueServiceImpl extends ALAbstractBusinessServiceImpl implements RubriqueService {

    @Override
    public String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException {

        return (ALImplServiceLocator.getRubriqueComptableService(RubriqueComptableServiceFactory
                .getServiceRubriqueComptable(date))).getRubriqueComptable(dossier, entete, detail, date);
    }
}