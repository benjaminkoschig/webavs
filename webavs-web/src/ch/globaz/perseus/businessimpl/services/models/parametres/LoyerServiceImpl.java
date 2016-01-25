package ch.globaz.perseus.businessimpl.services.models.parametres;

/**
 * MBO
 */

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSTypeLoyer;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.models.parametres.LienLocaliteSearchModel;
import ch.globaz.perseus.business.models.parametres.Loyer;
import ch.globaz.perseus.business.models.parametres.LoyerSearchModel;
import ch.globaz.perseus.business.models.parametres.SimpleLoyer;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.parametres.LoyerService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class LoyerServiceImpl extends PerseusAbstractServiceImpl implements LoyerService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.demande.DemandeService#count
     * (ch.globaz.perseus.business.models.demande.DemandeSearch)
     */
    @Override
    public int count(LoyerSearchModel search) throws ParametresException, JadePersistenceException {
        if (search == null) {
            throw new ParametresException("Unable to count loyer, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#create(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public Loyer create(Loyer loyer) throws JadePersistenceException, ParametresException {
        if (loyer == null) {
            throw new ParametresException("Unable to create loyer, the given model is null!");
        }

        try {
            // Création du loyer
            SimpleLoyer simpleLoyer = loyer.getSimpleLoyer();
            simpleLoyer.setIdZone(loyer.getSimpleZone().getId());
            simpleLoyer = PerseusImplServiceLocator.getSimpleLoyerService().create(simpleLoyer);
            loyer.setSimpleLoyer(simpleLoyer);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available - " + e.getMessage());
        }

        return loyer;
    }

    @Override
    public Loyer delete(Loyer loyer) throws JadePersistenceException, ParametresException {
        if (loyer == null) {
            throw new ParametresException("Unable to delete a loyer, the model passed is null!");
        }

        try {
            // Suppression du loyer
            loyer.setSimpleLoyer(PerseusImplServiceLocator.getSimpleLoyerService().delete(loyer.getSimpleLoyer()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available - " + e.getMessage());
        }

        return loyer;
    }

    @Override
    public Loyer read(String idLoyer) throws JadePersistenceException, ParametresException {
        if (JadeStringUtil.isEmpty(idLoyer)) {
            throw new ParametresException("Unable to read a loyer, the id passed is null!");
        }
        // Lecture du loyer
        Loyer loyer = new Loyer();
        loyer.setId(idLoyer);

        return (Loyer) JadePersistenceManager.read(loyer);
    }

    @Override
    public LoyerSearchModel search(LoyerSearchModel searchModel) throws JadePersistenceException, ParametresException {
        if (searchModel == null) {
            throw new ParametresException("Unable to search a loyer, the search model passed is null!");
        }

        return (LoyerSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public Loyer searchForLocalite(String idLocalite, Integer nbPersonnes, String date)
            throws JadePersistenceException, ParametresException {

        // Recherche de la localité
        LienLocaliteSearchModel lienLocaliteSearchModel = new LienLocaliteSearchModel();
        lienLocaliteSearchModel.setForDateValable(date);
        lienLocaliteSearchModel.setForIdLocalite(idLocalite);
        try {
            lienLocaliteSearchModel = PerseusServiceLocator.getLienLocaliteService().search(lienLocaliteSearchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available : " + e.getMessage());
        }

        if (lienLocaliteSearchModel.getSize() == 1) {
            LienLocalite lienLocalite = (LienLocalite) lienLocaliteSearchModel.getSearchResults()[0];
            // Recherche du plafond de loyer pour la zone
            LoyerSearchModel loyerSearchModel = new LoyerSearchModel();
            loyerSearchModel.setForIdZone(lienLocalite.getSimpleZone().getId());
            loyerSearchModel.setForDateValable(date);
            switch (nbPersonnes) {
                case 1:
                    loyerSearchModel.setForCsTypeLoyer(CSTypeLoyer.UNE_PERSONNE.getCodeSystem());
                    break;
                case 2:
                    loyerSearchModel.setForCsTypeLoyer(CSTypeLoyer.DEUX_PERSONNES.getCodeSystem());
                    break;
                case 3:
                    loyerSearchModel.setForCsTypeLoyer(CSTypeLoyer.TROIS_PERSONNES.getCodeSystem());
                    break;
                case 4:
                    loyerSearchModel.setForCsTypeLoyer(CSTypeLoyer.QUATRE_PERSONNES.getCodeSystem());
                    break;
                default:
                    loyerSearchModel.setForCsTypeLoyer(CSTypeLoyer.CINQ_PERSONNES_ET_PLUS.getCodeSystem());
                    break;
            }
            try {
                loyerSearchModel = PerseusServiceLocator.getLoyerService().search(loyerSearchModel);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new ParametresException("Service not available : " + e.getMessage());
            }
            if (loyerSearchModel.getSize() == 1) {
                return (Loyer) loyerSearchModel.getSearchResults()[0];
            } else {
                throw new ParametresException("Erreur lors de la recherche du plafond du loyer");
            }
        } else {
            throw new ParametresException(
                    "Erreur lors de la recherche de la localité pour retrouver le plafond du loyer, la localité n'est peut-être pas dans le canton de vaud");
        }
    }

    @Override
    public Loyer update(Loyer loyer) throws JadePersistenceException, ParametresException {
        if (loyer == null) {
            throw new ParametresException("Unable to update loyer, the given model is null!");
        }

        try {
            // Mise à jour du loyer
            SimpleLoyer simpleLoyer = loyer.getSimpleLoyer();
            simpleLoyer.setIdZone(loyer.getSimpleZone().getId());
            simpleLoyer = PerseusImplServiceLocator.getSimpleLoyerService().update(simpleLoyer);
            loyer.setSimpleLoyer(simpleLoyer);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available - " + e.getMessage());
        }

        return loyer;
    }
}
