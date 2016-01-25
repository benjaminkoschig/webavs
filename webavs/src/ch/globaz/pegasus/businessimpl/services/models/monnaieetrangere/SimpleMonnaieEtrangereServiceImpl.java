package ch.globaz.pegasus.businessimpl.services.models.monnaieetrangere;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangereSearch;
import ch.globaz.pegasus.business.services.models.monnaieetrangere.SimpleMonnaieEtrangereService;
import ch.globaz.pegasus.businessimpl.checkers.monnaieetrangere.SimpleMonnaieEtrangereChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleMonnaieEtrangereServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleMonnaieEtrangereService {

    /**
     * @param simpleMonnaieEtrangere
     * @throws MonnaieEtrangereException
     * @throws JadePersistenceException
     * 
     *             Recherche si une période est ouverte pour la meme monnaie, et le clot.
     */
    private void closeOpenPeriode(SimpleMonnaieEtrangere simpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException {

        // recherche si periode ouverte existe deja pour meme monnaie
        SimpleMonnaieEtrangereSearch simpleMonnaieEtrangereSearch = new SimpleMonnaieEtrangereSearch();
        simpleMonnaieEtrangereSearch.setForCsTypeMonnaie(simpleMonnaieEtrangere.getCsTypeMonnaie());
        simpleMonnaieEtrangereSearch.setWhereKey("withPeriodeOpen");
        simpleMonnaieEtrangereSearch = (SimpleMonnaieEtrangereSearch) JadePersistenceManager
                .search(simpleMonnaieEtrangereSearch);

        // si recherche > 1, exception
        if (simpleMonnaieEtrangereSearch.getSize() > 1) {
            throw new MonnaieEtrangereException("Unable to create monnaieetrangere, more one monnaieetrangere found!");
        }
        // Si result, traitement update période anterieur
        else if (simpleMonnaieEtrangereSearch.getSize() == 1) {

            // objet to update
            SimpleMonnaieEtrangere simpleMonnaieEtrangereToUpdate = (SimpleMonnaieEtrangere) simpleMonnaieEtrangereSearch
                    .getSearchResults()[0];
            // on update seulement si la période n'a pas de date supérieur,
            // seulement si la période est la plus récente
            if (JadeDateUtil.isDateMonthYearAfter(simpleMonnaieEtrangere.getDateDebut(),
                    simpleMonnaieEtrangereToUpdate.getDateDebut())) {

                // Conversion date objet passe (ddmmyyyy)-> objet recherche

                JACalendarGregorian j = new JACalendarGregorian();

                try {
                    // Set date de fin periode ouverte
                    simpleMonnaieEtrangereToUpdate.setDateFin(JACalendar.format(
                            j.addMonths(simpleMonnaieEtrangere.getDateDebut(), -1), JACalendar.FORMAT_MMsYYYY));
                    // update
                    update(simpleMonnaieEtrangereToUpdate);
                } catch (JAException e) {
                    throw new MonnaieEtrangereException("unable to update MonnaieEtrangere, the date format is wrong");
                }
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. SimpleMonnaieEtrangereService
     * #count(ch.globaz.pegasus.business.models.monnaieetrangere .SimpleMonnaieEtrangereSearch)
     */
    @Override
    public int count(SimpleMonnaieEtrangereSearch search) throws MonnaieEtrangereException, JadePersistenceException {
        if (search == null) {
            throw new MonnaieEtrangereException("Unable to count monnaieetrangere, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. SimpleMonnaieEtrangereService
     * #create(ch.globaz.pegasus.business.models.monnaieetrangere .SimpleMonnaieEtrangere)
     */
    @Override
    public SimpleMonnaieEtrangere create(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException {

        if (simpleMonnaieEtrangere == null) {
            throw new MonnaieEtrangereException("unable to create monnaieetrangere, the model passed is null");
        }

        // recherche si periode ouverte existe deja pour meme monnaie
        closeOpenPeriode(simpleMonnaieEtrangere);

        SimpleMonnaieEtrangereChecker.checkForCreate(simpleMonnaieEtrangere);

        return (SimpleMonnaieEtrangere) JadePersistenceManager.add(simpleMonnaieEtrangere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. SimpleMonnaieEtrangereService
     * #delete(ch.globaz.pegasus.business.models.monnaieetrangere .SimpleMonnaieEtrangere)
     */
    @Override
    public SimpleMonnaieEtrangere delete(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException {
        if (simpleMonnaieEtrangere == null) {
            throw new MonnaieEtrangereException("Unable to delete monnaieetrangere, the model passed is null!");
        }
        if (simpleMonnaieEtrangere.isNew()) {
            throw new MonnaieEtrangereException("Unable to delete monnaieetrangere, the model passed is new!");
        }

        SimpleMonnaieEtrangereChecker.checkForDelete(simpleMonnaieEtrangere);
        return (SimpleMonnaieEtrangere) JadePersistenceManager.delete(simpleMonnaieEtrangere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere.
     * SimpleMonnaieEtrangereService#read(java.lang.String)
     */
    @Override
    public SimpleMonnaieEtrangere read(String idSimpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimpleMonnaieEtrangere)) {
            throw new MonnaieEtrangereException("Unable to read monnaieetrangere, the id passed is not defined!");
        }
        SimpleMonnaieEtrangere simpleMonnaieEtrangere = new SimpleMonnaieEtrangere();
        simpleMonnaieEtrangere.setId(idSimpleMonnaieEtrangere);
        return (SimpleMonnaieEtrangere) JadePersistenceManager.read(simpleMonnaieEtrangere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. SimpleMonnaieEtrangereService
     * #search(ch.globaz.pegasus.business.models.monnaieetrangere .SimpleMonnaieEtrangereSearch)
     */
    @Override
    public SimpleMonnaieEtrangereSearch search(SimpleMonnaieEtrangereSearch simpleMonnaieEtrangereSearch)
            throws JadePersistenceException, MonnaieEtrangereException {

        if (simpleMonnaieEtrangereSearch == null) {
            throw new MonnaieEtrangereException("Unable to search monnaieetrangere, the search model passed is null!");
        }
        return (SimpleMonnaieEtrangereSearch) JadePersistenceManager.search(simpleMonnaieEtrangereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. SimpleMonnaieEtrangereService
     * #update(ch.globaz.pegasus.business.models.monnaieetrangere .SimpleMonnaieEtrangere)
     */
    @Override
    public SimpleMonnaieEtrangere update(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException {

        if (simpleMonnaieEtrangere == null) {
            throw new MonnaieEtrangereException("Unable to update monnaieetrangere, the  model passed is null!");
        }
        SimpleMonnaieEtrangereChecker.checkForUpdate(simpleMonnaieEtrangere);
        return (SimpleMonnaieEtrangere) JadePersistenceManager.update(simpleMonnaieEtrangere);
    }
}
