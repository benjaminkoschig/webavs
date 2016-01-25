package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierDeclarationVersementComplexSearchModel;

/**
 * Service de gestion de la persistance des services liés DossierDeclarationVersementModelService
 * 
 * @author PTA
 * 
 */
public interface DossierDeclarationVersementComplexModelService extends JadeApplicationService {

    /**
     * 
     * @param searchModel
     *            modèle de rcherche
     * @return DossierDeclarationVersmentComplexSearchModel
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DossierDeclarationVersementComplexSearchModel search(
            DossierDeclarationVersementComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
