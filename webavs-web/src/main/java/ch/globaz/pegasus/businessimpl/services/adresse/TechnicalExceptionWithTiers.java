package ch.globaz.pegasus.businessimpl.services.adresse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class TechnicalExceptionWithTiers extends RuntimeException {

    public TechnicalExceptionWithTiers(String message, String idTiers) {
        super(message + " " + readDescriptionTiers(idTiers));
    }

    public TechnicalExceptionWithTiers(String message, String idTiers, String... params) {
        super(message + " " + readDescriptionTiers(idTiers));
    }

    public TechnicalExceptionWithTiers(String message, String idTiers, Exception e) {
        super(message + " " + readDescriptionTiers(idTiers), e);
    }

    public static String readDescriptionTiers(String idTiers) {
        return generateTiersMessage(readTiers(idTiers));
    }

    public TechnicalExceptionWithTiers(String message, PersonneEtendueComplexModel personne, Exception e) {
        super(message + " " + generateTiersMessage(personne), e);
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

    private static String generateTiersMessage(PersonneEtendueComplexModel personne) {
        String msg = "Tiers: " + personne.getPersonneEtendue().getNumAvsActuel() + " "
                + personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2();
        return msg;
    }

}
