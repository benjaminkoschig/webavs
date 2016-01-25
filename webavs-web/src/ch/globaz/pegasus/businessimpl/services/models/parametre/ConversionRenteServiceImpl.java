package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.ConversionRente;
import ch.globaz.pegasus.business.models.parametre.ConversionRenteSearch;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRente;
import ch.globaz.pegasus.business.services.models.parametre.ConversionRenteService;
import ch.globaz.pegasus.business.services.models.parametre.SimpleConversionRenteService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class ConversionRenteServiceImpl extends PegasusAbstractServiceImpl implements ConversionRenteService {

    private ConversionRenteSearch conversionRenteSearch = null;

    private ConversionRenteSearch converToSearchAgeAndDateDebut(ConversionRente conversionRente) {
        conversionRenteSearch = new ConversionRenteSearch();
        conversionRenteSearch.setForAge(conversionRente.getSimpleConversionRente().getAge());
        conversionRenteSearch.setForDateDebut(conversionRente.getSimpleConversionRente().getDateDebut());
        // search.setForDateFin(conversionRente.getSimpleConversionRente().getDateFin());
        return conversionRenteSearch;
    }

    @Override
    public int count(ConversionRenteSearch search) throws ConversionRenteException, JadePersistenceException {
        if (search == null) {
            throw new ConversionRenteException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public ConversionRente create(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException {
        int size = 0;
        ConversionRente conversionRenteFind = null;
        SimpleConversionRente simpleConversionRente = null;
        if (conversionRente == null) {
            throw new ConversionRenteException("Unable to create conversionRente, the model passed is null!");
        }

        conversionRenteSearch = converToSearchAgeAndDateDebut(conversionRente);
        conversionRenteSearch.setWhereKey("withValueYunger");
        search(conversionRenteSearch);

        size = conversionRenteSearch.getSize();
        if (size > 0) {
            conversionRenteFind = ((ConversionRente) conversionRenteSearch.getSearchResults()[size - 1]);
            if (JadeDateUtil.isDateAfter(conversionRente.getSimpleConversionRente().getDateDebut(), conversionRenteFind
                    .getSimpleConversionRente().getDateDebut())) {
                // on insère la plus viellie valeur il pas besoin de faire
                // d'update
                conversionRente.getSimpleConversionRente().setDateFin(getDateDebutMoins1Jour(conversionRenteFind));
            } else {
                // on insére une ancien valeur, on se situe entre 2 valeurs,
                conversionRente.getSimpleConversionRente().setDateFin(getDateDebutMoins1Jour(conversionRenteFind));

                conversionRenteSearch.setForDateFin(getDateDebutMoins1Jour(conversionRenteFind));
                conversionRenteSearch.setWhereKey("oldLastValue");

                // Recheche l'ancien valeur en fonction de la date de fin qui
                // n'est pas
                // null
                conversionRenteSearch = search(conversionRenteSearch);

                if (conversionRenteSearch.getSize() == 1) {
                    simpleConversionRente = ((ConversionRente) conversionRenteSearch.getSearchResults()[0])
                            .getSimpleConversionRente();
                    simpleConversionRente.setDateFin(getDateDebutMoins1Jour(conversionRente));
                    if (JadeDateUtil.isDateAfter(simpleConversionRente.getDateFin(),
                            simpleConversionRente.getDateDebut())) {
                        getSimpleService().update(simpleConversionRente);
                    }
                }
            }
        } else {
            updateDateFinOpen(conversionRente);
        }

        simpleConversionRente = conversionRente.getSimpleConversionRente();

        try {
            conversionRente.setSimpleConversionRente(getSimpleService().create(simpleConversionRente));
        } catch (JadePersistenceException e) {
            throw new ConversionRenteException("Unable to create conversionRente", e);
        }

        return conversionRente;
    }

    @Override
    public ConversionRente delete(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException {
        if (conversionRente == null) {
            throw new ConversionRenteException("Unable to delete conversionRente, the model passed is null!");
        }
        this.updateDateFinForOldValue(conversionRente, true);
        getSimpleService().delete(conversionRente.getSimpleConversionRente());
        return conversionRente;
    }

    private String getDateDebutMoins1Jour(ConversionRente conversionRente) {
        return JadeDateUtil.addDays(conversionRente.getSimpleConversionRente().getDateDebut(), -1);
    }

    private SimpleConversionRenteService getSimpleService() throws ConversionRenteException {
        Throwable throwable = new Throwable();
        StackTraceElement ste = throwable.getStackTrace()[1];
        String nameMethodeCallMe = ste.getMethodName();
        try {
            return PegasusImplServiceLocator.getSimpleConversionRenteService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ConversionRenteException("Unable to " + nameMethodeCallMe
                    + " conversionRente the service is not available", e);
        }
    }

    public boolean isValueBetweenPeriode(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException {
        conversionRenteSearch = new ConversionRenteSearch();
        conversionRenteSearch.setWhereKey("hasDateBetween");
        return search(conversionRenteSearch).getSize() > 0;
    }

    @Override
    public ConversionRente read(String idConversionRente) throws ConversionRenteException, JadePersistenceException {
        if (idConversionRente == null) {
            throw new ConversionRenteException("Unable to read idConversionRente, the id passed is null!");
        }
        ConversionRente conversionRente = new ConversionRente();
        conversionRente.setId(idConversionRente);
        return (ConversionRente) JadePersistenceManager.read(conversionRente);
    }

    @Override
    public ConversionRenteSearch search(ConversionRenteSearch conversionRenteSearch) throws ConversionRenteException,
            JadePersistenceException {
        if (conversionRenteSearch == null) {
            throw new ConversionRenteException("Unable to search conversionRenteSearch, the model passed is null!");
        }
        return (ConversionRenteSearch) JadePersistenceManager.search(conversionRenteSearch);
    }

    @Override
    public ConversionRente update(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException {
        /*
         * conversionRente.getSimpleConversionRente().setDateDebut(
         * JACalendar.format(conversionRente.getSimpleConversionRente().getDateDebut()));
         */
        this.updateDateFinForOldValue(conversionRente);
        getSimpleService().update(conversionRente.getSimpleConversionRente());
        return conversionRente;
    }

    private void updateDateFinForOldValue(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException {
        this.updateDateFinForOldValue(conversionRente, false);
    }

    private void updateDateFinForOldValue(ConversionRente conversionRente, boolean delete)
            throws ConversionRenteException, JadePersistenceException {

        ConversionRente conversionRenteOld = read(conversionRente.getId());
        SimpleConversionRente simpleConversionRente = null;
        conversionRenteSearch = new ConversionRenteSearch();
        conversionRenteSearch.setForAge(conversionRente.getSimpleConversionRente().getAge());
        conversionRenteSearch.setForDateFin(getDateDebutMoins1Jour(conversionRenteOld));
        conversionRenteSearch.setForDateDebut(conversionRenteOld.getSimpleConversionRente().getDateDebut());
        conversionRenteSearch.setForIdConversionRente(conversionRente.getId());
        conversionRenteSearch.setWhereKey("oldLastValue");

        // Recheche l'ancien valeur en fonction de la date de fin qui n'est pas
        // null
        conversionRenteSearch = search(conversionRenteSearch);

        if (conversionRenteSearch.getSize() == 1) {
            simpleConversionRente = ((ConversionRente) conversionRenteSearch.getSearchResults()[0])
                    .getSimpleConversionRente();

            if (!JadeDateUtil.isDateAfter(simpleConversionRente.getDateFin(), simpleConversionRente.getDateDebut())
                    && !delete) {
                conversionRenteSearch.setWhereKey("lastValues");
                conversionRenteSearch = search(conversionRenteSearch);

                if (conversionRenteSearch.getSize() == 1) {
                    simpleConversionRente = ((ConversionRente) conversionRenteSearch.getSearchResults()[0])
                            .getSimpleConversionRente();
                    // simpleConversionRente.setDateFin(this.getDateDebutMoins1Jour(conversionRente));
                }
            }

            if (delete) {
                simpleConversionRente.setDateFin(null);
            } else {
                simpleConversionRente.setDateFin(getDateDebutMoins1Jour(conversionRente));
            }
            getSimpleService().update(simpleConversionRente);
        }

    }

    private void updateDateFinOpen(ConversionRente conversionRente) throws ConversionRenteException {
        SimpleConversionRente simpleConversionRente = null;
        // SimpleConversionRenteSearch search = new
        // SimpleConversionRenteSearch();
        // search.setForAge(conversionRente.getSimpleConversionRente().getAge());
        // search.setForDateFin(null);

        converToSearchAgeAndDateDebut(conversionRente);
        conversionRenteSearch.setWhereKey("withPeriodeOpen");
        try {
            conversionRenteSearch = search(conversionRenteSearch);
            if (conversionRenteSearch.getSize() > 1) {
                throw new ConversionRenteException("Unable to create conversionRente, more one ConversionRente found");
            } else if (conversionRenteSearch.getSize() == 1) {
                simpleConversionRente = ((ConversionRente) (conversionRenteSearch.getSearchResults()[0]))
                        .getSimpleConversionRente();
                simpleConversionRente.setDateFin(getDateDebutMoins1Jour(conversionRente));

                getSimpleService().update(simpleConversionRente);
            }

        } catch (JadePersistenceException e) {
            throw new ConversionRenteException("Unable to creat conversionRente", e);
        }

        // this.getSimpleService().update(simpleConversionRente);
    }
}
