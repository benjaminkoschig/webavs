package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleRenteAvsAiService;
import ch.globaz.pegasus.businessimpl.checkers.renteijapi.SimpleRenteAvsAiChecker;

/**
 * Classe pour le service des rentesAvsAi
 * 
 * @author SCE
 * 
 */
public class SimpleRenteAvsAiServiceImpl extends PegasusServiceLocator implements SimpleRenteAvsAiService {

    /**
     * Création d'une rente AvsAi
     * 
     * @param renteAvsAi
     *            la rente AvsAi à créer
     * @throws RenteAvsAiException
     * @return simpleRenteAvsAi la rente crée
     */
    @Override
    public SimpleRenteAvsAi create(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException {
        if (renteAvsAi == null) {
            throw new RenteAvsAiException("Unable to create renteAvsAi, the model passed is null!");
        }
        SimpleRenteAvsAiChecker.checkForCreate(renteAvsAi);

        return (SimpleRenteAvsAi) JadePersistenceManager.add(renteAvsAi);
    }

    /**
     * Suppression d'une rente AvsAi
     * 
     * @param renteAvsAi
     *            la rente AvsAi à supprimer
     * @throws RenteAvsAiException
     * @return simpleRenteAvsAi la rente supprimé
     */
    @Override
    public SimpleRenteAvsAi delete(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException {

        // Check si model pas null
        if (renteAvsAi == null) {
            throw new RenteAvsAiException("Unable to delete renteAvsAi, the model passed is null!");
        }
        // check si model pas new
        if (renteAvsAi.isNew()) {
            throw new RenteAvsAiException("Unable to delelte renteAvsAi, the model passed is new!");
        }
        SimpleRenteAvsAiChecker.checkForDelete(renteAvsAi);
        return (SimpleRenteAvsAi) JadePersistenceManager.delete(renteAvsAi);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.renteijapi.SimpleRenteAvsAiService
     * #deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleRenteAvsAiSearch search = new SimpleRenteAvsAiSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);

    }

    /**
     * Chargement d'une rente AvsAi
     * 
     * @param idRenteAvsAi
     *            l'identifiant de la rente à charger
     * @throws RenteAvsAiException
     * @return renteAvsAi la rente chargé
     */
    @Override
    public SimpleRenteAvsAi read(String idRenteAvsAi) throws RenteAvsAiException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRenteAvsAi)) {
            throw new RenteAvsAiException("Unable to read the model, the id passed is null!");
        }
        SimpleRenteAvsAi simpleRenteAvsAi = new SimpleRenteAvsAi();
        simpleRenteAvsAi.setId(idRenteAvsAi);

        return (SimpleRenteAvsAi) JadePersistenceManager.read(simpleRenteAvsAi);
    }

/**
	 * Mise à jour de la rente AvsAi
	 * 
	 * @throws RenteAvsAiException, {@link JadePersistenceException
	 * @param renteAvsAi la rente à mettre à jour
	 * @return RenteAvsAiException la rente mise à jour
	 * 
	 */
    @Override
    public SimpleRenteAvsAi update(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException {
        // check si model pas null
        if (renteAvsAi == null) {
            throw new RenteAvsAiException("Unable to update the renteAvsAi, the model passed is null!");
        }
        // check si model pas new
        if (renteAvsAi.isNew()) {
            throw new RenteAvsAiException("Unable to update renteAvsAi, the model passed is new!");
        }
        SimpleRenteAvsAiChecker.checkForUpdate(renteAvsAi);
        return (SimpleRenteAvsAi) JadePersistenceManager.update(renteAvsAi);
    }

}
