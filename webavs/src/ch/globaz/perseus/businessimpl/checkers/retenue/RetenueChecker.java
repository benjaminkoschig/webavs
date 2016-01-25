package ch.globaz.perseus.businessimpl.checkers.retenue;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.retenue.Retenue;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class RetenueChecker extends PerseusAbstractChecker {

    public static void checkForCreate(Retenue retenue) throws RetenueException, JadePersistenceException {
        RetenueChecker.checkMandatory(retenue);
    }

    public static void checkForDelete(Retenue retenue) throws RetenueException, JadePersistenceException {

    }

    public static void checkForUpdate(Retenue retenue) {
        RetenueChecker.checkMandatory(retenue);
    }

    private static void checkMandatory(Retenue retenue) {

    }

}
