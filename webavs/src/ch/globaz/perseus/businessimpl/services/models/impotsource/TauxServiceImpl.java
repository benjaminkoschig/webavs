package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleTaux;
import ch.globaz.perseus.business.models.impotsource.Taux;
import ch.globaz.perseus.business.models.impotsource.TauxSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.impotsource.TauxService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class TauxServiceImpl extends PerseusAbstractServiceImpl implements TauxService {

    @Override
    public int count(TauxSearchModel search) throws TauxException, JadePersistenceException {
        if (search == null) {
            throw new TauxException("Unable to count taux, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Taux create(Taux taux) throws JadePersistenceException, TauxException {
        if (taux == null) {
            throw new TauxException("Unable to create taux, the given model is null!");
        }

        try {
            // Création du taux
            SimpleTaux simpleTaux = taux.getSimpleTaux();
            simpleTaux.setIdTrancheSalaire(taux.getTrancheSalaire().getId());
            simpleTaux.setIdBareme(taux.getSimpleBareme().getIdBareme());
            simpleTaux = PerseusImplServiceLocator.getSimpleTauxService().create(simpleTaux);
            taux.setSimpleTaux(simpleTaux);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TauxException("Service not available - " + e.toString());
        }

        return taux;
    }

    @Override
    public Taux delete(Taux taux) throws JadePersistenceException, TauxException {
        if (taux == null) {
            throw new TauxException("Unable to delete a taux, the model passed is null!");
        }

        try {
            // Suppression du taux
            taux.setSimpleTaux(PerseusImplServiceLocator.getSimpleTauxService().delete(taux.getSimpleTaux()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TauxException("Service not available - " + e.getMessage());
        }

        return taux;
    }

    @Override
    public Taux getTauxImpotSource(BigDecimal salaireBrut, int nbPersonne, String annee, String csTypeBareme)
            throws JadePersistenceException, TauxException, JadeApplicationServiceNotAvailableException {
        Taux taux = null;
        // Récupération du taux
        TauxSearchModel tauxSearch = new TauxSearchModel();
        tauxSearch.setForAnnee(annee);
        tauxSearch.setForCsTypeBareme(csTypeBareme);
        tauxSearch.setForSalaireBrut(salaireBrut.toString());
        tauxSearch.setForNombrePersonne(Integer.toString(nbPersonne));
        tauxSearch.setWhereKey(TauxSearchModel.WITH_TAUX_VALABLE);
        tauxSearch = PerseusServiceLocator.getTauxService().search(tauxSearch);

        if (tauxSearch.getSize() > 0) {
            taux = (Taux) tauxSearch.getSearchResults()[0];
        }

        return taux;
    }

    @Override
    public Taux read(String idTaux) throws JadePersistenceException, TauxException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(idTaux)) {
            throw new TauxException("Unable to read a taux, the id passed is null!");
        }
        // Lecture du taux
        Taux taux = new Taux();
        taux.setId(idTaux);

        return (Taux) JadePersistenceManager.read(taux);

    }

    @Override
    public TauxSearchModel search(TauxSearchModel searchModel) throws JadePersistenceException, TauxException {
        if (searchModel == null) {
            throw new TauxException("Unable to search a taux, the search model passed is null!");
        }

        return (TauxSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public Taux update(Taux taux) throws JadePersistenceException, TauxException {
        if (taux == null) {
            throw new TauxException("Unable to update taux, the given model is null!");
        }

        try {
            // Mise à jour du taux
            SimpleTaux simpleTaux = taux.getSimpleTaux();
            simpleTaux.setIdTrancheSalaire(taux.getTrancheSalaire().getId());
            simpleTaux.setIdBareme(taux.getSimpleBareme().getIdBareme());
            simpleTaux = PerseusImplServiceLocator.getSimpleTauxService().update(simpleTaux);
            taux.setSimpleTaux(simpleTaux);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TauxException("Service not available - " + e.toString());
        }

        return taux;
    }
}
