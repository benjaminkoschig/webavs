package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.RevenusFraisGardeChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.RevenuActiviteLucrativeDependanteService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class RevenuActiviteLucrativeDependanteServiceImpl extends PegasusAbstractServiceImpl implements
        RevenuActiviteLucrativeDependanteService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeDependanteService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeDependanteSearch)
     */
    @Override
    public int count(RevenuActiviteLucrativeDependanteSearch search) throws RevenuActiviteLucrativeDependanteException,
            JadePersistenceException {
        if (search == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to count RevenuActiviteLucrativeDependante, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeDependanteService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeDependante)
     */
    @Override
    public RevenuActiviteLucrativeDependante create(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException, DonneeFinanciereException {
        if (revenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to create RevenuActiviteLucrativeDependante, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante().getFraisDeGarde())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE);
            }

            revenuActiviteLucrativeDependante.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleRevenuActiviteLucrativeDependanteService().create(
                    revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante());
            List<SimpleTypeFraisObtentionRevenu> list = new ArrayList<SimpleTypeFraisObtentionRevenu>();

            for (SimpleTypeFraisObtentionRevenu model : revenuActiviteLucrativeDependante.getListTypeDeFrais()) {
                try {
                    model.setIdRevenuActiviteLucrativeDependante(revenuActiviteLucrativeDependante.getId());
                    list.add(PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService().create(model));
                } catch (SimpleTypeFraisObtentionRevenuException e) {
                    throw new RevenuActiviteLucrativeDependanteException(
                            "Unable to create RevenuActiviteLucrativeDependante, probleme with typeDeFrais!");
                }
            }

            revenuActiviteLucrativeDependante.setListTypeDeFrais(list);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeDependanteException("Service not available - " + e.getMessage());
        }

        return revenuActiviteLucrativeDependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeDependanteService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeDependante)
     */
    @Override
    public RevenuActiviteLucrativeDependante delete(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException {
        if (revenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to delete RevenuActiviteLucrativeDependante, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleRevenuActiviteLucrativeDependanteService().delete(
                    revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeDependanteException("Service not available - " + e.getMessage());
        }

        return revenuActiviteLucrativeDependante;
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RevenuActiviteLucrativeDependanteException {

        RevenuActiviteLucrativeDependanteSearch search = new RevenuActiviteLucrativeDependanteSearch();

        search.setForListIdDonneeFinanciere(idsDonneFinanciere);
        search = PegasusImplServiceLocator.getRevenuActiviteLucrativeDependanteService().search(search);

        for (JadeAbstractModel searchR : search.getSearchResults()) {
            RevenuActiviteLucrativeDependante revenu = (RevenuActiviteLucrativeDependante) searchR;
            // SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();

            SimpleTypeFraisObtentionRevenuSearch searchType = new SimpleTypeFraisObtentionRevenuSearch();
            searchType.setForIdRevenuActiviteLucrativeDependante(revenu.getId());
            PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService().delete(searchType);
        }

        PegasusImplServiceLocator.getSimpleRevenuActiviteLucrativeDependanteService().deleteParListeIdDoFinH(
                idsDonneFinanciere);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * RevenuActiviteLucrativeDependanteService#read(java.lang.String)
     */
    @Override
    public RevenuActiviteLucrativeDependante read(String idRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException {
        if (JadeStringUtil.isEmpty(idRevenuActiviteLucrativeDependante)) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to read RevenuActiviteLucrativeDependante, the id passed is null!");
        }
        RevenuActiviteLucrativeDependante RevenuActiviteLucrativeDependante = new RevenuActiviteLucrativeDependante();
        RevenuActiviteLucrativeDependante.setId(idRevenuActiviteLucrativeDependante);
        return (RevenuActiviteLucrativeDependante) JadePersistenceManager.read(RevenuActiviteLucrativeDependante);
    }

    /**
     * Chargement d'une RevenuActiviteLucrativeDependante via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuActiviteLucrativeDependanteException
     * @throws JadePersistenceException
     */
    @Override
    public RevenuActiviteLucrativeDependante readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to find RevenuActiviteLucrativeDependante the idDonneeFinanciereHeader passed si null!");
        }

        RevenuActiviteLucrativeDependanteSearch search = new RevenuActiviteLucrativeDependanteSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (RevenuActiviteLucrativeDependanteSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "More than one RevenuActiviteLucrativeDependante find, one was exepcted!");
        }

        return (RevenuActiviteLucrativeDependante) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((RevenuActiviteLucrativeDependanteSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeDependanteService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeDependanteSearch)
     */
    @Override
    public RevenuActiviteLucrativeDependanteSearch search(
            RevenuActiviteLucrativeDependanteSearch revenuActiviteLucrativeDependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException {
        if (revenuActiviteLucrativeDependanteSearch == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to search RevenuActiviteLucrativeDependante, the search model passed is null!");
        }
        return (RevenuActiviteLucrativeDependanteSearch) JadePersistenceManager
                .search(revenuActiviteLucrativeDependanteSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeDependanteService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeDependante)
     */
    @Override
    public RevenuActiviteLucrativeDependante update(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException, DonneeFinanciereException {
        if (revenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to update RevenuActiviteLucrativeDependante, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante().getFraisDeGarde())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE);
            }
            revenuActiviteLucrativeDependante.setSimpleRevenuActiviteLucrativeDependante(PegasusImplServiceLocator
                    .getSimpleRevenuActiviteLucrativeDependanteService().update(
                            revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante()));
            revenuActiviteLucrativeDependante.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeDependanteException("Service not available - " + e.getMessage());
        }

        return revenuActiviteLucrativeDependante;
    }

}
