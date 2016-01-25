package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRente;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRenteSearch;
import ch.globaz.pegasus.business.services.models.parametre.SimpleConversionRenteService;
import ch.globaz.pegasus.businessimpl.checkers.parametre.SimpleConversionRenteChecker;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class SimpleConversionRenteServiceImpl implements SimpleConversionRenteService {

    @Override
    public int count(SimpleConversionRenteSearch search) throws ConversionRenteException, JadePersistenceException {
        if (search == null) {
            throw new ConversionRenteException(
                    "Unable to count SimpleVariableMetierSearch, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleConversionRente create(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException {
        if (simpleConversionRente == null) {
            throw new ConversionRenteException("Unable to create simpleConversionRente, the model passed is null!");
        }

        SimpleConversionRenteChecker.checkForCreate(simpleConversionRente);

        return (SimpleConversionRente) JadePersistenceManager.add(simpleConversionRente);
    }

    @Override
    public SimpleConversionRente delete(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException {

        if (simpleConversionRente == null) {
            throw new ConversionRenteException("Unable to delete simpleConversionRente, the model passed is null!");
        }

        return (SimpleConversionRente) JadePersistenceManager.delete(simpleConversionRente);
    }

    @Override
    public SimpleConversionRente read(String id) throws ConversionRenteException, JadePersistenceException {
        if (id == null) {
            throw new ConversionRenteException("Unable to read id, the id passed is null!");
        }
        SimpleConversionRente conversionRente = new SimpleConversionRente();
        conversionRente.setId(id);
        return (SimpleConversionRente) JadePersistenceManager.read(conversionRente);
    }

    @Override
    public SimpleConversionRenteSearch search(SimpleConversionRenteSearch search) throws ConversionRenteException,
            JadePersistenceException {
        if (search == null) {
            throw new ConversionRenteException(
                    "Unable to search SimpleConversionRenteSearch, the model passed is null!");
        }
        return (SimpleConversionRenteSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleConversionRente update(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException {

        if (simpleConversionRente == null) {
            throw new ConversionRenteException("Unable to update simpleConversionRente, the model passed is null!");
        }

        SimpleConversionRenteChecker.checkForUpdate(simpleConversionRente);

        return (SimpleConversionRente) JadePersistenceManager.update(simpleConversionRente);

    }

}
