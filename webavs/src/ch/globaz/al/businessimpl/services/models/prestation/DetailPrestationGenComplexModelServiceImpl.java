package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.exceptions.model.prestation.ALEntetePrestationModelException;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;
import ch.globaz.al.business.services.models.prestation.DetailPrestationGenComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services <code>DetailPrestationModel</code>
 * 
 * @author jts
 * @see ch.globaz.al.business.services.models.prestation.DetailPrestationGenComplexModelService
 */
public class DetailPrestationGenComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DetailPrestationGenComplexModelService {
    private static final String WHERE_PRESTATION_EXISTANTE = "PrestationExistante";

    @Override
    public int count(DetailPrestationGenComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public boolean hasExistingPrestations(String idDossier, String idDroit, String periodeDe, String periodeA)
            throws JadeApplicationException, JadePersistenceException {
        // Création du searchModel
        DetailPrestationGenComplexSearchModel searchModel = new DetailPrestationGenComplexSearchModel();
        searchModel.setForIdDossier(idDossier);
        searchModel.setForPeriodeDebut(periodeDe);
        searchModel.setForPeriodeFin(periodeA);
        searchModel.setForIdDroit(idDroit);
        // Rechercher les prestations dont l'état est différent de TMP
        searchModel.setForEtatPrestation(ALCSPrestation.ETAT_TMP);
        // Recherche les montants supérieur à 0
        searchModel.setForMontant("0");
        searchModel.setWhereKey(DetailPrestationGenComplexModelServiceImpl.WHERE_PRESTATION_EXISTANTE);
        searchModel.setOrderKey(DetailPrestationGenComplexModelServiceImpl.WHERE_PRESTATION_EXISTANTE);

        return count(searchModel) > 0;
    }

    @Override
    public DetailPrestationGenComplexSearchModel search(DetailPrestationGenComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (searchModel == null) {
            throw new ALEntetePrestationModelException(
                    "DetailPrestationComplexModelServiceImpl#search : searchModel is null");
        }

        return (DetailPrestationGenComplexSearchModel) JadePersistenceManager.search(searchModel);
    }
}
