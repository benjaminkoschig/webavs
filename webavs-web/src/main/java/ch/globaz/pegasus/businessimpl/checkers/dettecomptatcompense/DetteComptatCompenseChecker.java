package ch.globaz.pegasus.businessimpl.checkers.dettecomptatcompense;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.dettecomptatcompense.DetteComptatCompenseException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.business.vo.decompte.DetteEnComptaVO;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;

public class DetteComptatCompenseChecker {
    public static void checkForCreate(SimpleDetteComptatCompense detteComptatCompense) throws JadePersistenceException,
            JadeNoBusinessLogSessionError, JadeApplicationException {
        DetteComptatCompenseChecker.checkIntegrity(detteComptatCompense);
    }

    /**
     * @param detteComptatCompense
     */
    public static void checkForDelete(SimpleDetteComptatCompense detteComptatCompense) {
    }

    /**
     * @param detteComptatCompense
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static void checkForUpdate(SimpleDetteComptatCompense detteComptatCompense) throws JadePersistenceException,
            JadeApplicationException {
        DetteComptatCompenseChecker.checkIntegrity(detteComptatCompense);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param detteComptatCompense
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws exception
     */
    private static void checkIntegrity(SimpleDetteComptatCompense detteComptatCompense)
            throws JadePersistenceException, JadeApplicationException {
        if (!DetteComptatCompenseChecker.isEditable(detteComptatCompense.getIdVersionDroit())) {
            JadeThread.logError(detteComptatCompense.getClass().getName(), "pegasus.detteComptat.editable.integrity");
        }

        // On test que le montant modifié ne soit pas plus grand que le montant de la dette
        if (new BigDecimal(detteComptatCompense.getMontant()).subtract(
                new BigDecimal(detteComptatCompense.getMontantModifie())).signum() == -1) {
            String[] t = new String[1];
            t[0] = DetteComptatCompenseChecker.toString(detteComptatCompense);
            JadeThread.logError(detteComptatCompense.getClass().getName(),
                    "pegasus.detteComptat.montantModifiableTropGrand.integrity", t);
        }

        DecompteTotalPcVO decompte = PegasusServiceLocator.getDecompteService().getDecompteTotalPCA(
                detteComptatCompense.getIdVersionDroit());

        String montantDette = (JadeStringUtil.isBlankOrZero(detteComptatCompense.getMontantModifie())) ? detteComptatCompense
                .getMontant() : detteComptatCompense.getMontantModifie();

        DetteEnComptaVO oldDetteCompense = null;
        if (!detteComptatCompense.isNew()) {
            for (DetteEnComptaVO dette : decompte.getDettesCompta().getList()) {
                if (dette.getDette().getId().equals(detteComptatCompense.getId())) {
                    oldDetteCompense = dette;
                }
            }
        }
        BigDecimal montantDecompt = decompte.getTotal();
        if (oldDetteCompense != null) {
            String mnt = (JadeStringUtil.isBlankOrZero(oldDetteCompense.getDette().getMontantModifie())) ? oldDetteCompense
                    .getDette().getMontant() : oldDetteCompense.getDette().getMontantModifie();
            montantDecompt = montantDecompt.add(new BigDecimal(mnt));
        }

        BigDecimal montant = montantDecompt.subtract(new BigDecimal(montantDette));
        // on test si on peut bien prendre de l'argent sur le solde disponible
        if (montant.signum() == -1) {
            String[] t = new String[1];
            t[0] = DetteComptatCompenseChecker.toString(detteComptatCompense);
            JadeThread.logError(SimpleDetteComptatCompense.class.getName(),
                    "pegasus.detteComptat.editable.montantDisponibleInsufisant.integrity", t);

        }
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * @param detteComptatCompense
     */
    private static void checkMandatory(SimpleDetteComptatCompense detteComptatCompense) {

    }

    private static boolean isEditable(String idVersionDroit) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DetteComptatCompenseException {
        // TODO CHECKER en attente de l'objet
        SimpleVersionDroit droit;
        try {
            droit = PegasusImplServiceLocator.getSimpleVersionDroitService().read(idVersionDroit);
        } catch (DroitException e) {
            throw new DetteComptatCompenseException("Unable to read the versionDroit(" + idVersionDroit
                    + ") for check integrity");
        }
        return IPCDroits.CS_CALCULE.equals(droit.getCsEtatDroit());

    }

    private static String toString(final SimpleDetteComptatCompense detteComptatCompense)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {

        OldPersistence<String> pers = new OldPersistence<String>() {
            @Override
            public String action() throws JadeApplicationException {
                return CABusinessServiceLocator.getSectionService().findDescription(
                        detteComptatCompense.getIdSectionDetteEnCompta())
                        + ": " + detteComptatCompense.getMontant();
            }
        };

        return pers.executeWithOutException();
    }

}
