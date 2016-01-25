package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRenteSearch;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleAutreRenteService;
import ch.globaz.pegasus.businessimpl.checkers.renteijapi.SimpleAutreRenteChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAutreRenteServiceImpl extends PegasusAbstractServiceImpl implements SimpleAutreRenteService {

    /*
     * public int count(SimpleAutreRenteSearch search) throws AutreRenteException, JadePersistenceException { if (search
     * == null) { throw new AutreRenteException( "Unable to count search, the model passed is null!"); } return
     * JadePersistenceManager.count(search); }
     */

    @Override
    public SimpleAutreRente create(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException {
        if (autreRente == null) {
            throw new AutreRenteException("Unable to create autreRente, the model passed is null!");
        }
        SimpleAutreRenteChecker.checkForCreate(autreRente);
        return (SimpleAutreRente) JadePersistenceManager.add(autreRente);
    }

    @Override
    public SimpleAutreRente delete(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException {
        if (autreRente == null) {
            throw new AutreRenteException("Unable to delete , the model autreRente passed is null!");
        }

        if (autreRente.isNew()) {
            throw new AutreRenteException("Unable to delete autreRente, the model passed is new!");
        }
        SimpleAutreRenteChecker.checkForDelete(autreRente);
        return (SimpleAutreRente) JadePersistenceManager.add(autreRente);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {

        SimpleAutreRenteSearch search = new SimpleAutreRenteSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.renteijapi.SimpleAutreRenteService
     * #deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public SimpleAutreRente read(String idSimpleAutreRente) throws AutreRenteException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimpleAutreRente)) {
            throw new AutreRenteException(
                    "Unable to read , the model JadeStringUtil.isEmpty(idSimpleAutreRente) passed is null!");
        }
        SimpleAutreRente autreRente = new SimpleAutreRente();
        autreRente.setId(idSimpleAutreRente);
        return (SimpleAutreRente) JadePersistenceManager.read(autreRente);
    }

    @Override
    public SimpleAutreRente update(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException {
        if (autreRente == null) {
            throw new AutreRenteException("Unable to update , the model autreRente passed is null!");
        }
        if (autreRente.isNew()) {
            throw new AutreRenteException("Unable to update autreRente, the model passed is new!");
        }
        SimpleAutreRenteChecker.checkForUpdate(autreRente);
        return (SimpleAutreRente) JadePersistenceManager.update(autreRente);
    }

}
