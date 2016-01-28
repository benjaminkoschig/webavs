package ch.globaz.al.businessimpl.checker.model.droit;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Checker du mod�le <code>DroitComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.droit.DroitComplexModel
 */
public class DroitComplexModelChecker extends ALAbstractChecker {

    /**
     * V�rification de l'int�grit� m�tier des donn�es
     * 
     * @param droitComplexModel
     *            le mod�le � v�rifier
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(DroitComplexModel droitComplexModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        int nbMonths = JadeDateUtil.getNbMonthsBetween(droitComplexModel.getEnfantComplexModel()
                .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(), droitComplexModel.getDroitModel()
                .getFinDroitForcee());

        // si le droit est de type formation
        if (droitComplexModel.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_FORM) && (nbMonths > 301)) {

            JadeThread.logError(DroitComplexModelChecker.class.getName(),
                    "al.droit.enfantModel.dateNaissance.businessIntegrity.older");

        }
        // if (droitComplexModel.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_FORM)
        // && (JadeDateUtil.getNbYearsBetween(droitComplexModel.getEnfantComplexModel()
        // .getPersonneEtendueComplexModel().getPersonne().getDateNaissance(), droitComplexModel
        // .getDroitModel().getFinDroitForcee(), JadeDateUtil.FULL_DATE_COMPARISON) > 25)) {
        //
        // JadeThread.logError(DroitComplexModelChecker.class.getName(),
        // "al.droit.enfantModel.dateNaissance.businessIntegrity.older");
        //
        // }

        // Si le droit est de type formation, on ne peut pas changer les donn�es
        // naissance
        if (droitComplexModel.getDroitModel().getTypeDroit().equals(ALCSDroit.TYPE_FORM)
                && !droitComplexModel.getEnfantComplexModel().getEnfantModel().isNew()) {
            EnfantModel enfantDB = new EnfantModel();
            enfantDB = ALImplServiceLocator.getEnfantModelService().read(
                    droitComplexModel.getEnfantComplexModel().getId());

            boolean isDiffNaissanceVersee = enfantDB.getAllocationNaissanceVersee().booleanValue() != droitComplexModel
                    .getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee().booleanValue() ? true
                    : false;

            boolean isDiffMontantNaissance = (!enfantDB.getMontantAllocationNaissanceFixe().equals(
                    droitComplexModel.getEnfantComplexModel().getEnfantModel().getMontantAllocationNaissanceFixe())) ? true
                    : false;

            boolean isDiffTypeNaissance = (!enfantDB.getTypeAllocationNaissance().equals(
                    droitComplexModel.getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance())) ? true
                    : false;

            if (isDiffMontantNaissance || isDiffNaissanceVersee || isDiffTypeNaissance) {
                JadeThread.logError(EnfantModelChecker.class.getName(),
                        "al.droit.enfantModel.allocationNaissance.businessIntegrity.readOnlyForm");

            }

        }

        // une date d'attestation ne peut �tre saisie que sur un droit FORM ou un enfant incapable d'exercer
        if (!JadeNumericUtil.isEmptyOrZero(droitComplexModel.getDroitModel().getDateAttestationEtude())
                && !(ALCSDroit.TYPE_FORM.equals(droitComplexModel.getDroitModel().getTypeDroit()) || !droitComplexModel
                        .getEnfantComplexModel().getEnfantModel().getCapableExercer())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.dateAttestation.businessIntegrity.notTypeForm");
        }

    }

    /**
     * Validation des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(DroitComplexModel model) throws JadeApplicationException, JadePersistenceException {
        DroitComplexModelChecker.checkBusinessIntegrity(model);
    }
}
