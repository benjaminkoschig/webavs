package ch.globaz.perseus.businessimpl.services.models.parametres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.models.parametres.LienLocaliteSearchModel;
import ch.globaz.perseus.business.models.parametres.SimpleLienLocalite;
import ch.globaz.perseus.business.services.models.parametres.LienLocaliteService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class LienLocaliteServiceImpl extends PerseusAbstractServiceImpl implements LienLocaliteService {

    @Override
    public int count(LienLocaliteSearchModel search) throws ParametresException, JadePersistenceException {
        if (search == null) {
            throw new ParametresException("Unable to count LienLocalite, the search model passed is null !");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public LienLocalite create(LienLocalite lienLocalite) throws JadePersistenceException, ParametresException {
        if (lienLocalite == null) {
            throw new ParametresException("Unable to create LienLocalite, the given model is null !");
        }

        try {
            SimpleLienLocalite simpleLienLocalite = lienLocalite.getSimpleLienLocalite();
            simpleLienLocalite.setIdLocalite(lienLocalite.getLocaliteSimpleModel().getIdLocalite());
            simpleLienLocalite.setIdZone(lienLocalite.getSimpleZone().getId());
            simpleLienLocalite = PerseusImplServiceLocator.getSimpleLienLocaliteService().create(simpleLienLocalite);
            lienLocalite.setSimpleLienLocalite(simpleLienLocalite);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available -" + e.getMessage());
        }
        return lienLocalite;
    }

    @Override
    public LienLocalite delete(LienLocalite lienLocalite) throws JadePersistenceException, ParametresException {
        if (lienLocalite == null) {
            throw new ParametresException("Unable to delete LienLocalite, the given model is null !");
        }

        try {
            lienLocalite.setSimpleLienLocalite(PerseusImplServiceLocator.getSimpleLienLocaliteService().delete(
                    lienLocalite.getSimpleLienLocalite()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available - " + e.getMessage());
        }
        return lienLocalite;
    }

    @Override
    public LienLocalite read(String idLienLocalite) throws JadePersistenceException, ParametresException {
        if (JadeStringUtil.isEmpty(idLienLocalite)) {
            throw new ParametresException("Unable to read LienLocalite, the id passed is null !");
        }
        LienLocalite lienLocalite = new LienLocalite();
        lienLocalite.setId(idLienLocalite);
        return (LienLocalite) JadePersistenceManager.read(lienLocalite);
    }

    @Override
    public LienLocaliteSearchModel search(LienLocaliteSearchModel searchModel) throws JadePersistenceException,
            ParametresException {
        if (searchModel == null) {
            throw new ParametresException("Unable to search LienLocalite, the search model passed is null !");
        }
        return (LienLocaliteSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public LienLocalite update(LienLocalite lienLocalite) throws JadePersistenceException, ParametresException {
        if (lienLocalite == null) {
            throw new ParametresException("Unable to update LienLocalite, the given model is null!");
        }

        try {
            // Mise à jour du lien localite
            SimpleLienLocalite simpleLienLocalite = lienLocalite.getSimpleLienLocalite();
            simpleLienLocalite.setIdLocalite(lienLocalite.getLocaliteSimpleModel().getIdLocalite());
            simpleLienLocalite.setIdZone(lienLocalite.getSimpleZone().getId());
            lienLocalite.setSimpleLienLocalite(PerseusImplServiceLocator.getSimpleLienLocaliteService().update(
                    simpleLienLocalite));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available - " + e.getMessage());
        }

        return lienLocalite;
    }

}
