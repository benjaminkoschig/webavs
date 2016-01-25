package ch.globaz.pegasus.businessimpl.services.adresse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class TechnicalExceptionWithTiers extends RuntimeException {

    public TechnicalExceptionWithTiers(Exception e) {
        super(e);
    }

    public TechnicalExceptionWithTiers(String message, Exception e) {
        super(message, e);
    }

    public TechnicalExceptionWithTiers(String message, String idTiers, Exception e) {
        super(message + " " + generateTiersMessage(readTiers(idTiers)), e);
        readTiers(idTiers);
    }

    private static PersonneEtendueComplexModel readTiers(String idTiers) {
        try {
            return TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e.toString(), e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

    public TechnicalExceptionWithTiers(String message, PersonneEtendueComplexModel personne, Exception e) {
        super(message + " " + generateTiersMessage(personne), e);
    }

    private static String generateTiersMessage(PersonneEtendueComplexModel personne) {
        String msg = " Tiers concerné: " + personne.getPersonneEtendue().getNumAvsActuel() + " "
                + personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2();
        return msg;
    }

}
