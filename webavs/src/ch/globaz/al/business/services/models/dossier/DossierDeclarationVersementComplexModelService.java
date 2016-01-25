package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierDeclarationVersementComplexSearchModel;

/**
 * Service de gestion de la persistance des services li�s DossierDeclarationVersementModelService
 * 
 * @author PTA
 * 
 */
public interface DossierDeclarationVersementComplexModelService extends JadeApplicationService {

    /**
     * 
     * @param searchModel
     *            mod�le de rcherche
     * @return DossierDeclarationVersmentComplexSearchModel
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DossierDeclarationVersementComplexSearchModel search(
            DossierDeclarationVersementComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
