package ch.globaz.perseus.businessimpl.services.models.variablemetier;

import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.constantes.IPFVariableMetier;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.perseus.business.models.variablemetier.SimpleVariableMetierSearch;
import ch.globaz.perseus.business.services.models.variablemetier.SimpleVariableMetierService;
import ch.globaz.perseus.businessimpl.checkers.variablemetier.SimpleVariableMetierChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.utils.Parametre;

/**
 * @author DMA
 * 
 */
public class SimpleVariableMetierServiceImpl extends PerseusAbstractServiceImpl implements SimpleVariableMetierService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. SimpleVaraibleService
     * #count(ch.globaz.perseus.business.models.variableMetier .SimpleVariableMetierSearch)
     */

    @Override
    public int count(SimpleVariableMetierSearch search) throws VariableMetierException, JadePersistenceException {
        if (search == null) {
            throw new VariableMetierException("Unable to count variableMetier, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. SimpleVaraibleService
     * #create(ch.globaz.perseus.business.models.variableMetier .SimpleVariableMetier)
     */
    @Override
    public SimpleVariableMetier create(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {
        if (variableMetier == null) {
            throw new VariableMetierException("Unable to creat variableMetier, the model passed is null!");
        }
        variableMetier.setTypeDeDonnee(findTypeDeDonner(variableMetier));
        updateDateFinOpen(variableMetier);
        SimpleVariableMetierChecker.checkForCreate(variableMetier);

        return (SimpleVariableMetier) JadePersistenceManager.add(variableMetier);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. SimpleVaraibleService
     * #delete(ch.globaz.perseus'.business.models.variableMetier .SimpleVariableMetier)
     */
    @Override
    public SimpleVariableMetier delete(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {
        if (variableMetier == null) {
            throw new VariableMetierException("Unable to delete variableMetier, the  model passed is null!");
        }
        if (variableMetier.isNew()) {
            throw new VariableMetierException("Unable to delete variableMetier, the  model passed is new!");
        }
        SimpleVariableMetierChecker.checkForDelete(variableMetier);
        return (SimpleVariableMetier) JadePersistenceManager.delete(variableMetier);
    }

    /**
     * Permet de trouver la constante liée à la donnée qui est enregistré. Ceci permet de connaître le champs dans la bd
     * qui contient la valeur à utiliser. Le choix de créer un colonne supplémentaire permet de contourner la non
     * gestion des valeurs null.( Montant =1 Taux = 2 Fraction = 3)
     * 
     * @param variableMetier
     * @return int
     */
    private String findTypeDeDonner(SimpleVariableMetier variableMetier) {
        String typeDeDonnee = null;
        if (!JadeStringUtil.isEmpty(variableMetier.getMontant())) {
            typeDeDonnee = IPFVariableMetier.CS_MONTANT;
        } else if (!JadeStringUtil.isEmpty(variableMetier.getTaux())) {
            typeDeDonnee = IPFVariableMetier.CS_TAUX;
        } else if (!JadeStringUtil.isEmpty(variableMetier.getFractionDenominateur())) {
            typeDeDonnee = IPFVariableMetier.CS_FRACTION;
        }
        return typeDeDonnee;
    }

    /**
     * Ferme la date valable(encour) en fonction da la nouvelle variableMetier. Date de fin (Ancien) = Prend la nouvelle
     * date de début moins 1 jour
     * 
     * @param variableMetier
     * @throws VariableMetierException. Si
     *             la date donnée n'est pas au bon format ou si il existe plusieurs variableMetier encour
     * @throws JadePersistenceException
     */
    private boolean isFiledsOk(SimpleVariableMetier variableMetier) {
        return !(JadeStringUtil.isEmpty(variableMetier.getDateDebut()) && JadeStringUtil.isEmpty(variableMetier
                .getCsTypeVariableMetier()));
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. SimpleVaraibleService #read(java.lang.String)
     */
    @Override
    public SimpleVariableMetier read(String idVariableMetier) throws VariableMetierException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idVariableMetier)) {
            throw new VariableMetierException("Unable to read variableMetier, the id passed is not defined!");
        }
        SimpleVariableMetier variableMetier = new SimpleVariableMetier();
        variableMetier.setId(idVariableMetier);
        return (SimpleVariableMetier) JadePersistenceManager.read(variableMetier);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. SimpleVaraibleService
     * #update(ch.globaz.perseus.business.models.variableMetier
     */

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. SimpleVariableMetierService
     * #search(ch.globaz.perseus.business.models.variablemetier .SimpleVariableMetierSearch)
     */
    @Override
    public SimpleVariableMetierSearch search(SimpleVariableMetierSearch simpleVariableMetierSearch)
            throws VariableMetierException, JadePersistenceException {
        if (simpleVariableMetierSearch == null) {
            throw new VariableMetierException("Unable to search variableMetier, the  model passed is null!");
        }

        return (SimpleVariableMetierSearch) JadePersistenceManager.search(simpleVariableMetierSearch);
    }

    @Override
    public SimpleVariableMetier update(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {
        variableMetier.setTypeDeDonnee(findTypeDeDonner(variableMetier));
        return this.update(variableMetier, true);
    }

    private SimpleVariableMetier update(SimpleVariableMetier variableMetier, boolean check)
            throws VariableMetierException, JadePersistenceException {
        if (variableMetier == null) {
            throw new VariableMetierException("Unable to update variableMetier, the  model passed is null!");
        }
        if (check) {
            SimpleVariableMetierChecker.checkForUpdate(variableMetier);
        }
        return (SimpleVariableMetier) JadePersistenceManager

        .update(variableMetier);
    }

    private void updateDateFinOpen(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {

        if (isFiledsOk(variableMetier)) {
            SimpleVariableMetierSearch variableMetierSearch = new SimpleVariableMetierSearch();
            variableMetierSearch.setForforCsTypeVariableMetier(variableMetier.getCsTypeVariableMetier());
            variableMetierSearch.setWhereKey("withPeriodeOpen");
            variableMetierSearch = (SimpleVariableMetierSearch) JadePersistenceManager.search(variableMetierSearch);

            if (variableMetierSearch.getSize() > 1) {
                throw new VariableMetierException("Unable to creat variableMetier, more one variableMetier found!");
            } else if ((variableMetierSearch.getSize() == 1)) {
                // JACalendarGregorian calendarGregorian = new
                // JACalendarGregorian();
                SimpleVariableMetier simpleVariableMetierToUpdate = (SimpleVariableMetier) variableMetierSearch
                        .getSearchResults()[0];
                if (!JadeDateUtil.areDatesEquals(simpleVariableMetierToUpdate.getDateDebut(),
                        variableMetier.getDateDebut())) {
                    // il ne faut pas updater la date de fin si la date de la
                    // nouvelle pèriode est inferieur à la date de debut de la
                    // période a updater
                    if (JadeDateUtil.isDateMonthYearAfter(variableMetier.getDateDebut(),
                            simpleVariableMetierToUpdate.getDateDebut())) {
                        try {
                            /*
                             * simpleVariableMetierToUpdate.setDateFin(JACalendar .format(calendarGregorian.addMonths(
                             * variableMetier.getDateDebut(), -1), JACalendar.FORMAT_MMsYYYY));
                             */

                            simpleVariableMetierToUpdate.setDateFin(Parametre.removeOneMonth(variableMetier
                                    .getDateDebut()));

                            this.update(simpleVariableMetierToUpdate, false);

                        } catch (JAException e) {
                            throw new VariableMetierException(
                                    "Unable to update variableMetier, the date fromat is not good!");
                        }
                    }
                } else {
                    JadeThread.logError(variableMetier.getClass().getName(),
                            "perseus.simpleVariableMetier.dateDebutEgale.mandatory");
                }
            }
        }
    }
}
